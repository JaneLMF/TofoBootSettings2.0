/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\work\\src\\tests\\TofuBootSettings\\src\\com\\baustem\\service\\vodnavigation\\IVODNavigation.aidl
 */
package com.baustem.service.vodnavigation;
public interface IVODNavigation extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.baustem.service.vodnavigation.IVODNavigation
{
private static final java.lang.String DESCRIPTOR = "com.baustem.service.vodnavigation.IVODNavigation";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.baustem.service.vodnavigation.IVODNavigation interface,
 * generating a proxy if needed.
 */
public static com.baustem.service.vodnavigation.IVODNavigation asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.baustem.service.vodnavigation.IVODNavigation))) {
return ((com.baustem.service.vodnavigation.IVODNavigation)iin);
}
return new com.baustem.service.vodnavigation.IVODNavigation.Stub.Proxy(obj);
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
case TRANSACTION_searchContent:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
java.lang.String _result = this.searchContent(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getContentList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
java.lang.String _result = this.getContentList(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getTimeShift:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getTimeShift(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getContent:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _result = this.getContent(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_addContent:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
java.lang.String _result = this.addContent(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_delContent:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.delContent(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getTimeShiftPrograms:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getTimeShiftPrograms(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getTimeShiftByServiceId:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getTimeShiftByServiceId(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getTimeShiftProgramsByServiceId:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getTimeShiftProgramsByServiceId(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getRecommendList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getRecommendList(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.baustem.service.vodnavigation.IVODNavigation
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
	 * 		按条件搜索VOD节目，如按影片名、导演、主演
	 * 
	 * 参数说明：
	 * 		target 	- 要搜索的内容
	 * 		type   	- 0 ：影片名；1：导演；2：主演
	 * 		startIndex	- 获取项目的起始位置
	 * 		maxCount	- 每次最大获取项目数
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Items>
	 * 		          VODProgram*
	 * 		          SerialConatiner*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String searchContent(java.lang.String target, int type, int startIndex, int maxCount) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(target);
_data.writeInt(type);
_data.writeInt(startIndex);
_data.writeInt(maxCount);
mRemote.transact(Stub.TRANSACTION_searchContent, _data, _reply, 0);
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
	 * 		获得VOD内容列表
	 * 
	 * 参数说明：
	 * 		parentID 	- 父目录ID
	 * 		contentType	- 0:视频点播；1:电视回看；2:书签；3:收藏；4:时移电视
	 * 		startIndex	- 获取项目的起始位置
	 * 		maxCount	- 每次最大获取项目数
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          GeneralContainer*
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          VODProgram*
	 * 		          SerialConatiner*
	 * 		          TimeShiftProgram*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getContentList(java.lang.String parentID, int contentType, int startIndex, int maxCount) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(parentID);
_data.writeInt(contentType);
_data.writeInt(startIndex);
_data.writeInt(maxCount);
mRemote.transact(Stub.TRANSACTION_getContentList, _data, _reply, 0);
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
	 * 		获得指定逻辑频道号的时移频道
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				TimeShiftProgram?
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getTimeShift(java.lang.String channelNr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(channelNr);
mRemote.transact(Stub.TRANSACTION_getTimeShift, _data, _reply, 0);
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
	 * 		获得指定的VOD内容
	 * 
	 * 参数说明：
	 * 		contentid 	- VOD ID
	 * 		contentType	- 0:视频点播；1:电视回看；2:书签；3:收藏；4:时移电视
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Containers>
	 * 		          GeneralContainer?
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          VODProgram?|SerialConatiner?|TimeShiftProgram?
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getContent(java.lang.String contentid, int contentType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(contentid);
_data.writeInt(contentType);
mRemote.transact(Stub.TRANSACTION_getContent, _data, _reply, 0);
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
	 * 		添加内容
	 * 
	 * 参数说明：
	 * 		contentid	- 
	 * 		contentName	- 
	 * 		contentType	- 0：书签；1：收藏；2:其它
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
@Override public java.lang.String addContent(java.lang.String contentid, java.lang.String contentName, int contentType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(contentid);
_data.writeString(contentName);
_data.writeInt(contentType);
mRemote.transact(Stub.TRANSACTION_addContent, _data, _reply, 0);
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
	 * 		删除内容
	 * 
	 * 参数说明：
	 * 		contentid	- 
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
@Override public java.lang.String delContent(java.lang.String contentid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(contentid);
mRemote.transact(Stub.TRANSACTION_delContent, _data, _reply, 0);
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
	 * 		获得指定逻辑频道号的时移节目列表
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getTimeShiftPrograms(java.lang.String channelNr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(channelNr);
mRemote.transact(Stub.TRANSACTION_getTimeShiftPrograms, _data, _reply, 0);
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
	 * 		获得指定Service ID的时移频道
	 * 
	 * 参数说明：
	 * 		serviceId 	- 频道的serviceid,是serviceid,tsid,onid的组合
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				TimeShiftProgram?
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getTimeShiftByServiceId(java.lang.String serviceId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(serviceId);
mRemote.transact(Stub.TRANSACTION_getTimeShiftByServiceId, _data, _reply, 0);
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
	 * 		获得指定Service ID的时移节目列表
	 * 
	 * 参数说明：
	 * 		serviceId 	- 频道的serviceid,是serviceid,tsid,onid的组合
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getTimeShiftProgramsByServiceId(java.lang.String serviceId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(serviceId);
mRemote.transact(Stub.TRANSACTION_getTimeShiftProgramsByServiceId, _data, _reply, 0);
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
	 * 		获得推荐节目列表
	 * 
	 * 参数说明：
	 * 		id 	- VOD节目ID
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				VODProgram*
	 * 				SerialConatiner*
	 * 				TimeShiftProgram*
	 * 		     < /Items>
	 * 		</Response>
	 * */
@Override public java.lang.String getRecommendList(java.lang.String id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(id);
mRemote.transact(Stub.TRANSACTION_getRecommendList, _data, _reply, 0);
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
static final int TRANSACTION_searchContent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getContentList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getTimeShift = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getContent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_addContent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_delContent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getTimeShiftPrograms = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getTimeShiftByServiceId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getTimeShiftProgramsByServiceId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getRecommendList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
}
/*
	 * 功能描述：
	 * 		按条件搜索VOD节目，如按影片名、导演、主演
	 * 
	 * 参数说明：
	 * 		target 	- 要搜索的内容
	 * 		type   	- 0 ：影片名；1：导演；2：主演
	 * 		startIndex	- 获取项目的起始位置
	 * 		maxCount	- 每次最大获取项目数
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Items>
	 * 		          VODProgram*
	 * 		          SerialConatiner*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String searchContent(java.lang.String target, int type, int startIndex, int maxCount) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获得VOD内容列表
	 * 
	 * 参数说明：
	 * 		parentID 	- 父目录ID
	 * 		contentType	- 0:视频点播；1:电视回看；2:书签；3:收藏；4:时移电视
	 * 		startIndex	- 获取项目的起始位置
	 * 		maxCount	- 每次最大获取项目数
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          GeneralContainer*
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          VODProgram*
	 * 		          SerialConatiner*
	 * 		          TimeShiftProgram*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getContentList(java.lang.String parentID, int contentType, int startIndex, int maxCount) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获得指定逻辑频道号的时移频道
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				TimeShiftProgram?
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getTimeShift(java.lang.String channelNr) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获得指定的VOD内容
	 * 
	 * 参数说明：
	 * 		contentid 	- VOD ID
	 * 		contentType	- 0:视频点播；1:电视回看；2:书签；3:收藏；4:时移电视
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Containers>
	 * 		          GeneralContainer?
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          VODProgram?|SerialConatiner?|TimeShiftProgram?
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getContent(java.lang.String contentid, int contentType) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		添加内容
	 * 
	 * 参数说明：
	 * 		contentid	- 
	 * 		contentName	- 
	 * 		contentType	- 0：书签；1：收藏；2:其它
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
public java.lang.String addContent(java.lang.String contentid, java.lang.String contentName, int contentType) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		删除内容
	 * 
	 * 参数说明：
	 * 		contentid	- 
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
public java.lang.String delContent(java.lang.String contentid) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获得指定逻辑频道号的时移节目列表
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getTimeShiftPrograms(java.lang.String channelNr) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获得指定Service ID的时移频道
	 * 
	 * 参数说明：
	 * 		serviceId 	- 频道的serviceid,是serviceid,tsid,onid的组合
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				TimeShiftProgram?
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getTimeShiftByServiceId(java.lang.String serviceId) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获得指定Service ID的时移节目列表
	 * 
	 * 参数说明：
	 * 		serviceId 	- 频道的serviceid,是serviceid,tsid,onid的组合
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getTimeShiftProgramsByServiceId(java.lang.String serviceId) throws android.os.RemoteException;
/*
	 * 功能描述：
	 * 		获得推荐节目列表
	 * 
	 * 参数说明：
	 * 		id 	- VOD节目ID
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				VODProgram*
	 * 				SerialConatiner*
	 * 				TimeShiftProgram*
	 * 		     < /Items>
	 * 		</Response>
	 * */
public java.lang.String getRecommendList(java.lang.String id) throws android.os.RemoteException;
}
