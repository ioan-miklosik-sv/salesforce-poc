package org.noip.imiklosik.sfpoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ForceProperties extends Properties {

    private static final String FILENAME_SALESFORCE_PROPERTIES = "salesforce.properties";

    public static final ForceProperties instance = new ForceProperties();

    public ForceProperties() {
        super();
        try {
            load(new FileInputStream(new File(
                    System.getProperty("user.home"),
                    FILENAME_SALESFORCE_PROPERTIES
            )));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserName(){ return getProperty("USERNAME"); }
    public String getPassword(){ return getProperty("PASSWORD"); }
    public String getApiVersion(){ return getProperty("APIVERSION"); }
    public String getOauth2GrantService(){ return getProperty("OAUTH2GRANTSERVICE"); }
    public String getClientId(){ return getProperty("CLIENTID"); }
    public String getClientSecret(){ return getProperty("CLIENTSECRET"); }
    public String getAccountsCsvFilePath(){ return getProperty("ACCOUNTSCSV"); }

}
