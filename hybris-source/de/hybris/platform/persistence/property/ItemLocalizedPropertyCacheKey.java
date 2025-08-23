package de.hybris.platform.persistence.property;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.ExtensibleItemEJB;
import de.hybris.platform.persistence.ItemEJB;

public final class ItemLocalizedPropertyCacheKey extends ItemPropertyCacheKey
{
    public static final String LOC = ".loc.";
    private final PK langPK;
    private final String quali;


    ItemLocalizedPropertyCacheKey(EJBPropertyRowCache preset)
    {
        super(preset);
        this.langPK = preset.getLangPK();
        this.quali = "item.property.loc." + this.langPK.getLongValueAsString();
    }


    public ItemLocalizedPropertyCacheKey(TypeInfoMap info, PK langPK)
    {
        super(info);
        if(langPK == null)
        {
            throw new JaloSystemException(null, "'null' is no valid language pk", 4711);
        }
        this.langPK = langPK;
        this.quali = "item.property.loc." + langPK.getLongValueAsString();
    }


    protected ItemLocalizedPropertyCacheKey(TypeInfoMap info, PK itemPK, PK langPK)
    {
        super(itemPK);
        if(langPK == null)
        {
            throw new JaloSystemException(null, "'null' is no valid language pk", 4711);
        }
        this.langPK = langPK;
        this.quali = "item.property.loc." + langPK.getLongValueAsString();
    }


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.cloned)
        {
            throw new EJBInternalException(null, "you cannot clone a cloned ItemLocalizedPropertyCacheKey again", 0);
        }
        return new ItemLocalizedPropertyCacheKey(this.info, this.itemPK, this.langPK);
    }


    protected String getQualifier()
    {
        return this.quali;
    }


    protected Object computeValue(ItemEJB item)
    {
        ExtensibleItemEJB ei = (ExtensibleItemEJB)item;
        this.itemPK = ei.getPK();
        return ei.isBeforeCreate() ? PropertyJDBC.createProperties((this.info != null) ? this.info : ei.getTypeInfoMap(), this.itemPK, ei
                        .getTypePkString(), this.langPK, ei.getPropertyTimestampInternal()) : PropertyJDBC.getProperties(
                        (this.info != null) ? this.info :
                                        ei.getTypeInfoMap(), this.itemPK, ei.getTypePkString(), this.langPK, ei.getPropertyTimestampInternal());
    }
}
