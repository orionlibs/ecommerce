package de.hybris.platform.variants.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Deprecated(since = "ages", forRemoval = false)
public class VariantType extends GeneratedVariantType
{
    private static final String LOC_REM_TYPE_IN_USE = "exception.varianttype.remove.remtypeinuse";


    @Deprecated(since = "ages", forRemoval = false)
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        JaloSession session = JaloSession.getCurrentSession();
        TypeManager typeManager = session.getTypeManager();
        ComposedType variantProductType = typeManager.getComposedType(VariantProduct.class);
        ComposedType superType = (ComposedType)allAttributes.get(SUPERTYPE);
        if(superType == null)
        {
            superType = variantProductType;
        }
        else if(!variantProductType.isAssignableFrom((Type)superType))
        {
            throw new JaloBusinessException("Incorrect supertype " + superType + " (has to be " + variantProductType + " or any of its subtypes)", 0);
        }
        Item.ItemAttributeMap attr = new Item.ItemAttributeMap((Map)allAttributes);
        attr.put(SUPERTYPE, superType);
        return super.createItem(ctx, type, attr);
    }


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        if(getBaseProductCount() > 0)
        {
            throw new ConsistencyCheckException(Localization.getLocalizedString("exception.varianttype.remove.remtypeinuse", new String[] {getCode()}), 0);
        }
        super.remove(ctx);
    }


    protected void removePartOfItems(SessionContext ctx, Map<String, Object> emptyValuesMap) throws ConsistencyCheckException
    {
        if(emptyValuesMap != null)
        {
            emptyValuesMap.remove("variantAttributes");
            emptyValuesMap.remove(DECLAREDATTRIBUTEDESCRIPTORS);
        }
        super.removePartOfItems(ctx, emptyValuesMap);
    }


    public boolean isAbstract()
    {
        return "VariantProduct".equalsIgnoreCase(getCode()) ? true : super.isAbstract();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getValidProductBaseTypes(SessionContext ctx)
    {
        ComposedType pt = TypeManager.getInstance().getComposedType(Product.class);
        Set<ComposedType> l = new HashSet();
        l.addAll(pt.getAllSubTypes());
        l.add(pt);
        ComposedType vpt = TypeManager.getInstance().getComposedType(VariantProduct.class);
        l.removeAll(vpt.getAllSubTypes());
        l.remove(vpt);
        return l;
    }


    public Collection<Product> getBaseProducts()
    {
        return getBaseProducts(0, -1);
    }


    public Collection<Product> getBaseProducts(int start, int count)
    {
        return getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" +
                                                        getSession().getTypeManager().getComposedType(Product.class).getCode() + "} WHERE {" + GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE + "}=?me ORDER BY {code} ASC",
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(Product.class), true, true, start, count)
                        .getResult();
    }


    public int getBaseProductCount()
    {
        return ((Integer)getSession()
                        .getFlexibleSearch()
                        .search("SELECT count({" + Item.PK + "}) FROM {" +
                                                        getSession().getTypeManager().getComposedType(Product.class).getCode() + "} WHERE {" + GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE + "}=?me",
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator().next()).intValue();
    }


    public VariantAttributeDescriptor createVariantAttributeDescriptor(String qualifier, Type type, int modifiers) throws JaloDuplicateQualifierException
    {
        AttributeDescriptor ad = createAttributeDescriptor(qualifier, type, modifiers);
        return (VariantAttributeDescriptor)ad.setComposedType(getSession().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.VARIANTATTRIBUTEDESCRIPTOR));
    }


    private static final Comparator COMP = (Comparator)new Object();


    @Deprecated(since = "ages", forRemoval = false)
    public List<VariantAttributeDescriptor> getVariantAttributes(SessionContext ctx)
    {
        List<?> ret = new ArrayList(getAttributeDescriptors());
        for(Iterator<AttributeDescriptor> it = ret.iterator(); it.hasNext(); )
        {
            AttributeDescriptor ad = it.next();
            if(!(ad instanceof VariantAttributeDescriptor))
            {
                it.remove();
            }
        }
        Collections.sort(ret, COMP);
        return (List)ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setVariantAttributes(SessionContext ctx, List<VariantAttributeDescriptor> newOnes)
    {
        Set<VariantAttributeDescriptor> toRemove = new HashSet<>(getVariantAttributes(ctx));
        if(newOnes != null && !newOnes.isEmpty())
        {
            toRemove.removeAll(newOnes);
        }
        try
        {
            for(VariantAttributeDescriptor ad : toRemove)
            {
                ad.remove(ctx);
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
        if(newOnes != null)
        {
            ComposedType metaType = getSession().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.VARIANTATTRIBUTEDESCRIPTOR);
            int i = 0;
            for(Iterator<VariantAttributeDescriptor> it = newOnes.iterator(); it.hasNext(); )
            {
                VariantAttributeDescriptor variantAttributeDescriptor;
                AttributeDescriptor ad = (AttributeDescriptor)it.next();
                if(!equals(ad.getEnclosingType()))
                {
                    throw new JaloInvalidParameterException("attribute descriptor " + ad + " has different enclosing type (expected " +
                                    getCode() + " but got " + ad.getEnclosingType().getCode() + ")", 0);
                }
                if(!(ad instanceof VariantAttributeDescriptor))
                {
                    variantAttributeDescriptor = (VariantAttributeDescriptor)ad.setComposedType(metaType);
                }
                variantAttributeDescriptor.setPosition(i++);
            }
        }
    }


    public Set getDeclaredAttributeDescriptors()
    {
        return super.getDeclaredAttributeDescriptors();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDeclaredAttributeDescriptors(Set fds) throws JaloInvalidParameterException
    {
        super.setDeclaredAttributeDescriptors(fds);
    }
}
