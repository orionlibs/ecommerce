package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ImportBatchContentModel extends ItemModel
{
    public static final String _TYPECODE = "ImportBatchContent";
    public static final String CODE = "code";
    public static final String CONTENT = "content";


    public ImportBatchContentModel()
    {
    }


    public ImportBatchContentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImportBatchContentModel(String _code, String _content)
    {
        setCode(_code);
        setContent(_content);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImportBatchContentModel(String _code, String _content, ItemModel _owner)
    {
        setCode(_code);
        setContent(_content);
        setOwner(_owner);
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
}
