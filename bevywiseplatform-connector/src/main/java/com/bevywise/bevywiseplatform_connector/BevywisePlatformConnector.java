package com.bevywise.bevywiseplatform_connector;

import android.content.Context;

import com.bevywise.bevywiseplatform_connector.exceptions.PasswordNotFoundException;
import com.bevywise.bevywiseplatform_connector.listeners.ClientListListener;
import com.bevywise.bevywiseplatform_connector.listeners.CommandsListListener;
import com.bevywise.bevywiseplatform_connector.listeners.DashboardDetailsListener;
import com.bevywise.bevywiseplatform_connector.listeners.DeviceAuthKeyListener;
import com.bevywise.bevywiseplatform_connector.listeners.DeviceDetailsListener;
import com.bevywise.bevywiseplatform_connector.listeners.DeviceListListener;
import com.bevywise.bevywiseplatform_connector.listeners.EventsListListener;
import com.bevywise.bevywiseplatform_connector.listeners.LoginListener;
import com.bevywise.bevywiseplatform_connector.listeners.PlatformResponseListener;
import com.bevywise.bevywiseplatform_connector.listeners.SignupListener;
import com.bevywise.bevywiseplatform_connector.listeners.TokenRefreshListener;
import com.bevywise.bevywiseplatform_connector.listeners.TopicsListListener;
import com.bevywise.bevywiseplatform_connector.utilities.NetworkConnection;
import com.bevywise.bevywiseplatform_connector.utilities.NetworkErrorCode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class BevywisePlatformConnector {
    private static BevywisePlatformConnector instance;
    private PlatformConfiguration configuration;
    private Context context;

    private BevywisePlatformConnector(PlatformConfiguration configuration, Context context) {
        this.configuration = configuration;
        this.context = context;
    }

    /**
     * Constructor.
     *
     * @param configuration (required) configuration to connect to platform.
     * @param context (required) context (application context or activity context).
     */
    public static BevywisePlatformConnector getInstance(PlatformConfiguration configuration, Context context) {
        if (instance == null) {
            instance = new BevywisePlatformConnector(configuration, context);
        }
        return instance;
    }
    /**
     * Login in to bevywise platform.
     *
     * This method creates new thread to login into platform.
     * @param listener login listener.
     */
    public void login(final LoginListener listener) throws MalformedURLException, PasswordNotFoundException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_login));
        if (configuration.getUsername() != null && configuration.getPassword() != null) {
            NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                            String token = jsonObject.getString(context.getString(R.string.resultToken));
                            String refreshToken =jsonObject.getString("refresh_token");
                            Long expiresIn = jsonObject.getLong("expires_in");
                            listener.onLoginSuccess(token, refreshToken, expiresIn);
                        } else {
                            String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                            listener.onFailure(reasonForFailure);
                        }
                    } catch (Exception e) {
                        //Tell user the api data is changed or something happned.
                        listener.onFailure(context.getString(R.string.invalidJson));
                    }
                }

                @Override
                public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                    listener.onFailure(errorDescription);
                }
            }, context).setMethod("POST")
                    .addBodyParam("username",configuration.getUsername())
                    .addBodyParam("password",configuration.getPassword())
                    .addBodyParam("client_id", configuration.getClientId())
                    .addBodyParam("grant_type", "password")
                    .addBodyParam("client_secret", configuration.getClientSecret())
                    .build();
            networkConnection.Connect();
        } else {
            throw new PasswordNotFoundException("Password not provided in configuration. Hint: Signup first and try to login.");
        }
    }

    /**
     * Refresh token
     *
     * This method refresh expired token
     *
     */

    public void refreshToken(final TokenRefreshListener listener, String refreshToken) throws MalformedURLException, PasswordNotFoundException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_refresh));
        if (configuration.getUsername() != null && configuration.getPassword() != null) {
            NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                            String token = jsonObject.getString(context.getString(R.string.resultToken));
                            String refreshToken =jsonObject.getString("refresh_token");
                            Long expiresIn = jsonObject.getLong("expires_in");
                            listener.onTokenRefreshSuccess(token, refreshToken, expiresIn);
                        } else {
                            String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                            listener.onFailure(reasonForFailure);
                        }
                    } catch (Exception e) {
                        //Tell user the api data is changed or something happned.
                        listener.onFailure(context.getString(R.string.invalidJson));
                    }
                }

                @Override
                public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                    listener.onFailure(errorDescription);
                }
            }, context).setMethod("POST")
                    .addBodyParam("client_id", configuration.getClientId())
                    .addBodyParam("client_secret", configuration.getClientSecret())
                    .addBodyParam("refresh_token", refreshToken)
                    .addBodyParam("grant_type", "refresh_token")
                    .build();
            networkConnection.Connect();
        } else {
            throw new PasswordNotFoundException("Password not provided in configuration. Hint: Signup first and try to login.");
        }
    }

    /**
     * Logout of bevywise platform.
     *
     * This method creates new thread to logout from platform.
     * @param listener logout listener.
     * @param token token
     */
    public void logout(final PlatformResponseListener listener, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_logout));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        listener.onSuccess();
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .addBodyParam("client_id", configuration.getClientId())
                .addBodyParam("client_secret", configuration.getClientSecret())
                .addBodyParam("token", token)
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * New user signup in bevywise platform.
     *
     * This method creates new thread to signup from platform.
     * @param listener signup listener.
     * @param username email id of the user.
     * @param password password for the user
     */
    public void signup(final SignupListener listener, final String username, final String password) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_signup));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        String token = jsonObject.getString(context.getString(R.string.resultToken));
                        String refreshToken =jsonObject.getString("refresh_token");
                        Long expiresIn = jsonObject.getLong("expires_in");
                        configuration = new PlatformConfiguration(configuration.getUrl(), username, password);
                        listener.onSignupSuccess(token,refreshToken, expiresIn);
                    } else {
                        String reasonForFailure = jsonObject.getString("message");
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    e.printStackTrace();
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .addBodyParam("username",username)
                .addBodyParam("grant_type", "password")
                .addBodyParam("password",password)
                .addBodyParam("client_id", configuration.getClientId())
                .addBodyParam("client_secret", configuration.getClientSecret())
                .build();
        networkConnection.Connect();
    }

    /**
     * Get device auth key.
     *
     * This method creates new thread to get device auth key from platform.
     * @param listener DeviceAuthKeyListener listener.
     * @param permissions permission for the device.
     * @param description key description.
     * @param token token
     */
    public void getDeviceAuthKey(final DeviceAuthKeyListener listener, DevicePermissions permissions, String description, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_generate_key));
        String keyPermission;
        String keyDescription = description;
        switch (permissions) {
            case READ:
                keyPermission = "Read";
                break;
            case WRITE:
                keyPermission = "Write";
                break;
            case READ_WRITE:
                keyPermission = "Read Write";
                break;
            default:
                keyPermission = "Read";
                break;
        }
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        listener.onAuthKeyGenerated(data);
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .addBodyParam("permissions",keyPermission)
                .addBodyParam("key_desc", keyDescription)
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * Get list of all devices.
     *
     * This method creates new thread to get list of all available device from platform.
     * @param listener DeviceListListener listener.
     * @param pageNo pageno to get.
     * @param token token
     */
    public void getDeviceList(final DeviceListListener listener, long pageNo, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_device_list));
        NetworkConnection.Builder builder = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        Boolean nextPage = jsonObject.getBoolean("next_page");
                        long pageNo = jsonObject.getLong("page_no");
                        listener.onGetDeviceListSuccess(data, nextPage, pageNo);
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context);
        builder.setMethod("POST");
        builder.addBodyParam("page_no", "" + pageNo);
        builder.setToken(token);
        NetworkConnection networkConnection = builder.build();
        networkConnection.Connect();
    }

    /**
     * Edit device name.
     *
     * This method creates new thread to get list of all available device from platform.
     * @param listener PlatformResponseListener listener.
     * @param deviceId id of the device.
     * @param deviceName name of the device.
     * @param token token
     */
    public void editDeviceName(final PlatformResponseListener listener, String deviceId, String deviceName, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_device_edit_name));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        listener.onSuccess();
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .addBodyParam("device_id", deviceId)
                .addBodyParam("new_device_name", deviceName)
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * Edit device name.
     *
     * This method creates new thread to get list of all available device from platform.
     * @param listener PlatformResponseListener listener.
     * @param pushNotificationToken token generated for FCM notification.
     * @param token token
     */
    public void setNotificationToken(final PlatformResponseListener listener, String pushNotificationToken, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_set_token));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        listener.onSuccess();
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .addBodyParam("notification_token", pushNotificationToken)
                .addBodyParam("device", "android")
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * Get device events list.
     *
     * This method creates new thread to get list of all available device from platform.
     * @param listener EventsListListener listener.
     * @param deviceId id of the device.
     * @param pageNo pageNo to get.
     * @param token token
     */
    public void getDeviceEvents(final EventsListListener listener, String deviceId, long pageNo, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_get_device_rcvd_events));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        JSONArray data = jsonObject.getJSONArray("recv_data");
                            Boolean nextPage = jsonObject.getJSONObject("page_nav_data").getBoolean("next_page");
                        long pageNo = jsonObject.getJSONObject("page_nav_data").getLong("page_no");
                        listener.onGetDeviceEventSuccess(data, nextPage, pageNo);
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .addBodyParam("page_no", ""+pageNo)
                .addBodyParam("device_id", deviceId)
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * Get list of received commands for device.
     *
     * This method get list of received commands for device.
     * @param listener CommandsListListener listener.
     * @param deviceId id of the device.
     * @param pageNo pageNo to get.
     * @param token token
     */
    public void getRecievedCommandsForDevice(final CommandsListListener listener, String deviceId, long pageNo, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_get_device_rcvd_cmds));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        JSONArray data = jsonObject.getJSONArray("recv_data");
                        Boolean nextPage = jsonObject.getJSONObject("page_nav_data").getBoolean("next_page");
                        long pageNo = jsonObject.getJSONObject("page_nav_data").getLong("page_no");
                        listener.onGetDeviceCmdsSuccess(data, nextPage, pageNo);
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .addBodyParam("page_no", ""+pageNo)
                .addBodyParam("device_id", deviceId)
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * Get client list for topic.
     *
     * This method get list of clients for a topic.
     * @param listener ClientListListener listener.
     * @param token token
     */
    public void getClientListForTopic(final ClientListListener listener, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_get_client_topic_list));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        JSONArray clients = jsonObject.getJSONArray("clients");
                        listener.onGetClientListSuccess(clients);
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * Get active topic subscription of device.
     *
     * This method get list of clients for a topic.
     * @param listener TopicsListListener listener.
     * @param deviceId id of the device.
     * @param token token
     */
    public void getActiveTopicSubscriptionofDevice(final TopicsListListener listener, String deviceId, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_get_active_topic_subscription));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        JSONArray topics = jsonObject.getJSONArray("topics");
                        listener.onGetTopicListSuccess(topics);
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .addBodyParam("device_id", deviceId)
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * Get active subscriptions.
     *
     * This method get active subscriptions.
     * @param listener TopicsListListener listener.
     * @param token token
     */
    public void getActiveSubscriptionListener(final TopicsListListener listener, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_get_active_subscription));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        JSONArray topics = jsonObject.getJSONArray("active_topics");
                        listener.onGetTopicListSuccess(topics);
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * Get dashboard details.
     *
     * This method get dashboard details.
     * @param listener TopicsListListener listener.
     * @param token token
     */
    public void getDashboardDetails(final DashboardDetailsListener listener, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_get_dashboard_details));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        JSONObject dashboardData = jsonObject.getJSONObject("data");
                        listener.onDashboardDetailsSuccess(dashboardData);
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .setToken(token)
                .build();
        networkConnection.Connect();
    }

    /**
     * Get device details.
     *
     * This method gets device details.
     * @param listener DeviceDetailsListener listener.
     * @param deviceId id of the device.
     * @param token token
     */
    public void getDeviceDetails(final DeviceDetailsListener listener, String deviceId, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_get_device_details));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        listener.onDeviceDetailsSuccess(jsonObject);
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .setToken(token)
                .addBodyParam("device_id",deviceId)
                .build();
        networkConnection.Connect();
    }

    /**
     * Send commands to topics.
     *
     * This method sends commmands to topic.
     * @param listener PlatformResponseListener listener.
     * @param topic Topic to send command.
     * @param commandToSend Command to send.
     * @param token token
     */
    public void sendCommandsToTopic(final PlatformResponseListener listener, String topic, String commandToSend, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_send_command_topic));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        listener.onSuccess();
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .setToken(token)
                .addBodyParam("topic", topic)
                .addBodyParam("command",commandToSend)
                .build();
        networkConnection.Connect();
    }

    /**
     * Send commands to device.
     *
     * This method sends commands to device.
     * @param listener PlatformResponseListener listener.
     * @param topic Topic to send command.
     * @param commandToSend Command to send.
     * @param token token
     * @param deviceId deviceId of the device.
     */
    public void sendCommandsToDevice(final PlatformResponseListener listener, String topic, String commandToSend, String deviceId, String token) throws MalformedURLException {
        URL url = new URL(configuration.getUrl()+context.getString(R.string.api_version_one)+context.getString(R.string.api_send_command_device));
        NetworkConnection networkConnection = new NetworkConnection.Builder(url, new NetworkConnection.Listener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString(context.getString(R.string.api_version_result_status)).equals(context.getString(R.string.api_version_result_status_value))) {
                        listener.onSuccess();
                    } else {
                        String reasonForFailure = jsonObject.getString(context.getString(R.string.resultStatusReason));
                        listener.onFailure(reasonForFailure);
                    }
                } catch (Exception e) {
                    //Tell user the api data is changed or something happned.
                    listener.onFailure(context.getString(R.string.invalidJson));
                }
            }

            @Override
            public void onFailed(String errorDescription, NetworkErrorCode errorCode) {
                listener.onFailure(errorDescription);
            }
        }, context).setMethod("POST")
                .setToken(token)
                .addBodyParam("topic", topic)
                .addBodyParam("device_id", deviceId)
                .addBodyParam("command",commandToSend)
                .build();
        networkConnection.Connect();
    }

}
