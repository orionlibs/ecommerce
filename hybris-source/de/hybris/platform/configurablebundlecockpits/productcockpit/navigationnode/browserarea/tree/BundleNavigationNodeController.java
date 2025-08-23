package de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree;

import com.google.common.collect.Sets;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.components.ComponentsHelper;
import de.hybris.platform.cockpit.components.mvc.tree.Tree;
import de.hybris.platform.cockpit.components.mvc.tree.TreeController;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sync.dialog.OneSourceManyTargetItemSyncDialog;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.configurablebundlecockpits.servicelayer.services.BundleNavigationService;
import de.hybris.platform.configurablebundleservices.enums.BundleTemplateStatusEnum;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

public class BundleNavigationNodeController implements TreeController<TypedObject>
{
    private static final Logger LOG = Logger.getLogger(BundleNavigationNodeController.class);
    private static final String SYNCHRONIZATION_SERVICE = "synchronizationService";
    private BundleNavigationService bundleNavigationService;
    private ModelService modelService;
    private SystemService systemService;
    private SynchronizationService synchronizationService;
    private Set<TypedObject> selectedItems = Sets.newHashSet();
    private FlexibleSearchService flexibleSearchService;
    private BundleTreeController bundleTreeController;


    public void selected(Tree tree, Set<Treeitem> selectedTreeItems)
    {
        Collection<Treeitem> treeChildren = tree.getTreechildren().getItems();
        if(!CollectionUtils.isEmpty(treeChildren))
        {
            for(Treeitem treeitem : treeChildren)
            {
                ((Treecell)treeitem.getTreerow().getChildren().get(1)).getFirstChild().setVisible(false);
            }
            if(selectedTreeItems != null && !selectedTreeItems.isEmpty())
            {
                ((Treecell)((Treeitem)selectedTreeItems.iterator().next()).getTreerow().getChildren().get(1)).getFirstChild().setVisible(true);
            }
        }
        Set<TypedObject> selectedItem = new HashSet<>();
        for(Treeitem treeitem : selectedTreeItems)
        {
            selectedItem.add((TypedObject)treeitem.getValue());
        }
        this.selectedItems = selectedItem;
    }


    public Set<TypedObject> getSelected()
    {
        return this.selectedItems;
    }


    public TypedObject create(Tree tree, TypedObject target)
    {
        if(target == null)
        {
            TypedObject treeModelRoot = (TypedObject)tree.getModel().getRoot();
            return create(tree, treeModelRoot, 0);
        }
        if(tree.getSelectedItem() != null)
        {
            int index = tree.getSelectedItem().indexOf();
            return create(tree, target, index);
        }
        return null;
    }


    public TypedObject create(Tree tree, TypedObject target, int index)
    {
        throw new UnsupportedOperationException();
    }


    public void add(Tree tree, Object object, TypedObject target)
    {
        DraggedItem draggedItem = UISessionUtils.getCurrentSession().getCurrentPerspective().getDragAndDropWrapperService().getWrapper().getDraggedItem((Component)object);
        if(draggedItem.getSingleTypedObject() != null)
        {
            List<TypedObject> typedObjects = new ArrayList<>(draggedItem.getAllTypedObjects());
            List<ProductModel> draggedObjects = new ArrayList<>();
            Iterator<TypedObject> it = typedObjects.iterator();
            while(it.hasNext())
            {
                draggedObjects.add((ProductModel)((TypedObject)it.next()).getObject());
            }
            BundleTemplateModel targetBundleTemplateModel = (BundleTemplateModel)target.getObject();
            this.bundleNavigationService.add(targetBundleTemplateModel, draggedObjects);
            UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser().updateItems();
        }
        if(object instanceof Listitem)
        {
            Listitem listitem = (Listitem)object;
            Object value = listitem.getValue();
            if(value instanceof TypedObject)
            {
                Window modal = MoveItemModalDialogRenderer.getMoveItemModalDialog((Tree)tree, (TypedObject)value, target, getBundleNavigationService());
                modal.doHighlighted();
            }
        }
    }


    public void move(Tree tree, TypedObject node, TypedObject target, boolean addAsChild)
    {
        ComponentsHelper.displayNotification("general.error", "cmscockpit.navigationnode.drag.error", new Object[0]);
    }


    public void add(Tree tree, Object object, TypedObject target, int index)
    {
    }


    public void delete(Tree tree, TypedObject node)
    {
        if(!getSystemService().checkPermissionOn(node.getType().getCode(), "remove"))
        {
            return;
        }
        String message = "";
        BundleTemplateModel templateModel = (BundleTemplateModel)node.getObject();
        BundleTemplateStatusModel statusModel = templateModel.getStatus();
        if(templateModel.getParentTemplate() == null)
        {
            if(BundleTemplateStatusEnum.ARCHIVED.equals(statusModel.getStatus()))
            {
                message = Labels.getLabel("configurablebundlecockpits.bundle.restore.confirmationMessage");
            }
            else
            {
                message = Labels.getLabel("configurablebundlecockpits.bundle.archive.confirmationMessage");
            }
        }
        else if(isBundleTemplateDeletable(templateModel))
        {
            message = Labels.getLabel("configurablebundlecockpits.bundle.delete.confirmationMessage");
        }
        else
        {
            BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(basePerspective.getNotifier() == null)
            {
                return;
            }
            basePerspective.getNotifier().setDialogNotification(new Notification(
                            Labels.getLabel("configurablebundlecockpits.bundle.delete.notallowedHeader"),
                            Labels.getLabel("configurablebundlecockpits.bundle.delete.notallowedMessage")));
            return;
        }
        ComponentsHelper.displayConfirmationPopup("", message, (EventListener)new DeleteListener(this, tree, node));
    }


    public void doubleClicked(Tree tree, TypedObject currentNode)
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().activateItemInEditor(currentNode);
    }


    public BundleNavigationService getBundleNavigationService()
    {
        if(this.bundleNavigationService == null)
        {
            this.bundleNavigationService = (BundleNavigationService)SpringUtil.getBean("bundleNavigationService");
        }
        return this.bundleNavigationService;
    }


    public void setBundleNavigationService(BundleNavigationService bundleNavigationService)
    {
        this.bundleNavigationService = bundleNavigationService;
    }


    public Object customAction(Tree tree, Event event, TypedObject node)
    {
        SynchronizationService.SyncContext syncContext = getSynchronizationService().getSyncContext(node);
        CatalogVersionModel sourceCatalogVersionModel = getSynchronizationService().getCatalogVersionForItem(node);
        if(syncContext.isProductSynchronized() != 0)
        {
            synchronize(tree, event, node, syncContext, sourceCatalogVersionModel);
        }
        return null;
    }


    private void synchronize(Tree tree, Event event, TypedObject node, SynchronizationService.SyncContext syncContext, CatalogVersionModel sourceCatalogVersionModel)
    {
        try
        {
            List[] arrayOfList = syncContext.getSyncJobs();
            int size = arrayOfList[0].size() + arrayOfList[1].size();
            if(size > 1)
            {
                Object object = new Object(this, node, sourceCatalogVersionModel, arrayOfList, tree, node);
                detachDialog((Window)object);
                appendHighlightedDialog(event, (OneSourceManyTargetItemSyncDialog)object);
            }
            else if(arrayOfList[0].size() == 1 && CollectionUtils.isEmpty(arrayOfList[1]))
            {
                Clients.showBusy(Labels.getLabel("busy.sync"), true);
                for(SyncItemJobModel job : sourceCatalogVersionModel.getSynchronizations())
                {
                    LOG.info(job);
                }
                getSynchronizationService().performSynchronization(Collections.singletonList(node.getObject()), null, null, null);
                sendUpdateEvents(tree, node);
            }
        }
        finally
        {
            Clients.showBusy(null, false);
        }
    }


    public void refresh(Tree tree)
    {
        refresh(tree, this.bundleTreeController.getOpenedPath());
    }


    public void refresh(Tree tree, List<List<Integer>> openedNodes)
    {
        tree.setModel((TreeModel)new BundleTemplateTreeModel(tree.getModel().getRoot()));
        tree.invalidate();
        restoreOpenedState(tree, openedNodes);
        restoreSelectionState(tree);
    }


    protected void detachDialog(Window dialog)
    {
        dialog.addEventListener("onOpen", dialogEvent -> {
            if(dialogEvent instanceof OpenEvent && !((OpenEvent)dialogEvent).isOpen())
            {
                dialog.detach();
            }
        });
    }


    public void addProductsToNode(BundleTemplateModel bundleTemplateModel, Iterable<TypedObject> typedProductsToAdd)
    {
        Collection<ProductModel> products = new ArrayList<>();
        for(TypedObject typedObject : typedProductsToAdd)
        {
            products.add((ProductModel)typedObject.getObject());
        }
        this.bundleNavigationService.add(bundleTemplateModel, products);
    }


    private boolean isBundleTemplateDeletable(BundleTemplateModel templateModel)
    {
        List<BundleTemplateModel> bundleTemplates = getAllCatalogVersionsOfBundleTemplate(templateModel);
        if(CollectionUtils.isNotEmpty(bundleTemplates))
        {
            return false;
        }
        BundleTemplateStatusModel statusModel = templateModel.getStatus();
        return !BundleTemplateStatusEnum.APPROVED.equals(statusModel.getStatus());
    }


    private List<BundleTemplateModel> getAllCatalogVersionsOfBundleTemplate(BundleTemplateModel templateModel)
    {
        BundleTemplateModel exampleTemplate = new BundleTemplateModel();
        exampleTemplate.setId(templateModel.getId());
        exampleTemplate.setVersion(templateModel.getVersion());
        return getFlexibleSearchService().getModelsByExample(exampleTemplate);
    }


    private void restoreSelectionState(Tree tree)
    {
        if(CollectionUtils.isEmpty(this.selectedItems))
        {
            return;
        }
        TypedObject selectedItem = this.selectedItems.iterator().next();
        List<Treeitem> treeitems = tree.getTreechildren().getChildren();
        Treeitem treeitem = searchForSelectedNode(treeitems, selectedItem);
        if(treeitem == null)
        {
            return;
        }
        tree.selectItem(treeitem);
        Set<Treeitem> selectedTreeItems = Collections.singleton(treeitem);
        Collection<Treeitem> treeChildren = tree.getTreechildren().getItems();
        if(!CollectionUtils.isEmpty(treeChildren))
        {
            for(Treeitem selectedTreeItem : treeChildren)
            {
                ((Treecell)selectedTreeItem.getTreerow().getChildren().get(1)).getFirstChild().setVisible(false);
            }
            ((Treecell)((Treeitem)selectedTreeItems.iterator().next()).getTreerow().getChildren().get(1)).getFirstChild().setVisible(true);
        }
    }


    private Treeitem searchForSelectedNode(List<Treeitem> treeitems, TypedObject selectedItem)
    {
        for(Treeitem treeitem : treeitems)
        {
            if(selectedItem == treeitem.getValue())
            {
                return treeitem;
            }
            if(treeitem.isOpen() && !treeitem.isEmpty())
            {
                Treeitem foundToBeSelected = searchForSelectedNode(treeitem.getTreechildren().getChildren(), selectedItem);
                if(foundToBeSelected != null)
                {
                    return foundToBeSelected;
                }
            }
        }
        return null;
    }


    private void restoreOpenedState(Tree tree, List<List<Integer>> openedNodes)
    {
        for(List<Integer> path : openedNodes)
        {
            openPath(tree, path);
        }
    }


    private void openPath(Tree tree, List<Integer> path)
    {
        List<Treeitem> treeitems = tree.getTreechildren().getChildren();
        for(Integer integer : path)
        {
            if(integer.intValue() < treeitems.size())
            {
                Treeitem treeItem = treeitems.get(integer.intValue());
                treeItem.setOpen(true);
                if(treeItem.getTreechildren() == null)
                {
                    break;
                }
                treeitems = treeItem.getTreechildren().getChildren();
            }
        }
    }


    private void appendHighlightedDialog(Event event, OneSourceManyTargetItemSyncDialog dialog)
    {
        event.getTarget().getRoot().appendChild((Component)dialog);
        dialog.doHighlighted();
    }


    private void sendUpdateEvents(Tree tree, TypedObject node)
    {
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(tree, node, null));
        BaseUICockpitPerspective perspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
        perspective.getNavigationArea().update();
        for(BrowserModel visBrowser : perspective.getBrowserArea().getVisibleBrowsers())
        {
            visBrowser.updateItems();
        }
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = UISessionUtils.getCurrentSession().getModelService();
        }
        return this.modelService;
    }


    protected SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    protected SynchronizationService getSynchronizationService()
    {
        if(this.synchronizationService == null)
        {
            this.synchronizationService = (SynchronizationService)SpringUtil.getBean("synchronizationService");
        }
        return this.synchronizationService;
    }


    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setSynchronizationService(SynchronizationService synchronizationService)
    {
        this.synchronizationService = synchronizationService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public BundleTreeController getBundleTreeController()
    {
        return this.bundleTreeController;
    }


    public void setBundleTreeController(BundleTreeController bundleTreeController)
    {
        this.bundleTreeController = bundleTreeController;
    }
}
