package de.hybris.platform.commons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class DocumentModel extends MediaModel
{
    public static final String _TYPECODE = "Document";
    public static final String _ITEMDOCRRELATION = "ItemDocrRelation";
    public static final String ITEMTIMESTAMP = "itemTimestamp";
    public static final String FORMAT = "format";
    public static final String SOURCEITEM = "sourceItem";


    public DocumentModel()
    {
    }


    public DocumentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DocumentModel(CatalogVersionModel _catalogVersion, String _code, FormatModel _format)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setFormat(_format);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DocumentModel(CatalogVersionModel _catalogVersion, String _code, FormatModel _format, ItemModel _owner, ItemModel _sourceItem)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setFormat(_format);
        setOwner(_owner);
        setSourceItem(_sourceItem);
    }


    @Accessor(qualifier = "format", type = Accessor.Type.GETTER)
    public FormatModel getFormat()
    {
        return (FormatModel)getPersistenceContext().getPropertyValue("format");
    }


    @Accessor(qualifier = "itemTimestamp", type = Accessor.Type.GETTER)
    public Date getItemTimestamp()
    {
        return (Date)getPersistenceContext().getPropertyValue("itemTimestamp");
    }


    @Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
    public ItemModel getSourceItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("sourceItem");
    }


    @Accessor(qualifier = "format", type = Accessor.Type.SETTER)
    public void setFormat(FormatModel value)
    {
        getPersistenceContext().setPropertyValue("format", value);
    }


    @Accessor(qualifier = "itemTimestamp", type = Accessor.Type.SETTER)
    public void setItemTimestamp(Date value)
    {
        getPersistenceContext().setPropertyValue("itemTimestamp", value);
    }


    @Accessor(qualifier = "sourceItem", type = Accessor.Type.SETTER)
    public void setSourceItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("sourceItem", value);
    }
}
