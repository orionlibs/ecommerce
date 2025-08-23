package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.warehousing.util.builder.PriceRowBuilder;
import de.hybris.platform.warehousing.util.dao.impl.PriceRowWarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public class PriceRows extends AbstractItems<PriceRowModel>
{
    public static final Double CAMERA_PRICE = Double.valueOf(23.0D);
    public static final Double MEMORY_CARD_PRICE = Double.valueOf(17.0D);
    public static final Double LENS_PRICE = Double.valueOf(13.0D);
    private PriceRowWarehousingDao priceRowWarehousingDao;
    private Currencies currencies;
    private Units units;
    private Catalogs catalogs;
    private Customers customers;


    public PriceRowModel CameraPrice(String productId)
    {
        return (PriceRowModel)getOrSaveAndReturn(() -> (PriceRowModel)getPriceRowWarehousingDao().getByCode(productId),
                        () -> PriceRowBuilder.aModel().withCurrency(getCurrencies().AmericanDollar()).withPrice(CAMERA_PRICE).withProductId(productId).withUnit(getUnits().Unit()).withCatalogVersion(getCatalogs().Primary().getActiveCatalogVersion()).withUser(getCustomers().polo()).build());
    }


    public PriceRowModel MemoryCardPrice(String productId)
    {
        return (PriceRowModel)getOrSaveAndReturn(() -> (PriceRowModel)getPriceRowWarehousingDao().getByCode(productId),
                        () -> PriceRowBuilder.aModel().withCurrency(getCurrencies().AmericanDollar()).withPrice(MEMORY_CARD_PRICE).withProductId(productId).withUnit(getUnits().Unit()).withCatalogVersion(getCatalogs().Primary().getActiveCatalogVersion()).withUser(getCustomers().polo()).build());
    }


    public PriceRowModel LensPrice(String productId)
    {
        return (PriceRowModel)getOrSaveAndReturn(() -> (PriceRowModel)getPriceRowWarehousingDao().getByCode(productId),
                        () -> PriceRowBuilder.aModel().withCurrency(getCurrencies().AmericanDollar()).withPrice(LENS_PRICE).withProductId(productId).withUnit(getUnits().Unit()).withCatalogVersion(getCatalogs().Primary().getActiveCatalogVersion()).withUser(getCustomers().polo()).build());
    }


    protected PriceRowWarehousingDao getPriceRowWarehousingDao()
    {
        return this.priceRowWarehousingDao;
    }


    @Required
    public void setPriceRowWarehousingDao(PriceRowWarehousingDao priceRowWarehousingDao)
    {
        this.priceRowWarehousingDao = priceRowWarehousingDao;
    }


    protected Currencies getCurrencies()
    {
        return this.currencies;
    }


    @Required
    public void setCurrencies(Currencies currencies)
    {
        this.currencies = currencies;
    }


    protected Units getUnits()
    {
        return this.units;
    }


    @Required
    public void setUnits(Units units)
    {
        this.units = units;
    }


    protected Customers getCustomers()
    {
        return this.customers;
    }


    @Required
    public void setCustomers(Customers customers)
    {
        this.customers = customers;
    }


    protected Catalogs getCatalogs()
    {
        return this.catalogs;
    }


    @Required
    public void setCatalogs(Catalogs catalogs)
    {
        this.catalogs = catalogs;
    }
}
