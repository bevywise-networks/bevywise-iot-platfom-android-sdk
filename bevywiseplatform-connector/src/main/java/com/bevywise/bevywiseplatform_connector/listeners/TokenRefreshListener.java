package com.bevywise.bevywiseplatform_connector.listeners;

import java.net.MalformedURLException;

public interface TokenRefreshListener extends FailedOperationListener {
    void onTokenRefreshSuccess(String token, String refreshToken, Long expiresIn) throws MalformedURLException;
}
