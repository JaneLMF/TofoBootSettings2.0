/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\work\\src\\tests\\TofuBootSettings\\src\\com\\baustem\\service\\vodstream\\IVODStream.aidl
 */
package com.baustem.service.vodstream;
public interface IVODStream extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.baustem.service.vodstream.IVODStream
{
private static final java.lang.String DESCRIPTOR = "com.baustem.service.vodstream.IVODStream";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.baustem.service.vodstream.IVODStream interface,
 * generating a proxy if needed.
 */
public static com.baustem.service.vodstream.IVODStream asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.baustem.service.vodstream.IVODStream))) {
return ((com.baustem.service.vodstream.IVODStream)iin);
}
return new com.baustem.service.vodstream.IVODStream.Stub.Proxy(obj);
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
case TRANSACTION_getPlayInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getPlayInfo(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_play:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _result = this.play(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.stop(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_seek:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _result = this.seek(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_pause:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.pause(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getPlayStatus:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getPlayStatus(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getPlayProgress:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getPlayProgress(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.baustem.service.vodstream.IVODStream
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
	 * 		获得播放VOD节目的URL和播放实例ID
	 * 
	 * 参数说明：
	 * 		playURL 	- VOD影片中的playURL属性值
	 * 
	 * 返回结果：
	 * 		返回要播放的节目的实例句柄(INSTANCEID)和传输该节目内容的连接的URL(CONNECTION_URL).
	 * 		<Response code=”” message=””>
	 * 		   	<instanceid></ instanceid >
	 * 			<connection_url></connection_url>
	 * 		</Response>
	 * */
@Override public java.lang.String getPlayInfo(java.lang.String playURL) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(playURL);
mRemote.transact(Stub.TRANSACTION_getPlayInfo, _data, _reply, 0);
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
	 * 		播放VOD节目,可以指定倍速
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 		speed     	- 播放速度,0为正常播放，大于0为快进，小于0为快退
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
@Override public java.lang.String play(java.lang.String instanceID, int speed) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(instanceID);
_data.writeInt(speed);
mRemote.transact(Stub.TRANSACTION_play, _data, _reply, 0);
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
	 * 		停止正在播放的VOD节目
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
@Override public java.lang.String stop(java.lang.String instanceID) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(instanceID);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
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
	 * 		让正在播放的VOD节目跳到指定的时刻
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 		time    	- 格式：00:00:00
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
@Override public java.lang.String seek(java.lang.String instanceID, java.lang.String time) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(instanceID);
_data.writeString(time);
mRemote.transact(Stub.TRANSACTION_seek, _data, _reply, 0);
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
	 * 		让正在播放的VOD节目暂停
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
@Override public java.lang.String pause(java.lang.String instanceID) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(instanceID);
mRemote.transact(Stub.TRANSACTION_pause, _data, _reply, 0);
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
	 * 		获取正在播放的VOD节目的当前状态
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 *		<Response code=”” message=””>
	 *		   <status>0 播放; 1 停止; 2 暂停</status>
	 *		</Response>
	 * */
@Override public java.lang.String getPlayStatus(java.lang.String instanceID) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(instanceID);
mRemote.transact(Stub.TRANSACTION_getPlayStatus, _data, _reply, 0);
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
	 * 		获取正在播放的VOD节目的播放进度
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		   <progress>播放时间点，格式：00:00:00</ progress >
	 * 		</Response>
	 * */
@Override public java.lang.String getPlayProgress(java.lang.String instanceID) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(instanceID);
mRemote.transact(Stub.TRANSACTION_getPlayProgress, _data, _reply, 0);
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
static final int TRANSACTION_getPlayInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_play = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_seek = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_pause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getPlayStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getPlayProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
/*
	 * 功能描述：
	 * 		获得播放VOD节目的URL和播放实例ID
	 * 
	 * 参数说明：
	 * 		playURL 	- VOD影片中的playURL属性值
	 * 
	 * 返回结果：
	 * 		返回要播放的节目的实例句柄(INSTANCEID)和传输该节目内容的连接的URL(CONNECTION_URL).
	 * 		<Response code=”” message=””>
	 * 		   	<instanceid></ instanceid >
	 * 			<connection_url></connection_url>
	 * 		</Response>
	 * */
public java.lang.String getPlayInfo(java.lang.String playURL) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		播放VOD节目,可以指定倍速
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 		speed     	- 播放速度,0为正常播放，大于0为快进，小于0为快退
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
public java.lang.String play(java.lang.String instanceID, int speed) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		停止正在播放的VOD节目
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
public java.lang.String stop(java.lang.String instanceID) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		让正在播放的VOD节目跳到指定的时刻
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 		time    	- 格式：00:00:00
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
public java.lang.String seek(java.lang.String instanceID, java.lang.String time) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		让正在播放的VOD节目暂停
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
public java.lang.String pause(java.lang.String instanceID) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获取正在播放的VOD节目的当前状态
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 *		<Response code=”” message=””>
	 *		   <status>0 播放; 1 停止; 2 暂停</status>
	 *		</Response>
	 * */
public java.lang.String getPlayStatus(java.lang.String instanceID) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获取正在播放的VOD节目的播放进度
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		   <progress>播放时间点，格式：00:00:00</ progress >
	 * 		</Response>
	 * */
public java.lang.String getPlayProgress(java.lang.String instanceID) throws android.os.RemoteException;
}
