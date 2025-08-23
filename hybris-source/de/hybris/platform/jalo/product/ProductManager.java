package de.hybris.platform.jalo.product;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

@Deprecated(since = "ages", forRemoval = false)
public class ProductManager extends Manager
{
    @Deprecated(since = "ages", forRemoval = false)
    public static final String BEAN_NAME = "core.productManager";


    @Deprecated(since = "ages", forRemoval = false)
    public static ProductManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getProductManager();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getProductTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Product.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Product type code not found", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getUnitTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Unit.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Unit type code not found", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Product getProductByPK(PK pk) throws JaloItemNotFoundException
    {
        return (Product)getSession().getItem(pk);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Product createProduct(SessionContext ctx, String code)
    {
        return createProduct(ctx, null, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Product createProduct(SessionContext ctx, PK pk, String code)
    {
        return createProduct(ctx, pk, code, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Product createProduct(SessionContext ctx, String code, ComposedType type)
    {
        return createProduct(ctx, null, code, type);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Product createProduct(SessionContext ctx, PK pkBase, String code, ComposedType type)
    {
        if(type != null && !Product.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("type " + type + " is not compatbile with jalo class " + Product.class
                            .getName(), 0);
        }
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put(Item.PK, pkBase);
            params.put("code", code);
            if(type == null)
            {
                type = TypeManager.getInstance().getComposedType(Product.class);
            }
            return (Product)type.newInstance(ctx, (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Product> getAllProducts()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                        getProductTypeCode() + "}", null, Collections.singletonList(Product.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getProductsByCode(String filter)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("code", filter);
        String query = "SELECT {" + Item.PK + "} FROM {" + getProductTypeCode() + "} WHERE {code} ";
        query = query + query;
        SearchResult res = getSession().getFlexibleSearch().search(query, values, Collections.singletonList(Product.class), false, false, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public int getQuantityOfProducts()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT count(*) FROM {" + getProductTypeCode() + "}", null,
                        Collections.singletonList(Integer.class), false, false, 0, -1);
        return ((Integer)res.getResult().get(0)).intValue();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Product createProduct(String code)
    {
        return createProduct(getSession().getSessionContext(), code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Unit getUnit(PK pk) throws JaloItemNotFoundException
    {
        return (Unit)getSession().getItem(pk);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection searchUnits(String code, String type)
    {
        StringBuilder query = (new StringBuilder("SELECT {")).append(Item.PK).append("} FROM {").append(getUnitTypeCode()).append("} WHERE 1=1");
        Map<Object, Object> values = new HashMap<>();
        if(code != null)
        {
            values.put("code", code);
            query.append(" AND {").append("code").append("} = ?").append("code");
        }
        if(type != null)
        {
            values.put("unitType", type);
            query.append(" AND {").append("unitType").append("} = ?").append("unitType");
        }
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(), values,
                        Collections.singletonList(Unit.class), false, false, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Unit createUnit(String type, String code)
    {
        return createUnit(null, type, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Unit createUnit(PK pkBase, String type, String code)
    {
        if(type == null || "".equals(type.trim()))
        {
            throw new JaloInvalidParameterException("type be null or empty", 0);
        }
        if(code == null || "".equals(code.trim()))
        {
            throw new JaloInvalidParameterException("code cannot be null or empty", 0);
        }
        try
        {
            return (Unit)ComposedType.newInstance(getSession().getSessionContext(), Unit.class, new Object[] {Item.PK, pkBase, "code", code, "unitType", type});
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllUnits()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                        getUnitTypeCode() + "}", null, Collections.singletonList(Unit.class), false, false, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Unit> getUnitsByUnitType(String type)
    {
        return searchUnits(null, type);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Unit getUnit(String code)
    {
        Collection<Unit> units = getUnitsByCode(code);
        if(units.isEmpty())
        {
            return null;
        }
        if(units.size() > 1)
        {
            throw new JaloInvalidParameterException("unit code '" + code + "' is not uniqe", 0);
        }
        return units.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Unit> getUnitsByCode(String code)
    {
        return searchUnits(code, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Unit getUnit(String type, String code)
    {
        Collection<Unit> units = getUnitsByTypeAndCode(type, code);
        if(units.isEmpty())
        {
            return null;
        }
        if(units.size() > 1)
        {
            throw new JaloInvalidParameterException("unit type '" + type + "' and code '" + code + "' is not uniqe", 0);
        }
        return units.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Unit> getUnitsByTypeAndCode(String type, String code)
    {
        return searchUnits(code, type);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllUnitTypes()
    {
        Collection<String> types = new HashSet();
        Collection allUnits = getAllUnits();
        for(Iterator<Unit> it = allUnits.iterator(); it.hasNext(); )
        {
            types.add(((Unit)it.next()).getUnitType());
        }
        return types;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllProductsChangedAfter(Date date)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("date", date);
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getProductTypeCode() + "}  WHERE {" + Item.MODIFIED_TIME + "} >= ?date OR {" + Item.CREATION_TIME + "} > ?date", values,
                        Collections.singletonList(Product.class), false, false, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Object writeReplace() throws ObjectStreamException
    {
        return new ProductManagerSerializableDTO(getTenant());
    }
}
