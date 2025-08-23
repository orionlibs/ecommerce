package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class AttributeConstraintModel extends AbstractConstraintModel
{
    public static final String _TYPECODE = "AttributeConstraint";
    public static final String QUALIFIER = "qualifier";
    public static final String LANGUAGES = "languages";
    public static final String DESCRIPTOR = "descriptor";


    public AttributeConstraintModel()
    {
    }


    public AttributeConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AttributeConstraintModel(Class _annotation, String _id)
    {
        setAnnotation(_annotation);
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AttributeConstraintModel(Class _annotation, String _id, ItemModel _owner)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "descriptor", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getDescriptor()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("descriptor");
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
    public Set<LanguageModel> getLanguages()
    {
        return (Set<LanguageModel>)getPersistenceContext().getPropertyValue("languages");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "descriptor", type = Accessor.Type.SETTER)
    public void setDescriptor(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("descriptor", value);
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
    public void setLanguages(Set<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("languages", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }
}
