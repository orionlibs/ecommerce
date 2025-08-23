package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class AtomicTypeModel extends TypeModel
{
    public static final String _TYPECODE = "AtomicType";
    public static final String INHERITANCEPATHSTRING = "inheritancePathString";
    public static final String JAVACLASS = "javaClass";
    public static final String SUBTYPES = "subtypes";
    public static final String SUPERTYPE = "superType";


    public AtomicTypeModel()
    {
    }


    public AtomicTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AtomicTypeModel(String _code, Boolean _generate, Class _javaClass)
    {
        setCode(_code);
        setGenerate(_generate);
        setJavaClass(_javaClass);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AtomicTypeModel(String _code, Boolean _generate, Class _javaClass, ItemModel _owner, AtomicTypeModel _superType)
    {
        setCode(_code);
        setGenerate(_generate);
        setJavaClass(_javaClass);
        setOwner(_owner);
        setSuperType(_superType);
    }


    @Accessor(qualifier = "javaClass", type = Accessor.Type.GETTER)
    public Class getJavaClass()
    {
        return (Class)getPersistenceContext().getPropertyValue("javaClass");
    }


    @Accessor(qualifier = "subtypes", type = Accessor.Type.GETTER)
    public Collection<AtomicTypeModel> getSubtypes()
    {
        return (Collection<AtomicTypeModel>)getPersistenceContext().getPropertyValue("subtypes");
    }


    @Accessor(qualifier = "superType", type = Accessor.Type.GETTER)
    public AtomicTypeModel getSuperType()
    {
        return (AtomicTypeModel)getPersistenceContext().getPropertyValue("superType");
    }


    @Accessor(qualifier = "javaClass", type = Accessor.Type.SETTER)
    public void setJavaClass(Class value)
    {
        getPersistenceContext().setPropertyValue("javaClass", value);
    }


    @Accessor(qualifier = "superType", type = Accessor.Type.SETTER)
    public void setSuperType(AtomicTypeModel value)
    {
        getPersistenceContext().setPropertyValue("superType", value);
    }
}
