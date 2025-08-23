package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.core.Operator;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import java.util.Collection;
import java.util.Collections;
import org.apache.log4j.Logger;

public class TypedAdvancedSavedQuerySearchParameter extends GeneratedTypedAdvancedSavedQuerySearchParameter
{
    private static final Logger LOG = Logger.getLogger(TypedAdvancedSavedQuerySearchParameter.class.getName());


    @ForceJALO(reason = "abstract method implementation")
    public Collection getTypeAttributes(SessionContext ctx)
    {
        AttributeDescriptor attributeDiscriptor = getTypedSearchParameter(ctx);
        if(attributeDiscriptor != null)
        {
            return attributeDiscriptor.getEnclosingType().getAttributeDescriptors();
        }
        return Collections.EMPTY_LIST;
    }


    public String toFlexibleSearchForm(SessionContext ctx, Operator operator)
    {
        AttributeDescriptor attributeDiscriptor = getTypedSearchParameter(ctx);
        String joinAlias = (getJoinAlias(ctx) != null) ? (getJoinAlias(ctx) + ".") : "";
        String qualifier = joinAlias + joinAlias;
        return getFlexibleSearchPartFor(qualifier, operator);
    }


    @ForceJALO(reason = "something else")
    public void setSearchParameterName(SessionContext ctx, String value)
    {
        throw new JaloSystemException("not applicable to this type.");
    }


    @ForceJALO(reason = "something else")
    public String getSearchParameterName(SessionContext ctx)
    {
        return getTypedSearchParameter(ctx).getEnclosingType().getCode() + "." + getTypedSearchParameter(ctx).getEnclosingType().getCode();
    }


    @ForceJALO(reason = "something else")
    public Type getValueType(SessionContext ctx)
    {
        AttributeDescriptor attributeDiscriptor = getTypedSearchParameter(ctx);
        if(attributeDiscriptor != null)
        {
            return attributeDiscriptor.getAttributeType(ctx);
        }
        return null;
    }


    @ForceJALO(reason = "something else")
    protected boolean isStringValueType()
    {
        AttributeDescriptor attributeDiscriptor = getTypedSearchParameter();
        if(attributeDiscriptor != null && attributeDiscriptor.isLocalized())
        {
            Type type = ((MapType)attributeDiscriptor.getRealAttributeType()).getReturnType();
            return (type instanceof AtomicType && String.class.isAssignableFrom(((AtomicType)type).getJavaClass()));
        }
        return super.isStringValueType();
    }
}
