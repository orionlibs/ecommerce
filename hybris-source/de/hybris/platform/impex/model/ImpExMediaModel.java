package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ImpExMediaModel extends MediaModel
{
    public static final String _TYPECODE = "ImpExMedia";
    public static final String FIELDSEPARATOR = "fieldSeparator";
    public static final String QUOTECHARACTER = "quoteCharacter";
    public static final String COMMENTCHARACTER = "commentCharacter";
    public static final String ENCODING = "encoding";
    public static final String LINESTOSKIP = "linesToSkip";
    public static final String REMOVEONSUCCESS = "removeOnSuccess";
    public static final String ZIPENTRY = "zipentry";
    public static final String PREVIEW = "preview";
    public static final String EXTRACTIONID = "extractionId";


    public ImpExMediaModel()
    {
    }


    public ImpExMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExMediaModel(CatalogVersionModel _catalogVersion, String _code, int _linesToSkip, boolean _removeOnSuccess)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setLinesToSkip(_linesToSkip);
        setRemoveOnSuccess(_removeOnSuccess);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExMediaModel(CatalogVersionModel _catalogVersion, String _code, String _extractionId, int _linesToSkip, ItemModel _owner, boolean _removeOnSuccess)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setExtractionId(_extractionId);
        setLinesToSkip(_linesToSkip);
        setOwner(_owner);
        setRemoveOnSuccess(_removeOnSuccess);
    }


    @Accessor(qualifier = "commentCharacter", type = Accessor.Type.GETTER)
    public Character getCommentCharacter()
    {
        return (Character)getPersistenceContext().getPropertyValue("commentCharacter");
    }


    @Accessor(qualifier = "encoding", type = Accessor.Type.GETTER)
    public EncodingEnum getEncoding()
    {
        return (EncodingEnum)getPersistenceContext().getPropertyValue("encoding");
    }


    @Accessor(qualifier = "extractionId", type = Accessor.Type.GETTER)
    public String getExtractionId()
    {
        return (String)getPersistenceContext().getPropertyValue("extractionId");
    }


    @Accessor(qualifier = "fieldSeparator", type = Accessor.Type.GETTER)
    public Character getFieldSeparator()
    {
        return (Character)getPersistenceContext().getPropertyValue("fieldSeparator");
    }


    @Accessor(qualifier = "linesToSkip", type = Accessor.Type.GETTER)
    public int getLinesToSkip()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("linesToSkip"));
    }


    @Accessor(qualifier = "preview", type = Accessor.Type.GETTER)
    public String getPreview()
    {
        return (String)getPersistenceContext().getPropertyValue("preview");
    }


    @Accessor(qualifier = "quoteCharacter", type = Accessor.Type.GETTER)
    public Character getQuoteCharacter()
    {
        return (Character)getPersistenceContext().getPropertyValue("quoteCharacter");
    }


    @Accessor(qualifier = "zipentry", type = Accessor.Type.GETTER)
    public String getZipentry()
    {
        return (String)getPersistenceContext().getPropertyValue("zipentry");
    }


    @Accessor(qualifier = "removeOnSuccess", type = Accessor.Type.GETTER)
    public boolean isRemoveOnSuccess()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("removeOnSuccess"));
    }


    @Accessor(qualifier = "commentCharacter", type = Accessor.Type.SETTER)
    public void setCommentCharacter(Character value)
    {
        getPersistenceContext().setPropertyValue("commentCharacter", value);
    }


    @Accessor(qualifier = "encoding", type = Accessor.Type.SETTER)
    public void setEncoding(EncodingEnum value)
    {
        getPersistenceContext().setPropertyValue("encoding", value);
    }


    @Accessor(qualifier = "extractionId", type = Accessor.Type.SETTER)
    public void setExtractionId(String value)
    {
        getPersistenceContext().setPropertyValue("extractionId", value);
    }


    @Accessor(qualifier = "fieldSeparator", type = Accessor.Type.SETTER)
    public void setFieldSeparator(Character value)
    {
        getPersistenceContext().setPropertyValue("fieldSeparator", value);
    }


    @Accessor(qualifier = "linesToSkip", type = Accessor.Type.SETTER)
    public void setLinesToSkip(int value)
    {
        getPersistenceContext().setPropertyValue("linesToSkip", toObject(value));
    }


    @Accessor(qualifier = "quoteCharacter", type = Accessor.Type.SETTER)
    public void setQuoteCharacter(Character value)
    {
        getPersistenceContext().setPropertyValue("quoteCharacter", value);
    }


    @Accessor(qualifier = "removeOnSuccess", type = Accessor.Type.SETTER)
    public void setRemoveOnSuccess(boolean value)
    {
        getPersistenceContext().setPropertyValue("removeOnSuccess", toObject(value));
    }


    @Accessor(qualifier = "zipentry", type = Accessor.Type.SETTER)
    public void setZipentry(String value)
    {
        getPersistenceContext().setPropertyValue("zipentry", value);
    }
}
