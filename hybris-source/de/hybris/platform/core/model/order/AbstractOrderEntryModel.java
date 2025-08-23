package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.ProductTaxGroup;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.sap.sapmodel.model.SAPPricingConditionModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class AbstractOrderEntryModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractOrderEntry";
    public static final String _ABSTRACTORDER2ABSTRACTORDERENTRY = "AbstractOrder2AbstractOrderEntry";
    public static final String _CONSIGNMENTENTRYORDERENTRYRELATION = "ConsignmentEntryOrderEntryRelation";
    public static final String BASEPRICE = "basePrice";
    public static final String CALCULATED = "calculated";
    public static final String DISCOUNTVALUESINTERNAL = "discountValuesInternal";
    public static final String DISCOUNTVALUES = "discountValues";
    public static final String ENTRYNUMBER = "entryNumber";
    public static final String INFO = "info";
    public static final String PRODUCT = "product";
    public static final String QUANTITY = "quantity";
    public static final String TAXVALUES = "taxValues";
    public static final String TAXVALUESINTERNAL = "taxValuesInternal";
    public static final String TOTALPRICE = "totalPrice";
    public static final String UNIT = "unit";
    public static final String GIVEAWAY = "giveAway";
    public static final String REJECTED = "rejected";
    public static final String ENTRYGROUPNUMBERS = "entryGroupNumbers";
    public static final String ORDER = "order";
    public static final String PRODUCTINFOS = "productInfos";
    public static final String EUROPE1PRICEFACTORY_PPG = "Europe1PriceFactory_PPG";
    public static final String EUROPE1PRICEFACTORY_PTG = "Europe1PriceFactory_PTG";
    public static final String EUROPE1PRICEFACTORY_PDG = "Europe1PriceFactory_PDG";
    public static final String CHOSENVENDOR = "chosenVendor";
    public static final String DELIVERYADDRESS = "deliveryAddress";
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String NAMEDDELIVERYDATE = "namedDeliveryDate";
    public static final String QUANTITYSTATUS = "quantityStatus";
    public static final String CONSIGNMENTENTRIES = "consignmentEntries";
    public static final String DELIVERYPOINTOFSERVICE = "deliveryPointOfService";
    public static final String COSTCENTER = "costCenter";
    public static final String CONTRACTPRICE = "contractPrice";
    public static final String TIMETOLIVE = "timeToLive";
    public static final String FREIGHTPRICE = "freightPrice";
    public static final String EXTERNALCONFIGURATION = "externalConfiguration";
    public static final String SAPPRICINGCONDITIONS = "sapPricingConditions";


    public AbstractOrderEntryModel()
    {
    }


    public AbstractOrderEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractOrderEntryModel(ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractOrderEntryModel(ItemModel _owner, ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setOwner(_owner);
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Accessor(qualifier = "basePrice", type = Accessor.Type.GETTER)
    public Double getBasePrice()
    {
        return (Double)getPersistenceContext().getPropertyValue("basePrice");
    }


    @Accessor(qualifier = "calculated", type = Accessor.Type.GETTER)
    public Boolean getCalculated()
    {
        Boolean value = (Boolean)getPersistenceContext().getPropertyValue("calculated");
        return (value != null) ? value : Boolean.valueOf(false);
    }


    @Accessor(qualifier = "chosenVendor", type = Accessor.Type.GETTER)
    public VendorModel getChosenVendor()
    {
        return (VendorModel)getPersistenceContext().getPropertyValue("chosenVendor");
    }


    @Accessor(qualifier = "consignmentEntries", type = Accessor.Type.GETTER)
    public Set<ConsignmentEntryModel> getConsignmentEntries()
    {
        return (Set<ConsignmentEntryModel>)getPersistenceContext().getPropertyValue("consignmentEntries");
    }


    @Accessor(qualifier = "contractPrice", type = Accessor.Type.GETTER)
    public Double getContractPrice()
    {
        return (Double)getPersistenceContext().getPropertyValue("contractPrice");
    }


    @Accessor(qualifier = "costCenter", type = Accessor.Type.GETTER)
    public B2BCostCenterModel getCostCenter()
    {
        return (B2BCostCenterModel)getPersistenceContext().getPropertyValue("costCenter");
    }


    @Accessor(qualifier = "deliveryAddress", type = Accessor.Type.GETTER)
    public AddressModel getDeliveryAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("deliveryAddress");
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
    public DeliveryModeModel getDeliveryMode()
    {
        return (DeliveryModeModel)getPersistenceContext().getPropertyValue("deliveryMode");
    }


    @Accessor(qualifier = "deliveryPointOfService", type = Accessor.Type.GETTER)
    public PointOfServiceModel getDeliveryPointOfService()
    {
        return (PointOfServiceModel)getPersistenceContext().getPropertyValue("deliveryPointOfService");
    }


    @Accessor(qualifier = "discountValues", type = Accessor.Type.GETTER)
    public List<DiscountValue> getDiscountValues()
    {
        return (List<DiscountValue>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "discountValues");
    }


    @Accessor(qualifier = "discountValuesInternal", type = Accessor.Type.GETTER)
    public String getDiscountValuesInternal()
    {
        return (String)getPersistenceContext().getPropertyValue("discountValuesInternal");
    }


    @Accessor(qualifier = "entryGroupNumbers", type = Accessor.Type.GETTER)
    public Set<Integer> getEntryGroupNumbers()
    {
        return (Set<Integer>)getPersistenceContext().getPropertyValue("entryGroupNumbers");
    }


    @Accessor(qualifier = "entryNumber", type = Accessor.Type.GETTER)
    public Integer getEntryNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("entryNumber");
    }


    @Accessor(qualifier = "Europe1PriceFactory_PDG", type = Accessor.Type.GETTER)
    public ProductDiscountGroup getEurope1PriceFactory_PDG()
    {
        return (ProductDiscountGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_PDG");
    }


    @Accessor(qualifier = "Europe1PriceFactory_PPG", type = Accessor.Type.GETTER)
    public ProductPriceGroup getEurope1PriceFactory_PPG()
    {
        return (ProductPriceGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_PPG");
    }


    @Accessor(qualifier = "Europe1PriceFactory_PTG", type = Accessor.Type.GETTER)
    public ProductTaxGroup getEurope1PriceFactory_PTG()
    {
        return (ProductTaxGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_PTG");
    }


    @Accessor(qualifier = "externalConfiguration", type = Accessor.Type.GETTER)
    public String getExternalConfiguration()
    {
        return (String)getPersistenceContext().getPropertyValue("externalConfiguration");
    }


    @Accessor(qualifier = "freightPrice", type = Accessor.Type.GETTER)
    public Double getFreightPrice()
    {
        return (Double)getPersistenceContext().getPropertyValue("freightPrice");
    }


    @Accessor(qualifier = "giveAway", type = Accessor.Type.GETTER)
    public Boolean getGiveAway()
    {
        Boolean value = (Boolean)getPersistenceContext().getPropertyValue("giveAway");
        return (value != null) ? value : Boolean.valueOf(false);
    }


    @Accessor(qualifier = "info", type = Accessor.Type.GETTER)
    public String getInfo()
    {
        return (String)getPersistenceContext().getPropertyValue("info");
    }


    @Accessor(qualifier = "namedDeliveryDate", type = Accessor.Type.GETTER)
    public Date getNamedDeliveryDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("namedDeliveryDate");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public AbstractOrderModel getOrder()
    {
        return (AbstractOrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "productInfos", type = Accessor.Type.GETTER)
    public List<AbstractOrderEntryProductInfoModel> getProductInfos()
    {
        return (List<AbstractOrderEntryProductInfoModel>)getPersistenceContext().getPropertyValue("productInfos");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public Long getQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "quantityStatus", type = Accessor.Type.GETTER)
    public OrderEntryStatus getQuantityStatus()
    {
        return (OrderEntryStatus)getPersistenceContext().getPropertyValue("quantityStatus");
    }


    @Accessor(qualifier = "rejected", type = Accessor.Type.GETTER)
    public Boolean getRejected()
    {
        Boolean value = (Boolean)getPersistenceContext().getPropertyValue("rejected");
        return (value != null) ? value : Boolean.valueOf(false);
    }


    @Accessor(qualifier = "sapPricingConditions", type = Accessor.Type.GETTER)
    public Set<SAPPricingConditionModel> getSapPricingConditions()
    {
        return (Set<SAPPricingConditionModel>)getPersistenceContext().getPropertyValue("sapPricingConditions");
    }


    @Accessor(qualifier = "taxValues", type = Accessor.Type.GETTER)
    public Collection<TaxValue> getTaxValues()
    {
        return (Collection<TaxValue>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "taxValues");
    }


    @Accessor(qualifier = "taxValuesInternal", type = Accessor.Type.GETTER)
    public String getTaxValuesInternal()
    {
        return (String)getPersistenceContext().getPropertyValue("taxValuesInternal");
    }


    @Accessor(qualifier = "timeToLive", type = Accessor.Type.GETTER)
    public Date getTimeToLive()
    {
        return (Date)getPersistenceContext().getPropertyValue("timeToLive");
    }


    @Accessor(qualifier = "totalPrice", type = Accessor.Type.GETTER)
    public Double getTotalPrice()
    {
        return (Double)getPersistenceContext().getPropertyValue("totalPrice");
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
    public UnitModel getUnit()
    {
        return (UnitModel)getPersistenceContext().getPropertyValue("unit");
    }


    @Accessor(qualifier = "basePrice", type = Accessor.Type.SETTER)
    public void setBasePrice(Double value)
    {
        getPersistenceContext().setPropertyValue("basePrice", value);
    }


    @Accessor(qualifier = "calculated", type = Accessor.Type.SETTER)
    public void setCalculated(Boolean value)
    {
        getPersistenceContext().setPropertyValue("calculated", value);
    }


    @Accessor(qualifier = "chosenVendor", type = Accessor.Type.SETTER)
    public void setChosenVendor(VendorModel value)
    {
        getPersistenceContext().setPropertyValue("chosenVendor", value);
    }


    @Accessor(qualifier = "consignmentEntries", type = Accessor.Type.SETTER)
    public void setConsignmentEntries(Set<ConsignmentEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("consignmentEntries", value);
    }


    @Accessor(qualifier = "contractPrice", type = Accessor.Type.SETTER)
    public void setContractPrice(Double value)
    {
        getPersistenceContext().setPropertyValue("contractPrice", value);
    }


    @Accessor(qualifier = "costCenter", type = Accessor.Type.SETTER)
    public void setCostCenter(B2BCostCenterModel value)
    {
        getPersistenceContext().setPropertyValue("costCenter", value);
    }


    @Accessor(qualifier = "deliveryAddress", type = Accessor.Type.SETTER)
    public void setDeliveryAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryAddress", value);
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
    public void setDeliveryMode(DeliveryModeModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryMode", value);
    }


    @Accessor(qualifier = "deliveryPointOfService", type = Accessor.Type.SETTER)
    public void setDeliveryPointOfService(PointOfServiceModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryPointOfService", value);
    }


    @Accessor(qualifier = "discountValues", type = Accessor.Type.SETTER)
    public void setDiscountValues(List<DiscountValue> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "discountValues", value);
    }


    @Accessor(qualifier = "discountValuesInternal", type = Accessor.Type.SETTER)
    public void setDiscountValuesInternal(String value)
    {
        getPersistenceContext().setPropertyValue("discountValuesInternal", value);
    }


    @Accessor(qualifier = "entryGroupNumbers", type = Accessor.Type.SETTER)
    public void setEntryGroupNumbers(Set<Integer> value)
    {
        getPersistenceContext().setPropertyValue("entryGroupNumbers", value);
    }


    @Accessor(qualifier = "entryNumber", type = Accessor.Type.SETTER)
    public void setEntryNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("entryNumber", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_PDG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_PDG(ProductDiscountGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_PDG", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_PPG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_PPG(ProductPriceGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_PPG", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_PTG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_PTG(ProductTaxGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_PTG", value);
    }


    @Accessor(qualifier = "externalConfiguration", type = Accessor.Type.SETTER)
    public void setExternalConfiguration(String value)
    {
        getPersistenceContext().setPropertyValue("externalConfiguration", value);
    }


    @Accessor(qualifier = "freightPrice", type = Accessor.Type.SETTER)
    public void setFreightPrice(Double value)
    {
        getPersistenceContext().setPropertyValue("freightPrice", value);
    }


    @Accessor(qualifier = "giveAway", type = Accessor.Type.SETTER)
    public void setGiveAway(Boolean value)
    {
        getPersistenceContext().setPropertyValue("giveAway", value);
    }


    @Accessor(qualifier = "info", type = Accessor.Type.SETTER)
    public void setInfo(String value)
    {
        getPersistenceContext().setPropertyValue("info", value);
    }


    @Accessor(qualifier = "namedDeliveryDate", type = Accessor.Type.SETTER)
    public void setNamedDeliveryDate(Date value)
    {
        getPersistenceContext().setPropertyValue("namedDeliveryDate", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }


    @Accessor(qualifier = "productInfos", type = Accessor.Type.SETTER)
    public void setProductInfos(List<AbstractOrderEntryProductInfoModel> value)
    {
        getPersistenceContext().setPropertyValue("productInfos", value);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }


    @Accessor(qualifier = "quantityStatus", type = Accessor.Type.SETTER)
    public void setQuantityStatus(OrderEntryStatus value)
    {
        getPersistenceContext().setPropertyValue("quantityStatus", value);
    }


    @Accessor(qualifier = "rejected", type = Accessor.Type.SETTER)
    public void setRejected(Boolean value)
    {
        getPersistenceContext().setPropertyValue("rejected", value);
    }


    @Accessor(qualifier = "sapPricingConditions", type = Accessor.Type.SETTER)
    public void setSapPricingConditions(Set<SAPPricingConditionModel> value)
    {
        getPersistenceContext().setPropertyValue("sapPricingConditions", value);
    }


    @Accessor(qualifier = "taxValues", type = Accessor.Type.SETTER)
    public void setTaxValues(Collection<TaxValue> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "taxValues", value);
    }


    @Accessor(qualifier = "taxValuesInternal", type = Accessor.Type.SETTER)
    public void setTaxValuesInternal(String value)
    {
        getPersistenceContext().setPropertyValue("taxValuesInternal", value);
    }


    @Accessor(qualifier = "timeToLive", type = Accessor.Type.SETTER)
    public void setTimeToLive(Date value)
    {
        getPersistenceContext().setPropertyValue("timeToLive", value);
    }


    @Accessor(qualifier = "totalPrice", type = Accessor.Type.SETTER)
    public void setTotalPrice(Double value)
    {
        getPersistenceContext().setPropertyValue("totalPrice", value);
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
    public void setUnit(UnitModel value)
    {
        getPersistenceContext().setPropertyValue("unit", value);
    }
}
