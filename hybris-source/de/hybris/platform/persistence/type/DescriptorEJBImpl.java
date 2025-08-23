package de.hybris.platform.persistence.type;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.Descriptor;
import de.hybris.platform.jalo.type.Type;
import java.util.ArrayList;
import java.util.List;

public class DescriptorEJBImpl extends TypeManagerManagedEJBImpl implements Descriptor.DescriptorImpl
{
    public DescriptorEJBImpl(Tenant tenant, DescriptorRemote remoteObject)
    {
        super(tenant, (TypeManagerManagedRemote)remoteObject);
    }


    public String getQualifier()
    {
        return getDescriptorRemote().getQualifier();
    }


    public Type getAttributeType()
    {
        return (Type)wrap(getDescriptorRemote().getAttributeType());
    }


    protected boolean isLocalized(List context)
    {
        return getDescriptorRemote().isLocalized();
    }


    protected List getContext(SessionContext ctx, List<?> context)
    {
        List<Language> result = new ArrayList(context);
        if(ctx != null && ctx.getLanguage() != null && isLocalized(context))
        {
            Language l = ctx.getLanguage();
            if(l != null)
            {
                result.add(l);
            }
        }
        return result;
    }


    protected DescriptorRemote getDescriptorRemote()
    {
        return (DescriptorRemote)getRemote();
    }
}
