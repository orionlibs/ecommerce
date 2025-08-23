package de.hybris.platform.commerceservices.backoffice.editor;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class ConsignmentsFinderEditor extends AbstractComponentWidgetAdapterAware implements CockpitEditorRenderer<Object>
{
    private static final Logger LOG = Logger.getLogger(ConsignmentsFinderEditor.class);
    private static final String OUTPUT_SOCKET = "finderOutput";
    private static final String CONSIGNMENT_TYPE_CODE = "Consignment";
    private static final String CONSIGNMENT_SEARCH_EDITOR_NAME = "consignment-advanced-search";


    public void render(Component parent, EditorContext<Object> warehouseEditorContext, EditorListener<Object> warehouseEditorListener)
    {
        Div cnt = new Div();
        Button button = new Button(Labels.getLabel("hmc.findconsignmentsforwarehouse"));
        button.addEventListener("onClick", (EventListener)new Object(this, warehouseEditorContext));
        parent.appendChild((Component)cnt);
        cnt.appendChild((Component)button);
    }


    protected AdvancedSearchInitContext createSearchContext(AdvancedSearchData searchData, WidgetInstanceManager wim, EditorContext<Object> warehouseEditorContext)
    {
        AdvancedSearch config = loadAdvancedConfiguration(wim);
        for(FieldType field : config.getFieldList().getField())
        {
            if("warehouse".equals(field.getName()))
            {
                searchData.addCondition(field, ValueComparisonOperator.EQUALS, warehouseEditorContext.getParameter("parentObject"));
                field.setDisabled(Boolean.TRUE);
            }
            if("order".equals(field.getName()))
            {
                searchData.addCondition(field, ValueComparisonOperator.IS_NOT_EMPTY, null);
            }
        }
        return new AdvancedSearchInitContext(searchData, config);
    }


    protected AdvancedSearch loadAdvancedConfiguration(WidgetInstanceManager wim)
    {
        DefaultConfigContext context = new DefaultConfigContext("consignment-advanced-search", "Consignment");
        try
        {
            return (AdvancedSearch)wim.loadConfiguration((ConfigContext)context, AdvancedSearch.class);
        }
        catch(CockpitConfigurationException cce)
        {
            LOG.error("Failed to load advanced configuration.", (Throwable)cce);
            return null;
        }
    }
}
