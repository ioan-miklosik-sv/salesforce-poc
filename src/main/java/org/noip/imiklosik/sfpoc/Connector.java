package org.noip.imiklosik.sfpoc;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.noip.imiklosik.sfpoc.auth.Authenticator;

public class Connector {

    public final static String SOAP_SERVICE_PATH_PARTNER = "/services/Soap/u/" + ForceProperties.instance.getApiVersion();
    public final static String SOAP_SERVICE_PATH_ENTERPRISE = "/services/Soap/c/" + ForceProperties.instance.getApiVersion();

    public static PartnerConnection newPartnerConnection(ConnectorConfig config){
        try {
            config.setAuthEndpoint(Authenticator.USER_PARTNER_AUTH_ENDPOINT);
            if(config.isManualLogin()){
                fixServiceEndpoint(config, SOAP_SERVICE_PATH_PARTNER);
            }
            return new PartnerConnection(config);
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static EnterpriseConnection newEnterpriseConnection(ConnectorConfig config){
        try {
            config.setAuthEndpoint(Authenticator.USER_ENTERPRISE_AUTH_ENDPOINT);
            if(config.isManualLogin()){
                fixServiceEndpoint(config, SOAP_SERVICE_PATH_ENTERPRISE);
            }
            return new EnterpriseConnection(config);
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fixServiceEndpoint(ConnectorConfig config, String path){
        // TODO: must make sure this is documented somewhere
        String sessionId = config.getSessionId();
        String id = sessionId.substring(0, sessionId.indexOf('!'));
        config.setServiceEndpoint(config.getServiceEndpoint() + path + "/" + id);
    }

}
