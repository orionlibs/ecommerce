package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.List;

public class CurrencyModel extends C2LItemModel
{
    public static final String _TYPECODE = "Currency";
    public static final String _SOLRFACETSEARCHCONFIG2CURRENCYRELATION = "SolrFacetSearchConfig2CurrencyRelation";
    public static final String _BASESTORE2CURRENCYREL = "BaseStore2CurrencyRel";
    public static final String BASE = "base";
    public static final String CONVERSION = "conversion";
    public static final String DIGITS = "digits";
    public static final String SYMBOL = "symbol";
    public static final String FACETSEARCHCONFIGS = "facetSearchConfigs";
    public static final String BASESTORES = "baseStores";
    public static final String SAPCODE = "sapCode";


    public CurrencyModel()
    {
    }


    public CurrencyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CurrencyModel(String _isocode, String _symbol)
    {
        setIsocode(_isocode);
        setSymbol(_symbol);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CurrencyModel(String _isocode, ItemModel _owner, String _symbol)
    {
        setIsocode(_isocode);
        setOwner(_owner);
        setSymbol(_symbol);
    }


    @Accessor(qualifier = "base", type = Accessor.Type.GETTER)
    public Boolean getBase()
    {
        Boolean value = (Boolean)getPersistenceContext().getPropertyValue("base");
        return (value != null) ? value : Boolean.valueOf(false);
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
    public Collection<BaseStoreModel> getBaseStores()
    {
        return (Collection<BaseStoreModel>)getPersistenceContext().getPropertyValue("baseStores");
    }


    @Accessor(qualifier = "conversion", type = Accessor.Type.GETTER)
    public Double getConversion()
    {
        Double value = (Double)getPersistenceContext().getPropertyValue("conversion");
        return (value != null) ? value : Double.valueOf(0.0D);
    }


    @Accessor(qualifier = "digits", type = Accessor.Type.GETTER)
    public Integer getDigits()
    {
        Integer value = (Integer)getPersistenceContext().getPropertyValue("digits");
        return (value != null) ? value : Integer.valueOf(0);
    }


    @Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.GETTER)
    public List<SolrFacetSearchConfigModel> getFacetSearchConfigs()
    {
        return (List<SolrFacetSearchConfigModel>)getPersistenceContext().getPropertyValue("facetSearchConfigs");
    }


    @Accessor(qualifier = "sapCode", type = Accessor.Type.GETTER)
    public String getSapCode()
    {
        return (String)getPersistenceContext().getPropertyValue("sapCode");
    }


    @Accessor(qualifier = "symbol", type = Accessor.Type.GETTER)
    public String getSymbol()
    {
        return (String)getPersistenceContext().getPropertyValue("symbol");
    }


    @Accessor(qualifier = "base", type = Accessor.Type.SETTER)
    public void setBase(Boolean value)
    {
        getPersistenceContext().setPropertyValue("base", value);
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
    public void setBaseStores(Collection<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("baseStores", value);
    }


    @Accessor(qualifier = "conversion", type = Accessor.Type.SETTER)
    public void setConversion(Double value)
    {
        getPersistenceContext().setPropertyValue("conversion", value);
    }


    @Accessor(qualifier = "digits", type = Accessor.Type.SETTER)
    public void setDigits(Integer value)
    {
        getPersistenceContext().setPropertyValue("digits", value);
    }


    @Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.SETTER)
    public void setFacetSearchConfigs(List<SolrFacetSearchConfigModel> value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfigs", value);
    }


    @Accessor(qualifier = "sapCode", type = Accessor.Type.SETTER)
    public void setSapCode(String value)
    {
        getPersistenceContext().setPropertyValue("sapCode", value);
    }


    @Accessor(qualifier = "symbol", type = Accessor.Type.SETTER)
    public void setSymbol(String value)
    {
        getPersistenceContext().setPropertyValue("symbol", value);
    }
}
