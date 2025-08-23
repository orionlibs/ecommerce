package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class MediaFormatMappingModel extends ItemModel
{
    public static final String _TYPECODE = "MediaFormatMapping";
    public static final String _MEDIACONTEXT2MEDIAFORMATMAPPINGREL = "MediaContext2MediaFormatMappingRel";
    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    public static final String MEDIACONTEXT = "mediaContext";


    public MediaFormatMappingModel()
    {
    }


    public MediaFormatMappingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaFormatMappingModel(MediaContextModel _mediaContext, MediaFormatModel _source, MediaFormatModel _target)
    {
        setMediaContext(_mediaContext);
        setSource(_source);
        setTarget(_target);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaFormatMappingModel(MediaContextModel _mediaContext, ItemModel _owner, MediaFormatModel _source, MediaFormatModel _target)
    {
        setMediaContext(_mediaContext);
        setOwner(_owner);
        setSource(_source);
        setTarget(_target);
    }


    @Accessor(qualifier = "mediaContext", type = Accessor.Type.GETTER)
    public MediaContextModel getMediaContext()
    {
        return (MediaContextModel)getPersistenceContext().getPropertyValue("mediaContext");
    }


    @Accessor(qualifier = "source", type = Accessor.Type.GETTER)
    public MediaFormatModel getSource()
    {
        return (MediaFormatModel)getPersistenceContext().getPropertyValue("source");
    }


    @Accessor(qualifier = "target", type = Accessor.Type.GETTER)
    public MediaFormatModel getTarget()
    {
        return (MediaFormatModel)getPersistenceContext().getPropertyValue("target");
    }


    @Accessor(qualifier = "mediaContext", type = Accessor.Type.SETTER)
    public void setMediaContext(MediaContextModel value)
    {
        getPersistenceContext().setPropertyValue("mediaContext", value);
    }


    @Accessor(qualifier = "source", type = Accessor.Type.SETTER)
    public void setSource(MediaFormatModel value)
    {
        getPersistenceContext().setPropertyValue("source", value);
    }


    @Accessor(qualifier = "target", type = Accessor.Type.SETTER)
    public void setTarget(MediaFormatModel value)
    {
        getPersistenceContext().setPropertyValue("target", value);
    }
}
