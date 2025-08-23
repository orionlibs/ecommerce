package de.hybris.platform.servicelayer.internal.service;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class AbstractBusinessService extends AbstractService
{
    @Deprecated(since = "ages", forRemoval = true)
    protected ModelService modelService;
    @Deprecated(since = "ages", forRemoval = true)
    protected SessionService sessionService;
    @Deprecated(since = "ages", forRemoval = true)
    protected PlatformTransactionManager txManager;


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setTxManager(PlatformTransactionManager txManager)
    {
        this.txManager = txManager;
    }


    protected PlatformTransactionManager getTxManager()
    {
        return this.txManager;
    }
}
