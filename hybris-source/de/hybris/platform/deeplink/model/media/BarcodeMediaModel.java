package de.hybris.platform.deeplink.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.BarcodeType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class BarcodeMediaModel extends MediaModel
{
    public static final String _TYPECODE = "BarcodeMedia";
    public static final String BARCODETEXT = "barcodeText";
    public static final String BARCODETYPE = "barcodeType";
    public static final String CONTEXTITEM = "contextItem";
    public static final String DEEPLINKURL = "deeplinkUrl";


    public BarcodeMediaModel()
    {
    }


    public BarcodeMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BarcodeMediaModel(String _barcodeText, BarcodeType _barcodeType, CatalogVersionModel _catalogVersion, String _code)
    {
        setBarcodeText(_barcodeText);
        setBarcodeType(_barcodeType);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BarcodeMediaModel(String _barcodeText, BarcodeType _barcodeType, CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setBarcodeText(_barcodeText);
        setBarcodeType(_barcodeType);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "barcodeText", type = Accessor.Type.GETTER)
    public String getBarcodeText()
    {
        return (String)getPersistenceContext().getPropertyValue("barcodeText");
    }


    @Accessor(qualifier = "barcodeType", type = Accessor.Type.GETTER)
    public BarcodeType getBarcodeType()
    {
        return (BarcodeType)getPersistenceContext().getPropertyValue("barcodeType");
    }


    @Accessor(qualifier = "contextItem", type = Accessor.Type.GETTER)
    public ItemModel getContextItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("contextItem");
    }


    @Accessor(qualifier = "deeplinkUrl", type = Accessor.Type.GETTER)
    public DeeplinkUrlModel getDeeplinkUrl()
    {
        return (DeeplinkUrlModel)getPersistenceContext().getPropertyValue("deeplinkUrl");
    }


    @Accessor(qualifier = "barcodeText", type = Accessor.Type.SETTER)
    public void setBarcodeText(String value)
    {
        getPersistenceContext().setPropertyValue("barcodeText", value);
    }


    @Accessor(qualifier = "barcodeType", type = Accessor.Type.SETTER)
    public void setBarcodeType(BarcodeType value)
    {
        getPersistenceContext().setPropertyValue("barcodeType", value);
    }


    @Accessor(qualifier = "contextItem", type = Accessor.Type.SETTER)
    public void setContextItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("contextItem", value);
    }


    @Accessor(qualifier = "deeplinkUrl", type = Accessor.Type.SETTER)
    public void setDeeplinkUrl(DeeplinkUrlModel value)
    {
        getPersistenceContext().setPropertyValue("deeplinkUrl", value);
    }
}
