package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class AbstractPromotionRestriction extends GeneratedAbstractPromotionRestriction
{
    private static Logger log = Logger.getLogger(AbstractPromotionRestriction.class.getName());


    public abstract RestrictionResult evaluate(SessionContext paramSessionContext, Collection<Product> paramCollection, Date paramDate, AbstractOrder paramAbstractOrder);


    public final RestrictionResult evaluate(SessionContext ctx, Product product, Date date, AbstractOrder order)
    {
        ArrayList<Product> products = new ArrayList<>(1);
        if(product != null)
        {
            products.add(product);
        }
        return evaluate(ctx, products, date, order);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getClass().getSimpleName() + " '" + getClass().getSimpleName() + "' (" + getRenderedDescription() + ")";
    }


    public String getRestrictionType(SessionContext ctx)
    {
        return getComposedType().getName(ctx);
    }


    public Map getAllRestrictionType(SessionContext ctx)
    {
        Map<Object, Object> result = new HashMap<>();
        SessionContext localContext = getSession().createSessionContext();
        for(Language language : getSession().getC2LManager().getAllLanguages())
        {
            localContext.setLanguage(language);
            String itemTypeName = getRestrictionType(localContext);
            if(itemTypeName != null)
            {
                result.put(language, itemTypeName);
            }
        }
        return result;
    }


    public String getRenderedDescription(SessionContext ctx)
    {
        String pattern = null;
        if(ctx.getLanguage() == null)
        {
            getDescriptionPattern();
        }
        else
        {
            pattern = getDescriptionPattern(ctx);
        }
        if(pattern != null)
        {
            return MessageFormat.format(pattern, getDescriptionPatternArguments(ctx));
        }
        return null;
    }


    protected Object[] getDescriptionPatternArguments(SessionContext ctx)
    {
        return new Object[] {getRestrictionType(ctx)};
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        builder.append(getClass().getSimpleName()).append('|');
    }


    protected AbstractPromotionRestriction deepClone(SessionContext ctx)
    {
        try
        {
            Map values = getAllAttributes(ctx);
            values.remove(Item.PK);
            values.remove(Item.MODIFIED_TIME);
            values.remove(Item.CREATION_TIME);
            values.remove("savedvalues");
            values.remove("customLinkQualifier");
            values.remove("synchronizedCopies");
            values.remove("synchronizationSources");
            values.remove("alldocuments");
            values.remove(Item.TYPE);
            values.remove(Item.OWNER);
            values.remove("promotion");
            deepCloneAttributes(ctx, values);
            ComposedType type = TypeManager.getInstance().getComposedType(getClass());
            try
            {
                return (AbstractPromotionRestriction)type.newInstance(ctx, values);
            }
            catch(JaloGenericCreationException | de.hybris.platform.jalo.type.JaloAbstractTypeException ex)
            {
                log.warn("deepClone [" + this + "] failed to create instance of " + getClass().getSimpleName(), (Throwable)ex);
            }
        }
        catch(JaloSecurityException ex)
        {
            log.warn("deepClone [" + this + "] failed to getAllAttributes", (Throwable)ex);
        }
        return null;
    }


    protected void deepCloneAttributes(SessionContext ctx, Map values)
    {
        values.remove("restrictionType");
        values.remove("renderedDescription");
    }
}
