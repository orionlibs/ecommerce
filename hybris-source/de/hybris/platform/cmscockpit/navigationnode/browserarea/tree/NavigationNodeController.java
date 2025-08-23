package de.hybris.platform.cmscockpit.navigationnode.browserarea.tree;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cockpit.components.ComponentsHelper;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.mvc.tree.PositionAwareTreeController;
import de.hybris.platform.cockpit.components.mvc.tree.Tree;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardContext;
import de.hybris.platform.cockpit.wizards.impl.DefaultWizardContext;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

public class NavigationNodeController implements PositionAwareTreeController<TypedObject>
{
    private static final Logger LOG = Logger.getLogger(NavigationNodeController.class);
    private static final String CMS_NAVIGATION_SERVICE = "cmsNavigationService";
    private static final String SYNCHRONIZATION_SERVICE = "synchronizationService";
    private static final String SYNC_JOBS_PARAM = "syncJobs";
    private CMSNavigationService cmsNavigationService;
    private TypeService typeService;
    private ModelService modelService;
    private SystemService systemService;
    private SynchronizationService synchronizationService;
    private String navigationNodeWizardId;
    private String relatedResourceWizardId;
    private Set<TypedObject> selectedItems;


    public void selected(Tree tree, Set<Treeitem> selectedTreeItems)
    {
        showNodeActions(tree, selectedTreeItems);
        this.selectedItems = extractSelectedItem(selectedTreeItems);
    }


    public Set<TypedObject> getSelected()
    {
        return this.selectedItems;
    }


    protected void showNodeActions(Tree tree, Set<Treeitem> selectedItems)
    {
        Collection<Treeitem> treeChildren = tree.getTreechildren().getItems();
        if(!CollectionUtils.isEmpty(treeChildren))
        {
            for(Treeitem treeitem : treeChildren)
            {
                ((Treecell)treeitem.getTreerow().getChildren().get(1)).getFirstChild().setVisible(false);
            }
            if(selectedItems != null && !selectedItems.isEmpty())
            {
                ((Treecell)((Treeitem)selectedItems.iterator().next()).getTreerow().getChildren().get(1)).getFirstChild().setVisible(true);
            }
        }
    }


    public TypedObject create(Tree tree, TypedObject target)
    {
        if(target == null)
        {
            target = (TypedObject)tree.getModel().getRoot();
            return create(tree, target, 0);
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
        TypedObject ret = null;
        if(StringUtils.isNotEmpty(getNavigationNodeWizardId()))
        {
            DefaultWizardContext ctx = new DefaultWizardContext();
            ctx.setAttribute("target", target);
            ctx.setAttribute("index", Integer.valueOf(index));
            Map<String, Object> map = new HashMap<>();
            TypedObject wrappedCatalogVersion = (TypedObject)tree.getModel().getRoot();
            map.put("CMSNavigationNode.catalogVersion", wrappedCatalogVersion);
            ctx.setAttribute("predefinedPropertyValues", map);
            ctx.setAttribute("currentCatalogVersion", wrappedCatalogVersion);
            ctx.setAttribute("finalizeWizard", new Object(this, tree, target));
            Wizard.show(getNavigationNodeWizardId(), (WizardContext)ctx);
        }
        return ret;
    }


    public void add(Tree tree, Object object, TypedObject target)
    {
        if(object instanceof Listitem)
        {
            Listitem listitem = (Listitem)object;
            Object value = listitem.getValue();
            if(value instanceof TypedObject)
            {
                Window modal = getMoveItemModalDialog(tree, (TypedObject)value, target, getCmsNavigationService());
                modal.doHighlighted();
            }
        }
    }


    public void move(Tree tree, TypedObject node, TypedObject target, boolean addAsChild)
    {
        move(tree, node, target, addAsChild, false);
    }


    public void move(Tree tree, TypedObject node, TypedObject target, boolean addAsChild, boolean append)
    {
        CMSNavigationNodeModel nodeModel = (CMSNavigationNodeModel)node.getObject();
        CMSNavigationNodeModel targetModel = (CMSNavigationNodeModel)target.getObject();
        boolean canMove = true;
        CMSNavigationNodeModel parent = targetModel.getParent();
        while(parent != null)
        {
            if(parent.equals(nodeModel) || targetModel.equals(nodeModel))
            {
                canMove = false;
                break;
            }
            parent = parent.getParent();
        }
        if(!canMove)
        {
            ComponentsHelper.displayNotification("general.error", "cmscockpit.navigationnode.drag.error", new Object[0]);
            return;
        }
        getCmsNavigationService().move(nodeModel, targetModel, addAsChild, append);
        refresh(tree);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("tree invalidated after move");
        }
    }


    public static Window getMoveItemModalDialog(Tree tree, TypedObject typedObject, TypedObject targetNodeObj, CMSNavigationService cmsNavigationService)
    {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("title", Labels.getLabel("general.choose"));
        Button moveNodeButton = new Button(Labels.getLabel("general.move"));
        moveNodeButton.addEventListener("onClick", (EventListener)new Object(typedObject, targetNodeObj, tree, cmsNavigationService, moveNodeButton));
        arguments.put("moveNodeButton", moveNodeButton);
        Button copyNodeButton = new Button(Labels.getLabel("general.copy"));
        copyNodeButton.addEventListener("onClick", (EventListener)new Object(targetNodeObj, cmsNavigationService, typedObject, copyNodeButton));
        arguments.put("copyNodeButton", copyNodeButton);
        return (Window)Executions.createComponents("/cmscockpit/messagedialog.zul", null, arguments);
    }


    public void add(Tree tree, Object object, TypedObject target, int index)
    {
        if(object == null)
        {
            create(tree, target, index);
        }
    }


    public void delete(Tree tree, TypedObject node)
    {
        if(getSystemService().checkPermissionOn(node.getType().getCode(), "remove"))
        {
            ComponentsHelper.displayConfirmationPopup("", Labels.getLabel("general.confirm.delete"), (EventListener)new DeleteListener(this, tree, node));
        }
    }


    public void doubleClicked(Tree tree, TypedObject currentNode)
    {
        UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        currentPerspective.activateItemInEditor(currentNode);
    }


    protected Set<TypedObject> extractSelectedItem(Set<Treeitem> selectedItems)
    {
        Set<TypedObject> selectedItem = new HashSet<>();
        for(Treeitem treeitem : selectedItems)
        {
            selectedItem.add((TypedObject)treeitem.getValue());
        }
        return selectedItem;
    }


    protected List<List<Integer>> getOpenedNodes(Tree tree, int depth)
    {
        List<List<Integer>> ret = new ArrayList<>();
        for(Object treeitem : tree.getItems())
        {
            Treeitem treeItem = (Treeitem)treeitem;
            if(treeItem.getLevel() <= depth && treeItem.isOpen() && treeItem.getTreechildren() != null && treeItem
                            .getTreechildren().getChildren().size() > 0)
            {
                List<Integer> path = getPathToRoot(treeItem);
                if(!path.isEmpty())
                {
                    ret.add(path);
                }
            }
        }
        return ret;
    }


    public void openCreatedNode(Tree tree, TypedObject typedObject, boolean addAsAChild)
    {
        if(tree == null || typedObject == null)
        {
            return;
        }
        Treeitem treeitem = tree.getSelectedItem();
        if(treeitem != null)
        {
            treeitem.setOpen(addAsAChild);
        }
        if(this.selectedItems == null)
        {
            this.selectedItems = new HashSet<>();
        }
        else
        {
            this.selectedItems.clear();
            this.selectedItems.add(typedObject);
        }
        refresh(tree);
    }


    protected List<Integer> getPathToRoot(Treeitem treeItem)
    {
        List<Integer> ret = new ArrayList<>();
        Treeitem currentItem = treeItem;
        while(currentItem != null)
        {
            ret.add(Integer.valueOf(currentItem.indexOf()));
            if(!currentItem.isOpen() || !currentItem.isVisible())
            {
                ret.clear();
                break;
            }
            currentItem = currentItem.getParentItem();
        }
        Collections.reverse(ret);
        return ret;
    }


    protected void restoreSelectionState(Tree tree)
    {
        if(CollectionUtils.isNotEmpty(this.selectedItems))
        {
            TypedObject selectedItem = this.selectedItems.iterator().next();
            List<Treeitem> treeitems = tree.getTreechildren().getChildren();
            Treeitem treeitem = searchForSelectedNode(treeitems, selectedItem);
            tree.selectItem(treeitem);
            Event event = new Event("onSelect", (Component)tree, null);
            Events.sendEvent(event);
        }
    }


    protected Treeitem searchForSelectedNode(List<Treeitem> treeitems, TypedObject selectedItem)
    {
        for(Treeitem treeitem : treeitems)
        {
            if(selectedItem.equals(treeitem.getValue()))
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


    protected void restoreOpenedState(Tree tree, List<List<Integer>> openedNodes)
    {
        for(List<Integer> path : openedNodes)
        {
            openPath(tree, path);
        }
    }


    protected void openPath(Tree tree, List<Integer> path)
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


    public CMSNavigationService getCmsNavigationService()
    {
        if(this.cmsNavigationService == null)
        {
            this.cmsNavigationService = (CMSNavigationService)SpringUtil.getBean("cmsNavigationService");
        }
        return this.cmsNavigationService;
    }


    public void setCmsNavigationService(CMSNavigationService navigationNodeService)
    {
        this.cmsNavigationService = navigationNodeService;
    }


    protected List<SyncItemJobModel>[] getSyncJobs(ListViewAction.Context context)
    {
        return (List<SyncItemJobModel>[])context.getMap().get("syncJobs");
    }


    public Object customAction(Tree tree, Event event, TypedObject node)
    {
        SynchronizationService.SyncContext syncContext = getSynchronizationService().getSyncContext(node);
        CatalogVersionModel sourceCatalogVersionModel = getSynchronizationService().getCatalogVersionForItem(node);
        if(syncContext.isProductSynchronized() != 0)
        {
            try
            {
                List[] arrayOfList = syncContext.getSyncJobs();
                int size = arrayOfList[0].size() + arrayOfList[1].size();
                if(size > 1)
                {
                    Object object = new Object(this, node, sourceCatalogVersionModel, arrayOfList, tree, node);
                    detachDialog((Window)object);
                    try
                    {
                        event.getTarget().getRoot().appendChild((Component)object);
                        object.doHighlighted();
                    }
                    catch(Exception e)
                    {
                        LOG.warn("Could not open synchronization dialog (Reason: '" + e.getMessage() + "')", e);
                    }
                }
                else if(arrayOfList[0].size() == 1 && arrayOfList[1].size() == 0)
                {
                    Clients.showBusy(Labels.getLabel("busy.sync"), true);
                    for(SyncItemJobModel job : sourceCatalogVersionModel.getSynchronizations())
                    {
                        CatalogVersionModel targetVersion = job.getTargetVersion();
                        if(this.cmsNavigationService.getSuperRootNavigationNode(targetVersion) == null)
                        {
                            CMSNavigationNodeModel root = this.cmsNavigationService.createSuperRootNavigationNode(targetVersion);
                            getModelService().save(root);
                        }
                    }
                    getSynchronizationService().performSynchronization(Collections.singletonList(node.getObject()), null, null, null);
                    sendUpdateEvents(tree, node);
                }
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
            finally
            {
                Clients.showBusy(null, false);
            }
        }
        return null;
    }


    protected void sendUpdateEvents(Tree tree, TypedObject node)
    {
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(tree, node, null));
        BaseUICockpitPerspective perspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
        perspective.getNavigationArea().update();
        for(BrowserModel visBrowser : perspective.getBrowserArea().getVisibleBrowsers())
        {
            visBrowser.updateItems();
        }
    }


    public void refresh(Tree tree)
    {
        List<List<Integer>> openedNodes = getOpenedNodes(tree, 99999);
        tree.setModel((TreeModel)new TreeModel((TypedObject)tree.getModel().getRoot()));
        tree.invalidate();
        restoreOpenedState(tree, openedNodes);
        restoreSelectionState(tree);
    }


    protected void detachDialog(Window dialog)
    {
        dialog.addEventListener("onOpen", (EventListener)new Object(this, dialog));
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = UISessionUtils.getCurrentSession().getModelService();
        }
        return this.modelService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
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


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public String getNavigationNodeWizardId()
    {
        return this.navigationNodeWizardId;
    }


    public void setNavigationNodeWizardId(String navigationNodeWizardId)
    {
        this.navigationNodeWizardId = navigationNodeWizardId;
    }


    public void setSynchronizationService(SynchronizationService synchronizationService)
    {
        this.synchronizationService = synchronizationService;
    }


    public String getRelatedResourceWizardId()
    {
        return this.relatedResourceWizardId;
    }


    public void setRelatedResourceWizardId(String relatedResourceWizardId)
    {
        this.relatedResourceWizardId = relatedResourceWizardId;
    }
}
