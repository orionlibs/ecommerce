package de.hybris.platform.util;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloOnlySingletonItem;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public final class ViewResultItem extends Item implements JaloOnlySingletonItem
{
    private final PK MYPK;
    private final ComposedType composedType;
    private final Item.ItemAttributeMap values;
    private final Date creationTime;


    public ViewResultItem()
    {
        this(null, Collections.EMPTY_MAP);
    }


    protected ViewResultItem(ComposedType type, Map values)
    {
        this.MYPK = PK.createUUIDPK(93);
        this.composedType = type;
        this.values = new Item.ItemAttributeMap(values);
        this.creationTime = new Date();
    }


    public Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        return new ViewResultItem(type, (Map)allAttributes);
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap ret = super.getNonInitialAttributes(ctx, allAttributes);
        if(allAttributes != null)
        {
            for(Map.Entry<String, Object> e : (Iterable<Map.Entry<String, Object>>)allAttributes.entrySet())
            {
                ret.remove(e.getKey());
            }
        }
        return ret;
    }


    public void removeJaloOnly() throws ConsistencyCheckException
    {
    }


    public PK providePK()
    {
        return this.MYPK;
    }


    public ComposedType provideComposedType()
    {
        return this.composedType;
    }


    public Date provideCreationTime()
    {
        return this.creationTime;
    }


    public Date provideModificationTime()
    {
        return this.creationTime;
    }


    public Object doGetAttribute(SessionContext ctx, String qualifier)
    {
        return this.values.get(qualifier);
    }


    public void doSetAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        throw new JaloInvalidParameterException("cannot modify ViewResultItem", 0);
    }
}
