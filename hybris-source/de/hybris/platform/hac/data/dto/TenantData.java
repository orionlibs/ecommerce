package de.hybris.platform.hac.data.dto;

import java.util.List;
import java.util.Locale;

public class TenantData
{
    public static final String TABLE_PREFIX_PLACEHOLDER = "{tenantID}";
    public static final int DB_PREFIX_MAX_LENGTH = 6;
    public static final int TENANT_ID_MAX_LENGTH = 24;
    private String ctxEnabled;
    private String ctx;
    private String dbUrl;
    private String dbDriver;
    private String dbUser;
    private String dbPassword;
    private String tenantID;
    private String timezone;
    private String jndiPool;
    private String tablePrefix;
    private Locale locale;
    private String creationMessage;
    private List<String> extensions;
    private boolean initialized;
    private boolean activated;
    private boolean created;
    private boolean master;


    public boolean isMaster()
    {
        return this.master;
    }


    public void setMaster(boolean master)
    {
        this.master = master;
    }


    public List<String> getExtensions()
    {
        return this.extensions;
    }


    public void setExtensions(List<String> extensions)
    {
        this.extensions = extensions;
    }


    public String getDbUrl()
    {
        return this.dbUrl;
    }


    public void setDbUrl(String dbUrl)
    {
        this.dbUrl = dbUrl;
    }


    public String getDbDriver()
    {
        return this.dbDriver;
    }


    public void setDbDriver(String dbDriver)
    {
        this.dbDriver = dbDriver;
    }


    public String getDbUser()
    {
        return this.dbUser;
    }


    public void setDbUser(String dbUser)
    {
        this.dbUser = dbUser;
    }


    public String getDbPassword()
    {
        return this.dbPassword;
    }


    public void setDbPassword(String dbPassword)
    {
        this.dbPassword = dbPassword;
    }


    public String getTenantID()
    {
        return this.tenantID;
    }


    public void setTenantID(String tenantID)
    {
        this.tenantID = tenantID;
    }


    public String getTimezone()
    {
        return this.timezone;
    }


    public void setTimezone(String timezone)
    {
        this.timezone = timezone;
    }


    public String getJndiPool()
    {
        return this.jndiPool;
    }


    public void setJndiPool(String jndiPool)
    {
        this.jndiPool = jndiPool;
    }


    public String getTablePrefix()
    {
        return this.tablePrefix;
    }


    public void setTablePrefix(String tablePrefix)
    {
        this.tablePrefix = tablePrefix;
    }


    public Locale getLocale()
    {
        return this.locale;
    }


    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }


    public boolean isInitialized()
    {
        return this.initialized;
    }


    public void setInitialized(boolean initialized)
    {
        this.initialized = initialized;
    }


    public boolean isActivated()
    {
        return this.activated;
    }


    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }


    public String getCreationMessage()
    {
        return this.creationMessage;
    }


    public void setCreationMessage(String creationMessage)
    {
        this.creationMessage = creationMessage;
    }


    public boolean isCreated()
    {
        return this.created;
    }


    public void setCreated(boolean created)
    {
        this.created = created;
    }


    public void setCtx(String ctx)
    {
        this.ctx = ctx;
    }


    public String getCtx()
    {
        return this.ctx;
    }


    public void setCtxEnabled(String ctxEnabled)
    {
        this.ctxEnabled = ctxEnabled;
    }


    public String getCtxEnabled()
    {
        return this.ctxEnabled;
    }
}
