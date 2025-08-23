package de.hybris.datasupplier.generator.tomcat.data;

public class ClusterSystem
{
    private String clusterSystemName;
    private String clusterTechnologyType;
    private String computerName;


    public String getClusterSystemName()
    {
        return this.clusterSystemName;
    }


    public void setClusterSystemName(String clusterSystemName)
    {
        this.clusterSystemName = clusterSystemName;
    }


    public String getClusterTechnologyType()
    {
        return this.clusterTechnologyType;
    }


    public void setClusterTechnologyType(String clusterTechnologyType)
    {
        this.clusterTechnologyType = clusterTechnologyType;
    }


    public String getComputerName()
    {
        return this.computerName;
    }


    public void setComputerName(String computerName)
    {
        this.computerName = computerName;
    }


    public String toString()
    {
        return "ClusterSystem [ClusterSystemName=" + this.clusterSystemName + ", ClusterTechnologyType=" + this.clusterTechnologyType + ", ComputerName=" + this.computerName + "]";
    }


    public int hashCode()
    {
        int PRIME = 31;
        int result = 1;
        result = 31 * result + ((this.clusterSystemName == null) ? 0 : this.clusterSystemName.hashCode());
        result = 31 * result + ((this.clusterTechnologyType == null) ? 0 : this.clusterTechnologyType.hashCode());
        result = 31 * result + ((this.computerName == null) ? 0 : this.computerName.hashCode());
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
        ClusterSystem other = (ClusterSystem)obj;
        if(this.clusterSystemName == null)
        {
            if(other.clusterSystemName != null)
            {
                return false;
            }
        }
        else if(!this.clusterSystemName.equals(other.clusterSystemName))
        {
            return false;
        }
        if(this.clusterTechnologyType == null)
        {
            if(other.clusterTechnologyType != null)
            {
                return false;
            }
        }
        else if(!this.clusterTechnologyType.equals(other.clusterTechnologyType))
        {
            return false;
        }
        if(this.computerName == null)
        {
            if(other.computerName != null)
            {
                return false;
            }
        }
        else if(!this.computerName.equals(other.computerName))
        {
            return false;
        }
        return true;
    }
}
