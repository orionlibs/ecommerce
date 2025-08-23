package de.hybris.platform.core.model.enumeration;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class EnumerationValueModel extends ItemModel
{
    public static final String _TYPECODE = "EnumerationValue";
    public static final String CODE = "code";
    public static final String CODELOWERCASE = "codeLowerCase";
    public static final String NAME = "name";
    public static final String SEQUENCENUMBER = "sequenceNumber";
    public static final String DEPRECATED = "deprecated";
    public static final String EXTENSIONNAME = "extensionName";
    public static final String ICON = "icon";


    public EnumerationValueModel()
    {
    }


    public EnumerationValueModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EnumerationValueModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EnumerationValueModel(String _code, String _codeLowerCase, ItemModel _owner)
    {
        setCode(_code);
        setCodeLowerCase(_codeLowerCase);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "codeLowerCase", type = Accessor.Type.GETTER)
    public String getCodeLowerCase()
    {
        return (String)getPersistenceContext().getPropertyValue("codeLowerCase");
    }


    @Accessor(qualifier = "deprecated", type = Accessor.Type.GETTER)
    public Boolean getDeprecated()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("deprecated");
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.GETTER)
    public String getExtensionName()
    {
        return (String)getPersistenceContext().getPropertyValue("extensionName");
    }


    @Accessor(qualifier = "icon", type = Accessor.Type.GETTER)
    public MediaModel getIcon()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("icon");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
    public Integer getSequenceNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("sequenceNumber");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "codeLowerCase", type = Accessor.Type.SETTER)
    public void setCodeLowerCase(String value)
    {
        getPersistenceContext().setPropertyValue("codeLowerCase", value);
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.SETTER)
    public void setExtensionName(String value)
    {
        getPersistenceContext().setPropertyValue("extensionName", value);
    }


    @Accessor(qualifier = "icon", type = Accessor.Type.SETTER)
    public void setIcon(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("icon", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
    public void setSequenceNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("sequenceNumber", value);
    }
}
