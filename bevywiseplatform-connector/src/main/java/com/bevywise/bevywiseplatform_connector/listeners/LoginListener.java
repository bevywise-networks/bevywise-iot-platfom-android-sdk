package com.bevywise.bevywiseplatform_connector.listeners;

import java.net.MalformedURLException;

public interface LoginListener extends FailedOperationListener {
    void onLoginSuccess(String token, String refreshToken, Long expiresIn) throws MalformedURLException;
}
