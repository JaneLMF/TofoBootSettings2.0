/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\work\\src\\tests\\TXBootSettings\\src\\com\\go3c\\lighttpd\\IHttpService.aidl
 */
package com.go3c.lighttpd;
public interface IHttpService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.go3c.lighttpd.IHttpService
{
private static final java.lang.String DESCRIPTOR = "com.go3c.lighttpd.IHttpService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.go3c.lighttpd.IHttpService interface,
 * generating a proxy if needed.
 */
public static com.go3c.lighttpd.IHttpService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.go3c.lighttpd.IHttpService))) {
return ((com.go3c.lighttpd.IHttpService)iin);
}
return new com.go3c.lighttpd.IHttpService.Stub.Proxy(obj);
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
case TRANSACTION_setConfigPath:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setConfigPath(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_startServer:
{
data.enforceInterface(DESCRIPTOR);
this.startServer();
reply.writeNoException();
return true;
}
case TRANSACTION_stopServer:
{
data.enforceInterface(DESCRIPTOR);
this.stopServer();
reply.writeNoException();
return true;
}
case TRANSACTION_setEnvValue:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.setEnvValue(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.go3c.lighttpd.IHttpService
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
@Override public void setConfigPath(java.lang.String path) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
mRemote.transact(Stub.TRANSACTION_setConfigPath, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startServer() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startServer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopServer() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopServer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setEnvValue(java.lang.String name, java.lang.String value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
_data.writeString(value);
mRemote.transact(Stub.TRANSACTION_setEnvValue, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_setConfigPath = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_startServer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_stopServer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_setEnvValue = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void setConfigPath(java.lang.String path) throws android.os.RemoteException;
public void startServer() throws android.os.RemoteException;
public void stopServer() throws android.os.RemoteException;
public void setEnvValue(java.lang.String name, java.lang.String value) throws android.os.RemoteException;
}
