import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.ws.ConnectionException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.noip.imiklosik.sfpoc.Connector;
import org.noip.imiklosik.sfpoc.ForceProperties;
import org.noip.imiklosik.sfpoc.auth.Authenticated;
import org.noip.imiklosik.sfpoc.auth.Authenticator;
import org.noip.imiklosik.sfpoc.examples.SoapExample;


public class SoapClientTest {

    private static ForceProperties properties = new ForceProperties();

    private static SoapExample runner = new SoapExample();

    @BeforeClass
    public static void init() throws ConnectionException {
        /*
        Authenticated<EnterpriseConnection> authenticated = Authenticator.asConnectedApp(
                properties.getUserName(),
                properties.getPassword(),
                ForceProperties.instance.getClientId(),
                ForceProperties.instance.getClientSecret(),
                Connector::newEnterpriseConnection
        );*/

        Authenticated<EnterpriseConnection> authenticated = Authenticator.asUser(
                properties.getUserName(),
                properties.getPassword(),
                Connector::newEnterpriseConnection
        );

        runner.initialize(authenticated);

    }

    @AfterClass
    public static void cleanup(){
        runner.cleanup();
    }

    @Test
    public void describeGlobal(){
        runner.describeGlobal();
    }

    @Test
    public void describeObject(){
        runner.describeSObject("Account");
    }

    @Test
    public void query(){
        String soqlQuery = "SELECT FirstName, LastName FROM Contact";
        runner.query(soqlQuery);
    }

}
