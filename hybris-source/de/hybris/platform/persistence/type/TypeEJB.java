package de.hybris.platform.persistence.type;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;

public abstract class TypeEJB extends TypeManagerManagedEJB implements TypeRemote, TypeHome
{
    public static final String INTERNAL_DESCRIPTION_PROPERTY = "typeDescription";


    public abstract String getInternalCode();


    public abstract void setInternalCode(String paramString);


    public abstract String getInternalCodeLowerCase();


    public abstract void setInternalCodeLowerCase(String paramString);


    public abstract boolean isAssignableFrom(TypeRemote paramTypeRemote);


    public PK ejbCreate(PK pkBase, String code, ComposedTypeRemote metaType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        if(code == null)
        {
            throw new EJBInvalidParameterException(null, "code must be non-null", 4711);
        }
        PK pk = doCreateInternal(pkBase, metaType, null, null);
        setCode(code);
        return pk;
    }


    public void ejbPostCreate(PK pkBase, String code, ComposedTypeRemote metaType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        doPostCreateInternal(null, null, null);
    }


    public String getCode()
    {
        return getInternalCode();
    }


    public void setCode(String code) throws EJBDuplicateCodeException
    {
        if(code.equals(getInternalCode()))
        {
            return;
        }
        try
        {
            TypeRemote type = getTypeManager().getType(code);
            if(type != null)
            {
                throw new EJBDuplicateCodeException(null, "A type with code " + code + " already exists (" + type.getPkString() + ").", 4711);
            }
        }
        catch(EJBItemNotFoundException e)
        {
            setInternalCode(code);
            setInternalCodeLowerCase(code.toLowerCase(LocaleHelper.getPersistenceLocale()));
        }
    }
}
