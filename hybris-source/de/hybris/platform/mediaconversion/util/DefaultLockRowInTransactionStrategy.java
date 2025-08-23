package de.hybris.platform.mediaconversion.util;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultLockRowInTransactionStrategy implements LockRowInTransactionStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultLockRowInTransactionStrategy.class);
    private ModelService modelService;


    public boolean lock(ItemModel model)
    {
        if(Config.isHSQLDBUsed())
        {
            LOG.warn("Row locking not supported in HSQLDB.");
            return false;
        }
        try
        {
            Transaction.current().lock((Item)getModelService().getSource(model));
            return true;
        }
        catch(IllegalStateException e)
        {
            LOG.warn("Failed to retrieve lock on '" + model + "'.", e);
            return false;
        }
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
