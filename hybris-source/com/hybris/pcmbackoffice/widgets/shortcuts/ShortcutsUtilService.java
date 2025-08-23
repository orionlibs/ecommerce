package com.hybris.pcmbackoffice.widgets.shortcuts;

import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.dnd.SelectionSupplier;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.pcmbackoffice.services.ShortcutsService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;

public class ShortcutsUtilService
{
    public static final String MAX_SIZE = ".maxsize";
    public static final String PCMBACKOFFICE = "pcmbackoffice.";
    public static final String NOTIFICATION_SOURCE_SHORTCUTS_ITEMS_NOT_EXIST = "shortcutsItemsNotExist";
    public static final String NOTIFICATION_SOURCE_SHORTCUTS_ITEMS_OVER_LIMIT = "shortcutsItemsOverLimit";
    protected static final int MAX_SIZE_OF_NOTIFICATIONS_CAN_DISPLAY = 3;
    private static final Logger LOG = LoggerFactory.getLogger(ShortcutsUtilService.class);
    private ModelService modelService;
    private NotificationService notificationService;
    private AdvancedSearchOperatorService advancedSearchOperatorService;
    private TypeFacade typeFacade;
    private ShortcutsService shortcutsService;
    private LabelService labelService;


    protected List<SearchConditionData> getConditions(ValueComparisonOperator operator, List<PK> list)
    {
        if(CollectionUtils.isNotEmpty(list))
        {
            return addFilterConditionOnSearchData(operator, list);
        }
        return new ArrayList<>();
    }


    protected List<ItemModel> getSelectedData(Event event)
    {
        DropEvent dropEvent = (DropEvent)event;
        SelectionSupplier<ItemModel> selectionSupplier = (SelectionSupplier<ItemModel>)dropEvent.getDragged().getAttribute("selectionSupplier", true);
        List<ItemModel> draggedObjects = new ArrayList<>();
        if(selectionSupplier != null)
        {
            draggedObjects.addAll(selectionSupplier.findSelection());
        }
        if(draggedObjects.isEmpty())
        {
            draggedObjects.add((ItemModel)dropEvent.getDragged().getAttribute("dndData", true));
        }
        return draggedObjects;
    }


    protected FullTextSearchData getFullTextSearchData()
    {
        DefaultContext defaultContext = new DefaultContext();
        defaultContext.addAttribute("originalQuery", null);
        return new FullTextSearchData(CollectionUtils.EMPTY_COLLECTION, "", (Context)defaultContext);
    }


    protected List<SearchConditionData> addFilterConditionOnSearchData(ValueComparisonOperator operator, List<PK> list)
    {
        List<SearchConditionData> conditions = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(list))
        {
            FieldType fieldType = new FieldType();
            fieldType.setDisabled(Boolean.FALSE);
            fieldType.setSelected(Boolean.TRUE);
            fieldType.setName("pk");
            SearchConditionData condition = new SearchConditionData(fieldType, list, operator);
            conditions.add(condition);
        }
        return conditions;
    }


    protected SearchConditionData getPKEmptyCondition()
    {
        FieldType fieldType = new FieldType();
        fieldType.setName("pk");
        return new SearchConditionData(fieldType, null, ValueComparisonOperator.IS_EMPTY);
    }


    public boolean isItemsAlreadyDeleted(Collection<ItemModel> items)
    {
        List<ItemModel> deletedItems = (List<ItemModel>)items.stream().filter(item -> getModelService().isRemoved(item)).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(deletedItems))
        {
            getNotificationService().notifyUser("shortcutsItemsNotExist", "General", NotificationEvent.Level.FAILURE, new Object[] {getNotificationLabel(deletedItems)});
            return true;
        }
        return false;
    }


    protected boolean isLimitationExceeded(List<ItemModel> items, Shortcutsitem shortcutsitem)
    {
        int maxSize = getMaxSize(shortcutsitem);
        int totalCount = getShortcutsService().getAllCollectionList(shortcutsitem.getCollectionModel()).size();
        if(items.size() + totalCount > maxSize)
        {
            getNotificationService().notifyUser("shortcutsItemsOverLimit", "General", NotificationEvent.Level.FAILURE, new Object[] {Integer.valueOf(maxSize)});
            return true;
        }
        return false;
    }


    protected int getMaxSize(Shortcutsitem shortcutsitem)
    {
        String maxSize = "pcmbackoffice." + shortcutsitem.getFlag().getCode() + ".maxsize";
        return Config.getInt(maxSize, 0);
    }


    public List<ItemModel> getAlreadyExistItems(Collection<ItemModel> items, BackofficeObjectSpecialCollectionModel collectionModel)
    {
        return (List<ItemModel>)items.stream().filter(item -> getShortcutsService().collectionContainsItem((ProductModel)item, collectionModel))
                        .collect(Collectors.toList());
    }


    public List<ItemModel> getNotExistItems(Collection<ItemModel> items, BackofficeObjectSpecialCollectionModel collectionModel)
    {
        return (List<ItemModel>)items.stream().filter(item -> !getShortcutsService().collectionContainsItem((ProductModel)item, collectionModel))
                        .collect(Collectors.toList());
    }


    private String getNotificationLabel(List<ItemModel> items)
    {
        String label = "";
        List<ItemModel> displayedItems = items;
        if(items.size() > 3)
        {
            displayedItems = items.subList(0, 3);
        }
        for(ItemModel item : displayedItems)
        {
            if(label.isEmpty())
            {
                label = getLabelService().getObjectLabel(item);
                continue;
            }
            label = String.format("%s, %s", new Object[] {label, getLabelService().getObjectLabel(item)});
        }
        if(items.size() > 3)
        {
            label = String.format("%s...", new Object[] {label});
        }
        return label;
    }


    protected String getDroppables()
    {
        Set<String> droppableTypes = new HashSet<>();
        droppableTypes.addAll(resolveAllConcreteSubtypes("Product"));
        return droppableTypes.stream().reduce((s1, s2) -> s1 + "," + s1).orElse("true");
    }


    private List<String> resolveAllConcreteSubtypes(String type)
    {
        List<String> types = new ArrayList<>();
        try
        {
            DataType dataType = this.typeFacade.load(type);
            if(dataType != null)
            {
                if(!dataType.isAbstract())
                {
                    types.add(dataType.getCode());
                }
                dataType.getSubtypes().forEach(subtype -> types.addAll(resolveAllConcreteSubtypes(subtype)));
            }
        }
        catch(TypeNotFoundException e)
        {
            LOG.warn("Unable to load type", (Throwable)e);
        }
        return types;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public NotificationService getNotificationService()
    {
        return this.notificationService;
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


    @Required
    public void setShortcutsService(ShortcutsService shortcutsService)
    {
        this.shortcutsService = shortcutsService;
    }


    public ShortcutsService getShortcutsService()
    {
        return this.shortcutsService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
