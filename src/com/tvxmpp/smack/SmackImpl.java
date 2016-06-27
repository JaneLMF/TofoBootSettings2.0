package com.tvxmpp.smack;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.carbons.Carbon;
import org.jivesoftware.smackx.carbons.CarbonManager;
import org.jivesoftware.smackx.forward.Forwarded;
import org.jivesoftware.smackx.packet.DelayInfo;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.ping.provider.PingProvider;
import org.jivesoftware.smackx.provider.DelayInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tvxmpp.WindowService;
import com.tvxmpp.exception.XXException;
import com.tvxmpp.model.IXMPPDataPaser;
import com.tvxmpp.model.XMPPData;
import com.tvxmpp.model.XMPPDataPaserImpl;
import com.tvxmpp.ui.DialogShowView;
import com.tvxmpp.util.L;
import com.tvxmpp.util.MessageUtil;
import com.tvxmpp.util.PreferenceConstants;


public class SmackImpl implements Smack {
	// �ͻ������ƺ����͡���Ҫ����������Ǽ�.
	public static final String XMPP_IDENTITY_NAME = "XMPP";// �ͻ�������
	public static final String XMPP_IDENTITY_TYPE = "tv";// �ͻ�������
	public static final String XMPP_IDENTITY_RESOURCE = "pivos";// �ͻ���Resource
	private static final int PACKET_TIMEOUT = 30000;// ��ʱʱ��
	
	
	private final int SERVERPORT = 5222;
	private final String SERVERHOST = "192.168.1.202";
	public static final String SERVICE = "message.localserver";

	private Context mContext = null;
	static {
		registerSmackProviders();
	}

	// ��һЩ����������
	static void registerSmackProviders() {
		ProviderManager pm = ProviderManager.getInstance();
		// add IQ handling
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// add delayed delivery notifications
		pm.addExtensionProvider("delay", "urn:xmpp:delay",
				new DelayInfoProvider());
		pm.addExtensionProvider("x", "jabber:x:delay", new DelayInfoProvider());
		// add carbons and forwarding
		pm.addExtensionProvider("forwarded", Forwarded.NAMESPACE,
				new Forwarded.Provider());
		pm.addExtensionProvider("sent", Carbon.NAMESPACE, new Carbon.Provider());
		pm.addExtensionProvider("received", Carbon.NAMESPACE,
				new Carbon.Provider());
		// add delivery receipts
		pm.addExtensionProvider(DeliveryReceipt.ELEMENT,
				DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
		pm.addExtensionProvider(DeliveryReceiptRequest.ELEMENT,
				DeliveryReceipt.NAMESPACE,
				new DeliveryReceiptRequest.Provider());
		// add XMPP Ping (XEP-0199)
		pm.addIQProvider("ping", "urn:xmpp:ping", new PingProvider());

		ServiceDiscoveryManager.setIdentityName(XMPP_IDENTITY_NAME);
		ServiceDiscoveryManager.setIdentityType(XMPP_IDENTITY_TYPE);
	}

	private ConnectionConfiguration mXMPPConfig;// ��������
	private XMPPConnection mXMPPConnection;// ���Ӷ���
	private Roster mRoster;// ��ϵ�˶���

	private PacketListener mPacketListener;// ��Ϣ��̬����
	private PacketListener mPongListener;// ping pong��������̬����

	// ping-pong������
/*	private String mPingID;// ping��������id
	private long mPingTimestamp;// ʱ���
	private PendingIntent mPingAlarmPendIntent;// ��ͨ������������ping��������ʱ����
	private PendingIntent mPongTimeoutAlarmPendIntent;// �жϷ��������ӳ�ʱ������
	private static final String PING_ALARM = "com.way.xx.PING_ALARM";// ping����������BroadcastReceiver��Action
	private static final String PONG_TIMEOUT_ALARM = "com.way.xx.PONG_TIMEOUT_ALARM";// �ж����ӳ�ʱ������BroadcastReceiver��Action
	private Intent mPingAlarmIntent = new Intent(PING_ALARM);
	private Intent mPongTimeoutAlarmIntent = new Intent(PONG_TIMEOUT_ALARM);
	private PongTimeoutAlarmReceiver mPongTimeoutAlarmReceiver = new PongTimeoutAlarmReceiver();
	private BroadcastReceiver mPingAlarmReceiver = new PingAlarmReceiver();*/

	// ping ������

	public SmackImpl(Context ctx) {
		
		L.d("SmackImpl  Constructor");
		mContext = ctx;
		
		this.mXMPPConfig = new ConnectionConfiguration(SERVERHOST, SERVERPORT,
				SERVICE);
		this.mXMPPConfig.setReconnectionAllowed(false);
		this.mXMPPConfig.setSendPresence(false);
		this.mXMPPConfig.setCompressionEnabled(false); // disable for now
		this.mXMPPConfig.setDebuggerEnabled(true);
		this.mXMPPConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		this.mXMPPConfig.setSASLAuthenticationEnabled( false);

		this.mXMPPConnection = new XMPPConnection(mXMPPConfig);
	}

	@Override
	public boolean login(String account, String password) throws XXException {// ��½ʵ��
		
		L.d("SmackImpl login start");
		try {
			if (mXMPPConnection.isConnected()) {// �����ж��Ƿ������ŷ���������Ҫ�ȶϿ�
				try {
					mXMPPConnection.disconnect();
				} catch (Exception e) {
					L.d("conn.disconnect() failed: " + e);
				}
			}
			
			SmackConfiguration.setPacketReplyTimeout(PACKET_TIMEOUT);// ���ó�ʱʱ��

			mXMPPConnection.connect();
			if (!mXMPPConnection.isConnected()) {
				throw new XXException("SMACK connect failed without exception!");
			}
			mXMPPConnection.addConnectionListener(new ConnectionListener() {
				public void connectionClosedOnError(Exception e) {
					//mService.postConnectionFailed(e.getMessage());// ���ӹر�ʱ����̬����������
				}

				public void connectionClosed() {
				}

				public void reconnectingIn(int seconds) {
				}

				public void reconnectionFailed(Exception e) {
				}

				public void reconnectionSuccessful() {
				}
			});
			
			initServiceDiscovery();// �������������Ϣ����,������Ϣ��Ҫ��ִ���ж��Ƿ��ͳɹ�
			
			// SMACK auto-logins if we were authenticated before
			if (!mXMPPConnection.isAuthenticated()) {
				
				mXMPPConnection.login(account, password, XMPP_IDENTITY_RESOURCE);
			}
			setStatusFromConfig();// ����״̬

		} catch (XMPPException e) {
			L.e(SmackImpl.class, "login() XMPPException: " + Log.getStackTraceString(e));
			
			throw new XXException(e.getLocalizedMessage(),
					e.getWrappedThrowable());
		} catch (Exception e) {
			// actually we just care for IllegalState or NullPointer or XMPPEx.
			L.e(SmackImpl.class, "login(): " + Log.getStackTraceString(e));
			throw new XXException(e.getLocalizedMessage(), e.getCause());
		}
		registerAllListener();// ע������������¼�����������Ϣ
		return mXMPPConnection.isAuthenticated();
	}

	/**
	 * ע�����еļ���
	 */
	private void registerAllListener() {
		// actually, authenticated must be true now, or an exception must have
		// been thrown.
		if (isAuthenticated()) {
			registerMessageListener();// ע������Ϣ����
			//registerPongListener();// ע���������Ӧping��Ϣ����
		}
	}

	/************ start ����Ϣ���� ********************/
	private void registerMessageListener() {
		// do not register multiple packet listeners
		if (mPacketListener != null)
			mXMPPConnection.removePacketListener(mPacketListener);

		PacketTypeFilter filter = new PacketTypeFilter(Message.class);

		mPacketListener = new PacketListener() {

			@Override
			public void processPacket(Packet packet) {
				// TODO Auto-generated method stub
				try {
					if (packet instanceof Message) {// �������Ϣ����
						Message msg = (Message) packet;
						String chatMessage = msg.getBody();
						L.d("chatMessage: " + chatMessage);
						
						IXMPPDataPaser xmpdatapaser = new XMPPDataPaserImpl();
				        try {
				        	XMPPData data = xmpdatapaser.parse(chatMessage);

				        	checkAndShowMessage(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// try to extract a carbon
						/*Carbon cc = CarbonManager.getCarbon(msg);
						if (cc != null
								&& cc.getDirection() == Carbon.Direction.received) {// �յ�����Ϣ
							L.d("carbon: " + cc.toXML());
							msg = (Message) cc.getForwarded()
									.getForwardedPacket();
							chatMessage = msg.getBody();
							// fall through
						} else if (cc != null
								&& cc.getDirection() == Carbon.Direction.sent) {// ������Լ����͵���Ϣ������ӵ����ݿ��ֱ�ӷ���
							L.d("carbon: " + cc.toXML());
							msg = (Message) cc.getForwarded()
									.getForwardedPacket();
							chatMessage = msg.getBody();
							if (chatMessage == null)
								return;
							String fromJID = getJabberID(msg.getTo());

							// always return after adding
							return;// �ǵ�Ҫ����
						}

						if (chatMessage == null) {
							return;// �����ϢΪ�գ�ֱ�ӷ�����
						}

						if (msg.getType() == Message.Type.error) {
							chatMessage = "<Error> " + chatMessage;// �������Ϣ����
						}

						long ts;// ��Ϣʱ���
						DelayInfo timestamp = (DelayInfo) msg.getExtension(
								"delay", "urn:xmpp:delay");
						if (timestamp == null)
							timestamp = (DelayInfo) msg.getExtension("x",
									"jabber:x:delay");
						if (timestamp != null)
							ts = timestamp.getStamp().getTime();
						else
							ts = System.currentTimeMillis();

						String fromJID = getJabberID(msg.getFrom());// ��Ϣ���Զ���
*/					}
				} catch (Exception e) {
					// SMACK silently discards exceptions dropped from
					// processPacket :(
					L.e("failed to process packet:");
					e.printStackTrace();
				}
			}
			
		};

		mXMPPConnection.addPacketListener(mPacketListener, filter);// ���PacketListener
	}

	private void checkAndShowMessage(XMPPData data) {
		// register connection features
		if (data == null)
			return;
		
		String strShowMessage = MessageUtil.getShowString(data.getSubtype());

		if (strShowMessage == null){
			return;
		}

		if (data.getLevel().equalsIgnoreCase(MessageUtil.MessageType_INFO)){
			showInfoMessage(strShowMessage);
			
		} else if(data.getLevel().equalsIgnoreCase(MessageUtil.MessageType_WARN)){
			showDialogMessage("����", strShowMessage);
			
		} else if(data.getLevel().equalsIgnoreCase(MessageUtil.MessageType_DEBUG)){
			showDialogMessage("����", strShowMessage);
			
		} else if(data.getLevel().equalsIgnoreCase(MessageUtil.MessageType_ERROR)){
			showDialogMessage("����", strShowMessage);
			
		} else {
			showInfoMessage(strShowMessage);
		}
	}

	private void showInfoMessage(String strMessage) {

		Intent i = new Intent(mContext, WindowService.class);
		i.putExtra("showstr", strMessage);
		mContext.startService(i);
	}
	
	private void showDialogMessage(String strTitle, String strMessage) {
		Intent i = new Intent(mContext, DialogShowView.class);
		i.putExtra("showstr", strMessage);
		i.putExtra("titlestr", strTitle);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}
	
	
	private String getJabberID(String from) {
		String[] res = from.split("/");
		return res[0].toLowerCase();
	}


	/**
	 * ��ȡ��ϵ������
	 * 
	 * @param rosterEntry
	 * @return
	 */
	private String getName(RosterEntry rosterEntry) {
		String name = rosterEntry.getName();
		if (name != null && name.length() > 0) {
			return name;
		}
		name = StringUtils.parseName(rosterEntry.getUser());
		if (name.length() > 0) {
			return name;
		}
		return rosterEntry.getUser();
	}


	/**
	 * �������������Ϣ����,������Ϣ��Ҫ��ִ���ж϶Է��Ƿ��Ѷ�����Ϣ
	 */
	private void initServiceDiscovery() {
		// register connection features
		ServiceDiscoveryManager sdm = ServiceDiscoveryManager
				.getInstanceFor(mXMPPConnection);
		if (sdm == null)
			sdm = new ServiceDiscoveryManager(mXMPPConnection);

		sdm.addFeature("http://jabber.org/protocol/disco#info");

		// reference PingManager, set ping flood protection to 10s
		PingManager.getInstanceFor(mXMPPConnection).setPingMinimumInterval(
				10 * 1000);
		// reference DeliveryReceiptManager, add listener

		DeliveryReceiptManager dm = DeliveryReceiptManager
				.getInstanceFor(mXMPPConnection);
		dm.enableAutoReceipts();
		dm.registerReceiptReceivedListener(new DeliveryReceiptManager.ReceiptReceivedListener() {
			public void onReceiptReceived(String fromJid, String toJid,
					String receiptId) {
				L.d(SmackImpl.class, "got delivery receipt for " + receiptId);
			}
		});
	}

	@Override
	public void setStatusFromConfig() {// �����Լ��ĵ�ǰ״̬�����ⲿ�������
		
		CarbonManager.getInstanceFor(mXMPPConnection).sendCarbonsEnabled(
				true);
		
		Presence presence = new Presence(Presence.Type.available);
		Mode mode = Mode.valueOf(PreferenceConstants.AVAILABLE);
		presence.setMode(mode);
		presence.setStatus("����");
		presence.setPriority(0);
		mXMPPConnection.sendPacket(presence);
		
		
	}

	@Override
	public boolean isAuthenticated() {// �Ƿ�������������ϣ���������ⲿ�������
		if (mXMPPConnection != null) {
			return (mXMPPConnection.isConnected() && mXMPPConnection
					.isAuthenticated());
		}
		return false;
	}

	@Override
	public void sendMessage(String toJID, String message) {// ������Ϣ
		// TODO Auto-generated method stub
		final Message newMessage = new Message(toJID, Message.Type.chat);
		newMessage.setBody(message);
		newMessage.addExtension(new DeliveryReceiptRequest());
	}


	@Override
	public String getNameForJID(String jid) {
		if (null != this.mRoster.getEntry(jid)
				&& null != this.mRoster.getEntry(jid).getName()
				&& this.mRoster.getEntry(jid).getName().length() > 0) {
			return this.mRoster.getEntry(jid).getName();
		} else {
			return jid;
		}
	}

	@Override
	public boolean logout() {// ע����¼
		L.d("unRegisterCallback()");
		// remove callbacks _before_ tossing old connection
		try {
			mXMPPConnection.removePacketListener(mPacketListener);
			mXMPPConnection.removePacketListener(mPongListener);

		} catch (Exception e) {
			// ignore it!
			return false;
		}
		if (mXMPPConnection.isConnected()) {
			// work around SMACK's #%&%# blocking disconnect()
			new Thread() {
				public void run() {
					L.d("shutDown thread started");
					mXMPPConnection.disconnect();
					L.d("shutDown thread finished");
				}
			}.start();
		}

		return true;
	}

	
	/***************** start ����ping��������Ϣ ***********************/
	private void registerPongListener() {
		// reset ping expectation on new connection
	}

	/**
	 * BroadcastReceiver to trigger reconnect on pong timeout.
	 */
	private class PongTimeoutAlarmReceiver extends BroadcastReceiver {
		public void onReceive(Context ctx, Intent i) {
			
		}
	}

	@Override
	public void sendServerPing() {
		
	}
	
	/**
	 * BroadcastReceiver to trigger sending pings to the server
	 */
	private class PingAlarmReceiver extends BroadcastReceiver {
		public void onReceive(Context ctx, Intent i) {
		}
	}

	/***************** end ����ping��������Ϣ ***********************/
	
	
	/**
	 * Test
	 */
	
	public void Test(){
		
		String chatMessage = "<event><type>CA</type><subtype>1002</subtype><SessionId></SessionId><level>Info</level><privateData></privateData></event>";
		String chatMessage1 = "<event><type>CA</type><subtype>1007</subtype><SessionId></SessionId><level>Warning</level><privateData></privateData></event>";
		String chatMessage2 = "<event><type>SM</type><subtype>4035</subtype><SessionId></SessionId><level>Error</level><privateData></privateData></event>";
		String chatMessage3 = "<event><type>VOD</type><subtype>3120</subtype><SessionId></SessionId><level>Debug</level><privateData></privateData></event>";
		L.d("chatMessage: " + chatMessage);
		
		IXMPPDataPaser xmpdatapaser = new XMPPDataPaserImpl();
        	
			try {
				
				XMPPData data;
				data = xmpdatapaser.parse(chatMessage2);
				checkAndShowMessage(data);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}
