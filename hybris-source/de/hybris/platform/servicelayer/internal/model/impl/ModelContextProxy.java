package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.ModelContextFactory;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class ModelContextProxy implements ModelContext, InitializingBean
{
    private final ThreadLocal<ModelContext> activeContext = (ThreadLocal<ModelContext>)new Object(this);
    private ModelContextFactory modelContextFactory;


    public void afterPropertiesSet() throws Exception
    {
        Tenant myTenant = Registry.getCurrentTenantNoFallback();
        Registry.registerTenantListener((TenantListener)new Object(this, myTenant));
    }


    protected void unsetContext()
    {
        this.activeContext.remove();
    }


    public ModelContext getActiveContext()
    {
        return this.activeContext.get();
    }


    public void afterPersist(Collection<ModelWrapper> models)
    {
        getActiveContext().afterPersist(models);
    }


    public void afterDirectPersist(Collection<ModelWrapper> models)
    {
        getActiveContext().afterDirectPersist(models);
    }


    public void afterDirectPersist(ModelWrapper wr)
    {
        getActiveContext().afterDirectPersist(wr);
    }


    public void attach(Object model, Object source, ModelConverter conv)
    {
        getActiveContext().attach(model, source, conv);
    }


    public void clear()
    {
        getActiveContext().clear();
    }


    public void detach(Object model, Object source, ModelConverter conv)
    {
        getActiveContext().detach(model, source, conv);
    }


    public Object getAttached(Object source, ModelConverter conv)
    {
        return getActiveContext().getAttached(source, conv);
    }


    public Set<Object> getModified()
    {
        return getActiveContext().getModified();
    }


    public Set<Object> getNew()
    {
        return getActiveContext().getNew();
    }


    public boolean isAttached(Object model, ModelConverter conv)
    {
        return getActiveContext().isAttached(model, conv);
    }


    @Required
    public void setModelContextFactory(ModelContextFactory modelContextFactory)
    {
        this.modelContextFactory = modelContextFactory;
    }
}
