package de.hybris.platform.persistence.enumeration;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.c2l.LocalizableItemRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;

public interface EnumerationValueRemote extends LocalizableItemRemote
{
    String getCode();


    void setCode(String paramString) throws ConsistencyCheckException;


    void setCode(ComposedTypeRemote paramComposedTypeRemote, String paramString) throws ConsistencyCheckException, EJBInvalidParameterException;


    int getSequenceNumber();


    void setSequenceNumber(int paramInt);


    boolean isEditable();


    void disableEditing();
}
