package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.enums.Severity;
import java.util.Locale;
import java.util.Set;

public class AbstractConstraintModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractConstraint";
    public static final String ID = "id";
    public static final String ACTIVE = "active";
    public static final String NEEDRELOAD = "needReload";
    public static final String ANNOTATION = "annotation";
    public static final String SEVERITY = "severity";
    public static final String DEFAULTMESSAGE = "defaultMessage";
    public static final String MESSAGE = "message";
    public static final String TARGET = "target";
    public static final String TYPE = "type";
    public static final String CONSTRAINTGROUPS = "constraintGroups";


    public AbstractConstraintModel()
    {
    }


    public AbstractConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractConstraintModel(Class _annotation, String _id)
    {
        setAnnotation(_annotation);
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractConstraintModel(Class _annotation, String _id, ItemModel _owner)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "annotation", type = Accessor.Type.GETTER)
    public Class getAnnotation()
    {
        return (Class)getPersistenceContext().getPropertyValue("annotation");
    }


    @Accessor(qualifier = "constraintGroups", type = Accessor.Type.GETTER)
    public Set<ConstraintGroupModel> getConstraintGroups()
    {
        return (Set<ConstraintGroupModel>)getPersistenceContext().getPropertyValue("constraintGroups");
    }


    @Accessor(qualifier = "defaultMessage", type = Accessor.Type.GETTER)
    public String getDefaultMessage()
    {
        return getDefaultMessage(null);
    }


    @Accessor(qualifier = "defaultMessage", type = Accessor.Type.GETTER)
    public String getDefaultMessage(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("defaultMessage", loc);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public String getMessage()
    {
        return getMessage(null);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public String getMessage(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("message", loc);
    }


    @Accessor(qualifier = "severity", type = Accessor.Type.GETTER)
    public Severity getSeverity()
    {
        return (Severity)getPersistenceContext().getPropertyValue("severity");
    }


    @Accessor(qualifier = "target", type = Accessor.Type.GETTER)
    public Class getTarget()
    {
        return (Class)getPersistenceContext().getPropertyValue("target");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public ComposedTypeModel getType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public boolean isActive()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("active"));
    }


    @Accessor(qualifier = "needReload", type = Accessor.Type.GETTER)
    public boolean isNeedReload()
    {
        return toPrimitive((Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "needReload"));
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(boolean value)
    {
        getPersistenceContext().setPropertyValue("active", toObject(value));
    }


    @Accessor(qualifier = "annotation", type = Accessor.Type.SETTER)
    public void setAnnotation(Class value)
    {
        getPersistenceContext().setPropertyValue("annotation", value);
    }


    @Accessor(qualifier = "constraintGroups", type = Accessor.Type.SETTER)
    public void setConstraintGroups(Set<ConstraintGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("constraintGroups", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(String value)
    {
        setMessage(value, null);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("message", loc, value);
    }


    @Accessor(qualifier = "severity", type = Accessor.Type.SETTER)
    public void setSeverity(Severity value)
    {
        getPersistenceContext().setPropertyValue("severity", value);
    }


    @Accessor(qualifier = "target", type = Accessor.Type.SETTER)
    public void setTarget(Class value)
    {
        getPersistenceContext().setPropertyValue("target", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }
}
