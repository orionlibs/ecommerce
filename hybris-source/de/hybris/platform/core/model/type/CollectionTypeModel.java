package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CollectionTypeModel extends TypeModel
{
    public static final String _TYPECODE = "CollectionType";
    public static final String ELEMENTTYPE = "elementType";
    public static final String TYPEOFCOLLECTION = "typeOfCollection";
    public static final String TYPEOFCOLLECTIONINTERNAL = "typeOfCollectionInternal";


    public CollectionTypeModel()
    {
    }


    public CollectionTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CollectionTypeModel(String _code, TypeModel _elementType, Boolean _generate)
    {
        setCode(_code);
        setElementType(_elementType);
        setGenerate(_generate);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CollectionTypeModel(String _code, TypeModel _elementType, Boolean _generate, ItemModel _owner, TypeOfCollectionEnum _typeOfCollection)
    {
        setCode(_code);
        setElementType(_elementType);
        setGenerate(_generate);
        setOwner(_owner);
        setTypeOfCollection(_typeOfCollection);
    }


    @Accessor(qualifier = "elementType", type = Accessor.Type.GETTER)
    public TypeModel getElementType()
    {
        return (TypeModel)getPersistenceContext().getPropertyValue("elementType");
    }


    @Accessor(qualifier = "typeOfCollection", type = Accessor.Type.GETTER)
    public TypeOfCollectionEnum getTypeOfCollection()
    {
        return (TypeOfCollectionEnum)getPersistenceContext().getPropertyValue("typeOfCollection");
    }


    @Accessor(qualifier = "elementType", type = Accessor.Type.SETTER)
    public void setElementType(TypeModel value)
    {
        getPersistenceContext().setPropertyValue("elementType", value);
    }


    @Accessor(qualifier = "typeOfCollection", type = Accessor.Type.SETTER)
    public void setTypeOfCollection(TypeOfCollectionEnum value)
    {
        getPersistenceContext().setPropertyValue("typeOfCollection", value);
    }
}
