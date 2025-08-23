package com.hybris.hybrisdatasupplierbackoffice.jalo;

import com.hybris.hybrisdatasupplierbackoffice.constants.GeneratedHybrisdatasupplierbackofficeConstants;
import de.hybris.datasupplier.jalo.GenericDataSupplierGenerationWizard;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedHybrisdatasupplierbackofficeManager extends Extension
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


    public GenericDataSupplierGenerationWizard createGenericDataSupplierGenerationWizard(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedHybrisdatasupplierbackofficeConstants.TC.GENERICDATASUPPLIERGENERATIONWIZARD);
            return (GenericDataSupplierGenerationWizard)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating GenericDataSupplierGenerationWizard : " + e.getMessage(), 0);
        }
    }


    public GenericDataSupplierGenerationWizard createGenericDataSupplierGenerationWizard(Map attributeValues)
    {
        return createGenericDataSupplierGenerationWizard(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "hybrisdatasupplierbackoffice";
    }
}
