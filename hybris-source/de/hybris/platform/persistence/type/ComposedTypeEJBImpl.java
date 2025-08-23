package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemWrapperCreator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.spring.CGLibUtils;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;

public class ComposedTypeEJBImpl extends TypeEJBImpl implements ComposedType.ComposedTypeImpl
{
    private static final Logger LOG = Logger.getLogger(ComposedTypeEJBImpl.class.getName());
    private volatile transient Optional<PK> superTypeCache = null;
    private volatile String inheritancePathCache = null;
    private volatile Integer itemTypeCodeCache;
    private volatile String itemJNDICache;
    private volatile String itemTableCache;


    public Class getJaloObjectClass()
    {
        if(getPK().equals(getTypeKey()))
        {
            String customCompTypeJaloClassName = ((ComposedTypeRemote)getRemote()).getJaloClassName();
            try
            {
                return (customCompTypeJaloClassName != null) ? CGLibUtils.getOriginalClass(
                                Class.forName(customCompTypeJaloClassName)) :
                                ComposedType.class;
            }
            catch(ClassNotFoundException e1)
            {
                return ComposedType.class;
            }
        }
        return super.getJaloObjectClass();
    }


    static
    {
        WrapperFactory.registerItemWrapperCreator(82, (ItemWrapperCreator)new Object());
    }

    public ComposedTypeEJBImpl(Tenant tenant, ComposedTypeRemote remoteObject)
    {
        super(tenant, (TypeRemote)remoteObject);
    }


    public Class getJaloClass()
    {
        String className = null;
        try
        {
            className = getComposedTypeRemote().getJaloClassName();
            return (className != null) ? CGLibUtils.getOriginalClass(Class.forName(className)) : null;
        }
        catch(ClassNotFoundException e)
        {
            LOG.warn("missing jalo class '" + className + "' for item type '" + getCode() + "' - trying supertype class instead");
            return null;
        }
    }


    public void setJaloClass(Class jaloClass)
    {
        getComposedTypeRemote().setJaloClassName((jaloClass != null) ? jaloClass.getName() : null);
    }


    public String getTable()
    {
        if(this.itemTableCache == null)
        {
            this.itemTableCache = getComposedTypeRemote().getItemTableName();
        }
        return this.itemTableCache;
    }


    public String getDumpPropertyTable()
    {
        return getComposedTypeRemote().getItemPropertyTableName();
    }


    public ComposedType getSuperType()
    {
        try
        {
            if(this.superTypeCache == null)
            {
                PK stpk = getComposedTypeRemote().getSuperTypeItemPK();
                this.superTypeCache = Optional.ofNullable(stpk);
            }
            if(!this.superTypeCache.isPresent())
            {
                return null;
            }
            return (ComposedType)WrapperFactory.getCachedItem(getCache(), this.superTypeCache.get());
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
        catch(IllegalArgumentException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Set getSubTypes()
    {
        return (Set)(new Object(this, getCache(), getPK().getTypeCode(), ComposedType.SUBTYPES + ComposedType.SUBTYPES))
                        .getCached();
    }


    public Set getAllSubTypes()
    {
        return (Set)(new Object(this, getCache(), getPK().getTypeCode(), "all" + ComposedType.SUBTYPES +
                        getPK().getLongValueAsString()))
                        .getCached();
    }


    public Set getDeclaredAttributeDescriptors()
    {
        return new TreeSet((Set)wrap(getTypeManagerEJB().getDeclaredAttributeDescriptors(getComposedTypeRemote())));
    }


    public Set getInheritedAttributeDescriptors()
    {
        return new TreeSet((Set)wrap(getTypeManagerEJB().getInheritedAttributeDescriptors(getComposedTypeRemote())));
    }


    public void setDeclaredAttributeDescriptors(Set fds) throws JaloInvalidParameterException
    {
        try
        {
            getTypeManagerEJB().setDeclaredAttributeDescriptors(getComposedTypeRemote(), (Set)unwrap(fds));
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public Set getAttributeDescriptors()
    {
        return (Set)(new Object(this, getCache(), 87, ComposedType.ATTRIBUTEDESCRIPTORS + ComposedType.ATTRIBUTEDESCRIPTORS))
                        .getCached();
    }


    public Set getAttributeDescriptorsIncludingPrivate()
    {
        return new TreeSet((Set)wrap(getTypeManagerEJB().getAttributeDescriptors(getComposedTypeRemote(), true)));
    }


    public AttributeDescriptor getDeclaredAttributeDescriptor(String qualifier) throws JaloItemNotFoundException
    {
        try
        {
            return (AttributeDescriptor)wrap(
                            getTypeManagerEJB().getDeclaredAttributeDescriptor(getComposedTypeRemote(), qualifier));
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloItemNotFoundException(e, 4711);
        }
    }


    public AttributeDescriptor getAttributeDescriptor(String qualifier) throws JaloItemNotFoundException
    {
        return (AttributeDescriptor)(new Object(this, getCache(), 87, ComposedType.ATTRIBUTEDESCRIPTORS + ComposedType.ATTRIBUTEDESCRIPTORS +
                        getPK().getLongValueAsString(), qualifier))
                        .getCached();
    }


    public AttributeDescriptor getEveryAttributeDescriptor(String qualifier) throws JaloItemNotFoundException
    {
        try
        {
            return (AttributeDescriptor)wrap((
                            (TypeManagerEJB)TypeManager.getInstance().getRemote()).getEveryAttributeDescriptor(
                            getComposedTypeRemote(), qualifier));
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloItemNotFoundException(e, 4711);
        }
    }


    public AttributeDescriptor createAttributeDescriptor(PK pkBase, String qualifier, Type type, int modifiers) throws JaloDuplicateQualifierException, JaloInvalidParameterException
    {
        try
        {
            return (AttributeDescriptor)wrap(((TypeManagerEJB)TypeManager.getInstance().getRemote()).createPropertyDescriptor(pkBase,
                            getComposedTypeRemote(), qualifier, (TypeRemote)unwrap(type), modifiers, null, null, false));
        }
        catch(EJBDuplicateQualifierException e)
        {
            throw new JaloDuplicateQualifierException(e, 4711);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    public AttributeDescriptor redeclareAttributeDescriptor(String qualifier, Type type, int modifiers) throws JaloInvalidParameterException, JaloItemNotFoundException
    {
        try
        {
            return (AttributeDescriptor)wrap((
                            (TypeManagerEJB)TypeManager.getInstance().getRemote()).redeclareAttributeDescriptor(
                            getComposedTypeRemote(), qualifier, (TypeRemote)unwrap(type), modifiers));
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloItemNotFoundException(e, 4711);
        }
    }


    public boolean isSingleton()
    {
        return getComposedTypeRemote().isSingleton();
    }


    public void setSingleton(boolean isSingleton)
    {
        getComposedTypeRemote().setSingleton(isSingleton);
    }


    public boolean isAbstract()
    {
        return getComposedTypeRemote().isAbstract();
    }


    protected ComposedTypeRemote getComposedTypeRemote()
    {
        return (ComposedTypeRemote)getRemote();
    }


    public String getJNDIName()
    {
        if(this.itemJNDICache == null)
        {
            this.itemJNDICache = getComposedTypeRemote().getItemJNDIName();
        }
        return this.itemJNDICache;
    }


    public int getItemTypeCode()
    {
        if(this.itemTypeCodeCache == null)
        {
            this.itemTypeCodeCache = Integer.valueOf(getComposedTypeRemote().getItemTypeCode());
        }
        return (this.itemTypeCodeCache != null) ? this.itemTypeCodeCache.intValue() : 0;
    }


    public String getInheritancePathString()
    {
        if(this.inheritancePathCache == null)
        {
            this.inheritancePathCache = getComposedTypeRemote().getInheritancePathString();
        }
        return this.inheritancePathCache;
    }
}
