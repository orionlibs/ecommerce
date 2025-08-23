package de.hybris.platform.platformbackoffice.editors.membersfindereditor;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.Parameter;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class MembersFinderEditor extends AbstractComponentWidgetAdapterAware implements CockpitEditorRenderer<Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(MembersFinderEditor.class);
    private static final String GROUPS_FIELD = "groups";
    private static final String FINDER_OUTPUT_SOCKET = "finderOutput";
    private static final String HMC_FINDJOBLOGSFORCRONJOB = "hmc.findmembersforusergroup";
    protected static final String PRINCIPAL_SEARCH_EDITOR_NAME = "advanced-search";
    private static final String PRINCIPAL_TYPECODE = "Principal";
    private static final String PARENT_OBJECT = "parentObject";
    private static final String WIM = "wim";


    public void render(Component parent, EditorContext<Object> editorContext, EditorListener<Object> editorListener)
    {
        Div cnt = new Div();
        Button button = createFinderButton();
        button.addEventListener("onClick", event -> {
            AdvancedSearchData searchData = new AdvancedSearchData();
            searchData.setTypeCode("Principal");
            WidgetInstanceManager wim = (WidgetInstanceManager)editorContext.getParameter("wim");
            searchData.setGlobalOperator(ValueComparisonOperator.AND);
            AdvancedSearchInitContext initContext = createSearchContext(searchData, wim, editorContext);
            sendOutput("finderOutput", initContext);
        });
        parent.appendChild((Component)cnt);
        cnt.appendChild((Component)button);
    }


    protected Button createFinderButton()
    {
        return new Button(Labels.getLabel("hmc.findmembersforusergroup"));
    }


    protected AdvancedSearchInitContext createSearchContext(AdvancedSearchData searchData, WidgetInstanceManager wim, EditorContext<Object> editorContext)
    {
        AdvancedSearch originalConfig = loadAdvancedConfiguration(wim, "advanced-search");
        if(originalConfig == null)
        {
            return new AdvancedSearchInitContext(searchData, null);
        }
        AdvancedSearchWrapper advancedSearchWrapper = new AdvancedSearchWrapper(this, originalConfig, Boolean.TRUE);
        for(FieldType field : advancedSearchWrapper.getFieldList().getField())
        {
            if("groups".equals(field.getName()))
            {
                FieldType groupsFieldClone = new FieldType();
                groupsFieldClone.setDisabled(Boolean.TRUE);
                groupsFieldClone.setEditor(field.getEditor());
                groupsFieldClone.setMandatory(Boolean.valueOf(field.isMandatory()));
                groupsFieldClone.setMergeMode(field.getMergeMode());
                groupsFieldClone.setName(field.getName());
                groupsFieldClone.setOperator(field.getOperator());
                groupsFieldClone.setPosition(field.getPosition());
                groupsFieldClone.setSelected(Boolean.valueOf(field.isSelected()));
                groupsFieldClone.setSortable(Boolean.valueOf(field.isSortable()));
                if(CollectionUtils.isNotEmpty(field.getEditorParameter()))
                {
                    groupsFieldClone.getEditorParameter().addAll((Collection)field.getEditorParameter().stream().map(param -> {
                        Parameter paramClone = new Parameter();
                        paramClone.setName(param.getName());
                        paramClone.setValue(param.getValue());
                        return paramClone;
                    }).collect(Collectors.toList()));
                }
                searchData.addCondition(groupsFieldClone, ValueComparisonOperator.CONTAINS, editorContext
                                .getParameter("parentObject"));
                searchData.setIncludeSubtypes(Boolean.TRUE);
            }
        }
        return new AdvancedSearchInitContext(searchData, (AdvancedSearch)advancedSearchWrapper);
    }


    protected AdvancedSearch loadAdvancedConfiguration(WidgetInstanceManager wim, String name)
    {
        DefaultConfigContext context = new DefaultConfigContext(name, "Principal");
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
