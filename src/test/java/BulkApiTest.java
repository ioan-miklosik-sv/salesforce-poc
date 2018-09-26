import com.sforce.async.AsyncApiException;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import org.junit.Test;
import org.noip.imiklosik.sfpoc.Connector;
import org.noip.imiklosik.sfpoc.ForceProperties;
import org.noip.imiklosik.sfpoc.auth.Authenticated;
import org.noip.imiklosik.sfpoc.auth.Authenticator;
import org.noip.imiklosik.sfpoc.examples.BulkExample;

import java.io.IOException;

public class BulkApiTest {

    @Test
    public void bulkApiImportCsv_AsUser_Partner() throws ConnectionException, AsyncApiException, IOException {
        Authenticated<PartnerConnection> authenticated = Authenticator.asUser(
                ForceProperties.instance.getUserName(),
                ForceProperties.instance.getPassword(),
                Connector::newPartnerConnection
        );
        runCsvImport(authenticated, "Account", ForceProperties.instance.getAccountsCsvFilePath());
    }

    @Test
    public void bulkApiImportCsv_AsUser_Enterprise() throws ConnectionException, AsyncApiException, IOException {
        Authenticated<EnterpriseConnection> authenticated = Authenticator.asUser(
                ForceProperties.instance.getUserName(),
                ForceProperties.instance.getPassword(),
                Connector::newEnterpriseConnection
        );
        runCsvImport(authenticated, "Account", ForceProperties.instance.getAccountsCsvFilePath());
    }

    @Test
    public void bulkApiImportCsv_AsConnectedApp_Partner() throws ConnectionException, AsyncApiException, IOException {
        Authenticated authenticated = Authenticator.asConnectedApp(
                ForceProperties.instance.getUserName(),
                ForceProperties.instance.getPassword(),
                ForceProperties.instance.getClientId(),
                ForceProperties.instance.getClientSecret(),
                Connector::newPartnerConnection
        );
        runCsvImport(authenticated, "Account", ForceProperties.instance.getAccountsCsvFilePath());
    }

    @Test
    public void bulkApiImportCsv_AsConnectedApp_Enterprise() throws ConnectionException, AsyncApiException, IOException {
        Authenticated authenticated = Authenticator.asConnectedApp(
                ForceProperties.instance.getUserName(),
                ForceProperties.instance.getPassword(),
                ForceProperties.instance.getClientId(),
                ForceProperties.instance.getClientSecret(),
                Connector::newEnterpriseConnection
        );
        runCsvImport(authenticated, "Account", ForceProperties.instance.getAccountsCsvFilePath());
    }

    private void runCsvImport(Authenticated authenticated, String importObject, String filePath)
    throws ConnectionException, AsyncApiException, IOException {
        BulkExample runner = new BulkExample();
        runner.initialize(authenticated);
        runner.fromCsv(importObject, filePath);
    }
}
