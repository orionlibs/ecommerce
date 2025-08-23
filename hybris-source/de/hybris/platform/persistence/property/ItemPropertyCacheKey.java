package de.hybris.platform.persistence.property;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.ExtensibleItemEJB;
import de.hybris.platform.persistence.ItemCacheKey;
import de.hybris.platform.persistence.ItemEJB;

public class ItemPropertyCacheKey extends ItemCacheKey implements PropertyOwner
{
    public static final String QUALI = "item.property.unloc".intern();
    protected PK itemPK = null;
    protected TypeInfoMap info;
    protected final boolean cloned;


    public ItemPropertyCacheKey(TypeInfoMap info)
    {
        this.cloned = false;
        this.info = info;
    }


    protected ItemPropertyCacheKey(EJBPropertyRowCache preset)
    {
        super(preset);
        this.itemPK = preset.getItemPK();
        this.cloned = false;
    }


    public ItemPropertyCacheKey(EJBPropertyRowCache prc, PK itemPK)
    {
        super(prc);
        this.itemPK = itemPK;
        this.cloned = false;
    }


    protected ItemPropertyCacheKey(PK itemPK)
    {
        this.itemPK = itemPK;
        this.cloned = true;
    }


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.cloned)
        {
            throw new EJBInternalException(null, "you cannot clone a cloned ItemPropertyCacheKey again", 0);
        }
        return new ItemPropertyCacheKey(this.itemPK);
    }


    protected String getQualifier()
    {
        return QUALI;
    }


    protected boolean isValid(ItemEJB item)
    {
        ExtensibleItemEJB ei = (ExtensibleItemEJB)item;
        EJBPropertyRowCache pc = (EJBPropertyRowCache)getValueInternal();
        boolean isValid = (pc == null || ei.getPK().equals(this.itemPK));
        if(!isValid)
        {
            throw new JaloSystemException(null, "ItemPropertyCacheKey not valid:\n  pc = " + pc + "\n  item pk = " + this.itemPK + "\n  lang pk = " + pc
                            .getLangPK() + "\n  cache pk =  " + ei.getPkString() + "\n  item time = " + ei
                            .getPropertyTimestampInternal() + "\n  pc time =   " + pc.getVersion(), 0);
        }
        return isValid;
    }


    protected void dispose(boolean inRemove)
    {
        EJBPropertyRowCache pc = (EJBPropertyRowCache)getValueInternal();
        if(!inRemove && pc != null && pc.hasChanged())
        {
            throw new JaloSystemException("removing changed property row cache from item cache will lose these changes : itemPK = " + this.itemPK);
        }
        super.dispose(inRemove);
    }


    protected Object computeValue(ItemEJB item)
    {
        ExtensibleItemEJB ei = (ExtensibleItemEJB)item;
        this.itemPK = ei.getPK();
        try
        {
            return ei.isBeforeCreate() ? PropertyJDBC.createProperties((this.info != null) ? this.info : ei.getTypeInfoMap(), this.itemPK, ei
                            .getTypePkString(), null, ei.getPropertyTimestampInternal()) : PropertyJDBC.getProperties(
                            (this.info != null) ? this.info :
                                            ei.getTypeInfoMap(), this.itemPK, ei.getTypePkString(), null, ei.getPropertyTimestampInternal());
        }
        finally
        {
            this.info = null;
        }
    }


    protected Object cloneValue(Object value)
    {
        try
        {
            return ((EJBPropertyRowCache)value).clone();
        }
        catch(CloneNotSupportedException e)
        {
            throw new EJBInternalException(e, "EJBPropertyRowCache should be cloneable, but...", 0);
        }
    }


    protected final Object createUnmodifiableViewOfValue(Object value)
    {
        return value;
    }


    public final void notifyPropertyDataChanged(long ts)
    {
        if(!this.cloned)
        {
            throw new EJBInternalException(null, "illegal modification on non-cloned ItemPropertyCacheKey", 0);
        }
    }
}
