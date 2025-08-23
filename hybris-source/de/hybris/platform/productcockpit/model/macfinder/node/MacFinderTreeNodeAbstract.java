package de.hybris.platform.productcockpit.model.macfinder.node;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeColumn;
import org.apache.commons.lang.StringUtils;

public abstract class MacFinderTreeNodeAbstract implements MacFinderTreeNode
{
    private MacFinderTreeColumn column;
    private boolean selected;
    private int quantity;
    private TypedObject orginalItem;


    public void setParentColumn(MacFinderTreeColumn column)
    {
        this.column = column;
    }


    public MacFinderTreeColumn getContainingColumn()
    {
        return this.column;
    }


    public boolean isSelected()
    {
        return this.selected;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }


    public int getQuantity()
    {
        return this.quantity;
    }


    public String getDisplayedLabel()
    {
        String ret = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(getOriginalItem());
        return StringUtils.isBlank(ret) ? "undefined" : ret;
    }


    public TypedObject getOriginalItem()
    {
        return this.orginalItem;
    }


    public void setOriginalItem(TypedObject orginalItem)
    {
        this.orginalItem = orginalItem;
    }


    public boolean isNameAttributeExist() throws JaloSecurityException
    {
        return true;
    }


    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + ((this.orginalItem == null) ? 0 : this.orginalItem.hashCode());
        return result;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        MacFinderTreeNodeAbstract other = (MacFinderTreeNodeAbstract)obj;
        if(this.orginalItem == null)
        {
            if(other.orginalItem != null)
            {
                return false;
            }
        }
        else if(!this.orginalItem.equals(other.orginalItem))
        {
            return false;
        }
        return true;
    }


    public boolean isVisible()
    {
        return true;
    }
}
