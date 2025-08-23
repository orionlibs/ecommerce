package de.hybris.platform.scripting.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ScriptModel extends AbstractDynamicContentModel
{
    public static final String _TYPECODE = "Script";
    public static final String DESCRIPTION = "description";
    public static final String SCRIPTTYPE = "scriptType";
    public static final String AUTODISABLING = "autodisabling";
    public static final String DISABLED = "disabled";


    public ScriptModel()
    {
    }


    public ScriptModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ScriptModel(String _code, String _content)
    {
        setCode(_code);
        setContent(_content);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ScriptModel(String _code, String _content, ItemModel _owner)
    {
        setCode(_code);
        setContent(_content);
        setOwner(_owner);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "scriptType", type = Accessor.Type.GETTER)
    public ScriptType getScriptType()
    {
        return (ScriptType)getPersistenceContext().getPropertyValue("scriptType");
    }


    @Accessor(qualifier = "autodisabling", type = Accessor.Type.GETTER)
    public boolean isAutodisabling()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("autodisabling"));
    }


    @Accessor(qualifier = "disabled", type = Accessor.Type.GETTER)
    public boolean isDisabled()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("disabled"));
    }


    @Accessor(qualifier = "autodisabling", type = Accessor.Type.SETTER)
    public void setAutodisabling(boolean value)
    {
        getPersistenceContext().setPropertyValue("autodisabling", toObject(value));
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "disabled", type = Accessor.Type.SETTER)
    public void setDisabled(boolean value)
    {
        getPersistenceContext().setPropertyValue("disabled", toObject(value));
    }


    @Accessor(qualifier = "scriptType", type = Accessor.Type.SETTER)
    public void setScriptType(ScriptType value)
    {
        getPersistenceContext().setPropertyValue("scriptType", value);
    }
}
