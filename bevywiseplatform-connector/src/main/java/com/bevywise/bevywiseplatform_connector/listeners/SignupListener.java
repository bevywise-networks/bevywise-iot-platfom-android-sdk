package com.bevywise.bevywiseplatform_connector.listeners;

public interface SignupListener extends FailedOperationListener {
    void onSignupSuccess(String token, String refreshToken, Long expiresIn);
}
