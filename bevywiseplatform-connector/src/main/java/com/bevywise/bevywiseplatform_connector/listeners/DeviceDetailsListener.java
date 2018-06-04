package com.bevywise.bevywiseplatform_connector.listeners;

import org.json.JSONObject;

public interface DeviceDetailsListener extends FailedOperationListener {
    void onDeviceDetailsSuccess(JSONObject deviceDetails);
}
