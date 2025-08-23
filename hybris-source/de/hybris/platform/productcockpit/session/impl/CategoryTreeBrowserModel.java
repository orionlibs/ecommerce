package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.browser.BrowserModelFactory;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.BulkUndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.ItemChangeUndoableOperation;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractAdvancedBrowserModel;
import de.hybris.platform.cockpit.session.impl.AbstractPageableBrowserModel;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.util.UndoTools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.productcockpit.components.contentbrowser.CategoryTreeContentBrowser;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeColumn;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeModelAbstract;
import de.hybris.platform.productcockpit.model.macfinder.node.LeafNode;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import de.hybris.platform.productcockpit.model.undo.CategoryAssignmentUndoableOperation;
import de.hybris.platform.productcockpit.services.catalog.CatalogListService;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import de.hybris.platform.productcockpit.services.catalog.ConnectedItemService;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

public class CategoryTreeBrowserModel extends AbstractAdvancedBrowserModel
{
    private static final Logger log = LoggerFactory.getLogger(CategoryTreeBrowserModel.class);
    private CatalogListService productCockpitCatalogService;
    private TypeService typeService;
    private ConnectedItemService connectedItemService;
    private CatalogVersionModel catalogVersion;
    private final MacFinderTreeModelAbstract treeModel;
    private final List<TypedObject> selectedItems = new ArrayList<>();


    public List<TypedObject> getSelectedItems()
    {
        return Collections.unmodifiableList(this.selectedItems);
    }


    public void setSelectedItems(List<TypedObject> selectedItems)
    {
        this.selectedItems.clear();
        this.selectedItems.addAll(selectedItems);
    }


    public CategoryTreeBrowserModel()
    {
        this.treeModel = (MacFinderTreeModelAbstract)new MacFinderTreeModelDefault(this);
    }


    private CategoryTreeBrowserModel(CatalogVersionModel catalogVersion, MacFinderTreeModelAbstract treeModel)
    {
        this.catalogVersion = catalogVersion;
        this.treeModel = (MacFinderTreeModelAbstract)new MacFinderTreeModelDefault(this, treeModel.getSelectedNodes());
    }


    public void setCatalogVersion(CatalogVersionModel catalogVersion)
    {
        if(this.catalogVersion == null || !this.catalogVersion.equals(catalogVersion))
        {
            this.catalogVersion = catalogVersion;
            this.treeModel.reInit();
            fireChanged();
        }
    }


    public CatalogVersionModel getCatalogVersion()
    {
        if(this.catalogVersion == null)
        {
            if(!UISessionUtils.getCurrentSession().getSelectedCatalogVersions().isEmpty())
            {
                this.catalogVersion = UISessionUtils.getCurrentSession().getSelectedCatalogVersions().get(0);
            }
            else
            {
                UserModel currentUser = UISessionUtils.getCurrentSession().getUser();
                List<CatalogVersionModel> versions = getProductCockpitCatalogService().getSortedCatalogVersions(currentUser);
                if(!versions.isEmpty())
                {
                    this.catalogVersion = versions.get(0);
                }
            }
        }
        return this.catalogVersion;
    }


    public void updateItems()
    {
        this.treeModel.refreshWholeTree();
        fireChanged();
    }


    public CatalogBrowserArea getArea()
    {
        return (CatalogBrowserArea)super.getArea();
    }


    public void openRelatedQueryBrowser()
    {
        focus();
        getArea().setSplitModeActiveDirectly(true);
        BrowserModelFactory factory = (BrowserModelFactory)SpringUtil.getBean("BrowserModelFactory");
        DefaultProductSearchBrowserModel browserModel = (DefaultProductSearchBrowserModel)factory.createBrowserModel("DefaultProductSearchBrowserModel");
        browserModel.setLastQuery(new Query(null, "*", 0, 0));
        browserModel.setBrowserFilterFixed((BrowserFilter)new QueryBrowserCatalogVersionFilter(getCatalogVersion()));
        getArea().addVisibleBrowser(1, (BrowserModel)browserModel);
        browserModel.updateItems();
        browserModel.focus();
        getArea().update();
    }


    public void handleDrop(List<TypedObject> draggedItems, TypedObject dropTarget, CategoryModel oldSuperCat)
    {
        boolean updateNeeded = false;
        boolean groupUndo = false;
        if(draggedItems.size() > 1)
        {
            UndoTools.startUndoGrouping();
            groupUndo = true;
        }
        if(dropTarget.getType() != null)
        {
            Map<BaseType, List<TypedObject>> groupedItems = groupDraggedItemsPerType(draggedItems);
            if(TypeTools.checkInstanceOfCategory(getTypeService(), dropTarget))
            {
                for(BaseType draggedType : groupedItems.keySet())
                {
                    if(isOfType("Product", draggedType))
                    {
                        updateNeeded = handleDropProductsToCategory(dropTarget,
                                        (oldSuperCat != null) ? Collections.<CategoryModel>singletonList(oldSuperCat) : null, groupedItems.get(draggedType));
                        continue;
                    }
                    if(isOfType("Category", draggedType))
                    {
                        updateNeeded = handleDropCategoriesToCategory(dropTarget,
                                        (oldSuperCat != null) ? Collections.<CategoryModel>singletonList(oldSuperCat) : null, groupedItems.get(draggedType));
                    }
                }
            }
            else if(TypeTools.checkInstanceOfCatalogVersion(getTypeService(), dropTarget))
            {
                for(BaseType draggedType : groupedItems.keySet())
                {
                    if(isOfType("Product", draggedType))
                    {
                        updateNeeded = handleDropProductsToCatalogVersion(dropTarget, oldSuperCat, groupedItems.get(draggedType));
                        continue;
                    }
                    if(isOfType("Category", draggedType))
                    {
                        updateNeeded = handleDropCategoriesToCatalogVersion(groupedItems.get(draggedType));
                    }
                }
            }
        }
        if(groupUndo)
        {
            BulkUndoableOperation buo = new BulkUndoableOperation(UndoTools.getGroupedOperations());
            buo.setShowOperationCount(false);
            UndoTools.stopUndoGrouping(UISessionUtils.getCurrentSession().getUndoManager(), getArea(), buo);
        }
        if(updateNeeded)
        {
            for(BrowserModel visBrowser : getArea().getVisibleBrowsers())
            {
                if(visBrowser instanceof AbstractPageableBrowserModel)
                {
                    ((AbstractPageableBrowserModel)visBrowser)
                                    .updateItems(((AbstractPageableBrowserModel)visBrowser).getCurrentPage());
                    continue;
                }
                visBrowser.updateItems();
            }
        }
    }


    public void handleDropAndRemoveFromCategories(List<TypedObject> draggedItems, TypedObject dropTarget, List<CategoryModel> oldSuperCategories)
    {
        boolean updateNeeded = false;
        boolean groupUndo = false;
        if(draggedItems.size() > 1)
        {
            UndoTools.startUndoGrouping();
            groupUndo = true;
        }
        if(dropTarget.getType() != null)
        {
            Map<BaseType, List<TypedObject>> groupedItems = groupDraggedItemsPerType(draggedItems);
            if(TypeTools.checkInstanceOfCategory(getTypeService(), dropTarget))
            {
                for(BaseType draggedType : groupedItems.keySet())
                {
                    if(isOfType("Product", draggedType))
                    {
                        updateNeeded = handleDropProductsToCategory(dropTarget, oldSuperCategories, groupedItems.get(draggedType));
                        continue;
                    }
                    if(isOfType("Category", draggedType))
                    {
                        updateNeeded = handleDropAndRemoveCategoriesToCategory(dropTarget, oldSuperCategories, groupedItems
                                        .get(draggedType));
                    }
                }
            }
            else if(TypeTools.checkInstanceOfCatalogVersion(getTypeService(), dropTarget))
            {
                for(BaseType draggedType : groupedItems.keySet())
                {
                    if(isOfType("Product", draggedType))
                    {
                        updateNeeded = handleDropProductsToCatalogVersion(dropTarget, oldSuperCategories.get(0), groupedItems
                                        .get(draggedType));
                        continue;
                    }
                    if(isOfType("Category", draggedType))
                    {
                        updateNeeded = handleDropCategoriesToCatalogVersion(groupedItems.get(draggedType));
                    }
                }
            }
        }
        if(groupUndo)
        {
            BulkUndoableOperation buo = new BulkUndoableOperation(UndoTools.getGroupedOperations());
            buo.setShowOperationCount(false);
            UndoTools.stopUndoGrouping(UISessionUtils.getCurrentSession().getUndoManager(), getArea(), buo);
        }
        if(updateNeeded)
        {
            for(BrowserModel visBrowser : getArea().getVisibleBrowsers())
            {
                if(visBrowser instanceof AbstractPageableBrowserModel)
                {
                    ((AbstractPageableBrowserModel)visBrowser)
                                    .updateItems(((AbstractPageableBrowserModel)visBrowser).getCurrentPage());
                    continue;
                }
                visBrowser.updateItems();
            }
        }
    }


    private boolean handleDropCategoriesToCatalogVersion(List<TypedObject> draggedCategories)
    {
        boolean updateNeeded = false;
        int numberOfCategories = draggedCategories.size();
        for(TypedObject draggedCategory : draggedCategories)
        {
            List<TypedObject> ownSupercategories = getProductCockpitCatalogService().getSupercategories(draggedCategory, true);
            if(getProductCockpitCatalogService().setAsRootCategory(getProductCockpitCatalogService().wrapCategory(draggedCategory)))
            {
                updateNeeded = true;
                UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), (UndoableOperation)new CategoryAssignmentUndoableOperation(ownSupercategories, null,
                                                Collections.singletonList(draggedCategory), true, (CatalogService)
                                                getProductCockpitCatalogService()),
                                getArea());
                if(numberOfCategories > 0 && numberOfCategories <= Config.getInt("cockpit.changeevents.threshold", 3))
                {
                    UISessionUtils.getCurrentSession()
                                    .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(), draggedCategory,
                                                    Collections.singleton(getTypeService().getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + "." + GeneratedCatalogConstants.TC.CATEGORY))));
                    for(TypedObject supercat : ownSupercategories)
                    {
                        UISessionUtils.getCurrentSession()
                                        .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(), supercat,
                                                        Collections.singleton(getTypeService().getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + ".categories"))));
                    }
                }
                continue;
            }
            addPermissionDeniedMsg();
        }
        if(numberOfCategories > Config.getInt("cockpit.changeevents.threshold", 3))
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(), null,
                            Collections.singleton(getTypeService().getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + "." + GeneratedCatalogConstants.TC.CATEGORY))));
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(
                            getArea(), null, Collections.singleton(getTypeService().getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + ".categories"))));
        }
        return updateNeeded;
    }


    private boolean handleDropProductsToCatalogVersion(TypedObject dropTarget, CategoryModel oldSuperCat, List<TypedObject> draggedItems)
    {
        boolean move = (oldSuperCat != null);
        boolean updateNeeded = false;
        int numberOfProducts = draggedItems.size();
        for(TypedObject draggedItem : draggedItems)
        {
            CatalogVersionModel origVersion = getProductCockpitCatalogService().getCatalogVersion(((ItemModel)draggedItem.getObject()).getPk());
            CatalogVersionModel version = getProductCockpitCatalogService().wrapCatalogVersion(dropTarget);
            List<TypedObject> supercatsToRemove = new ArrayList<>();
            List<TypedObject> supercategories = getProductCockpitCatalogService().getSupercategories(draggedItem, false);
            for(TypedObject supercat : supercategories)
            {
                CatalogVersionModel supercatVersion = getProductCockpitCatalogService().getCatalogVersion(((ItemModel)supercat.getObject()).getPk());
                if(version.equals(supercatVersion))
                {
                    supercatsToRemove.add(supercat);
                }
            }
            if(version != null && getProductCockpitCatalogService().assignProduct(draggedItem, version, oldSuperCat))
            {
                updateNeeded = true;
                List<UndoableOperation> operations = new ArrayList<>(3);
                operations.add(new CategoryAssignmentUndoableOperation(
                                (oldSuperCat != null) ? Collections.<TypedObject>singletonList(getTypeService().wrapItem(oldSuperCat.getPk())) :
                                                Collections.EMPTY_LIST, Collections.EMPTY_LIST,
                                Collections.singletonList(draggedItem), move, (CatalogService)getProductCockpitCatalogService()));
                operations.add(new CategoryAssignmentUndoableOperation(supercatsToRemove, Collections.EMPTY_LIST,
                                Collections.singletonList(draggedItem), true, (CatalogService)getProductCockpitCatalogService()));
                ObjectValueContainer valueContainer = new ObjectValueContainer((ObjectType)draggedItem.getType(), draggedItem.getObject());
                valueContainer
                                .addValue(getTypeService().getPropertyDescriptor("Product." + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION), null,
                                                getTypeService().wrapItem(origVersion))
                                .setLocalValue(version);
                operations.add(new ItemChangeUndoableOperation(draggedItem, valueContainer));
                BulkUndoableOperation bulk = new BulkUndoableOperation(operations);
                bulk.setShowOperationCount(false);
                UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), (UndoableOperation)bulk, getArea());
                if(numberOfProducts > 0 && numberOfProducts <= Config.getInt("cockpit.changeevents.threshold", 3))
                {
                    UISessionUtils.getCurrentSession()
                                    .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(), draggedItem, Arrays.asList(new PropertyDescriptor[] {getTypeService().getPropertyDescriptor("Product." + GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES),
                                                    getTypeService().getPropertyDescriptor("Product." + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION)})));
                    for(TypedObject supercat : supercatsToRemove)
                    {
                        UISessionUtils.getCurrentSession()
                                        .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(), supercat,
                                                        Collections.singleton(getTypeService().getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + ".products"))));
                    }
                }
                continue;
            }
            addPermissionDeniedMsg();
        }
        if(numberOfProducts > Config.getInt("cockpit.changeevents.threshold", 3))
        {
            UISessionUtils.getCurrentSession()
                            .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(), null, Arrays.asList(new PropertyDescriptor[] {getTypeService().getPropertyDescriptor("Product." + GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES),
                                            getTypeService().getPropertyDescriptor("Product." + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION)})));
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(
                            getArea(), null, Collections.singleton(getTypeService().getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + ".products"))));
        }
        else if(oldSuperCat != null)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(),
                            getTypeService().wrapItem(oldSuperCat.getPk()), Collections.singleton(getTypeService().getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + ".products"))));
        }
        return updateNeeded;
    }


    private boolean handleDropCategoriesToCategory(TypedObject targetCategory, List<CategoryModel> oldSuperCats, List<TypedObject> draggedItems)
    {
        boolean updateNeeded = false;
        CategoryModel targetCategoryModel = getProductCockpitCatalogService().wrapCategory(targetCategory);
        if(targetCategoryModel != null)
        {
            updateNeeded = getProductCockpitCatalogService().moveCategories(draggedItems, targetCategoryModel,
                            (oldSuperCats != null) ? oldSuperCats.get(0) : null);
            if(updateNeeded)
            {
                addUndoableOperation(targetCategory, oldSuperCats, draggedItems);
                fireChangeEvents(targetCategory, oldSuperCats, draggedItems);
            }
            else
            {
                addPermissionDeniedMsg();
            }
        }
        return updateNeeded;
    }


    private boolean handleDropAndRemoveCategoriesToCategory(TypedObject targetCategory, List<CategoryModel> oldSuperCats, List<TypedObject> draggedItems)
    {
        boolean updateNeeded = false;
        CategoryModel targetCategoryModel = getProductCockpitCatalogService().wrapCategory(targetCategory);
        if(targetCategoryModel != null)
        {
            updateNeeded = getProductCockpitCatalogService().assignAndRemoveCategories(draggedItems, targetCategoryModel);
            if(updateNeeded)
            {
                addUndoableOperation(targetCategory, oldSuperCats, draggedItems);
                fireChangeEvents(targetCategory, oldSuperCats, draggedItems);
            }
            else
            {
                addPermissionDeniedMsg();
            }
        }
        return updateNeeded;
    }


    private void addUndoableOperation(TypedObject targetCategory, List<CategoryModel> oldSuperCats, List<TypedObject> draggedItems)
    {
        UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), (UndoableOperation)new CategoryAssignmentUndoableOperation(
                                        !CollectionUtils.isEmpty(oldSuperCats) ? getTypeService().wrapItems(oldSuperCats) :
                                                        Collections.EMPTY_LIST,
                                        Collections.singletonList(targetCategory), draggedItems, !CollectionUtils.isEmpty(oldSuperCats), (CatalogService)
                                        getProductCockpitCatalogService()),
                        getArea());
    }


    private void fireChangeEvents(TypedObject targetCategory, List<CategoryModel> oldSuperCats, List<TypedObject> draggedItems)
    {
        int numberOfItems = draggedItems.size();
        if(numberOfItems > 0 && numberOfItems <= Config.getInt("cockpit.changeevents.threshold", 3))
        {
            for(TypedObject dragItem : draggedItems)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(
                                getArea(), dragItem, Collections.singleton(getTypeService().getPropertyDescriptor("Category.supercategories"))));
            }
        }
        else
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(
                            getArea(), null, Collections.singleton(getTypeService().getPropertyDescriptor("Category.supercategories"))));
        }
        if(!CollectionUtils.isEmpty(oldSuperCats))
        {
            for(CategoryModel category : oldSuperCats)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(),
                                getTypeService().wrapItem(category.getPk()), Collections.singleton(getTypeService().getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + ".categories"))));
            }
        }
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(
                        getArea(), targetCategory, Collections.singleton(getTypeService().getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + ".categories"))));
    }


    private boolean handleDropProductsToCategory(TypedObject targetCategory, List<CategoryModel> oldSuperCategories, List<TypedObject> draggedProducts)
    {
        boolean updateNeeded = false;
        boolean move = (oldSuperCategories != null);
        CategoryModel targetCategoryModel = getProductCockpitCatalogService().wrapCategory(targetCategory);
        if(targetCategoryModel != null)
        {
            if(CollectionUtils.isEmpty(oldSuperCategories))
            {
                updateNeeded = getProductCockpitCatalogService().assignProducts(draggedProducts, targetCategoryModel, null);
            }
            else
            {
                updateNeeded = getProductCockpitCatalogService().assignAndRemoveProducts(draggedProducts, targetCategoryModel, oldSuperCategories);
            }
            if(updateNeeded)
            {
                UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), (UndoableOperation)new CategoryAssignmentUndoableOperation(
                                                CollectionUtils.isNotEmpty(oldSuperCategories) ?
                                                                getTypeService().wrapItems(oldSuperCategories) : Collections.EMPTY_LIST,
                                                Collections.singletonList(targetCategory), draggedProducts, move, (CatalogService)getProductCockpitCatalogService()),
                                getArea());
                int numberOfProducts = draggedProducts.size();
                if(numberOfProducts > 0 && numberOfProducts < Config.getInt("cockpit.changeevents.threshold", 3))
                {
                    for(TypedObject dragProduct : draggedProducts)
                    {
                        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(
                                        getArea(), dragProduct, Collections.singleton(getTypeService().getPropertyDescriptor("Product.supercategories"))));
                    }
                }
                else
                {
                    UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(
                                    getArea(), null, Collections.singleton(getTypeService().getPropertyDescriptor("Product.supercategories"))));
                }
                if(move)
                {
                    for(CategoryModel category : oldSuperCategories)
                    {
                        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(),
                                        getTypeService().wrapItem(category.getPk()), Collections.singleton(getTypeService().getPropertyDescriptor("Category.products"))));
                    }
                }
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(
                                getArea(), targetCategory, Collections.singleton(getTypeService().getPropertyDescriptor("Category.products"))));
            }
            else
            {
                addPermissionDeniedMsg();
            }
        }
        return updateNeeded;
    }


    private boolean isOfType(String typecode, BaseType draggedType)
    {
        return getTypeService().getBaseType(typecode).isAssignableFrom((ObjectType)draggedType);
    }


    private Map<BaseType, List<TypedObject>> groupDraggedItemsPerType(List<TypedObject> items)
    {
        Map<BaseType, List<TypedObject>> result = new HashMap<>();
        for(TypedObject item : items)
        {
            BaseType itemType = item.getType();
            if(itemType == null)
            {
                throw new IllegalStateException("TypedObject's type cannot be null!");
            }
            if(!result.containsKey(itemType))
            {
                result.put(itemType, new ArrayList<>());
            }
            ((List<TypedObject>)result.get(itemType)).add(item);
        }
        return result;
    }


    public List<Integer> getSelectedTreeIndices()
    {
        List<Integer> ret = new ArrayList<>();
        for(MacFinderTreeColumn col : getTreeModel().getColumns())
        {
            int i = col.getChildren().indexOf(col.getSelectedNode());
            if(i >= 0)
            {
                ret.add(Integer.valueOf(i));
            }
        }
        return ret;
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new CategoryTreeContentBrowser();
    }


    protected void setSelection(List<Integer> selectedIndices)
    {
        try
        {
            int colIndex = 0;
            MacFinderTreeModelAbstract tm = getTreeModel();
            for(Integer index : selectedIndices)
            {
                MacFinderTreeColumn col = tm.getColumns().get(colIndex);
                MacFinderTreeNode node = col.getChildren().get(index.intValue());
                node.setSelected(true);
                tm.extendHierarchy(node);
                colIndex++;
            }
        }
        catch(Exception e)
        {
            log.warn("set selection failed.", e);
        }
    }


    public Object clone() throws CloneNotSupportedException
    {
        CategoryTreeBrowserModel clone = new CategoryTreeBrowserModel(getCatalogVersion(), getTreeModel());
        clone.setBrowserFilterFixed(getBrowserFilterFixed());
        return clone;
    }


    public List<TypedObject> getItems()
    {
        List<TypedObject> list = new ArrayList<>();
        for(LeafNode node : getTreeModel().getLeafNodes())
        {
            list.add(node.getOriginalItem());
        }
        return list;
    }


    @Required
    public void setProductCockpitCatalogService(CatalogListService productCockpitCatalogService)
    {
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    public CatalogListService getProductCockpitCatalogService()
    {
        if(this.productCockpitCatalogService == null)
        {
            this.productCockpitCatalogService = (CatalogListService)SpringUtil.getBean("productCockpitCatalogService");
        }
        return this.productCockpitCatalogService;
    }


    public void resetAreaEditor()
    {
        getArea().getPerspective().getEditorArea().setCurrentObjectType(null);
        getArea().getPerspective().getEditorArea().setCurrentObject(null);
    }


    public void setActiveItem(TypedObject activeItem)
    {
        super.setActiveItem(activeItem);
        getArea().getPerspective().activateItemInEditor(activeItem);
        refreshActiveItems();
    }


    public void refreshActiveItems()
    {
        TypedObject activeItem = getArea().getPerspective().getActiveItem();
        for(LeafNode node : getTreeModel().getLeafNodes())
        {
            boolean isSelected = false;
            isSelected = node.getOriginalItem().equals(activeItem);
            node.setSelected(isSelected);
        }
    }


    public ConnectedItemService getConnectedItemService()
    {
        if(this.connectedItemService == null)
        {
            this.connectedItemService = (ConnectedItemService)SpringUtil.getBean("connectedItemService");
        }
        return this.connectedItemService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    @Required
    public void setConnectedItemService(ConnectedItemService connectedItemService)
    {
        this.connectedItemService = connectedItemService;
    }


    public TypedObject getItem(int index)
    {
        List<? extends TypedObject> items = getItems();
        return items.get(index);
    }


    public MacFinderTreeModelAbstract getTreeModel()
    {
        return this.treeModel;
    }


    public String getLabel()
    {
        String ret = "";
        if(this.catalogVersion != null)
        {
            CatalogModel catalog = getProductCockpitCatalogService().getCatalog(this.catalogVersion);
            String langIso = UISessionUtils.getCurrentSession().getGlobalDataLanguageIso();
            Language language = C2LManager.getInstance().getLanguageByIsoCode(langIso);
            String catalogname = null;
            if(catalog.getName(language.getLocale()) != null)
            {
                catalogname = catalog.getName(language.getLocale());
            }
            else
            {
                List<String> result = null;
                try
                {
                    result = UITools.searchForLabel(catalog, CatalogModel.class.getMethod("getName", new Class[] {Locale.class}), this.catalogVersion
                                    .getLanguages());
                }
                catch(Exception e)
                {
                    log.debug(e.getMessage(), e);
                }
                if(result != null && result.size() == 2)
                {
                    catalogname = result.get(0);
                }
            }
            ret = ((catalogname != null) ? catalogname : ("<" + catalog.getId() + ">")) + " " + ((catalogname != null) ? catalogname : ("<" + catalog.getId() + ">")) + " (" + this.catalogVersion.getVersion() + ")";
        }
        return ret;
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
        UINavigationArea na = getArea().getManagingPerspective().getNavigationArea();
        List<ObjectCollection> specialCollections = na.getObjectCollectionService().getSpecialCollections(UISessionUtils.getCurrentSession().getUser());
        UICollectionQuery cq = null;
        for(ObjectCollection oc : specialCollections)
        {
            if("Blacklist".equalsIgnoreCase(oc.getLabel()))
            {
                cq = new UICollectionQuery(oc);
                break;
            }
        }
        if(na instanceof BaseUICockpitNavigationArea)
        {
            Object[] indexesArray = indexes.toArray();
            for(int i = 0; i < indexesArray.length; i++)
            {
                ((BaseUICockpitNavigationArea)na).addToCollection(getItem(((Integer)indexesArray[i]).intValue()), cq, true);
            }
        }
    }


    public void removeItems(Collection<Integer> indexes)
    {
        UINavigationArea na = getArea().getManagingPerspective().getNavigationArea();
        if(na instanceof BaseUICockpitNavigationArea)
        {
            Object[] indexesArray = indexes.toArray();
            for(int i = 0; i < indexesArray.length; i++)
            {
                getItems().remove(((Integer)indexesArray[i]).intValue());
            }
        }
    }


    private void addPermissionDeniedMsg()
    {
        getArea().getPerspective().getNotifier().setNotification(new Notification(Labels.getLabel("security.permision_denied")));
    }
}
