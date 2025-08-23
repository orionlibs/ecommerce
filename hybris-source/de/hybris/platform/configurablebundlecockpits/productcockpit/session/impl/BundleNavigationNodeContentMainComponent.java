package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractMainAreaBrowserComponent;
import de.hybris.platform.cockpit.components.mvc.listbox.Listbox;
import de.hybris.platform.cockpit.components.mvc.listbox.ListboxController;
import de.hybris.platform.cockpit.components.mvc.tree.Tree;
import de.hybris.platform.cockpit.components.mvc.tree.TreeController;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.list.BundleRelatedItemListController;
import de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.list.BundleRelatedItemListModel;
import de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.list.BundleRelatedItemListRenderer;
import de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree.BundleNavigationNodeController;
import de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree.BundleNavigationNodeRenderer;
import de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree.BundleTemplateTreeModel;
import de.hybris.platform.core.Registry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.fest.util.Collections;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Box;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Splitter;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Vbox;

public class BundleNavigationNodeContentMainComponent extends AbstractMainAreaBrowserComponent
{
    private static final String MESSAGE_TEXT_SCLASS = "messageText";
    private static final String MESSAGE_TITLE_SCLASS = "messageTitle";
    private static final String MESSAGE_CONTAINER_SCLASS = "messageContainer";
    protected static final String RELATED_ITEMS_LIST_SCLASS = "relatedItemList";
    protected static final String NAVIGATION_TREE_SCLASS = "navigationNodeTree";
    protected static final String NAVIGATION_TREE_PANEL_SCLASS = "navigationTreePanel";
    protected static final String ROOT_NAVIGATION_CNT_SCLASS = "rootNavigationContainer";
    protected static final String ROOT_NAVIGATION_SUB_CNT_SCLASS = "rootNavigationSubContainer";
    protected static final String NAVIGATION_SECTION_CONTAINER_LEFT_SCLASS = "navigationSectionContainerLeft";
    protected static final String NAVIGATION_SECTION_CONTAINER_RIGHT_SCLASS = "navigationSectionContainerRight";
    protected static final String RELATED_ITEMS_CONTAINER_SCLASS = "relatedItemsContainer";
    protected static final String ADD_RELATED_ITEMS_BTG_SCLASS = "addRelatedItemsAddBtn";
    protected static final String MARGIN_HELPER_SCLASS = "marginHelper";
    protected static final String ADD_PRODUCT_LABEL = "configurablebundlecockpits.product.add";
    protected static final String ADD_BUNDLE_LABEL = "configurablebundlecockpits.bundle.add";
    protected static final String ADD_BUNDLE_TOOLTIP = "configurablebundlecockpits.bundle.add.tooltip";
    protected static final String SEARCH_PRODUCT_LABEL = "configurablebundlecockpits.product.search";
    protected static final String INFO_AREA_DIV_ID = "infoAreaContainer2";
    private transient TreeController treeController;
    private Tree tree;
    private Component relatedItemListContainer;
    private Button addItemButton;
    private Button searchProductButton;
    private Button addBundleButton;


    public BundleNavigationNodeContentMainComponent(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    protected void initTreeComponent(CatalogVersionModel catalogVersion, Component parent)
    {
        Set<TypedObject> selected = getModel().getSelectedNode();
        BundleRelatedItemListController relatedItemListController = getRelatedItemListController();
        Listbox<TypedObject> listBox = createRelatedItemList(CollectionUtils.isNotEmpty(selected) ?
                        selected.iterator().next() : null);
        listBox.setVflex(false);
        this.relatedItemListContainer = createRelatedItemListContainer(listBox);
        listBox.setController((ListboxController)relatedItemListController);
        this.treeController = createTreeController(this.relatedItemListContainer, relatedItemListController, listBox, this.addItemButton, this.searchProductButton);
        this.tree = createTree(catalogVersion);
        this.tree.setController(this.treeController);
        this.addItemButton = createAddItemButton();
        this.searchProductButton = createSearchProductButton();
        this.addBundleButton = createAddBundleButton(catalogVersion);
        renderTreeComponent(this.tree, this.relatedItemListContainer, createProductActionButtonContainer(this.searchProductButton, this.addItemButton),
                        createButtonContainer(this.addBundleButton), parent);
    }


    protected void updateTreeComponent(CatalogVersionModel catalogVersion, Component parent)
    {
        updateTreeModel();
        getNavigationNodeController().refresh(this.tree);
        renderTreeComponent(this.tree, this.relatedItemListContainer, createProductActionButtonContainer(this.searchProductButton, this.addItemButton),
                        createButtonContainer(this.addBundleButton), parent);
    }


    protected void renderTreeComponent(Tree tree, Component contentItemListContainer, Component productActionButtonContainer, Component addBundleActionButtonContainer, Component parent)
    {
        Hbox hbox = new Hbox();
        hbox.setSclass("navigationTreePanel");
        UITools.maximize((HtmlBasedComponent)hbox);
        hbox.setPack("start");
        parent.appendChild((Component)hbox);
        if(getModel().getTreeRootChildCount() == 0)
        {
            Div noNavigationNodes = new Div();
            Div rootNavigationContainer = new Div();
            rootNavigationContainer.setSclass("rootNavigationContainer");
            createMessageArea(Labels.getLabel("configurablebundlecockpits.perspective.bundleList.title"),
                            Labels.getLabel("configurablebundlecockpits.navigationnode.nonodes"), (Component)noNavigationNodes);
            Div noNavigationNodesDiv = new Div();
            noNavigationNodesDiv.appendChild((Component)noNavigationNodes);
            noNavigationNodesDiv.appendChild(addBundleActionButtonContainer);
            rootNavigationContainer.appendChild((Component)noNavigationNodesDiv);
            Div div = new Div();
            div.setSclass("rootNavigationSubContainer");
            div.appendChild((Component)rootNavigationContainer);
            UITools.maximize((HtmlBasedComponent)div);
            hbox.appendChild((Component)rootNavigationContainer);
        }
        else
        {
            Splitter splitter = new Splitter();
            Div leftContainer = new Div();
            leftContainer.setWidth("99%");
            leftContainer.setSclass("navigationSectionContainerLeft");
            Vbox rightContainer = new Vbox();
            rightContainer.setSclass("navigationSectionContainerRight");
            rightContainer.setAlign("end");
            rightContainer.setPack("start");
            rightContainer.setWidth("99%");
            createMessageArea(Labels.getLabel("configurablebundlecockpits.perspective.bundleList.title"),
                            Labels.getLabel("configurablebundlecockpits.perspective.bundleList.text"), (Component)leftContainer);
            createMessageArea(Labels.getLabel("configurablebundlecockpits.perspective.productList.title"),
                            Labels.getLabel("configurablebundlecockpits.perspective.productList.text"), (Component)rightContainer);
            hbox.appendChild((Component)leftContainer);
            hbox.appendChild((Component)splitter);
            hbox.appendChild((Component)rightContainer);
            hbox.setWidths("50%,50%");
            leftContainer.appendChild((Component)tree);
            leftContainer.appendChild(addBundleActionButtonContainer);
            rightContainer.appendChild(contentItemListContainer);
            rightContainer.appendChild(productActionButtonContainer);
        }
    }


    protected Div createMainArea()
    {
        Div mainAreaContainer = new Div();
        UITools.maximize((HtmlBasedComponent)mainAreaContainer);
        this.mainArea = mainAreaContainer;
        renderMainAreaComponent((Component)this.mainArea);
        return mainAreaContainer;
    }


    protected void renderMainAreaComponent(Component parent)
    {
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof BundleNavigationNodeBrowserArea)
        {
            CatalogVersionModel catalogVersion = ((BundleNavigationNodeBrowserArea)browserArea).getActiveCatalogVersion();
            UITools.detachChildren(parent);
            if(catalogVersion == null)
            {
                createBlankArea(Labels.getLabel("cmscockpit.select.catalog.version"), parent);
            }
            else
            {
                initTreeComponent(catalogVersion, (Component)this.mainArea);
            }
        }
    }


    protected void updateMainAreaCompoenent(Component parent)
    {
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof BundleNavigationNodeBrowserArea)
        {
            CatalogVersionModel catalogVersion = ((BundleNavigationNodeBrowserArea)browserArea).getActiveCatalogVersion();
            UITools.detachChildren(parent);
            if(catalogVersion == null)
            {
                createBlankArea(Labels.getLabel("cmscockpit.select.catalog.version"), parent);
            }
            else
            {
                updateTreeComponent(catalogVersion, (Component)this.mainArea);
            }
        }
    }


    public boolean update()
    {
        updateMainAreaCompoenent((Component)this.mainArea);
        return false;
    }


    public void fireAddRootNavigationNode()
    {
        this.treeController.create(this.tree, null);
    }


    public void removeSelectedNavigationNode()
    {
        Treeitem selectedTreeItem = this.tree.getSelectedItem();
        if(selectedTreeItem != null)
        {
            this.treeController.delete(this.tree, selectedTreeItem.getValue());
        }
    }


    public BundleNavigationNodeBrowserModel getModel()
    {
        BundleNavigationNodeBrowserModel ret = null;
        if(super.getModel() instanceof BundleNavigationNodeBrowserModel)
        {
            ret = (BundleNavigationNodeBrowserModel)super.getModel();
        }
        return ret;
    }


    protected UIItemView getCurrentItemView()
    {
        return null;
    }


    protected void cleanup()
    {
    }


    protected TreeController createTreeController(Component relatedItemListContainer, BundleRelatedItemListController relatedItemListController, Listbox listbox, Button addItemButton, Button searchProductButton)
    {
        return (TreeController)new BundleTreeControllerWrapper(this, getNavigationNodeController(), relatedItemListController, listbox);
    }


    protected Component createAddItemButtonContainer(Button addItemButton)
    {
        Box box = new Box();
        box.setWidth("99%");
        box.setHeight("99%");
        box.setAlign("end");
        box.setPack("end");
        box.appendChild((Component)addItemButton);
        return (Component)box;
    }


    protected Component createButtonContainer(Button button)
    {
        Div marginHelper = new Div();
        marginHelper.setSclass("marginHelper");
        Box box = new Box();
        box.setWidth("99%");
        box.setHeight("99%");
        box.setAlign("left");
        box.setSclass("search-product-button");
        box.setPack("left");
        box.appendChild((Component)button);
        marginHelper.appendChild((Component)box);
        return (Component)marginHelper;
    }


    protected Component createProductActionButtonContainer(Button searchProductButton, Button addProductButton)
    {
        Box box = new Box();
        box.setWidth("99%");
        box.setHeight("99%");
        box.setAlign("right");
        box.setSclass("search-product-button");
        box.setPack("left");
        box.appendChild((Component)searchProductButton);
        box.appendChild((Component)addProductButton);
        box.setOrient("horizontal");
        return (Component)box;
    }


    protected Listbox createRelatedItemList(TypedObject naviNode)
    {
        Listbox<TypedObject> listBox = new Listbox();
        listBox.setSclass("relatedItemList");
        listBox.setOddRowSclass("no");
        listBox.setItemRenderer((ListitemRenderer)new BundleRelatedItemListRenderer());
        listBox.setModel((ListModel)new BundleRelatedItemListModel(naviNode));
        return listBox;
    }


    protected Button createAddItemButton()
    {
        Button button = new Button(Labels.getLabel("configurablebundlecockpits.product.add"));
        button.setTooltiptext(Labels.getLabel("configurablebundlecockpits.product.add"));
        UITools.modifySClass((HtmlBasedComponent)this, "buttonDisabled", true);
        button.setVisible((getModel().getSelectedNode() != null));
        button.addEventListener("onClick", (EventListener)new MandatorySearchPageWizardStarter(this));
        button.setSclass("addRelatedItemsAddBtn");
        return button;
    }


    protected Button createSearchProductButton()
    {
        Button button = new Button(Labels.getLabel("configurablebundlecockpits.product.search"));
        button.setTooltiptext(Labels.getLabel("configurablebundlecockpits.product.search"));
        UITools.modifySClass((HtmlBasedComponent)this, "buttonDisabled", true);
        button.setVisible((getModel().getSelectedNode() != null));
        button.addEventListener("onClick", event -> getModel().openRelatedQueryBrowser());
        button.setSclass("addRelatedItemsAddBtn");
        return button;
    }


    protected Button createAddBundleButton(CatalogVersionModel catalogVersion)
    {
        Button button = new Button(Labels.getLabel("configurablebundlecockpits.bundle.add"));
        button.setTooltiptext(Labels.getLabel("configurablebundlecockpits.bundle.add.tooltip"));
        UITools.modifySClass((HtmlBasedComponent)this, "buttonDisabled", true);
        button.setVisible(true);
        button.addEventListener("onClick", (EventListener)new NewItemWizardStarter(this, catalogVersion));
        button.setSclass("addRelatedItemsAddBtn");
        return button;
    }


    protected Component createRelatedItemListContainer(Listbox relatedItemList)
    {
        Div content = new Div();
        content.setWidth("100%");
        content.setHeight("80%");
        content.setSclass("relatedItemsContainer");
        content.appendChild((Component)relatedItemList);
        return (Component)content;
    }


    protected void updateTreeModel()
    {
        this.tree.setModel((TreeModel)new BundleTemplateTreeModel(getModel()));
    }


    protected Tree createTree(CatalogVersionModel catVer)
    {
        Tree<TypedObject> typedObjectTree = new Tree();
        typedObjectTree.setSclass("navigationNodeTree");
        typedObjectTree.setTreeitemRenderer(getNavigationNodeRenderer(this.treeController));
        typedObjectTree.setModel((TreeModel)new BundleTemplateTreeModel(getTypeService().wrapItem(catVer)));
        typedObjectTree.setWidth("100%");
        Treecols treecols = new Treecols();
        Treecol firstColumn = new Treecol("");
        treecols.appendChild((Component)firstColumn);
        Treecol secondColumn = new Treecol("");
        treecols.appendChild((Component)secondColumn);
        typedObjectTree.appendChild((Component)treecols);
        typedObjectTree.setZclass("z-dottree");
        getNavigationNodeController().refresh(typedObjectTree, getModel().getOpenedPath());
        return typedObjectTree;
    }


    protected void createBlankArea(String message, Component parent)
    {
        Div marginHelper = new Div();
        marginHelper.setSclass("marginHelper");
        UITools.maximize((HtmlBasedComponent)marginHelper);
        Div mainContainer = new Div();
        mainContainer.setWidth("99%");
        mainContainer.setHeight("99%");
        mainContainer.appendChild((Component)new Br());
        mainContainer.appendChild((Component)new Label(message));
        marginHelper.appendChild((Component)mainContainer);
        parent.appendChild((Component)marginHelper);
    }


    protected void createMessageArea(String title, String message, Component parent)
    {
        Div marginHelper = new Div();
        marginHelper.setSclass("marginHelper");
        Div messageContainer = new Div();
        messageContainer.setSclass("messageContainer");
        messageContainer.setWidth("99%");
        messageContainer.setHeight("99%");
        Div messageTitle = new Div();
        messageTitle.setSclass("messageTitle");
        messageTitle.appendChild((Component)new Label(title));
        Div messageText = new Div();
        messageTitle.setSclass("messageText");
        messageText.appendChild((Component)new Html(message));
        messageContainer.appendChild((Component)messageTitle);
        messageContainer.appendChild((Component)messageText);
        marginHelper.appendChild((Component)messageContainer);
        parent.appendChild((Component)marginHelper);
    }


    protected void refreshAddItemButton(Set<Treeitem> selectedItems, Button addItemButton)
    {
        if(addItemButton != null)
        {
            UITools.modifySClass((HtmlBasedComponent)addItemButton, "buttonDisabled", false);
            addItemButton.setVisible(true);
            if(Collections.isEmpty(selectedItems))
            {
                addItemButton.setVisible(false);
            }
        }
    }


    protected void refreshSearchProductButton(Set<Treeitem> selectedItems, Button searchProductButton)
    {
        if(searchProductButton != null)
        {
            UITools.modifySClass((HtmlBasedComponent)searchProductButton, "buttonDisabled", false);
            searchProductButton.setVisible(true);
            if(Collections.isEmpty(selectedItems))
            {
                searchProductButton.setVisible(false);
            }
        }
    }


    protected void refreshRelatedItemList(Component parent, Listbox listBox, BundleRelatedItemListController controller)
    {
        UITools.detachChildren(parent);
        if(controller.updateList(listBox))
        {
            parent.appendChild((Component)listBox);
        }
        else
        {
            Label label = new Label(Labels.getLabel("cmcockpit.navigation.node.no.content.item"));
            parent.appendChild((Component)label);
        }
    }


    protected TreeitemRenderer getNavigationNodeRenderer(TreeController controller)
    {
        BundleNavigationNodeRenderer renderer = (BundleNavigationNodeRenderer)SpringUtil.getBean("bundleNavigationNodeRenderer", BundleNavigationNodeRenderer.class);
        renderer.setController(controller);
        return (TreeitemRenderer)renderer;
    }


    protected BundleRelatedItemListController getRelatedItemListController()
    {
        return (BundleRelatedItemListController)SpringUtil.getBean("bundleRelatedItemListController", BundleRelatedItemListController.class);
    }


    protected BundleNavigationNodeController getNavigationNodeController()
    {
        return (BundleNavigationNodeController)SpringUtil.getBean("bundleNavigationNodeController", BundleNavigationNodeController.class);
    }


    protected Set<ObjectType> getAllowedProductTypes()
    {
        ArrayList<String> allowedTyesCodes = (ArrayList)Registry.getApplicationContext().getBean("allowedProductTypesList");
        Set<ObjectType> allowedTypesSet = new HashSet<>();
        Iterator<String> iterator = allowedTyesCodes.iterator();
        while(iterator.hasNext())
        {
            BaseType baseType = getTypeService().getBaseType(iterator.next());
            allowedTypesSet.add(baseType);
        }
        if(allowedTypesSet.isEmpty())
        {
            BaseType baseType = getTypeService().getBaseType("Product");
            allowedTypesSet.add(baseType);
        }
        return allowedTypesSet;
    }
}
