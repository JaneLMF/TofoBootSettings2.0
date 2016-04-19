/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\work\\src\\tests\\TofuBootSettings\\src\\com\\baustem\\service\\dvbnavigation\\IDVBNavigation.aidl
 */
package com.baustem.service.dvbnavigation;
public interface IDVBNavigation extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.baustem.service.dvbnavigation.IDVBNavigation
{
private static final java.lang.String DESCRIPTOR = "com.baustem.service.dvbnavigation.IDVBNavigation";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.baustem.service.dvbnavigation.IDVBNavigation interface,
 * generating a proxy if needed.
 */
public static com.baustem.service.dvbnavigation.IDVBNavigation asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.baustem.service.dvbnavigation.IDVBNavigation))) {
return ((com.baustem.service.dvbnavigation.IDVBNavigation)iin);
}
return new com.baustem.service.dvbnavigation.IDVBNavigation.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getChannelList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getChannelList(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getChannel:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getChannel(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.baustem.service.dvbnavigation.IDVBNavigation
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/*
	 * 功能描述：
	 * 		按照给定的目录ID获取该目录下的子目录或频道
	 * 
	 * 参数说明：
	 * 		parentID 	- DVB目录ID,如果parentID==“”那么表示从根目录开始获取
	 * 
	 * 返回结果：
	 * 		<Response  code=””  message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          DVBContainer*
	 * 		     < /Containers >
	 * 		     <Items>
	 * 		          Channel*
	 * 		     < /Items>
	 * 		</ Response >
	 * */
@Override public java.lang.String getChannelList(java.lang.String parentID) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(parentID);
mRemote.transact(Stub.TRANSACTION_getChannelList, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/*
	 * 功能描述：
	 * 		按照给定的内容ID获取该内容信息
	 * 
	 * 参数说明：
	 * 		contentid 	- DVB内容ID
	 * 
	 * 返回结果：
	 * 		<Content code=”” message=””>
	 * 		     <Containers>
	 * 		          DVBContainer？
	 * 		     < /Containers >
	 * 		     <Items>
	 * 		          Channel？
	 * 		     < /Items>
	 * 		</Content>
	 * */
@Override public java.lang.String getChannel(java.lang.String contentid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(contentid);
mRemote.transact(Stub.TRANSACTION_getChannel, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getChannelList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
/*
	 * 功能描述：
	 * 		按照给定的目录ID获取该目录下的子目录或频道
	 * 
	 * 参数说明：
	 * 		parentID 	- DVB目录ID,如果parentID==“”那么表示从根目录开始获取
	 * 
	 * 返回结果：
	 * 		<Response  code=””  message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          DVBContainer*
	 * 		     < /Containers >
	 * 		     <Items>
	 * 		          Channel*
	 * 		     < /Items>
	 * 		</ Response >
	 * */
public java.lang.String getChannelList(java.lang.String parentID) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		按照给定的内容ID获取该内容信息
	 * 
	 * 参数说明：
	 * 		contentid 	- DVB内容ID
	 * 
	 * 返回结果：
	 * 		<Content code=”” message=””>
	 * 		     <Containers>
	 * 		          DVBContainer？
	 * 		     < /Containers >
	 * 		     <Items>
	 * 		          Channel？
	 * 		     < /Items>
	 * 		</Content>
	 * */
public java.lang.String getChannel(java.lang.String contentid) throws android.os.RemoteException;
}
