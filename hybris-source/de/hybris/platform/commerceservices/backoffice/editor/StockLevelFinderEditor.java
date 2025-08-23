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
import de.hybris.platform.commons.renderer.exceptions.RendererException;
import de.hybris.platform.core.model.product.ProductModel;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class StockLevelFinderEditor extends AbstractComponentWidgetAdapterAware implements CockpitEditorRenderer<Object>
{
    private static final Logger LOG = Logger.getLogger(StockLevelFinderEditor.class);
    private static final String STOCK_LEVEL_SEARCH_FIELD = "stockLevelSearchField";
    private static final String OUTPUT_SOCKET = "finderOutput";
    private static final String STOCK_LEVEL_TYPE_CODE = "StockLevel";
    private static final String WAREHOUSE_SEARCH_EDITOR_NAME = "warehouse-advanced-search";
    private static final String PRODUCTCODE_SEARCH_EDITOR_NAME = "productcode-advanced-search";


    public void render(Component parent, EditorContext<Object> warehouseEditorContext, EditorListener<Object> warehouseEditorListener)
    {
        Object stockLevelSearchField = warehouseEditorContext.getParameter("stockLevelSearchField");
        if(stockLevelSearchField == null)
        {
            throw new RendererException("No parameter stockLevelSearchField found");
        }
        Div cnt = new Div();
        SearchFieldType fieldType = SearchFieldType.valueOf(stockLevelSearchField.toString().toUpperCase());
        Button button = createFinderButton(fieldType);
        button.addEventListener("onClick", (EventListener)new Object(this, warehouseEditorContext, fieldType));
        parent.appendChild((Component)cnt);
        cnt.appendChild((Component)button);
    }


    protected Button createFinderButton(SearchFieldType fieldType)
    {
        if(SearchFieldType.WAREHOUSE == fieldType)
        {
            return new Button(Labels.getLabel("hmc.findstocklevelsforwarehouse"));
        }
        if(SearchFieldType.PRODUCT == fieldType)
        {
            return new Button(Labels.getLabel("hmc.findstocklevelsforproduct"));
        }
        return null;
    }


    protected AdvancedSearchInitContext createSearchContext(AdvancedSearchData searchData, SearchFieldType type, WidgetInstanceManager wim, EditorContext<Object> warehouseEditorContext)
    {
        if(SearchFieldType.WAREHOUSE == type)
        {
            AdvancedSearch config = loadAdvancedConfiguration(wim, "warehouse-advanced-search");
            for(FieldType field : config.getFieldList().getField())
            {
                if("warehouse".equals(field.getName()))
                {
                    searchData
                                    .addCondition(field, ValueComparisonOperator.EQUALS, warehouseEditorContext.getParameter("parentObject"));
                    field.setDisabled(Boolean.TRUE);
                }
                if("productCode".equals(field.getName()))
                {
                    searchData.addCondition(field, ValueComparisonOperator.STARTS_WITH, "");
                }
            }
            return new AdvancedSearchInitContext(searchData, config);
        }
        if(SearchFieldType.PRODUCT == type)
        {
            AdvancedSearch config = loadAdvancedConfiguration(wim, "productcode-advanced-search");
            for(FieldType field : config.getFieldList().getField())
            {
                if("productCode".equals(field.getName()))
                {
                    searchData.addCondition(field, ValueComparisonOperator.EQUALS, ((ProductModel)warehouseEditorContext
                                    .getParameter("parentObject")).getCode());
                    field.setDisabled(Boolean.TRUE);
                }
                if("warehouse".equals(field.getName()))
                {
                    searchData.addCondition(field, ValueComparisonOperator.IS_NOT_EMPTY, null);
                }
            }
            return new AdvancedSearchInitContext(searchData, config);
        }
        return null;
    }


    protected AdvancedSearch loadAdvancedConfiguration(WidgetInstanceManager wim, String name)
    {
        DefaultConfigContext context = new DefaultConfigContext(name, "StockLevel");
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
