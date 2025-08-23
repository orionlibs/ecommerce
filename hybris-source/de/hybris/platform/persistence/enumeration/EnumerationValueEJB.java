package de.hybris.platform.persistence.enumeration;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.c2l.LocalizableItemEJB;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.util.EJBTools;

public abstract class EnumerationValueEJB extends LocalizableItemEJB implements EnumerationValueRemote, EnumerationValueHome
{
    public abstract String getCodeInternal();


    public abstract void setCodeInternal(String paramString);


    public abstract String getCodeLowerCase();


    public abstract void setCodeLowerCase(String paramString);


    public abstract int getSequenceNumber();


    public abstract void setSequenceNumber(int paramInt);


    public abstract boolean getEditableFlag();


    public abstract void setEditableFlag(boolean paramBoolean);


    public PK ejbCreate(PK pkBase, ComposedTypeRemote enumerationType, String code, int number) throws ConsistencyCheckException, EJBInvalidParameterException
    {
        doCreateInternal(pkBase, enumerationType, null, null);
        setEditableFlag(true);
        setCode(enumerationType, code);
        setSequenceNumber(number);
        return null;
    }


    public void ejbPostCreate(PK pkBase, ComposedTypeRemote enumerationType, String code, int number)
    {
        doPostCreateInternal(pkBase, enumerationType, null);
    }


    protected int typeCode()
    {
        return 91;
    }


    public String getCode()
    {
        return getCodeInternal();
    }


    public void setCode(String newCode) throws ConsistencyCheckException
    {
        try
        {
            setCode(getComposedType(), newCode);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public void setCode(ComposedTypeRemote enumerationType, String newCode) throws ConsistencyCheckException, EJBInvalidParameterException
    {
        EnumerationValueRemote conflict;
        if(!isEditable())
        {
            throw new ConsistencyCheckException(null, "" + getPkString() + " is not editable", 0);
        }
        try
        {
            conflict = SystemEJB.getInstance().getEnumerationManager().getEnumerationValue(enumerationType, newCode);
        }
        catch(EJBItemNotFoundException e)
        {
            conflict = null;
        }
        if(conflict != null && !conflict.getPK().equals(getPkString()))
        {
            throw new ConsistencyCheckException(null, "there is already an EnumerationValue " + EJBTools.getPK(conflict) + " with type " +
                            EJBTools.getPK(enumerationType) + " and code " + newCode, 0);
        }
        setCodeInternal(newCode);
        setCodeLowerCase(newCode.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public boolean isEditable()
    {
        return getEditableFlag();
    }


    public void disableEditing()
    {
        setEditableFlag(false);
    }
}
