package de.hybris.platform.deeplink.model.rules;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.deeplink.model.media.BarcodeMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class DeeplinkUrlModel extends ItemModel
{
    public static final String _TYPECODE = "DeeplinkUrl";
    public static final String _BARCODEMEDIA2DEEPLINKURL = "BarcodeMedia2DeeplinkUrl";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String BASEURL = "baseUrl";
    public static final String BARCODEMEDIAS = "barcodeMedias";


    public DeeplinkUrlModel()
    {
    }


    public DeeplinkUrlModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DeeplinkUrlModel(String _baseUrl, String _code, String _name)
    {
        setBaseUrl(_baseUrl);
        setCode(_code);
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DeeplinkUrlModel(String _baseUrl, String _code, String _name, ItemModel _owner)
    {
        setBaseUrl(_baseUrl);
        setCode(_code);
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "barcodeMedias", type = Accessor.Type.GETTER)
    public Collection<BarcodeMediaModel> getBarcodeMedias()
    {
        return (Collection<BarcodeMediaModel>)getPersistenceContext().getPropertyValue("barcodeMedias");
    }


    @Accessor(qualifier = "baseUrl", type = Accessor.Type.GETTER)
    public String getBaseUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("baseUrl");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "barcodeMedias", type = Accessor.Type.SETTER)
    public void setBarcodeMedias(Collection<BarcodeMediaModel> value)
    {
        getPersistenceContext().setPropertyValue("barcodeMedias", value);
    }


    @Accessor(qualifier = "baseUrl", type = Accessor.Type.SETTER)
    public void setBaseUrl(String value)
    {
        getPersistenceContext().setPropertyValue("baseUrl", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }
}
