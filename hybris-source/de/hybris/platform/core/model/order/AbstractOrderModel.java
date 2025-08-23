package de.hybris.platform.core.model.order;

import com.olympus.oca.commerce.core.model.HeavyOrderQuestionsModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.promotions.PromotionOrderRestrictionModel;
import de.hybris.platform.core.enums.DeliveryStatus;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class AbstractOrderModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractOrder";
    public static final String _CONSIGNMENTORDERRELATION = "ConsignmentOrderRelation";
    public static final String _B2BUNIT2ABSTRACTORDERS = "B2BUnit2AbstractOrders";
    public static final String CALCULATED = "calculated";
    public static final String CODE = "code";
    public static final String CURRENCY = "currency";
    public static final String DATE = "date";
    public static final String DELIVERYADDRESS = "deliveryAddress";
    public static final String DELIVERYCOST = "deliveryCost";
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String DELIVERYSTATUS = "deliveryStatus";
    public static final String DESCRIPTION = "description";
    public static final String EXPIRATIONTIME = "expirationTime";
    public static final String GLOBALDISCOUNTVALUESINTERNAL = "globalDiscountValuesInternal";
    public static final String GLOBALDISCOUNTVALUES = "globalDiscountValues";
    public static final String NAME = "name";
    public static final String NET = "net";
    public static final String PAYMENTADDRESS = "paymentAddress";
    public static final String PAYMENTCOST = "paymentCost";
    public static final String PAYMENTINFO = "paymentInfo";
    public static final String PAYMENTMODE = "paymentMode";
    public static final String PAYMENTSTATUS = "paymentStatus";
    public static final String STATUS = "status";
    public static final String EXPORTSTATUS = "exportStatus";
    public static final String STATUSINFO = "statusInfo";
    public static final String TOTALPRICE = "totalPrice";
    public static final String TOTALDISCOUNTS = "totalDiscounts";
    public static final String TOTALTAX = "totalTax";
    public static final String TOTALTAXVALUESINTERNAL = "totalTaxValuesInternal";
    public static final String TOTALTAXVALUES = "totalTaxValues";
    public static final String USER = "user";
    public static final String SUBTOTAL = "subtotal";
    public static final String DISCOUNTSINCLUDEDELIVERYCOST = "discountsIncludeDeliveryCost";
    public static final String DISCOUNTSINCLUDEPAYMENTCOST = "discountsIncludePaymentCost";
    public static final String ENTRYGROUPS = "entryGroups";
    public static final String ENTRIES = "entries";
    public static final String DISCOUNTS = "discounts";
    public static final String EUROPE1PRICEFACTORY_UDG = "Europe1PriceFactory_UDG";
    public static final String EUROPE1PRICEFACTORY_UPG = "Europe1PriceFactory_UPG";
    public static final String EUROPE1PRICEFACTORY_UTG = "Europe1PriceFactory_UTG";
    public static final String CONSIGNMENTS = "consignments";
    public static final String PAYMENTTRANSACTIONS = "paymentTransactions";
    public static final String APPLIEDVOUCHERCODES = "appliedVoucherCodes";
    public static final String PREVIOUSDELIVERYMODE = "previousDeliveryMode";
    public static final String ALLPROMOTIONRESULTS = "allPromotionResults";
    public static final String SITE = "site";
    public static final String STORE = "store";
    public static final String GUID = "guid";
    public static final String QUOTEDISCOUNTVALUESINTERNAL = "quoteDiscountValuesInternal";
    public static final String PROMOTIONORDERRESTRICTIONS = "promotionOrderRestrictions";
    public static final String LOCALE = "locale";
    public static final String WORKFLOW = "workflow";
    public static final String QUOTEEXPIRATIONDATE = "quoteExpirationDate";
    public static final String UNIT = "Unit";
    public static final String B2BCOMMENTS = "b2bcomments";
    public static final String PERMISSIONRESULTS = "PermissionResults";
    public static final String PURCHASEORDERNUMBER = "purchaseOrderNumber";
    public static final String PAYMENTTYPE = "paymentType";
    public static final String SHIPPINGCARRIERACCOUNT = "shippingCarrierAccount";
    public static final String SHIPPINGCARRIER = "shippingCarrier";
    public static final String ERPORDERNUMBER = "erpOrderNumber";
    public static final String TOTALENTRIES = "totalEntries";
    public static final String HEAVYORDERQUESTIONS = "heavyOrderQuestions";


    public AbstractOrderModel()
    {
    }


    public AbstractOrderModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractOrderModel(CurrencyModel _currency, Date _date, UserModel _user)
    {
        setCurrency(_currency);
        setDate(_date);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractOrderModel(CurrencyModel _currency, Date _date, ItemModel _owner, UserModel _user)
    {
        setCurrency(_currency);
        setDate(_date);
        setOwner(_owner);
        setUser(_user);
    }


    @Accessor(qualifier = "allPromotionResults", type = Accessor.Type.GETTER)
    public Set<PromotionResultModel> getAllPromotionResults()
    {
        return (Set<PromotionResultModel>)getPersistenceContext().getPropertyValue("allPromotionResults");
    }


    @Accessor(qualifier = "b2bcomments", type = Accessor.Type.GETTER)
    public Collection<B2BCommentModel> getB2bcomments()
    {
        return (Collection<B2BCommentModel>)getPersistenceContext().getPropertyValue("b2bcomments");
    }


    @Accessor(qualifier = "calculated", type = Accessor.Type.GETTER)
    public Boolean getCalculated()
    {
        Boolean value = (Boolean)getPersistenceContext().getPropertyValue("calculated");
        return (value != null) ? value : Boolean.valueOf(false);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "consignments", type = Accessor.Type.GETTER)
    public Set<ConsignmentModel> getConsignments()
    {
        return (Set<ConsignmentModel>)getPersistenceContext().getPropertyValue("consignments");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "date", type = Accessor.Type.GETTER)
    public Date getDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("date");
    }


    @Accessor(qualifier = "deliveryAddress", type = Accessor.Type.GETTER)
    public AddressModel getDeliveryAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("deliveryAddress");
    }


    @Accessor(qualifier = "deliveryCost", type = Accessor.Type.GETTER)
    public Double getDeliveryCost()
    {
        return (Double)getPersistenceContext().getPropertyValue("deliveryCost");
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
    public DeliveryModeModel getDeliveryMode()
    {
        return (DeliveryModeModel)getPersistenceContext().getPropertyValue("deliveryMode");
    }


    @Accessor(qualifier = "deliveryStatus", type = Accessor.Type.GETTER)
    public DeliveryStatus getDeliveryStatus()
    {
        return (DeliveryStatus)getPersistenceContext().getPropertyValue("deliveryStatus");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "discounts", type = Accessor.Type.GETTER)
    public List<DiscountModel> getDiscounts()
    {
        return (List<DiscountModel>)getPersistenceContext().getPropertyValue("discounts");
    }


    @Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
    public List<AbstractOrderEntryModel> getEntries()
    {
        return (List<AbstractOrderEntryModel>)getPersistenceContext().getPropertyValue("entries");
    }


    @Accessor(qualifier = "entryGroups", type = Accessor.Type.GETTER)
    public List<EntryGroup> getEntryGroups()
    {
        return (List<EntryGroup>)getPersistenceContext().getPropertyValue("entryGroups");
    }


    @Accessor(qualifier = "erpOrderNumber", type = Accessor.Type.GETTER)
    public String getErpOrderNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("erpOrderNumber");
    }


    @Accessor(qualifier = "Europe1PriceFactory_UDG", type = Accessor.Type.GETTER)
    public UserDiscountGroup getEurope1PriceFactory_UDG()
    {
        return (UserDiscountGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_UDG");
    }


    @Accessor(qualifier = "Europe1PriceFactory_UPG", type = Accessor.Type.GETTER)
    public UserPriceGroup getEurope1PriceFactory_UPG()
    {
        return (UserPriceGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_UPG");
    }


    @Accessor(qualifier = "Europe1PriceFactory_UTG", type = Accessor.Type.GETTER)
    public UserTaxGroup getEurope1PriceFactory_UTG()
    {
        return (UserTaxGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_UTG");
    }


    @Accessor(qualifier = "expirationTime", type = Accessor.Type.GETTER)
    public Date getExpirationTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("expirationTime");
    }


    @Accessor(qualifier = "exportStatus", type = Accessor.Type.GETTER)
    public ExportStatus getExportStatus()
    {
        return (ExportStatus)getPersistenceContext().getPropertyValue("exportStatus");
    }


    @Accessor(qualifier = "globalDiscountValues", type = Accessor.Type.GETTER)
    public List<DiscountValue> getGlobalDiscountValues()
    {
        return (List<DiscountValue>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "globalDiscountValues");
    }


    @Accessor(qualifier = "globalDiscountValuesInternal", type = Accessor.Type.GETTER)
    public String getGlobalDiscountValuesInternal()
    {
        return (String)getPersistenceContext().getPropertyValue("globalDiscountValuesInternal");
    }


    @Accessor(qualifier = "guid", type = Accessor.Type.GETTER)
    public String getGuid()
    {
        return (String)getPersistenceContext().getPropertyValue("guid");
    }


    @Accessor(qualifier = "heavyOrderQuestions", type = Accessor.Type.GETTER)
    public HeavyOrderQuestionsModel getHeavyOrderQuestions()
    {
        return (HeavyOrderQuestionsModel)getPersistenceContext().getPropertyValue("heavyOrderQuestions");
    }


    @Accessor(qualifier = "locale", type = Accessor.Type.GETTER)
    public String getLocale()
    {
        return (String)getPersistenceContext().getPropertyValue("locale");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "net", type = Accessor.Type.GETTER)
    public Boolean getNet()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("net");
    }


    @Accessor(qualifier = "paymentAddress", type = Accessor.Type.GETTER)
    public AddressModel getPaymentAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("paymentAddress");
    }


    @Accessor(qualifier = "paymentCost", type = Accessor.Type.GETTER)
    public Double getPaymentCost()
    {
        return (Double)getPersistenceContext().getPropertyValue("paymentCost");
    }


    @Accessor(qualifier = "paymentInfo", type = Accessor.Type.GETTER)
    public PaymentInfoModel getPaymentInfo()
    {
        return (PaymentInfoModel)getPersistenceContext().getPropertyValue("paymentInfo");
    }


    @Accessor(qualifier = "paymentMode", type = Accessor.Type.GETTER)
    public PaymentModeModel getPaymentMode()
    {
        return (PaymentModeModel)getPersistenceContext().getPropertyValue("paymentMode");
    }


    @Accessor(qualifier = "paymentStatus", type = Accessor.Type.GETTER)
    public PaymentStatus getPaymentStatus()
    {
        return (PaymentStatus)getPersistenceContext().getPropertyValue("paymentStatus");
    }


    @Accessor(qualifier = "paymentTransactions", type = Accessor.Type.GETTER)
    public List<PaymentTransactionModel> getPaymentTransactions()
    {
        return (List<PaymentTransactionModel>)getPersistenceContext().getPropertyValue("paymentTransactions");
    }


    @Accessor(qualifier = "paymentType", type = Accessor.Type.GETTER)
    public CheckoutPaymentType getPaymentType()
    {
        return (CheckoutPaymentType)getPersistenceContext().getPropertyValue("paymentType");
    }


    @Accessor(qualifier = "PermissionResults", type = Accessor.Type.GETTER)
    public Collection<B2BPermissionResultModel> getPermissionResults()
    {
        return (Collection<B2BPermissionResultModel>)getPersistenceContext().getPropertyValue("PermissionResults");
    }


    @Accessor(qualifier = "previousDeliveryMode", type = Accessor.Type.GETTER)
    public DeliveryModeModel getPreviousDeliveryMode()
    {
        return (DeliveryModeModel)getPersistenceContext().getPropertyValue("previousDeliveryMode");
    }


    @Accessor(qualifier = "promotionOrderRestrictions", type = Accessor.Type.GETTER)
    public Collection<PromotionOrderRestrictionModel> getPromotionOrderRestrictions()
    {
        return (Collection<PromotionOrderRestrictionModel>)getPersistenceContext().getPropertyValue("promotionOrderRestrictions");
    }


    @Accessor(qualifier = "purchaseOrderNumber", type = Accessor.Type.GETTER)
    public String getPurchaseOrderNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("purchaseOrderNumber");
    }


    @Accessor(qualifier = "quoteDiscountValuesInternal", type = Accessor.Type.GETTER)
    public String getQuoteDiscountValuesInternal()
    {
        return (String)getPersistenceContext().getPropertyValue("quoteDiscountValuesInternal");
    }


    @Accessor(qualifier = "quoteExpirationDate", type = Accessor.Type.GETTER)
    public Date getQuoteExpirationDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("quoteExpirationDate");
    }


    @Accessor(qualifier = "shippingCarrier", type = Accessor.Type.GETTER)
    public String getShippingCarrier()
    {
        return (String)getPersistenceContext().getPropertyValue("shippingCarrier");
    }


    @Accessor(qualifier = "shippingCarrierAccount", type = Accessor.Type.GETTER)
    public String getShippingCarrierAccount()
    {
        return (String)getPersistenceContext().getPropertyValue("shippingCarrierAccount");
    }


    @Accessor(qualifier = "site", type = Accessor.Type.GETTER)
    public BaseSiteModel getSite()
    {
        return (BaseSiteModel)getPersistenceContext().getPropertyValue("site");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public OrderStatus getStatus()
    {
        return (OrderStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "statusInfo", type = Accessor.Type.GETTER)
    public String getStatusInfo()
    {
        return (String)getPersistenceContext().getPropertyValue("statusInfo");
    }


    @Accessor(qualifier = "store", type = Accessor.Type.GETTER)
    public BaseStoreModel getStore()
    {
        return (BaseStoreModel)getPersistenceContext().getPropertyValue("store");
    }


    @Accessor(qualifier = "subtotal", type = Accessor.Type.GETTER)
    public Double getSubtotal()
    {
        return (Double)getPersistenceContext().getPropertyValue("subtotal");
    }


    @Accessor(qualifier = "totalDiscounts", type = Accessor.Type.GETTER)
    public Double getTotalDiscounts()
    {
        return (Double)getPersistenceContext().getPropertyValue("totalDiscounts");
    }


    @Accessor(qualifier = "totalEntries", type = Accessor.Type.GETTER)
    public Integer getTotalEntries()
    {
        return (Integer)getPersistenceContext().getPropertyValue("totalEntries");
    }


    @Accessor(qualifier = "totalPrice", type = Accessor.Type.GETTER)
    public Double getTotalPrice()
    {
        return (Double)getPersistenceContext().getPropertyValue("totalPrice");
    }


    @Accessor(qualifier = "totalTax", type = Accessor.Type.GETTER)
    public Double getTotalTax()
    {
        return (Double)getPersistenceContext().getPropertyValue("totalTax");
    }


    @Accessor(qualifier = "totalTaxValues", type = Accessor.Type.GETTER)
    public Collection<TaxValue> getTotalTaxValues()
    {
        return (Collection<TaxValue>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "totalTaxValues");
    }


    @Accessor(qualifier = "totalTaxValuesInternal", type = Accessor.Type.GETTER)
    public String getTotalTaxValuesInternal()
    {
        return (String)getPersistenceContext().getPropertyValue("totalTaxValuesInternal");
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.GETTER)
    public B2BUnitModel getUnit()
    {
        return (B2BUnitModel)getPersistenceContext().getPropertyValue("Unit");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
    public WorkflowModel getWorkflow()
    {
        return (WorkflowModel)getPersistenceContext().getPropertyValue("workflow");
    }


    @Accessor(qualifier = "discountsIncludeDeliveryCost", type = Accessor.Type.GETTER)
    public boolean isDiscountsIncludeDeliveryCost()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("discountsIncludeDeliveryCost"));
    }


    @Accessor(qualifier = "discountsIncludePaymentCost", type = Accessor.Type.GETTER)
    public boolean isDiscountsIncludePaymentCost()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("discountsIncludePaymentCost"));
    }


    @Accessor(qualifier = "allPromotionResults", type = Accessor.Type.SETTER)
    public void setAllPromotionResults(Set<PromotionResultModel> value)
    {
        getPersistenceContext().setPropertyValue("allPromotionResults", value);
    }


    @Accessor(qualifier = "b2bcomments", type = Accessor.Type.SETTER)
    public void setB2bcomments(Collection<B2BCommentModel> value)
    {
        getPersistenceContext().setPropertyValue("b2bcomments", value);
    }


    @Accessor(qualifier = "calculated", type = Accessor.Type.SETTER)
    public void setCalculated(Boolean value)
    {
        getPersistenceContext().setPropertyValue("calculated", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "consignments", type = Accessor.Type.SETTER)
    public void setConsignments(Set<ConsignmentModel> value)
    {
        getPersistenceContext().setPropertyValue("consignments", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "date", type = Accessor.Type.SETTER)
    public void setDate(Date value)
    {
        getPersistenceContext().setPropertyValue("date", value);
    }


    @Accessor(qualifier = "deliveryAddress", type = Accessor.Type.SETTER)
    public void setDeliveryAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryAddress", value);
    }


    @Accessor(qualifier = "deliveryCost", type = Accessor.Type.SETTER)
    public void setDeliveryCost(Double value)
    {
        getPersistenceContext().setPropertyValue("deliveryCost", value);
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
    public void setDeliveryMode(DeliveryModeModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryMode", value);
    }


    @Accessor(qualifier = "deliveryStatus", type = Accessor.Type.SETTER)
    public void setDeliveryStatus(DeliveryStatus value)
    {
        getPersistenceContext().setPropertyValue("deliveryStatus", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "discounts", type = Accessor.Type.SETTER)
    public void setDiscounts(List<DiscountModel> value)
    {
        getPersistenceContext().setPropertyValue("discounts", value);
    }


    @Accessor(qualifier = "discountsIncludeDeliveryCost", type = Accessor.Type.SETTER)
    public void setDiscountsIncludeDeliveryCost(boolean value)
    {
        getPersistenceContext().setPropertyValue("discountsIncludeDeliveryCost", toObject(value));
    }


    @Accessor(qualifier = "discountsIncludePaymentCost", type = Accessor.Type.SETTER)
    public void setDiscountsIncludePaymentCost(boolean value)
    {
        getPersistenceContext().setPropertyValue("discountsIncludePaymentCost", toObject(value));
    }


    @Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
    public void setEntries(List<AbstractOrderEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("entries", value);
    }


    @Accessor(qualifier = "entryGroups", type = Accessor.Type.SETTER)
    public void setEntryGroups(List<EntryGroup> value)
    {
        getPersistenceContext().setPropertyValue("entryGroups", value);
    }


    @Accessor(qualifier = "erpOrderNumber", type = Accessor.Type.SETTER)
    public void setErpOrderNumber(String value)
    {
        getPersistenceContext().setPropertyValue("erpOrderNumber", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_UDG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_UDG(UserDiscountGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_UDG", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_UPG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_UPG(UserPriceGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_UPG", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_UTG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_UTG(UserTaxGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_UTG", value);
    }


    @Accessor(qualifier = "expirationTime", type = Accessor.Type.SETTER)
    public void setExpirationTime(Date value)
    {
        getPersistenceContext().setPropertyValue("expirationTime", value);
    }


    @Accessor(qualifier = "exportStatus", type = Accessor.Type.SETTER)
    public void setExportStatus(ExportStatus value)
    {
        getPersistenceContext().setPropertyValue("exportStatus", value);
    }


    @Accessor(qualifier = "globalDiscountValues", type = Accessor.Type.SETTER)
    public void setGlobalDiscountValues(List<DiscountValue> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "globalDiscountValues", value);
    }


    @Accessor(qualifier = "globalDiscountValuesInternal", type = Accessor.Type.SETTER)
    public void setGlobalDiscountValuesInternal(String value)
    {
        getPersistenceContext().setPropertyValue("globalDiscountValuesInternal", value);
    }


    @Accessor(qualifier = "guid", type = Accessor.Type.SETTER)
    public void setGuid(String value)
    {
        getPersistenceContext().setPropertyValue("guid", value);
    }


    @Accessor(qualifier = "heavyOrderQuestions", type = Accessor.Type.SETTER)
    public void setHeavyOrderQuestions(HeavyOrderQuestionsModel value)
    {
        getPersistenceContext().setPropertyValue("heavyOrderQuestions", value);
    }


    @Accessor(qualifier = "locale", type = Accessor.Type.SETTER)
    public void setLocale(String value)
    {
        getPersistenceContext().setPropertyValue("locale", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "net", type = Accessor.Type.SETTER)
    public void setNet(Boolean value)
    {
        getPersistenceContext().setPropertyValue("net", value);
    }


    @Accessor(qualifier = "paymentAddress", type = Accessor.Type.SETTER)
    public void setPaymentAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("paymentAddress", value);
    }


    @Accessor(qualifier = "paymentCost", type = Accessor.Type.SETTER)
    public void setPaymentCost(Double value)
    {
        getPersistenceContext().setPropertyValue("paymentCost", value);
    }


    @Accessor(qualifier = "paymentInfo", type = Accessor.Type.SETTER)
    public void setPaymentInfo(PaymentInfoModel value)
    {
        getPersistenceContext().setPropertyValue("paymentInfo", value);
    }


    @Accessor(qualifier = "paymentMode", type = Accessor.Type.SETTER)
    public void setPaymentMode(PaymentModeModel value)
    {
        getPersistenceContext().setPropertyValue("paymentMode", value);
    }


    @Accessor(qualifier = "paymentStatus", type = Accessor.Type.SETTER)
    public void setPaymentStatus(PaymentStatus value)
    {
        getPersistenceContext().setPropertyValue("paymentStatus", value);
    }


    @Accessor(qualifier = "paymentTransactions", type = Accessor.Type.SETTER)
    public void setPaymentTransactions(List<PaymentTransactionModel> value)
    {
        getPersistenceContext().setPropertyValue("paymentTransactions", value);
    }


    @Accessor(qualifier = "paymentType", type = Accessor.Type.SETTER)
    public void setPaymentType(CheckoutPaymentType value)
    {
        getPersistenceContext().setPropertyValue("paymentType", value);
    }


    @Accessor(qualifier = "PermissionResults", type = Accessor.Type.SETTER)
    public void setPermissionResults(Collection<B2BPermissionResultModel> value)
    {
        getPersistenceContext().setPropertyValue("PermissionResults", value);
    }


    @Accessor(qualifier = "previousDeliveryMode", type = Accessor.Type.SETTER)
    public void setPreviousDeliveryMode(DeliveryModeModel value)
    {
        getPersistenceContext().setPropertyValue("previousDeliveryMode", value);
    }


    @Accessor(qualifier = "promotionOrderRestrictions", type = Accessor.Type.SETTER)
    public void setPromotionOrderRestrictions(Collection<PromotionOrderRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("promotionOrderRestrictions", value);
    }


    @Accessor(qualifier = "purchaseOrderNumber", type = Accessor.Type.SETTER)
    public void setPurchaseOrderNumber(String value)
    {
        getPersistenceContext().setPropertyValue("purchaseOrderNumber", value);
    }


    @Accessor(qualifier = "quoteDiscountValuesInternal", type = Accessor.Type.SETTER)
    public void setQuoteDiscountValuesInternal(String value)
    {
        getPersistenceContext().setPropertyValue("quoteDiscountValuesInternal", value);
    }


    @Accessor(qualifier = "quoteExpirationDate", type = Accessor.Type.SETTER)
    public void setQuoteExpirationDate(Date value)
    {
        getPersistenceContext().setPropertyValue("quoteExpirationDate", value);
    }


    @Accessor(qualifier = "shippingCarrier", type = Accessor.Type.SETTER)
    public void setShippingCarrier(String value)
    {
        getPersistenceContext().setPropertyValue("shippingCarrier", value);
    }


    @Accessor(qualifier = "shippingCarrierAccount", type = Accessor.Type.SETTER)
    public void setShippingCarrierAccount(String value)
    {
        getPersistenceContext().setPropertyValue("shippingCarrierAccount", value);
    }


    @Accessor(qualifier = "site", type = Accessor.Type.SETTER)
    public void setSite(BaseSiteModel value)
    {
        getPersistenceContext().setPropertyValue("site", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(OrderStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "statusInfo", type = Accessor.Type.SETTER)
    public void setStatusInfo(String value)
    {
        getPersistenceContext().setPropertyValue("statusInfo", value);
    }


    @Accessor(qualifier = "store", type = Accessor.Type.SETTER)
    public void setStore(BaseStoreModel value)
    {
        getPersistenceContext().setPropertyValue("store", value);
    }


    @Accessor(qualifier = "subtotal", type = Accessor.Type.SETTER)
    public void setSubtotal(Double value)
    {
        getPersistenceContext().setPropertyValue("subtotal", value);
    }


    @Accessor(qualifier = "totalDiscounts", type = Accessor.Type.SETTER)
    public void setTotalDiscounts(Double value)
    {
        getPersistenceContext().setPropertyValue("totalDiscounts", value);
    }


    @Accessor(qualifier = "totalEntries", type = Accessor.Type.SETTER)
    public void setTotalEntries(Integer value)
    {
        getPersistenceContext().setPropertyValue("totalEntries", value);
    }


    @Accessor(qualifier = "totalPrice", type = Accessor.Type.SETTER)
    public void setTotalPrice(Double value)
    {
        getPersistenceContext().setPropertyValue("totalPrice", value);
    }


    @Accessor(qualifier = "totalTax", type = Accessor.Type.SETTER)
    public void setTotalTax(Double value)
    {
        getPersistenceContext().setPropertyValue("totalTax", value);
    }


    @Accessor(qualifier = "totalTaxValues", type = Accessor.Type.SETTER)
    public void setTotalTaxValues(Collection<TaxValue> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "totalTaxValues", value);
    }


    @Accessor(qualifier = "totalTaxValuesInternal", type = Accessor.Type.SETTER)
    public void setTotalTaxValuesInternal(String value)
    {
        getPersistenceContext().setPropertyValue("totalTaxValuesInternal", value);
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.SETTER)
    public void setUnit(B2BUnitModel value)
    {
        getPersistenceContext().setPropertyValue("Unit", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }


    @Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
    public void setWorkflow(WorkflowModel value)
    {
        getPersistenceContext().setPropertyValue("workflow", value);
    }
}
