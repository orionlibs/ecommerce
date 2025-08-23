package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.jalo.ConsignmentEntry;
import de.hybris.platform.ordersplitting.jalo.StockLevel;
import de.hybris.platform.ordersplitting.jalo.Warehouse;
import de.hybris.platform.payment.constants.GeneratedPaymentConstants;
import de.hybris.platform.payment.jalo.PaymentTransactionEntry;
import de.hybris.platform.returns.jalo.OrderReturnRecordEntry;
import de.hybris.platform.returns.jalo.ReturnRequest;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.storelocator.jalo.PointOfService;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedWarehousingManager extends Extension
{
    protected static String WAREHOUSE2DELIVERYMODERELATION_SRC_ORDERED = "relation.Warehouse2DeliveryModeRelation.source.ordered";
    protected static String WAREHOUSE2DELIVERYMODERELATION_TGT_ORDERED = "relation.Warehouse2DeliveryModeRelation.target.ordered";
    protected static String WAREHOUSE2DELIVERYMODERELATION_MARKMODIFIED = "relation.Warehouse2DeliveryModeRelation.markmodified";
    protected static final OneToManyHandler<SourcingBan> WAREHOUSE2SOURCINGBANRELATIONSOURCINGBANHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.SOURCINGBAN, false, "warehouse", null, false, true, 0);
    protected static final OneToManyHandler<AdvancedShippingNotice> ADVANCEDSHIPPINGNOTICE2WAREHOUSERELATIONADVANCEDSHIPPINGNOTICESHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.ADVANCEDSHIPPINGNOTICE, false, "warehouse", null, false, true, 1);
    protected static final OneToManyHandler<WarehouseBin> WAREHOUSE2WAREHOUSEBINRELATIONWAREHOUSEBINSHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSEBIN, false, "warehouse", null, false, true, 2);
    protected static final OneToManyHandler<InventoryEvent> STOCKLEVEL2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.INVENTORYEVENT, false, "stockLevel", null, false, true, 0);
    protected static final OneToManyHandler<PackagingInfo> CONSIGNMENT2PACKAGINGINFORELATIONPACKAGINGINFOSHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.PACKAGINGINFO, false, "consignment", "consignmentPOS", true, true, 2);
    protected static final OneToManyHandler<PaymentTransactionEntry> CONSIGNMENT2PAYMENTTRANSACTIONENTRYRELATIONPAYMENTTRANSACTIONENTRIESHANDLER = new OneToManyHandler(GeneratedPaymentConstants.TC.PAYMENTTRANSACTIONENTRY, true, "consignment", null, false, true, 1);
    protected static final OneToManyHandler<DeclineConsignmentEntryEvent> DECLINECONSIGNMENTENTRYEVENTCONSIGNMENTENTRYRELATIONDECLINEENTRYEVENTSHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.DECLINECONSIGNMENTENTRYEVENT, false, "consignmentEntry", null, false, true, 1);
    protected static final OneToManyHandler<InventoryEvent> CONSIGNMENTENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.INVENTORYEVENT, false, "consignmentEntry", null, false, true, 0);
    protected static final OneToManyHandler<InventoryEvent> ORDERENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.INVENTORYEVENT, false, "orderEntry", null, false, true, 0);
    protected static final OneToManyHandler<AdvancedShippingNotice> ADVANCEDSHIPPINGNOTICE2POSRELATIONADVANCEDSHIPPINGNOTICESHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.ADVANCEDSHIPPINGNOTICE, false, "pointOfService", null, false, true, 1);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("score", Item.AttributeMode.INITIAL);
        tmp.put("isAllowRestock", Item.AttributeMode.INITIAL);
        tmp.put("external", Item.AttributeMode.INITIAL);
        tmp.put("warehouseBinTransferWorkflowName", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.ordersplitting.jalo.Warehouse", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("latitude", Item.AttributeMode.INITIAL);
        tmp.put("longitude", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.user.Address", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("bin", Item.AttributeMode.INITIAL);
        tmp.put("asnEntry", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.ordersplitting.jalo.StockLevel", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("packagingInfo", Item.AttributeMode.INITIAL);
        tmp.put("shippingLabel", Item.AttributeMode.INITIAL);
        tmp.put("returnLabel", Item.AttributeMode.INITIAL);
        tmp.put("returnForm", Item.AttributeMode.INITIAL);
        tmp.put("taskAssignmentWorkflow", Item.AttributeMode.INITIAL);
        tmp.put("fulfillmentSystemConfig", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.ordersplitting.jalo.Consignment", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("sourceOrderEntry", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.ordersplitting.jalo.ConsignmentEntry", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("returnRequest", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.returns.jalo.OrderReturnRecordEntry", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("defaultAtpFormula", Item.AttributeMode.INITIAL);
        tmp.put("sourcingConfig", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.store.BaseStore", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("consignment", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.payment.jalo.PaymentTransactionEntry", Collections.unmodifiableMap(tmp));
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


    public Set<AdvancedShippingNotice> getAdvancedShippingNotices(SessionContext ctx, Warehouse item)
    {
        return (Set<AdvancedShippingNotice>)ADVANCEDSHIPPINGNOTICE2WAREHOUSERELATIONADVANCEDSHIPPINGNOTICESHANDLER.getValues(ctx, (Item)item);
    }


    public Set<AdvancedShippingNotice> getAdvancedShippingNotices(Warehouse item)
    {
        return getAdvancedShippingNotices(getSession().getSessionContext(), item);
    }


    public void setAdvancedShippingNotices(SessionContext ctx, Warehouse item, Set<AdvancedShippingNotice> value)
    {
        ADVANCEDSHIPPINGNOTICE2WAREHOUSERELATIONADVANCEDSHIPPINGNOTICESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setAdvancedShippingNotices(Warehouse item, Set<AdvancedShippingNotice> value)
    {
        setAdvancedShippingNotices(getSession().getSessionContext(), item, value);
    }


    public void addToAdvancedShippingNotices(SessionContext ctx, Warehouse item, AdvancedShippingNotice value)
    {
        ADVANCEDSHIPPINGNOTICE2WAREHOUSERELATIONADVANCEDSHIPPINGNOTICESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToAdvancedShippingNotices(Warehouse item, AdvancedShippingNotice value)
    {
        addToAdvancedShippingNotices(getSession().getSessionContext(), item, value);
    }


    public void removeFromAdvancedShippingNotices(SessionContext ctx, Warehouse item, AdvancedShippingNotice value)
    {
        ADVANCEDSHIPPINGNOTICE2WAREHOUSERELATIONADVANCEDSHIPPINGNOTICESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromAdvancedShippingNotices(Warehouse item, AdvancedShippingNotice value)
    {
        removeFromAdvancedShippingNotices(getSession().getSessionContext(), item, value);
    }


    public Set<AdvancedShippingNotice> getAdvancedShippingNotices(SessionContext ctx, PointOfService item)
    {
        return (Set<AdvancedShippingNotice>)ADVANCEDSHIPPINGNOTICE2POSRELATIONADVANCEDSHIPPINGNOTICESHANDLER.getValues(ctx, (Item)item);
    }


    public Set<AdvancedShippingNotice> getAdvancedShippingNotices(PointOfService item)
    {
        return getAdvancedShippingNotices(getSession().getSessionContext(), item);
    }


    public void setAdvancedShippingNotices(SessionContext ctx, PointOfService item, Set<AdvancedShippingNotice> value)
    {
        ADVANCEDSHIPPINGNOTICE2POSRELATIONADVANCEDSHIPPINGNOTICESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setAdvancedShippingNotices(PointOfService item, Set<AdvancedShippingNotice> value)
    {
        setAdvancedShippingNotices(getSession().getSessionContext(), item, value);
    }


    public void addToAdvancedShippingNotices(SessionContext ctx, PointOfService item, AdvancedShippingNotice value)
    {
        ADVANCEDSHIPPINGNOTICE2POSRELATIONADVANCEDSHIPPINGNOTICESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToAdvancedShippingNotices(PointOfService item, AdvancedShippingNotice value)
    {
        addToAdvancedShippingNotices(getSession().getSessionContext(), item, value);
    }


    public void removeFromAdvancedShippingNotices(SessionContext ctx, PointOfService item, AdvancedShippingNotice value)
    {
        ADVANCEDSHIPPINGNOTICE2POSRELATIONADVANCEDSHIPPINGNOTICESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromAdvancedShippingNotices(PointOfService item, AdvancedShippingNotice value)
    {
        removeFromAdvancedShippingNotices(getSession().getSessionContext(), item, value);
    }


    public AdvancedShippingNoticeEntry getAsnEntry(SessionContext ctx, StockLevel item)
    {
        return (AdvancedShippingNoticeEntry)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.StockLevel.ASNENTRY);
    }


    public AdvancedShippingNoticeEntry getAsnEntry(StockLevel item)
    {
        return getAsnEntry(getSession().getSessionContext(), item);
    }


    public void setAsnEntry(SessionContext ctx, StockLevel item, AdvancedShippingNoticeEntry value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.StockLevel.ASNENTRY, value);
    }


    public void setAsnEntry(StockLevel item, AdvancedShippingNoticeEntry value)
    {
        setAsnEntry(getSession().getSessionContext(), item, value);
    }


    public String getBin(SessionContext ctx, StockLevel item)
    {
        return (String)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.StockLevel.BIN);
    }


    public String getBin(StockLevel item)
    {
        return getBin(getSession().getSessionContext(), item);
    }


    public void setBin(SessionContext ctx, StockLevel item, String value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.StockLevel.BIN, value);
    }


    public void setBin(StockLevel item, String value)
    {
        setBin(getSession().getSessionContext(), item, value);
    }


    public Consignment getConsignment(SessionContext ctx, PaymentTransactionEntry item)
    {
        return (Consignment)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.PaymentTransactionEntry.CONSIGNMENT);
    }


    public Consignment getConsignment(PaymentTransactionEntry item)
    {
        return getConsignment(getSession().getSessionContext(), item);
    }


    public void setConsignment(SessionContext ctx, PaymentTransactionEntry item, Consignment value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.PaymentTransactionEntry.CONSIGNMENT, value);
    }


    public void setConsignment(PaymentTransactionEntry item, Consignment value)
    {
        setConsignment(getSession().getSessionContext(), item, value);
    }


    public AdvancedShippingNotice createAdvancedShippingNotice(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.ADVANCEDSHIPPINGNOTICE);
            return (AdvancedShippingNotice)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating AdvancedShippingNotice : " + e.getMessage(), 0);
        }
    }


    public AdvancedShippingNotice createAdvancedShippingNotice(Map attributeValues)
    {
        return createAdvancedShippingNotice(getSession().getSessionContext(), attributeValues);
    }


    public AdvancedShippingNoticeEntry createAdvancedShippingNoticeEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.ADVANCEDSHIPPINGNOTICEENTRY);
            return (AdvancedShippingNoticeEntry)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating AdvancedShippingNoticeEntry : " + e.getMessage(), 0);
        }
    }


    public AdvancedShippingNoticeEntry createAdvancedShippingNoticeEntry(Map attributeValues)
    {
        return createAdvancedShippingNoticeEntry(getSession().getSessionContext(), attributeValues);
    }


    public AdvancedShippingNoticeValidConstraint createAdvancedShippingNoticeValidConstraint(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.ADVANCEDSHIPPINGNOTICEVALIDCONSTRAINT);
            return (AdvancedShippingNoticeValidConstraint)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating AdvancedShippingNoticeValidConstraint : " + e.getMessage(), 0);
        }
    }


    public AdvancedShippingNoticeValidConstraint createAdvancedShippingNoticeValidConstraint(Map attributeValues)
    {
        return createAdvancedShippingNoticeValidConstraint(getSession().getSessionContext(), attributeValues);
    }


    public AllocationEvent createAllocationEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.ALLOCATIONEVENT);
            return (AllocationEvent)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating AllocationEvent : " + e.getMessage(), 0);
        }
    }


    public AllocationEvent createAllocationEvent(Map attributeValues)
    {
        return createAllocationEvent(getSession().getSessionContext(), attributeValues);
    }


    public AtpFormula createAtpFormula(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.ATPFORMULA);
            return (AtpFormula)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating AtpFormula : " + e.getMessage(), 0);
        }
    }


    public AtpFormula createAtpFormula(Map attributeValues)
    {
        return createAtpFormula(getSession().getSessionContext(), attributeValues);
    }


    public CancellationEvent createCancellationEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.CANCELLATIONEVENT);
            return (CancellationEvent)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CancellationEvent : " + e.getMessage(), 0);
        }
    }


    public CancellationEvent createCancellationEvent(Map attributeValues)
    {
        return createCancellationEvent(getSession().getSessionContext(), attributeValues);
    }


    public DeclineConsignmentEntryEvent createDeclineConsignmentEntryEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.DECLINECONSIGNMENTENTRYEVENT);
            return (DeclineConsignmentEntryEvent)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating DeclineConsignmentEntryEvent : " + e.getMessage(), 0);
        }
    }


    public DeclineConsignmentEntryEvent createDeclineConsignmentEntryEvent(Map attributeValues)
    {
        return createDeclineConsignmentEntryEvent(getSession().getSessionContext(), attributeValues);
    }


    public IncreaseEvent createIncreaseEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.INCREASEEVENT);
            return (IncreaseEvent)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating IncreaseEvent : " + e.getMessage(), 0);
        }
    }


    public IncreaseEvent createIncreaseEvent(Map attributeValues)
    {
        return createIncreaseEvent(getSession().getSessionContext(), attributeValues);
    }


    public PackagingInfo createPackagingInfo(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.PACKAGINGINFO);
            return (PackagingInfo)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PackagingInfo : " + e.getMessage(), 0);
        }
    }


    public PackagingInfo createPackagingInfo(Map attributeValues)
    {
        return createPackagingInfo(getSession().getSessionContext(), attributeValues);
    }


    public ReservationEvent createReservationEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.RESERVATIONEVENT);
            return (ReservationEvent)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ReservationEvent : " + e.getMessage(), 0);
        }
    }


    public ReservationEvent createReservationEvent(Map attributeValues)
    {
        return createReservationEvent(getSession().getSessionContext(), attributeValues);
    }


    public RestockConfig createRestockConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.RESTOCKCONFIG);
            return (RestockConfig)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating RestockConfig : " + e.getMessage(), 0);
        }
    }


    public RestockConfig createRestockConfig(Map attributeValues)
    {
        return createRestockConfig(getSession().getSessionContext(), attributeValues);
    }


    public ShrinkageEvent createShrinkageEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.SHRINKAGEEVENT);
            return (ShrinkageEvent)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ShrinkageEvent : " + e.getMessage(), 0);
        }
    }


    public ShrinkageEvent createShrinkageEvent(Map attributeValues)
    {
        return createShrinkageEvent(getSession().getSessionContext(), attributeValues);
    }


    public SourcingBan createSourcingBan(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.SOURCINGBAN);
            return (SourcingBan)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating SourcingBan : " + e.getMessage(), 0);
        }
    }


    public SourcingBan createSourcingBan(Map attributeValues)
    {
        return createSourcingBan(getSession().getSessionContext(), attributeValues);
    }


    public SourcingConfig createSourcingConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.SOURCINGCONFIG);
            return (SourcingConfig)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating SourcingConfig : " + e.getMessage(), 0);
        }
    }


    public SourcingConfig createSourcingConfig(Map attributeValues)
    {
        return createSourcingConfig(getSession().getSessionContext(), attributeValues);
    }


    public WarehouseBin createWarehouseBin(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.WAREHOUSEBIN);
            return (WarehouseBin)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating WarehouseBin : " + e.getMessage(), 0);
        }
    }


    public WarehouseBin createWarehouseBin(Map attributeValues)
    {
        return createWarehouseBin(getSession().getSessionContext(), attributeValues);
    }


    public WarehouseBinEntry createWarehouseBinEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.WAREHOUSEBINENTRY);
            return (WarehouseBinEntry)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating WarehouseBinEntry : " + e.getMessage(), 0);
        }
    }


    public WarehouseBinEntry createWarehouseBinEntry(Map attributeValues)
    {
        return createWarehouseBinEntry(getSession().getSessionContext(), attributeValues);
    }


    public WarehouseTransfer createWarehouseTransfer(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFER);
            return (WarehouseTransfer)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating WarehouseTransfer : " + e.getMessage(), 0);
        }
    }


    public WarehouseTransfer createWarehouseTransfer(Map attributeValues)
    {
        return createWarehouseTransfer(getSession().getSessionContext(), attributeValues);
    }


    public WarehouseTransferEntry createWarehouseTransferEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFERENTRY);
            return (WarehouseTransferEntry)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating WarehouseTransferEntry : " + e.getMessage(), 0);
        }
    }


    public WarehouseTransferEntry createWarehouseTransferEntry(Map attributeValues)
    {
        return createWarehouseTransferEntry(getSession().getSessionContext(), attributeValues);
    }


    public WastageEvent createWastageEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWarehousingConstants.TC.WASTAGEEVENT);
            return (WastageEvent)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating WastageEvent : " + e.getMessage(), 0);
        }
    }


    public WastageEvent createWastageEvent(Map attributeValues)
    {
        return createWastageEvent(getSession().getSessionContext(), attributeValues);
    }


    public Set<DeclineConsignmentEntryEvent> getDeclineEntryEvents(SessionContext ctx, ConsignmentEntry item)
    {
        return (Set<DeclineConsignmentEntryEvent>)DECLINECONSIGNMENTENTRYEVENTCONSIGNMENTENTRYRELATIONDECLINEENTRYEVENTSHANDLER.getValues(ctx, (Item)item);
    }


    public Set<DeclineConsignmentEntryEvent> getDeclineEntryEvents(ConsignmentEntry item)
    {
        return getDeclineEntryEvents(getSession().getSessionContext(), item);
    }


    public void setDeclineEntryEvents(SessionContext ctx, ConsignmentEntry item, Set<DeclineConsignmentEntryEvent> value)
    {
        DECLINECONSIGNMENTENTRYEVENTCONSIGNMENTENTRYRELATIONDECLINEENTRYEVENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setDeclineEntryEvents(ConsignmentEntry item, Set<DeclineConsignmentEntryEvent> value)
    {
        setDeclineEntryEvents(getSession().getSessionContext(), item, value);
    }


    public void addToDeclineEntryEvents(SessionContext ctx, ConsignmentEntry item, DeclineConsignmentEntryEvent value)
    {
        DECLINECONSIGNMENTENTRYEVENTCONSIGNMENTENTRYRELATIONDECLINEENTRYEVENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToDeclineEntryEvents(ConsignmentEntry item, DeclineConsignmentEntryEvent value)
    {
        addToDeclineEntryEvents(getSession().getSessionContext(), item, value);
    }


    public void removeFromDeclineEntryEvents(SessionContext ctx, ConsignmentEntry item, DeclineConsignmentEntryEvent value)
    {
        DECLINECONSIGNMENTENTRYEVENTCONSIGNMENTENTRYRELATIONDECLINEENTRYEVENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromDeclineEntryEvents(ConsignmentEntry item, DeclineConsignmentEntryEvent value)
    {
        removeFromDeclineEntryEvents(getSession().getSessionContext(), item, value);
    }


    public AtpFormula getDefaultAtpFormula(SessionContext ctx, BaseStore item)
    {
        return (AtpFormula)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.BaseStore.DEFAULTATPFORMULA);
    }


    public AtpFormula getDefaultAtpFormula(BaseStore item)
    {
        return getDefaultAtpFormula(getSession().getSessionContext(), item);
    }


    public void setDefaultAtpFormula(SessionContext ctx, BaseStore item, AtpFormula value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.BaseStore.DEFAULTATPFORMULA, value);
    }


    public void setDefaultAtpFormula(BaseStore item, AtpFormula value)
    {
        setDefaultAtpFormula(getSession().getSessionContext(), item, value);
    }


    public Set<DeliveryMode> getDeliveryModes(SessionContext ctx, Warehouse item)
    {
        List<DeliveryMode> items = item.getLinkedItems(ctx, true, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, "DeliveryMode", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<DeliveryMode> getDeliveryModes(Warehouse item)
    {
        return getDeliveryModes(getSession().getSessionContext(), item);
    }


    public long getDeliveryModesCount(SessionContext ctx, Warehouse item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, "DeliveryMode", null);
    }


    public long getDeliveryModesCount(Warehouse item)
    {
        return getDeliveryModesCount(getSession().getSessionContext(), item);
    }


    public void setDeliveryModes(SessionContext ctx, Warehouse item, Set<DeliveryMode> value)
    {
        item.setLinkedItems(ctx, true, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WAREHOUSE2DELIVERYMODERELATION_MARKMODIFIED));
    }


    public void setDeliveryModes(Warehouse item, Set<DeliveryMode> value)
    {
        setDeliveryModes(getSession().getSessionContext(), item, value);
    }


    public void addToDeliveryModes(SessionContext ctx, Warehouse item, DeliveryMode value)
    {
        item.addLinkedItems(ctx, true, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WAREHOUSE2DELIVERYMODERELATION_MARKMODIFIED));
    }


    public void addToDeliveryModes(Warehouse item, DeliveryMode value)
    {
        addToDeliveryModes(getSession().getSessionContext(), item, value);
    }


    public void removeFromDeliveryModes(SessionContext ctx, Warehouse item, DeliveryMode value)
    {
        item.removeLinkedItems(ctx, true, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WAREHOUSE2DELIVERYMODERELATION_MARKMODIFIED));
    }


    public void removeFromDeliveryModes(Warehouse item, DeliveryMode value)
    {
        removeFromDeliveryModes(getSession().getSessionContext(), item, value);
    }


    public Boolean isExternal(SessionContext ctx, Warehouse item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.EXTERNAL);
    }


    public Boolean isExternal(Warehouse item)
    {
        return isExternal(getSession().getSessionContext(), item);
    }


    public boolean isExternalAsPrimitive(SessionContext ctx, Warehouse item)
    {
        Boolean value = isExternal(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExternalAsPrimitive(Warehouse item)
    {
        return isExternalAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setExternal(SessionContext ctx, Warehouse item, Boolean value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.EXTERNAL, value);
    }


    public void setExternal(Warehouse item, Boolean value)
    {
        setExternal(getSession().getSessionContext(), item, value);
    }


    public void setExternal(SessionContext ctx, Warehouse item, boolean value)
    {
        setExternal(ctx, item, Boolean.valueOf(value));
    }


    public void setExternal(Warehouse item, boolean value)
    {
        setExternal(getSession().getSessionContext(), item, value);
    }


    public Object getFulfillmentSystemConfig(SessionContext ctx, Consignment item)
    {
        return item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.FULFILLMENTSYSTEMCONFIG);
    }


    public Object getFulfillmentSystemConfig(Consignment item)
    {
        return getFulfillmentSystemConfig(getSession().getSessionContext(), item);
    }


    public void setFulfillmentSystemConfig(SessionContext ctx, Consignment item, Object value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.FULFILLMENTSYSTEMCONFIG, value);
    }


    public void setFulfillmentSystemConfig(Consignment item, Object value)
    {
        setFulfillmentSystemConfig(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "warehousing";
    }


    public Collection<InventoryEvent> getInventoryEvents(SessionContext ctx, StockLevel item)
    {
        return STOCKLEVEL2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<InventoryEvent> getInventoryEvents(StockLevel item)
    {
        return getInventoryEvents(getSession().getSessionContext(), item);
    }


    public void setInventoryEvents(SessionContext ctx, StockLevel item, Collection<InventoryEvent> value)
    {
        STOCKLEVEL2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setInventoryEvents(StockLevel item, Collection<InventoryEvent> value)
    {
        setInventoryEvents(getSession().getSessionContext(), item, value);
    }


    public void addToInventoryEvents(SessionContext ctx, StockLevel item, InventoryEvent value)
    {
        STOCKLEVEL2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToInventoryEvents(StockLevel item, InventoryEvent value)
    {
        addToInventoryEvents(getSession().getSessionContext(), item, value);
    }


    public void removeFromInventoryEvents(SessionContext ctx, StockLevel item, InventoryEvent value)
    {
        STOCKLEVEL2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromInventoryEvents(StockLevel item, InventoryEvent value)
    {
        removeFromInventoryEvents(getSession().getSessionContext(), item, value);
    }


    public Collection<InventoryEvent> getInventoryEvents(SessionContext ctx, ConsignmentEntry item)
    {
        return CONSIGNMENTENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<InventoryEvent> getInventoryEvents(ConsignmentEntry item)
    {
        return getInventoryEvents(getSession().getSessionContext(), item);
    }


    public void setInventoryEvents(SessionContext ctx, ConsignmentEntry item, Collection<InventoryEvent> value)
    {
        CONSIGNMENTENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setInventoryEvents(ConsignmentEntry item, Collection<InventoryEvent> value)
    {
        setInventoryEvents(getSession().getSessionContext(), item, value);
    }


    public void addToInventoryEvents(SessionContext ctx, ConsignmentEntry item, InventoryEvent value)
    {
        CONSIGNMENTENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToInventoryEvents(ConsignmentEntry item, InventoryEvent value)
    {
        addToInventoryEvents(getSession().getSessionContext(), item, value);
    }


    public void removeFromInventoryEvents(SessionContext ctx, ConsignmentEntry item, InventoryEvent value)
    {
        CONSIGNMENTENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromInventoryEvents(ConsignmentEntry item, InventoryEvent value)
    {
        removeFromInventoryEvents(getSession().getSessionContext(), item, value);
    }


    public Collection<InventoryEvent> getInventoryEvents(SessionContext ctx, OrderEntry item)
    {
        return ORDERENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<InventoryEvent> getInventoryEvents(OrderEntry item)
    {
        return getInventoryEvents(getSession().getSessionContext(), item);
    }


    public void setInventoryEvents(SessionContext ctx, OrderEntry item, Collection<InventoryEvent> value)
    {
        ORDERENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setInventoryEvents(OrderEntry item, Collection<InventoryEvent> value)
    {
        setInventoryEvents(getSession().getSessionContext(), item, value);
    }


    public void addToInventoryEvents(SessionContext ctx, OrderEntry item, InventoryEvent value)
    {
        ORDERENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToInventoryEvents(OrderEntry item, InventoryEvent value)
    {
        addToInventoryEvents(getSession().getSessionContext(), item, value);
    }


    public void removeFromInventoryEvents(SessionContext ctx, OrderEntry item, InventoryEvent value)
    {
        ORDERENTRY2INVENTORYEVENTRELATIONINVENTORYEVENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromInventoryEvents(OrderEntry item, InventoryEvent value)
    {
        removeFromInventoryEvents(getSession().getSessionContext(), item, value);
    }


    public Boolean isIsAllowRestock(SessionContext ctx, Warehouse item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.ISALLOWRESTOCK);
    }


    public Boolean isIsAllowRestock(Warehouse item)
    {
        return isIsAllowRestock(getSession().getSessionContext(), item);
    }


    public boolean isIsAllowRestockAsPrimitive(SessionContext ctx, Warehouse item)
    {
        Boolean value = isIsAllowRestock(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIsAllowRestockAsPrimitive(Warehouse item)
    {
        return isIsAllowRestockAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setIsAllowRestock(SessionContext ctx, Warehouse item, Boolean value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.ISALLOWRESTOCK, value);
    }


    public void setIsAllowRestock(Warehouse item, Boolean value)
    {
        setIsAllowRestock(getSession().getSessionContext(), item, value);
    }


    public void setIsAllowRestock(SessionContext ctx, Warehouse item, boolean value)
    {
        setIsAllowRestock(ctx, item, Boolean.valueOf(value));
    }


    public void setIsAllowRestock(Warehouse item, boolean value)
    {
        setIsAllowRestock(getSession().getSessionContext(), item, value);
    }


    public Double getLatitude(SessionContext ctx, Address item)
    {
        return (Double)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Address.LATITUDE);
    }


    public Double getLatitude(Address item)
    {
        return getLatitude(getSession().getSessionContext(), item);
    }


    public double getLatitudeAsPrimitive(SessionContext ctx, Address item)
    {
        Double value = getLatitude(ctx, item);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getLatitudeAsPrimitive(Address item)
    {
        return getLatitudeAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setLatitude(SessionContext ctx, Address item, Double value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Address.LATITUDE, value);
    }


    public void setLatitude(Address item, Double value)
    {
        setLatitude(getSession().getSessionContext(), item, value);
    }


    public void setLatitude(SessionContext ctx, Address item, double value)
    {
        setLatitude(ctx, item, Double.valueOf(value));
    }


    public void setLatitude(Address item, double value)
    {
        setLatitude(getSession().getSessionContext(), item, value);
    }


    public Double getLongitude(SessionContext ctx, Address item)
    {
        return (Double)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Address.LONGITUDE);
    }


    public Double getLongitude(Address item)
    {
        return getLongitude(getSession().getSessionContext(), item);
    }


    public double getLongitudeAsPrimitive(SessionContext ctx, Address item)
    {
        Double value = getLongitude(ctx, item);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getLongitudeAsPrimitive(Address item)
    {
        return getLongitudeAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setLongitude(SessionContext ctx, Address item, Double value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Address.LONGITUDE, value);
    }


    public void setLongitude(Address item, Double value)
    {
        setLongitude(getSession().getSessionContext(), item, value);
    }


    public void setLongitude(SessionContext ctx, Address item, double value)
    {
        setLongitude(ctx, item, Double.valueOf(value));
    }


    public void setLongitude(Address item, double value)
    {
        setLongitude(getSession().getSessionContext(), item, value);
    }


    public PackagingInfo getPackagingInfo(SessionContext ctx, Consignment item)
    {
        return (PackagingInfo)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.PACKAGINGINFO);
    }


    public PackagingInfo getPackagingInfo(Consignment item)
    {
        return getPackagingInfo(getSession().getSessionContext(), item);
    }


    public void setPackagingInfo(SessionContext ctx, Consignment item, PackagingInfo value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.PACKAGINGINFO, value);
    }


    public void setPackagingInfo(Consignment item, PackagingInfo value)
    {
        setPackagingInfo(getSession().getSessionContext(), item, value);
    }


    public List<PackagingInfo> getPackaginginfos(SessionContext ctx, Consignment item)
    {
        return (List<PackagingInfo>)CONSIGNMENT2PACKAGINGINFORELATIONPACKAGINGINFOSHANDLER.getValues(ctx, (Item)item);
    }


    public List<PackagingInfo> getPackaginginfos(Consignment item)
    {
        return getPackaginginfos(getSession().getSessionContext(), item);
    }


    public void setPackaginginfos(SessionContext ctx, Consignment item, List<PackagingInfo> value)
    {
        CONSIGNMENT2PACKAGINGINFORELATIONPACKAGINGINFOSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setPackaginginfos(Consignment item, List<PackagingInfo> value)
    {
        setPackaginginfos(getSession().getSessionContext(), item, value);
    }


    public void addToPackaginginfos(SessionContext ctx, Consignment item, PackagingInfo value)
    {
        CONSIGNMENT2PACKAGINGINFORELATIONPACKAGINGINFOSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToPackaginginfos(Consignment item, PackagingInfo value)
    {
        addToPackaginginfos(getSession().getSessionContext(), item, value);
    }


    public void removeFromPackaginginfos(SessionContext ctx, Consignment item, PackagingInfo value)
    {
        CONSIGNMENT2PACKAGINGINFORELATIONPACKAGINGINFOSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromPackaginginfos(Consignment item, PackagingInfo value)
    {
        removeFromPackaginginfos(getSession().getSessionContext(), item, value);
    }


    public Set<PaymentTransactionEntry> getPaymentTransactionEntries(SessionContext ctx, Consignment item)
    {
        return (Set<PaymentTransactionEntry>)CONSIGNMENT2PAYMENTTRANSACTIONENTRYRELATIONPAYMENTTRANSACTIONENTRIESHANDLER.getValues(ctx, (Item)item);
    }


    public Set<PaymentTransactionEntry> getPaymentTransactionEntries(Consignment item)
    {
        return getPaymentTransactionEntries(getSession().getSessionContext(), item);
    }


    public void setPaymentTransactionEntries(SessionContext ctx, Consignment item, Set<PaymentTransactionEntry> value)
    {
        CONSIGNMENT2PAYMENTTRANSACTIONENTRYRELATIONPAYMENTTRANSACTIONENTRIESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setPaymentTransactionEntries(Consignment item, Set<PaymentTransactionEntry> value)
    {
        setPaymentTransactionEntries(getSession().getSessionContext(), item, value);
    }


    public void addToPaymentTransactionEntries(SessionContext ctx, Consignment item, PaymentTransactionEntry value)
    {
        CONSIGNMENT2PAYMENTTRANSACTIONENTRYRELATIONPAYMENTTRANSACTIONENTRIESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToPaymentTransactionEntries(Consignment item, PaymentTransactionEntry value)
    {
        addToPaymentTransactionEntries(getSession().getSessionContext(), item, value);
    }


    public void removeFromPaymentTransactionEntries(SessionContext ctx, Consignment item, PaymentTransactionEntry value)
    {
        CONSIGNMENT2PAYMENTTRANSACTIONENTRYRELATIONPAYMENTTRANSACTIONENTRIESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromPaymentTransactionEntries(Consignment item, PaymentTransactionEntry value)
    {
        removeFromPaymentTransactionEntries(getSession().getSessionContext(), item, value);
    }


    public Integer getPriority(SessionContext ctx, Warehouse item)
    {
        return (Integer)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.PRIORITY);
    }


    public Integer getPriority(Warehouse item)
    {
        return getPriority(getSession().getSessionContext(), item);
    }


    public int getPriorityAsPrimitive(SessionContext ctx, Warehouse item)
    {
        Integer value = getPriority(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive(Warehouse item)
    {
        return getPriorityAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setPriority(SessionContext ctx, Warehouse item, Integer value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.PRIORITY, value);
    }


    public void setPriority(Warehouse item, Integer value)
    {
        setPriority(getSession().getSessionContext(), item, value);
    }


    public void setPriority(SessionContext ctx, Warehouse item, int value)
    {
        setPriority(ctx, item, Integer.valueOf(value));
    }


    public void setPriority(Warehouse item, int value)
    {
        setPriority(getSession().getSessionContext(), item, value);
    }


    public Media getReturnForm(SessionContext ctx, Consignment item)
    {
        return (Media)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.RETURNFORM);
    }


    public Media getReturnForm(Consignment item)
    {
        return getReturnForm(getSession().getSessionContext(), item);
    }


    public void setReturnForm(SessionContext ctx, Consignment item, Media value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.RETURNFORM, value);
    }


    public void setReturnForm(Consignment item, Media value)
    {
        setReturnForm(getSession().getSessionContext(), item, value);
    }


    public Media getReturnLabel(SessionContext ctx, Consignment item)
    {
        return (Media)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.RETURNLABEL);
    }


    public Media getReturnLabel(Consignment item)
    {
        return getReturnLabel(getSession().getSessionContext(), item);
    }


    public void setReturnLabel(SessionContext ctx, Consignment item, Media value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.RETURNLABEL, value);
    }


    public void setReturnLabel(Consignment item, Media value)
    {
        setReturnLabel(getSession().getSessionContext(), item, value);
    }


    public ReturnRequest getReturnRequest(SessionContext ctx, OrderReturnRecordEntry item)
    {
        return (ReturnRequest)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.OrderReturnRecordEntry.RETURNREQUEST);
    }


    public ReturnRequest getReturnRequest(OrderReturnRecordEntry item)
    {
        return getReturnRequest(getSession().getSessionContext(), item);
    }


    public void setReturnRequest(SessionContext ctx, OrderReturnRecordEntry item, ReturnRequest value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.OrderReturnRecordEntry.RETURNREQUEST, value);
    }


    public void setReturnRequest(OrderReturnRecordEntry item, ReturnRequest value)
    {
        setReturnRequest(getSession().getSessionContext(), item, value);
    }


    public Double getScore(SessionContext ctx, Warehouse item)
    {
        return (Double)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.SCORE);
    }


    public Double getScore(Warehouse item)
    {
        return getScore(getSession().getSessionContext(), item);
    }


    public double getScoreAsPrimitive(SessionContext ctx, Warehouse item)
    {
        Double value = getScore(ctx, item);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getScoreAsPrimitive(Warehouse item)
    {
        return getScoreAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setScore(SessionContext ctx, Warehouse item, Double value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.SCORE, value);
    }


    public void setScore(Warehouse item, Double value)
    {
        setScore(getSession().getSessionContext(), item, value);
    }


    public void setScore(SessionContext ctx, Warehouse item, double value)
    {
        setScore(ctx, item, Double.valueOf(value));
    }


    public void setScore(Warehouse item, double value)
    {
        setScore(getSession().getSessionContext(), item, value);
    }


    public Media getShippingLabel(SessionContext ctx, Consignment item)
    {
        return (Media)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.SHIPPINGLABEL);
    }


    public Media getShippingLabel(Consignment item)
    {
        return getShippingLabel(getSession().getSessionContext(), item);
    }


    public void setShippingLabel(SessionContext ctx, Consignment item, Media value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.SHIPPINGLABEL, value);
    }


    public void setShippingLabel(Consignment item, Media value)
    {
        setShippingLabel(getSession().getSessionContext(), item, value);
    }


    public OrderEntry getSourceOrderEntry(SessionContext ctx, ConsignmentEntry item)
    {
        return (OrderEntry)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.ConsignmentEntry.SOURCEORDERENTRY);
    }


    public OrderEntry getSourceOrderEntry(ConsignmentEntry item)
    {
        return getSourceOrderEntry(getSession().getSessionContext(), item);
    }


    public void setSourceOrderEntry(SessionContext ctx, ConsignmentEntry item, OrderEntry value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.ConsignmentEntry.SOURCEORDERENTRY, value);
    }


    public void setSourceOrderEntry(ConsignmentEntry item, OrderEntry value)
    {
        setSourceOrderEntry(getSession().getSessionContext(), item, value);
    }


    public Collection<SourcingBan> getSourcingban(SessionContext ctx, Warehouse item)
    {
        return WAREHOUSE2SOURCINGBANRELATIONSOURCINGBANHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<SourcingBan> getSourcingban(Warehouse item)
    {
        return getSourcingban(getSession().getSessionContext(), item);
    }


    public void setSourcingban(SessionContext ctx, Warehouse item, Collection<SourcingBan> value)
    {
        WAREHOUSE2SOURCINGBANRELATIONSOURCINGBANHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setSourcingban(Warehouse item, Collection<SourcingBan> value)
    {
        setSourcingban(getSession().getSessionContext(), item, value);
    }


    public void addToSourcingban(SessionContext ctx, Warehouse item, SourcingBan value)
    {
        WAREHOUSE2SOURCINGBANRELATIONSOURCINGBANHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToSourcingban(Warehouse item, SourcingBan value)
    {
        addToSourcingban(getSession().getSessionContext(), item, value);
    }


    public void removeFromSourcingban(SessionContext ctx, Warehouse item, SourcingBan value)
    {
        WAREHOUSE2SOURCINGBANRELATIONSOURCINGBANHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromSourcingban(Warehouse item, SourcingBan value)
    {
        removeFromSourcingban(getSession().getSessionContext(), item, value);
    }


    public SourcingConfig getSourcingConfig(SessionContext ctx, BaseStore item)
    {
        return (SourcingConfig)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.BaseStore.SOURCINGCONFIG);
    }


    public SourcingConfig getSourcingConfig(BaseStore item)
    {
        return getSourcingConfig(getSession().getSessionContext(), item);
    }


    public void setSourcingConfig(SessionContext ctx, BaseStore item, SourcingConfig value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.BaseStore.SOURCINGCONFIG, value);
    }


    public void setSourcingConfig(BaseStore item, SourcingConfig value)
    {
        setSourcingConfig(getSession().getSessionContext(), item, value);
    }


    public String getTaskAssignmentWorkflow(SessionContext ctx, Consignment item)
    {
        return (String)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.TASKASSIGNMENTWORKFLOW);
    }


    public String getTaskAssignmentWorkflow(Consignment item)
    {
        return getTaskAssignmentWorkflow(getSession().getSessionContext(), item);
    }


    public void setTaskAssignmentWorkflow(SessionContext ctx, Consignment item, String value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Consignment.TASKASSIGNMENTWORKFLOW, value);
    }


    public void setTaskAssignmentWorkflow(Consignment item, String value)
    {
        setTaskAssignmentWorkflow(getSession().getSessionContext(), item, value);
    }


    public List<WarehouseBin> getWarehouseBins(SessionContext ctx, Warehouse item)
    {
        return (List<WarehouseBin>)WAREHOUSE2WAREHOUSEBINRELATIONWAREHOUSEBINSHANDLER.getValues(ctx, (Item)item);
    }


    public List<WarehouseBin> getWarehouseBins(Warehouse item)
    {
        return getWarehouseBins(getSession().getSessionContext(), item);
    }


    public void setWarehouseBins(SessionContext ctx, Warehouse item, List<WarehouseBin> value)
    {
        WAREHOUSE2WAREHOUSEBINRELATIONWAREHOUSEBINSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setWarehouseBins(Warehouse item, List<WarehouseBin> value)
    {
        setWarehouseBins(getSession().getSessionContext(), item, value);
    }


    public void addToWarehouseBins(SessionContext ctx, Warehouse item, WarehouseBin value)
    {
        WAREHOUSE2WAREHOUSEBINRELATIONWAREHOUSEBINSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToWarehouseBins(Warehouse item, WarehouseBin value)
    {
        addToWarehouseBins(getSession().getSessionContext(), item, value);
    }


    public void removeFromWarehouseBins(SessionContext ctx, Warehouse item, WarehouseBin value)
    {
        WAREHOUSE2WAREHOUSEBINRELATIONWAREHOUSEBINSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromWarehouseBins(Warehouse item, WarehouseBin value)
    {
        removeFromWarehouseBins(getSession().getSessionContext(), item, value);
    }


    public String getWarehouseBinTransferWorkflowName(SessionContext ctx, Warehouse item)
    {
        return (String)item.getProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.WAREHOUSEBINTRANSFERWORKFLOWNAME);
    }


    public String getWarehouseBinTransferWorkflowName(Warehouse item)
    {
        return getWarehouseBinTransferWorkflowName(getSession().getSessionContext(), item);
    }


    public void setWarehouseBinTransferWorkflowName(SessionContext ctx, Warehouse item, String value)
    {
        item.setProperty(ctx, GeneratedWarehousingConstants.Attributes.Warehouse.WAREHOUSEBINTRANSFERWORKFLOWNAME, value);
    }


    public void setWarehouseBinTransferWorkflowName(Warehouse item, String value)
    {
        setWarehouseBinTransferWorkflowName(getSession().getSessionContext(), item, value);
    }


    public Set<Warehouse> getWarehouses(SessionContext ctx, DeliveryMode item)
    {
        List<Warehouse> items = item.getLinkedItems(ctx, false, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, "Warehouse", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<Warehouse> getWarehouses(DeliveryMode item)
    {
        return getWarehouses(getSession().getSessionContext(), item);
    }


    public long getWarehousesCount(SessionContext ctx, DeliveryMode item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, "Warehouse", null);
    }


    public long getWarehousesCount(DeliveryMode item)
    {
        return getWarehousesCount(getSession().getSessionContext(), item);
    }


    public void setWarehouses(SessionContext ctx, DeliveryMode item, Set<Warehouse> value)
    {
        item.setLinkedItems(ctx, false, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WAREHOUSE2DELIVERYMODERELATION_MARKMODIFIED));
    }


    public void setWarehouses(DeliveryMode item, Set<Warehouse> value)
    {
        setWarehouses(getSession().getSessionContext(), item, value);
    }


    public void addToWarehouses(SessionContext ctx, DeliveryMode item, Warehouse value)
    {
        item.addLinkedItems(ctx, false, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WAREHOUSE2DELIVERYMODERELATION_MARKMODIFIED));
    }


    public void addToWarehouses(DeliveryMode item, Warehouse value)
    {
        addToWarehouses(getSession().getSessionContext(), item, value);
    }


    public void removeFromWarehouses(SessionContext ctx, DeliveryMode item, Warehouse value)
    {
        item.removeLinkedItems(ctx, false, GeneratedWarehousingConstants.Relations.WAREHOUSE2DELIVERYMODERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WAREHOUSE2DELIVERYMODERELATION_MARKMODIFIED));
    }


    public void removeFromWarehouses(DeliveryMode item, Warehouse value)
    {
        removeFromWarehouses(getSession().getSessionContext(), item, value);
    }
}
