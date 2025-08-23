package de.hybris.platform.persistence.type.update;

import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.type.update.misc.UpdateDataUtil;
import de.hybris.platform.persistence.type.update.misc.UpdateModelException;
import de.hybris.platform.persistence.type.update.strategy.ChangeColumnStrategy;
import de.hybris.platform.util.Config;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ModelUpdater
{
    private static final Logger LOG = Logger.getLogger(ModelUpdater.class.getName());
    private ChangeColumnStrategy strategy = null;


    public static ModelUpdater getInstance()
    {
        return (ModelUpdater)Registry.getApplicationContext().getBean("modelUpdater");
    }


    @Required
    public void setStrategy(ChangeColumnStrategy strategy)
    {
        this.strategy = strategy;
    }


    public void doUpdate(List<ModelChangedEvent> modelsList) throws UpdateModelException
    {
        if(isDatabaseSupported())
        {
            checkModelForModification(modelsList);
        }
        else
        {
            LOG.warn("Current database implementation <<" + Config.getDatabase() + ">> is not supported for update model ");
        }
    }


    protected boolean isDatabaseSupported()
    {
        return UpdateDataUtil.isDatabaseSupported();
    }


    protected void checkModelForModification(List<ModelChangedEvent> modelsList) throws UpdateModelException
    {
        for(ModelChangedEvent mce : modelsList)
        {
            boolean success = this.strategy.doChangeColumn(mce.getTargetType(), mce.getCurrentColumnModel(), mce
                            .getAttributeDescriptor());
            if(!success && LOG.isDebugEnabled())
            {
                LOG.debug(" change column from " + mce);
            }
        }
    }
}
