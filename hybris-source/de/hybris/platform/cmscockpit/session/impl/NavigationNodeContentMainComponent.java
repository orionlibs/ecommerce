package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.navigationnode.browserarea.list.RelatedItemListController;
import de.hybris.platform.cmscockpit.navigationnode.browserarea.list.RelatedItemListModel;
import de.hybris.platform.cmscockpit.navigationnode.browserarea.list.RelatedItemListRenderer;
import de.hybris.platform.cmscockpit.navigationnode.browserarea.tree.NavigationNodeController;
import de.hybris.platform.cmscockpit.navigationnode.browserarea.tree.NavigationNodeRenderer;
import de.hybris.platform.cmscockpit.navigationnode.browserarea.tree.TreeModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractMainAreaBrowserComponent;
import de.hybris.platform.cockpit.components.mvc.listbox.Listbox;
import de.hybris.platform.cockpit.components.mvc.listbox.ListboxController;
import de.hybris.platform.cockpit.components.mvc.tree.PositionAwareTreeController;
import de.hybris.platform.cockpit.components.mvc.tree.Tree;
import de.hybris.platform.cockpit.components.mvc.tree.TreeController;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Splitter;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Vbox;

public class NavigationNodeContentMainComponent extends AbstractMainAreaBrowserComponent
{
    private static final Logger LOG = Logger.getLogger(NavigationNodeContentMainComponent.class);
    protected static final String RELATED_ITEMS_LIST_SCLASS = "relatedItemList";
    protected static final String NAVIGATION_TREE_SCLASS = "navigationNodeTree";
    protected static final String NAVIGATION_TREE_PANEL_SCLASS = "navigationTreePanel";
    protected static final String ROOT_NAVIGATION_CNT_SCLASS = "rootNavigationContainer";
    protected static final String ROOT_NAVIGATION_SUB_CNT_SCLASS = "rootNavigationSubContainer";
    protected static final String NAVIGATION_SECTION_CONTAINER_LEFT_SCLASS = "navigationSectionContainerLeft";
    protected static final String NAVIGATION_SECTION_CONTAINER_RIGHT_SCLASS = "navigationSectionContainerRight";
    protected static final String RELATED_ITEMS_CONTAINER_SCLASS = "relatedItemsContainer";
    protected static final String ADD_RELATED_ITEMS_BTG_SCLASS = "addRelatedItemsAddBtn";
    protected static final String MARGIN_HELPER_SCLASS = "maringHelper";
    protected static final String ADD_ITEM_LABEL = "cmscockpit.navigationnode.add.item";
    protected static final String INFO_AREA_DIV_ID = "infoAreaContainer2";
    private TreeController treeController;
    private Tree tree;
    private Component relatedItemListContainer;
    private Button addItemButton;


    public NavigationNodeContentMainComponent(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    protected void initTreeComponent(CatalogVersionModel catalogVersion, Component parent)
    {
        RelatedItemListController relatedItemListController = getRelatedItemListController();
        Listbox<TypedObject> listBox = createRelatedItemList();
        this.relatedItemListContainer = createRelatedItemListContainer(listBox);
        listBox.setController((ListboxController)relatedItemListController);
        this.tree = createTree(catalogVersion);
        this.addItemButton = createAddItemButton();
        this.treeController = createTreeController(this.relatedItemListContainer, relatedItemListController, listBox, this.addItemButton);
        this.tree.setController(this.treeController);
        renderTreeComponent(this.tree, this.relatedItemListContainer, createAddItemButtonContainer(this.addItemButton), parent);
    }


    protected void updateTreeComponent(CatalogVersionModel catalogVersion, Component parent)
    {
        getNavigationNodeController().refresh(this.tree);
        renderTreeComponent(this.tree, this.relatedItemListContainer, createAddItemButtonContainer(this.addItemButton), parent);
    }


    protected void renderTreeComponent(Tree tree, Component contentItemListContainer, Component addItemButtonContainer, Component parent)
    {
        Hbox hbox = new Hbox();
        hbox.setSclass("navigationTreePanel");
        UITools.maximize((HtmlBasedComponent)hbox);
        hbox.setPack("start");
        parent.appendChild((Component)hbox);
        if(getModel().getTreeRootChildCount() == 0)
        {
            Div noNavigationNodes = new Div();
            Label noNavigationNodesLabel = new Label(Labels.getLabel("cmscockpit.navigationnode.nonodes"));
            Div rootNavigationContainer = new Div();
            rootNavigationContainer.setSclass("rootNavigationContainer");
            Button addRootNode = new Button(Labels.getLabel("cmscockpit.navigationnode.addnode"));
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "addNavigationNode";
                UITools.applyTestID((Component)addRootNode, "addNavigationNode");
            }
            addRootNode.addEventListener("onClick", (EventListener)new Object(this));
            noNavigationNodes.appendChild((Component)new Br());
            noNavigationNodes.appendChild((Component)noNavigationNodesLabel);
            noNavigationNodes.appendChild((Component)new Br());
            noNavigationNodes.appendChild((Component)new Br());
            Div noNavigationNodesDiv = new Div();
            noNavigationNodesDiv.appendChild((Component)noNavigationNodes);
            rootNavigationContainer.appendChild((Component)noNavigationNodesDiv);
            rootNavigationContainer.appendChild((Component)addRootNode);
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
            leftContainer.setHeight("100%");
            leftContainer.setSclass("navigationSectionContainerLeft");
            Vbox rightContainer = new Vbox();
            rightContainer.setSclass("navigationSectionContainerRight");
            rightContainer.setAlign("end");
            rightContainer.setPack("start");
            rightContainer.setWidth("99%");
            rightContainer.setHeight("100%");
            hbox.appendChild((Component)leftContainer);
            hbox.appendChild((Component)splitter);
            hbox.appendChild((Component)rightContainer);
            hbox.setWidths("50%,50%");
            leftContainer.appendChild((Component)tree);
            rightContainer.appendChild(contentItemListContainer);
            rightContainer.appendChild(addItemButtonContainer);
        }
    }


    protected Div createMainArea()
    {
        Div mainAreaContainer = new Div();
        UITools.maximize((HtmlBasedComponent)mainAreaContainer);
        this.mainArea = mainAreaContainer;
        renderMainAreaCompoenent((Component)this.mainArea);
        return mainAreaContainer;
    }


    protected void renderMainAreaCompoenent(Component parent)
    {
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof NavigationNodeBrowserArea)
        {
            CMSSiteModel currentSite = ((NavigationNodeBrowserArea)browserArea).getActiveSite();
            CatalogVersionModel catalogVersion = ((NavigationNodeBrowserArea)browserArea).getActiveCatalogVersion();
            UITools.detachChildren(parent);
            if(currentSite == null)
            {
                createBlankArea(Labels.getLabel("cmscockpit.select.website"), parent);
            }
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
        if(browserArea instanceof NavigationNodeBrowserArea)
        {
            CMSSiteModel currentSite = ((NavigationNodeBrowserArea)browserArea).getActiveSite();
            CatalogVersionModel catalogVersion = ((NavigationNodeBrowserArea)browserArea).getActiveCatalogVersion();
            UITools.detachChildren(parent);
            if(currentSite == null)
            {
                createBlankArea(Labels.getLabel("cmscockpit.select.website"), parent);
            }
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


    public NavigationNodeBrowserModel getModel()
    {
        NavigationNodeBrowserModel ret = null;
        if(super.getModel() instanceof NavigationNodeBrowserModel)
        {
            ret = (NavigationNodeBrowserModel)super.getModel();
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


    protected TreeController createTreeController(Component relatedItemListContainer, RelatedItemListController relatedItemListController, Listbox listbox, Button addItemButton)
    {
        return (TreeController)new Object(this, (PositionAwareTreeController)
                        getNavigationNodeController(), relatedItemListContainer, listbox, relatedItemListController, addItemButton);
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


    protected Listbox createRelatedItemList()
    {
        Listbox<TypedObject> listBox = new Listbox();
        listBox.setSclass("relatedItemList");
        listBox.setOddRowSclass("no");
        listBox.setItemRenderer((ListitemRenderer)new RelatedItemListRenderer());
        listBox.setModel((ListModel)new RelatedItemListModel(null));
        return listBox;
    }


    protected Button createAddItemButton()
    {
        Button button = new Button(Labels.getLabel("cmscockpit.navigationnode.add.item"));
        button.setTooltiptext(Labels.getLabel("cmscockpit.navigationnode.add.item"));
        UITools.modifySClass((HtmlBasedComponent)this, "buttonDisabled", true);
        button.setVisible(false);
        button.addEventListener("onClick", (EventListener)new Object(this));
        button.setSclass("addRelatedItemsAddBtn");
        return button;
    }


    protected Component createRelatedItemListContainer(Listbox relatedItemList)
    {
        Div content = new Div();
        content.setWidth("100%");
        content.setHeight("50%");
        content.setSclass("relatedItemsContainer");
        content.appendChild((Component)relatedItemList);
        return (Component)content;
    }


    protected Tree createTree(CatalogVersionModel catVer)
    {
        Tree<TypedObject> tree = new Tree();
        tree.setSclass("navigationNodeTree");
        TreeModel treeModel = new TreeModel(getTypeService().wrapItem(catVer));
        tree.setTreeitemRenderer(getNavigationNodeRenderer());
        tree.setModel((TreeModel)treeModel);
        tree.setWidth("100%");
        tree.setHeight("100%");
        Treecols treecols = new Treecols();
        Treecol firstColumn = new Treecol("");
        treecols.appendChild((Component)firstColumn);
        Treecol secondColumn = new Treecol("");
        treecols.appendChild((Component)secondColumn);
        tree.appendChild((Component)treecols);
        tree.setZclass("z-dottree");
        return tree;
    }


    protected void createBlankArea(String message, Component parent)
    {
        Div marginHelper = new Div();
        marginHelper.setSclass("maringHelper");
        UITools.maximize((HtmlBasedComponent)marginHelper);
        Div mainContainer = new Div();
        mainContainer.setWidth("99%");
        mainContainer.setHeight("99%");
        mainContainer.appendChild((Component)new Br());
        mainContainer.appendChild((Component)new Label(message));
        marginHelper.appendChild((Component)mainContainer);
        parent.appendChild((Component)marginHelper);
    }


    protected void refreshAddItemButton(Set<Treeitem> selectedItems, Button addItemButton)
    {
        if(addItemButton != null)
        {
            UITools.modifySClass((HtmlBasedComponent)addItemButton, "buttonDisabled", false);
            addItemButton.setVisible(true);
            if(CollectionUtils.isEmpty(selectedItems))
            {
                addItemButton.setVisible(false);
            }
        }
    }


    protected void refreshRelatedItemList(Component parent, Listbox listBox, RelatedItemListController controller)
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


    protected TreeitemRenderer getNavigationNodeRenderer()
    {
        return (TreeitemRenderer)SpringUtil.getBean("navigationNodeRenderer", NavigationNodeRenderer.class);
    }


    protected RelatedItemListController getRelatedItemListController()
    {
        return (RelatedItemListController)SpringUtil.getBean("relatedItemListController", RelatedItemListController.class);
    }


    protected NavigationNodeController getNavigationNodeController()
    {
        return (NavigationNodeController)SpringUtil.getBean("navigationNodeController", NavigationNodeController.class);
    }
}
