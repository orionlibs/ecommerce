package de.hybris.platform.variants.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class VariantProduct extends GeneratedVariantProduct
{
    private static final String VARIANT_BASE_REMOVAL_CTX = "variant.base";
    private static final Logger log = Logger.getLogger(VariantProduct.class.getName());


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "VariantProductPrepareInterceptor", portingMethod = "onPrepare(final Object model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("baseProduct", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new VariantProduct", 0);
        }
        if(!(allAttributes.get("baseProduct") instanceof Product))
        {
            throw new JaloInvalidParameterException("base product value " + allAttributes.get("baseProduct") + " is no instance of BaseProduct", 0);
        }
        Product base = (Product)allAttributes.get("baseProduct");
        checkBaseProductVariantType((VariantType)type, base);
        Item item = super.createItem(ctx, type, allAttributes);
        base.setModificationTime(new Date());
        return item;
    }


    protected void checkBaseProductVariantType(VariantType myType, Product base) throws JaloInvalidParameterException
    {
        if(base == null)
        {
            throw new JaloInvalidParameterException("base product cannot be null", 0);
        }
        VariantType baseVariantType = VariantsManager.getInstance().getVariantType(base);
        if(baseVariantType == null)
        {
            throw new JaloInvalidParameterException("Proposed base product " + base + " got no variant type - cannot be base product", 0);
        }
        if(!baseVariantType.isAssignableFrom((Type)myType))
        {
            throw new JaloInvalidParameterException("Base variant type " + baseVariantType + " of base product " + base + " is incompatible to variant type " + myType, 0);
        }
    }


    @SLDSafe(portingClass = "VariantProductRemoveInterceptor", portingMethod = "onRemove(final Object model, final InterceptorContext ctx)")
    protected void doBeforeRemove(SessionContext ctx, Map<String, Product> removalCtx)
    {
        super.doBeforeRemove(ctx, removalCtx);
        removalCtx.put("variant.base", getBaseProduct());
    }


    @SLDSafe(portingClass = "VariantProductRemoveInterceptor", portingMethod = "onRemove(final Object model, final InterceptorContext ctx)")
    protected void doAfterRemove(SessionContext ctx, Map removalCtx)
    {
        super.doAfterRemove(ctx, removalCtx);
        Product base = (Product)removalCtx.get("variant.base");
        if(base != null && !Item.isCurrentlyRemoving((Item)base))
        {
            base.setModificationTime(new Date());
        }
    }


    @SLDSafe(portingClass = "VariantProductPrepareInterceptor", portingMethod = "onPrepare(final Object model, final InterceptorContext ctx)")
    public void setBaseProduct(SessionContext ctx, Product param)
    {
        Product prev = getBaseProduct();
        if(prev == null || !prev.equals(param))
        {
            checkBaseProductVariantType((VariantType)getComposedType(), param);
            super.setBaseProduct(ctx, param);
            Date d = new Date();
            if(prev != null)
            {
                prev.setModificationTime(d);
            }
            param.setModificationTime(d);
        }
    }
}
