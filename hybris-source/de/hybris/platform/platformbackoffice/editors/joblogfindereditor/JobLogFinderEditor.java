package de.hybris.platform.platformbackoffice.editors.joblogfindereditor;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class JobLogFinderEditor extends AbstractComponentWidgetAdapterAware implements CockpitEditorRenderer<Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(JobLogFinderEditor.class);
    private static final String CRON_JOB_FIELD = "cronJob";
    private static final String FINDER_OUTPUT_SOCKET = "finderOutput";
    private static final String HMC_FINDJOBLOGSFORCRONJOB = "hmc.findjoblogsforcronjob";
    private static final String HMC_TYPENODE_JOBLOG = "hmc_typenode_joblog";
    private static final String JOB_LOG_SEARCH_EDITOR_NAME = "joblog-advanced-search";
    private static final String JOB_LOG_TYPECODE = "JobLog";
    private static final String NODE_OUTPUT_SOCKET = "nodeOutput";
    private static final String PARENT_OBJECT = "parentObject";
    private static final String WIM = "wim";


    public void render(Component parent, EditorContext<Object> editorContext, EditorListener<Object> editorListener)
    {
        Div cnt = new Div();
        Button button = createFinderButton();
        button.addEventListener("onClick", event -> {
            AdvancedSearchData searchData = new AdvancedSearchData();
            searchData.setTypeCode("JobLog");
            WidgetInstanceManager wim = (WidgetInstanceManager)editorContext.getParameter("wim");
            searchData.setGlobalOperator(ValueComparisonOperator.AND);
            AdvancedSearchInitContext initContext = createSearchContext(searchData, wim, editorContext);
            sendOutput("finderOutput", initContext);
            sendOutput("nodeOutput", new TreeNodeSelector("hmc_typenode_joblog", false));
        });
        parent.appendChild((Component)cnt);
        cnt.appendChild((Component)button);
    }


    private Button createFinderButton()
    {
        return new Button(Labels.getLabel("hmc.findjoblogsforcronjob"));
    }


    private AdvancedSearchInitContext createSearchContext(AdvancedSearchData searchData, WidgetInstanceManager wim, EditorContext<Object> editorContext)
    {
        AdvancedSearch config = loadAdvancedConfiguration(wim, "joblog-advanced-search");
        if(config != null)
        {
            config.setDisableSimpleSearch(Boolean.TRUE);
            for(FieldType field : config.getFieldList().getField())
            {
                if("cronJob".equals(field.getName()))
                {
                    searchData.addCondition(field, ValueComparisonOperator.EQUALS, editorContext.getParameter("parentObject"));
                    field.setDisabled(Boolean.TRUE);
                }
            }
        }
        return new AdvancedSearchInitContext(searchData, config);
    }


    private AdvancedSearch loadAdvancedConfiguration(WidgetInstanceManager wim, String name)
    {
        DefaultConfigContext context = new DefaultConfigContext(name, "JobLog");
        try
        {
            return (AdvancedSearch)wim.loadConfiguration((ConfigContext)context, AdvancedSearch.class);
        }
        catch(CockpitConfigurationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not find Advanced Search configuration.", (Throwable)e);
            }
            return null;
        }
    }
}
