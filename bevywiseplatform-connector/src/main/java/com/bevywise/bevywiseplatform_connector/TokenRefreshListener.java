package com.bevywise.bevywiseplatform_connector;

import com.bevywise.bevywiseplatform_connector.listeners.FailedOperationListener;

import java.net.MalformedURLException;

interface TokenRefreshListener extends FailedOperationListener {
    void onTokenRefreshSuccess(String token, String refreshToken, Long expiresIn) throws MalformedURLException;
}
