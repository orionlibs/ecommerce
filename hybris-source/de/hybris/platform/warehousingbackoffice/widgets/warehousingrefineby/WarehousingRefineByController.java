package de.hybris.platform.warehousingbackoffice.widgets.warehousingrefineby;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionDataList;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.warehousingbackoffice.config.impl.jaxb.hybris.warehousingrefineby.FieldList;
import de.hybris.platform.warehousingbackoffice.config.impl.jaxb.hybris.warehousingrefineby.RefineBy;
import de.hybris.platform.warehousingbackoffice.config.impl.jaxb.hybris.warehousingrefineby.SearchValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class WarehousingRefineByController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(WarehousingRefineByController.class);
    public static final String SOCKET_IN_NODE_SELECTED = "nodeSelected";
    public static final String SOCKET_IN_ADVANCED_SEARCH_INIT_CONTEXT = "inputContext";
    public static final String SOCKET_OUT_OUTPUT_CONTEXT = "outputContext";
    public static final String GET_NAME = "getName";
    public static final String WAREHOUSINGBACKOFFICE_REFINE_BY = "warehousingbackoffice-refine-by";
    public static final String GROUP_VALUES_SEPARATOR = "/";
    public static final String REFINE_BY_CONTAINER = "refine-by-container";
    public static final String CURRENT_SEARCH = "currentSearch";
    public static final String TYPE_CODE = "typeCode";
    public static final String CONTEXT = "context";
    public static final String CURRENT_SEARCH_SEPARATOR = "/";
    @Wire
    private Div warehousingFilterContainer;
    @Wire
    private Label emptyFilters;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient FlexibleSearchService flexibleSearchService;
    @WireVariable
    private transient EnumerationService enumerationService;
    private transient RefineBy refineByConfig;


    @SocketEvent(socketId = "nodeSelected")
    public void onNodeSelected(NavigationNode nodeSelected)
    {
        getModel().setValue("currentSearch", null);
        getModel().setValue("typeCode", null);
        getModel().setValue("context", null);
        resetWidget();
    }


    public void initialize(Component comp)
    {
        String typeCode = (String)getModel().getValue("typeCode", String.class);
        if(typeCode != null)
        {
            renderRefineByFromConfig(typeCode);
        }
        getModel().setValue("currentSearch", null);
    }


    @SocketEvent(socketId = "inputContext")
    public void completeAdvancedSearchInitContext(AdvancedSearchInitContext context)
    {
        resetWidget();
        getModel().setValue("context", context);
        getModel().setValue("typeCode", context.getAdvancedSearchData().getTypeCode());
        renderRefineByFromConfig(context.getAdvancedSearchData().getTypeCode());
        sendOutput("outputContext", context);
    }


    protected void addConditionList(AdvancedSearchData searchData, Map<String, List<SearchConditionData>> searchExpressions)
    {
        List<SearchConditionData> searchConditionDataList = new ArrayList<>();
        searchExpressions.entrySet().forEach(entry -> {
            if(CollectionUtils.isNotEmpty(searchData.getConditions((String)entry.getKey())))
            {
                searchData.getConditions((String)entry.getKey()).clear();
            }
            searchConditionDataList.add(SearchConditionDataList.or((List)entry.getValue()));
        });
        searchData.addConditionList(ValueComparisonOperator.AND, searchConditionDataList);
    }


    protected void addSearchCondition(Checkbox checkbox, Map<String, List<SearchConditionData>> searchFieldConditions, AdvancedSearchData advancedSearchData)
    {
        if(!searchFieldConditions.containsKey(checkbox.getName()))
        {
            searchFieldConditions.put(checkbox.getName(), new ArrayList<>());
        }
        DataType dataType = loadDataTypeForCode(advancedSearchData.getTypeCode());
        DataAttribute dataAttribute = dataType.getAttribute(checkbox.getName());
        String searchValue = ((Label)checkbox.getNextSibling()).getValue();
        ArrayList<? super String> searchValues = new ArrayList();
        if(searchValue.contains("/"))
        {
            Collections.addAll(searchValues, searchValue.split("/"));
        }
        else
        {
            searchValues.add(searchValue);
        }
        String currentSearch = (String)getModel().getValue("currentSearch", String.class);
        getModel().setValue("currentSearch",
                        (currentSearch != null) ? (currentSearch + "/" + currentSearch) : checkbox.getLabel());
        FieldType fieldType = new FieldType();
        fieldType.setDisabled(Boolean.TRUE);
        fieldType.setSelected(Boolean.TRUE);
        fieldType.setName(checkbox.getName());
        searchValues.forEach(
                        value -> ((List<SearchConditionData>)searchFieldConditions.get(checkbox.getName())).add(new SearchConditionData(fieldType, getFlexibleSearchService().searchUnique(buildSearchQuery(dataAttribute.getValueType().getCode(), value.toString())), ValueComparisonOperator.EQUALS)));
    }


    protected FlexibleSearchQuery buildSearchQuery(String typeCode, String searchValue)
    {
        return new FlexibleSearchQuery("SELECT {pk} FROM {" + typeCode + "} WHERE {code} = ?code",
                        Collections.singletonMap("code", searchValue));
    }


    protected DataType loadDataTypeForCode(String typeCode)
    {
        if(StringUtils.isNotBlank(typeCode))
        {
            try
            {
                return getTypeFacade().load(typeCode.trim());
            }
            catch(TypeNotFoundException e)
            {
                LOG.error("Could not find type " + typeCode.trim(), (Throwable)e);
            }
        }
        return null;
    }


    protected RefineBy loadRefineByConfiguration(String typeCode)
    {
        DefaultConfigContext context = new DefaultConfigContext("warehousingbackoffice-refine-by", typeCode.trim());
        try
        {
            return (RefineBy)getWidgetInstanceManager().loadConfiguration((ConfigContext)context, RefineBy.class);
        }
        catch(CockpitConfigurationException cce)
        {
            LOG.warn(String.format("Could not load refine by configuration for type [%s] ", new Object[] {typeCode}));
            return null;
        }
    }


    protected void renderCheckbox(String searchFieldName, ArrayList<Object> searchFieldPossibleValues, String typeCode, SearchValue searchFieldValue, Div searchFieldDiv)
    {
        String searchValue, fieldLabel = "";
        if(searchFieldValue.getUniqueValue() != null && StringUtils.isNotEmpty(searchFieldValue.getUniqueValue()))
        {
            searchValue = searchFieldValue.getUniqueValue();
            FlexibleSearchQuery query = buildSearchQuery(typeCode, searchFieldValue.getUniqueValue());
            Object possibleValue = getFlexibleSearchService().searchUnique(query);
            searchFieldPossibleValues.add(possibleValue);
            try
            {
                fieldLabel = StringUtils.isNotEmpty(searchFieldValue.getLabel()) ? Labels.getLabel(searchFieldValue.getLabel()) : possibleValue.getClass().getMethod("getName", new Class[0]).invoke(possibleValue, new Object[0]).toString();
            }
            catch(IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | NullPointerException e)
            {
                LOG.info(String.format("No name defined into the database for the given type for code %s .", new Object[] {searchFieldValue}));
            }
        }
        else
        {
            fieldLabel = Labels.getLabel(searchFieldValue.getLabel());
            StringJoiner joiner = new StringJoiner("/");
            searchFieldValue.getGroupMemberValue().forEach(value -> {
                joiner.add(value.getValue());
                FlexibleSearchQuery query = buildSearchQuery(typeCode, value.getValue());
                searchFieldPossibleValues.add(getFlexibleSearchService().searchUnique(query));
            });
            searchValue = joiner.toString();
        }
        Checkbox checkbox = new Checkbox();
        checkbox.setName(searchFieldName);
        checkbox.setLabel(fieldLabel);
        List<String> checkedBoxes = new ArrayList<>();
        String currentSearch = (String)getModel().getValue("currentSearch", String.class);
        if(currentSearch != null)
        {
            checkedBoxes = Arrays.asList(currentSearch.split("/"));
        }
        checkbox.setChecked(checkedBoxes.contains(fieldLabel));
        checkbox.addEventListener("onCheck", this::submitFilter);
        getEmptyFilters().setVisible(false);
        Label searchFieldLabel = new Label();
        searchFieldLabel.setVisible(false);
        searchFieldLabel.setValue(searchValue);
        searchFieldDiv.appendChild((Component)checkbox);
        searchFieldDiv.appendChild((Component)searchFieldLabel);
        FieldType searchField = new FieldType();
        searchField.setName(searchFieldName);
    }


    protected void renderRefineByFromConfig(String typeCode)
    {
        setRefineByConfig(loadRefineByConfiguration(typeCode));
        if(getRefineByConfig() != null)
        {
            FieldList fieldList = getRefineByConfig().getFieldList();
            DataType dataType = loadDataTypeForCode(typeCode);
            if(dataType != null && fieldList != null)
            {
                fieldList.getSearchField().forEach(searchField -> {
                    Div searchFieldDiv = new Div();
                    Label searchFieldLabel = new Label(Labels.getLabel(searchField.getLabel()));
                    searchFieldDiv.appendChild((Component)searchFieldLabel);
                    String searchFieldName = searchField.getName();
                    List<SearchValue> searchValues = searchField.getSearchValue();
                    ArrayList searchExpressionsList = new ArrayList();
                    DataAttribute dataAttribute = dataType.getAttribute(searchFieldName);
                    if(dataAttribute != null && dataAttribute.isSearchable())
                    {
                        Validate.notNull(String.format("Cannot find attribute = %s for type = %s ", new Object[] {searchFieldName, dataType.getCode()}), new Object[] {dataAttribute});
                        searchValues.forEach(());
                    }
                    getWarehousingFilterContainer().appendChild((Component)searchFieldDiv);
                });
            }
        }
    }


    protected void resetWidget()
    {
        getWarehousingFilterContainer().setId("refine-by-container");
        while(getWarehousingFilterContainer().getChildren().size() > 0)
        {
            Component comp = getWarehousingFilterContainer().getChildren().get(0);
            getWarehousingFilterContainer().removeChild(comp);
        }
        getEmptyFilters().setVisible(true);
    }


    protected void submitFilter(Event event)
    {
        AdvancedSearchInitContext context = (AdvancedSearchInitContext)getModel().getValue("context", AdvancedSearchInitContext.class);
        AdvancedSearchData advancedSearchData = context.getAdvancedSearchData();
        Map<String, List<SearchConditionData>> searchFieldConditions = new HashMap<>();
        List<SearchConditionData> conditions = advancedSearchData.getConditions("_orphanedSearchConditions");
        if(CollectionUtils.isNotEmpty(conditions))
        {
            conditions.clear();
        }
        List<Component> refineBySections = getWarehousingFilterContainer().getChildren();
        List checkboxes = new ArrayList();
        refineBySections.forEach(component -> checkboxes.addAll((Collection)component.getChildren().stream().filter(()).collect(Collectors.toList())));
        getModel().setValue("currentSearch", null);
        checkboxes.stream().filter(component -> ((Checkbox)component).isChecked())
                        .forEach(component -> addSearchCondition((Checkbox)component, searchFieldConditions, advancedSearchData));
        addConditionList(advancedSearchData, searchFieldConditions);
        sendOutput("outputContext", context);
    }


    protected RefineBy getRefineByConfig()
    {
        return this.refineByConfig;
    }


    public void setRefineByConfig(RefineBy refineByConfig)
    {
        this.refineByConfig = refineByConfig;
    }


    protected Div getWarehousingFilterContainer()
    {
        return this.warehousingFilterContainer;
    }


    public void setWarehousingFilterContainer(Div warehousingFilterContainer)
    {
        this.warehousingFilterContainer = warehousingFilterContainer;
    }


    protected Label getEmptyFilters()
    {
        return this.emptyFilters;
    }


    public void setEmptyFilters(Label emptyFilters)
    {
        this.emptyFilters = emptyFilters;
    }


    protected TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }


    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }
}
