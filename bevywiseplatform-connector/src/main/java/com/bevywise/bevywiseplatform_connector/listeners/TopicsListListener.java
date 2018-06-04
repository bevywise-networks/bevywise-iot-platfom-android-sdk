package com.bevywise.bevywiseplatform_connector.listeners;

import org.json.JSONArray;

public interface TopicsListListener extends FailedOperationListener {
    void onGetTopicListSuccess(JSONArray topics);
}
