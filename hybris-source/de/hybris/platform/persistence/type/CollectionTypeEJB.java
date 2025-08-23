package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.util.EJBTools;

public abstract class CollectionTypeEJB extends TypeEJB implements CollectionTypeRemote, CollectionTypeHome
{
    public abstract PK getElementTypePK();


    public abstract void setElementTypePK(PK paramPK);


    public abstract int getTypeOfCollection();


    public abstract void setTypeOfCollection(int paramInt);


    protected int typeCode()
    {
        return 83;
    }


    public PK ejbCreate(PK pkBase, String code, TypeRemote elementType, int collectionType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        PK pk = ejbCreate(pkBase, code, null);
        reinitializeType(elementType, collectionType);
        return pk;
    }


    public void ejbPostCreate(PK pkBase, String code, TypeRemote elementType, int collectionType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        ejbPostCreate(pkBase, code, null);
    }


    public void reinitializeType(TypeRemote elementType, int typeOfCollection) throws EJBInvalidParameterException
    {
        if(elementType == null)
        {
            throw new EJBInvalidParameterException(null, "type must be non-null", 4711);
        }
        if(typeOfCollection < 0 || typeOfCollection > 3)
        {
            throw new EJBInvalidParameterException(null, "invalid collection type " + typeOfCollection + " for collection type " +
                            getCode(), 4711);
        }
        setElementTypePK(EJBTools.getPK((ItemRemote)elementType));
        setTypeOfCollection(typeOfCollection);
    }


    public TypeRemote getElementType()
    {
        return (TypeRemote)EJBTools.instantiatePK(getElementTypePK());
    }


    public boolean isAssignableFrom(TypeRemote type)
    {
        return (type instanceof CollectionTypeRemote &&
                        getElementType().isAssignableFrom(((CollectionTypeRemote)type).getElementType()));
    }


    protected boolean isAdmissibleCollection(Object o)
    {
        switch(getTypeOfCollection())
        {
            case 0:
                return o instanceof java.util.Collection;
            case 1:
                return o instanceof java.util.Set;
            case 2:
                return o instanceof java.util.List;
            case 3:
                return o instanceof java.util.SortedSet;
        }
        return false;
    }
}
