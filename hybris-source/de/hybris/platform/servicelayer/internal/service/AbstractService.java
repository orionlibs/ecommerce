package de.hybris.platform.servicelayer.internal.service;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Tenant;
import java.io.ObjectStreamException;
import java.io.Serializable;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractService implements InitializingBean, BeanNameAware, Serializable
{
    private static final Logger LOG = Logger.getLogger(AbstractService.class.getName());
    @Deprecated(since = "5.0", forRemoval = true)
    protected volatile Tenant tenant;
    private volatile Tenant currentTenant;
    private String beanname;


    public void afterPropertiesSet() throws Exception
    {
        checkObsoleteTenantAssignment();
        Preconditions.checkNotNull(this.currentTenant, "Current tenant for service" + this + " should be not null");
        this.tenant = this.currentTenant;
    }


    private void checkObsoleteTenantAssignment()
    {
        if(this.tenant != null && this.currentTenant == null)
        {
            LOG.warn("!!!!!! Obsolete direct current tenant property assignment found !!!!!");
            LOG.warn("Please remove an explicit assignment for current tenant property in bean " + this + " this will be removed in future versions - causing code to not compile.");
            this.currentTenant = this.tenant;
        }
    }


    public void setCurrentTenant(Tenant currentTenant)
    {
        this.currentTenant = currentTenant;
    }


    protected Tenant getCurrentTenant()
    {
        return this.currentTenant;
    }


    public void setBeanName(String name)
    {
        this.beanname = name;
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new SerializableDTO(getCurrentTenant(), this.beanname);
    }
}
