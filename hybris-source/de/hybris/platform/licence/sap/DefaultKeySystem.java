package de.hybris.platform.licence.sap;

import com.sap.security.core.server.likey.KeySystem;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;

public class DefaultKeySystem implements KeySystem
{
    private final String cmdPrefix = "windows".equals(getLowercaseOsName()) ? "license.bat" : "license.sh";
    private final Properties platformProperties = loadPlatformProperties();
    private final String sapsystem = this.platformProperties.getProperty("license.sap.sapsystem", "CPS");
    private final String hardwareKey = this.platformProperties.getProperty("license.hardware.key", "Y4989890650");
    public static final String HYBRIS_PRODUCT_ID = "CPS";
    public static final String BASIS_RELEASE = "hybris";
    private static final String LICENSE_DB_CODE_PREFIX_CONFIG = "license.db.code.";
    private static final String HW_ID_CONFIG = "license.hardware.key";
    private static final String SAPSYSTEM_CONFIG = "license.sap.sapsystem";
    private static final String DEFAULT_HYBRIS_HW_ID = "Y4989890650";
    private final Map<String, String> dbToProductNameMapping;
    private final Vector<String> swProducts;


    public DefaultKeySystem()
    {
        if(!"Y4989890650".equals(this.hardwareKey))
        {
            System.out.println("patching license hardware ID to '" + this.hardwareKey + "'");
        }
        this.dbToProductNameMapping = initializeDbToProductMapping();
        this.swProducts = buildSwProducts();
    }


    Properties loadPlatformProperties()
    {
        return Utilities.loadPlatformProperties();
    }


    private Map<String, String> initializeDbToProductMapping()
    {
        Map<String, String> tmp = new HashMap<>(5);
        tmp.put("sap", getProductNameForDB(Config.DatabaseName.HANA.getName(), "HDB"));
        tmp.put("hsqldb", getProductNameForDB(Config.DatabaseName.HSQLDB.getName(), "SQL"));
        tmp.put("mysql", getProductNameForDB(Config.DatabaseName.MYSQL.getName(), "MYS"));
        tmp.put("oracle", getProductNameForDB(Config.DatabaseName.ORACLE.getName(), "ORA"));
        tmp.put("sqlserver", getProductNameForDB(Config.DatabaseName.SQLSERVER.getName(), "MSS"));
        tmp.put("postgresql", getProductNameForDB(Config.DatabaseName.POSTGRESQL.getName(), "POS"));
        return Collections.unmodifiableMap(tmp);
    }


    private String getProductNameForDB(String dbName, String defaultDbCode)
    {
        Objects.requireNonNull(dbName, "dbName is required");
        Objects.requireNonNull(defaultDbCode, "defaultDbCode is required");
        Objects.requireNonNull(this.platformProperties, "platformProperties member is required");
        String configuredCode = this.platformProperties.getProperty("license.db.code." + dbName);
        if(StringUtils.isNotBlank(configuredCode) && !defaultDbCode.equals(configuredCode))
        {
            System.out.println("patching license database code for '" + dbName + "' to '" + configuredCode + "' instead of default code '" + defaultDbCode + "'");
        }
        return "CPS_" + (StringUtils.isNotBlank(configuredCode) ? configuredCode.toUpperCase() : defaultDbCode);
    }


    private Vector<String> buildSwProducts()
    {
        Vector<String> swProducts = new Vector<>();
        swProducts.addAll(this.dbToProductNameMapping.values());
        return swProducts;
    }


    private String getLowercaseOsName()
    {
        String osName = System.getProperty("os.name");
        return (osName != null) ? osName.toLowerCase(LocaleHelper.getPersistenceLocale()) : null;
    }


    public boolean init()
    {
        return true;
    }


    public String getSystemId()
    {
        return this.sapsystem;
    }


    public Vector getSwProducts()
    {
        return new Vector<>(this.swProducts);
    }


    public String getBasisRelease()
    {
        return "hybris";
    }


    public String getHwId()
    {
        return this.hardwareKey;
    }


    public String getCmdPrefix()
    {
        return this.cmdPrefix;
    }


    public String getSwProductForDatabase(String dbName)
    {
        return this.dbToProductNameMapping.get(dbName);
    }
}
