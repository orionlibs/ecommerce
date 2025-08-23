package com.hybris.pcmbackoffice.widgets.shortcuts;

import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.advancedsearch.util.AdvancedSearchDataUtil;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldListType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.pcmbackoffice.services.ShortcutsService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;

public class ShortcutsController extends DefaultWidgetController
{
    public static final String SOCKET_OUT_DESELECT_ALL_ITEMS = "deselectAllItems";
    public static final String SOCKET_OUT_SELECTED_ITEM = "selectedItem";
    public static final String SOCKET_OUT_UPDATE_FULL_TEXT_SEARCH = "updateFullTextSearch";
    public static final String SOCKET_OUT_RESET_FULL_TEXT_SEARCH = "resetFullTextSearch";
    public static final String SOCKET_OUT_UPDATE_ASSORTMENT_FULL_TEXT_SEARCH = "updateAssortmentFullTextSearch";
    public static final String SOCKET_OUT_INIT_SEARCH = "initSearch";
    public static final String SOCKET_IN_REFRESH = "refresh";
    public static final String SOCKET_IN_RESET = "reset";
    public static final String SOCKET_IN_SEARCH_INIT = "searchInit";
    public static final String SOCKET_IN_UPDATE_CONTEXT = "updateContext";
    public static final String SOCKET_IN_REFRESH_ASSORTMENT_FULL_TEXT_SEARCH = "refreshAssortmentFullTextSearch";
    protected static final String LABEL = "shortcuts.label.";
    private static final String CONDITIONS = "conditions";
    private static final String CONTEXT = "context";
    protected static final String MODEL_DATA_TYPE = "dataType";
    private static final ArrayList<ShortcutsFlagEnum> SHORTCUTS_FLAGS = new ArrayList<>(
                    Arrays.asList(new ShortcutsFlagEnum[] {ShortcutsFlagEnum.QUICK_LIST, ShortcutsFlagEnum.BLOCKED_LIST}));
    private transient ShortcutsService shortcutsService;
    private transient ShortcutsUtilService shortcutsUtilService;
    private ShortcutsFlagEnum currentLabel;
    private transient List<Shortcutsitem> shortcutsList;
    private transient TypeFacade typeFacade;
    private transient AdvancedSearchOperatorService advancedSearchOperatorService;
    @Wire
    public Listbox shortcuts;
    private static final Logger LOG = LoggerFactory.getLogger(ShortcutsController.class);


    public void initialize(Component comp)
    {
        super.initialize(comp);
        initShortcutsitemList();
        this.currentLabel = ShortcutsFlagEnum.ALL_PRODUCTS;
        refresh();
    }


    private void initShortcutsitemList()
    {
        this.shortcutsList = new ArrayList<>();
        SHORTCUTS_FLAGS.forEach(flagEnum -> {
            Shortcutsitem shortcutsitem = new Shortcutsitem(this.shortcuts, flagEnum, getShortcutsService().initCollection(flagEnum.getCode()));
            shortcutsitem.addEventListener("onClick", onClick(shortcutsitem)).enableDrop(getShortcutsUtilService().getDroppables()).addEventListener("onDrop", onDrop(shortcutsitem));
            this.shortcutsList.add(shortcutsitem);
        });
    }


    private EventListener<Event> onClick(Shortcutsitem shortcutsitem)
    {
        return (EventListener<Event>)new Object(this, shortcutsitem);
    }


    private EventListener<Event> onDrop(Shortcutsitem shortcutsitem)
    {
        return (EventListener<Event>)new Object(this, shortcutsitem);
    }


    private void refresh()
    {
        if(CollectionUtils.isNotEmpty(this.shortcutsList))
        {
            for(Shortcutsitem shortcutsitem : this.shortcutsList)
            {
                List<PK> list = getShortcutsService().getAllCollectionList(shortcutsitem.getCollectionModel());
                updateListItemTotalCount(list.size(), shortcutsitem);
            }
        }
    }


    private void updateFullTextSearch(ValueComparisonOperator operator, List<PK> list)
    {
        List<SearchConditionData> conditions = getShortcutsUtilService().getConditions(operator, list);
        sendOutput("updateFullTextSearch", conditions);
    }


    private void updateAssortmentFullTextSearch(ValueComparisonOperator operator, List<PK> list)
    {
        List<SearchConditionData> conditions = getShortcutsUtilService().getConditions(operator, list);
        sendOutput("updateAssortmentFullTextSearch", conditions);
    }


    private void sendEmptyPageable()
    {
        sendOutput("updateFullTextSearch", Arrays.asList(new SearchConditionData[] {getShortcutsUtilService().getPKEmptyCondition()}));
    }


    private void updateProductView(ShortcutsFlagEnum label)
    {
        if(label != null && label != ShortcutsFlagEnum.ALL_PRODUCTS)
        {
            BackofficeObjectSpecialCollectionModel listCollection = getShortcutsService().initCollection(label.getCode());
            List<PK> list = getShortcutsService().getAllCollectionList(listCollection);
            if(CollectionUtils.isEmpty(list))
            {
                sendEmptyPageable();
            }
            else
            {
                updateFullTextSearch(ValueComparisonOperator.IN, list);
            }
        }
        else
        {
            List<PK> blockedList = getBlockedList();
            updateFullTextSearch(ValueComparisonOperator.NOT_IN, blockedList);
        }
    }


    private void updateAssortmentView()
    {
        BackofficeObjectSpecialCollectionModel blockedListCollection = getShortcutsService().initCollection(ShortcutsFlagEnum.BLOCKED_LIST.getCode());
        List<PK> blockedList = getShortcutsService().getAllCollectionList(blockedListCollection);
        updateAssortmentFullTextSearch(ValueComparisonOperator.NOT_IN, blockedList);
    }


    private void resetFullTextSearch(ValueComparisonOperator operator, List<PK> list, AdvancedSearchInitContext context)
    {
        Map<String, Object> data = new HashMap<>();
        data.put("conditions", getShortcutsUtilService().getConditions(operator, list));
        data.put("context", context);
        sendOutput("resetFullTextSearch", data);
    }


    private void resetProductView(List<PK> list)
    {
        if(CollectionUtils.isEmpty(list))
        {
            sendEmptyPageable();
        }
        else
        {
            resetFullTextSearch(ValueComparisonOperator.IN, list, getAdvancedSearchInitContext());
        }
    }


    @GlobalCockpitEvent(eventName = "objectsDeleted", scope = "session")
    public void handleObjectDeleteEvent(CockpitEvent event)
    {
        Collection<Object> deletedObjects = event.getDataAsCollection();
        for(Object deletedObject : deletedObjects)
        {
            if(deletedObject instanceof ProductModel)
            {
                ProductModel deletedProduct = (ProductModel)deletedObject;
                handleDeletedProduct(deletedProduct);
                refresh();
                updateAssortmentView();
                updateProductView(this.currentLabel);
            }
        }
    }


    private void handleDeletedProduct(ProductModel product)
    {
        if(CollectionUtils.isNotEmpty(this.shortcutsList))
        {
            for(Shortcutsitem shortcutsitem : this.shortcutsList)
            {
                BackofficeObjectSpecialCollectionModel collectionModel = shortcutsitem.getCollectionModel();
                if(getShortcutsService().collectionContainsItem(product, collectionModel))
                {
                    getShortcutsService().deleteProductFromCollectionlist(product, collectionModel);
                }
            }
        }
    }


    @SocketEvent(socketId = "refresh")
    public void refresh(Object obj)
    {
        refresh();
        updateProductView(this.currentLabel);
        updateAssortmentView();
    }


    @SocketEvent(socketId = "searchInit")
    public void initializeFullTextSearch(String typeCode)
    {
        String previousTypeCode = (String)getValue("dataType", String.class);
        if(!StringUtils.equals(typeCode, previousTypeCode))
        {
            setValue("dataType", typeCode);
            sendOutput("initSearch", typeCode);
            updateProductView(this.currentLabel);
            updateAssortmentView();
        }
    }


    @SocketEvent(socketId = "updateContext")
    public void updateContext(AdvancedSearchInitContext context)
    {
        if(context == null)
        {
            context = getAdvancedSearchInitContext();
        }
        this.currentLabel = ShortcutsFlagEnum.ALL_PRODUCTS;
        resetFullTextSearch(ValueComparisonOperator.NOT_IN, getBlockedList(), context);
    }


    @SocketEvent(socketId = "reset")
    public void reset(Object object)
    {
        makeAllItemsEnableDrop();
        this.shortcuts.clearSelection();
    }


    @SocketEvent(socketId = "refreshAssortmentFullTextSearch")
    public void refreshAssortmentViewFullTextSearch()
    {
        updateAssortmentView();
    }


    private void updateListItemTotalCount(int totalCount, Shortcutsitem shortcutsitem)
    {
        shortcutsitem.getListItem().setLabel(
                        Labels.getLabel(String.format("%s%s", new Object[] {"shortcuts.label.", shortcutsitem.getFlag().getCode()}), ArrayUtils.toArray((Object[])new Integer[] {Integer.valueOf(totalCount)})));
    }


    protected void addItemsToCollection(List<ItemModel> items, Shortcutsitem targetShortcutsitem)
    {
        List<ItemModel> itemsToAdd = getShortcutsUtilService().getNotExistItems(items, targetShortcutsitem
                        .getCollectionModel());
        if(CollectionUtils.isNotEmpty(itemsToAdd))
        {
            for(ItemModel item : itemsToAdd)
            {
                BackofficeObjectSpecialCollectionModel originCollectionModel = findOriginCollectionModel(item);
                if(originCollectionModel != null)
                {
                    this.shortcutsService.deleteProductFromCollectionlist((ProductModel)item, originCollectionModel);
                }
                this.shortcutsService.insertProductToCollectionlist((ProductModel)item, targetShortcutsitem.getCollectionModel());
            }
            refresh();
            updateAssortmentView();
            updateProductView(this.currentLabel);
        }
    }


    protected BackofficeObjectSpecialCollectionModel findOriginCollectionModel(ItemModel itemModel)
    {
        if(CollectionUtils.isNotEmpty(this.shortcutsList))
        {
            for(Shortcutsitem item : this.shortcutsList)
            {
                if(getShortcutsService().collectionContainsItem((ProductModel)itemModel, item.getCollectionModel()))
                {
                    return item.getCollectionModel();
                }
            }
        }
        return null;
    }


    private List<PK> getBlockedList()
    {
        BackofficeObjectSpecialCollectionModel blockedListCollection = getShortcutsService().initCollection(ShortcutsFlagEnum.BLOCKED_LIST.getCode());
        return getShortcutsService().getAllCollectionList(blockedListCollection);
    }


    private void makeAllItemsEnableDrop()
    {
        if(CollectionUtils.isNotEmpty(this.shortcutsList))
        {
            for(Shortcutsitem item : this.shortcutsList)
            {
                item.enableDrop(getShortcutsUtilService().getDroppables());
            }
        }
    }


    protected AdvancedSearchInitContext getAdvancedSearchInitContext()
    {
        DefaultConfigContext configContext = new DefaultConfigContext("advanced-search", "Product");
        try
        {
            AdvancedSearch config = (AdvancedSearch)getWidgetInstanceManager().loadConfiguration((ConfigContext)configContext, AdvancedSearch.class);
            if(config == null)
            {
                return null;
            }
            FieldListType fieldList = config.getFieldList();
            AdvancedSearchData searchData = new AdvancedSearchData(fieldList);
            DataType dataType = getTypeFacade().load("Product");
            if(fieldList != null)
            {
                List<FieldType> selectedFieldTypeList = (List<FieldType>)fieldList.getField().stream().filter(item -> item.isSelected()).collect(Collectors.toList());
                for(FieldType field : selectedFieldTypeList)
                {
                    DataAttribute dataAttr = dataType.getAttribute(field.getName());
                    AdvancedSearchDataUtil.tryAddCondition(getAdvancedSearchOperatorService(), dataType, searchData, field, dataAttr);
                }
                searchData.setIncludeSubtypes(Boolean.valueOf(fieldList.isIncludeSubtypes()));
            }
            searchData.setTypeCode(dataType.getCode());
            if(config.getSortField() != null)
            {
                searchData.setSortData(new SortData(config.getSortField().getName(), config.getSortField().isAsc()));
            }
            searchData.setGlobalOperator(ValueComparisonOperator.AND);
            return new AdvancedSearchInitContext(searchData, config);
        }
        catch(CockpitConfigurationException e)
        {
            LOG.error("Error while enquiring config product", (Throwable)e);
            return null;
        }
        catch(TypeNotFoundException e)
        {
            LOG.error("Cannot load type", (Throwable)e);
            return null;
        }
    }


    @Required
    public void setShortcutsService(ShortcutsService shortcutsService)
    {
        this.shortcutsService = shortcutsService;
    }


    public void setCurrentLabel(ShortcutsFlagEnum currentLabel)
    {
        this.currentLabel = currentLabel;
    }


    public ShortcutsService getShortcutsService()
    {
        return this.shortcutsService;
    }


    @Required
    public void setShortcutsUtilService(ShortcutsUtilService shortcutsUtilService)
    {
        this.shortcutsUtilService = shortcutsUtilService;
    }


    public ShortcutsUtilService getShortcutsUtilService()
    {
        return this.shortcutsUtilService;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }


    @Required
    public void setAdvancedSearchOperatorService(AdvancedSearchOperatorService advancedSearchOperatorService)
    {
        this.advancedSearchOperatorService = advancedSearchOperatorService;
    }


    public AdvancedSearchOperatorService getAdvancedSearchOperatorService()
    {
        return this.advancedSearchOperatorService;
    }
}
