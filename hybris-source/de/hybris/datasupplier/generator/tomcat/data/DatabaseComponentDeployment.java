package de.hybris.datasupplier.generator.tomcat.data;

import java.io.Serializable;

public class DatabaseComponentDeployment implements Serializable
{
    private static final long serialVersionUID = 9113599611508397996L;
    private DatabaseVersion databaseVersion;
    private String technicalName;


    public DatabaseComponentDeployment()
    {
    }


    public DatabaseComponentDeployment(DatabaseVersion databaseVersion, String technicalName)
    {
        this.databaseVersion = databaseVersion;
        this.technicalName = technicalName;
    }


    public DatabaseComponentDeployment copy()
    {
        return new DatabaseComponentDeployment(this.databaseVersion, this.technicalName);
    }


    public DatabaseComponentDeployment copy(DatabaseVersion databaseVersion, String technicalName)
    {
        return new DatabaseComponentDeployment(databaseVersion, technicalName);
    }


    public DatabaseVersion getDatabaseVersion()
    {
        return this.databaseVersion;
    }


    public void setDatabaseVersion(DatabaseVersion databaseVersion)
    {
        this.databaseVersion = databaseVersion;
    }


    public String getTechnicalName()
    {
        return this.technicalName;
    }


    public void setTechnicalName(String technicalName)
    {
        this.technicalName = technicalName;
    }


    public String toString()
    {
        return "DatabaseComponentDeployment [databaseVersion=" + this.databaseVersion + ", technicalName=" + this.technicalName + "]";
    }


    public int hashCode()
    {
        int PRIME = 31;
        int result = 1;
        result = 31 * result + ((this.databaseVersion == null) ? 0 : this.databaseVersion.hashCode());
        result = 31 * result + ((this.technicalName == null) ? 0 : this.technicalName.hashCode());
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
        DatabaseComponentDeployment other = (DatabaseComponentDeployment)obj;
        if(this.databaseVersion == null)
        {
            if(other.databaseVersion != null)
            {
                return false;
            }
        }
        else if(!this.databaseVersion.equals(other.databaseVersion))
        {
            return false;
        }
        if(this.technicalName == null)
        {
            if(other.technicalName != null)
            {
                return false;
            }
        }
        else if(!this.technicalName.equals(other.technicalName))
        {
            return false;
        }
        return true;
    }
}
