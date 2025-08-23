package de.hybris.platform.jalo.type;

import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericValueCondition;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.JaloTools;
import de.hybris.platform.util.StandardSearchResult;
import de.hybris.platform.util.ViewResultItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewType extends ComposedType
{
    public static final String QUERY = "query";
    public static final String COLUMNS = "columns";
    public static final String PARAMS = "params";


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item.ItemAttributeMap newMap = new Item.ItemAttributeMap((Map)allAttributes);
        newMap.put(SUPERTYPE, type.getSession().getTypeManager().getComposedType(Item.class));
        return super.createItem(ctx, type, newMap);
    }


    static
    {
        registerAccessFor(ViewType.class, "params", (AttributeAccess)new Object());
        registerAccessFor(ViewType.class, "columns", (AttributeAccess)new Object());
    }

    public Class getJaloClass()
    {
        return ViewResultItem.class;
    }


    public boolean isAbstract()
    {
        return false;
    }


    public String toString(SessionContext ctx, Object value) throws JaloInvalidParameterException
    {
        throw new JaloInvalidParameterException("ViewTypes do not support toString(Object)", 0);
    }


    public Object parseValue(SessionContext ctx, String value) throws JaloInvalidParameterException
    {
        throw new JaloInvalidParameterException("ViewTypes do not support parseValue(String)", 0);
    }


    public String getQuery()
    {
        return (String)getProperty("query");
    }


    public void setQuery(String q)
    {
        if(q == null)
        {
            throw new JaloInvalidParameterException("query cannot be null", 0);
        }
        getSession().getFlexibleSearch().checkQuery(q, true);
        setProperty("query", q);
    }


    protected void setParamAttributeDescriptors(Set<?> params)
    {
        Set old = getParamAttributeDescriptors();
        if(params != null)
        {
            old.removeAll(params);
            for(Iterator<?> iter = params.iterator(); iter.hasNext(); )
            {
                ViewAttributeDescriptor a = (ViewAttributeDescriptor)iter.next();
                if(!equals(a.getEnclosingType()))
                {
                    throw new JaloInvalidParameterException("view attribute " + a + " does not belong to this type (" + getCode() + ") but to " + a
                                    .getEnclosingType().getCode(), 0);
                }
                if(!a.isParam())
                {
                    a.setParam(true);
                }
            }
        }
        for(Iterator<ViewAttributeDescriptor> it = old.iterator(); it.hasNext(); )
        {
            ViewAttributeDescriptor a = it.next();
            try
            {
                a.remove();
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    public Set getParamAttributeDescriptors()
    {
        Set<AttributeDescriptor> ret = new HashSet();
        for(Iterator<AttributeDescriptor> it = getAttributeDescriptors().iterator(); it.hasNext(); )
        {
            AttributeDescriptor ad = it.next();
            if(ad instanceof ViewAttributeDescriptor && ((ViewAttributeDescriptor)ad).isParam())
            {
                ret.add(ad);
            }
        }
        return ret;
    }


    protected void setColumnAttributeDescriptors(List<?> cols)
    {
        List old = getColumnAttributeDescriptors();
        if(cols != null)
        {
            old.removeAll(cols);
            int i = 0;
            for(Iterator<?> iter = cols.iterator(); iter.hasNext(); )
            {
                ViewAttributeDescriptor a = (ViewAttributeDescriptor)iter.next();
                if(!equals(a.getEnclosingType()))
                {
                    throw new JaloInvalidParameterException("view attribute " + a + " does not belong to this type (" + getCode() + ") but to " + a
                                    .getEnclosingType().getCode(), 0);
                }
                if(a.isParam())
                {
                    a.setParam(false);
                }
                a.setPosition(i++);
            }
        }
        for(Iterator<ViewAttributeDescriptor> it = old.iterator(); it.hasNext(); )
        {
            ViewAttributeDescriptor a = it.next();
            try
            {
                a.remove();
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    public List getColumnAttributeDescriptors()
    {
        List<AttributeDescriptor> ret = new ArrayList();
        for(Iterator<AttributeDescriptor> it = getAttributeDescriptors().iterator(); it.hasNext(); )
        {
            AttributeDescriptor ad = it.next();
            if(ad instanceof ViewAttributeDescriptor && !((ViewAttributeDescriptor)ad).isParam())
            {
                ret.add(ad);
            }
        }
        Collections.sort(ret, VAD_COMP);
        return ret;
    }


    private static final Comparator VAD_COMP = (Comparator)new Object();


    protected List getResultRowSignature(List<?> resultRowAttributes)
    {
        List<ViewAttributeDescriptor> l = new ArrayList(resultRowAttributes);
        for(int i = 0; i < l.size(); i++)
        {
            Type t = ((ViewAttributeDescriptor)l.get(i)).getAttributeType();
            if(t instanceof AtomicType)
            {
                l.set(i, ((AtomicType)t).getJavaClass());
            }
            else if(t instanceof ComposedType)
            {
                l.set(i, ((ComposedType)t).getJaloClass());
            }
            else
            {
                throw new JaloInvalidParameterException("illegal column type " + t, 0);
            }
        }
        return l;
    }


    protected Map createAttributeMapFromRow(List resultAttributes, List rowData)
    {
        Item.ItemAttributeMap map = new Item.ItemAttributeMap();
        int i = 0;
        for(Iterator<ViewAttributeDescriptor> it = resultAttributes.iterator(); it.hasNext(); i++)
        {
            ViewAttributeDescriptor vad = it.next();
            map.put(vad.getQualifier(), rowData.get(i));
        }
        return (Map)map;
    }


    public Map convertGenericValueConditions(GenericCondition cond)
    {
        Item.ItemAttributeMap itemAttributeMap = new Item.ItemAttributeMap();
        if(cond != null)
        {
            if(!(cond instanceof GenericConditionList))
            {
                appendGenericValueCondition(cond, (Map)itemAttributeMap);
            }
            else
            {
                for(Iterator<GenericCondition> it = ((GenericConditionList)cond).getConditionList().iterator(); it.hasNext(); )
                {
                    appendGenericValueCondition(it.next(), (Map)itemAttributeMap);
                }
            }
        }
        return (Map)itemAttributeMap;
    }


    private void appendGenericValueCondition(GenericCondition cond, Map<String, Object> params)
    {
        if(!(cond instanceof GenericValueCondition))
        {
            throw new JaloInvalidParameterException("queries upon view types cannot contain non-value conditions (cond " + cond + " )", 0);
        }
        params.put(((GenericValueCondition)cond).getField().getQualifier(), ((GenericValueCondition)cond).getValue());
    }


    public SearchResult search(Map values, boolean dontNeedTotal, int start, int count)
    {
        return search(JaloSession.getCurrentSession().getSessionContext(), values, dontNeedTotal, start, count);
    }


    public SearchResult search(SessionContext ctx, Map values, boolean dontNeedTotal, int start, int count)
    {
        List resAtt = getColumnAttributeDescriptors();
        SearchResult rs = getSession().getFlexibleSearch().search(ctx, getQuery(), values, getResultRowSignature(resAtt), true, dontNeedTotal, start, count);
        int total = rs.getTotalCount();
        List<Item> items = new ArrayList(rs.getCount());
        for(Iterator it = rs.getResult().iterator(); it.hasNext(); )
        {
            Object o = it.next();
            try
            {
                items.add(newInstance(ctx,
                                createAttributeMapFromRow(resAtt,
                                                (o instanceof List) ? (List)o : Collections.<Object>singletonList(o))));
            }
            catch(JaloGenericCreationException e)
            {
                throw new JaloSystemException(e);
            }
            catch(JaloAbstractTypeException e)
            {
                throw new JaloSystemException(e);
            }
        }
        return (SearchResult)new StandardSearchResult(items, total, start, count, rs.getDataSourceId());
    }


    protected Map getXMLCustomProperties(String forExtension)
    {
        String myExt = getExtensionName();
        if(forExtension == myExt || (forExtension != null && forExtension.equals(myExt)))
        {
            return Collections.singletonMap("query", "\"" + JaloTools.encodeJAVA(getQuery()) + "\"");
        }
        return Collections.EMPTY_MAP;
    }
}
