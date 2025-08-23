package de.hybris.platform.catalog.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class ProductReference extends GeneratedProductReference
{
    private static final Logger LOG = Logger.getLogger(ProductReference.class.getName());


    @SLDSafe(portingClass = "MandatoryAttributesValidator", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("source", allAttributes, missing) ? 1 : 0) | (!checkMandatoryAttribute("target", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " to create a new " + type.getCode(), 0);
        }
        allAttributes.setAttributeMode("source", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("target", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("referenceType", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("quantity", Item.AttributeMode.INITIAL);
        ProductReference ret = (ProductReference)super.createItem(ctx, type, allAttributes);
        return (Item)ret;
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "qualifer >" + getQualifier() + "< src >" + ((getSource() != null) ? getSource().getCode() : "NULL") + "< target >" + (
                        (getTarget() != null) ? getTarget().getCode() : "NULL") + "< (PK=" + getPK() + ")";
    }


    protected void markSourceProductModified()
    {
        Product product = getSource();
        if(product != null)
        {
            product.setModificationTime(new Date());
        }
    }


    @SLDSafe(portingClass = "ProductReferenceRemoveInterceptor", portingMethod = "onRemove(final ProductReferenceModel productReferenceModel, final InterceptorContext ctx)")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        try
        {
            markSourceProductModified();
        }
        catch(Exception e)
        {
            LOG.warn("could not mark product modified due to " + e.getMessage() + " - ignored");
        }
        super.remove(ctx);
    }


    @SLDSafe(portingClass = "ProductReferencePrepareInterceptor", portingMethod = "onPrepare(final ProductReferenceModel productReferenceModel, final InterceptorContext ctx)")
    public Object setProperty(SessionContext ctx, String name, Object value)
    {
        Object prev = super.setProperty(ctx, name, value);
        if(prev != value && (prev == null || !prev.equals(value)))
        {
            markSourceProductModified();
            if(prev instanceof Product && name.equals("source"))
            {
                ((Product)prev).setModificationTime(new Date());
            }
        }
        return prev;
    }
}
