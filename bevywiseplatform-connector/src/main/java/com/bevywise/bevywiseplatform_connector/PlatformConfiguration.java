package com.bevywise.bevywiseplatform_connector;

public class PlatformConfiguration {
    private final String url;
    private final String username;
    private final String password;
    private final String clientId;
    private final String clientSecret;

    /**
     * Constructor.
     *
     * @param url (required) url where your bevywise platform is running
     * @param username (required) username
     * @param password (required) password
     */
    public PlatformConfiguration(String url, String username, String password, String clientId, String clientSecret) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public PlatformConfiguration(String url, String clientId, String clientSecret) {
        this.url = url;
        this.username = null;
        this.password = null;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
