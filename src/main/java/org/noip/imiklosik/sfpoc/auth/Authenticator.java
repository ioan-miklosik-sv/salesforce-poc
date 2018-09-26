package org.noip.imiklosik.sfpoc.auth;

import com.sforce.ws.ConnectorConfig;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.noip.imiklosik.sfpoc.ForceProperties;

import java.io.IOException;
import java.util.function.Function;

public class Authenticator {

    public static final String USER_PARTNER_AUTH_ENDPOINT = "https://login.salesforce.com/services/Soap/u/" + ForceProperties.instance.getApiVersion();
    public static final String USER_ENTERPRISE_AUTH_ENDPOINT = "https://login.salesforce.com/services/Soap/c/" + ForceProperties.instance.getApiVersion();
    public static final String OAUTH2_ENDPOINT = "https://login.salesforce.com/services/oauth2/token";

    public  static <T> Authenticated<T> asUser(
            String username,
            String password,
            Function<ConnectorConfig, T> connectionFactory
    ) {
        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(username);
        config.setPassword(password);

        // Creating the connection automatically handles login and stores
        // the session in config
        T connection = connectionFactory.apply(config);

        return new Authenticated<>(config, connection);
    }

    public static <T> Authenticated<T> asConnectedApp(
            String username, String password, String clientId,
            String clientSecret,
            Function<ConnectorConfig, T> connectionFactory
    ){

        HttpClient httpclient = HttpClientBuilder.create().build();

        // Assemble the login request URL
        String authUrl = OAUTH2_ENDPOINT + "?" +
                "grant_type=password" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&username=" + username +
                "&password=" + password;

        // Login requests must be POSTs
        HttpPost httpPost = new HttpPost(authUrl);
        HttpResponse response;

        try {
            // Execute the login POST request
            response = httpclient.execute(httpPost);
        } catch (ClientProtocolException ex) {
            // Handle protocol exception
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            // Handle system IO exception
            throw new RuntimeException(ex);
        }

        // verify response is HTTP OK
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            System.out.println("Error authenticating to Force.com: "+statusCode);
            // Error is in EntityUtils.toString(response.getEntity())
            throw new RuntimeException("Could not authenticate: " + statusCode);
        }

        String getResult;
        try {
            getResult = EntityUtils.toString(response.getEntity());
            System.out.println(getResult);
        } catch (IOException ex) {
            // Handle system IO exception
            throw new RuntimeException(ex);
        }
        JSONObject jsonObject;
        String loginAccessToken;
        String instanceUrl;
        try {
            jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
            loginAccessToken = jsonObject.getString("access_token");
            instanceUrl = jsonObject.getString("instance_url");
        } catch (JSONException ex) {
            // Handle JSON exception
            throw new RuntimeException(ex);
        }

        // release connection
        httpPost.releaseConnection();

        // we don't need to connect (authenticate) any more since
        //   we have our oauth authentication completed above
        // so we are going to build the config and the connection
        //   with the tokens inside manually so connection will not be attempted here
        ConnectorConfig config = new ConnectorConfig();
        config.setManualLogin(true);
        config.setAuthEndpoint(authUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setServiceEndpoint(instanceUrl);
        config.setSessionId(loginAccessToken);
        T connection = connectionFactory.apply(config);

        // print infos
        System.out.println("Successful login");
        System.out.println("  access/token/session ID: " + config.getSessionId());
        System.out.println("  service URL: " + config.getServiceEndpoint());

        return new Authenticated<>(config, connection);
    }

}
