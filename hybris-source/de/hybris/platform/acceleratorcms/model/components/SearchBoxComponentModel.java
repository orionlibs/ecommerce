package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SearchBoxComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "SearchBoxComponent";
    public static final String DISPLAYSUGGESTIONS = "displaySuggestions";
    public static final String DISPLAYPRODUCTS = "displayProducts";
    public static final String DISPLAYPRODUCTIMAGES = "displayProductImages";
    public static final String MAXSUGGESTIONS = "maxSuggestions";
    public static final String MAXPRODUCTS = "maxProducts";
    public static final String MINCHARACTERSBEFOREREQUEST = "minCharactersBeforeRequest";
    public static final String WAITTIMEBEFOREREQUEST = "waitTimeBeforeRequest";


    public SearchBoxComponentModel()
    {
    }


    public SearchBoxComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SearchBoxComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SearchBoxComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "maxProducts", type = Accessor.Type.GETTER)
    public Integer getMaxProducts()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxProducts");
    }


    @Accessor(qualifier = "maxSuggestions", type = Accessor.Type.GETTER)
    public Integer getMaxSuggestions()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxSuggestions");
    }


    @Accessor(qualifier = "minCharactersBeforeRequest", type = Accessor.Type.GETTER)
    public Integer getMinCharactersBeforeRequest()
    {
        return (Integer)getPersistenceContext().getPropertyValue("minCharactersBeforeRequest");
    }


    @Accessor(qualifier = "waitTimeBeforeRequest", type = Accessor.Type.GETTER)
    public Integer getWaitTimeBeforeRequest()
    {
        return (Integer)getPersistenceContext().getPropertyValue("waitTimeBeforeRequest");
    }


    @Accessor(qualifier = "displayProductImages", type = Accessor.Type.GETTER)
    public boolean isDisplayProductImages()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("displayProductImages"));
    }


    @Accessor(qualifier = "displayProducts", type = Accessor.Type.GETTER)
    public boolean isDisplayProducts()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("displayProducts"));
    }


    @Accessor(qualifier = "displaySuggestions", type = Accessor.Type.GETTER)
    public boolean isDisplaySuggestions()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("displaySuggestions"));
    }


    @Accessor(qualifier = "displayProductImages", type = Accessor.Type.SETTER)
    public void setDisplayProductImages(boolean value)
    {
        getPersistenceContext().setPropertyValue("displayProductImages", toObject(value));
    }


    @Accessor(qualifier = "displayProducts", type = Accessor.Type.SETTER)
    public void setDisplayProducts(boolean value)
    {
        getPersistenceContext().setPropertyValue("displayProducts", toObject(value));
    }


    @Accessor(qualifier = "displaySuggestions", type = Accessor.Type.SETTER)
    public void setDisplaySuggestions(boolean value)
    {
        getPersistenceContext().setPropertyValue("displaySuggestions", toObject(value));
    }


    @Accessor(qualifier = "maxProducts", type = Accessor.Type.SETTER)
    public void setMaxProducts(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxProducts", value);
    }


    @Accessor(qualifier = "maxSuggestions", type = Accessor.Type.SETTER)
    public void setMaxSuggestions(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxSuggestions", value);
    }


    @Accessor(qualifier = "minCharactersBeforeRequest", type = Accessor.Type.SETTER)
    public void setMinCharactersBeforeRequest(Integer value)
    {
        getPersistenceContext().setPropertyValue("minCharactersBeforeRequest", value);
    }


    @Accessor(qualifier = "waitTimeBeforeRequest", type = Accessor.Type.SETTER)
    public void setWaitTimeBeforeRequest(Integer value)
    {
        getPersistenceContext().setPropertyValue("waitTimeBeforeRequest", value);
    }
}
