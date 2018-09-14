import com.sforce.async.AsyncApiException;
import com.sforce.ws.ConnectionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    private static final String FILENAME_SALESFORCE_PROPERTIES = "salesforce.properties";

    public static void main(String[] args) throws AsyncApiException, ConnectionException, IOException {

        Properties salesforceProps = new Properties();
        salesforceProps.load(new FileInputStream(
                new File(System.getProperty("user.home"), FILENAME_SALESFORCE_PROPERTIES)
        ));

        String userName = salesforceProps.getProperty("USERNAME");
        String password = salesforceProps.getProperty("PASSWORD");
        String loginUrl = salesforceProps.getProperty("LOGINURL");
        String grantService = salesforceProps.getProperty("GRANTSERVICE");
        String clientId = salesforceProps.getProperty("CLIENTID");
        String clientSecret = salesforceProps.getProperty("CLIENTSECRET");

        new TestBulkCsv().run(
                "Account",
                userName,
                password,
                "mySampleData.csv"
        );

    }



}
