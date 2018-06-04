package com.bevywise.bevywiseplatform_connector.listeners;

import org.json.JSONArray;

public interface CommandsListListener extends FailedOperationListener {
    void onGetDeviceCmdsSuccess(JSONArray cmdList, Boolean nextPage, long pageNo);
}
