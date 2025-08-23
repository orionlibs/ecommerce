package de.hybris.platform.productcockpit.components.macfinder;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.NewItemService;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.services.meta.ExtendedTypeLoader;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.ListProvider;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTree;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeColumn;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeModelAbstract;
import de.hybris.platform.productcockpit.model.macfinder.node.CategoryNode;
import de.hybris.platform.productcockpit.model.macfinder.node.ClassAttributeAssignmentNode;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import de.hybris.platform.productcockpit.model.macfinder.node.MediaNode;
import de.hybris.platform.productcockpit.model.macfinder.node.ProductNode;
import de.hybris.platform.productcockpit.session.impl.CatalogNavigationArea;
import de.hybris.platform.productcockpit.session.impl.CatalogPerspective;
import de.hybris.platform.productcockpit.session.impl.CategoryTreeBrowserModel;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

public class MacFinderTreeComponent extends AbstractBrowserComponent implements MacFinderTree
{
    private static final String BGCOLOR_WHITE = "background-color:white;";
    private static final String IMPORTANT = " !important;";
    private static final String HEIGHT = "height:";
    private static final String CELLPADDING = "cellpadding";
    private static final String CELLSPACING = "cellspacing";
    private static final String BORDER_NONE = "border:none;";
    private static final String PRODUCT_SUPERCATS = "Product.supercategories";
    private static final String PRODUCT_CATALOGVERSION = "Product.catalogVersion";
    private static final String CATEGORY_SUPERCATS = "Category.supercategories";
    private static final String CATEGORY_CATALOGVERSION = "Category.catalogVersion";
    private static final String CLASSATTRIBUTEASSIGNMENT_SYSTEMVERSION = "ClassAttributeAssignment.systemVersion";
    private static final String CLASSATTRIBUTEASSIGNMENT_CLASSIFICATIONCLASS = "ClassAttributeAssignment.classificationClass";
    private static final String PRODUCT_CODE = "Product";
    private static final String CATEGORY_CODE = "Category";
    private static final String CLASSIFICATION_CODE = "ClassificationClass";
    private static final String MEDIA_CODE = "Media";
    private static final String CLASSATTRIBUTEASSIGNMENT_CODE = "ClassAttributeAssignment";
    private static final int COLUMNS = 3;
    private static final String BREADCRUMBS_HEIGHT = "15px";
    private static final String BREADCRUMBS_POSTFIX = " >> ";
    private static final String REMOVE_AFTER_SCROLL = "removeAfterScroll";
    private static final int ESC_CHAR = 27;
    private static final int ENTER_CHAR = 13;
    private static final Logger LOG = LoggerFactory.getLogger(MacFinderTreeComponent.class);
    private Borderlayout macFinderBorderLayout = null;
    private final Menupopup dropContextMenu = new Menupopup();
    private Div breadCrumbsArea;
    private Table tableContainer;
    private Div scrolledContainer;
    private ListitemRenderer listItemRenderer;
    private CatalogVersionModel currentCatalogVersion;
    private transient TypeService typeService = null;
    private transient NewItemService newItemService;
    private transient ExtendedTypeLoader cockpitTypeLoaderChain;


    public MacFinderTreeComponent(CategoryTreeBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super((BrowserModel)model, contentBrowser);
        getModel().getTreeModel().setBindedComponent(this);
    }


    public CategoryTreeBrowserModel getModel()
    {
        return (CategoryTreeBrowserModel)super.getModel();
    }


    public ListitemRenderer getListItemRenderer()
    {
        return this.listItemRenderer;
    }


    public void setListItemRenderer(ListitemRenderer listItemRenderer)
    {
        this.listItemRenderer = listItemRenderer;
    }


    public MacFinderTreeModelAbstract getTreeModel()
    {
        return getModel().getTreeModel();
    }


    public void checkOpenDropContext(int keys, int x, int y, List<TypedObject> draggedItems, TypedObject dropTarget, CategoryModel oldSuperCat)
    {
        if(keys == 0)
        {
            this.dropContextMenu.getChildren().clear();
            if(dropTarget.getObject() instanceof CatalogVersionModel)
            {
                Menuitem mi = new Menuitem(Labels.getLabel("menu.removealllinks"));
                UITools.addBusyListener((Component)mi, "onClick", (EventListener)new Object(this, draggedItems, dropTarget), null, "menu.removealllinks");
                this.dropContextMenu.appendChild((Component)mi);
            }
            else
            {
                Menuitem mi = new Menuitem(Labels.getLabel("menu.link"));
                UITools.addBusyListener((Component)mi, "onClick", (EventListener)new Object(this, draggedItems, dropTarget), null, "menu.link");
                this.dropContextMenu.appendChild((Component)mi);
                mi = new Menuitem(Labels.getLabel("menu.movelink"));
                if(oldSuperCat != null)
                {
                    mi.setDisabled(false);
                    UITools.addBusyListener((Component)mi, "onClick", (EventListener)new Object(this, draggedItems, dropTarget, oldSuperCat), null, "menu.movelink");
                }
                else
                {
                    mi.setDisabled(true);
                }
                this.dropContextMenu.appendChild((Component)mi);
                mi = new Menuitem(Labels.getLabel("menu.exclusivelylink"));
                UITools.addBusyListener((Component)mi, "onClick", (EventListener)new Object(this, draggedItems, dropTarget), null, "menu.exclusivelylink");
                this.dropContextMenu.appendChild((Component)mi);
            }
            Menuitem menuitem = new Menuitem(Labels.getLabel("menu.cancel"));
            menuitem.addEventListener("onClick", (EventListener)new Object(this));
            this.dropContextMenu.appendChild((Component)menuitem);
            this.dropContextMenu.open(x, y);
        }
        else if(keys == 2)
        {
            getModel().handleDrop(draggedItems, dropTarget, null);
        }
        else if(keys == 4)
        {
            getModel().handleDrop(draggedItems, dropTarget, oldSuperCat);
        }
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            this.macFinderBorderLayout = new Borderlayout();
            this.macFinderBorderLayout.setStyle("background: white");
            UITools.maximize((HtmlBasedComponent)this.macFinderBorderLayout);
            this.breadCrumbsArea = new Div();
            this.breadCrumbsArea.setSclass("breadcrumbsArea");
            Table breadcrumbsTable = new Table();
            breadcrumbsTable.setStyle("height:15px");
            Tr breadcrumbsRow = new Tr();
            breadcrumbsTable.appendChild((Component)breadcrumbsRow);
            breadcrumbsTable.setParent((Component)this.breadCrumbsArea);
            this.scrolledContainer = new Div();
            this.scrolledContainer.setSclass("scrolledContainer");
            this.tableContainer = new Table();
            this.tableContainer.setStyle("height:97% !important;");
            this.tableContainer.setDynamicProperty("cellpadding", Integer.valueOf(0));
            this.tableContainer.setDynamicProperty("cellspacing", Integer.valueOf(0));
            Tr tableContainerRow = new Tr();
            tableContainerRow.setParent((Component)this.tableContainer);
            this.scrolledContainer.appendChild((Component)this.tableContainer);
            this.scrolledContainer.appendChild((Component)this.dropContextMenu);
            North north = new North();
            north.setHeight("18px");
            north.setBorder("none");
            Center center = new Center();
            center.setBorder("none");
            north.appendChild((Component)this.breadCrumbsArea);
            center.appendChild((Component)this.scrolledContainer);
            center.setSclass("treeBrowserCenter");
            this.macFinderBorderLayout.appendChild((Component)north);
            this.macFinderBorderLayout.appendChild((Component)center);
            appendChild((Component)this.macFinderBorderLayout);
            setSclass("macfindertree");
            this.currentCatalogVersion = getModel().getCatalogVersion();
            renderComponent(false);
            this.initialized = true;
        }
        return this.initialized;
    }


    public void resize()
    {
    }


    public void setActiveItem(TypedObject activeItem)
    {
        refreshLeafNodes();
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            if(this.currentCatalogVersion != null)
            {
                this.currentCatalogVersion = getModel().getCatalogVersion();
                getBreadCrumbsTable().getFirstChild().getChildren().clear();
                resetNeedlessColumns(-1);
            }
            renderComponent(true);
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public void refreshLeafNodes()
    {
        int columnIndex = getTreeModel().getColumns().size() - 1;
        if(((MacFinderTreeColumn)getTreeModel().getColumns().get(columnIndex)).isLeaf())
        {
            getCorrespondingListbox(columnIndex).map(Component::getParent).ifPresent(parent -> {
                UITools.detachChildren(parent);
                Listbox newContent = renderColumnContent(getTreeModel().getColumns().get(columnIndex));
                parent.appendChild((Component)newContent);
                if(parent instanceof Div)
                {
                    String javaScriptBody = "var pressedKey = event.keyCode; if(event.keyCode == 0 ) \tpressedKey = event.charCode;  if( pressedKey == 13 || pressedKey == 27) {   comm.sendUser('" + newContent.getId() + "', pressedKey); return false;} return false;";
                    ((Div)parent).setAction("onkeydown:" + javaScriptBody);
                }
            });
        }
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        boolean smallUpdate = true;
        if(item != null)
        {
            if(TypeTools.checkInstanceOfCategory(getTypeService(), item))
            {
                smallUpdate = false;
            }
            BaseType categoryType = getTypeService().getBaseType(GeneratedCatalogConstants.TC.CATEGORY);
            for(PropertyDescriptor pd : modifiedProperties)
            {
                ObjectTemplate objTemplate = TypeTools.getValueTypeAsObjectTemplate(pd, getTypeService());
                if(objTemplate != null)
                {
                    if(categoryType.isAssignableFrom((ObjectType)objTemplate.getBaseType()))
                    {
                        smallUpdate = false;
                        break;
                    }
                }
            }
        }
        else
        {
            smallUpdate = false;
        }
        if(smallUpdate)
        {
            refreshLeafNodes();
        }
        else
        {
            getModel().updateItems();
        }
    }


    public void updateActiveItems()
    {
        refreshLeafNodes();
    }


    public void updateSelectedItems()
    {
        refreshLeafNodes();
    }


    private void renderComponent(boolean fullRefresh)
    {
        try
        {
            renderBreadCrumbs();
            renderBrowserContent(fullRefresh);
        }
        catch(JaloSecurityException e)
        {
            LOG.error(e.getMessage(), (Throwable)e);
        }
    }


    private Table getTableContainer()
    {
        return this.tableContainer;
    }


    private Table getBreadCrumbsTable()
    {
        return (Table)this.breadCrumbsArea.getFirstChild();
    }


    private void renderBrowserContent(boolean fullRefresh)
    {
        Tr mainTableRow = (Tr)getTableContainer().getFirstChild();
        if(fullRefresh)
        {
            UITools.detachChildren((Component)mainTableRow);
        }
        for(int i = fullRefresh ? 0 : mainTableRow.getChildren().size(); i < getTreeModel().getColumns().size(); i++)
        {
            boolean allowCreateButton = false;
            MacFinderTreeColumn treeColumn = getTreeModel().getColumns().get(i);
            Td mainColumn = new Td();
            mainColumn.setSclass("mainColumnClass");
            mainColumn.setParent((Component)mainTableRow);
            Borderlayout borderlayout = new Borderlayout();
            borderlayout.setStyle("background: transparent");
            UITools.maximize((HtmlBasedComponent)borderlayout);
            Div scrollContainer = new Div();
            scrollContainer.setSclass("scrollContainer");
            Listbox columnContent = renderColumnContent(treeColumn);
            Center center = new Center();
            center.setBorder("none");
            center.appendChild((Component)scrollContainer);
            borderlayout.appendChild((Component)center);
            scrollContainer.appendChild((Component)columnContent);
            Div bottomCell = new Div();
            bottomCell.setSclass("verticalContainerBottom");
            Button createNewButton = new Button();
            if(treeColumn.isColumnType(CategoryNode.class))
            {
                CatalogVersionModel catVer = getModel().getCatalogVersion();
                boolean classificationSystem = catVer instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
                if(classificationSystem)
                {
                    allowCreateButton = checkPermissions("ClassificationClass", "create");
                    createNewButton.setLabel(Labels.getLabel("classificationClass.add"));
                    createNewButton.setTooltiptext(Labels.getLabel("classificationClass.add"));
                    if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                    {
                        String id = "CatalogBrowser_Add_new_classification_button";
                        UITools.applyTestID((Component)createNewButton, "CatalogBrowser_Add_new_classification_button");
                    }
                }
                else
                {
                    allowCreateButton = checkPermissions("Category", "create");
                    createNewButton.setLabel(Labels.getLabel("category.add"));
                    createNewButton.setTooltiptext(Labels.getLabel("category.add"));
                    if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                    {
                        String id = "CatalogBrowser_Add_new_category_button";
                        UITools.applyTestID((Component)createNewButton, "CatalogBrowser_Add_new_category_button");
                    }
                }
                createNewButton.addEventListener("onClick", (EventListener)new Object(this, treeColumn, classificationSystem, bottomCell));
            }
            else if(treeColumn.isColumnType(ClassAttributeAssignmentNode.class))
            {
                allowCreateButton = checkPermissions("ClassAttributeAssignment", "create");
                UICockpitPerspective perspective = getModel().getArea().getManagingPerspective();
                CatalogVersionModel catVer = ((CatalogNavigationArea)((CatalogPerspective)perspective).getNavigationArea()).getSelectedCatalogVersion();
                if(catVer instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel)
                {
                    createNewButton.setLabel(Labels.getLabel("feature.add"));
                    createNewButton.setTooltiptext(Labels.getLabel("feature.add"));
                    createNewButton.addEventListener("onClick", (EventListener)new Object(this, perspective, treeColumn, bottomCell));
                }
            }
            else if(treeColumn.isColumnType(ProductNode.class))
            {
                allowCreateButton = checkPermissions("Product", "create");
                createNewButton.setLabel(Labels.getLabel("product.add"));
                createNewButton.setTooltiptext(Labels.getLabel("product.add"));
                createNewButton.addEventListener("onClick", (EventListener)new Object(this, treeColumn, bottomCell));
            }
            else if(treeColumn.isColumnType(MediaNode.class))
            {
                allowCreateButton = checkPermissions("Media", "create");
                createNewButton.setLabel(Labels.getLabel("media.add"));
                createNewButton.setTooltiptext(Labels.getLabel("media.add"));
                createNewButton.addEventListener("onClick", (EventListener)new Object(this));
            }
            if(allowCreateButton)
            {
                bottomCell.appendChild((Component)createNewButton);
            }
            South south = new South();
            south.setBorder("none");
            south.appendChild((Component)bottomCell);
            borderlayout.appendChild((Component)south);
            Div containerDiv = new Div();
            containerDiv.setHeight("100%");
            containerDiv.setSclass("mainColumnClass");
            containerDiv.appendChild((Component)borderlayout);
            mainColumn.appendChild((Component)containerDiv);
        }
        if(mainTableRow.getChildren().size() < 3)
        {
            addDummyColumns(mainTableRow, 3 - mainTableRow.getChildren().size());
        }
    }


    protected DragAndDropWrapper getDDWrapper()
    {
        return getModel().getArea().getPerspective().getDragAndDropWrapperService().getWrapper();
    }


    private Listbox renderColumnContent(MacFinderTreeColumn treeColumn)
    {
        Listbox columnContent = new Listbox();
        columnContent.setVflex(true);
        columnContent.setOddRowSclass("oddRowRowSclass");
        columnContent.setSclass("categoryColumnListbox");
        columnContent.setItemRenderer(getListItemRenderer());
        List<MacFinderTreeNode> children = treeColumn.getChildren();
        SimpleListModel model = new SimpleListModel(children);
        columnContent.setModel((ListModel)model);
        if(treeColumn.isLeaf())
        {
            columnContent.setMultiple(true);
            if(treeColumn.isColumnType(ProductNode.class))
            {
                columnContent.setDroppable("ct_product, PerspectiveDND");
            }
            else if(treeColumn.isColumnType(MediaNode.class))
            {
                columnContent.setDroppable("ct_media");
            }
            else if(treeColumn.isColumnType(CategoryNode.class))
            {
                columnContent.setDroppable("ct_category, ct_product, ct_media, PerspectiveDND");
            }
            columnContent.addEventListener("onDrop", (EventListener)new Object(this, treeColumn));
            columnContent.addEventListener("onSelect", (EventListener)new Object(this, columnContent, children));
        }
        else
        {
            columnContent.setMultiple(false);
            columnContent.setDroppable("ct_category");
            columnContent.addEventListener("onDrop", (EventListener)new Object(this, treeColumn));
            columnContent.addEventListener("onSelect", (EventListener)new Object(this, columnContent));
        }
        columnContent.addEventListener("onCancel", (EventListener)new Object(this, columnContent));
        columnContent
                        .setAction("onkeyup: var pressedKey = event.keyCode; if(pressedKey == 37 || pressedKey == 39 || pressedKey == 27 || pressedKey == 13) comm.sendUser(this,pressedKey);");
        columnContent.addEventListener("onOK", (EventListener)new Object(this, columnContent));
        columnContent.addEventListener("onUser", (EventListener)new Object(this, columnContent));
        return columnContent;
    }


    private void handleOnSelectOrOnClick(Listbox listbox)
    {
        MacFinderTreeNode selectedNode = (MacFinderTreeNode)listbox.getModel().getElementAt(listbox.getSelectedIndex());
        if(selectedNode.getContainingColumn().getIndex() < getTreeModel().getColumns().size() - 2)
        {
            scrollBeforeRemove(selectedNode.getContainingColumn().getIndex() + 1);
        }
        else
        {
            getTreeModel().deselectAll(selectedNode.getContainingColumn());
            selectedNode.setSelected(true);
            resetNeedlessColumns(selectedNode.getContainingColumn().getIndex());
            resetNeedlessBreadcrumbs(selectedNode.getContainingColumn().getIndex());
            getTreeModel().refreshAfterAdd(selectedNode);
            renderComponent(false);
            scrollToRight();
        }
    }


    private void scrollBeforeRemove(int index)
    {
        Table table = getTableContainer();
        int size = table.getFirstChild().getChildren().size();
        if(size != 0)
        {
            String id = null;
            String scrolledDivId = table.getParent().getId();
            if(index - 1 >= 0)
            {
                id = this.scrolledContainer.getId();
                Td listBoxTd = table.getFirstChild().getChildren().get(index - 1);
                String boxId = listBoxTd.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getId();
                Clients.evalJavaScript("slideMove('" + scrolledDivId + "','" + id + "',true,'" + boxId + "');");
            }
        }
    }


    private void scrollToRight()
    {
        Component tableContainerRow = getTableContainer().getFirstChild();
        int size = tableContainerRow.getChildren().size();
        if(size != 0)
        {
            String id = null;
            String divId = this.scrolledContainer.getId();
            id = ((Component)tableContainerRow.getChildren().get(size - 1)).getId();
            String jsStr = "slideMove(\"" + divId + "\",\"" + id + "\",false,'');";
            Clients.evalJavaScript(jsStr);
        }
    }


    private void handleCategoryColumnChange(Listbox listbox, int index)
    {
        if(listbox.getModel().getSize() > 0)
        {
            MacFinderTreeNode selectedNode = (MacFinderTreeNode)listbox.getModel().getElementAt(index);
            ((Listitem)listbox.getChildren().get(index)).setFocus(true);
            listbox.setSelectedIndex(index);
            getTreeModel().deselectAll(selectedNode.getContainingColumn());
            selectedNode.setSelected(true);
            if(selectedNode.getContainingColumn().isLeaf())
            {
                return;
            }
            resetNeedlessColumns(selectedNode.getContainingColumn().getIndex());
            resetNeedlessBreadcrumbs(selectedNode.getContainingColumn().getIndex());
            getTreeModel().extendHierarchy(selectedNode);
            renderComponent(false);
            scrollToRight();
        }
    }


    private Optional<Listbox> getCorrespondingListbox(int index)
    {
        Table table = getTableContainer();
        if(table != null)
        {
            Tr firstRow = (Tr)table.getFirstChild();
            Td tableCell = firstRow.getChildren().get(index);
            return Optional.of((Listbox)tableCell.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild());
        }
        return Optional.empty();
    }


    public void addDummyColumns(Tr parent, int quantity)
    {
        for(int i = 0; i < quantity; i++)
        {
            Vbox containerBox = new Vbox();
            containerBox.setHeights("80%,20%");
            Td cell = new Td();
            cell.setSclass("mainColumnClass");
            Listbox dummyColumn = new Listbox();
            dummyColumn.setHeight("100px");
            dummyColumn.setStyle("background-color:white;border:none;");
            Listitem item = new Listitem();
            dummyColumn.appendChild((Component)item);
            containerBox.appendChild((Component)dummyColumn);
            Div divlabel = new Div();
            containerBox.appendChild((Component)divlabel);
            cell.appendChild((Component)containerBox);
            cell.setParent((Component)parent);
        }
    }


    private void resetNeedlessColumns(int index)
    {
        Table table = getTableContainer();
        if(table != null)
        {
            Tr firstRow = (Tr)table.getFirstChild();
            for(int i = firstRow.getChildren().size() - 1; i > index; i--)
            {
                firstRow.getChildren().remove(i);
            }
        }
    }


    private void replaceBreadcrumb(int index, Component replacement)
    {
        Table table = getBreadCrumbsTable();
        Tr firstTr = (Tr)table.getFirstChild();
        firstTr.getChildren().remove(index);
        firstTr.getChildren().add(index, replacement);
    }


    private void resetNeedlessBreadcrumbs(int index)
    {
        Table table = getBreadCrumbsTable();
        if(table != null)
        {
            Tr firstRow = (Tr)table.getFirstChild();
            for(int i = firstRow.getChildren().size() - 1; i > index; i--)
            {
                firstRow.getChildren().remove(i);
            }
        }
    }


    private void renderBreadCrumbs() throws JaloSecurityException
    {
        Table table = getBreadCrumbsTable();
        if(table != null)
        {
            Tr firstChild = (Tr)table.getFirstChild();
            List<MacFinderTreeNode> categoryBreadcrumbs = getTreeModel().getCategoryElements();
            if(firstChild.getChildren().size() < categoryBreadcrumbs.size())
            {
                for(int index = firstChild.getChildren().size(); index < categoryBreadcrumbs.size(); index++)
                {
                    if(categoryBreadcrumbs.get(index) != null)
                    {
                        Td singleBreadcrumb = createBredcrumbElement((index == 0), categoryBreadcrumbs.get(index));
                        firstChild.appendChild((Component)singleBreadcrumb);
                    }
                }
            }
            else if(firstChild.getChildren().size() == categoryBreadcrumbs.size() && !categoryBreadcrumbs.isEmpty())
            {
                if(categoryBreadcrumbs.get(categoryBreadcrumbs.size() - 1) != null)
                {
                    Td singleBreadcrumb = createBredcrumbElement((categoryBreadcrumbs.size() == 1), categoryBreadcrumbs
                                    .get(categoryBreadcrumbs.size() - 1));
                    replaceBreadcrumb(firstChild.getChildren().size() - 1, (Component)singleBreadcrumb);
                }
                else
                {
                    replaceBreadcrumb(firstChild.getChildren().size() - 1, (Component)new Td());
                }
            }
        }
    }


    private Td createBredcrumbElement(boolean first, MacFinderTreeNode category) throws JaloSecurityException
    {
        Td container = new Td();
        container.setSclass("breadcrumbsContent");
        Label labelContainer = null;
        if(!first)
        {
            labelContainer = new Label();
            labelContainer.setValue(" >> ");
        }
        Toolbarbutton linkContainer = new Toolbarbutton(category.getDisplayedLabel());
        linkContainer.setSclass("breadcrumbs");
        linkContainer.addEventListener("onClick", event -> getCorreExpandableTreeNode(category).map(()).map(Optional::get).ifPresent(this::handleOnSelectOrOnClick));
        if(labelContainer != null)
        {
            container.appendChild((Component)labelContainer);
        }
        container.appendChild((Component)linkContainer);
        return container;
    }


    private Optional<MacFinderTreeNode> getCorreExpandableTreeNode(MacFinderTreeNode category)
    {
        return getTreeModel().getColumns().stream().flatMap(column -> column.getChildren().stream())
                        .filter(Predicate.isEqual(category)).findFirst();
    }


    public MacFinderTreeNode getLastSelectedCategory()
    {
        MacFinderTreeNode category = null;
        for(MacFinderTreeColumn item : getTreeModel().getColumns())
        {
            for(MacFinderTreeNode node : item.getChildren())
            {
                if(node != null && node.isSelected() && node instanceof de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNodeChildable)
                {
                    category = node;
                }
            }
        }
        return category;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected NewItemService getNewItemService()
    {
        if(this.newItemService == null)
        {
            this.newItemService = (NewItemService)SpringUtil.getBean("newItemService");
        }
        return this.newItemService;
    }


    protected boolean checkPermissions(String typeCode, String permissionCode)
    {
        return UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(typeCode, permissionCode);
    }


    public ExtendedTypeLoader getCockpitTypeLoaderChain()
    {
        if(this.cockpitTypeLoaderChain == null)
        {
            this.cockpitTypeLoaderChain = (ExtendedTypeLoader)SpringUtil.getBean("cockpitTypeLoaderChain");
        }
        return this.cockpitTypeLoaderChain;
    }


    protected void updateInspectorArea(TypedObject object, AbstractBrowserArea area)
    {
        if(object != null)
        {
            ((BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective()).collapseEditorArea();
            area.updateInfoArea((ListProvider)new Object(this, object), true);
        }
    }
}
