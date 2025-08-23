package de.hybris.platform.europe1.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PriceRowModel extends PDTRowModel
{
    public static final String _TYPECODE = "PriceRow";
    public static final String _PRODUCT2OWNEUROPE1PRICES = "Product2OwnEurope1Prices";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String MATCHVALUE = "matchValue";
    public static final String CURRENCY = "currency";
    public static final String MINQTD = "minqtd";
    public static final String NET = "net";
    public static final String PRICE = "price";
    public static final String UNIT = "unit";
    public static final String UNITFACTOR = "unitFactor";
    public static final String GIVEAWAYPRICE = "giveAwayPrice";
    public static final String CHANNEL = "channel";
    public static final String SEQUENCEID = "sequenceId";
    public static final String SAPCONDITIONID = "sapConditionId";


    public PriceRowModel()
    {
    }


    public PriceRowModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PriceRowModel(CurrencyModel _currency, Double _price, UnitModel _unit)
    {
        setCurrency(_currency);
        setPrice(_price);
        setUnit(_unit);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PriceRowModel(CurrencyModel _currency, ItemModel _owner, ProductPriceGroup _pg, Double _price, ProductModel _product, String _productId, UnitModel _unit)
    {
        setCurrency(_currency);
        setOwner(_owner);
        setPg((HybrisEnumValue)_pg);
        setPrice(_price);
        setProduct(_product);
        setProductId(_productId);
        setUnit(_unit);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "channel", type = Accessor.Type.GETTER)
    public PriceRowChannel getChannel()
    {
        return (PriceRowChannel)getPersistenceContext().getPropertyValue("channel");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "giveAwayPrice", type = Accessor.Type.GETTER)
    public Boolean getGiveAwayPrice()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("giveAwayPrice");
    }


    @Accessor(qualifier = "matchValue", type = Accessor.Type.GETTER)
    public Integer getMatchValue()
    {
        return (Integer)getPersistenceContext().getPropertyValue("matchValue");
    }


    @Accessor(qualifier = "minqtd", type = Accessor.Type.GETTER)
    public Long getMinqtd()
    {
        return (Long)getPersistenceContext().getPropertyValue("minqtd");
    }


    @Accessor(qualifier = "net", type = Accessor.Type.GETTER)
    public Boolean getNet()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("net");
    }


    @Accessor(qualifier = "price", type = Accessor.Type.GETTER)
    public Double getPrice()
    {
        return (Double)getPersistenceContext().getPropertyValue("price");
    }


    @Accessor(qualifier = "sapConditionId", type = Accessor.Type.GETTER)
    public String getSapConditionId()
    {
        return (String)getPersistenceContext().getPropertyValue("sapConditionId");
    }


    @Accessor(qualifier = "sequenceId", type = Accessor.Type.GETTER)
    public Long getSequenceId()
    {
        return (Long)getPersistenceContext().getPropertyValue("sequenceId");
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
    public UnitModel getUnit()
    {
        return (UnitModel)getPersistenceContext().getPropertyValue("unit");
    }


    @Accessor(qualifier = "unitFactor", type = Accessor.Type.GETTER)
    public Integer getUnitFactor()
    {
        return (Integer)getPersistenceContext().getPropertyValue("unitFactor");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "channel", type = Accessor.Type.SETTER)
    public void setChannel(PriceRowChannel value)
    {
        getPersistenceContext().setPropertyValue("channel", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "giveAwayPrice", type = Accessor.Type.SETTER)
    public void setGiveAwayPrice(Boolean value)
    {
        getPersistenceContext().setPropertyValue("giveAwayPrice", value);
    }


    @Accessor(qualifier = "matchValue", type = Accessor.Type.SETTER)
    public void setMatchValue(Integer value)
    {
        getPersistenceContext().setPropertyValue("matchValue", value);
    }


    @Accessor(qualifier = "minqtd", type = Accessor.Type.SETTER)
    public void setMinqtd(Long value)
    {
        getPersistenceContext().setPropertyValue("minqtd", value);
    }


    @Accessor(qualifier = "net", type = Accessor.Type.SETTER)
    public void setNet(Boolean value)
    {
        getPersistenceContext().setPropertyValue("net", value);
    }


    @Accessor(qualifier = "pg", type = Accessor.Type.SETTER)
    public void setPg(HybrisEnumValue value)
    {
        if(value == null || value instanceof ProductPriceGroup)
        {
            super.setPg(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.europe1.enums.ProductPriceGroup");
        }
    }


    @Accessor(qualifier = "price", type = Accessor.Type.SETTER)
    public void setPrice(Double value)
    {
        getPersistenceContext().setPropertyValue("price", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        super.setProduct(value);
    }


    @Accessor(qualifier = "sapConditionId", type = Accessor.Type.SETTER)
    public void setSapConditionId(String value)
    {
        getPersistenceContext().setPropertyValue("sapConditionId", value);
    }


    @Accessor(qualifier = "sequenceId", type = Accessor.Type.SETTER)
    public void setSequenceId(Long value)
    {
        getPersistenceContext().setPropertyValue("sequenceId", value);
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
    public void setUnit(UnitModel value)
    {
        getPersistenceContext().setPropertyValue("unit", value);
    }


    @Accessor(qualifier = "unitFactor", type = Accessor.Type.SETTER)
    public void setUnitFactor(Integer value)
    {
        getPersistenceContext().setPropertyValue("unitFactor", value);
    }
}
