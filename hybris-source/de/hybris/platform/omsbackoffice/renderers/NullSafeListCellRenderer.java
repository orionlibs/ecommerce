package de.hybris.platform.omsbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Listcell;

@Deprecated(since = "1905", forRemoval = true)
public class NullSafeListCellRenderer implements WidgetComponentRenderer<Listcell, ListColumn, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(NullSafeListCellRenderer.class);
    private WidgetComponentRenderer<Listcell, ListColumn, Object> defaultListCellRenderer;
    private NestedAttributeUtils nestedAttributeUtils;


    public void render(Listcell parent, ListColumn columnConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        String qualifier = columnConfiguration.getQualifier();
        Object nestedObject = object;
        Object targetField = object;
        try
        {
            List<String> tokenMap = getNestedAttributeUtils().splitQualifier(qualifier);
            for(int i = 0; i < tokenMap.size() - 1; i++)
            {
                nestedObject = getNestedAttributeUtils().getNestedObject(nestedObject, tokenMap.get(i));
            }
            for(String aTokenMap : tokenMap)
            {
                targetField = getNestedAttributeUtils().getNestedObject(targetField, aTokenMap);
            }
            if(nestedObject == null || targetField == null || checkIfObjectIsEmptyCollection(targetField))
            {
                LOG.info("Either Property {} is null or the field {} is null, skipping render of {}", new Object[] {nestedObject, qualifier, qualifier});
            }
            else
            {
                getDefaultListCellRenderer().render(parent, columnConfiguration, object, dataType, widgetInstanceManager);
            }
        }
        catch(InvalidNestedAttributeException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            LOG.error(e.getMessage(), e);
        }
    }


    protected WidgetComponentRenderer<Listcell, ListColumn, Object> getDefaultListCellRenderer()
    {
        return this.defaultListCellRenderer;
    }


    @Required
    public void setDefaultListCellRenderer(WidgetComponentRenderer<Listcell, ListColumn, Object> defaultListCellRenderer)
    {
        this.defaultListCellRenderer = defaultListCellRenderer;
    }


    protected boolean checkIfObjectIsEmptyCollection(Object object)
    {
        return (object instanceof Collection && CollectionUtils.isEmpty((Collection)object));
    }


    protected NestedAttributeUtils getNestedAttributeUtils()
    {
        return this.nestedAttributeUtils;
    }


    @Required
    public void setNestedAttributeUtils(NestedAttributeUtils nestedAttributeUtils)
    {
        this.nestedAttributeUtils = nestedAttributeUtils;
    }
}
