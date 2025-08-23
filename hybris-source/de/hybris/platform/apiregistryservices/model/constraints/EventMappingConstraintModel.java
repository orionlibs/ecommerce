package de.hybris.platform.apiregistryservices.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.enums.RegexpFlag;
import de.hybris.platform.validation.model.constraints.TypeConstraintModel;
import java.util.Set;

public class EventMappingConstraintModel extends TypeConstraintModel
{
    public static final String _TYPECODE = "EventMappingConstraint";
    public static final String KEYREGEXP = "keyRegexp";
    public static final String KEYFLAGS = "keyFlags";
    public static final String VALUEREGEXP = "valueRegexp";
    public static final String VALUEFLAGS = "valueFlags";


    public EventMappingConstraintModel()
    {
    }


    public EventMappingConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EventMappingConstraintModel(Class _annotation, String _id, Set<RegexpFlag> _keyFlags, String _keyRegexp, Set<RegexpFlag> _valueFlags, String _valueRegexp)
    {
        setAnnotation(_annotation);
        setId(_id);
        setKeyFlags(_keyFlags);
        setKeyRegexp(_keyRegexp);
        setValueFlags(_valueFlags);
        setValueRegexp(_valueRegexp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EventMappingConstraintModel(Class _annotation, String _id, Set<RegexpFlag> _keyFlags, String _keyRegexp, ItemModel _owner, Set<RegexpFlag> _valueFlags, String _valueRegexp)
    {
        setAnnotation(_annotation);
        setId(_id);
        setKeyFlags(_keyFlags);
        setKeyRegexp(_keyRegexp);
        setOwner(_owner);
        setValueFlags(_valueFlags);
        setValueRegexp(_valueRegexp);
    }


    @Accessor(qualifier = "keyFlags", type = Accessor.Type.GETTER)
    public Set<RegexpFlag> getKeyFlags()
    {
        return (Set<RegexpFlag>)getPersistenceContext().getPropertyValue("keyFlags");
    }


    @Accessor(qualifier = "keyRegexp", type = Accessor.Type.GETTER)
    public String getKeyRegexp()
    {
        return (String)getPersistenceContext().getPropertyValue("keyRegexp");
    }


    @Accessor(qualifier = "valueFlags", type = Accessor.Type.GETTER)
    public Set<RegexpFlag> getValueFlags()
    {
        return (Set<RegexpFlag>)getPersistenceContext().getPropertyValue("valueFlags");
    }


    @Accessor(qualifier = "valueRegexp", type = Accessor.Type.GETTER)
    public String getValueRegexp()
    {
        return (String)getPersistenceContext().getPropertyValue("valueRegexp");
    }


    @Accessor(qualifier = "keyFlags", type = Accessor.Type.SETTER)
    public void setKeyFlags(Set<RegexpFlag> value)
    {
        getPersistenceContext().setPropertyValue("keyFlags", value);
    }


    @Accessor(qualifier = "keyRegexp", type = Accessor.Type.SETTER)
    public void setKeyRegexp(String value)
    {
        getPersistenceContext().setPropertyValue("keyRegexp", value);
    }


    @Accessor(qualifier = "valueFlags", type = Accessor.Type.SETTER)
    public void setValueFlags(Set<RegexpFlag> value)
    {
        getPersistenceContext().setPropertyValue("valueFlags", value);
    }


    @Accessor(qualifier = "valueRegexp", type = Accessor.Type.SETTER)
    public void setValueRegexp(String value)
    {
        getPersistenceContext().setPropertyValue("valueRegexp", value);
    }
}
