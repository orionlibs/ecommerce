package de.hybris.platform.basecommerce.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.basecommerce.jalo.externaltax.ProductTaxCode;
import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.campaigns.jalo.Campaign;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.deeplink.jalo.media.BarcodeMedia;
import de.hybris.platform.deeplink.jalo.rules.DeeplinkUrl;
import de.hybris.platform.deeplink.jalo.rules.DeeplinkUrlRule;
import de.hybris.platform.fraud.jalo.FraudReport;
import de.hybris.platform.fraud.jalo.FraudSymptomScoring;
import de.hybris.platform.fraud.jalo.ProductOrderLimit;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.ordercancel.jalo.OrderCancelConfig;
import de.hybris.platform.ordercancel.jalo.OrderCancelRecord;
import de.hybris.platform.ordercancel.jalo.OrderCancelRecordEntry;
import de.hybris.platform.ordercancel.jalo.OrderEntryCancelRecordEntry;
import de.hybris.platform.orderhistory.jalo.OrderHistoryEntry;
import de.hybris.platform.ordermodify.jalo.OrderModificationRecord;
import de.hybris.platform.orderprocessing.jalo.OrderProcess;
import de.hybris.platform.orderscheduling.jalo.CartToOrderCronJob;
import de.hybris.platform.orderscheduling.jalo.OrderScheduleCronJob;
import de.hybris.platform.orderscheduling.jalo.OrderTemplateToOrderCronJob;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.jalo.ConsignmentEntry;
import de.hybris.platform.ordersplitting.jalo.ConsignmentProcess;
import de.hybris.platform.ordersplitting.jalo.StockLevel;
import de.hybris.platform.ordersplitting.jalo.Vendor;
import de.hybris.platform.ordersplitting.jalo.Warehouse;
import de.hybris.platform.refund.jalo.OrderRefundRecordEntry;
import de.hybris.platform.returns.jalo.OrderEntryReturnRecordEntry;
import de.hybris.platform.returns.jalo.OrderReplacementRecordEntry;
import de.hybris.platform.returns.jalo.OrderReturnRecord;
import de.hybris.platform.returns.jalo.RefundEntry;
import de.hybris.platform.returns.jalo.ReplacementEntry;
import de.hybris.platform.returns.jalo.ReplacementOrder;
import de.hybris.platform.returns.jalo.ReplacementOrderEntry;
import de.hybris.platform.returns.jalo.ReturnProcess;
import de.hybris.platform.returns.jalo.ReturnRequest;
import de.hybris.platform.stock.jalo.StockLevelHistoryEntry;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.storelocator.jalo.GeocodeAddressesCronJob;
import de.hybris.platform.storelocator.jalo.OpeningSchedule;
import de.hybris.platform.storelocator.jalo.PointOfService;
import de.hybris.platform.storelocator.jalo.SpecialOpeningDay;
import de.hybris.platform.storelocator.jalo.WeekdayOpeningDay;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.variants.jalo.GenericVariantProduct;
import de.hybris.platform.variants.jalo.VariantCategory;
import de.hybris.platform.variants.jalo.VariantValueCategory;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedBasecommerceManager extends Extension
{
    protected static final OneToManyHandler<FraudReport> ORDERFRAUDREPORTRELATIONFRAUDREPORTSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.FRAUDREPORT, false, "order", null, false, true, 1);
    protected static final OneToManyHandler<OrderHistoryEntry> ORDERHISTORYRELATIONHISTORYENTRIESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERHISTORYENTRY, false, "order", "orderPOS", true, true, 2);
    protected static final OneToManyHandler<OrderTemplateToOrderCronJob> ORDER2ORDERTEMPLATETOORDERCRONJOBORDERTEMPLATECRONJOBHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERTEMPLATETOORDERCRONJOB, false, "orderTemplate", null, false, true, 0);
    protected static final OneToManyHandler<OrderScheduleCronJob> ORDER2ORDERSCHEDULECRONJOBORDERSCHEDULECRONJOBHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERSCHEDULECRONJOB, false, "order", null, false, true, 0);
    protected static final OneToManyHandler<OrderModificationRecord> ORDER2ORDERMODIFICATIONRECORDSMODIFICATIONRECORDSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERMODIFICATIONRECORD, false, "order", null, false, true, 1);
    protected static final OneToManyHandler<ReturnRequest> ORDER2RETURNREQUESTRETURNREQUESTSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.RETURNREQUEST, false, "order", "orderPOS", true, true, 2);
    protected static final OneToManyHandler<OrderProcess> ORDER2ORDERPROCESSORDERPROCESSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERPROCESS, false, "order", null, false, true, 0);
    protected static final OneToManyHandler<ConsignmentEntry> CONSIGNMENTENTRYORDERENTRYRELATIONCONSIGNMENTENTRIESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENTENTRY, false, "orderEntry", null, false, true, 1);
    protected static String PRODUCTVENDORRELATION_SRC_ORDERED = "relation.ProductVendorRelation.source.ordered";
    protected static String PRODUCTVENDORRELATION_TGT_ORDERED = "relation.ProductVendorRelation.target.ordered";
    protected static String PRODUCTVENDORRELATION_MARKMODIFIED = "relation.ProductVendorRelation.markmodified";
    protected static String PRODUCTDELIVERYMODERELATION_SRC_ORDERED = "relation.ProductDeliveryModeRelation.source.ordered";
    protected static String PRODUCTDELIVERYMODERELATION_TGT_ORDERED = "relation.ProductDeliveryModeRelation.target.ordered";
    protected static String PRODUCTDELIVERYMODERELATION_MARKMODIFIED = "relation.ProductDeliveryModeRelation.markmodified";
    protected static String STOCKLEVELPRODUCTRELATION_SRC_ORDERED = "relation.StockLevelProductRelation.source.ordered";
    protected static String STOCKLEVELPRODUCTRELATION_TGT_ORDERED = "relation.StockLevelProductRelation.target.ordered";
    protected static String STOCKLEVELPRODUCTRELATION_MARKMODIFIED = "relation.StockLevelProductRelation.markmodified";
    protected static final OneToManyHandler<CartToOrderCronJob> DELIVERYADDRESSS2CARTTOORDERCRONJOBDELIVERYADDRESSS2CARTTOORDERCRONJOBHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CARTTOORDERCRONJOB, false, "deliveryAddress", null, false, true, 0);
    protected static final OneToManyHandler<CartToOrderCronJob> PAYMENTADDRESSS2CARTTOORDERCRONJOBPAYMENTADDRESSS2CARTTOORDERCRONJOBHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CARTTOORDERCRONJOB, false, "paymentAddress", null, false, true, 0);
    protected static String CATALOGSFORBASESTORES_SRC_ORDERED = "relation.CatalogsForBaseStores.source.ordered";
    protected static String CATALOGSFORBASESTORES_TGT_ORDERED = "relation.CatalogsForBaseStores.target.ordered";
    protected static String CATALOGSFORBASESTORES_MARKMODIFIED = "relation.CatalogsForBaseStores.markmodified";
    protected static final OneToManyHandler<Consignment> CONSIGNMENTORDERRELATIONCONSIGNMENTSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENT, false, "order", null, false, true, 1);
    protected static final OneToManyHandler<CartToOrderCronJob> CART2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CARTTOORDERCRONJOB, false, "cart", null, false, true, 0);
    protected static final OneToManyHandler<CartToOrderCronJob> PAYMENTINFO2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CARTTOORDERCRONJOB, false, "paymentInfo", null, false, true, 0);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("versionID", Item.AttributeMode.INITIAL);
        tmp.put("originalVersion", Item.AttributeMode.INITIAL);
        tmp.put("fraudulent", Item.AttributeMode.INITIAL);
        tmp.put("potentiallyFraudulent", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.order.Order", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("chosenVendor", Item.AttributeMode.INITIAL);
        tmp.put("deliveryAddress", Item.AttributeMode.INITIAL);
        tmp.put("deliveryMode", Item.AttributeMode.INITIAL);
        tmp.put("namedDeliveryDate", Item.AttributeMode.INITIAL);
        tmp.put("quantityStatus", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("productOrderLimit", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Collection<BarcodeMedia> getBarcodes(Product item)
    {
        return getBarcodes(getSession().getSessionContext(), item);
    }


    public Collection<BaseStore> getBaseStores(SessionContext ctx, Catalog item)
    {
        List<BaseStore> items = item.getLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, "BaseStore", null,
                        Utilities.getRelationOrderingOverride(CATALOGSFORBASESTORES_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<BaseStore> getBaseStores(Catalog item)
    {
        return getBaseStores(getSession().getSessionContext(), item);
    }


    public long getBaseStoresCount(SessionContext ctx, Catalog item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, "BaseStore", null);
    }


    public long getBaseStoresCount(Catalog item)
    {
        return getBaseStoresCount(getSession().getSessionContext(), item);
    }


    public void setBaseStores(SessionContext ctx, Catalog item, Collection<BaseStore> value)
    {
        item.setLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, null, value,
                        Utilities.getRelationOrderingOverride(CATALOGSFORBASESTORES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORBASESTORES_MARKMODIFIED));
    }


    public void setBaseStores(Catalog item, Collection<BaseStore> value)
    {
        setBaseStores(getSession().getSessionContext(), item, value);
    }


    public void addToBaseStores(SessionContext ctx, Catalog item, BaseStore value)
    {
        item.addLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATALOGSFORBASESTORES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORBASESTORES_MARKMODIFIED));
    }


    public void addToBaseStores(Catalog item, BaseStore value)
    {
        addToBaseStores(getSession().getSessionContext(), item, value);
    }


    public void removeFromBaseStores(SessionContext ctx, Catalog item, BaseStore value)
    {
        item.removeLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATALOGSFORBASESTORES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORBASESTORES_MARKMODIFIED));
    }


    public void removeFromBaseStores(Catalog item, BaseStore value)
    {
        removeFromBaseStores(getSession().getSessionContext(), item, value);
    }


    public Collection<CartToOrderCronJob> getCartToOrderCronJob(SessionContext ctx, Cart item)
    {
        return CART2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CartToOrderCronJob> getCartToOrderCronJob(Cart item)
    {
        return getCartToOrderCronJob(getSession().getSessionContext(), item);
    }


    public void setCartToOrderCronJob(SessionContext ctx, Cart item, Collection<CartToOrderCronJob> value)
    {
        CART2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCartToOrderCronJob(Cart item, Collection<CartToOrderCronJob> value)
    {
        setCartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public void addToCartToOrderCronJob(SessionContext ctx, Cart item, CartToOrderCronJob value)
    {
        CART2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCartToOrderCronJob(Cart item, CartToOrderCronJob value)
    {
        addToCartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public void removeFromCartToOrderCronJob(SessionContext ctx, Cart item, CartToOrderCronJob value)
    {
        CART2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCartToOrderCronJob(Cart item, CartToOrderCronJob value)
    {
        removeFromCartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public Collection<CartToOrderCronJob> getCartToOrderCronJob(SessionContext ctx, PaymentInfo item)
    {
        return PAYMENTINFO2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CartToOrderCronJob> getCartToOrderCronJob(PaymentInfo item)
    {
        return getCartToOrderCronJob(getSession().getSessionContext(), item);
    }


    public void setCartToOrderCronJob(SessionContext ctx, PaymentInfo item, Collection<CartToOrderCronJob> value)
    {
        PAYMENTINFO2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCartToOrderCronJob(PaymentInfo item, Collection<CartToOrderCronJob> value)
    {
        setCartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public void addToCartToOrderCronJob(SessionContext ctx, PaymentInfo item, CartToOrderCronJob value)
    {
        PAYMENTINFO2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCartToOrderCronJob(PaymentInfo item, CartToOrderCronJob value)
    {
        addToCartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public void removeFromCartToOrderCronJob(SessionContext ctx, PaymentInfo item, CartToOrderCronJob value)
    {
        PAYMENTINFO2CARTTOORDERCRONJOBCARTTOORDERCRONJOBHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCartToOrderCronJob(PaymentInfo item, CartToOrderCronJob value)
    {
        removeFromCartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public Vendor getChosenVendor(SessionContext ctx, AbstractOrderEntry item)
    {
        return (Vendor)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.CHOSENVENDOR);
    }


    public Vendor getChosenVendor(AbstractOrderEntry item)
    {
        return getChosenVendor(getSession().getSessionContext(), item);
    }


    public void setChosenVendor(SessionContext ctx, AbstractOrderEntry item, Vendor value)
    {
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.CHOSENVENDOR, value);
    }


    public void setChosenVendor(AbstractOrderEntry item, Vendor value)
    {
        setChosenVendor(getSession().getSessionContext(), item, value);
    }


    public Set<ConsignmentEntry> getConsignmentEntries(SessionContext ctx, AbstractOrderEntry item)
    {
        return (Set<ConsignmentEntry>)CONSIGNMENTENTRYORDERENTRYRELATIONCONSIGNMENTENTRIESHANDLER.getValues(ctx, (Item)item);
    }


    public Set<ConsignmentEntry> getConsignmentEntries(AbstractOrderEntry item)
    {
        return getConsignmentEntries(getSession().getSessionContext(), item);
    }


    public void setConsignmentEntries(SessionContext ctx, AbstractOrderEntry item, Set<ConsignmentEntry> value)
    {
        CONSIGNMENTENTRYORDERENTRYRELATIONCONSIGNMENTENTRIESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setConsignmentEntries(AbstractOrderEntry item, Set<ConsignmentEntry> value)
    {
        setConsignmentEntries(getSession().getSessionContext(), item, value);
    }


    public void addToConsignmentEntries(SessionContext ctx, AbstractOrderEntry item, ConsignmentEntry value)
    {
        CONSIGNMENTENTRYORDERENTRYRELATIONCONSIGNMENTENTRIESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToConsignmentEntries(AbstractOrderEntry item, ConsignmentEntry value)
    {
        addToConsignmentEntries(getSession().getSessionContext(), item, value);
    }


    public void removeFromConsignmentEntries(SessionContext ctx, AbstractOrderEntry item, ConsignmentEntry value)
    {
        CONSIGNMENTENTRYORDERENTRYRELATIONCONSIGNMENTENTRIESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromConsignmentEntries(AbstractOrderEntry item, ConsignmentEntry value)
    {
        removeFromConsignmentEntries(getSession().getSessionContext(), item, value);
    }


    public Set<Consignment> getConsignments(SessionContext ctx, AbstractOrder item)
    {
        return (Set<Consignment>)CONSIGNMENTORDERRELATIONCONSIGNMENTSHANDLER.getValues(ctx, (Item)item);
    }


    public Set<Consignment> getConsignments(AbstractOrder item)
    {
        return getConsignments(getSession().getSessionContext(), item);
    }


    public void setConsignments(SessionContext ctx, AbstractOrder item, Set<Consignment> value)
    {
        CONSIGNMENTORDERRELATIONCONSIGNMENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setConsignments(AbstractOrder item, Set<Consignment> value)
    {
        setConsignments(getSession().getSessionContext(), item, value);
    }


    public void addToConsignments(SessionContext ctx, AbstractOrder item, Consignment value)
    {
        CONSIGNMENTORDERRELATIONCONSIGNMENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToConsignments(AbstractOrder item, Consignment value)
    {
        addToConsignments(getSession().getSessionContext(), item, value);
    }


    public void removeFromConsignments(SessionContext ctx, AbstractOrder item, Consignment value)
    {
        CONSIGNMENTORDERRELATIONCONSIGNMENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromConsignments(AbstractOrder item, Consignment value)
    {
        removeFromConsignments(getSession().getSessionContext(), item, value);
    }


    public BarcodeMedia createBarcodeMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.BARCODEMEDIA);
            return (BarcodeMedia)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BarcodeMedia : " + e.getMessage(), 0);
        }
    }


    public BarcodeMedia createBarcodeMedia(Map attributeValues)
    {
        return createBarcodeMedia(getSession().getSessionContext(), attributeValues);
    }


    public BaseSite createBaseSite(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.BASESITE);
            return (BaseSite)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BaseSite : " + e.getMessage(), 0);
        }
    }


    public BaseSite createBaseSite(Map attributeValues)
    {
        return createBaseSite(getSession().getSessionContext(), attributeValues);
    }


    public BaseStore createBaseStore(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.BASESTORE);
            return (BaseStore)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BaseStore : " + e.getMessage(), 0);
        }
    }


    public BaseStore createBaseStore(Map attributeValues)
    {
        return createBaseStore(getSession().getSessionContext(), attributeValues);
    }


    public Campaign createCampaign(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.CAMPAIGN);
            return (Campaign)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Campaign : " + e.getMessage(), 0);
        }
    }


    public Campaign createCampaign(Map attributeValues)
    {
        return createCampaign(getSession().getSessionContext(), attributeValues);
    }


    public CartToOrderCronJob createCartToOrderCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.CARTTOORDERCRONJOB);
            return (CartToOrderCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CartToOrderCronJob : " + e.getMessage(), 0);
        }
    }


    public CartToOrderCronJob createCartToOrderCronJob(Map attributeValues)
    {
        return createCartToOrderCronJob(getSession().getSessionContext(), attributeValues);
    }


    public Consignment createConsignment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.CONSIGNMENT);
            return (Consignment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Consignment : " + e.getMessage(), 0);
        }
    }


    public Consignment createConsignment(Map attributeValues)
    {
        return createConsignment(getSession().getSessionContext(), attributeValues);
    }


    public ConsignmentEntry createConsignmentEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.CONSIGNMENTENTRY);
            return (ConsignmentEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ConsignmentEntry : " + e.getMessage(), 0);
        }
    }


    public ConsignmentEntry createConsignmentEntry(Map attributeValues)
    {
        return createConsignmentEntry(getSession().getSessionContext(), attributeValues);
    }


    public ConsignmentProcess createConsignmentProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.CONSIGNMENTPROCESS);
            return (ConsignmentProcess)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ConsignmentProcess : " + e.getMessage(), 0);
        }
    }


    public ConsignmentProcess createConsignmentProcess(Map attributeValues)
    {
        return createConsignmentProcess(getSession().getSessionContext(), attributeValues);
    }


    public DeeplinkUrl createDeeplinkUrl(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.DEEPLINKURL);
            return (DeeplinkUrl)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DeeplinkUrl : " + e.getMessage(), 0);
        }
    }


    public DeeplinkUrl createDeeplinkUrl(Map attributeValues)
    {
        return createDeeplinkUrl(getSession().getSessionContext(), attributeValues);
    }


    public DeeplinkUrlRule createDeeplinkUrlRule(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.DEEPLINKURLRULE);
            return (DeeplinkUrlRule)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DeeplinkUrlRule : " + e.getMessage(), 0);
        }
    }


    public DeeplinkUrlRule createDeeplinkUrlRule(Map attributeValues)
    {
        return createDeeplinkUrlRule(getSession().getSessionContext(), attributeValues);
    }


    public FraudReport createFraudReport(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.FRAUDREPORT);
            return (FraudReport)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating FraudReport : " + e.getMessage(), 0);
        }
    }


    public FraudReport createFraudReport(Map attributeValues)
    {
        return createFraudReport(getSession().getSessionContext(), attributeValues);
    }


    public FraudSymptomScoring createFraudSymptomScoring(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.FRAUDSYMPTOMSCORING);
            return (FraudSymptomScoring)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating FraudSymptomScoring : " + e.getMessage(), 0);
        }
    }


    public FraudSymptomScoring createFraudSymptomScoring(Map attributeValues)
    {
        return createFraudSymptomScoring(getSession().getSessionContext(), attributeValues);
    }


    public GenericVariantProduct createGenericVariantProduct(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.GENERICVARIANTPRODUCT);
            return (GenericVariantProduct)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating GenericVariantProduct : " + e.getMessage(), 0);
        }
    }


    public GenericVariantProduct createGenericVariantProduct(Map attributeValues)
    {
        return createGenericVariantProduct(getSession().getSessionContext(), attributeValues);
    }


    public GeocodeAddressesCronJob createGeocodeAddressesCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.GEOCODEADDRESSESCRONJOB);
            return (GeocodeAddressesCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating GeocodeAddressesCronJob : " + e.getMessage(), 0);
        }
    }


    public GeocodeAddressesCronJob createGeocodeAddressesCronJob(Map attributeValues)
    {
        return createGeocodeAddressesCronJob(getSession().getSessionContext(), attributeValues);
    }


    public MultiAddressInMemoryCart createMultiAddressInMemoryCart(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.MULTIADDRESSINMEMORYCART);
            return (MultiAddressInMemoryCart)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MultiAddressInMemoryCart : " + e.getMessage(), 0);
        }
    }


    public MultiAddressInMemoryCart createMultiAddressInMemoryCart(Map attributeValues)
    {
        return createMultiAddressInMemoryCart(getSession().getSessionContext(), attributeValues);
    }


    public OpeningSchedule createOpeningSchedule(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.OPENINGSCHEDULE);
            return (OpeningSchedule)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OpeningSchedule : " + e.getMessage(), 0);
        }
    }


    public OpeningSchedule createOpeningSchedule(Map attributeValues)
    {
        return createOpeningSchedule(getSession().getSessionContext(), attributeValues);
    }


    public OrderCancelConfig createOrderCancelConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERCANCELCONFIG);
            return (OrderCancelConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderCancelConfig : " + e.getMessage(), 0);
        }
    }


    public OrderCancelConfig createOrderCancelConfig(Map attributeValues)
    {
        return createOrderCancelConfig(getSession().getSessionContext(), attributeValues);
    }


    public OrderCancelRecord createOrderCancelRecord(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERCANCELRECORD);
            return (OrderCancelRecord)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderCancelRecord : " + e.getMessage(), 0);
        }
    }


    public OrderCancelRecord createOrderCancelRecord(Map attributeValues)
    {
        return createOrderCancelRecord(getSession().getSessionContext(), attributeValues);
    }


    public OrderCancelRecordEntry createOrderCancelRecordEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERCANCELRECORDENTRY);
            return (OrderCancelRecordEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderCancelRecordEntry : " + e.getMessage(), 0);
        }
    }


    public OrderCancelRecordEntry createOrderCancelRecordEntry(Map attributeValues)
    {
        return createOrderCancelRecordEntry(getSession().getSessionContext(), attributeValues);
    }


    public OrderEntryCancelRecordEntry createOrderEntryCancelRecordEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERENTRYCANCELRECORDENTRY);
            return (OrderEntryCancelRecordEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderEntryCancelRecordEntry : " + e.getMessage(), 0);
        }
    }


    public OrderEntryCancelRecordEntry createOrderEntryCancelRecordEntry(Map attributeValues)
    {
        return createOrderEntryCancelRecordEntry(getSession().getSessionContext(), attributeValues);
    }


    public OrderEntryReturnRecordEntry createOrderEntryReturnRecordEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERENTRYRETURNRECORDENTRY);
            return (OrderEntryReturnRecordEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderEntryReturnRecordEntry : " + e.getMessage(), 0);
        }
    }


    public OrderEntryReturnRecordEntry createOrderEntryReturnRecordEntry(Map attributeValues)
    {
        return createOrderEntryReturnRecordEntry(getSession().getSessionContext(), attributeValues);
    }


    public OrderHistoryEntry createOrderHistoryEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERHISTORYENTRY);
            return (OrderHistoryEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderHistoryEntry : " + e.getMessage(), 0);
        }
    }


    public OrderHistoryEntry createOrderHistoryEntry(Map attributeValues)
    {
        return createOrderHistoryEntry(getSession().getSessionContext(), attributeValues);
    }


    public OrderProcess createOrderProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERPROCESS);
            return (OrderProcess)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderProcess : " + e.getMessage(), 0);
        }
    }


    public OrderProcess createOrderProcess(Map attributeValues)
    {
        return createOrderProcess(getSession().getSessionContext(), attributeValues);
    }


    public OrderRefundRecordEntry createOrderRefundRecordEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERREFUNDRECORDENTRY);
            return (OrderRefundRecordEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderRefundRecordEntry : " + e.getMessage(), 0);
        }
    }


    public OrderRefundRecordEntry createOrderRefundRecordEntry(Map attributeValues)
    {
        return createOrderRefundRecordEntry(getSession().getSessionContext(), attributeValues);
    }


    public OrderReplacementRecordEntry createOrderReplacementRecordEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERREPLACEMENTRECORDENTRY);
            return (OrderReplacementRecordEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderReplacementRecordEntry : " + e.getMessage(), 0);
        }
    }


    public OrderReplacementRecordEntry createOrderReplacementRecordEntry(Map attributeValues)
    {
        return createOrderReplacementRecordEntry(getSession().getSessionContext(), attributeValues);
    }


    public OrderReturnRecord createOrderReturnRecord(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERRETURNRECORD);
            return (OrderReturnRecord)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderReturnRecord : " + e.getMessage(), 0);
        }
    }


    public OrderReturnRecord createOrderReturnRecord(Map attributeValues)
    {
        return createOrderReturnRecord(getSession().getSessionContext(), attributeValues);
    }


    public OrderScheduleCronJob createOrderScheduleCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERSCHEDULECRONJOB);
            return (OrderScheduleCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderScheduleCronJob : " + e.getMessage(), 0);
        }
    }


    public OrderScheduleCronJob createOrderScheduleCronJob(Map attributeValues)
    {
        return createOrderScheduleCronJob(getSession().getSessionContext(), attributeValues);
    }


    public OrderTemplateToOrderCronJob createOrderTemplateToOrderCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.ORDERTEMPLATETOORDERCRONJOB);
            return (OrderTemplateToOrderCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderTemplateToOrderCronJob : " + e.getMessage(), 0);
        }
    }


    public OrderTemplateToOrderCronJob createOrderTemplateToOrderCronJob(Map attributeValues)
    {
        return createOrderTemplateToOrderCronJob(getSession().getSessionContext(), attributeValues);
    }


    public PointOfService createPointOfService(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.POINTOFSERVICE);
            return (PointOfService)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PointOfService : " + e.getMessage(), 0);
        }
    }


    public PointOfService createPointOfService(Map attributeValues)
    {
        return createPointOfService(getSession().getSessionContext(), attributeValues);
    }


    public ProductOrderLimit createProductOrderLimit(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.PRODUCTORDERLIMIT);
            return (ProductOrderLimit)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductOrderLimit : " + e.getMessage(), 0);
        }
    }


    public ProductOrderLimit createProductOrderLimit(Map attributeValues)
    {
        return createProductOrderLimit(getSession().getSessionContext(), attributeValues);
    }


    public ProductTaxCode createProductTaxCode(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.PRODUCTTAXCODE);
            return (ProductTaxCode)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductTaxCode : " + e.getMessage(), 0);
        }
    }


    public ProductTaxCode createProductTaxCode(Map attributeValues)
    {
        return createProductTaxCode(getSession().getSessionContext(), attributeValues);
    }


    public RefundEntry createRefundEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.REFUNDENTRY);
            return (RefundEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RefundEntry : " + e.getMessage(), 0);
        }
    }


    public RefundEntry createRefundEntry(Map attributeValues)
    {
        return createRefundEntry(getSession().getSessionContext(), attributeValues);
    }


    public ReplacementEntry createReplacementEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.REPLACEMENTENTRY);
            return (ReplacementEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ReplacementEntry : " + e.getMessage(), 0);
        }
    }


    public ReplacementEntry createReplacementEntry(Map attributeValues)
    {
        return createReplacementEntry(getSession().getSessionContext(), attributeValues);
    }


    public ReplacementOrder createReplacementOrder(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.REPLACEMENTORDER);
            return (ReplacementOrder)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ReplacementOrder : " + e.getMessage(), 0);
        }
    }


    public ReplacementOrder createReplacementOrder(Map attributeValues)
    {
        return createReplacementOrder(getSession().getSessionContext(), attributeValues);
    }


    public ReplacementOrderEntry createReplacementOrderEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.REPLACEMENTORDERENTRY);
            return (ReplacementOrderEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ReplacementOrderEntry : " + e.getMessage(), 0);
        }
    }


    public ReplacementOrderEntry createReplacementOrderEntry(Map attributeValues)
    {
        return createReplacementOrderEntry(getSession().getSessionContext(), attributeValues);
    }


    public ReturnProcess createReturnProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.RETURNPROCESS);
            return (ReturnProcess)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ReturnProcess : " + e.getMessage(), 0);
        }
    }


    public ReturnProcess createReturnProcess(Map attributeValues)
    {
        return createReturnProcess(getSession().getSessionContext(), attributeValues);
    }


    public ReturnRequest createReturnRequest(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.RETURNREQUEST);
            return (ReturnRequest)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ReturnRequest : " + e.getMessage(), 0);
        }
    }


    public ReturnRequest createReturnRequest(Map attributeValues)
    {
        return createReturnRequest(getSession().getSessionContext(), attributeValues);
    }


    public SpecialOpeningDay createSpecialOpeningDay(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.SPECIALOPENINGDAY);
            return (SpecialOpeningDay)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SpecialOpeningDay : " + e.getMessage(), 0);
        }
    }


    public SpecialOpeningDay createSpecialOpeningDay(Map attributeValues)
    {
        return createSpecialOpeningDay(getSession().getSessionContext(), attributeValues);
    }


    public StockLevel createStockLevel(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.STOCKLEVEL);
            return (StockLevel)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating StockLevel : " + e.getMessage(), 0);
        }
    }


    public StockLevel createStockLevel(Map attributeValues)
    {
        return createStockLevel(getSession().getSessionContext(), attributeValues);
    }


    public StockLevelHistoryEntry createStockLevelHistoryEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.STOCKLEVELHISTORYENTRY);
            return (StockLevelHistoryEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating StockLevelHistoryEntry : " + e.getMessage(), 0);
        }
    }


    public StockLevelHistoryEntry createStockLevelHistoryEntry(Map attributeValues)
    {
        return createStockLevelHistoryEntry(getSession().getSessionContext(), attributeValues);
    }


    public VariantCategory createVariantCategory(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.VARIANTCATEGORY);
            return (VariantCategory)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating VariantCategory : " + e.getMessage(), 0);
        }
    }


    public VariantCategory createVariantCategory(Map attributeValues)
    {
        return createVariantCategory(getSession().getSessionContext(), attributeValues);
    }


    public VariantValueCategory createVariantValueCategory(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.VARIANTVALUECATEGORY);
            return (VariantValueCategory)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating VariantValueCategory : " + e.getMessage(), 0);
        }
    }


    public VariantValueCategory createVariantValueCategory(Map attributeValues)
    {
        return createVariantValueCategory(getSession().getSessionContext(), attributeValues);
    }


    public Vendor createVendor(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.VENDOR);
            return (Vendor)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Vendor : " + e.getMessage(), 0);
        }
    }


    public Vendor createVendor(Map attributeValues)
    {
        return createVendor(getSession().getSessionContext(), attributeValues);
    }


    public Warehouse createWarehouse(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.WAREHOUSE);
            return (Warehouse)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Warehouse : " + e.getMessage(), 0);
        }
    }


    public Warehouse createWarehouse(Map attributeValues)
    {
        return createWarehouse(getSession().getSessionContext(), attributeValues);
    }


    public WeekdayOpeningDay createWeekdayOpeningDay(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBasecommerceConstants.TC.WEEKDAYOPENINGDAY);
            return (WeekdayOpeningDay)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WeekdayOpeningDay : " + e.getMessage(), 0);
        }
    }


    public WeekdayOpeningDay createWeekdayOpeningDay(Map attributeValues)
    {
        return createWeekdayOpeningDay(getSession().getSessionContext(), attributeValues);
    }


    public Address getDeliveryAddress(SessionContext ctx, AbstractOrderEntry item)
    {
        return (Address)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.DELIVERYADDRESS);
    }


    public Address getDeliveryAddress(AbstractOrderEntry item)
    {
        return getDeliveryAddress(getSession().getSessionContext(), item);
    }


    public void setDeliveryAddress(SessionContext ctx, AbstractOrderEntry item, Address value)
    {
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.DELIVERYADDRESS, value);
    }


    public void setDeliveryAddress(AbstractOrderEntry item, Address value)
    {
        setDeliveryAddress(getSession().getSessionContext(), item, value);
    }


    public Collection<CartToOrderCronJob> getDeliveryAddresss2CartToOrderCronJob(SessionContext ctx, Address item)
    {
        return DELIVERYADDRESSS2CARTTOORDERCRONJOBDELIVERYADDRESSS2CARTTOORDERCRONJOBHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CartToOrderCronJob> getDeliveryAddresss2CartToOrderCronJob(Address item)
    {
        return getDeliveryAddresss2CartToOrderCronJob(getSession().getSessionContext(), item);
    }


    public void setDeliveryAddresss2CartToOrderCronJob(SessionContext ctx, Address item, Collection<CartToOrderCronJob> value)
    {
        DELIVERYADDRESSS2CARTTOORDERCRONJOBDELIVERYADDRESSS2CARTTOORDERCRONJOBHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setDeliveryAddresss2CartToOrderCronJob(Address item, Collection<CartToOrderCronJob> value)
    {
        setDeliveryAddresss2CartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public void addToDeliveryAddresss2CartToOrderCronJob(SessionContext ctx, Address item, CartToOrderCronJob value)
    {
        DELIVERYADDRESSS2CARTTOORDERCRONJOBDELIVERYADDRESSS2CARTTOORDERCRONJOBHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToDeliveryAddresss2CartToOrderCronJob(Address item, CartToOrderCronJob value)
    {
        addToDeliveryAddresss2CartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public void removeFromDeliveryAddresss2CartToOrderCronJob(SessionContext ctx, Address item, CartToOrderCronJob value)
    {
        DELIVERYADDRESSS2CARTTOORDERCRONJOBDELIVERYADDRESSS2CARTTOORDERCRONJOBHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromDeliveryAddresss2CartToOrderCronJob(Address item, CartToOrderCronJob value)
    {
        removeFromDeliveryAddresss2CartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public DeliveryMode getDeliveryMode(SessionContext ctx, AbstractOrderEntry item)
    {
        return (DeliveryMode)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.DELIVERYMODE);
    }


    public DeliveryMode getDeliveryMode(AbstractOrderEntry item)
    {
        return getDeliveryMode(getSession().getSessionContext(), item);
    }


    public void setDeliveryMode(SessionContext ctx, AbstractOrderEntry item, DeliveryMode value)
    {
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.DELIVERYMODE, value);
    }


    public void setDeliveryMode(AbstractOrderEntry item, DeliveryMode value)
    {
        setDeliveryMode(getSession().getSessionContext(), item, value);
    }


    public Set<DeliveryMode> getDeliveryModes(SessionContext ctx, Product item)
    {
        List<DeliveryMode> items = item.getLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTDELIVERYMODERELATION, "DeliveryMode", null,
                        Utilities.getRelationOrderingOverride(PRODUCTDELIVERYMODERELATION_SRC_ORDERED, true), false);
        return new LinkedHashSet<>(items);
    }


    public Set<DeliveryMode> getDeliveryModes(Product item)
    {
        return getDeliveryModes(getSession().getSessionContext(), item);
    }


    public long getDeliveryModesCount(SessionContext ctx, Product item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTDELIVERYMODERELATION, "DeliveryMode", null);
    }


    public long getDeliveryModesCount(Product item)
    {
        return getDeliveryModesCount(getSession().getSessionContext(), item);
    }


    public void setDeliveryModes(SessionContext ctx, Product item, Set<DeliveryMode> value)
    {
        item.setLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTDELIVERYMODERELATION, null, value,
                        Utilities.getRelationOrderingOverride(PRODUCTDELIVERYMODERELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTDELIVERYMODERELATION_MARKMODIFIED));
    }


    public void setDeliveryModes(Product item, Set<DeliveryMode> value)
    {
        setDeliveryModes(getSession().getSessionContext(), item, value);
    }


    public void addToDeliveryModes(SessionContext ctx, Product item, DeliveryMode value)
    {
        item.addLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTDELIVERYMODERELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTDELIVERYMODERELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTDELIVERYMODERELATION_MARKMODIFIED));
    }


    public void addToDeliveryModes(Product item, DeliveryMode value)
    {
        addToDeliveryModes(getSession().getSessionContext(), item, value);
    }


    public void removeFromDeliveryModes(SessionContext ctx, Product item, DeliveryMode value)
    {
        item.removeLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTDELIVERYMODERELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTDELIVERYMODERELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTDELIVERYMODERELATION_MARKMODIFIED));
    }


    public void removeFromDeliveryModes(Product item, DeliveryMode value)
    {
        removeFromDeliveryModes(getSession().getSessionContext(), item, value);
    }


    public Set<FraudReport> getFraudReports(SessionContext ctx, Order item)
    {
        return (Set<FraudReport>)ORDERFRAUDREPORTRELATIONFRAUDREPORTSHANDLER.getValues(ctx, (Item)item);
    }


    public Set<FraudReport> getFraudReports(Order item)
    {
        return getFraudReports(getSession().getSessionContext(), item);
    }


    public void setFraudReports(SessionContext ctx, Order item, Set<FraudReport> value)
    {
        ORDERFRAUDREPORTRELATIONFRAUDREPORTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setFraudReports(Order item, Set<FraudReport> value)
    {
        setFraudReports(getSession().getSessionContext(), item, value);
    }


    public void addToFraudReports(SessionContext ctx, Order item, FraudReport value)
    {
        ORDERFRAUDREPORTRELATIONFRAUDREPORTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToFraudReports(Order item, FraudReport value)
    {
        addToFraudReports(getSession().getSessionContext(), item, value);
    }


    public void removeFromFraudReports(SessionContext ctx, Order item, FraudReport value)
    {
        ORDERFRAUDREPORTRELATIONFRAUDREPORTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromFraudReports(Order item, FraudReport value)
    {
        removeFromFraudReports(getSession().getSessionContext(), item, value);
    }


    public Boolean isFraudulent(SessionContext ctx, Order item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.Order.FRAUDULENT);
    }


    public Boolean isFraudulent(Order item)
    {
        return isFraudulent(getSession().getSessionContext(), item);
    }


    public boolean isFraudulentAsPrimitive(SessionContext ctx, Order item)
    {
        Boolean value = isFraudulent(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFraudulentAsPrimitive(Order item)
    {
        return isFraudulentAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setFraudulent(SessionContext ctx, Order item, Boolean value)
    {
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.Order.FRAUDULENT, value);
    }


    public void setFraudulent(Order item, Boolean value)
    {
        setFraudulent(getSession().getSessionContext(), item, value);
    }


    public void setFraudulent(SessionContext ctx, Order item, boolean value)
    {
        setFraudulent(ctx, item, Boolean.valueOf(value));
    }


    public void setFraudulent(Order item, boolean value)
    {
        setFraudulent(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "basecommerce";
    }


    public List<OrderHistoryEntry> getHistoryEntries(SessionContext ctx, Order item)
    {
        return (List<OrderHistoryEntry>)ORDERHISTORYRELATIONHISTORYENTRIESHANDLER.getValues(ctx, (Item)item);
    }


    public List<OrderHistoryEntry> getHistoryEntries(Order item)
    {
        return getHistoryEntries(getSession().getSessionContext(), item);
    }


    public void setHistoryEntries(SessionContext ctx, Order item, List<OrderHistoryEntry> value)
    {
        ORDERHISTORYRELATIONHISTORYENTRIESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setHistoryEntries(Order item, List<OrderHistoryEntry> value)
    {
        setHistoryEntries(getSession().getSessionContext(), item, value);
    }


    public void addToHistoryEntries(SessionContext ctx, Order item, OrderHistoryEntry value)
    {
        ORDERHISTORYRELATIONHISTORYENTRIESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToHistoryEntries(Order item, OrderHistoryEntry value)
    {
        addToHistoryEntries(getSession().getSessionContext(), item, value);
    }


    public void removeFromHistoryEntries(SessionContext ctx, Order item, OrderHistoryEntry value)
    {
        ORDERHISTORYRELATIONHISTORYENTRIESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromHistoryEntries(Order item, OrderHistoryEntry value)
    {
        removeFromHistoryEntries(getSession().getSessionContext(), item, value);
    }


    public Set<OrderModificationRecord> getModificationRecords(SessionContext ctx, Order item)
    {
        return (Set<OrderModificationRecord>)ORDER2ORDERMODIFICATIONRECORDSMODIFICATIONRECORDSHANDLER.getValues(ctx, (Item)item);
    }


    public Set<OrderModificationRecord> getModificationRecords(Order item)
    {
        return getModificationRecords(getSession().getSessionContext(), item);
    }


    public void setModificationRecords(SessionContext ctx, Order item, Set<OrderModificationRecord> value)
    {
        ORDER2ORDERMODIFICATIONRECORDSMODIFICATIONRECORDSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setModificationRecords(Order item, Set<OrderModificationRecord> value)
    {
        setModificationRecords(getSession().getSessionContext(), item, value);
    }


    public void addToModificationRecords(SessionContext ctx, Order item, OrderModificationRecord value)
    {
        ORDER2ORDERMODIFICATIONRECORDSMODIFICATIONRECORDSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToModificationRecords(Order item, OrderModificationRecord value)
    {
        addToModificationRecords(getSession().getSessionContext(), item, value);
    }


    public void removeFromModificationRecords(SessionContext ctx, Order item, OrderModificationRecord value)
    {
        ORDER2ORDERMODIFICATIONRECORDSMODIFICATIONRECORDSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromModificationRecords(Order item, OrderModificationRecord value)
    {
        removeFromModificationRecords(getSession().getSessionContext(), item, value);
    }


    public Date getNamedDeliveryDate(SessionContext ctx, AbstractOrderEntry item)
    {
        return (Date)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.NAMEDDELIVERYDATE);
    }


    public Date getNamedDeliveryDate(AbstractOrderEntry item)
    {
        return getNamedDeliveryDate(getSession().getSessionContext(), item);
    }


    public void setNamedDeliveryDate(SessionContext ctx, AbstractOrderEntry item, Date value)
    {
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.NAMEDDELIVERYDATE, value);
    }


    public void setNamedDeliveryDate(AbstractOrderEntry item, Date value)
    {
        setNamedDeliveryDate(getSession().getSessionContext(), item, value);
    }


    public Collection<OrderProcess> getOrderProcess(SessionContext ctx, Order item)
    {
        return ORDER2ORDERPROCESSORDERPROCESSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<OrderProcess> getOrderProcess(Order item)
    {
        return getOrderProcess(getSession().getSessionContext(), item);
    }


    public void setOrderProcess(SessionContext ctx, Order item, Collection<OrderProcess> value)
    {
        ORDER2ORDERPROCESSORDERPROCESSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setOrderProcess(Order item, Collection<OrderProcess> value)
    {
        setOrderProcess(getSession().getSessionContext(), item, value);
    }


    public void addToOrderProcess(SessionContext ctx, Order item, OrderProcess value)
    {
        ORDER2ORDERPROCESSORDERPROCESSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToOrderProcess(Order item, OrderProcess value)
    {
        addToOrderProcess(getSession().getSessionContext(), item, value);
    }


    public void removeFromOrderProcess(SessionContext ctx, Order item, OrderProcess value)
    {
        ORDER2ORDERPROCESSORDERPROCESSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromOrderProcess(Order item, OrderProcess value)
    {
        removeFromOrderProcess(getSession().getSessionContext(), item, value);
    }


    public Collection<OrderScheduleCronJob> getOrderScheduleCronJob(SessionContext ctx, Order item)
    {
        return ORDER2ORDERSCHEDULECRONJOBORDERSCHEDULECRONJOBHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<OrderScheduleCronJob> getOrderScheduleCronJob(Order item)
    {
        return getOrderScheduleCronJob(getSession().getSessionContext(), item);
    }


    public void setOrderScheduleCronJob(SessionContext ctx, Order item, Collection<OrderScheduleCronJob> value)
    {
        ORDER2ORDERSCHEDULECRONJOBORDERSCHEDULECRONJOBHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setOrderScheduleCronJob(Order item, Collection<OrderScheduleCronJob> value)
    {
        setOrderScheduleCronJob(getSession().getSessionContext(), item, value);
    }


    public void addToOrderScheduleCronJob(SessionContext ctx, Order item, OrderScheduleCronJob value)
    {
        ORDER2ORDERSCHEDULECRONJOBORDERSCHEDULECRONJOBHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToOrderScheduleCronJob(Order item, OrderScheduleCronJob value)
    {
        addToOrderScheduleCronJob(getSession().getSessionContext(), item, value);
    }


    public void removeFromOrderScheduleCronJob(SessionContext ctx, Order item, OrderScheduleCronJob value)
    {
        ORDER2ORDERSCHEDULECRONJOBORDERSCHEDULECRONJOBHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromOrderScheduleCronJob(Order item, OrderScheduleCronJob value)
    {
        removeFromOrderScheduleCronJob(getSession().getSessionContext(), item, value);
    }


    public Collection<OrderTemplateToOrderCronJob> getOrderTemplateCronJob(SessionContext ctx, Order item)
    {
        return ORDER2ORDERTEMPLATETOORDERCRONJOBORDERTEMPLATECRONJOBHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<OrderTemplateToOrderCronJob> getOrderTemplateCronJob(Order item)
    {
        return getOrderTemplateCronJob(getSession().getSessionContext(), item);
    }


    public void setOrderTemplateCronJob(SessionContext ctx, Order item, Collection<OrderTemplateToOrderCronJob> value)
    {
        ORDER2ORDERTEMPLATETOORDERCRONJOBORDERTEMPLATECRONJOBHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setOrderTemplateCronJob(Order item, Collection<OrderTemplateToOrderCronJob> value)
    {
        setOrderTemplateCronJob(getSession().getSessionContext(), item, value);
    }


    public void addToOrderTemplateCronJob(SessionContext ctx, Order item, OrderTemplateToOrderCronJob value)
    {
        ORDER2ORDERTEMPLATETOORDERCRONJOBORDERTEMPLATECRONJOBHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToOrderTemplateCronJob(Order item, OrderTemplateToOrderCronJob value)
    {
        addToOrderTemplateCronJob(getSession().getSessionContext(), item, value);
    }


    public void removeFromOrderTemplateCronJob(SessionContext ctx, Order item, OrderTemplateToOrderCronJob value)
    {
        ORDER2ORDERTEMPLATETOORDERCRONJOBORDERTEMPLATECRONJOBHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromOrderTemplateCronJob(Order item, OrderTemplateToOrderCronJob value)
    {
        removeFromOrderTemplateCronJob(getSession().getSessionContext(), item, value);
    }


    public Order getOriginalVersion(SessionContext ctx, Order item)
    {
        return (Order)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.Order.ORIGINALVERSION);
    }


    public Order getOriginalVersion(Order item)
    {
        return getOriginalVersion(getSession().getSessionContext(), item);
    }


    protected void setOriginalVersion(SessionContext ctx, Order item, Order value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + GeneratedBasecommerceConstants.Attributes.Order.ORIGINALVERSION + "' is not changeable", 0);
        }
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.Order.ORIGINALVERSION, value);
    }


    protected void setOriginalVersion(Order item, Order value)
    {
        setOriginalVersion(getSession().getSessionContext(), item, value);
    }


    public Collection<CartToOrderCronJob> getPaymentAddresss2CartToOrderCronJob(SessionContext ctx, Address item)
    {
        return PAYMENTADDRESSS2CARTTOORDERCRONJOBPAYMENTADDRESSS2CARTTOORDERCRONJOBHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CartToOrderCronJob> getPaymentAddresss2CartToOrderCronJob(Address item)
    {
        return getPaymentAddresss2CartToOrderCronJob(getSession().getSessionContext(), item);
    }


    public void setPaymentAddresss2CartToOrderCronJob(SessionContext ctx, Address item, Collection<CartToOrderCronJob> value)
    {
        PAYMENTADDRESSS2CARTTOORDERCRONJOBPAYMENTADDRESSS2CARTTOORDERCRONJOBHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setPaymentAddresss2CartToOrderCronJob(Address item, Collection<CartToOrderCronJob> value)
    {
        setPaymentAddresss2CartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public void addToPaymentAddresss2CartToOrderCronJob(SessionContext ctx, Address item, CartToOrderCronJob value)
    {
        PAYMENTADDRESSS2CARTTOORDERCRONJOBPAYMENTADDRESSS2CARTTOORDERCRONJOBHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToPaymentAddresss2CartToOrderCronJob(Address item, CartToOrderCronJob value)
    {
        addToPaymentAddresss2CartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public void removeFromPaymentAddresss2CartToOrderCronJob(SessionContext ctx, Address item, CartToOrderCronJob value)
    {
        PAYMENTADDRESSS2CARTTOORDERCRONJOBPAYMENTADDRESSS2CARTTOORDERCRONJOBHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromPaymentAddresss2CartToOrderCronJob(Address item, CartToOrderCronJob value)
    {
        removeFromPaymentAddresss2CartToOrderCronJob(getSession().getSessionContext(), item, value);
    }


    public Boolean isPotentiallyFraudulent(SessionContext ctx, Order item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.Order.POTENTIALLYFRAUDULENT);
    }


    public Boolean isPotentiallyFraudulent(Order item)
    {
        return isPotentiallyFraudulent(getSession().getSessionContext(), item);
    }


    public boolean isPotentiallyFraudulentAsPrimitive(SessionContext ctx, Order item)
    {
        Boolean value = isPotentiallyFraudulent(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPotentiallyFraudulentAsPrimitive(Order item)
    {
        return isPotentiallyFraudulentAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setPotentiallyFraudulent(SessionContext ctx, Order item, Boolean value)
    {
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.Order.POTENTIALLYFRAUDULENT, value);
    }


    public void setPotentiallyFraudulent(Order item, Boolean value)
    {
        setPotentiallyFraudulent(getSession().getSessionContext(), item, value);
    }


    public void setPotentiallyFraudulent(SessionContext ctx, Order item, boolean value)
    {
        setPotentiallyFraudulent(ctx, item, Boolean.valueOf(value));
    }


    public void setPotentiallyFraudulent(Order item, boolean value)
    {
        setPotentiallyFraudulent(getSession().getSessionContext(), item, value);
    }


    public ProductOrderLimit getProductOrderLimit(SessionContext ctx, Product item)
    {
        return (ProductOrderLimit)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.Product.PRODUCTORDERLIMIT);
    }


    public ProductOrderLimit getProductOrderLimit(Product item)
    {
        return getProductOrderLimit(getSession().getSessionContext(), item);
    }


    public void setProductOrderLimit(SessionContext ctx, Product item, ProductOrderLimit value)
    {
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.Product.PRODUCTORDERLIMIT, value);
    }


    public void setProductOrderLimit(Product item, ProductOrderLimit value)
    {
        setProductOrderLimit(getSession().getSessionContext(), item, value);
    }


    public EnumerationValue getQuantityStatus(SessionContext ctx, AbstractOrderEntry item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.QUANTITYSTATUS);
    }


    public EnumerationValue getQuantityStatus(AbstractOrderEntry item)
    {
        return getQuantityStatus(getSession().getSessionContext(), item);
    }


    public void setQuantityStatus(SessionContext ctx, AbstractOrderEntry item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.AbstractOrderEntry.QUANTITYSTATUS, value);
    }


    public void setQuantityStatus(AbstractOrderEntry item, EnumerationValue value)
    {
        setQuantityStatus(getSession().getSessionContext(), item, value);
    }


    public List<ReturnRequest> getReturnRequests(SessionContext ctx, Order item)
    {
        return (List<ReturnRequest>)ORDER2RETURNREQUESTRETURNREQUESTSHANDLER.getValues(ctx, (Item)item);
    }


    public List<ReturnRequest> getReturnRequests(Order item)
    {
        return getReturnRequests(getSession().getSessionContext(), item);
    }


    public void setReturnRequests(SessionContext ctx, Order item, List<ReturnRequest> value)
    {
        ORDER2RETURNREQUESTRETURNREQUESTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setReturnRequests(Order item, List<ReturnRequest> value)
    {
        setReturnRequests(getSession().getSessionContext(), item, value);
    }


    public void addToReturnRequests(SessionContext ctx, Order item, ReturnRequest value)
    {
        ORDER2RETURNREQUESTRETURNREQUESTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToReturnRequests(Order item, ReturnRequest value)
    {
        addToReturnRequests(getSession().getSessionContext(), item, value);
    }


    public void removeFromReturnRequests(SessionContext ctx, Order item, ReturnRequest value)
    {
        ORDER2RETURNREQUESTRETURNREQUESTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromReturnRequests(Order item, ReturnRequest value)
    {
        removeFromReturnRequests(getSession().getSessionContext(), item, value);
    }


    public Set<StockLevel> getStockLevels(SessionContext ctx, Product item)
    {
        List<StockLevel> items = item.getLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, "StockLevel", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<StockLevel> getStockLevels(Product item)
    {
        return getStockLevels(getSession().getSessionContext(), item);
    }


    public long getStockLevelsCount(SessionContext ctx, Product item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, "StockLevel", null);
    }


    public long getStockLevelsCount(Product item)
    {
        return getStockLevelsCount(getSession().getSessionContext(), item);
    }


    public void setStockLevels(SessionContext ctx, Product item, Set<StockLevel> value)
    {
        item.setLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(STOCKLEVELPRODUCTRELATION_MARKMODIFIED));
    }


    public void setStockLevels(Product item, Set<StockLevel> value)
    {
        setStockLevels(getSession().getSessionContext(), item, value);
    }


    public void addToStockLevels(SessionContext ctx, Product item, StockLevel value)
    {
        item.addLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STOCKLEVELPRODUCTRELATION_MARKMODIFIED));
    }


    public void addToStockLevels(Product item, StockLevel value)
    {
        addToStockLevels(getSession().getSessionContext(), item, value);
    }


    public void removeFromStockLevels(SessionContext ctx, Product item, StockLevel value)
    {
        item.removeLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STOCKLEVELPRODUCTRELATION_MARKMODIFIED));
    }


    public void removeFromStockLevels(Product item, StockLevel value)
    {
        removeFromStockLevels(getSession().getSessionContext(), item, value);
    }


    public Set<Vendor> getVendors(SessionContext ctx, Product item)
    {
        List<Vendor> items = item.getLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTVENDORRELATION, "Vendor", null,
                        Utilities.getRelationOrderingOverride(PRODUCTVENDORRELATION_SRC_ORDERED, true), false);
        return new LinkedHashSet<>(items);
    }


    public Set<Vendor> getVendors(Product item)
    {
        return getVendors(getSession().getSessionContext(), item);
    }


    public long getVendorsCount(SessionContext ctx, Product item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTVENDORRELATION, "Vendor", null);
    }


    public long getVendorsCount(Product item)
    {
        return getVendorsCount(getSession().getSessionContext(), item);
    }


    public void setVendors(SessionContext ctx, Product item, Set<Vendor> value)
    {
        item.setLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTVENDORRELATION, null, value,
                        Utilities.getRelationOrderingOverride(PRODUCTVENDORRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTVENDORRELATION_MARKMODIFIED));
    }


    public void setVendors(Product item, Set<Vendor> value)
    {
        setVendors(getSession().getSessionContext(), item, value);
    }


    public void addToVendors(SessionContext ctx, Product item, Vendor value)
    {
        item.addLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTVENDORRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTVENDORRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTVENDORRELATION_MARKMODIFIED));
    }


    public void addToVendors(Product item, Vendor value)
    {
        addToVendors(getSession().getSessionContext(), item, value);
    }


    public void removeFromVendors(SessionContext ctx, Product item, Vendor value)
    {
        item.removeLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.PRODUCTVENDORRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTVENDORRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTVENDORRELATION_MARKMODIFIED));
    }


    public void removeFromVendors(Product item, Vendor value)
    {
        removeFromVendors(getSession().getSessionContext(), item, value);
    }


    public String getVersionID(SessionContext ctx, Order item)
    {
        return (String)item.getProperty(ctx, GeneratedBasecommerceConstants.Attributes.Order.VERSIONID);
    }


    public String getVersionID(Order item)
    {
        return getVersionID(getSession().getSessionContext(), item);
    }


    protected void setVersionID(SessionContext ctx, Order item, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + GeneratedBasecommerceConstants.Attributes.Order.VERSIONID + "' is not changeable", 0);
        }
        item.setProperty(ctx, GeneratedBasecommerceConstants.Attributes.Order.VERSIONID, value);
    }


    protected void setVersionID(Order item, String value)
    {
        setVersionID(getSession().getSessionContext(), item, value);
    }


    public abstract Collection<BarcodeMedia> getBarcodes(SessionContext paramSessionContext, Product paramProduct);
}
