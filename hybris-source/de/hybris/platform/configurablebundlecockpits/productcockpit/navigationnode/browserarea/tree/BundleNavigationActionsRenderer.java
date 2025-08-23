package de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.DeleteListener;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl.BundleNavigationNodeBrowserModel;
import de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl.type.BundleRuleType;
import de.hybris.platform.configurablebundleservices.enums.BundleTemplateStatusEnum;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.configurablebundleservices.model.ChangeProductPriceBundleRuleModel;
import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Treecell;

public class BundleNavigationActionsRenderer
{
    protected static final String ADD_AS_SIBLING_TOOLTIP = "configurablebundlecockpits.navigationnodes.addAsASibling";
    protected static final String ADD_AS_CHILD_TOOLTIP = "configurablebundlecockpits.navigationnodes.addAsAChild";
    protected static final String REMOVE_NN_TOOLTIP = "configurablebundlecockpits.bundle.delete.tooltip";
    protected static final String ARCHIVE_NN_TOOLTIP = "configurablebundlecockpits.bundle.archive.tooltip";
    protected static final String RESTORE_NN_TOOLTIP = "configurablebundlecockpits.bundle.restore.tooltip";
    protected static final String CLONE_NN_TOOLTIP = "configurablebundlecockpits.navigationnodes.clone";
    protected static final String EDIT_NN_TOOLTIP = "cmscockpit.navigationnodes.edit";
    protected static final String LIST_PRICE_RULES = "configurablebundlecockpits.product.list_changepricerules";
    protected static final String LIST_AVAILABILITY_RULES = "configurablebundlecockpits.product.list_availabilityrules";
    protected static final String ADD_RELATED_ITEMS_BTG_SCLASS = "addRelatedItemsAddBtn";
    private static final String SYNCHRONIZATION_SERVICE = "synchronizationService";
    private static final String MARGIN_4_PX = "margin-left:4px";
    private static final String MARGIN_2_PX = "margin-left:2px";
    private SystemService systemService;
    private SynchronizationService synchronizationService;
    private final Map<String, BundleTemplateModel> templateIdsMap = new HashMap<>();
    private KeyGenerator cloneIdGenerator;


    protected void addActions(Treecell actionsCell, Object data)
    {
        boolean testIDsEnabled = UISessionUtils.getCurrentSession().isUsingTestIDs();
        Div actionsCellCnt = new Div();
        actionsCellCnt.setParent((Component)actionsCell);
        TypedObject currentNode = (TypedObject)data;
        TypedObject bundleTemplate = getTypeService().wrapItem(currentNode.getObject());
        BundleTemplateModel bundleTemplateModel = (BundleTemplateModel)bundleTemplate.getObject();
        BundleTemplateStatusModel templateStatus = bundleTemplateModel.getStatus();
        if(ifNotArchivedAndHavePermission(currentNode, templateStatus, "create"))
        {
            addTemplateActions(testIDsEnabled, actionsCellCnt, currentNode, bundleTemplateModel);
        }
        if(ifNotArchivedNotRootAndHavePrems(currentNode, bundleTemplateModel, templateStatus))
        {
            actionsCellCnt.appendChild((Component)productPriceAction(bundleTemplate, testIDsEnabled));
        }
        if(ifNotArchivedNotRootAndHavePrems(currentNode, bundleTemplateModel, templateStatus))
        {
            actionsCellCnt.appendChild((Component)disablePrductRulesAction(bundleTemplate, testIDsEnabled));
        }
        if(ifNotArchivedAndHavePermission(currentNode, templateStatus, "change"))
        {
            actionsCellCnt.appendChild((Component)editBundleAction(testIDsEnabled, currentNode));
        }
        if(ifNotArchivedAndHavePermission(currentNode, templateStatus, "remove"))
        {
            actionsCellCnt.appendChild((Component)removeBundleAction(bundleTemplateModel, testIDsEnabled));
        }
        if(ifArchivedWithNoParentAndCanBeRemoved(currentNode, bundleTemplateModel, templateStatus))
        {
            actionsCellCnt.appendChild((Component)addRestoreBundleAction(testIDsEnabled));
        }
        actionsCellCnt.setVisible(false);
    }


    protected Toolbarbutton productPriceAction(TypedObject bundleTemplate, boolean testIDsEnabled)
    {
        Toolbarbutton changePriceRuleButton = new Toolbarbutton("", "/productcockpit/images/rules_chng_price_d.png");
        changePriceRuleButton.setTooltiptext(Labels.getLabel("configurablebundlecockpits.product.list_changepricerules"));
        changePriceRuleButton.setStyle("margin-left:4px");
        changePriceRuleButton.setImage("/productcockpit/images/rules_chng_price.png");
        changePriceRuleButton.addEventListener("onClick", event -> addRuleEventAction(BundleRuleType.CHANGE_PRODUCT_PRICE_BUNDLE_RULE, bundleTemplate, "BundleTemplate.changeProductPriceBundleRules"));
        if(testIDsEnabled)
        {
            UITools.applyTestID((Component)changePriceRuleButton, "chngPR");
        }
        return changePriceRuleButton;
    }


    protected Toolbarbutton disablePrductRulesAction(TypedObject bundleTemplate, boolean testIDsEnabled)
    {
        Toolbarbutton disableBundleRuleButton = new Toolbarbutton("", "/productcockpit/images/rules_disable_d.png");
        disableBundleRuleButton.setTooltiptext(Labels.getLabel("configurablebundlecockpits.product.list_availabilityrules"));
        disableBundleRuleButton.setStyle("margin-left:4px");
        disableBundleRuleButton.setImage("/productcockpit/images/rules_disable.png");
        disableBundleRuleButton.addEventListener("onClick", event -> addRuleEventAction(BundleRuleType.DISABLE_PRODUCT_BUNDLE_RULE, bundleTemplate, "BundleTemplate.disableProductBundleRules"));
        if(testIDsEnabled)
        {
            UITools.applyTestID((Component)disableBundleRuleButton, "disablePR");
        }
        return disableBundleRuleButton;
    }


    protected Toolbarbutton editBundleAction(boolean testIDsEnabled, TypedObject currentNode)
    {
        Toolbarbutton editButton = new Toolbarbutton("", "/cockpit/images/item_edit_action.png");
        editButton.setTooltiptext(Labels.getLabel("cmscockpit.navigationnodes.edit"));
        editButton.setStyle("margin-left:4px");
        editButton.addEventListener("onClick", event -> UISessionUtils.getCurrentSession().getCurrentPerspective().activateItemInEditor(currentNode));
        if(testIDsEnabled)
        {
            UITools.applyTestID((Component)editButton, "editNN");
        }
        return editButton;
    }


    protected Toolbarbutton removeBundleAction(BundleTemplateModel bundleTemplateModel, boolean testIDsEnabled)
    {
        String tooltiptext;
        if(bundleTemplateModel.getParentTemplate() == null)
        {
            tooltiptext = Labels.getLabel("configurablebundlecockpits.bundle.archive.tooltip");
        }
        else
        {
            tooltiptext = Labels.getLabel("configurablebundlecockpits.bundle.delete.tooltip");
        }
        Toolbarbutton deleteButton = new Toolbarbutton("", "/productcockpit/images/node_delete.png");
        deleteButton.setTooltiptext(tooltiptext);
        deleteButton.setStyle("margin-left:2px");
        deleteButton.addEventListener("onClick", (EventListener)new DeleteListener());
        if(testIDsEnabled)
        {
            UITools.applyTestID((Component)deleteButton, "delNN");
        }
        return deleteButton;
    }


    protected Toolbarbutton addRestoreBundleAction(boolean testIDsEnabled)
    {
        String tooltiptext = Labels.getLabel("configurablebundlecockpits.bundle.restore.tooltip");
        Toolbarbutton restoreButton = new Toolbarbutton("", "/productcockpit/images/icon_func_refresh_available.gif");
        restoreButton.setTooltiptext(tooltiptext);
        restoreButton.setStyle("margin-left:2px");
        restoreButton.addEventListener("onClick", (EventListener)new DeleteListener());
        if(testIDsEnabled)
        {
            UITools.applyTestID((Component)restoreButton, "restoreNN");
        }
        return restoreButton;
    }


    protected void addRuleEventAction(BundleRuleType bundleRuleType, TypedObject bundleTemplate, String propertyQualifier)
    {
        BrowserModel browserModel = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
        BundleTemplateModel bundleTemplateModel = (BundleTemplateModel)bundleTemplate.getObject();
        getModel(browserModel).openRelatedBundleQueryBrowser(bundleTemplateModel, browserModel, bundleRuleType);
        PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(propertyQualifier);
        ObjectTemplate objectTemplate = TypeTools.getValueTypeAsObjectTemplate(propertyDescriptor, getTypeService());
        Map<String, ? extends Object> params = new HashMap<>();
        UISessionUtils.getCurrentSession().getCurrentPerspective()
                        .openReferenceCollectionInBrowserContext(new ArrayList(), objectTemplate, bundleTemplate, params);
    }


    public BundleNavigationNodeBrowserModel getModel(BrowserModel browserModel)
    {
        BundleNavigationNodeBrowserModel ret = null;
        if(browserModel instanceof BundleNavigationNodeBrowserModel)
        {
            ret = (BundleNavigationNodeBrowserModel)browserModel;
        }
        return ret;
    }


    private void addTemplateActions(boolean testIDsEnabled, Div actionsCellCnt, TypedObject currentNode, BundleTemplateModel bundleTemplateModel)
    {
        Toolbarbutton siblingButton = addSiblingTemplateAction(bundleTemplateModel, testIDsEnabled, currentNode);
        Toolbarbutton childButton = addChildTemplateAction(bundleTemplateModel, testIDsEnabled, currentNode);
        Toolbarbutton cloneButton = addCloneTemplateAction(bundleTemplateModel, testIDsEnabled);
        actionsCellCnt.appendChild((Component)siblingButton);
        if(null != childButton)
        {
            actionsCellCnt.appendChild((Component)childButton);
        }
        if(null != cloneButton)
        {
            actionsCellCnt.appendChild((Component)cloneButton);
        }
    }


    protected Toolbarbutton addSiblingTemplateAction(BundleTemplateModel bundleTemplateModel, boolean testIDsEnabled, TypedObject currentNode)
    {
        BundleTemplateModel template = null;
        if(bundleTemplateModel.getParentTemplate() != null)
        {
            template = bundleTemplateModel.getParentTemplate();
        }
        Toolbarbutton addButton = createToolbarButton(
                        (CatalogVersionModel)computeSyncContext(currentNode).getSourceCatalogVersions().toArray()[0], template, "/productcockpit/images/node_duplicate.png", "configurablebundlecockpits.navigationnodes.addAsASibling");
        if(testIDsEnabled)
        {
            UITools.applyTestID((Component)addButton, "addNN");
        }
        return addButton;
    }


    protected Toolbarbutton addChildTemplateAction(BundleTemplateModel bundleTemplateModel, boolean testIDsEnabled, TypedObject currentNode)
    {
        Toolbarbutton addChildButton = createToolbarButton(
                        (CatalogVersionModel)computeSyncContext(currentNode).getSourceCatalogVersions().toArray()[0], bundleTemplateModel, "/productcockpit/images/node_add_child.png", "configurablebundlecockpits.navigationnodes.addAsAChild");
        if(testIDsEnabled)
        {
            UITools.applyTestID((Component)addChildButton, "addSubNN");
        }
        return addChildButton;
    }


    protected Toolbarbutton addCloneTemplateAction(BundleTemplateModel bundleTemplateModel, boolean testIDsEnabled)
    {
        if(bundleTemplateModel.getParentTemplate() == null)
        {
            Toolbarbutton cloneButton = new Toolbarbutton("", "/productcockpit/images/clone_btn.png");
            cloneButton.setTooltiptext(Labels.getLabel("configurablebundlecockpits.navigationnodes.clone"));
            cloneButton.setStyle("margin-left:2px");
            cloneButton.addEventListener("onClick", event -> {
                this.templateIdsMap.clear();
                BundleTemplateModel newTemplate = cloneBundle(bundleTemplateModel);
                UISessionUtils.getCurrentSession().getModelService().save(newTemplate);
                UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser().updateItems();
            });
            if(testIDsEnabled)
            {
                UITools.applyTestID((Component)cloneButton, "cloNN");
            }
            return cloneButton;
        }
        return null;
    }


    protected BundleTemplateModel cloneBundle(BundleTemplateModel bundleToBeCloned)
    {
        BundleTemplateStatusModel bundleStatus = null;
        BundleTemplateModel clonedBundleTemplateModel = (BundleTemplateModel)UISessionUtils.getCurrentSession().getModelService().clone(bundleToBeCloned);
        String cloneGeneratedId = (String)getCloneIdGenerator().generate();
        if(null == bundleToBeCloned.getParentTemplate())
        {
            clonedBundleTemplateModel.setName("CLONE: " + clonedBundleTemplateModel.getName());
            bundleStatus = (BundleTemplateStatusModel)UISessionUtils.getCurrentSession().getModelService().create(BundleTemplateStatusModel.class);
            bundleStatus.setCatalogVersion(clonedBundleTemplateModel.getCatalogVersion());
            bundleStatus.setStatus(BundleTemplateStatusEnum.CHECK);
        }
        cloneChildBundleTemplates(bundleToBeCloned, clonedBundleTemplateModel);
        clonedBundleTemplateModel.setId("BT_" + cloneGeneratedId);
        this.templateIdsMap.put(bundleToBeCloned.getId(), clonedBundleTemplateModel);
        clonedBundleTemplateModel.setStatus(bundleStatus);
        if(null != clonedBundleTemplateModel.getBundleSelectionCriteria())
        {
            clonedBundleTemplateModel.getBundleSelectionCriteria().setId("BSC_" + cloneGeneratedId);
        }
        if(!CollectionUtils.isEmpty(clonedBundleTemplateModel.getDisableProductBundleRules()))
        {
            for(DisableProductBundleRuleModel disableProductBundleRuleModel : clonedBundleTemplateModel
                            .getDisableProductBundleRules())
            {
                String dpbrGeneratedId = (String)getCloneIdGenerator().generate();
                disableProductBundleRuleModel.setId("DPBR_" + dpbrGeneratedId);
            }
        }
        if(!CollectionUtils.isEmpty(clonedBundleTemplateModel.getChangeProductPriceBundleRules()))
        {
            for(ChangeProductPriceBundleRuleModel changeProductPriceBundleRuleModel : clonedBundleTemplateModel
                            .getChangeProductPriceBundleRules())
            {
                String cppbrGeneratedId = (String)getCloneIdGenerator().generate();
                changeProductPriceBundleRuleModel.setId("CPPBR_" + cppbrGeneratedId);
            }
        }
        cloneRequiredBundleTemplates(bundleToBeCloned, clonedBundleTemplateModel);
        cloneDependentBundleTemplates(bundleToBeCloned, clonedBundleTemplateModel);
        Date now = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        clonedBundleTemplateModel.setCreationtime(now);
        clonedBundleTemplateModel.setModifiedtime(null);
        clonedBundleTemplateModel.setOwner((ItemModel)UISessionUtils.getCurrentSession().getUser());
        return clonedBundleTemplateModel;
    }


    protected void cloneChildBundleTemplates(BundleTemplateModel bundleToBeCloned, BundleTemplateModel clonedBundleTemplateModel)
    {
        if(CollectionUtils.isNotEmpty(bundleToBeCloned.getChildTemplates()))
        {
            List<BundleTemplateModel> childTemplates = new ArrayList<>();
            for(BundleTemplateModel childTemplate : bundleToBeCloned.getChildTemplates())
            {
                BundleTemplateModel newChildTemplateModel = cloneBundle(childTemplate);
                childTemplates.add(newChildTemplateModel);
            }
            clonedBundleTemplateModel.setChildTemplates(childTemplates);
        }
    }


    protected void cloneRequiredBundleTemplates(BundleTemplateModel bundleToBeCloned, BundleTemplateModel clonedBundleTemplateModel)
    {
        if(!CollectionUtils.isEmpty(bundleToBeCloned.getRequiredBundleTemplates()))
        {
            List<BundleTemplateModel> requiredBundles = new ArrayList<>();
            for(BundleTemplateModel requiredBundle : bundleToBeCloned.getRequiredBundleTemplates())
            {
                if(null != this.templateIdsMap.get(requiredBundle.getId()))
                {
                    requiredBundles.add(this.templateIdsMap.get(requiredBundle.getId()));
                }
            }
            clonedBundleTemplateModel.setRequiredBundleTemplates(requiredBundles);
        }
    }


    protected void cloneDependentBundleTemplates(BundleTemplateModel bundleToBeCloned, BundleTemplateModel clonedBundleTemplateModel)
    {
        for(BundleTemplateModel dependentBundle : bundleToBeCloned.getDependentBundleTemplates())
        {
            List<BundleTemplateModel> dependentBundles = new ArrayList<>();
            if(null != this.templateIdsMap.get(dependentBundle.getId()))
            {
                dependentBundles.add(this.templateIdsMap.get(dependentBundle.getId()));
            }
            clonedBundleTemplateModel.setDependentBundleTemplates(dependentBundles);
        }
    }


    protected SynchronizationService.SyncContext computeSyncContext(TypedObject currentNode)
    {
        if(currentNode.getObject() instanceof BundleTemplateModel)
        {
            return getSynchronizationService().getSyncContext(currentNode);
        }
        return null;
    }


    protected Toolbarbutton createToolbarButton(CatalogVersionModel catalogVersion, BundleTemplateModel template, String image, String tooltip)
    {
        Toolbarbutton addChild = new Toolbarbutton("", image);
        addChild.setTooltiptext(Labels.getLabel(tooltip));
        addChild.setStyle("margin-left:3px");
        addChild.addEventListener("onClick", (EventListener)new OnClickEventListener(this, catalogVersion, template));
        addChild.setSclass("addRelatedItemsAddBtn");
        return addChild;
    }


    private boolean ifArchivedWithNoParentAndCanBeRemoved(TypedObject currentNode, BundleTemplateModel bundleTemplateModel, BundleTemplateStatusModel templateStatus)
    {
        return (BundleTemplateStatusEnum.ARCHIVED.equals(templateStatus.getStatus()) && bundleTemplateModel
                        .getParentTemplate() == null &&
                        getSystemService().checkPermissionOn(currentNode.getType().getCode(), "remove"));
    }


    private boolean ifNotArchivedAndHavePermission(TypedObject currentNode, BundleTemplateStatusModel templateStatus, String permission)
    {
        return (!BundleTemplateStatusEnum.ARCHIVED.equals(templateStatus.getStatus()) &&
                        getSystemService().checkPermissionOn(currentNode.getType().getCode(), permission));
    }


    private boolean ifNotArchivedNotRootAndHavePrems(TypedObject currentNode, BundleTemplateModel bundleTemplateModel, BundleTemplateStatusModel templateStatus)
    {
        return (!BundleTemplateStatusEnum.ARCHIVED.equals(templateStatus.getStatus()) &&
                        getSystemService().checkPermissionOn(currentNode.getType().getCode(), "change") && bundleTemplateModel
                        .getParentTemplate() != null);
    }


    public SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }


    protected SynchronizationService getSynchronizationService()
    {
        if(this.synchronizationService == null)
        {
            this.synchronizationService = (SynchronizationService)SpringUtil.getBean("synchronizationService");
        }
        return this.synchronizationService;
    }


    public KeyGenerator getCloneIdGenerator()
    {
        return this.cloneIdGenerator;
    }


    public void setCloneIdGenerator(KeyGenerator cloneIdGenerator)
    {
        this.cloneIdGenerator = cloneIdGenerator;
    }
}
