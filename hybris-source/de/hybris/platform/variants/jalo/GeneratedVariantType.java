package de.hybris.platform.variants.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.List;

public abstract class GeneratedVariantType extends ComposedType
{
    public static final String VARIANTATTRIBUTES = "variantAttributes";
    public static final String VALIDPRODUCTBASETYPES = "validProductBaseTypes";


    public abstract Collection<ComposedType> getValidProductBaseTypes(SessionContext paramSessionContext);


    public Collection<ComposedType> getValidProductBaseTypes()
    {
        return getValidProductBaseTypes(getSession().getSessionContext());
    }


    public abstract List<VariantAttributeDescriptor> getVariantAttributes(SessionContext paramSessionContext);


    public List<VariantAttributeDescriptor> getVariantAttributes()
    {
        return getVariantAttributes(getSession().getSessionContext());
    }


    public abstract void setVariantAttributes(SessionContext paramSessionContext, List<VariantAttributeDescriptor> paramList);


    public void setVariantAttributes(List<VariantAttributeDescriptor> value)
    {
        setVariantAttributes(getSession().getSessionContext(), value);
    }
}
