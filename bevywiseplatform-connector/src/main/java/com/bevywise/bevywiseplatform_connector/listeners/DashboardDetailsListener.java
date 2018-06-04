package com.bevywise.bevywiseplatform_connector.listeners;

import org.json.JSONObject;

public interface DashboardDetailsListener extends FailedOperationListener {
    void onDashboardDetailsSuccess(JSONObject dashboardData);
}
