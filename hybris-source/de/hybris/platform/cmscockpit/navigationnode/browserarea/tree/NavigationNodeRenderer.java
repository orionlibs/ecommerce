package de.hybris.platform.cmscockpit.navigationnode.browserarea.tree;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cockpit.components.mvc.tree.events.ExtendedDropEvent;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.AddChildNodeListener;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.AddNodeListener;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.CustomActionListener;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.DeleteListener;
import de.hybris.platform.cockpit.components.mvc.tree.view.DefaultNodeWithActionsRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vbox;

public class NavigationNodeRenderer extends DefaultNodeWithActionsRenderer
{
    protected static final String SYNCHRONIZATION_STATUS_OK = "cockpit/images/icon_status_sync.png";
    protected static final String SYNCHRONIZATION_TOOLTIP_OK = "cmscockpit.navigationnodes.synchornized";
    protected static final String SYNCHRONIZATION_STATUS_NOT_OK = "cockpit/images/icon_status_unsync.png";
    protected static final String SYNCHRONIZATION_TOOLTIP_NOT_OK = "cmscockpit.navigationnodes.notSynchornized";
    protected static final String SYNCHRONIZATION_STATUS_NA = "cockpit/images/icon_status_sync_unavailable.png";
    protected static final String SYNCHRONIZATION_TOOLTIP_NA = "cmscockpit.navigationnodes.sync_na";
    protected static final String ADD_AS_SIBLING_TOOLTIP = "cmscockpit.navigationnodes.addAsASibling";
    protected static final String ADD_AS_CHILD_TOOLTIP = "cmscockpit.navigationnodes.addAsAChild";
    protected static final String REMOVE_NN_TOOLTIP = "cmscockpit.navigationnodes.remove";
    protected static final String EDIT_NN_TOOLTIP = "cmscockpit.navigationnodes.edit";
    protected static final String NAVIGATION_NODE_TREE_ROW_SCLASS = "navigationTreeRow";
    private static final String CMS_NAVIGATION_SERVICE = "cmsNavigationService";
    private static final String SYNCHRONIZATION_SERVICE = "synchronizationService";
    private LabelService labelService;
    private SystemService systemService;
    private SynchronizationService synchronizationService;
    private CMSNavigationService cmsNavigationService;
    private TypeService typeService;


    public NavigationNodeRenderer()
    {
    }


    public NavigationNodeRenderer(EventListener dropListener)
    {
        super(dropListener);
    }


    public void render(Treeitem item, Object data) throws Exception
    {
        super.render(item, data);
        Treerow treerow = item.getTreerow();
        if(treerow != null)
        {
            treerow.setDroppable("false");
        }
    }


    public Treerow renderRow(Treeitem treeItem, Object node)
    {
        treeItem.setSclass("navigationTreeRow");
        TypedObject typedObject = (TypedObject)node;
        Treerow treeRow = null;
        treeItem.setValue(node);
        if(treeItem.getTreerow() == null)
        {
            treeRow = new Treerow();
            treeRow.setParent((Component)treeItem);
        }
        else
        {
            treeRow = treeItem.getTreerow();
            treeRow.getChildren().clear();
        }
        prepareLabelCell(typedObject, treeRow);
        prepareActionsNode(typedObject, treeRow);
        return treeRow;
    }


    protected void prepareActionsNode(Object node, Treerow treeRow)
    {
        Treecell actionsCell = new Treecell();
        actionsCell.setStyle("text-align:right;");
        addActions(actionsCell, node);
        actionsCell.setParent((Component)treeRow);
    }


    protected void prepareLabelCell(TypedObject typedObject, Treerow treeRow)
    {
        boolean testIDsEnabled = UISessionUtils.getCurrentSession().isUsingTestIDs();
        Treecell labelCell = new Treecell();
        labelCell.setDroppable("false");
        labelCell.setParent((Component)treeRow);
        Vbox vertical = new Vbox();
        vertical.setSclass("navigationNodesDropSlotVbox");
        vertical.setParent((Component)labelCell);
        Div topDrop = new Div();
        topDrop.setDroppable("true");
        if(testIDsEnabled)
        {
            UITools.applyTestID((Component)topDrop, "nnDropSlotUp");
        }
        topDrop.setParent((Component)vertical);
        topDrop.setSclass("navigationNodesDropSlot");
        UITools.modifySClass((HtmlBasedComponent)topDrop, "dropSlotTop", true);
        topDrop.addEventListener("onDrop", (EventListener)new Object(this));
        Label label = new Label(getLabelService().getObjectTextLabelForTypedObject(typedObject));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)label, "navigationNode#" + label.hashCode());
        }
        label.setDroppable("true");
        label.setParent((Component)vertical);
        label.addEventListener("onDrop", (EventListener)new Object(this));
        Div bottomDrop = new Div();
        bottomDrop.setDroppable("true");
        if(testIDsEnabled)
        {
            UITools.applyTestID((Component)bottomDrop, "nnDropSlotDown");
        }
        bottomDrop.setParent((Component)vertical);
        bottomDrop.setSclass("navigationNodesDropSlot");
        UITools.modifySClass((HtmlBasedComponent)bottomDrop, "dropSlotBottom", true);
        bottomDrop.addEventListener("onDrop", (EventListener)new Object(this));
    }


    protected void addActions(Treecell actionsCell, Object data)
    {
        boolean testIDsEnabled = UISessionUtils.getCurrentSession().isUsingTestIDs();
        Div actionsCellCnt = new Div();
        actionsCellCnt.setParent((Component)actionsCell);
        TypedObject currentNode = (TypedObject)data;
        if(getSystemService().checkPermissionOn(currentNode.getType().getCode(), "create"))
        {
            Toolbarbutton addButton = new Toolbarbutton("", "/cmscockpit/images/node_duplicate.png");
            addButton.setTooltiptext(Labels.getLabel("cmscockpit.navigationnodes.addAsASibling"));
            addButton.setStyle("margin-left:3px");
            addButton.addEventListener("onClick", (EventListener)new AddNodeListener());
            if(testIDsEnabled)
            {
                UITools.applyTestID((Component)addButton, "addNN");
            }
            actionsCellCnt.appendChild((Component)addButton);
            Toolbarbutton addChild = new Toolbarbutton("", "/cmscockpit/images/node_add_child.png");
            addChild.setTooltiptext(Labels.getLabel("cmscockpit.navigationnodes.addAsAChild"));
            addChild.setStyle("margin-left:3px");
            addChild.addEventListener("onClick", (EventListener)new AddChildNodeListener());
            if(testIDsEnabled)
            {
                UITools.applyTestID((Component)addChild, "addSubNN");
            }
            actionsCellCnt.appendChild((Component)addChild);
        }
        if(getSystemService().checkPermissionOn(currentNode.getType().getCode(), "change"))
        {
            Toolbarbutton editButton = new Toolbarbutton("", "/cockpit/images/item_edit_action.png");
            editButton.setTooltiptext(Labels.getLabel("cmscockpit.navigationnodes.edit"));
            editButton.setStyle("margin-left:4px");
            editButton.addEventListener("onClick", (EventListener)new Object(this, currentNode));
            if(testIDsEnabled)
            {
                UITools.applyTestID((Component)editButton, "editNN");
            }
            actionsCellCnt.appendChild((Component)editButton);
        }
        SynchronizationService.SyncContext nodeSyncCtx = computeSyncContext(currentNode);
        String synchImageUrl = getSyncImageUrl(nodeSyncCtx);
        if(StringUtils.isNotBlank(synchImageUrl))
        {
            Toolbarbutton syncButton = new Toolbarbutton("", synchImageUrl);
            syncButton.setTooltiptext(Labels.getLabel(getSyncTooltip(nodeSyncCtx)));
            syncButton.setStyle("margin-left:4px");
            syncButton.addEventListener("onClick", (EventListener)new CustomActionListener());
            if(testIDsEnabled)
            {
                UITools.applyTestID((Component)syncButton, "synNN");
            }
            actionsCellCnt.appendChild((Component)syncButton);
        }
        if(getSystemService().checkPermissionOn(currentNode.getType().getCode(), "remove"))
        {
            Toolbarbutton deleteButton = new Toolbarbutton("", "/cmscockpit/images/node_delete.png");
            deleteButton.setTooltiptext(Labels.getLabel("cmscockpit.navigationnodes.remove"));
            deleteButton.setStyle("margin-left:2px");
            deleteButton.addEventListener("onClick", (EventListener)new DeleteListener());
            if(testIDsEnabled)
            {
                UITools.applyTestID((Component)deleteButton, "delNN");
            }
            actionsCellCnt.appendChild((Component)deleteButton);
        }
        actionsCellCnt.setVisible(false);
    }


    protected String computeSynchImageUrl(TypedObject currentNode)
    {
        if(currentNode.getObject() instanceof CMSNavigationNodeModel)
        {
            CMSNavigationNodeModel navigationNodeModel = (CMSNavigationNodeModel)currentNode.getObject();
            if(navigationNodeModel.getParent() != null)
            {
                CMSNavigationNodeModel parentNodeModel = navigationNodeModel.getParent();
                CatalogVersionModel sourceCatalogVersionModel = getSynchronizationService().getCatalogVersionForItem(currentNode);
                CMSNavigationNodeModel superRootNodeModel = getCmsNavigationService().getSuperRootNavigationNode(sourceCatalogVersionModel);
                SynchronizationService.SyncContext syncContext = getSynchronizationService().getSyncContext(currentNode);
                if(superRootNodeModel.equals(parentNodeModel))
                {
                    return getSyncImageUrl(syncContext);
                }
                SynchronizationService.SyncContext parentSyncContext = getSynchronizationService().getSyncContext(
                                getTypeService().wrapItem(parentNodeModel));
                if(parentSyncContext.isProductSynchronized() == 0)
                {
                    return getSyncImageUrl(syncContext);
                }
            }
        }
        return "";
    }


    protected String getSyncImageUrl(SynchronizationService.SyncContext syncContext)
    {
        if(syncContext == null)
        {
            return "cockpit/images/icon_status_sync_unavailable.png";
        }
        if(syncContext.isProductSynchronized() == 0)
        {
            return "cockpit/images/icon_status_sync.png";
        }
        if(syncContext.isProductSynchronized() == 1)
        {
            return "cockpit/images/icon_status_unsync.png";
        }
        if(syncContext.isProductSynchronized() == -1)
        {
            return "cockpit/images/icon_status_sync_unavailable.png";
        }
        return "";
    }


    protected String getSyncTooltip(SynchronizationService.SyncContext syncContext)
    {
        if(syncContext == null)
        {
            return "cmscockpit.navigationnodes.sync_na";
        }
        if(syncContext.isProductSynchronized() == 0)
        {
            return "cmscockpit.navigationnodes.synchornized";
        }
        if(syncContext.isProductSynchronized() == 1)
        {
            return "cmscockpit.navigationnodes.notSynchornized";
        }
        if(syncContext.isProductSynchronized() == -1)
        {
            return "cmscockpit.navigationnodes.sync_na";
        }
        return "";
    }


    protected SynchronizationService.SyncContext computeSyncContext(TypedObject currentNode)
    {
        if(currentNode.getObject() instanceof CMSNavigationNodeModel)
        {
            CMSNavigationNodeModel navigationNodeModel = (CMSNavigationNodeModel)currentNode.getObject();
            if(navigationNodeModel.getParent() != null)
            {
                CMSNavigationNodeModel parentNodeModel = navigationNodeModel.getParent();
                CatalogVersionModel sourceCatalogVersionModel = getSynchronizationService().getCatalogVersionForItem(currentNode);
                CMSNavigationNodeModel superRootNodeModel = getCmsNavigationService().getSuperRootNavigationNode(sourceCatalogVersionModel);
                SynchronizationService.SyncContext syncContext = getSynchronizationService().getSyncContext(currentNode);
                if(superRootNodeModel.equals(parentNodeModel))
                {
                    return syncContext;
                }
                SynchronizationService.SyncContext parentSyncContext = getSynchronizationService().getSyncContext(
                                getTypeService().wrapItem(parentNodeModel));
                if(parentSyncContext.isProductSynchronized() == 0)
                {
                    return syncContext;
                }
            }
        }
        return null;
    }


    protected void sendExtendedDropEvent(DropEvent dropEvent, boolean addAsChild, boolean append)
    {
        Tree tree = null;
        Component target = null;
        tree = ((Treerow)dropEvent.getTarget().getParent().getParent().getParent()).getTree();
        target = dropEvent.getTarget().getParent().getParent().getParent();
        Component source = dropEvent.getDragged();
        if(target != source)
        {
            ExtendedDropEvent extendedDropEvent = new ExtendedDropEvent("onSomethingWasDropped", target, source, dropEvent.getX(), dropEvent.getY(), dropEvent.getKeys(), addAsChild, append);
            Events.sendEvent((Component)tree, (Event)extendedDropEvent);
        }
    }


    protected void registerDefaultOnDoubleClickListeners(Treerow treeRow)
    {
    }


    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }


    public SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    public void setSynchronizationService(SynchronizationService synchronizationService)
    {
        this.synchronizationService = synchronizationService;
    }


    protected SynchronizationService getSynchronizationService()
    {
        if(this.synchronizationService == null)
        {
            this.synchronizationService = (SynchronizationService)SpringUtil.getBean("synchronizationService");
        }
        return this.synchronizationService;
    }


    public void setNavigationService(CMSNavigationService cmsNavigationService)
    {
        this.cmsNavigationService = cmsNavigationService;
    }


    protected CMSNavigationService getCmsNavigationService()
    {
        if(this.cmsNavigationService == null)
        {
            this.cmsNavigationService = (CMSNavigationService)SpringUtil.getBean("cmsNavigationService");
        }
        return this.cmsNavigationService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
