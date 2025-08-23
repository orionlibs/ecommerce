package de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference;

import com.google.common.base.Splitter;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.common.configuration.EditorConfigurationUtil;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorLogic;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRenderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.ext.SelectionControl;

public class ConfigurableMultiReferenceEditor<V, D extends AbstractEditorData> extends AbstractComponentWidgetAdapterAware implements CockpitEditorRenderer<Collection<V>>, MultiReferenceEditorLogic<D, V>
{
    private static final Logger LOG = Logger.getLogger(ConfigurableMultiReferenceEditor.class);
    protected static final String ERROR_NOTIFICATION_ID = "reference.editor.cannot.instantiate.type.selected";
    protected static final String CREATE_BUTTON_LABEL = "configurableMultiReferenceEditor.create";
    protected static final String DATA_HANDLER_PARAM = "dataHandler";
    protected static final String DATA_HANDLER_PARAMETERS_PARAM = "dataHandlerParameters";
    protected static final String ITEM_MASTER_RENDERER_PARAM = "itemMasterRenderer";
    protected static final String ITEM_DETAIL_RENDERER_PARAM = "itemDetailRenderer";
    protected static final String CONTEXT_PARAM = "context";
    protected static final String SORTABLE_PARAM = "sortable";
    protected static final String EDITABLE_COLUMNS_PARAM = "inlineEditingParams";
    protected static final String DEFAULT_ITEM_MASTER_RENDERER = "asMultiReferenceItemMasterRenderer";
    protected static final String DEFAULT_ITEM_DETAIL_RENDERER = "asMultiReferenceItemDetailRenderer";
    protected static final String SEARCH_RESULT = "searchResult";
    protected static final Pattern SPEL_REGEXP = Pattern.compile("\\{((.*))\\}");
    protected static final String EDITOR_SCLASS = "yas-editor";
    protected static final String ITEM_SCLASS = "yas-item";
    protected static final String ITEM_MASTER_SCLASS = "yas-item-master";
    protected static final String ITEM_MASTER_INNER_SCLASS = "yas-item-master-inner";
    protected static final String ITEM_DETAIL_SCLASS = "yas-item-detail";
    protected static final String ITEM_DETAIL_INNER_SCLASS = "yas-item-detail-inner";
    protected static final String IN_SEARCH_RESULT_SCLASS = "yas-in-search-result";
    protected static final String FILTER_SCLASS = "yas-filter";
    protected static final String CREATE_SCLASS = "yas-create";
    protected static final String CREATE_ICON_SCLASS = "z-icon-plus";
    protected static final String SOCKET_OUT_CREATE_REQUEST = "createRequest";
    protected static final String SOCKET_IN_CREATE_RESULT = "createResult";
    protected static final String SOCKET_OUT_UPDATE_REQUEST = "updateRequest";
    protected static final String SOCKET_IN_UPDATE_RESULT = "updateResult";
    protected static final String ITEM_ATTRIBUTE = "asItem";
    protected static final String OPEN_ATTRIBUTE = "asOpen";
    protected static final String EDITOR_DATA_KEY = "editorData";
    protected static final int EDITOR_MAX_SIZE = 20;
    protected static final int LISTBOX_PRELOAD_SIZE_FACTOR = 2;
    private EditorContext<Collection<V>> editorContext;
    private EditorListener<Collection<V>> editorListener;
    private Object parentObject;
    private String parentTypeCode;
    private String context;
    private boolean sortable;
    private Collection<String> columns;
    private Collection<String> editableColumns;
    private DataHandler<D, V> dataHandler;
    private ListModel<D> listModel;
    private EditorRenderer itemMasterRenderer;
    private EditorRenderer itemDetailRenderer;
    @Resource
    private TypeFacade typeFacade;
    @Resource
    private PermissionFacade permissionFacade;
    @Resource
    private PropertyValueService propertyValueService;


    public void render(Component parent, EditorContext<Collection<V>> editorContext, EditorListener<Collection<V>> editorListener)
    {
        setEditorContext(editorContext);
        setEditorListener(editorListener);
        setParentObject(resolveParentObject(parent));
        setParentTypeCode(resolveTypeCode(getParentObject()));
        setContext(resolveContext());
        setSortable(resolveSortable());
        setColumns(resolveColumns());
        setEditableColumns(resolveEditableColumns());
        setDataHandler(createDataHandler());
        setListModel(createListModel());
        setItemMasterRenderer(createItemMasterRenderer());
        setItemDetailRenderer(createItemDetailRenderer());
        renderList(parent);
        addSocketInputEventListener("createResult", event -> createReference(event.getData()));
        addSocketInputEventListener("updateResult", event -> updateReference(event.getData()));
    }


    protected Object resolveParentObject(Component parent)
    {
        return parent.getAttribute("parentObject");
    }


    protected String resolveTypeCode(Object object)
    {
        return this.typeFacade.getType(object);
    }


    protected String resolveContext()
    {
        return (String)this.editorContext.getParameterAs("context");
    }


    protected boolean resolveSortable()
    {
        return Boolean.parseBoolean((String)this.editorContext.getParameterAs("sortable"));
    }


    protected List<String> resolveColumns()
    {
        WidgetInstanceManager widgetInstanceManager = getWidgetInstanceManager();
        ListView columnsConfiguration = (ListView)EditorConfigurationUtil.loadConfiguration(null, widgetInstanceManager, this.context, ListView.class, true);
        if(columnsConfiguration == null)
        {
            return Collections.emptyList();
        }
        return (List<String>)columnsConfiguration.getColumn().stream().map(ListColumn::getQualifier).collect(Collectors.toList());
    }


    protected Collection<String> resolveEditableColumns()
    {
        String editableColumnsConfiguration = ObjectUtils.toString(this.editorContext.getParameter("inlineEditingParams"));
        if(StringUtils.isBlank(editableColumnsConfiguration))
        {
            return Collections.emptyList();
        }
        return Splitter.on(",").splitToList(editableColumnsConfiguration);
    }


    protected boolean canCreate(String typeCode)
    {
        try
        {
            DataType type = this.typeFacade.load(typeCode);
            return (!type.isAbstract() && this.permissionFacade.canCreateTypeInstance(typeCode));
        }
        catch(TypeNotFoundException e)
        {
            LOG.warn("Type not found", (Throwable)e);
            return false;
        }
    }


    protected SearchResultData resolveSearchResult()
    {
        WidgetInstanceManager widgetInstanceManager = getWidgetInstanceManager();
        return (SearchResultData)widgetInstanceManager.getWidgetslot().getViewModel().getValue("searchResult", SearchResultData.class);
    }


    protected void renderList(Component parent)
    {
        Div editorDiv = new Div();
        editorDiv.setParent(parent);
        editorDiv.setSclass("yas-editor " + this.context);
        if(this.listModel.getSize() <= 20)
        {
            Listbox listbox = new Listbox();
            listbox.setParent((Component)editorDiv);
            listbox.setModel(this.listModel);
            listbox.setItemRenderer((item, data, index) -> renderItem(item, (AbstractEditorData)data));
        }
        else
        {
            Div filterDiv = new Div();
            filterDiv.setParent((Component)editorDiv);
            filterDiv.setSclass("yas-filter");
            Textbox filterTextbox = new Textbox();
            filterTextbox.setParent((Component)filterDiv);
            Listbox listbox = new Listbox();
            listbox.setParent((Component)editorDiv);
            listbox.setModel(this.listModel);
            listbox.setItemRenderer((item, data, index) -> renderItem(item, (AbstractEditorData)data));
            listbox.setRows(20);
            listbox.setAttribute("org.zkoss.zul.listbox.rod", Boolean.TRUE);
            listbox.setAttribute("org.zkoss.zul.listbox.initRodSize", Integer.valueOf(20));
            listbox.setAttribute("org.zkoss.zul.listbox.preloadSize", Integer.valueOf(40));
            filterTextbox.addEventListener("onChanging", event -> {
                String filter = ((InputEvent)event).getValue();
                if(StringUtils.isBlank(filter))
                {
                    listbox.setModel(this.listModel);
                    listbox.setRows(20);
                }
                else
                {
                    ListModel<D> filteredListModel = createFilteredListModel(this.listModel, filter);
                    listbox.setModel(filteredListModel);
                    listbox.setRows((filteredListModel.getSize() <= 20) ? 0 : 20);
                }
            });
        }
        Div createDiv = new Div();
        createDiv.setParent((Component)editorDiv);
        createDiv.setSclass("yas-create");
        Button createButton = new Button();
        createButton.setParent((Component)createDiv);
        createButton.setIconSclass("z-icon-plus");
        createButton.setLabel(this.editorContext.getLabel("configurableMultiReferenceEditor.create"));
        createButton.addEventListener("onClick", event -> triggerCreateReference());
    }


    protected void renderItem(Listitem listitem, AbstractEditorData editorData)
    {
        listitem.setValue(editorData);
        Listcell listcell = new Listcell();
        listcell.setParent((Component)listitem);
        Div itemDiv = new Div();
        itemDiv.setParent((Component)listcell);
        itemDiv.setSclass(buildItemStyleClass(editorData));
        itemDiv.setAttribute("asItem", Boolean.TRUE);
        setOpen((Component)itemDiv, editorData.isOpen());
        renderItemMaster((Component)itemDiv, editorData);
        renderItemDetail((Component)itemDiv, editorData);
        if(editorData.isFromSearchConfiguration() && isSortable())
        {
            String typeCode = this.dataHandler.getTypeCode();
            listitem.setDraggable(typeCode);
            listitem.setDroppable(typeCode);
            listitem.addEventListener("onDrop", this::handleDropEvent);
        }
    }


    protected void renderItemMaster(Component item, AbstractEditorData editorData)
    {
        if(this.itemMasterRenderer != null && this.itemMasterRenderer.canRender((EditorLogic)this, item, editorData))
        {
            Div itemMasterDiv = new Div();
            itemMasterDiv.setParent(item);
            itemMasterDiv.setSclass("yas-item-master");
            Div itemMasterInnerDiv = new Div();
            itemMasterInnerDiv.setParent((Component)itemMasterDiv);
            itemMasterInnerDiv.setSclass("yas-item-master-inner");
            this.itemMasterRenderer.beforeRender((EditorLogic)this, (Component)itemMasterInnerDiv, editorData);
            this.itemMasterRenderer.render((EditorLogic)this, (Component)itemMasterInnerDiv, editorData);
            itemMasterDiv.addEventListener("onClick", event -> setOpen(item, !isOpen(item)));
        }
    }


    protected void renderItemDetail(Component item, AbstractEditorData editorData)
    {
        if(this.itemDetailRenderer != null && this.itemDetailRenderer.canRender((EditorLogic)this, item, editorData))
        {
            boolean open = isOpen(item);
            Div itemDetailDiv = new Div();
            itemDetailDiv.setParent(item);
            itemDetailDiv.setSclass("yas-item-detail");
            itemDetailDiv.setVisible(open);
            Div itemDetailInnerDiv = new Div();
            itemDetailInnerDiv.setParent((Component)itemDetailDiv);
            itemDetailInnerDiv.setSclass("yas-item-detail-inner");
            this.itemDetailRenderer.beforeRender((EditorLogic)this, (Component)itemDetailInnerDiv, editorData);
            renderItemDetailHelper((Component)itemDetailInnerDiv, editorData, open);
            item.addEventListener("onOpen", event -> {
                OpenEvent openEvent = (OpenEvent)event;
                itemDetailDiv.setVisible(openEvent.isOpen());
                renderItemDetailHelper((Component)itemDetailInnerDiv, editorData, openEvent.isOpen());
            });
        }
    }


    protected void renderItemDetailHelper(Component parent, AbstractEditorData editorData, boolean open)
    {
        if(open && parent.getChildren().isEmpty())
        {
            this.itemDetailRenderer.render((EditorLogic)this, parent, editorData);
        }
    }


    protected String buildItemStyleClass(AbstractEditorData editorData)
    {
        StringJoiner styleClass = new StringJoiner(" ");
        styleClass.add("yas-item");
        if(editorData.isValid())
        {
            styleClass.add("yas-valid");
        }
        else
        {
            styleClass.add("yas-invalid");
        }
        if(editorData.isFromSearchProfile())
        {
            styleClass.add("yas-from-search-profile");
        }
        if(editorData.isFromSearchConfiguration())
        {
            styleClass.add("yas-from-search-configuration");
        }
        if(editorData.isOverride())
        {
            styleClass.add("yas-override");
        }
        if(editorData.isOverrideFromSearchProfile())
        {
            styleClass.add("yas-override-from-search-profile");
        }
        if(editorData.isInSearchResult())
        {
            styleClass.add("yas-in-search-result");
        }
        return styleClass.toString();
    }


    @ViewEvent(eventName = "onDrop")
    protected void handleDropEvent(Event event)
    {
        DropEvent dropEvent = (DropEvent)event;
        Component dragged = dropEvent.getDragged();
        Component target = dropEvent.getTarget();
        if(!(dragged instanceof Listitem) && !(target instanceof Listitem))
        {
            return;
        }
        Listitem draggedItem = (Listitem)dragged;
        Listitem droppedItem = (Listitem)target;
        AbstractEditorData abstractEditorData1 = (AbstractEditorData)draggedItem.getValue();
        AbstractEditorData abstractEditorData2 = (AbstractEditorData)droppedItem.getValue();
        List<V> value = new ArrayList<>(this.dataHandler.getValue(this.listModel));
        V draggedItemValue = (V)this.dataHandler.getItemValue(abstractEditorData1);
        V droppedItemValue = (V)this.dataHandler.getItemValue(abstractEditorData2);
        int droppedIndex = value.indexOf(droppedItemValue);
        value.remove(draggedItemValue);
        value.add(droppedIndex, draggedItemValue);
        updateValue(value);
    }


    protected ListModel<D> createListModel()
    {
        SearchResultData searchResult = resolveSearchResult();
        Map<String, Object> dataHandlerParameters = createDataHandlerParameters();
        ListModel<D> data = this.dataHandler.loadData((Collection)this.editorContext.getInitialValue(), searchResult, dataHandlerParameters);
        if(data instanceof AbstractListModel)
        {
            ((AbstractListModel)data).setSelectionControl((SelectionControl)new Object(this, (AbstractListModel)data));
        }
        return data;
    }


    protected ListModel<D> createFilteredListModel(ListModel<D> originalListModel, String filter)
    {
        List<D> data = new ArrayList<>();
        for(int index = 0; index < originalListModel.getSize(); index++)
        {
            AbstractEditorData abstractEditorData = (AbstractEditorData)originalListModel.getElementAt(index);
            if(StringUtils.containsIgnoreCase(abstractEditorData.getLabel(), filter))
            {
                data.add((D)abstractEditorData);
            }
        }
        return (ListModel<D>)new ListModelList(data);
    }


    protected DataHandler createDataHandler()
    {
        String dataHandlerId = ObjectUtils.toString(this.editorContext.getParameter("dataHandler"));
        if(StringUtils.isBlank(dataHandlerId))
        {
            return null;
        }
        return (DataHandler)BackofficeSpringUtil.getBean(dataHandlerId);
    }


    protected Map<String, Object> createDataHandlerParameters()
    {
        String configParameters = ObjectUtils.toString(this.editorContext.getParameter("dataHandlerParameters"));
        if(StringUtils.isBlank(configParameters))
        {
            return Collections.emptyMap();
        }
        Map<String, String> parameters = Splitter.on(";").withKeyValueSeparator("=").split(configParameters);
        return (Map<String, Object>)parameters.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> evaluate((String)entry.getValue())));
    }


    protected EditorRenderer createItemMasterRenderer()
    {
        String itemMasterRendererId = ObjectUtils.toString(this.editorContext.getParameter("itemMasterRenderer"));
        if(StringUtils.isBlank(itemMasterRendererId))
        {
            itemMasterRendererId = "asMultiReferenceItemMasterRenderer";
        }
        return (EditorRenderer)BackofficeSpringUtil.getBean(itemMasterRendererId);
    }


    protected EditorRenderer createItemDetailRenderer()
    {
        String itemDetailRendererId = ObjectUtils.toString(this.editorContext.getParameter("itemDetailRenderer"));
        if(StringUtils.isBlank(itemDetailRendererId))
        {
            itemDetailRendererId = "asMultiReferenceItemDetailRenderer";
        }
        return (EditorRenderer)BackofficeSpringUtil.getBean(itemDetailRendererId);
    }


    protected Object evaluate(String value)
    {
        Matcher matcher = SPEL_REGEXP.matcher(value);
        if(matcher.find())
        {
            Map<String, Object> evaluationContext = createEvaluationContext();
            String expression = matcher.group(1);
            Object processedValue = this.propertyValueService.readValue(evaluationContext, expression);
            return (processedValue == null) ? value : processedValue;
        }
        return value;
    }


    protected Map<String, Object> createEvaluationContext()
    {
        Map<String, Object> evaluationContext = new HashMap<>();
        evaluationContext.put("parentObject", getParentObject());
        return evaluationContext;
    }


    public void triggerCreateReference()
    {
        String typeCode = this.dataHandler.getTypeCode();
        if(canCreate(typeCode))
        {
            Map<String, Object> wizardInput = new HashMap<>();
            wizardInput.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), typeCode);
            wizardInput.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT.getName(), getParentObject());
            wizardInput.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT_TYPE.getName(), getParentTypeCode());
            String configurableFlowConfigCtx = MapUtils.getString(this.editorContext.getParameters(), "configurableFlowConfigCtx");
            if(configurableFlowConfigCtx != null)
            {
                wizardInput.put("configurableFlowConfigCtx", configurableFlowConfigCtx);
            }
            sendOutput("createRequest", wizardInput);
        }
        else
        {
            Messagebox.show(Labels.getLabel("reference.editor.cannot.instantiate.type.selected", new Object[] {typeCode}), null, 1, "z-messagebox-icon z-messagebox-exclamation");
        }
    }


    protected void createReference(Object data)
    {
        V newItem = (V)((HashMap)data).get("newItem");
        Collection<V> value = this.dataHandler.getValue(this.listModel);
        value.add(newItem);
        updateValue(value);
    }


    public void triggerUpdateReference(AbstractEditorData editorData)
    {
        TypeAwareSelectionContext<Object> selectionContext = new TypeAwareSelectionContext(this.dataHandler.getTypeCode(), editorData.getModel(), Collections.singletonList(editorData.getModel()));
        selectionContext.addParameter("editorData", editorData);
        sendOutput("updateRequest", selectionContext);
    }


    protected void updateReference(Object data)
    {
        Collection<V> value = this.dataHandler.getValue(this.listModel);
        updateValue(value);
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return (WidgetInstanceManager)this.editorContext.getParameter("wim");
    }


    public boolean isOpen(Component component)
    {
        return Objects.equals(component.getAttribute("asOpen"), Boolean.TRUE);
    }


    public void setOpen(Component component, boolean open)
    {
        if(isOpen(component) != open)
        {
            component.setAttribute("asOpen", Boolean.valueOf(open));
            Events.sendEvent(component, (Event)new OpenEvent("onOpen", component, open));
        }
    }


    public Editor findEditor(Component component)
    {
        for(Component current = component; current != null; current = current.getParent())
        {
            if(current instanceof Editor)
            {
                return (Editor)current;
            }
        }
        return null;
    }


    public Component findEditorItem(Component component)
    {
        for(Component current = component; current != null; current = current.getParent())
        {
            if(Objects.equals(current.getAttribute("asItem"), Boolean.TRUE))
            {
                return current;
            }
        }
        return null;
    }


    public void updateValue(Collection<V> value)
    {
        this.editorListener.onValueChanged(value);
    }


    public void updateAttributeValue(D data, String attributeName, Object attributeValue)
    {
        this.dataHandler.setAttributeValue((AbstractEditorData)data, attributeName, attributeValue);
        updateValue(this.dataHandler.getValue(this.listModel));
    }


    public EditorContext<Collection<V>> getEditorContext()
    {
        return this.editorContext;
    }


    protected void setEditorContext(EditorContext<Collection<V>> editorContext)
    {
        this.editorContext = editorContext;
    }


    public EditorListener<Collection<V>> getEditorListener()
    {
        return this.editorListener;
    }


    protected void setEditorListener(EditorListener<Collection<V>> editorListener)
    {
        this.editorListener = editorListener;
    }


    protected Object getParentObject()
    {
        return this.parentObject;
    }


    protected void setParentObject(Object parentObject)
    {
        this.parentObject = parentObject;
    }


    protected String getParentTypeCode()
    {
        return this.parentTypeCode;
    }


    protected void setParentTypeCode(String parentTypeCode)
    {
        this.parentTypeCode = parentTypeCode;
    }


    public String getContext()
    {
        return this.context;
    }


    protected void setContext(String context)
    {
        this.context = context;
    }


    public boolean isSortable()
    {
        return this.sortable;
    }


    protected void setSortable(boolean sortable)
    {
        this.sortable = sortable;
    }


    public Collection<String> getColumns()
    {
        return this.columns;
    }


    protected void setColumns(Collection<String> columns)
    {
        this.columns = columns;
    }


    public Collection<String> getEditableColumns()
    {
        return this.editableColumns;
    }


    protected void setEditableColumns(Collection<String> editableColumns)
    {
        this.editableColumns = editableColumns;
    }


    public DataHandler<D, V> getDataHandler()
    {
        return this.dataHandler;
    }


    protected void setDataHandler(DataHandler<D, V> dataHandler)
    {
        this.dataHandler = dataHandler;
    }


    public ListModel<D> getListModel()
    {
        return this.listModel;
    }


    protected void setListModel(ListModel<D> listModel)
    {
        this.listModel = listModel;
    }


    public EditorRenderer getItemMasterRenderer()
    {
        return this.itemMasterRenderer;
    }


    protected void setItemMasterRenderer(EditorRenderer itemMasterRenderer)
    {
        this.itemMasterRenderer = itemMasterRenderer;
    }


    public EditorRenderer getItemDetailRenderer()
    {
        return this.itemDetailRenderer;
    }


    protected void setItemDetailRenderer(EditorRenderer itemDetailRenderer)
    {
        this.itemDetailRenderer = itemDetailRenderer;
    }
}
