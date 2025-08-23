package de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.components.mvc.tree.TreeController;
import de.hybris.platform.cockpit.components.mvc.tree.events.ExtendedDropEvent;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.CustomActionListener;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.DoubleClickListener;
import de.hybris.platform.cockpit.components.mvc.tree.view.DefaultNodeWithActionsRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.configurablebundleservices.enums.BundleTemplateStatusEnum;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Box;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

public class BundleNavigationNodeRenderer extends DefaultNodeWithActionsRenderer
{
    protected static final String SYNCHRONIZATION_STATUS_OK = "cockpit/images/icon_status_sync.png";
    protected static final String SYNCHRONIZATION_TOOLTIP_OK = "cmscockpit.navigationnodes.synchornized";
    protected static final String SYNCHRONIZATION_STATUS_NOT_OK = "cockpit/images/icon_status_unsync.png";
    protected static final String SYNCHRONIZATION_TOOLTIP_NOT_OK = "cmscockpit.navigationnodes.notSynchornized";
    protected static final String SYNCHRONIZATION_STATUS_NA = "cockpit/images/icon_status_sync_unavailable.png";
    protected static final String SYNCHRONIZATION_TOOLTIP_NA = "cmscockpit.navigationnodes.sync_na";
    protected static final String NAVIGATION_NODE_TREE_ROW_SCLASS = "navigationTreeRow";
    private static final String SYNCHRONIZATION_SERVICE = "synchronizationService";
    private TreeController controller;
    private LabelService labelService;
    private SynchronizationService synchronizationService;
    private BundleNavigationActionsRenderer actionsRenderer;


    public BundleNavigationNodeRenderer()
    {
    }


    public BundleNavigationNodeRenderer(EventListener dropListener)
    {
        super(dropListener);
    }


    public void render(Treeitem item, Object data) throws Exception
    {
        item.addEventListener("onOpen", event -> this.controller.customAction(null, event, data));
        super.render(item, data);
        Treerow treerow = item.getTreerow();
        if(treerow != null)
        {
            treerow.setDroppable("false");
            treerow.setDraggable("false");
        }
    }


    public Treerow renderRow(Treeitem treeItem, Object node)
    {
        Treerow treeRow;
        treeItem.setSclass("navigationTreeRow");
        TypedObject typedObject = (TypedObject)node;
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
        treeRow.addEventListener("onDoubleClick", (EventListener)new DoubleClickListener());
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
        BundleTemplateModel bundleTemplateModel = (BundleTemplateModel)typedObject.getObject();
        String droppable = "false";
        if(CollectionUtils.isEmpty(bundleTemplateModel.getChildTemplates()))
        {
            droppable = "true";
        }
        Treecell labelCell = new Treecell();
        labelCell.setDroppable(droppable);
        labelCell.setParent((Component)treeRow);
        labelCell.addEventListener("onDrop", event -> {
            if(event instanceof DropEvent)
            {
                sendExtendedDropEvent((DropEvent)event, true, true);
            }
        });
        Box labelBox = new Box();
        labelBox.setParent((Component)labelCell);
        labelBox.setOrient("horizontal");
        labelBox.setSclass("navigationNodesDropSlotVbox");
        SynchronizationService.SyncContext nodeSyncCtx = computeSyncContext(typedObject);
        boolean activeVersion = isActiveCatalog(nodeSyncCtx);
        String synchImageUrl = getSyncImageUrl(nodeSyncCtx);
        if(!activeVersion && StringUtils.isNotBlank(synchImageUrl))
        {
            Toolbarbutton syncButton = new Toolbarbutton("", synchImageUrl);
            syncButton.setTooltiptext(Labels.getLabel(getSyncTooltip(nodeSyncCtx)));
            syncButton.addEventListener("onClick", (EventListener)new CustomActionListener());
            if(testIDsEnabled)
            {
                UITools.applyTestID((Component)syncButton, "synNN");
            }
            labelBox.appendChild((Component)syncButton);
        }
        Label label = new Label(getLabelService().getObjectTextLabelForTypedObject(typedObject));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)label, "navigationNode#" + label.hashCode());
        }
        updateLabelIfArchived(bundleTemplateModel, label);
        label.setDroppable("false");
        labelBox.appendChild((Component)label);
    }


    protected boolean isActiveCatalog(SynchronizationService.SyncContext nodeSyncCtx)
    {
        boolean activeVersion = false;
        for(CatalogVersionModel catmodel : nodeSyncCtx.getSourceCatalogVersions())
        {
            if(catmodel.getActive().booleanValue() && !activeVersion)
            {
                activeVersion = catmodel.getActive().booleanValue();
            }
        }
        return activeVersion;
    }


    protected void updateLabelIfArchived(BundleTemplateModel bundleTemplateModel, Label label)
    {
        if(bundleTemplateModel.getParentTemplate() == null && BundleTemplateStatusEnum.ARCHIVED
                        .equals(bundleTemplateModel.getStatus().getStatus()))
        {
            label.setValue(Labels.getLabel("configurablebundlecockpits.bundle.archive.prefix") + " " + Labels.getLabel("configurablebundlecockpits.bundle.archive.prefix"));
        }
    }


    protected void addActions(Treecell actionsCell, Object data)
    {
        getActionsRenderer().addActions(actionsCell, data);
    }


    protected String computeSynchImageUrl(TypedObject currentNode)
    {
        if(currentNode.getObject() instanceof BundleTemplateModel)
        {
            SynchronizationService.SyncContext syncContext = getSynchronizationService().getSyncContext(currentNode);
            return getSyncImageUrl(syncContext);
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
        if(currentNode.getObject() instanceof BundleTemplateModel)
        {
            return getSynchronizationService().getSyncContext(currentNode);
        }
        return null;
    }


    protected void sendExtendedDropEvent(DropEvent dropEvent, boolean addAsChild, boolean append)
    {
        Tree tree = null;
        Component target = null;
        tree = ((Treerow)dropEvent.getTarget().getParent()).getTree();
        target = dropEvent.getTarget().getParent();
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


    protected SynchronizationService getSynchronizationService()
    {
        if(this.synchronizationService == null)
        {
            this.synchronizationService = (SynchronizationService)SpringUtil.getBean("synchronizationService");
        }
        return this.synchronizationService;
    }


    public TreeController getController()
    {
        return this.controller;
    }


    public void setController(TreeController controller)
    {
        this.controller = controller;
    }


    public BundleNavigationActionsRenderer getActionsRenderer()
    {
        return this.actionsRenderer;
    }


    public void setActionsRenderer(BundleNavigationActionsRenderer actionsRenderer)
    {
        this.actionsRenderer = actionsRenderer;
    }
}
