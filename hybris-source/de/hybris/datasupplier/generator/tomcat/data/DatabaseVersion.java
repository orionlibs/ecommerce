package de.hybris.datasupplier.generator.tomcat.data;

import java.io.Serializable;

public class DatabaseVersion implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String databaseName;
    private String dBTypeForSAP;
    private String databaseHost;
    private String databaseFQDName;
    private String databaseIPAddress;


    public String getDatabaseName()
    {
        return this.databaseName;
    }


    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }


    public String getDBTypeForSAP()
    {
        return this.dBTypeForSAP;
    }


    public void setDBTypeForSAP(String dBTypeForSAP)
    {
        this.dBTypeForSAP = dBTypeForSAP;
    }


    public String getDatabaseHost()
    {
        return this.databaseHost;
    }


    public void setDatabaseHost(String databaseHost)
    {
        this.databaseHost = databaseHost;
    }


    public String getDatabaseFQDName()
    {
        return this.databaseFQDName;
    }


    public void setDatabaseFQDName(String databaseFQDName)
    {
        this.databaseFQDName = databaseFQDName;
    }


    public String getDatabaseIPAddress()
    {
        return this.databaseIPAddress;
    }


    public void setDatabaseIPAddress(String databaseIPAddress)
    {
        this.databaseIPAddress = databaseIPAddress;
    }


    public String toString()
    {
        return "DatabaseVersion [DatabaseName=" + this.databaseName + ", DBTypeForSAP=" + this.dBTypeForSAP + ", DatabaseHost=" + this.databaseHost + ", DatabaseFQDName=" + this.databaseFQDName + ", DatabaseIPAddress=" + this.databaseIPAddress + "]";
    }


    public int hashCode()
    {
        int PRIME = 31;
        int result = 1;
        result = 31 * result + ((this.dBTypeForSAP == null) ? 0 : this.dBTypeForSAP.hashCode());
        result = 31 * result + ((this.databaseFQDName == null) ? 0 : this.databaseFQDName.hashCode());
        result = 31 * result + ((this.databaseHost == null) ? 0 : this.databaseHost.hashCode());
        result = 31 * result + ((this.databaseIPAddress == null) ? 0 : this.databaseIPAddress.hashCode());
        result = 31 * result + ((this.databaseName == null) ? 0 : this.databaseName.hashCode());
        return result;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        DatabaseVersion other = (DatabaseVersion)obj;
        if(this.dBTypeForSAP == null)
        {
            if(other.dBTypeForSAP != null)
            {
                return false;
            }
        }
        else if(!this.dBTypeForSAP.equals(other.dBTypeForSAP))
        {
            return false;
        }
        if(this.databaseFQDName == null)
        {
            if(other.databaseFQDName != null)
            {
                return false;
            }
        }
        else if(!this.databaseFQDName.equals(other.databaseFQDName))
        {
            return false;
        }
        if(this.databaseHost == null)
        {
            if(other.databaseHost != null)
            {
                return false;
            }
        }
        else if(!this.databaseHost.equals(other.databaseHost))
        {
            return false;
        }
        if(this.databaseIPAddress == null)
        {
            if(other.databaseIPAddress != null)
            {
                return false;
            }
        }
        else if(!this.databaseIPAddress.equals(other.databaseIPAddress))
        {
            return false;
        }
        if(this.databaseName == null)
        {
            if(other.databaseName != null)
            {
                return false;
            }
        }
        else if(!this.databaseName.equals(other.databaseName))
        {
            return false;
        }
        return true;
    }
}
