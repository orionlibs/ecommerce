package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class MediaFolderModel extends ItemModel
{
    public static final String _TYPECODE = "MediaFolder";
    public static final String QUALIFIER = "qualifier";
    public static final String PATH = "path";


    public MediaFolderModel()
    {
    }


    public MediaFolderModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaFolderModel(String _qualifier)
    {
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaFolderModel(ItemModel _owner, String _path, String _qualifier)
    {
        setOwner(_owner);
        setPath(_path);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "path", type = Accessor.Type.GETTER)
    public String getPath()
    {
        return (String)getPersistenceContext().getPropertyValue("path");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "path", type = Accessor.Type.SETTER)
    public void setPath(String value)
    {
        getPersistenceContext().setPropertyValue("path", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }
}
