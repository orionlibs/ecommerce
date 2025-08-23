package de.hybris.platform.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractDynamicContentModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractDynamicContent";
    public static final String CODE = "code";
    public static final String ACTIVE = "active";
    public static final String CHECKSUM = "checksum";
    public static final String CONTENT = "content";
    public static final String VERSION = "version";


    public AbstractDynamicContentModel()
    {
    }


    public AbstractDynamicContentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractDynamicContentModel(String _code, String _content)
    {
        setCode(_code);
        setContent(_content);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractDynamicContentModel(String _code, String _content, ItemModel _owner)
    {
        setCode(_code);
        setContent(_content);
        setOwner(_owner);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "checksum", type = Accessor.Type.GETTER)
    public String getChecksum()
    {
        return (String)getPersistenceContext().getPropertyValue("checksum");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "content", type = Accessor.Type.GETTER)
    public String getContent()
    {
        return (String)getPersistenceContext().getPropertyValue("content");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public Long getVersion()
    {
        return (Long)getPersistenceContext().getPropertyValue("version");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "checksum", type = Accessor.Type.SETTER)
    public void setChecksum(String value)
    {
        getPersistenceContext().setPropertyValue("checksum", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.SETTER)
    public void setContent(String value)
    {
        getPersistenceContext().setPropertyValue("content", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(Long value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }
}
