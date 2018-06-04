package com.bevywise.bevywiseplatform_connector.listeners;

import org.json.JSONArray;

public interface EventsListListener extends FailedOperationListener {
    void onGetDeviceEventSuccess(JSONArray data, Boolean nextPage, long pageNo);
}
