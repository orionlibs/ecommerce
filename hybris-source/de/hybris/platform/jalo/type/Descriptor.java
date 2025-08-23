package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;

public abstract class Descriptor extends TypeManagerManaged
{
    public static final String ATTRIBUTETYPE = "attributeType";
    public static final String QUALIFIER = "qualifier";
    private String qualifier = null;
    private static final AttributeAccess _AD_ATTRIBUTETYPE = (AttributeAccess)new Object();


    public String getQualifier()
    {
        if(this.qualifier == null)
        {
            this
                            .qualifier = (String)(new Object(this, "qualifier", false, true)).get(null);
        }
        return this.qualifier;
    }


    public Type getAttributeType()
    {
        return getAttributeType(getSession().getSessionContext());
    }


    public Type getAttributeType(SessionContext ctx)
    {
        return getContextAdjustedAttributeType(ctx, getRealAttributeType());
    }


    public Type getRealAttributeType()
    {
        return (Type)(new Object(this, "attributeType"))
                        .get(null);
    }


    protected Type getContextAdjustedAttributeType(SessionContext ctx, Type realtype)
    {
        Type translated = realtype;
        if(realtype instanceof MapType)
        {
            MapType mt = (MapType)realtype;
            if(ctx != null && ctx.getLanguage() != null)
            {
                Type argType = mt.getArgumentType(null);
                if(argType instanceof ComposedType && Language.class.isAssignableFrom(((ComposedType)argType).getJaloClass()))
                {
                    translated = mt.getReturnType(null);
                }
            }
        }
        return translated;
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getDescriptorImpl().toString();
    }


    protected DescriptorImpl getDescriptorImpl()
    {
        return (DescriptorImpl)this.impl;
    }


    public int compareTo(Object o)
    {
        String c1 = getQualifier();
        String c2 = ((Descriptor)o).getQualifier();
        return (c1 == null || c2 == null) ? 0 : c1.toLowerCase().compareTo(c2.toLowerCase());
    }
}
