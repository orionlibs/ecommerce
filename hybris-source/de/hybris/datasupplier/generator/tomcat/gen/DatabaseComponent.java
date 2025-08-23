package de.hybris.datasupplier.generator.tomcat.gen;

import de.hybris.datasupplier.generator.tomcat.data.DatabaseComponentDeployment;
import java.io.Serializable;

public class DatabaseComponent implements Serializable
{
    private DatabaseComponentDeployment deployment;


    public DatabaseComponent(DatabaseComponentDeployment deployment)
    {
        this.deployment = deployment;
    }


    public DatabaseComponentDeployment getDeployment()
    {
        return this.deployment;
    }


    public void setDeployment(DatabaseComponentDeployment deployment)
    {
        this.deployment = deployment;
    }


    public String toString()
    {
        return "DatabaseComponents [deployment=" + this.deployment + "]";
    }


    public int hashCode()
    {
        int PRIME = 31;
        int result = 1;
        result = 31 * result + ((this.deployment == null) ? 0 : this.deployment.hashCode());
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
        DatabaseComponent other = (DatabaseComponent)obj;
        if(this.deployment == null)
        {
            if(other.deployment != null)
            {
                return false;
            }
        }
        else if(!this.deployment.equals(other.deployment))
        {
            return false;
        }
        return true;
    }
}
