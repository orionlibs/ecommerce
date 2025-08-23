package de.hybris.platform.cockpit.services.label;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

@Deprecated
public abstract class AbstractObjectLabelProvider<T> implements ObjectLabelProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractObjectLabelProvider.class);
    private ModelService modelService;


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    public String getLabel(TypedObject object)
    {
        T item = getObjectSource(object);
        if(item != null)
        {
            return getItemLabel(item);
        }
        return "";
    }


    public String getLabel(TypedObject object, String languageIso)
    {
        T item = getObjectSource(object);
        if(item != null)
        {
            return getItemLabel(item, languageIso);
        }
        return "";
    }


    public String getDescription(TypedObject object)
    {
        T item = getObjectSource(object);
        if(item != null)
        {
            return getItemDescription(item);
        }
        return "";
    }


    public String getDescription(TypedObject object, String languageIso)
    {
        T item = getObjectSource(object);
        if(item != null)
        {
            return getItemDescription(item);
        }
        return "";
    }


    public String getIconPath(TypedObject object)
    {
        T item = getObjectSource(object);
        if(item != null)
        {
            return getIconPath(item);
        }
        return "";
    }


    public String getIconPath(TypedObject object, String languageIso)
    {
        T item = getObjectSource(object);
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


    private T getObjectSource(TypedObject object)
    {
        T source = null;
        Object model = object.getObject();
        if(model != null && !getModelService().isNew(model))
        {
            source = (T)getModelService().getSource(object.getObject());
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Trying to get source on not persisted object");
        }
        return source;
    }
}
