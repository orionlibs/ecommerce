package de.hybris.platform.licence.internal;

import com.sap.security.core.server.likey.LicenseChecker;
import com.sap.security.core.server.likey.LicenseKey;
import com.sap.security.core.server.likey.LogAndTrace;
import com.sap.security.core.server.likey.Persistence;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.licence.sap.DefaultKeySystem;
import de.hybris.platform.licence.sap.DefaultLogAndTrace;
import de.hybris.platform.licence.sap.DefaultPersistence;
import de.hybris.platform.licence.sap.HybrisAdmin;
import de.hybris.platform.licence.sap.SAPLicense;
import de.hybris.platform.util.ASCIITableReport;
import java.lang.reflect.Constructor;
import java.util.Vector;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class LicenseFactory
{
    private static final Logger LOG = Logger.getLogger(LicenseFactory.class);
    private Persistence persistence;
    private LogAndTrace logAndTrace;

    static
    {
        HybrisAdmin.initializeLicenseLocales();
    }

    public Licence getCurrentLicense(String dbName)
    {
        Licence licence;
        LicenseKey licenseKey = tryFindSAPLicenseKey(dbName);
        if(licenseKey == null)
        {
            licence = createHybrisLicense();
        }
        else
        {
            licence = wrapSapLicense(licenseKey);
        }
        return (licence == null) ? (Licence)new InvalidLicence() : licence;
    }


    private LicenseKey tryFindSAPLicenseKey(String dbName)
    {
        Vector<LicenseKey> allLicenses = LicenseChecker.getAllLicenses(getPersistence(), getLogAndTrace());
        if(CollectionUtils.isNotEmpty(allLicenses))
        {
            DefaultKeySystem keySystem = new DefaultKeySystem();
            for(LicenseKey liKey : allLicenses)
            {
                if(liKey.getSwProduct().equals(keySystem.getSwProductForDatabase(dbName)))
                {
                    return liKey;
                }
            }
            printInvalidSAPLicensesReport(allLicenses, keySystem, dbName);
        }
        else
        {
            System.out.println(">>> No SAP licenses found in the system. Trying fallback to hybris license.");
        }
        return null;
    }


    private void printInvalidSAPLicensesReport(Vector<LicenseKey> allLicenses, DefaultKeySystem keySystem, String dbName)
    {
        System.out.println(">>> There are installed following SAP licenses with invalid properties:");
        System.out.println("");
        ASCIITableReport reportBuilder = ASCIITableReport.builder().withTopHeaders(new String[] {"SYSTEM ID", "HW KEY", "SW PRODUCT"});
        reportBuilder.disableRowTitles();
        for(LicenseKey liKey : allLicenses)
        {
            reportBuilder.addDataRow(new String[] {liKey.getSystemId(), liKey.getHwKey(), liKey.getSwProduct()});
        }
        reportBuilder.printTable();
        System.out.println("");
        System.out.println(">>> This system is configured for the following properties:");
        ASCIITableReport configReportBuilder = ASCIITableReport.builder().withTopHeaders(new String[] {"SYSTEM ID", "HW KEY", "SW PRODUCT"});
        configReportBuilder.disableRowTitles();
        configReportBuilder.addDataRow(new String[] {keySystem.getSystemId(), keySystem.getHwId(),
                        nullOrEmptyToDefault(keySystem.getSwProductForDatabase(dbName), "<<SW-PRODUCT-UNDEFINED>>")});
        configReportBuilder.printTable();
        System.out.println("");
        System.out.println(">>> Trying to switch to hybris license");
        System.out.println("");
    }


    private String nullOrEmptyToDefault(String input, String defaultVal)
    {
        return StringUtils.isBlank(input) ? defaultVal : input;
    }


    protected Persistence getPersistence()
    {
        if(this.persistence == null)
        {
            this.persistence = (Persistence)new DefaultPersistence();
        }
        return this.persistence;
    }


    protected LogAndTrace getLogAndTrace()
    {
        if(this.logAndTrace == null)
        {
            this.logAndTrace = (LogAndTrace)new DefaultLogAndTrace();
        }
        return this.logAndTrace;
    }


    private Licence createHybrisLicense()
    {
        try
        {
            Class<?> dl_class = Class.forName("de.hybris.platform.licence.DefaultLicence");
            Constructor<?> declaredConstructor = dl_class.getDeclaredConstructor(new Class[0]);
            declaredConstructor.setAccessible(true);
            Licence licence = (Licence)declaredConstructor.newInstance(new Object[0]);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("##### hybris licence in use [" + licence + "]");
            }
            return licence;
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No valid licence found on the classpath.");
            }
            return null;
        }
    }


    private Licence wrapSapLicense(LicenseKey licenseKey)
    {
        SAPLicense license = new SAPLicense(licenseKey);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("##### SAP licence in use [" + license + "]");
        }
        return (Licence)license;
    }
}
