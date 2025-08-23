package de.hybris.platform.y2ysync.backoffice.editors;

import com.hybris.backoffice.navigation.TreeNodeSelector;
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
import de.hybris.platform.servicelayer.exceptions.SystemException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class Y2YSyncMediaFinderEditor extends AbstractComponentWidgetAdapterAware implements CockpitEditorRenderer<Object>
{
    private static final String FINDER_OUTPUT_SOCKET = "finderOutput";
    private static final String NODE_OUTPUT_SOCKET = "nodeOutput";
    private static final String SYNC_IMPEX_MEDIA_TYPECODE = "SyncImpExMedia";
    private static final String WIM = "wim";
    private static final String HMC_TYPENODE_SYNCIMPEXMEDIA = "hmc_typenode_syncimpexmedia";
    private static final String SYNC_EXECUTION_ID_FIELD = "syncExecutionId";
    private static final String JOB_LOG_SEARCH_EDITOR_NAME = "SyncImpExMedia-advanced-search";
    private static final String HMC_FINDY2YSYNCMEDIAFORCRONJOB = "hmc.findy2ysyncmediaforcronjob";


    public void render(Component parent, EditorContext<Object> editorContext, EditorListener<Object> editorListener)
    {
        Div cnt = new Div();
        Button button = createFinderButton();
        button.addEventListener("onClick", event -> {
            AdvancedSearchData searchData = new AdvancedSearchData();
            searchData.setTypeCode("SyncImpExMedia");
            WidgetInstanceManager wim = (WidgetInstanceManager)editorContext.getParameter("wim");
            searchData.setGlobalOperator(ValueComparisonOperator.AND);
            AdvancedSearchInitContext initContext = createSearchContext(searchData, wim);
            sendOutput("finderOutput", initContext);
            sendOutput("nodeOutput", new TreeNodeSelector("hmc_typenode_syncimpexmedia", false));
        });
        parent.appendChild((Component)cnt);
        cnt.appendChild((Component)button);
    }


    private Button createFinderButton()
    {
        return new Button(Labels.getLabel("hmc.findy2ysyncmediaforcronjob"));
    }


    private AdvancedSearchInitContext createSearchContext(AdvancedSearchData searchData, WidgetInstanceManager wim)
    {
        AdvancedSearch config = loadAdvancedConfiguration(wim, "SyncImpExMedia-advanced-search");
        config.setDisableSimpleSearch(Boolean.valueOf(true));
        for(FieldType field : config.getFieldList().getField())
        {
            if("syncExecutionId".equals(field.getName()))
            {
                field.setDisabled(Boolean.TRUE);
            }
        }
        return new AdvancedSearchInitContext(searchData, config);
    }


    private AdvancedSearch loadAdvancedConfiguration(WidgetInstanceManager wim, String name)
    {
        DefaultConfigContext context = new DefaultConfigContext(name, "SyncImpExMedia");
        try
        {
            return (AdvancedSearch)wim.loadConfiguration((ConfigContext)context, AdvancedSearch.class);
        }
        catch(CockpitConfigurationException cce)
        {
            throw new SystemException("Failed to read cockpit configuration", cce);
        }
    }
}
