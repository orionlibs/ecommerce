package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemWrapperCreator;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import java.util.Set;

public class AttributeDescriptorEJBImpl extends DescriptorEJBImpl implements AttributeDescriptor.AttributeDescriptorImpl
{
    static
    {
        WrapperFactory.registerItemWrapperCreator(87, (ItemWrapperCreator)new Object());
    }

    public Class getPersistenceClass()
    {
        return getAttributeDescriptorRemote().getPersistenceClass();
    }


    public String getDatabaseColumn()
    {
        return getAttributeDescriptorRemote().getDatabaseColumn();
    }


    public void setDatabaseColumn(String col)
    {
        getAttributeDescriptorRemote().setDatabaseColumn(col);
    }


    public AttributeDescriptor getSelectionOf()
    {
        return (AttributeDescriptor)wrap(getAttributeDescriptorRemote().getSelectionOf());
    }


    public void setSelectionOf(AttributeDescriptor descriptor)
    {
        try
        {
            getAttributeDescriptorRemote().setSelectionOf((AttributeDescriptorRemote)unwrap(descriptor));
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, -1);
        }
    }


    public String getProposedDatabaseColumn()
    {
        return getTypeManagerEJB().getProposedDatabaseColumn(getAttributeDescriptorRemote());
    }


    public AttributeDescriptorEJBImpl(Tenant tenant, AttributeDescriptorRemote remoteObject)
    {
        super(tenant, (DescriptorRemote)remoteObject);
    }


    public AttributeDescriptor getSuperAttributeDescriptor()
    {
        return (AttributeDescriptor)wrap(getAttributeDescriptorRemote().getDeclaringSuperAttributeDescriptor());
    }


    public void setAttributeType(Type type) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setAttributeType((TypeRemote)unwrap(type));
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public void redeclareAttributeType(Type type, int modifiers) throws JaloInvalidParameterException
    {
        try
        {
            getTypeManagerEJB().redeclareAttributeDescriptor(getAttributeDescriptorRemote(), (TypeRemote)unwrap(type), modifiers);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public ComposedType getDeclaringEnclosingType()
    {
        return (ComposedType)wrap(getAttributeDescriptorRemote().getDeclaringEnclosingType());
    }


    public ComposedType getEnclosingType()
    {
        return (ComposedType)wrap(getAttributeDescriptorRemote().getEnclosingType());
    }


    public void setEnclosingType(ComposedType type) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setEnclosingType((ComposedTypeRemote)unwrap(type));
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public int getModifiers()
    {
        return getAttributeDescriptorRemote().getModifiers();
    }


    public void setModifiers(int modifiers)
    {
        try
        {
            getAttributeDescriptorRemote().setModifiers(modifiers);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e.getMessage(), e.getErrorCode());
        }
    }


    public AtomicType getPersistenceType()
    {
        return (AtomicType)wrap(getAttributeDescriptorRemote().getPersistenceType());
    }


    public void setPartOf(boolean part) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setPartOf(part);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public void setInitial(boolean init) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setInitial(init);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public void setProperty(boolean isProperty)
    {
        getAttributeDescriptorRemote().setProperty(isProperty);
    }


    public void setLocalized(boolean localized)
    {
        getAttributeDescriptorRemote().setLocalized(localized);
    }


    public void setReadable(boolean readable) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setReadable(readable);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public void setWritable(boolean writable) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setWritable(writable);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public void setRemovable(boolean removable) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setRemovable(removable);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public void setOptional(boolean optional) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setOptional(optional);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public void setSearchable(boolean searchable) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setSearchable(searchable);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public void setPrivate(boolean priv) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setPrivate(priv);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public void setDontOptimize(boolean opt) throws JaloInvalidParameterException
    {
        try
        {
            getAttributeDescriptorRemote().setDontOptimize(opt);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public Set getAllSubAttributeDescriptors()
    {
        return (Set)wrap(getTypeManagerEJB().getAllSubAttributeDescriptors(getAttributeDescriptorRemote()));
    }


    protected AttributeDescriptorRemote getAttributeDescriptorRemote()
    {
        return (AttributeDescriptorRemote)getRemote();
    }


    public String getPersistenceQualifier()
    {
        return getAttributeDescriptorRemote().getPersistenceQualifier();
    }
}
