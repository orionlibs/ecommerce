package de.hybris.platform.adaptivesearchbackoffice.strategies;

import com.hybris.cockpitng.dataaccess.facades.clone.CloneStrategy;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCloningException;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsCloneStrategy;
import de.hybris.platform.core.model.ItemModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeAsCloneStrategy implements CloneStrategy
{
    private static final int DEFAULT_ORDER = 4;
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeAsCloneStrategy.class);
    private TypeFacade typeFacade;
    private ObjectFacade objectFacade;
    private AsCloneStrategy asCloneStrategy;


    public int getOrder()
    {
        return 536870911;
    }


    public <T> boolean canHandle(T objectToClone)
    {
        try
        {
            return (isSupportedModel(objectToClone) && !isNew(objectToClone) && !isSingleton(objectToClone));
        }
        catch(TypeNotFoundException e)
        {
            LOG.error("Can't find object type.", (Throwable)e);
            return false;
        }
    }


    public <T> T clone(T objectToClone) throws ObjectCloningException
    {
        if(!canHandle(objectToClone))
        {
            throw new IllegalStateException("You can't clone with strategy for which canHandle() return false");
        }
        ItemModel itemModel = this.asCloneStrategy.clone((ItemModel)objectToClone);
        if(itemModel instanceof AbstractAsSearchProfileModel)
        {
            AbstractAsSearchProfileModel asCloned = (AbstractAsSearchProfileModel)itemModel;
            asCloned.setCode("");
            asCloned.setActivationSet(null);
        }
        return (T)itemModel;
    }


    protected boolean isSupportedModel(Object objectToClone)
    {
        return (objectToClone instanceof AbstractAsSearchProfileModel || objectToClone instanceof de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel);
    }


    protected boolean isNew(Object objectToClone)
    {
        return getObjectFacade().isNew(objectToClone);
    }


    protected boolean isSingleton(Object objectToClone) throws TypeNotFoundException
    {
        String typeName = getTypeFacade().getType(objectToClone);
        DataType typeData = getTypeFacade().load(typeName);
        return typeData.isSingleton();
    }


    public TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }


    @Required
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public AsCloneStrategy getAsCloneStrategy()
    {
        return this.asCloneStrategy;
    }


    @Required
    public void setAsCloneStrategy(AsCloneStrategy asCloneStrategy)
    {
        this.asCloneStrategy = asCloneStrategy;
    }
}
