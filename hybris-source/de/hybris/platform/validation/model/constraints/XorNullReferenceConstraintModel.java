package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class XorNullReferenceConstraintModel extends TypeConstraintModel
{
    public static final String _TYPECODE = "XorNullReferenceConstraint";
    public static final String FIRSTFIELDNAME = "firstFieldName";
    public static final String SECONDFIELDNAME = "secondFieldName";


    public XorNullReferenceConstraintModel()
    {
    }


    public XorNullReferenceConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public XorNullReferenceConstraintModel(Class _annotation, String _firstFieldName, String _id, String _secondFieldName)
    {
        setAnnotation(_annotation);
        setFirstFieldName(_firstFieldName);
        setId(_id);
        setSecondFieldName(_secondFieldName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public XorNullReferenceConstraintModel(Class _annotation, String _firstFieldName, String _id, ItemModel _owner, String _secondFieldName)
    {
        setAnnotation(_annotation);
        setFirstFieldName(_firstFieldName);
        setId(_id);
        setOwner(_owner);
        setSecondFieldName(_secondFieldName);
    }


    @Accessor(qualifier = "firstFieldName", type = Accessor.Type.GETTER)
    public String getFirstFieldName()
    {
        return (String)getPersistenceContext().getPropertyValue("firstFieldName");
    }


    @Accessor(qualifier = "secondFieldName", type = Accessor.Type.GETTER)
    public String getSecondFieldName()
    {
        return (String)getPersistenceContext().getPropertyValue("secondFieldName");
    }


    @Accessor(qualifier = "firstFieldName", type = Accessor.Type.SETTER)
    public void setFirstFieldName(String value)
    {
        getPersistenceContext().setPropertyValue("firstFieldName", value);
    }


    @Accessor(qualifier = "secondFieldName", type = Accessor.Type.SETTER)
    public void setSecondFieldName(String value)
    {
        getPersistenceContext().setPropertyValue("secondFieldName", value);
    }
}
