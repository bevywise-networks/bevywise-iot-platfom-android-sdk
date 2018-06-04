package com.bevywise.apiexample;

import android.util.Log;
import android.widget.Toast;

import com.bevywise.bevywiseplatform_connector.BevywisePlatformConnector;
import com.bevywise.bevywiseplatform_connector.exceptions.PasswordNotFoundException;
import com.bevywise.bevywiseplatform_connector.listeners.LoginListener;
import com.bevywise.bevywiseplatform_connector.listeners.PlatformResponseListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.bevywise.bevywiseplatform_connector.PlatformConfiguration;

import java.net.MalformedURLException;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private BevywisePlatformConnector bevyWiseConnector;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        bevyWiseConnector = BevywisePlatformConnector.getInstance(new PlatformConfiguration("http://192.168.1.10:9486", "demo@bevywise.com", "pwd123"), this);

        try {
            notificationTest(refreshedToken);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (PasswordNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void notificationTest(final String refreshedToken) throws MalformedURLException, PasswordNotFoundException {
            bevyWiseConnector.login(new LoginListener() {
                @Override
                public void onLoginSuccess(String token, String refreshToken, Long expiresIn)  {
                    try {
                        bevyWiseConnector.setNotificationToken(new PlatformResponseListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("Bevywise-Result", "Sent token successfully");
                                Toast.makeText(getApplicationContext(),"Sent token successfully",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(String reasonForFailure) {
                                Log.d("Bevywise-Result", "Sent token Failed");
                            }
                        },refreshedToken, refreshToken);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String reasonForFailure) {
                    Log.d("Bevywise-Result", "Login Failed");

                }
            });
    }
}
