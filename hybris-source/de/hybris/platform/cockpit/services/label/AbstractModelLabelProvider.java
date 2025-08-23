package de.hybris.platform.cockpit.services.label;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractModelLabelProvider<T> implements ObjectLabelProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractModelLabelProvider.class);
    private ModelService modelService;


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public String getLabel(TypedObject object)
    {
        T item = getModel(object);
        if(item != null)
        {
            return getItemLabel(item);
        }
        return "";
    }


    public String getLabel(TypedObject object, String languageIso)
    {
        T item = getModel(object);
        if(item != null)
        {
            return getItemLabel(item, languageIso);
        }
        return "";
    }


    public String getDescription(TypedObject object)
    {
        T item = getModel(object);
        if(item != null)
        {
            return getItemDescription(item);
        }
        return "";
    }


    public String getDescription(TypedObject object, String languageIso)
    {
        T item = getModel(object);
        if(item != null)
        {
            return getItemDescription(item);
        }
        return "";
    }


    public String getIconPath(TypedObject object)
    {
        T item = getModel(object);
        if(item != null)
        {
            return getIconPath(item);
        }
        return "";
    }


    public String getIconPath(TypedObject object, String languageIso)
    {
        T item = getModel(object);
        if(item != null)
        {
            return getIconPath(item);
        }
        return null;
    }


    protected abstract String getItemLabel(T paramT);


    protected abstract String getItemLabel(T paramT, String paramString);


    protected abstract String getItemDescription(T paramT);


    protected abstract String getItemDescription(T paramT, String paramString);


    protected abstract String getIconPath(T paramT);


    protected abstract String getIconPath(T paramT, String paramString);


    private T getModel(TypedObject object)
    {
        T resultModel = null;
        Object model = object.getObject();
        if(model != null && !getModelService().isNew(model))
        {
            resultModel = (T)model;
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Trying to get model on not persisted object");
        }
        return resultModel;
    }
}
