package com.txbox.settings.utils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.txbox.txsdk.R;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
public class ClsUtils {


		/**
		 * ���豸��� �ο�Դ�룺platform/packages/apps/Settings.git
		 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
		 */
		static public boolean createBond(Class btClass, BluetoothDevice btDevice)
				throws Exception
		{
			Method createBondMethod = btClass.getMethod("createBond");
			Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
			return returnValue.booleanValue();
		}

		/**
		 * ���豸������ �ο�Դ�룺platform/packages/apps/Settings.git
		 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
		 */
		static public boolean removeBond(Class btClass, BluetoothDevice btDevice)
				throws Exception
		{
			Method removeBondMethod = btClass.getMethod("removeBond");
			Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
			return returnValue.booleanValue();
		}

		static public boolean setPin(Class btClass, BluetoothDevice btDevice,
				String str) throws Exception
		{
			try
			{
				Method removeBondMethod = btClass.getDeclaredMethod("setPin",
						new Class[]
						{byte[].class});
				Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
						new Object[]
						{str.getBytes()});
				Log.e("returnValue", "" + returnValue);
			}
			catch (SecurityException e)
			{
				// throw new RuntimeException(e.getMessage());
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				// throw new RuntimeException(e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		}

		// ȡ���û�����
		static public boolean cancelPairingUserInput(Class btClass,
				BluetoothDevice device)

		throws Exception
		{
			Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
			// cancelBondProcess()
			Boolean returnValue = (Boolean) createBondMethod.invoke(device);
			return returnValue.booleanValue();
		}

		// ȡ�����
		static public boolean cancelBondProcess(Class btClass,
				BluetoothDevice device)

		throws Exception
		{
			Method createBondMethod = btClass.getMethod("cancelBondProcess");
			Boolean returnValue = (Boolean) createBondMethod.invoke(device);
			return returnValue.booleanValue();
		}

		
		static void setDiscoveryTime(int seconds)
		{
			      //
		}
		/**
		 * 
		 * @param clsShow
		 */
		static public void printAllInform(Class clsShow)
		{
			try
			{
				// ȡ�����з���
				Method[] hideMethod = clsShow.getMethods();
				int i = 0;
				for (; i < hideMethod.length; i++)
				{
					Log.e("method name", hideMethod[i].getName() + ";and the i is:"
							+ i);
				}
				// ȡ�����г���
				Field[] allFields = clsShow.getFields();
				for (i = 0; i < allFields.length; i++)
				{
					Log.e("Field name", allFields[i].getName());
				}
			}
			catch (SecurityException e)
			{
				// throw new RuntimeException(e.getMessage());
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				// throw new RuntimeException(e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	    final static int BLUETHOOTH_DEV_UNKNOW = 0;
	    final static int BLUETOOTH_DEV_TYPE_PHONE = 1;  // mobile phone
	    public final static int BLUETOOTH_DEV_HEADPHONE_A2DP =2; // 
	    public final static int BLUETOOTH_DEV_HEADSET_HFP =3;   // headset 
	    public final static int BLUETOOTH_DEV_KEYBOAD= 4;      // keyboard
	    public final static int BLUETOOTH_DEV_POINTING_HID = 6;   // mouse
	    final static int BLUETOOTH_DEV_IMAGING = 7;      
	    public final static int BLUETOOTH_DEV_MISC_HID = 8;
	    final static int BLUETOOTH_DEV_COMPUTER= 9;    // PC
	    final static int BLUETHOOTH_DEV_NETWORK_PAN = 10;		

	    public static int getDeviceType(BluetoothDevice device) {
	        BluetoothClass btClass = device.getBluetoothClass();
	        if (btClass != null) {
	            switch (btClass.getMajorDeviceClass()) {
	                case BluetoothClass.Device.Major.COMPUTER:
	                       return BLUETOOTH_DEV_COMPUTER;
	                       
	                case BluetoothClass.Device.Major.PHONE:
	                       return BLUETOOTH_DEV_TYPE_PHONE;
	                case BluetoothClass.Device.Major.PERIPHERAL:
	                {
	                      switch (btClass.getDeviceClass()) {
	                            case BluetoothClass.Device.PERIPHERAL_KEYBOARD:
	                            case BluetoothClass.Device.PERIPHERAL_KEYBOARD_POINTING:
	                                    return BLUETOOTH_DEV_KEYBOAD;
	                            case BluetoothClass.Device.PERIPHERAL_POINTING:
	                                    return BLUETOOTH_DEV_POINTING_HID;
	                            default:
	                                     return  BLUETOOTH_DEV_POINTING_HID;
	                     }
	                }
	                case BluetoothClass.Device.Major.IMAGING:
	                       return BLUETOOTH_DEV_IMAGING;

	                default:
	                    // unrecognized device class; continue
	            }
	        } else {
	            System.out.println( "mBtClass is null");
	        }

	        if (btClass != null) {
	            if (btClass.doesClassMatch(BluetoothClass.PROFILE_A2DP)) {
	                return BLUETOOTH_DEV_HEADPHONE_A2DP;
	            }
	            if (btClass.doesClassMatch(BluetoothClass.PROFILE_HEADSET)) {
	                return BLUETOOTH_DEV_HEADSET_HFP;
	            }
	            if(btClass.doesClassMatch(BluetoothClass.PROFILE_NAP)){
	            	return BLUETHOOTH_DEV_NETWORK_PAN;
	            }
	        }
	        return BLUETHOOTH_DEV_UNKNOW;
	    }
		
	    public static int resIDFromType(int type)
	    {
	    	switch(type){
	    		case  BLUETOOTH_DEV_TYPE_PHONE:  // mobile phone
	    		        return R.drawable.ic_bt_cellphone;
	    	    case  BLUETOOTH_DEV_HEADPHONE_A2DP:   // 
	    		        return R.drawable.ic_bt_headphones_a2dp;
	    	    case  BLUETOOTH_DEV_HEADSET_HFP:   // headset 
	    		        return R.drawable.ic_bt_headset_hfp;
	    	    case  BLUETOOTH_DEV_KEYBOAD:     // keyboard
	    		        return R.drawable.ic_bt_keyboard_hid;
	    	    case BLUETOOTH_DEV_POINTING_HID:
	    		        return R.drawable.ic_bt_pointing_hid;
	    	    case  BLUETOOTH_DEV_IMAGING:
	    		        return R.drawable.ic_bt_imaging;
	    	    case BLUETOOTH_DEV_MISC_HID:
	    		        return R.drawable.ic_bt_misc_hid;
	    	    case BLUETOOTH_DEV_COMPUTER:
	    		        return R.drawable.ic_bt_laptop;
	    	    case 	 BLUETHOOTH_DEV_NETWORK_PAN:
	    		        return R.drawable.ic_bt_network_pan;
	    	default:
	    		        return R.drawable.ic_launcher;
	    	}
	    }
	    

		
}
