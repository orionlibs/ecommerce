package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.GridProperty;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.gridview.GridView;
import de.hybris.platform.cockpit.services.config.jaxb.gridview.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.gridview.Property;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridViewConfigurationFactory extends JAXBBasedUIComponentConfigurationFactory<GridViewConfiguration, GridView>
{
    protected GridViewConfiguration createUIComponent(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, GridView xmlGridView)
    {
        DefaultGridviewConfiguration ret = new DefaultGridviewConfiguration();
        ret.setLabelProperty(createGridProperty(xmlGridView.getLabelslot().getProperty()));
        ret.setDescriptionProperty(createGridProperty(xmlGridView.getDescriptionslot().getProperty()));
        ret.setImageURLProperty(createGridProperty(xmlGridView.getImageslot().getProperty()));
        ret.setShortInfoProperty(createGridProperty(xmlGridView.getShortinfoslot().getProperty()));
        ret.setActionSpringBeanID(xmlGridView.getActionslot().getSpringBean());
        if(xmlGridView.getSpecialactionslot() != null)
        {
            ret.setSpecialactionSpringBeanID(xmlGridView.getSpecialactionslot().getSpringBean());
        }
        return (GridViewConfiguration)ret;
    }


    protected GridProperty createGridProperty(Property property)
    {
        return (GridProperty)new MyGridProperty(this, property.getQualifier(), property.getPrefix(), createPropertyParameter(property.getParameter()));
    }


    private Map<String, String> createPropertyParameter(List<Parameter> parameters)
    {
        if(parameters == null || parameters.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        for(Parameter param : parameters)
        {
            result.put(param.getName(), param.getValue());
        }
        return result;
    }


    public UIComponentConfiguration createDefault(ObjectTemplate objectTemplate)
    {
        DefaultGridviewConfiguration ret = new DefaultGridviewConfiguration();
        ret.setLabelProperty((GridProperty)new MyGridProperty(this, "Item.pk"));
        return (UIComponentConfiguration)ret;
    }


    public Class getComponentClass()
    {
        return GridViewConfiguration.class;
    }
}
