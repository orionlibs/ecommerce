package de.hybris.platform.productcockpit.model.macfinder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.productcockpit.model.macfinder.node.CategoryNode;
import de.hybris.platform.productcockpit.model.macfinder.node.LeafNode;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNodeAbstract;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

public abstract class MacFinderTreeModelAbstract
{
    private static final Logger LOG = LoggerFactory.getLogger(MacFinderTreeModelAbstract.class);
    private final List<MacFinderTreeNode> categoryElements = new ArrayList<>();
    private List<MacFinderTreeColumn> columns = new ArrayList<>();
    private MacFinderTree bindedComponent;
    private MacFinderTreeNode category;


    public void setColumns(List<MacFinderTreeColumn> columns)
    {
        this.columns = columns;
    }


    public MacFinderTreeModelAbstract()
    {
        reloadRoot();
    }


    public MacFinderTreeModelAbstract(List<MacFinderTreeNode> selectedNodes)
    {
        this();
        for(MacFinderTreeNode treeNode : selectedNodes)
        {
            MacFinderTreeColumn lastColumn = getColumns().get(getColumns().size() - 1);
            label15:
            for(MacFinderTreeNode node : lastColumn.getChildren())
            {
                if(!(treeNode instanceof CategoryNode) || !node.getOriginalItem().equals(treeNode.getOriginalItem()))
                {
                    if(!(treeNode instanceof CategoryNode) && treeNode
                                    .getClass().equals(node.getClass()))
                    {
                        node.setSelected(true);
                        extendHierarchy(node);
                    }
                    continue;
                }
                break label15;
            }
        }
    }


    public void reInit()
    {
        getCategoryElements().clear();
        getColumns().clear();
        this.category = null;
        reloadRoot();
    }


    public void reloadRoot()
    {
        MacFinderTreeColumn firstColumn = new MacFinderTreeColumn();
        CategoryNode categoryNode = new CategoryNode();
        TypedObject catalogVersion = UISessionUtils.getCurrentSession().getTypeService().wrapItem(getCatalogVersion());
        categoryNode.setOriginalItem(catalogVersion);
        List<MacFinderTreeNode> categories = doCheckedSearchInternal((MacFinderTreeNode)categoryNode, getCatalogVersion(), (MacFinderTreeNode)categoryNode);
        firstColumn.setChildren(categories);
        addUncategoriezedProduct(firstColumn, getCatalogVersion());
        firstColumn.setColumnType((MacFinderTreeNode)categoryNode);
        getColumns().add(firstColumn);
    }


    private void addUncategoriezedProduct(MacFinderTreeColumn column, CatalogVersionModel version)
    {
        String parameterName = "default.categoryTreeBrowser.hideUncategorizedProduct";
        String parameterValue = UITools.getCockpitParameter(parameterName, Executions.getCurrent());
        boolean hideUncategoriezedProduct = Boolean.parseBoolean(parameterValue);
        parameterName = parameterName + "." + parameterName;
        parameterValue = UITools.getCockpitParameter(parameterName, Executions.getCurrent());
        boolean hideUncategorizedProductForCatalog = (parameterValue == null) ? hideUncategoriezedProduct : Boolean.parseBoolean(parameterValue);
        if(!hideUncategorizedProductForCatalog)
        {
            List<MacFinderTreeNode> connectedItems = new ArrayList<>(getConnectedItems());
            for(MacFinderTreeNode item : connectedItems)
            {
                CategoryNode categoryNode = new CategoryNode();
                int quantity = countSearchResultsInternal((MacFinderTreeNode)categoryNode, version, item);
                item.setQuantity(quantity);
            }
            column.appendToChild(connectedItems);
        }
    }


    public void refreshWholeTree()
    {
        MacFinderTreeColumn rootColumn = null;
        MacFinderTreeColumn previousColumn = null;
        List<Integer> columnsToRemove = new ArrayList<>();
        for(Iterator<MacFinderTreeColumn> iter = this.columns.iterator(); iter.hasNext(); )
        {
            MacFinderTreeColumn column = iter.next();
            if(column.getIndex() == 0)
            {
                rootColumn = new MacFinderTreeColumn();
                CategoryNode categoryNode = new CategoryNode();
                TypedObject catalogVersion = UISessionUtils.getCurrentSession().getTypeService().wrapItem(getCatalogVersion());
                categoryNode.setOriginalItem(catalogVersion);
                MacFinderTreeNode macFinderTreeNode = column.getSelectedNode();
                List<MacFinderTreeNode> categories = doCheckedSearchInternal((MacFinderTreeNode)categoryNode, getCatalogVersion(), (MacFinderTreeNode)categoryNode);
                for(MacFinderTreeNode node : categories)
                {
                    if(node.equals(macFinderTreeNode))
                    {
                        node.setSelected(Boolean.TRUE.booleanValue());
                        break;
                    }
                }
                rootColumn.setChildren(categories);
                addUncategoriezedProduct(rootColumn, getCatalogVersion());
                rootColumn.setColumnType((MacFinderTreeNode)categoryNode);
                previousColumn = column;
                continue;
            }
            MacFinderTreeNode selectedNode = (previousColumn == null) ? null : previousColumn.getSelectedNode();
            if(selectedNode != null)
            {
                if(selectedNode instanceof de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNodeChildable)
                {
                    List<MacFinderTreeNode> currentItemsFromDb = doCheckedSearchInternal(selectedNode, getCatalogVersion(), selectedNode);
                    List<MacFinderTreeNode> rockUpItems = new ArrayList<>();
                    List<MacFinderTreeNode> leftDownItems = new ArrayList<>();
                    rockUpItems.addAll(currentItemsFromDb);
                    rockUpItems.removeAll(column.getChildren());
                    leftDownItems.addAll(column.getChildren());
                    leftDownItems.removeAll(currentItemsFromDb);
                    int index = -1;
                    for(MacFinderTreeNode rTreeNode : leftDownItems)
                    {
                        if(rTreeNode.isSelected() && rTreeNode instanceof CategoryNode)
                        {
                            index = column.getIndex() + 1;
                            columnsToRemove.add(Integer.valueOf(index));
                        }
                    }
                    column.appendToChild(rockUpItems);
                    List<MacFinderTreeNode> connectedItems = new ArrayList<>();
                    for(MacFinderTreeNode treeNode : leftDownItems)
                    {
                        if(treeNode instanceof de.hybris.platform.productcockpit.model.macfinder.node.ProductNode || treeNode instanceof de.hybris.platform.productcockpit.model.macfinder.node.MediaNode
                                        || treeNode instanceof de.hybris.platform.productcockpit.model.macfinder.node.ClassAttributeAssignmentNode)
                        {
                            connectedItems.add(treeNode);
                        }
                        column.removeChild(treeNode);
                    }
                    for(MacFinderTreeNode item : connectedItems)
                    {
                        MacFinderTreeNode treeNode = previousColumn.getSelectedNode();
                        int quantity = countSearchResultsInternal(treeNode, getCatalogVersion(), item);
                        item.setQuantity(quantity);
                    }
                    column.appendToChild(connectedItems);
                }
                else if(column.getIndex() == this.columns.size() - 1)
                {
                    MacFinderTreeNode selectedTreeNode = column.getSelectedNode();
                    int index = column.getIndex();
                    if(index < 0)
                    {
                        index = 0;
                    }
                    MacFinderTreeNodeAbstract macFinderTreeNodeAbstract = getSelectedCategory(index - 2);
                    MacFinderTreeNode treeNode = previousColumn.getSelectedNode();
                    List<MacFinderTreeNode> leafItems = doCheckedSearchInternal((MacFinderTreeNode)macFinderTreeNodeAbstract, getCatalogVersion(), treeNode);
                    leafItems.stream().filter(item -> item.equals(selectedTreeNode)).forEach(item -> item.setSelected(true));
                    column.setChildren(leafItems);
                }
            }
            previousColumn = column;
        }
        this.columns.set(0, rootColumn);
        if(!columnsToRemove.isEmpty())
        {
            int start = ((Integer)columnsToRemove.get(0)).intValue();
            List<MacFinderTreeColumn> subColumns = this.columns.subList(0, start);
            setColumns(subColumns);
        }
    }


    public MacFinderTree getBindedComponent()
    {
        return this.bindedComponent;
    }


    public void setBindedComponent(MacFinderTree bindedComponent)
    {
        this.bindedComponent = bindedComponent;
    }


    public List<MacFinderTreeNode> getCategoryElements()
    {
        return this.categoryElements;
    }


    public List<MacFinderTreeNode> getSelectedNodes()
    {
        List<MacFinderTreeNode> ret = new ArrayList<>();
        for(MacFinderTreeColumn column : getColumns())
        {
            if(!column.isLeaf())
            {
                MacFinderTreeNode selectedNode = column.getSelectedNode();
                if(selectedNode != null)
                {
                    ret.add(selectedNode);
                }
            }
        }
        return ret;
    }


    private void setCategory(MacFinderTreeNode category)
    {
        this.category = category;
    }


    public List<MacFinderTreeColumn> getColumns()
    {
        return this.columns;
    }


    public MacFinderTreeNode getCategory()
    {
        return this.category;
    }


    public static int getMaxItemCountProperty()
    {
        int count = -1;
        String cockpitParameter = UITools.getCockpitParameter("default.catalogBrowser.maxEntries", Executions.getCurrent());
        if(cockpitParameter != null)
        {
            try
            {
                count = Integer.parseInt(cockpitParameter);
            }
            catch(NumberFormatException e)
            {
                LOG.error("Wrong number format for property 'default.catalogBrowser.maxEntries', expected integer, got '" + cockpitParameter + "'");
            }
        }
        return count;
    }


    private List<MacFinderTreeNode> doCheckedSearchInternal(MacFinderTreeNode node, CatalogVersionModel version, MacFinderTreeNode connectedItem)
    {
        List<MacFinderTreeNode> list = doSearchInternal(node, version, connectedItem);
        if(node != null && list != null && node.getQuantity() > list.size())
        {
            UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier().setNotification(new Notification(
                            Labels.getLabel("catalogperspective.categorybrowser.toomanyitemserror", (Object[])new String[] {String.valueOf(getMaxItemCountProperty())})));
        }
        return list;
    }


    public void extendHierarchy(MacFinderTreeNode node)
    {
        for(int i = this.columns.size() - 1; i > node.getContainingColumn().getIndex(); i--)
        {
            this.columns.remove(i);
        }
        handleBreadcrumbs(node);
        MacFinderTreeColumn nextColumn = new MacFinderTreeColumn();
        if(node instanceof de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNodeChildable)
        {
            List<MacFinderTreeNode> list = new ArrayList<>();
            list.addAll(doCheckedSearchInternal(node, getCatalogVersion(), node));
            for(MacFinderTreeNode node1 : list)
            {
                if(getCategoryElements().contains(node1))
                {
                    node1.setSelected(true);
                }
            }
            nextColumn.setChildren(list);
            List<MacFinderTreeNode> connectedItems = getConnectedItems();
            for(MacFinderTreeNode item : connectedItems)
            {
                int index = node.getContainingColumn().getIndex();
                int quantity = countSearchResultsInternal((MacFinderTreeNode)getSelectedCategory(index), getCatalogVersion(), item);
                item.setQuantity(quantity);
            }
            nextColumn.appendToChild(connectedItems);
        }
        else
        {
            int index = node.getContainingColumn().getIndex() - 1;
            if(index < 0)
            {
                index = 0;
            }
            MacFinderTreeNodeAbstract macFinderTreeNodeAbstract = getSelectedCategory(index);
            List<MacFinderTreeNode> leafItems = doCheckedSearchInternal((MacFinderTreeNode)macFinderTreeNodeAbstract, getCatalogVersion(), node);
            nextColumn.setChildren(leafItems);
        }
        nextColumn.setColumnType(node);
        setCategory(node);
        nextColumn.setIndex(this.columns.size());
        this.columns.add(nextColumn);
    }


    public MacFinderTreeNodeAbstract getSelectedCategory(int index)
    {
        if(index < 0 || index > getColumns().size())
        {
            return null;
        }
        MacFinderTreeColumn column = getColumns().get(index);
        for(MacFinderTreeNode node : column.getChildren())
        {
            if(node.isSelected())
            {
                return (MacFinderTreeNodeAbstract)node;
            }
        }
        return null;
    }


    private void handleBreadcrumbs(MacFinderTreeNode node)
    {
        MacFinderTreeColumn level = node.getContainingColumn();
        int actualSize = this.categoryElements.size();
        if(level.getIndex() < this.categoryElements.size())
        {
            for(int i = actualSize - 1; i >= level.getIndex(); i--)
            {
                this.categoryElements.remove(i);
            }
        }
        if(node instanceof de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNodeChildable)
        {
            this.categoryElements.add(node);
        }
    }


    public void refreshAfterAdd(MacFinderTreeNode node)
    {
        doBeforeExtendHierarchy();
        extendHierarchy(node);
        doAfterExtendHierarchy();
    }


    public void deselectAll(MacFinderTreeColumn level)
    {
        for(MacFinderTreeNode node : level.getChildren())
        {
            node.setSelected(false);
        }
    }


    public List<LeafNode> getLeafNodes()
    {
        List<LeafNode> list = new ArrayList<>();
        MacFinderTreeColumn lastColumn = getColumns().get(getColumns().size() - 1);
        for(MacFinderTreeNode treeNode : lastColumn.getChildren())
        {
            if(treeNode instanceof LeafNode)
            {
                list.add((LeafNode)treeNode);
            }
        }
        return list;
    }


    public void updateLeafNodes()
    {
        List<MacFinderTreeNode> selectedNodes = getSelectedNodes();
        if(!selectedNodes.isEmpty())
        {
            for(int i = 0; i < selectedNodes.size(); i++)
            {
                MacFinderTreeNode node = selectedNodes.get(i);
                if(node != null)
                {
                    extendHierarchy(node);
                }
            }
        }
    }


    public abstract CatalogVersionModel getCatalogVersion();


    public abstract List<MacFinderTreeNode> getConnectedItems();


    public abstract void doOnEnter(TypedObject paramTypedObject);


    public abstract void doBeforeExtendHierarchy();


    protected abstract void doAfterExtendHierarchy();


    public abstract List<MacFinderTreeNode> doSearchInternal(MacFinderTreeNode paramMacFinderTreeNode1, CatalogVersionModel paramCatalogVersionModel, MacFinderTreeNode paramMacFinderTreeNode2);


    public abstract int countSearchResultsInternal(MacFinderTreeNode paramMacFinderTreeNode1, CatalogVersionModel paramCatalogVersionModel, MacFinderTreeNode paramMacFinderTreeNode2);
}
