package de.hybris.platform.persistence.property;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.ExtensibleItemEJB;
import de.hybris.platform.persistence.ItemEJB;

public final class ItemOldPropertyCacheKey extends ItemPropertyCacheKey
{
    public static final String QUALI = "item.property.old".intern();


    public ItemOldPropertyCacheKey()
    {
        super((TypeInfoMap)null);
    }


    protected ItemOldPropertyCacheKey(PK itemPK)
    {
        super(itemPK);
    }


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.cloned)
        {
            throw new EJBInternalException(null, "you cannot clone a cloned ItemOldPropertyCacheKey again", 0);
        }
        return new ItemOldPropertyCacheKey(this.itemPK);
    }


    protected String getQualifier()
    {
        return QUALI;
    }


    protected boolean isValid(ItemEJB item)
    {
        ExtensibleItemEJB ei = (ExtensibleItemEJB)item;
        EJBPropertyCache pc = (EJBPropertyCache)getValueInternal();
        boolean isValid = (pc == null || ei.getPK().equals(this.itemPK));
        if(!isValid)
        {
            throw new JaloSystemException(null, "ItemOldPropertyCacheKey not valid:\n  pc = " + pc + "\n  item pk = " + this.itemPK + "\n  cache pk =  " + ei
                            .getPkString() + "\n  item time = " + ei
                            .getPropertyTimestampInternal() + "\n  pc time =   " + pc.getVersion(), 0);
        }
        return isValid;
    }


    protected void dispose(boolean inRemove)
    {
        EJBPropertyCache pc = (EJBPropertyCache)getValueInternal();
        if(!inRemove && pc != null && pc.needsUpdate())
        {
            throw new JaloSystemException("removing changed old property cache from item cache will lose these changes : itemPK = " + this.itemPK);
        }
        super.dispose(inRemove);
    }


    protected Object computeValue(ItemEJB item)
    {
        ExtensibleItemEJB ei = (ExtensibleItemEJB)item;
        this.itemPK = ei.getPK();
        return ei.isBeforeCreate() ? OldPropertyJDBC.createProperties(this.itemPK, ei.getPropertyTimestampInternal(), ei
                        .getPropertyTableNameImpl()) : OldPropertyJDBC.getProperties(this.itemPK, ei.getPropertyTimestampInternal(), ei
                        .getPropertyTableNameImpl());
    }


    protected Object cloneValue(Object value)
    {
        try
        {
            return ((EJBPropertyCache)value).clone();
        }
        catch(CloneNotSupportedException e)
        {
            throw new EJBInternalException(e, "EJBPropertyCache should be cloneable, but...", 0);
        }
    }
}
