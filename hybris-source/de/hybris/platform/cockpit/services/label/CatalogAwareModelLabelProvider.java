package de.hybris.platform.cockpit.services.label;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CatalogAwareModelLabelProvider<T> extends AbstractModelLabelProvider<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogAwareModelLabelProvider.class);


    public String getLabel(TypedObject object)
    {
        String label = super.getLabel(object);
        return (label.length() > 0) ? (label + label) : "";
    }


    public String getLabel(TypedObject object, String languageIso)
    {
        String label = super.getLabel(object, languageIso);
        return (label.length() > 0) ? (label + label) : "";
    }


    private String getCatalogVersionLabel(TypedObject object)
    {
        T itemModel = getObjectSource(object);
        if(itemModel != null)
        {
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
            CatalogVersionModel catversion = getCatalogVersionModel(itemModel);
            if(catversion != null)
            {
                return "/" + labelService.getObjectTextLabelForTypedObject(typeService.wrapItem(catversion));
            }
        }
        return "";
    }


    protected abstract CatalogVersionModel getCatalogVersionModel(T paramT);


    private T getObjectSource(TypedObject object)
    {
        T source = null;
        if(getModelService().isNew(object.getObject()))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Trying to get model on not persisted typed object");
            }
        }
        else
        {
            source = (T)object.getObject();
        }
        return source;
    }
}
