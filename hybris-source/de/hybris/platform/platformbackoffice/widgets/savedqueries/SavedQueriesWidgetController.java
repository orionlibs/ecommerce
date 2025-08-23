package de.hybris.platform.platformbackoffice.widgets.savedqueries;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.MessageboxUtils;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import de.hybris.platform.platformbackoffice.services.BackofficeSavedQueriesService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class SavedQueriesWidgetController extends DefaultWidgetController
{
    public static final String SOCKET_OUT_ADV_SEARCH_INIT_CTX = "advancedSearchInitContext";
    public static final String SETTING_SHARED_QUERIES_INITIALLY_VISIBLE = "sharedQueriesInitiallyVisible";
    private static final Logger LOG = LoggerFactory.getLogger(SavedQueriesWidgetController.class);
    private static final String SCLASS_DELETE_BUTTON = "ye-delete-btn";
    private static final String SCLASS_CELL_NAME = "cell-name";
    private static final String SCLASS_CELL_BTN = "cell-btn";
    private static final int RECENTLY_ADDED_TIMEOUT = 3000;
    private static final String NOTIFICATION_EVENT_LOAD_QUERY = "UseSavedQuery";
    @Wire
    private Listbox listbox;
    @Wire
    private Toolbarbutton showQueriesFilterBtn;
    @Wire
    private Textbox filterTextbox;
    @Wire
    private Checkbox sharedCheckBox;
    @WireVariable
    private transient BackofficeSavedQueriesService backofficeSavedQueriesService;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @WireVariable
    private transient ObjectFacade objectFacade;
    @WireVariable
    private transient LabelService labelService;
    @WireVariable
    private transient UserService userService;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient ModelService modelService;
    private BackofficeSavedQueryModel recentlyAdded;
    private ListModelList<BackofficeSavedQueryModel> listModel;
    private List<BackofficeSavedQueryModel> allQueries;


    public void initialize(Component comp)
    {
        super.initialize(comp);
        initializeListBox();
        initializeQueriesFilter();
        fetchSavedQueriesAsync();
    }


    protected void initializeListBox()
    {
        UserModel currentUser = this.userService.getCurrentUser();
        this.listbox.setEmptyMessage(getLabel("savedqueries.empty.list"));
        this.listbox.setItemRenderer((ListitemRenderer)new Object(this, currentUser));
        this.listbox.addEventListener("onSelect", event -> {
            Listitem selectedItem = this.listbox.getSelectedItem();
            if(selectedItem != null && selectedItem.getValue() != null)
            {
                sendInitContext((BackofficeSavedQueryModel)selectedItem.getValue());
            }
        });
    }


    protected String getSavedQueryNameWithFallback(BackofficeSavedQueryModel queryModel)
    {
        String name = queryModel.getName();
        if(StringUtils.isBlank(name))
        {
            name = getLabel("savedqueries.queryfortype", new Object[] {this.labelService
                            .getObjectLabel(queryModel.getTypeCode())});
        }
        return name;
    }


    protected void highlightRecentlyAdded(Listitem listitem, BackofficeSavedQueryModel queryModel)
    {
        if(Objects.equals(queryModel, this.recentlyAdded))
        {
            this.recentlyAdded = null;
            String uuid = listitem.getUuid();
            String cssClassName = "yw-savedqueries-recently-added-item";
            String script = String.format("jq('#%s').addClass('%s').delay(%d).queue(function(nxt) { $(this).removeClass('%s'); nxt(); })", new Object[] {uuid, "yw-savedqueries-recently-added-item",
                            Integer.valueOf(3000), "yw-savedqueries-recently-added-item"});
            Clients.evalJavaScript(script);
        }
    }


    protected void initializeListModel()
    {
        initializeListModel(this.filterTextbox.getText());
    }


    protected void initializeListModel(String filterText)
    {
        List<BackofficeSavedQueryModel> queries = filterQueries(filterText);
        this.listModel = new ListModelList((queries != null) ? queries : new ArrayList());
        this.listbox.setModel((ListModel)this.listModel);
        this.listbox.invalidate();
    }


    protected void initializeQueriesFilter()
    {
        this.sharedCheckBox.setChecked(getWidgetSettings().getBoolean("sharedQueriesInitiallyVisible"));
        this.sharedCheckBox.addEventListener("onCheck", event -> initializeListModel());
        this.filterTextbox.addEventListener("onChanging", event -> initializeListModel(((InputEvent)event).getValue()));
    }


    protected List<BackofficeSavedQueryModel> filterQueries(String filter)
    {
        String filterLowerCase = filter.toLowerCase();
        boolean hasFilterText = StringUtils.isNotBlank(filterLowerCase);
        boolean sharedVisible = this.sharedCheckBox.isChecked();
        if(getAllQueries() != null && (hasFilterText || !sharedVisible))
        {
            UserModel userModel = this.userService.getCurrentUser();
            Predicate<BackofficeSavedQueryModel> sharedFilter = query -> (sharedVisible || Objects.equals(query.getQueryOwner(), userModel));
            Predicate<BackofficeSavedQueryModel> textFilter = query -> (!hasFilterText || query.getName().toLowerCase().contains(filterLowerCase));
            return (List<BackofficeSavedQueryModel>)getAllQueries().stream().filter(textFilter.and(sharedFilter)).collect(Collectors.toList());
        }
        return getAllQueries();
    }


    protected void sendInitContext(BackofficeSavedQueryModel selectedQuery)
    {
        try
        {
            BackofficeSavedQueryModel savedQuery = (BackofficeSavedQueryModel)this.objectFacade.reload(selectedQuery);
            AdvancedSearchInitContext context = this.backofficeSavedQueriesService.getAdvancedSearchInitContext(savedQuery);
            context.addAttribute("disableSimpleSearch", Boolean.valueOf(true));
            Optional.<Map>ofNullable(selectedQuery.getSavedQueriesParameters()).filter(map -> !map.isEmpty())
                            .ifPresent(map -> context.addAttribute("initParameters", map));
            Collection<String> notConvertedAttributes = (Collection)context.getAttribute("notConvertedAttributeValues");
            if(CollectionUtils.isNotEmpty(notConvertedAttributes))
            {
                showMissingAttributesMessage(savedQuery, notConvertedAttributes);
            }
            else
            {
                sendOutput("advancedSearchInitContext", context);
            }
        }
        catch(ObjectNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Cannot open saved query", (Throwable)e);
            }
            getNotificationService().notifyUser(getNotificationSource(), "UseSavedQuery", NotificationEvent.Level.WARNING, new Object[] {getSavedQueryNameWithFallback(selectedQuery)});
            handleObjectsDeletedEventData(selectedQuery);
        }
        this.listbox.clearSelection();
    }


    protected String getNotificationSource()
    {
        return (getWidgetInstanceManager() != null) ? getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()) :
                        "unknown";
    }


    protected void showRemoveConfirmationMessage(BackofficeSavedQueryModel queryModel)
    {
        String msg = getLabel("savedqueries.remove.confirmation", (Object[])new String[] {queryModel
                        .getName()});
        Messagebox.show(msg, getLabel("savedqueries.remove.title"), new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.NO}, "z-messagebox-icon z-messagebox-question", event -> {
            if(Messagebox.Button.YES.equals(event.getButton()))
            {
                this.modelService.remove(queryModel);
                this.cockpitEventQueue.publishEvent((CockpitEvent)new DefaultCockpitEvent("objectsDeleted", queryModel, null));
            }
        });
    }


    protected void showMissingAttributesMessage(BackofficeSavedQueryModel queryModel, Collection<String> missingAttributes)
    {
        String typeCode = queryModel.getTypeCode();
        List<String> localizedAttributes = (List<String>)missingAttributes.stream().map(attribute -> this.labelService.getObjectLabel(String.format("%s.%s", new Object[] {typeCode, attribute}))).collect(Collectors.toList());
        String title = getLabel("savedqueries.conditions.not.found.title");
        if(Objects.equals(this.userService.getCurrentUser(), queryModel.getQueryOwner()))
        {
            String msg = getLabel("savedqueries.conditions.not.found.with.remove.msg", new Object[] {queryModel
                            .getName(), this.labelService.getObjectLabel(typeCode), localizedAttributes.toString()});
            Messagebox.show(msg, title, MessageboxUtils.order(new Messagebox.Button[] {Messagebox.Button.NO, Messagebox.Button.YES}, ), "z-messagebox-icon z-messagebox-exclamation", event -> {
                if(Messagebox.Button.YES.equals(event.getButton()))
                {
                    this.objectFacade.delete(queryModel);
                }
            });
        }
        else
        {
            String msg = getLabel("savedqueries.conditions.not.found.msg", new Object[] {queryModel
                            .getName(), this.labelService.getObjectLabel(typeCode), localizedAttributes.toString()});
            Messagebox.show(msg, title, 1, "z-messagebox-icon z-messagebox-exclamation");
        }
    }


    protected void fetchSavedQueriesAsync()
    {
        executeOperation((Operation)new Object(this), event -> {
            this.allQueries = new LinkedList<>((Collection<? extends BackofficeSavedQueryModel>)event.getData());
            this.showQueriesFilterBtn.setDisabled(false);
            initializeListModel();
        } null);
    }


    @GlobalCockpitEvent(eventName = "objectCreated", scope = "session")
    public void handleObjectCreateEvent(CockpitEvent event)
    {
        if(event.getData() instanceof BackofficeSavedQueryModel)
        {
            BackofficeSavedQueryModel data = (BackofficeSavedQueryModel)event.getData();
            if(!this.objectFacade.isNew(data))
            {
                this.recentlyAdded = data;
                this.allQueries.add(0, this.recentlyAdded);
                initializeListModel();
            }
        }
    }


    @GlobalCockpitEvent(eventName = "objectsUpdated", scope = "session")
    public void handleObjectUpdatedEvent(CockpitEvent event)
    {
        if(event != null)
        {
            Objects.requireNonNull(BackofficeSavedQueryModel.class);
            Objects.requireNonNull(BackofficeSavedQueryModel.class);
            event.getDataAsCollection().stream().filter(BackofficeSavedQueryModel.class::isInstance).map(BackofficeSavedQueryModel.class::cast).forEach(this::handleSavedQueryUpdatedEvent);
        }
    }


    protected void handleSavedQueryUpdatedEvent(BackofficeSavedQueryModel query)
    {
        if(query != null)
        {
            if(this.allQueries.remove(query))
            {
                this.allQueries.add(query);
            }
            else
            {
                this.recentlyAdded = query;
                this.allQueries.add(0, this.recentlyAdded);
            }
            initializeListModel();
        }
    }


    @GlobalCockpitEvent(eventName = "objectsDeleted", scope = "session")
    public void handleObjectsDeletedEvent(CockpitEvent event)
    {
        if(event != null)
        {
            Objects.requireNonNull(BackofficeSavedQueryModel.class);
            Objects.requireNonNull(BackofficeSavedQueryModel.class);
            event.getDataAsCollection().stream().filter(BackofficeSavedQueryModel.class::isInstance).map(BackofficeSavedQueryModel.class::cast).forEach(this::handleObjectsDeletedEventData);
        }
    }


    protected void handleObjectsDeletedEventData(BackofficeSavedQueryModel removedQuery)
    {
        if(removedQuery != null && this.allQueries.remove(removedQuery))
        {
            this.listModel.remove(removedQuery);
        }
    }


    public List<BackofficeSavedQueryModel> getAllQueries()
    {
        if(this.allQueries == null)
        {
            this.allQueries = new LinkedList<>();
        }
        return this.allQueries;
    }


    public Listbox getListbox()
    {
        return this.listbox;
    }


    public void setListbox(Listbox listbox)
    {
        this.listbox = listbox;
    }


    public ListModelList<BackofficeSavedQueryModel> getListModel()
    {
        return this.listModel;
    }


    public void setListModel(ListModelList<BackofficeSavedQueryModel> listModel)
    {
        this.listModel = listModel;
    }


    public Checkbox getSharedCheckBox()
    {
        return this.sharedCheckBox;
    }


    public Toolbarbutton getShowQueriesFilterBtn()
    {
        return this.showQueriesFilterBtn;
    }


    public Textbox getFilterTextbox()
    {
        return this.filterTextbox;
    }


    public BackofficeSavedQueriesService getBackofficeSavedQueriesService()
    {
        return this.backofficeSavedQueriesService;
    }


    public void setBackofficeSavedQueriesService(BackofficeSavedQueriesService backofficeSavedQueriesService)
    {
        this.backofficeSavedQueriesService = backofficeSavedQueriesService;
    }


    public ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }


    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public CockpitEventQueue getCockpitEventQueue()
    {
        return this.cockpitEventQueue;
    }


    public void setCockpitEventQueue(CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
