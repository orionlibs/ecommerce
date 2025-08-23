package de.hybris.platform.hac.data.dto;

import java.util.Map;

public class DatabaseInfoData
{
    private boolean active;
    private String dsId;
    private String url;
    private String dbName;
    private String dbVersion;
    private String dbDriverVersion;
    private String dbUser;
    private String pool;
    private String tablePrefixName;
    private int numInUse;
    private int numPhysicalOpen;
    private int maxPhysicalOpen;
    private int maxAllowedOpen;
    private int numReadOnlyOpen;
    private long totalGets;
    private long millisWaitedForConn;
    private Map<String, String> mysqlInfos;
    private boolean jndi;
    private boolean tablePrefix;


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setDsId(String dsId)
    {
        this.dsId = dsId;
    }


    public String getDsId()
    {
        return this.dsId;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }


    public String getDbName()
    {
        return this.dbName;
    }


    public void setDbVersion(String dbVersion)
    {
        this.dbVersion = dbVersion;
    }


    public String getDbVersion()
    {
        return this.dbVersion;
    }


    public void setDbDriverVersion(String dbDriverVersion)
    {
        this.dbDriverVersion = dbDriverVersion;
    }


    public String getDbDriverVersion()
    {
        return this.dbDriverVersion;
    }


    public void setDbUser(String dbUser)
    {
        this.dbUser = dbUser;
    }


    public String getDbUser()
    {
        return this.dbUser;
    }


    public void setPool(String pool)
    {
        this.pool = pool;
    }


    public String getPool()
    {
        return this.pool;
    }


    public void setTablePrefixName(String tablePrefixName)
    {
        this.tablePrefixName = tablePrefixName;
    }


    public String getTablePrefixName()
    {
        return this.tablePrefixName;
    }


    public void setNumInUse(int numInUse)
    {
        this.numInUse = numInUse;
    }


    public int getNumInUse()
    {
        return this.numInUse;
    }


    public void setNumPhysicalOpen(int numPhysicalOpen)
    {
        this.numPhysicalOpen = numPhysicalOpen;
    }


    public int getNumPhysicalOpen()
    {
        return this.numPhysicalOpen;
    }


    public void setMaxPhysicalOpen(int maxPhysicalOpen)
    {
        this.maxPhysicalOpen = maxPhysicalOpen;
    }


    public int getMaxPhysicalOpen()
    {
        return this.maxPhysicalOpen;
    }


    public void setMaxAllowedOpen(int maxAllowedOpen)
    {
        this.maxAllowedOpen = maxAllowedOpen;
    }


    public int getMaxAllowedOpen()
    {
        return this.maxAllowedOpen;
    }


    public void setTotalGets(long totalGets)
    {
        this.totalGets = totalGets;
    }


    public long getTotalGets()
    {
        return this.totalGets;
    }


    public void setMillisWaitedForConn(long millisWaitedForConn)
    {
        this.millisWaitedForConn = millisWaitedForConn;
    }


    public long getMillisWaitedForConn()
    {
        return this.millisWaitedForConn;
    }


    public void setMysqlInfos(Map<String, String> mysqlInfos)
    {
        this.mysqlInfos = mysqlInfos;
    }


    public Map<String, String> getMysqlInfos()
    {
        return this.mysqlInfos;
    }


    public void setJndi(boolean jndi)
    {
        this.jndi = jndi;
    }


    public boolean isJndi()
    {
        return this.jndi;
    }


    public void setTablePrefix(boolean tablePrefix)
    {
        this.tablePrefix = tablePrefix;
    }


    public boolean isTablePrefix()
    {
        return this.tablePrefix;
    }


    public int getNumReadOnlyOpen()
    {
        return this.numReadOnlyOpen;
    }


    public void setNumReadOnlyOpen(int numReadOnlyOpen)
    {
        this.numReadOnlyOpen = numReadOnlyOpen;
    }
}
