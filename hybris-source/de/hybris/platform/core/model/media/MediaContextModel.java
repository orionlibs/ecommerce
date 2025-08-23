package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class MediaContextModel extends ItemModel
{
    public static final String _TYPECODE = "MediaContext";
    public static final String QUALIFIER = "qualifier";
    public static final String NAME = "name";
    public static final String MAPPINGS = "mappings";


    public MediaContextModel()
    {
    }


    public MediaContextModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaContextModel(String _qualifier)
    {
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaContextModel(ItemModel _owner, String _qualifier)
    {
        setOwner(_owner);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "mappings", type = Accessor.Type.GETTER)
    public Collection<MediaFormatMappingModel> getMappings()
    {
        return (Collection<MediaFormatMappingModel>)getPersistenceContext().getPropertyValue("mappings");
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


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "mappings", type = Accessor.Type.SETTER)
    public void setMappings(Collection<MediaFormatMappingModel> value)
    {
        getPersistenceContext().setPropertyValue("mappings", value);
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


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }
}
