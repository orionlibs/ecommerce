package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.util.EJBTools;

public abstract class MapTypeEJB extends TypeEJB implements MapTypeRemote, MapTypeHome
{
    public abstract PK getArgumentTypePK();


    public abstract void setArgumentTypePK(PK paramPK);


    public abstract PK getReturnTypePK();


    public abstract void setReturnTypePK(PK paramPK);


    protected int typeCode()
    {
        return 84;
    }


    public PK ejbCreate(PK pkBase, String code, TypeRemote argumentType, TypeRemote returnType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        PK pk = ejbCreate(pkBase, code, null);
        reinitializeType(argumentType, returnType);
        return pk;
    }


    public void ejbPostCreate(PK pkBase, String code, TypeRemote argumentType, TypeRemote returnType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        ejbPostCreate(pkBase, code, null);
    }


    public void reinitializeType(TypeRemote keyType, TypeRemote valueType) throws EJBInvalidParameterException
    {
        if(keyType == null || valueType == null)
        {
            throw new EJBInvalidParameterException(null, "argument- and return type must be non-null", 4711);
        }
        setArgumentTypePK(EJBTools.getPK((ItemRemote)keyType));
        setReturnTypePK(EJBTools.getPK((ItemRemote)valueType));
    }


    public TypeRemote getArgumentType()
    {
        return (TypeRemote)EJBTools.instantiatePK(getArgumentTypePK());
    }


    public TypeRemote getReturnType()
    {
        return (TypeRemote)EJBTools.instantiatePK(getReturnTypePK());
    }


    public boolean isAssignableFrom(TypeRemote type)
    {
        if(type instanceof MapTypeRemote)
        {
            MapTypeRemote mt = (MapTypeRemote)type;
            return (getArgumentType().isAssignableFrom(mt.getArgumentType()) && mt.getReturnType()
                            .isAssignableFrom(getReturnType()));
        }
        return false;
    }
}
