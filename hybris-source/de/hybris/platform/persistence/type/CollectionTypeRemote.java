package de.hybris.platform.persistence.type;

import de.hybris.platform.persistence.EJBInvalidParameterException;

public interface CollectionTypeRemote extends TypeRemote
{
    TypeRemote getElementType();


    int getTypeOfCollection();


    void reinitializeType(TypeRemote paramTypeRemote, int paramInt) throws EJBInvalidParameterException;
}
