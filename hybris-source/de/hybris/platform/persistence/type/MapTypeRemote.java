package de.hybris.platform.persistence.type;

import de.hybris.platform.persistence.EJBInvalidParameterException;

public interface MapTypeRemote extends TypeRemote
{
    TypeRemote getArgumentType();


    TypeRemote getReturnType();


    void reinitializeType(TypeRemote paramTypeRemote1, TypeRemote paramTypeRemote2) throws EJBInvalidParameterException;
}
