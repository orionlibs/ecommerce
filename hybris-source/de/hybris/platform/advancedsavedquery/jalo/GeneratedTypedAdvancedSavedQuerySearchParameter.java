package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedTypedAdvancedSavedQuerySearchParameter extends AbstractAdvancedSavedQuerySearchParameter
{
    public static final String TYPEDSEARCHPARAMETER = "typedSearchParameter";
    public static final String TYPEATTRIBUTES = "typeAttributes";
    public static final String ENCLOSINGTYPE = "enclosingType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAdvancedSavedQuerySearchParameter.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("typedSearchParameter", Item.AttributeMode.INITIAL);
        tmp.put("enclosingType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ComposedType getEnclosingType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "enclosingType");
    }


    public ComposedType getEnclosingType()
    {
        return getEnclosingType(getSession().getSessionContext());
    }


    public void setEnclosingType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "enclosingType", value);
    }


    public void setEnclosingType(ComposedType value)
    {
        setEnclosingType(getSession().getSessionContext(), value);
    }


    public Collection<AttributeDescriptor> getTypeAttributes()
    {
        return getTypeAttributes(getSession().getSessionContext());
    }


    public AttributeDescriptor getTypedSearchParameter(SessionContext ctx)
    {
        return (AttributeDescriptor)getProperty(ctx, "typedSearchParameter");
    }


    public AttributeDescriptor getTypedSearchParameter()
    {
        return getTypedSearchParameter(getSession().getSessionContext());
    }


    public void setTypedSearchParameter(SessionContext ctx, AttributeDescriptor value)
    {
        setProperty(ctx, "typedSearchParameter", value);
    }


    public void setTypedSearchParameter(AttributeDescriptor value)
    {
        setTypedSearchParameter(getSession().getSessionContext(), value);
    }


    public abstract Collection<AttributeDescriptor> getTypeAttributes(SessionContext paramSessionContext);
}
