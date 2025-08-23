package de.hybris.platform.variants.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class VariantsManager
{
    private static final Logger log = Logger.getLogger(VariantsManager.class);


    @Deprecated(since = "ages", forRemoval = false)
    public void beforeItemCreation(SessionContext ctx, ComposedType type, Item.ItemAttributeMap attributes) throws JaloBusinessException
    {
        Class<?> c = type.getJaloClass();
        if(Product.class.isAssignableFrom(c))
        {
            attributes.setAttributeMode(GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE, Item.AttributeMode.INITIAL);
            if(VariantProduct.class.isAssignableFrom(c))
            {
                attributes.setAttributeMode("baseProduct", Item.AttributeMode.INITIAL);
            }
        }
    }


    public static VariantsManager getInstance()
    {
        return CatalogManager.getInstance().getVariantsManager();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public VariantType createVariantType(String code)
    {
        try
        {
            ComposedType metaType = getSession().getTypeManager().getComposedType(VariantType.class);
            Item.ItemAttributeMap parameters = new Item.ItemAttributeMap();
            parameters.put(VariantType.CODE, code);
            return (VariantType)metaType.newInstance((Map)parameters);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating new variant type '" + code + "'", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map<AttributeDescriptor, Object> getProductAttributes(Product product, Collection ignoreAttributes)
    {
        TreeMap<AttributeDescriptor, Object> productAttributes = new TreeMap<>();
        for(Iterator<AttributeDescriptor> it = product.getComposedType().getAttributeDescriptors().iterator(); it.hasNext(); )
        {
            AttributeDescriptor ad = it.next();
            String key = ad.getQualifier();
            if(ignoreAttributes != null && !ignoreAttributes.contains(key))
            {
                Object value = null;
                try
                {
                    value = product.getAttribute(key);
                }
                catch(JaloSecurityException e)
                {
                    throw new JaloSystemException(e);
                }
                productAttributes.put(ad, value);
            }
        }
        return productAttributes;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAssignedAttributeValues(SessionContext ctx, VariantType vt, String qualifier)
    {
        AttributeDescriptor ad = vt.getAttributeDescriptor(qualifier);
        Class<?> resultclass = null;
        resultclass = getAttributeValueClass(vt, qualifier);
        Collection result = getSession().getFlexibleSearch().search(ctx, "SELECT DISTINCT " + ad.getDatabaseColumn() + " FROM {Product} WHERE " + ad.getDatabaseColumn() + " IS NOT NULL ", null, Collections.singletonList(resultclass), true, true, 0, -1).getResult();
        return result;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map<String, Collection> getAssignedVariantAttributes(Product baseProduct) throws JaloInvalidParameterException, JaloSecurityException
    {
        VariantsManager vm = getInstance();
        VariantType vt = vm.getVariantType(baseProduct);
        Map<String, Collection> result = new HashMap<>();
        Collection<VariantProduct> c = vm.getVariants(JaloSession.getCurrentSession().getSessionContext(), baseProduct);
        Collection attributes = vt.getVariantAttributes();
        for(Iterator<VariantProduct> i = c.iterator(); i.hasNext(); )
        {
            VariantProduct variant = i.next();
            for(Iterator<VariantAttributeDescriptor> ia = attributes.iterator(); ia.hasNext(); )
            {
                VariantAttributeDescriptor vad = ia.next();
                Collection<Object> values = result.get(vad.getQualifier());
                if(values == null)
                {
                    values = new LinkedList();
                    result.put(vad.getQualifier(), values);
                }
                Object value = variant.getAttribute(vad.getQualifier());
                if(!values.contains(value))
                {
                    values.add(value);
                }
            }
        }
        return result;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Class getAttributeValueClass(VariantType vt, String qualifier)
    {
        AttributeDescriptor ad = vt.getAttributeDescriptor(qualifier);
        Object resultobj = ad.getAttributeType();
        Class<?> resultclass = null;
        if(resultobj instanceof AtomicType)
        {
            resultclass = ((AtomicType)resultobj).getJavaClass();
        }
        else
        {
            resultclass = resultobj.getClass();
        }
        return resultclass;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<VariantType> getVariantTypesByAttributes(Map<String, Type> attributeMap, boolean matchExact)
    {
        TypeManager tm = getSession().getTypeManager();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT {").append(Item.PK).append("} FROM {").append(GeneratedCatalogConstants.TC.VARIANTTYPE).append(" AS vt} ");
        if(!attributeMap.isEmpty())
        {
            String ATT = tm.getComposedType(GeneratedCatalogConstants.TC.VARIANTATTRIBUTEDESCRIPTOR).getCode();
            sb.append(" WHERE ");
            for(Iterator<Map.Entry<String, Type>> iterator = attributeMap.entrySet().iterator(); iterator.hasNext(); )
            {
                Map.Entry<String, Type> e = iterator.next();
                String q = e.getKey();
                sb.append("EXISTS ({{ SELECT * FROM {").append(ATT).append("} WHERE ");
                sb.append("{").append(AttributeDescriptor.ENCLOSING_TYPE).append("}={vt:").append(Item.PK).append("} AND ");
                sb.append("{").append("qualifier").append("}='").append(q).append("' }}) ");
                if(iterator.hasNext())
                {
                    sb.append(" AND ");
                }
            }
        }
        Collection<VariantType> toFilter = new ArrayList<>(getSession().getFlexibleSearch().search(sb.toString(), Collections.EMPTY_MAP, Collections.singletonList(VariantType.class), true, true, 0, -1).getResult());
        Iterator<VariantType> it;
        for(it = toFilter.iterator(); it.hasNext(); )
        {
            VariantType vt = it.next();
            for(Iterator<Map.Entry<String, Type>> it2 = attributeMap.entrySet().iterator(); it2.hasNext(); )
            {
                Map.Entry<String, Type> e = it2.next();
                String q = e.getKey();
                Type t = e.getValue();
                AttributeDescriptor ad = vt.getAttributeDescriptor(q);
                if(!ad.getAttributeType().isAssignableFrom(t))
                {
                    if(log.isInfoEnabled())
                    {
                        log.info("removing vt " + vt
                                        .getCode() + " due to incompatibility of '" + q + "' : " + t.getCode() + " != " + ad
                                        .getAttributeType().getCode());
                    }
                    it.remove();
                }
            }
        }
        if(matchExact)
        {
            for(it = toFilter.iterator(); it.hasNext(); )
            {
                VariantType vt = it.next();
                if(vt.getVariantAttributes().size() == attributeMap.size())
                {
                    for(Iterator<Map.Entry<String, Type>> it2 = attributeMap.entrySet().iterator(); it2.hasNext(); )
                    {
                        Map.Entry<String, Type> e = it2.next();
                        String key = e.getKey();
                        if(vt.getAttributeDescriptor(key) == null)
                        {
                            it.remove();
                        }
                    }
                    continue;
                }
                it.remove();
            }
        }
        return toFilter;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getVariantProductByAttributeValues(Map<AttributeDescriptor, Object> attributeValuesMap)
    {
        return getVariantProductByAttributeValues(attributeValuesMap, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<VariantProduct> getVariantProductByAttributeValues(Map<AttributeDescriptor, Object> attributeValuesMap, String variantTypeCode)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT {").append(Item.PK).append("} FROM {").append(GeneratedCatalogConstants.TC.VARIANTPRODUCT).append("} ");
        if(!attributeValuesMap.isEmpty())
        {
            sb.append(" WHERE ");
            Iterator<Map.Entry<AttributeDescriptor, Object>> it = attributeValuesMap.entrySet().iterator();
            while(it.hasNext())
            {
                Map.Entry<AttributeDescriptor, Object> e = it.next();
                AttributeDescriptor q = e.getKey();
                sb.append(q.getDatabaseColumn()).append(" = '").append(e.getValue()).append("' ");
                if(it.hasNext())
                {
                    sb.append(" AND ");
                }
            }
        }
        sb.append("ORDER BY {code} ASC");
        Collection<VariantProduct> toFilter = new ArrayList<>(getSession().getFlexibleSearch().search(sb.toString(), Collections.EMPTY_MAP, Collections.singletonList(VariantProduct.class), true, true, 0, -1).getResult());
        if(variantTypeCode != null)
        {
            for(Iterator<VariantProduct> it = toFilter.iterator(); it.hasNext(); )
            {
                VariantProduct vp = it.next();
                if(!getVariantType(vp.getBaseProduct()).getCode().equals(variantTypeCode))
                {
                    it.remove();
                }
            }
        }
        return toFilter;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<VariantProduct> getVariantProductByAttributeValues(Product base, Map values)
    {
        Collection<VariantProduct> ret = new ArrayList<>();
        for(Iterator<VariantProduct> iter = getVariants(base).iterator(); iter.hasNext(); )
        {
            VariantProduct vp = iter.next();
            boolean add = true;
            for(Iterator<Map.Entry> iterator = values.entrySet().iterator(); iterator.hasNext(); )
            {
                Map.Entry e = iterator.next();
                Object attrKey = e.getKey();
                String attr = (attrKey instanceof AttributeDescriptor) ? ((AttributeDescriptor)attrKey).getQualifier() : (String)attrKey;
                Object requestedValue = e.getValue();
                Object actualValue = null;
                try
                {
                    actualValue = vp.getAttribute(attr);
                }
                catch(JaloInvalidParameterException jaloInvalidParameterException)
                {
                }
                catch(JaloSecurityException jaloSecurityException)
                {
                }
                if(requestedValue != actualValue && (requestedValue == null || !requestedValue.equals(actualValue)))
                {
                    add = false;
                }
            }
            if(add)
            {
                ret.add(vp);
            }
        }
        return ret;
    }


    public boolean isCreatorDisabled()
    {
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void createEssentialData(Map params, JspContext jspc) throws Exception
    {
        if(getCatalogManager().getFirstItemByAttribute(SavedQuery.class, "code", "getAllVariantBases") == null)
        {
            createBaseProductsSearch();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SavedQuery createBaseProductsSearch()
    {
        try
        {
            TypeManager tm = TypeManager.getInstance();
            HashMap<Object, Object> initalValues = new HashMap<>();
            initalValues.put("code", "getAllVariantBases");
            initalValues.put("name", Localization.getLocalizedMap("getallvariantbases"));
            initalValues.put("query", "SELECT {" + Item.PK + "} FROM {" + tm.getComposedType(Product.class).getCode() + "}\nWHERE {" + GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE + "} IS NOT NULL \nORDER BY {code} ASC");
            initalValues.put("resultType", tm.getComposedType(Product.class));
            SessionContext ctx = getSession().createSessionContext();
            ctx.setLanguage(null);
            return (SavedQuery)tm.getComposedType(SavedQuery.class).newInstance(ctx, initalValues);
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


    @Deprecated(since = "ages", forRemoval = false)
    public Product createBaseProduct(String code, String variantTypeCode)
    {
        Product baseProduct = getSession().getProductManager().createProduct(code);
        setVariantType(baseProduct, (VariantType)getSession().getTypeManager().getComposedType(variantTypeCode));
        return baseProduct;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setVariants(SessionContext ctx, Product baseProduct, Collection variants)
    {
        getCatalogManager().setVariants(ctx, baseProduct, variants);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Product> getAllBaseProducts(SessionContext ctx)
    {
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {Product} WHERE {" + GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE + "} IS NOT NULL ORDER BY {code} ASC", null,
                                        Collections.singletonList(Product.class), true, true, 0, -1).getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isBaseProduct(Product product)
    {
        try
        {
            return (product.getAttribute(GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE) != null);
        }
        catch(JaloSecurityException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<VariantProduct> getVariants(SessionContext ctx, Product baseProduct)
    {
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.VARIANTPRODUCT + "} WHERE {baseProduct} = ?base ORDER BY {code} ASC",
                                        Collections.singletonMap("base", baseProduct), Collections.singletonList(VariantProduct.class), true, true, 0, -1)
                        .getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeVariants(Collection<VariantProduct> variants, Product baseProduct)
    {
        removeVariants(getSession().getSessionContext(), variants, baseProduct);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeVariants(SessionContext ctx, Collection<VariantProduct> variants, Product baseProduct)
    {
        Collection<VariantProduct> curVariants = new ArrayList<>(getVariants(ctx, baseProduct));
        curVariants.removeAll(variants);
        setVariants(baseProduct, curVariants);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setVariantType(SessionContext ctx, Product item, VariantType param)
    {
        VariantType vt = getVariantType(ctx, item);
        if(vt != null)
        {
            if(vt.equals(param))
            {
                return;
            }
            if(getVariants(item).size() > 0)
            {
                throw new JaloInvalidParameterException("product " + item.getCode() + " already owns a variant type and variants - cannot change variant type to " + (
                                (param != null) ? param.getCode() : "null") + "!", 0);
            }
        }
        if(param != null && param.isAbstract())
        {
            throw new JaloInvalidParameterException("cannot use variant type " + param.getCode() + " since it is abstract!", 0);
        }
        getCatalogManager().setVariantType(ctx, item, param);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setVariantType(Product item, VariantType param)
    {
        setVariantType(getSession().getSessionContext(), item, param);
    }


    private JaloSession getSession()
    {
        return getCatalogManager().getSession();
    }


    private CatalogManager getCatalogManager()
    {
        return CatalogManager.getInstance();
    }


    public VariantAttributeDescriptor createVariantAttributeDescriptor(SessionContext ctx, Map attributeValues)
    {
        return getCatalogManager().createVariantAttributeDescriptor(ctx, attributeValues);
    }


    public VariantAttributeDescriptor createVariantAttributeDescriptor(Map attributeValues)
    {
        return getCatalogManager().createVariantAttributeDescriptor(attributeValues);
    }


    public VariantType createVariantType(SessionContext ctx, Map attributeValues)
    {
        return getCatalogManager().createVariantType(ctx, attributeValues);
    }


    public VariantType createVariantType(Map attributeValues)
    {
        return getCatalogManager().createVariantType(attributeValues);
    }


    public String getName()
    {
        return getCatalogManager().getName();
    }


    public Collection<VariantProduct> getVariants(Product item)
    {
        return getCatalogManager().getVariants(item);
    }


    public void setVariants(Product item, Collection<VariantProduct> value)
    {
        getCatalogManager().setVariants(item, value);
    }


    public VariantType getVariantType(SessionContext ctx, Product item)
    {
        return getCatalogManager().getVariantType(ctx, item);
    }


    public VariantType getVariantType(Product item)
    {
        return getCatalogManager().getVariantType(item);
    }
}
