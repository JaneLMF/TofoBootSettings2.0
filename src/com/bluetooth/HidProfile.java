/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothInputDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;


import java.util.List;

/**
 * HidProfile handles Bluetooth HID profile.
 */
public  class HidProfile implements LocalBluetoothProfile {
    private static final String TAG = "HidProfile";
    private static boolean V = true;

    private BluetoothInputDevice mService;
    private boolean mIsProfileReady;

    static final String NAME = "HID";

    // Order of this profile in device profiles list
    private static final int ORDINAL = 3;

    // These callbacks run on the main thread.
    private final class InputDeviceServiceListener
            implements BluetoothProfile.ServiceListener {

        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (V) Log.d(TAG,"Bluetooth HidProfile  service connected");
            mService = (BluetoothInputDevice) proxy;
            mIsProfileReady=true;
        }

        public void onServiceDisconnected(int profile) {
            if (V) Log.d(TAG,"Bluetooth HidProfile service disconnected");
            mIsProfileReady=false;
        }
    }

    public boolean isProfileReady() {
        return mIsProfileReady;
    }

    public  HidProfile(Context context, LocalBluetoothAdapter adapter) {
        adapter.getProfileProxy(context, new InputDeviceServiceListener(),
                BluetoothProfile.INPUT_DEVICE);
    }

    public HidProfile(Context context,BluetoothAdapter adapter ) {
        // mProfileManager = profileManager;
         adapter.getProfileProxy(context, new InputDeviceServiceListener(),
                 BluetoothProfile.INPUT_DEVICE);
     }
    
    
    public boolean isConnectable() {
        return true;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public boolean connect(BluetoothDevice device) {
        if (mService == null) return false;
        return mService.connect(device);
    }

    public boolean disconnect(BluetoothDevice device) {
        if (mService == null) return false;
        return mService.disconnect(device);
    }

    public int getConnectionStatus(BluetoothDevice device) {
        if (mService == null) {
            return BluetoothProfile.STATE_DISCONNECTED;
        }
        List<BluetoothDevice> deviceList = mService.getConnectedDevices();

        return !deviceList.isEmpty() && deviceList.get(0).equals(device)
                ? mService.getConnectionState(device)
                : BluetoothProfile.STATE_DISCONNECTED;
    }

    public boolean isPreferred(BluetoothDevice device) {
        if (mService == null) return false;
        return mService.getPriority(device) > BluetoothProfile.PRIORITY_OFF;
    }

    public int getPreferred(BluetoothDevice device) {
        if (mService == null) return BluetoothProfile.PRIORITY_OFF;
        return mService.getPriority(device);
    }

    public void setPreferred(BluetoothDevice device, boolean preferred) {
        if (mService == null) return;
        if (preferred) {
            if (mService.getPriority(device) < BluetoothProfile.PRIORITY_ON) {
                mService.setPriority(device, BluetoothProfile.PRIORITY_ON);
            }
        } else {
            mService.setPriority(device, BluetoothProfile.PRIORITY_OFF);
        }
    }

    public String toString() {
        return NAME;
    }

    public int getOrdinal() {
        return ORDINAL;
    }

    
    public int getSummaryResourceForDevice(BluetoothDevice device) {
        int state = getConnectionStatus(device);
        switch (state) {
            case BluetoothProfile.STATE_DISCONNECTED:
                return 0;

            case BluetoothProfile.STATE_CONNECTED:
                return 1;

            default:
                return Utils.getConnectionStateSummary(state);
        }
    }

    public int getDrawableResource(BluetoothClass btClass) {
        if (btClass == null) {
            return 0;
        }
        return getHidClassDrawable(btClass);
    }

    static int getHidClassDrawable(BluetoothClass btClass) {
    	return 0;
    	
    	/*
        switch (btClass.getDeviceClass()) {
            case BluetoothClass.Device.PERIPHERAL_KEYBOARD:
            case BluetoothClass.Device.PERIPHERAL_KEYBOARD_POINTING:
                return R.drawable.ic_bt_keyboard_hid;
            case BluetoothClass.Device.PERIPHERAL_POINTING:
                return R.drawable.ic_bt_pointing_hid;
            default:
                return R.drawable.ic_bt_misc_hid;
        }
        */
    	
    }

    protected void finalize() {
        if (V) Log.d(TAG, "finalize()");
        if (mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(BluetoothProfile.INPUT_DEVICE,
                                                                       mService);
                mService = null;
            }catch (Throwable t) {
                Log.w(TAG, "Error cleaning up HID proxy", t);
            }
        }
    }

}
