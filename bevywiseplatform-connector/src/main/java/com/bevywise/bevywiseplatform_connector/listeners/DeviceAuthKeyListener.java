package com.bevywise.bevywiseplatform_connector.listeners;

import org.json.JSONObject;

public interface DeviceAuthKeyListener extends FailedOperationListener {
    void onAuthKeyGenerated(JSONObject datas);
}
