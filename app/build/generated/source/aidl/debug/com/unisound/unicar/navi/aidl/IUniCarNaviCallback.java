/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\work\\UI11\\app\\src\\main\\aidl\\com\\unisound\\unicar\\navi\\aidl\\IUniCarNaviCallback.aidl
 */
package com.unisound.unicar.navi.aidl;
public interface IUniCarNaviCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.unisound.unicar.navi.aidl.IUniCarNaviCallback
{
private static final java.lang.String DESCRIPTOR = "com.unisound.unicar.navi.aidl.IUniCarNaviCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.unisound.unicar.navi.aidl.IUniCarNaviCallback interface,
 * generating a proxy if needed.
 */
public static com.unisound.unicar.navi.aidl.IUniCarNaviCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.unisound.unicar.navi.aidl.IUniCarNaviCallback))) {
return ((com.unisound.unicar.navi.aidl.IUniCarNaviCallback)iin);
}
return new com.unisound.unicar.navi.aidl.IUniCarNaviCallback.Stub.Proxy(obj);
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
case TRANSACTION_onCallBack:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onCallBack(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.unisound.unicar.navi.aidl.IUniCarNaviCallback
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
@Override public void onCallBack(java.lang.String callBackJson) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callBackJson);
mRemote.transact(Stub.TRANSACTION_onCallBack, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onCallBack(java.lang.String callBackJson) throws android.os.RemoteException;
}
