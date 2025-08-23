package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.util.EJBTools;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.List;

public abstract class AtomicTypeEJB extends HierarchieTypeEJB implements AtomicTypeRemote, AtomicTypeHome
{
    public abstract String getJavaClassName();


    public abstract void setJavaClassName(String paramString);


    protected int typeCode()
    {
        return 81;
    }


    protected Class checkClass(Class<?> cl, Class superClass) throws EJBInvalidParameterException
    {
        Class<?> ret = cl;
        if(Modifier.isFinal(ret.getModifiers()) && !Serializable.class.isAssignableFrom(ret))
        {
            throw new EJBInvalidParameterException(null, ret.getName() + " is not admissible, because it is final and not serializable", 4711);
        }
        if(ItemRemote.class.isAssignableFrom(ret) || Item.class.isAssignableFrom(ret))
        {
            throw new EJBInvalidParameterException(null, ret
                            .getName() + " is not admissible. atomic types may not describe items", 4711);
        }
        if(superClass != null && !superClass.isAssignableFrom(ret))
        {
            throw new EJBInvalidParameterException(null, ret.getName() + " is no subclass of " + ret.getName(), 4711);
        }
        return ret;
    }


    public PK ejbCreate(PK pkBase, Class javaClass) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        return ejbCreate(pkBase, null, checkClass(javaClass, null));
    }


    public PK ejbCreate(PK pkBase, AtomicTypeRemote superType, Class cl) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        Class javaClass = checkClass(cl, (superType != null) ? superType.getJavaClass() : null);
        PK pk = ejbCreate(pkBase, superType, javaClass.getName());
        setJavaClassName(javaClass.getName());
        return pk;
    }


    public PK ejbCreate(PK pkBase, AtomicTypeRemote superType, String code) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        PK pk = ejbCreate(pkBase, code, null);
        setSuperTypePK((superType != null) ? EJBTools.getPK((ItemRemote)superType) : null);
        setInheritancePathStringInternal(EJBTools.addPKToPKCollectionString((superType != null) ?
                        superType.getInheritancePathString() : EJBTools.EMPTY_PK_COLLECTION_STRING, getPkString()));
        return pk;
    }


    public void ejbPostCreate(PK pkBase, Class javaClass) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        ejbPostCreate(pkBase, null, javaClass);
    }


    public void ejbPostCreate(PK pkBase, AtomicTypeRemote superType, Class javaClass) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        ejbPostCreate(pkBase, superType, javaClass.getName());
    }


    public void ejbPostCreate(PK pkBase, AtomicTypeRemote superType, String code) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        ejbPostCreate(pkBase, code, null);
    }


    public void reinitializeType(AtomicTypeRemote superType, Class javaClass) throws EJBInvalidParameterException
    {
        Class myClass = checkClass(javaClass, (superType != null) ? superType.getJavaClass() : null);
        setJavaClassName(myClass.getName());
        setSuperTypePK((superType != null) ? EJBTools.getPK((ItemRemote)superType) : null);
        setInheritancePathStringInternal(EJBTools.addPKToPKCollectionString((superType != null) ?
                        superType.getInheritancePathString() : EJBTools.EMPTY_PK_COLLECTION_STRING, getPkString()));
    }


    public Class getJavaClass()
    {
        try
        {
            String className = getJavaClassName();
            if(className != null)
            {
                return Class.forName(className);
            }
            AtomicTypeRemote superType = getSuperType();
            if(superType != null)
            {
                return superType.getJavaClass();
            }
            throw new EJBInternalException(null, "cannot determine java-class for type " + getPkString(), 4711);
        }
        catch(ClassNotFoundException e)
        {
            throw new JaloSystemException(e, "!!", 4711);
        }
    }


    public AtomicTypeRemote getSuperType()
    {
        PK superTypePK = getSuperTypePK();
        return (superTypePK == null || PK.NULL_PK.equals(superTypePK)) ? null :
                        (AtomicTypeRemote)EJBTools.instantiatePK(superTypePK);
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


    public boolean isAbstract()
    {
        return Modifier.isAbstract(getJavaClass().getModifiers());
    }


    public boolean isAssignableFrom(TypeRemote type)
    {
        return (type instanceof AtomicTypeRemote && ((AtomicTypeRemote)type)
                        .getInheritancePathString().startsWith(getInheritancePathString()));
    }
}
