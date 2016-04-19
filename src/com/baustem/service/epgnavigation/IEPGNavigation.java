/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\work\\src\\tests\\TofuBootSettings\\src\\com\\baustem\\service\\epgnavigation\\IEPGNavigation.aidl
 */
package com.baustem.service.epgnavigation;
public interface IEPGNavigation extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.baustem.service.epgnavigation.IEPGNavigation
{
private static final java.lang.String DESCRIPTOR = "com.baustem.service.epgnavigation.IEPGNavigation";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.baustem.service.epgnavigation.IEPGNavigation interface,
 * generating a proxy if needed.
 */
public static com.baustem.service.epgnavigation.IEPGNavigation asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.baustem.service.epgnavigation.IEPGNavigation))) {
return ((com.baustem.service.epgnavigation.IEPGNavigation)iin);
}
return new com.baustem.service.epgnavigation.IEPGNavigation.Stub.Proxy(obj);
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
case TRANSACTION_browseEPG:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
java.lang.String _result = this.browseEPG(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getEPGList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
java.lang.String _result = this.getEPGList(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_browsePF:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.browsePF(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getPF:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getPF(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getEvent:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getEvent(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.baustem.service.epgnavigation.IEPGNavigation
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
	 * 		浏览EPG目录下的内容
	 * 
	 * 参数说明：
	 * 		parentID 	- 父目录ID（""表示根目录）
	 * 		startIndex	- 获取项目的起始位置
	 * 		maxCount	- 每次最大获取项目数
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          EPGContainer*
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String browseEPG(java.lang.String parentID, int startIndex, int maxCount) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(parentID);
_data.writeInt(startIndex);
_data.writeInt(maxCount);
mRemote.transact(Stub.TRANSACTION_browseEPG, _data, _reply, 0);
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
	 * 		获取EPG列表
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号,如果为空则返回所有频道的EPG
	 * 		startTime	- 事件开始时间,如果为空则表示任何时间都可
	 * 		endTime  	- 事件结束时间,如果为空则表示任何时间都可
	 * 		startIndex	- 每次获取的起始位置
	 * 		maxCount	- 每次获取的最大数
	 * 
	 * 返回结果：
	 * 		返回EPGEvent的列表
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getEPGList(java.lang.String channelNr, java.lang.String startTime, java.lang.String endTime, int startIndex, int maxCount) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(channelNr);
_data.writeString(startTime);
_data.writeString(endTime);
_data.writeInt(startIndex);
_data.writeInt(maxCount);
mRemote.transact(Stub.TRANSACTION_getEPGList, _data, _reply, 0);
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
	 * 		浏览PF目录下的内容
	 * 
	 * 参数说明：
	 * 		parentID 	- 父目录ID（""表示根目录）
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          EPGContainer*
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String browsePF(java.lang.String parentID) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(parentID);
mRemote.transact(Stub.TRANSACTION_browsePF, _data, _reply, 0);
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
	 * 		获取指定频道的PF信息
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号
	 * 
	 * 返回结果：
	 * 		返回两条EPGEvent,当前Event和下一个Event
	 *		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getPF(java.lang.String channelNr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(channelNr);
mRemote.transact(Stub.TRANSACTION_getPF, _data, _reply, 0);
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
	 * 		获取指定的Event信息
	 * 
	 * 参数说明：
	 * 		eventid 	- 
	 * 
	 * 返回结果：
	 * 		返回EPGEvent
	 *		<Response code=”” message=””>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getEvent(java.lang.String eventid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(eventid);
mRemote.transact(Stub.TRANSACTION_getEvent, _data, _reply, 0);
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
static final int TRANSACTION_browseEPG = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getEPGList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_browsePF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getPF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
/*
	 * 功能描述：
	 * 		浏览EPG目录下的内容
	 * 
	 * 参数说明：
	 * 		parentID 	- 父目录ID（""表示根目录）
	 * 		startIndex	- 获取项目的起始位置
	 * 		maxCount	- 每次最大获取项目数
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          EPGContainer*
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String browseEPG(java.lang.String parentID, int startIndex, int maxCount) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获取EPG列表
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号,如果为空则返回所有频道的EPG
	 * 		startTime	- 事件开始时间,如果为空则表示任何时间都可
	 * 		endTime  	- 事件结束时间,如果为空则表示任何时间都可
	 * 		startIndex	- 每次获取的起始位置
	 * 		maxCount	- 每次获取的最大数
	 * 
	 * 返回结果：
	 * 		返回EPGEvent的列表
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getEPGList(java.lang.String channelNr, java.lang.String startTime, java.lang.String endTime, int startIndex, int maxCount) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		浏览PF目录下的内容
	 * 
	 * 参数说明：
	 * 		parentID 	- 父目录ID（""表示根目录）
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          EPGContainer*
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String browsePF(java.lang.String parentID) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获取指定频道的PF信息
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号
	 * 
	 * 返回结果：
	 * 		返回两条EPGEvent,当前Event和下一个Event
	 *		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getPF(java.lang.String channelNr) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获取指定的Event信息
	 * 
	 * 参数说明：
	 * 		eventid 	- 
	 * 
	 * 返回结果：
	 * 		返回EPGEvent
	 *		<Response code=”” message=””>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getEvent(java.lang.String eventid) throws android.os.RemoteException;
}
