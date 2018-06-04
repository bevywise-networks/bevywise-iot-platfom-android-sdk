package com.bevywise.bevywiseplatform_connector.listeners;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DeviceListListener extends FailedOperationListener {
    void onGetDeviceListSuccess(JSONArray deviceList, Boolean nextPage, long pageNo);
}
