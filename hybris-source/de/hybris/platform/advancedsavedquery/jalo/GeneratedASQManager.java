package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.advancedsavedquery.constants.GeneratedASQConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedASQManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public AdvancedSavedQuery createAdvancedSavedQuery(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedASQConstants.TC.ADVANCEDSAVEDQUERY);
            return (AdvancedSavedQuery)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AdvancedSavedQuery : " + e.getMessage(), 0);
        }
    }


    public AdvancedSavedQuery createAdvancedSavedQuery(Map attributeValues)
    {
        return createAdvancedSavedQuery(getSession().getSessionContext(), attributeValues);
    }


    public ComposedTypeAdvancedSavedQuerySearchParameter createComposedTypeAdvancedSavedQuerySearchParameter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedASQConstants.TC.COMPOSEDTYPEADVANCEDSAVEDQUERYSEARCHPARAMETER);
            return (ComposedTypeAdvancedSavedQuerySearchParameter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ComposedTypeAdvancedSavedQuerySearchParameter : " + e.getMessage(), 0);
        }
    }


    public ComposedTypeAdvancedSavedQuerySearchParameter createComposedTypeAdvancedSavedQuerySearchParameter(Map attributeValues)
    {
        return createComposedTypeAdvancedSavedQuerySearchParameter(getSession().getSessionContext(), attributeValues);
    }


    public SimpleAdvancedSavedQuerySearchParameter createSimpleAdvancedSavedQuerySearchParameter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedASQConstants.TC.SIMPLEADVANCEDSAVEDQUERYSEARCHPARAMETER);
            return (SimpleAdvancedSavedQuerySearchParameter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SimpleAdvancedSavedQuerySearchParameter : " + e.getMessage(), 0);
        }
    }


    public SimpleAdvancedSavedQuerySearchParameter createSimpleAdvancedSavedQuerySearchParameter(Map attributeValues)
    {
        return createSimpleAdvancedSavedQuerySearchParameter(getSession().getSessionContext(), attributeValues);
    }


    public TypedAdvancedSavedQuerySearchParameter createTypedAdvancedSavedQuerySearchParameter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedASQConstants.TC.TYPEDADVANCEDSAVEDQUERYSEARCHPARAMETER);
            return (TypedAdvancedSavedQuerySearchParameter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TypedAdvancedSavedQuerySearchParameter : " + e.getMessage(), 0);
        }
    }


    public TypedAdvancedSavedQuerySearchParameter createTypedAdvancedSavedQuerySearchParameter(Map attributeValues)
    {
        return createTypedAdvancedSavedQuerySearchParameter(getSession().getSessionContext(), attributeValues);
    }


    public WherePart createWherePart(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedASQConstants.TC.WHEREPART);
            return (WherePart)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WherePart : " + e.getMessage(), 0);
        }
    }


    public WherePart createWherePart(Map attributeValues)
    {
        return createWherePart(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "advancedsavedquery";
    }
}
