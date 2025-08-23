package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchHelper;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.AdvancedSearchConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldGroupConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultSearchFieldGroupConfiguration;
import de.hybris.platform.cockpit.services.config.impl.PropertySearchFieldConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceSelectorPage extends AdvancedSearchPage
{
    private static final Logger LOG = LoggerFactory.getLogger(ResourceSelectorPage.class);


    protected void addPredefinedFields(AdvancedSearchConfiguration config)
    {
        SearchFieldGroupConfiguration rootGroup = config.getRootGroupConfiguration();
        if(this.wizard != null && rootGroup instanceof DefaultSearchFieldGroupConfiguration)
        {
            Map<String, Object> initialValues = (Map<String, Object>)getParameters().get("predefinedPropertyValues");
            if(initialValues != null)
            {
                Map<String, SearchFieldConfiguration> searchFieldMap = getSearchFieldMap(rootGroup
                                .getAllSearchFieldConfigurations());
                for(Map.Entry<String, Object> entry : initialValues.entrySet())
                {
                    PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(entry.getKey());
                    SearchFieldConfiguration field = searchFieldMap.get(propertyDescriptor.getQualifier().toLowerCase());
                    if(field == null)
                    {
                        ((DefaultSearchFieldGroupConfiguration)rootGroup)
                                        .addSearchFieldConfiguration((SearchFieldConfiguration)new PropertySearchFieldConfiguration((PropertyDescriptor)UISessionUtils.getCurrentSession()
                                                        .getSearchService().getSearchDescriptor(propertyDescriptor)));
                        continue;
                    }
                    field.setVisible(true);
                }
            }
        }
    }


    protected void addPredefinedValues(DefaultAdvancedSearchModel searchModel)
    {
        if(this.wizard != null && MapUtils.isNotEmpty(getParameters()))
        {
            Map<String, Object> initialValues = (Map<String, Object>)getParameters().get("predefinedPropertyValues");
            if(initialValues != null)
            {
                for(Map.Entry<String, Object> entry : initialValues.entrySet())
                {
                    PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(entry.getKey());
                    SearchField field = searchModel.getSearchField(propertyDescriptor);
                    if(field != null)
                    {
                        try
                        {
                            searchModel.getParameterContainer().put(field,
                                            AdvancedSearchHelper.createSimpleConditionValue(TypeTools.container2Item(getTypeService(), entry
                                                            .getValue())));
                        }
                        catch(Exception e)
                        {
                            LOG.error("error prefilling condition value for search field [" + field + "]", e);
                        }
                    }
                }
            }
        }
    }
}
