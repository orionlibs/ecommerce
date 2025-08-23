package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;

public class CustomerSupportRAO implements Serializable
{
    private Boolean customerSupportAgentActive;
    private Boolean customerEmulationActive;


    public void setCustomerSupportAgentActive(Boolean customerSupportAgentActive)
    {
        this.customerSupportAgentActive = customerSupportAgentActive;
    }


    public Boolean getCustomerSupportAgentActive()
    {
        return this.customerSupportAgentActive;
    }


    public void setCustomerEmulationActive(Boolean customerEmulationActive)
    {
        this.customerEmulationActive = customerEmulationActive;
    }


    public Boolean getCustomerEmulationActive()
    {
        return this.customerEmulationActive;
    }


    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        CustomerSupportRAO other = (CustomerSupportRAO)o;
        return (Objects.equals(getCustomerSupportAgentActive(), other.getCustomerSupportAgentActive()) &&
                        Objects.equals(getCustomerEmulationActive(), other.getCustomerEmulationActive()));
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.customerSupportAgentActive;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.customerEmulationActive;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
