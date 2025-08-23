package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.enums.ValidatorLanguage;

public class DynamicConstraintModel extends TypeConstraintModel
{
    public static final String _TYPECODE = "DynamicConstraint";
    public static final String LANGUAGE = "language";
    public static final String EXPRESSION = "expression";


    public DynamicConstraintModel()
    {
    }


    public DynamicConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DynamicConstraintModel(Class _annotation, String _expression, String _id, ValidatorLanguage _language)
    {
        setAnnotation(_annotation);
        setExpression(_expression);
        setId(_id);
        setLanguage(_language);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DynamicConstraintModel(Class _annotation, String _expression, String _id, ValidatorLanguage _language, ItemModel _owner)
    {
        setAnnotation(_annotation);
        setExpression(_expression);
        setId(_id);
        setLanguage(_language);
        setOwner(_owner);
    }


    @Accessor(qualifier = "expression", type = Accessor.Type.GETTER)
    public String getExpression()
    {
        return (String)getPersistenceContext().getPropertyValue("expression");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public ValidatorLanguage getLanguage()
    {
        return (ValidatorLanguage)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "expression", type = Accessor.Type.SETTER)
    public void setExpression(String value)
    {
        getPersistenceContext().setPropertyValue("expression", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(ValidatorLanguage value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }
}
