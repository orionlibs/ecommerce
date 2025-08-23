package de.hybris.platform.validation.model.constraints.jsr303;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.enums.RegexpFlag;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import java.util.Set;

public class ObjectPatternConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "ObjectPatternConstraint";
    public static final String REGEXP = "regexp";
    public static final String FLAGS = "flags";


    public ObjectPatternConstraintModel()
    {
    }


    public ObjectPatternConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ObjectPatternConstraintModel(Class _annotation, Set<RegexpFlag> _flags, String _id, String _regexp)
    {
        setAnnotation(_annotation);
        setFlags(_flags);
        setId(_id);
        setRegexp(_regexp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ObjectPatternConstraintModel(Class _annotation, Set<RegexpFlag> _flags, String _id, ItemModel _owner, String _regexp)
    {
        setAnnotation(_annotation);
        setFlags(_flags);
        setId(_id);
        setOwner(_owner);
        setRegexp(_regexp);
    }


    @Accessor(qualifier = "flags", type = Accessor.Type.GETTER)
    public Set<RegexpFlag> getFlags()
    {
        return (Set<RegexpFlag>)getPersistenceContext().getPropertyValue("flags");
    }


    @Accessor(qualifier = "regexp", type = Accessor.Type.GETTER)
    public String getRegexp()
    {
        return (String)getPersistenceContext().getPropertyValue("regexp");
    }


    @Accessor(qualifier = "flags", type = Accessor.Type.SETTER)
    public void setFlags(Set<RegexpFlag> value)
    {
        getPersistenceContext().setPropertyValue("flags", value);
    }


    @Accessor(qualifier = "regexp", type = Accessor.Type.SETTER)
    public void setRegexp(String value)
    {
        getPersistenceContext().setPropertyValue("regexp", value);
    }
}
