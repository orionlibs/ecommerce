package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.util.ConfigParameterHelper;
import java.util.Map;

public class DatabaseSettings
{
    private final DataBaseProvider dataBaseProvider;
    private final String url;
    private final String driverName;
    private final String user;
    private final String password;
    private final String tablePrefix;
    private final PropertiesLoader propertiesLoader;
    private String statementDelimiter;


    public DatabaseSettings(DataBaseProvider dataBaseProvider, String url, String driverName, String user, String password, String tablePrefix, String statementDelimiter)
    {
        this(dataBaseProvider, url, driverName, user, password, tablePrefix, null, statementDelimiter);
    }


    public DatabaseSettings(DataBaseProvider dataBaseProvider, String url, String driverName, String user, String password, String tablePrefix, PropertiesLoader propertiesLoader, String statementDelimiter)
    {
        this.dataBaseProvider = dataBaseProvider;
        this.url = url;
        this.driverName = driverName;
        this.user = user;
        this.password = password;
        this.tablePrefix = tablePrefix;
        this.propertiesLoader = propertiesLoader;
        this.statementDelimiter = statementDelimiter;
    }


    public String getProperty(String key)
    {
        return (this.propertiesLoader == null) ? null : this.propertiesLoader.getProperty(key);
    }


    public String getProperty(String key, String defaultValue)
    {
        return (this.propertiesLoader == null) ? null : this.propertiesLoader.getProperty(key, defaultValue);
    }


    public Map<String, String> getParametersMatching(String keyRegExp, boolean stripMatchingKey)
    {
        return ConfigParameterHelper.getParametersMatching(this.propertiesLoader.getAllProperties(), keyRegExp, stripMatchingKey);
    }


    public DataBaseProvider getDataBaseProvider()
    {
        return this.dataBaseProvider;
    }


    public String getDriverName()
    {
        return this.driverName;
    }


    public String getPassword()
    {
        return this.password;
    }


    public String getTablePrefix()
    {
        if(this.dataBaseProvider != null)
        {
            return this.dataBaseProvider.getTableName(this.tablePrefix);
        }
        return this.tablePrefix;
    }


    public String getUrl()
    {
        return this.url;
    }


    public String getUser()
    {
        return this.user;
    }


    public String getStatementDelimiter()
    {
        return this.statementDelimiter;
    }


    public void setStatementDelimiter(String statementDelimiter)
    {
        this.statementDelimiter = statementDelimiter;
    }


    public String getTypeSystemName()
    {
        return (this.propertiesLoader == null) ? "DEFAULT" :
                        this.propertiesLoader.getProperty("db.type.system.name", "DEFAULT");
    }


    public boolean isDefaultTypeSystem()
    {
        return "DEFAULT".equals(getTypeSystemName());
    }


    public String toString()
    {
        return "DatabaseSettings{statementDelimiter='" + this.statementDelimiter + "', dataBaseProvider='" + this.dataBaseProvider + "', url='" + this.url + "', driverName='" + this.driverName + "', user='" + this.user + "', tablePrefix='" + this.tablePrefix + "'}";
    }
}
