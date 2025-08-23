package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.gridview.impl.GridValueHolder;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.zkoss.spring.SpringUtil;

public class MultiTypeValueHandler implements ValueHandler
{
    private final UIConfigurationService uiConfService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
    private final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
    private final String qualifier;


    public MultiTypeValueHandler(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public Object getValue(TypedObject item) throws ValueHandlerException
    {
        GridViewConfiguration gridViewConfig = (GridViewConfiguration)this.uiConfService.getComponentConfiguration(this.typeService.getBestTemplate(item), "gridView", GridViewConfiguration.class);
        GridValueHolder gridValueHolder = new GridValueHolder(gridViewConfig, item);
        if("code".equals(this.qualifier))
        {
            return gridValueHolder.getLabel();
        }
        if("previewimage".equals(this.qualifier))
        {
            return gridValueHolder.getImageURL();
        }
        if("shortinfo".equals(this.qualifier))
        {
            return gridValueHolder.getShortInfo();
        }
        if("description".equals(this.qualifier))
        {
            return gridValueHolder.getDescription();
        }
        if("type".equals(this.qualifier))
        {
            return item.getType().getName();
        }
        return null;
    }


    public Object getValue(TypedObject item, String languageIso) throws ValueHandlerException
    {
        return null;
    }


    public void setValue(TypedObject item, Object value) throws ValueHandlerException
    {
    }


    public void setValue(TypedObject item, Object value, String languageIso) throws ValueHandlerException
    {
    }
}
