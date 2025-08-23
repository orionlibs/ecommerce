package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationAttributeAccess implements AttributeAccess
{
    private final boolean isSource;
    private final boolean localized;
    private final String qualifier;
    private final boolean singleValued;


    public RelationAttributeAccess(String qualifier, boolean isSource, boolean localized, boolean singleValued)
    {
        this.localized = localized;
        this.isSource = isSource;
        this.qualifier = qualifier;
        this.singleValued = singleValued;
    }


    protected Map convertSingleValuedMapToListMap(Map singleValuedMap)
    {
        Map<Object, Object> ret = null;
        if(!singleValuedMap.isEmpty())
        {
            ret = new HashMap<>();
            for(Map.Entry e : singleValuedMap.entrySet())
            {
                ret.put(e.getKey(), Collections.singletonList(e.getValue()));
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }


    protected Map convertListMapToSingleValuedMap(Map listMap)
    {
        Map<Object, Object> ret = null;
        if(!listMap.isEmpty())
        {
            ret = new HashMap<>();
            for(Map.Entry e : listMap.entrySet())
            {
                ret.put(e.getKey(), ((List)e.getValue()).get(0));
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }


    public final void setValue(SessionContext ctx, Item item, Object value) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException
    {
        if(this.localized)
        {
            if(ctx == null || ctx.getLanguage() == null)
            {
                item.setAllLinkedItems(ctx, this.isSource, this.qualifier, this.singleValued ? convertSingleValuedMapToListMap((Map)value) :
                                (Map)value);
            }
            else
            {
                item.setLinkedItems(ctx, this.isSource, this.qualifier, ctx.getLanguage(),
                                this.singleValued ? Collections.<Item>singletonList((Item)value) :
                                                (List)value);
            }
        }
        else
        {
            item.setLinkedItems(ctx, this.isSource, this.qualifier, null,
                            this.singleValued ? Collections.<Item>singletonList((Item)value) : (List)value);
        }
    }


    public final Object getValue(SessionContext ctx, Item item) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException
    {
        if(this.localized)
        {
            if(ctx == null || ctx.getLanguage() == null)
            {
                Map map = item.getAllLinkedItems(this.isSource, this.qualifier);
                return this.singleValued ? convertListMapToSingleValuedMap(map) : map;
            }
            List list1 = item.getLinkedItems(ctx, this.isSource, this.qualifier, ctx.getLanguage());
            if(this.singleValued)
            {
                return (list1 != null && !list1.isEmpty()) ? list1.get(0) : null;
            }
            return list1;
        }
        List list = item.getLinkedItems(ctx, this.isSource, this.qualifier, null);
        if(this.singleValued)
        {
            return (list != null && !list.isEmpty()) ? list.get(0) : null;
        }
        return list;
    }
}
