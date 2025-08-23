package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemCacheKey;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.Utilities;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class ComposedTypeEJB extends HierarchieTypeEJB implements ComposedTypeRemote, ComposedTypeHome
{
    private static final Logger log = Logger.getLogger(ComposedTypeEJB.class);


    public abstract int getItemTypeCodeInt();


    public abstract void setItemTypeCodeInt(int paramInt);


    public abstract String getItemJNDIName();


    public abstract void setItemJNDIName(String paramString);


    public abstract String getJaloClassName();


    public abstract void setJaloClassName(String paramString);


    public abstract boolean getPropertyTableStatus();


    public abstract void setPropertyTableStatus(boolean paramBoolean);


    public abstract boolean getSingletonFlag();


    public abstract void setSingletonFlag(boolean paramBoolean);


    public abstract boolean getRemovableFlag();


    public abstract void setRemovableFlag(boolean paramBoolean);


    public String getItemTableName()
    {
        ItemDeployment depl = getDeployment();
        return (depl != null && !depl.isAbstract()) ? depl.getDatabaseTableName() : null;
    }


    protected int typeCode()
    {
        return 82;
    }


    protected boolean preLoadUnlocalizedProperties()
    {
        return true;
    }


    public int getItemTypeCode()
    {
        int i = getItemTypeCodeInt();
        return (i < 0) ? (i * -1) : i;
    }


    public void setItemTypeCode(int tc)
    {
        int i = getItemTypeCodeInt();
        setItemTypeCodeInt((i < 0 && tc > 0) ? (-1 * tc) : tc);
    }


    public boolean isAbstract()
    {
        return (getItemTypeCodeInt() <= 0);
    }


    public void setAbstract()
    {
        int i = getItemTypeCodeInt();
        if(i > 0)
        {
            setItemTypeCodeInt(-1 * i);
        }
    }


    public String getItemPropertyTableName()
    {
        ItemDeployment depl = getDeployment();
        return (depl != null && !depl.isAbstract()) ? depl.getDumpPropertyTableName() : null;
    }


    public void changeSTPK(String string)
    {
        setSuperTypePK(PK.parse(string));
    }


    public PK ejbCreate(PK pkBase, ComposedTypeRemote superType, String code, String jaloClassName, ItemDeployment deployment, ComposedTypeRemote metaType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        if(!TypeTools.isValidCode(code))
        {
            throw new EJBInvalidParameterException(null, code + " is no valid code of a type.", 4711);
        }
        if(deployment == null && superType == null)
        {
            throw new EJBInvalidParameterException(null, "cannot create item type with missing deployment and supertype - at least one has to be provided", 0);
        }
        ejbCreate(pkBase, code, metaType);
        doTypeInitialization(superType, deployment, jaloClassName, metaType);
        return null;
    }


    public void ejbPostCreate(PK pkBase, ComposedTypeRemote superType, String code, String jaloClassName, ItemDeployment deployment, ComposedTypeRemote metaType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        ejbPostCreate(pkBase, code, metaType);
    }


    public ComposedTypeRemote getComposedType()
    {
        PK tpk = getTypePkString();
        try
        {
            if(tpk == null || PK.NULL_PK.equals(tpk))
            {
                ComposedTypeRemote defaultType = getDefaultType();
                try
                {
                    setComposedType(defaultType);
                }
                catch(EJBInvalidParameterException e)
                {
                    throw new EJBInternalException(e);
                }
                return defaultType;
            }
            return (ComposedTypeRemote)EJBTools.instantiatePK(tpk);
        }
        catch(EJBItemNotFoundException e)
        {
            return null;
        }
    }


    public void setComposedType(ComposedTypeRemote type) throws EJBInvalidParameterException
    {
        PK old = getTypePkString();
        PK pk = EJBTools.getPK((ItemRemote)type);
        try
        {
            if(pk == null)
            {
                pk = EJBTools.getPK((ItemRemote)getDefaultType());
            }
        }
        catch(EJBItemNotFoundException e)
        {
            pk = PK.NULL_PK;
        }
        if(old != pk && (old == null || !old.equals(pk)))
        {
            if(old != null && !PK.NULL_PK.equals(pk))
            {
                typeChanged(old, pk);
            }
            setTypePkString(pk);
        }
    }


    public void reinitializeType(ComposedTypeRemote superType, ItemDeployment deployment, String jaloClassName, ComposedTypeRemote metaType)
    {
        doTypeInitialization(superType, deployment, jaloClassName, metaType);
    }


    protected void doTypeInitialization(ComposedTypeRemote newSuperType, ItemDeployment newDeployment, String jaloClassName, ComposedTypeRemote metaType)
    {
        ComposedTypeRemote myType = getComposedType();
        boolean hasOwnDeployment = (newDeployment != null);
        ItemDeployment myDeployment = newDeployment;
        String myJaloClass = jaloClassName;
        if(newSuperType != null)
        {
            if(myDeployment == null)
            {
                myDeployment = newSuperType.getDeployment();
            }
            if(myJaloClass == null)
            {
                myJaloClass = newSuperType.getJaloClassName();
            }
            try
            {
                if(metaType == null)
                {
                    ComposedTypeRemote superMetaType = newSuperType.getComposedType();
                    if(superMetaType != null && !Utilities.ejbEquals((ItemRemote)superMetaType, (ItemRemote)myType))
                    {
                        setTypePkString(EJBTools.getPK((ItemRemote)superMetaType));
                    }
                }
            }
            catch(Exception exception)
            {
            }
        }
        if(metaType != null)
        {
            setTypePkString(EJBTools.getPK((ItemRemote)metaType));
        }
        setSuperTypePK((newSuperType != null) ? EJBTools.getPK((ItemRemote)newSuperType) : null);
        setInheritancePathStringInternal(EJBTools.addPKToPKCollectionString(
                        (newSuperType != null) ? newSuperType.getInheritancePathString() : EJBTools.EMPTY_PK_COLLECTION_STRING,
                        getPkString()));
        setDeployment(myDeployment);
        setJaloClassName(myJaloClass);
        setRemovableFlag(!hasOwnDeployment);
        setPropertyTableStatus((newSuperType != null && newSuperType.getPropertyTableStatus()));
    }


    public void ejbRemove()
    {
        getEntityContext().getPersistencePool().getPersistenceManager().clearComposedType(getPkString(), getCode());
    }


    public boolean isRemovable()
    {
        return getRemovableFlag();
    }


    public ItemDeployment getDeployment()
    {
        return getDeploymentInternal(true);
    }


    protected ItemDeployment getDeploymentInternal(boolean warn)
    {
        String jndiName = getItemJNDIName();
        if(jndiName == null)
        {
            log.warn("Type " + getCode() + " doesn't have a JNDI name");
            return null;
        }
        ItemDeployment depl = getEntityContext().getPersistencePool().getPersistenceManager().getItemDeployment(jndiName);
        if(depl == null)
        {
            log.error("Type " + getCode() + " doesn't have a deployment for JNDI name '" + jndiName + "'");
            return null;
        }
        return depl;
    }


    public void setDeployment(ItemDeployment deployment)
    {
        if(deployment != null)
        {
            if(deployment instanceof AbstractItemDeploymentWrapper)
            {
                deployment = ((AbstractItemDeploymentWrapper)deployment).original;
            }
            setItemTypeCode(deployment.getTypeCode());
            setItemJNDIName(deployment.getName());
        }
        else
        {
            setItemTypeCode(0);
            setItemJNDIName(null);
        }
    }


    public PK getSuperTypeItemPK()
    {
        PK superTypePK = getSuperTypePK();
        return (superTypePK == null || PK.NULL_PK.equals(superTypePK)) ? null : superTypePK;
    }


    public ComposedTypeRemote getSuperType()
    {
        PK superTypePK = getSuperTypePK();
        return (superTypePK == null || PK.NULL_PK.equals(superTypePK)) ? null :
                        (ComposedTypeRemote)EJBTools.instantiatePK(superTypePK);
    }


    public String getInheritancePathString()
    {
        return getInheritancePathStringInternal();
    }


    public void setInheritancePathString(String pks)
    {
        setInheritancePathStringInternal(pks);
    }


    public List getInheritancePath()
    {
        return EJBTools.instantiateCommaSeparatedPKString(getInheritancePathString());
    }


    protected void setInheritancePath(List path)
    {
        setInheritancePathString(EJBTools.getPKCollectionString(path));
    }


    public boolean isAssignableFrom(TypeRemote type)
    {
        if(!(type instanceof ComposedTypeRemote))
        {
            return false;
        }
        Boolean value = (Boolean)getCachedValueForReading((ItemCacheKey)new AssignableFromItemCacheKey((ComposedTypeRemote)type));
        return value.booleanValue();
    }


    public boolean isSingleton()
    {
        return getSingletonFlag();
    }


    public void setSingleton(boolean isSingleton)
    {
        setSingletonFlag(isSingleton);
    }
}
