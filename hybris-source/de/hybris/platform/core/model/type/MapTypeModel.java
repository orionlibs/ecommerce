package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class MapTypeModel extends TypeModel
{
    public static final String _TYPECODE = "MapType";
    public static final String ARGUMENTTYPE = "argumentType";
    public static final String RETURNTYPE = "returntype";


    public MapTypeModel()
    {
    }


    public MapTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MapTypeModel(TypeModel _argumentType, String _code, Boolean _generate, TypeModel _returntype)
    {
        setArgumentType(_argumentType);
        setCode(_code);
        setGenerate(_generate);
        setReturntype(_returntype);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MapTypeModel(TypeModel _argumentType, String _code, Boolean _generate, ItemModel _owner, TypeModel _returntype)
    {
        setArgumentType(_argumentType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setReturntype(_returntype);
    }


    @Accessor(qualifier = "argumentType", type = Accessor.Type.GETTER)
    public TypeModel getArgumentType()
    {
        return (TypeModel)getPersistenceContext().getPropertyValue("argumentType");
    }


    @Accessor(qualifier = "returntype", type = Accessor.Type.GETTER)
    public TypeModel getReturntype()
    {
        return (TypeModel)getPersistenceContext().getPropertyValue("returntype");
    }


    @Accessor(qualifier = "argumentType", type = Accessor.Type.SETTER)
    public void setArgumentType(TypeModel value)
    {
        getPersistenceContext().setPropertyValue("argumentType", value);
    }


    @Accessor(qualifier = "returntype", type = Accessor.Type.SETTER)
    public void setReturntype(TypeModel value)
    {
        getPersistenceContext().setPropertyValue("returntype", value);
    }
}
