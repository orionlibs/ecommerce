package de.hybris.platform.productcockpit.model.macfinder;

import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MacFinderTreeColumn
{
    private static final Logger LOG = LoggerFactory.getLogger(MacFinderTreeColumn.class);
    private int index = 0;
    private List<MacFinderTreeNode> children = new ArrayList<>();
    private MacFinderTreeNode columnType;


    public boolean isColumnType(Class<? extends MacFinderTreeNode> columnTypeClass)
    {
        return columnTypeClass.isInstance(this.columnType);
    }


    public MacFinderTreeColumn getParentColumn()
    {
        MacFinderTreeColumn ret = null;
        try
        {
            ret = getParentNode().getContainingColumn();
        }
        catch(Exception e)
        {
            LOG.debug(e.getMessage(), e);
        }
        return ret;
    }


    public MacFinderTreeNode getParentNode()
    {
        return this.columnType;
    }


    public void setColumnType(MacFinderTreeNode columnType)
    {
        this.columnType = columnType;
    }


    public int getIndex()
    {
        return this.index;
    }


    public void setIndex(int index)
    {
        this.index = index;
    }


    public void setChildren(List<MacFinderTreeNode> children)
    {
        this.children = children;
    }


    public List<MacFinderTreeNode> getChildren()
    {
        for(MacFinderTreeNode child : this.children)
        {
            child.setParentColumn(this);
        }
        return this.children;
    }


    public void appendToChild(List<MacFinderTreeNode> additonalChilds)
    {
        for(MacFinderTreeNode child : additonalChilds)
        {
            child.setParentColumn(this);
            this.children.add(child);
        }
    }


    public void removeChild(MacFinderTreeNode treeNode)
    {
        if(this.children.contains(treeNode))
        {
            this.children.remove(treeNode);
        }
    }


    public MacFinderTreeNode getSelectedNode()
    {
        MacFinderTreeNode ret = null;
        for(MacFinderTreeNode node : getChildren())
        {
            if(node.isSelected())
            {
                ret = node;
                break;
            }
        }
        return ret;
    }


    public boolean isLeaf()
    {
        if(this.children.isEmpty())
        {
            return false;
        }
        Object object = this.children.get(0);
        if(object instanceof de.hybris.platform.productcockpit.model.macfinder.node.LeafNode)
        {
            return true;
        }
        return false;
    }
}
