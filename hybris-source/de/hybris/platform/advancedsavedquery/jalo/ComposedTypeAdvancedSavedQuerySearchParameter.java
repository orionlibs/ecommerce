package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import org.apache.log4j.Logger;

public class ComposedTypeAdvancedSavedQuerySearchParameter extends GeneratedComposedTypeAdvancedSavedQuerySearchParameter
{
    private static final Logger log = Logger.getLogger(ComposedTypeAdvancedSavedQuerySearchParameter.class.getName());


    @ForceJALO(reason = "something else")
    public Collection getTypeAttributes(SessionContext ctx)
    {
        return getWherePart(ctx).getSavedQuery(ctx).getResultType(ctx).getAttributeDescriptors();
    }


    @ForceJALO(reason = "something else")
    public ComposedType getEnclosingType(SessionContext ctx)
    {
        return getWherePart(ctx).getSavedQuery(ctx).getResultType(ctx);
    }


    @ForceJALO(reason = "something else")
    public void setEnclosingType(SessionContext ctx, ComposedType value)
    {
        throw new JaloSystemException("You can't set the enclosing type for a ComposedTypeDynamicSavedQuerySearchParameter");
    }
}
