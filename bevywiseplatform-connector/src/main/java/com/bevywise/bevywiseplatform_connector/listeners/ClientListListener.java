package com.bevywise.bevywiseplatform_connector.listeners;

import org.json.JSONArray;

public interface ClientListListener extends FailedOperationListener {
    void onGetClientListSuccess(JSONArray clients);
}
