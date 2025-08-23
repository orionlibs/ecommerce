package de.hybris.platform.cockpit.services.label;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public abstract class CatalogAwareLabelProvider<T> extends AbstractObjectLabelProvider<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogAwareLabelProvider.class);


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
        T item = getObjectSource(object);
        if(item != null)
        {
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
            CatalogVersion catversion = getCatalogVersion(item);
            if(catversion != null)
            {
                return "/" + labelService.getObjectTextLabel(typeService.wrapItem(catversion));
            }
        }
        return "";
    }


    protected abstract CatalogVersion getCatalogVersion(T paramT);


    private T getObjectSource(TypedObject object)
    {
        T source = null;
        if(getModelService().isNew(object.getObject()))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Trying to get source on not persisted object");
            }
        }
        else
        {
            source = (T)getModelService().getSource(object.getObject());
        }
        return source;
    }
}
