package de.hybris.platform.ordersplitting.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.sap.sapmodel.model.SAPOrderModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class ConsignmentModel extends ItemModel
{
    public static final String _TYPECODE = "Consignment";
    public static final String _CONSIGNMENTENTRYCONSIGNMENTRELATION = "ConsignmentEntryConsignmentRelation";
    public static final String CODE = "code";
    public static final String SHIPPINGADDRESS = "shippingAddress";
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String NAMEDDELIVERYDATE = "namedDeliveryDate";
    public static final String SHIPPINGDATE = "shippingDate";
    public static final String TRACKINGID = "trackingID";
    public static final String CARRIER = "carrier";
    public static final String STATUS = "status";
    public static final String WAREHOUSE = "warehouse";
    public static final String CONSIGNMENTENTRIES = "consignmentEntries";
    public static final String ORDER = "order";
    public static final String CONSIGNMENTPROCESSES = "consignmentProcesses";
    public static final String DELIVERYPOINTOFSERVICE = "deliveryPointOfService";
    public static final String STATUSDISPLAY = "statusDisplay";
    public static final String DELIVERYHOLD = "deliveryHold";
    public static final String SAPORDER = "sapOrder";


    public ConsignmentModel()
    {
    }


    public ConsignmentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsignmentModel(String _code, AddressModel _shippingAddress, ConsignmentStatus _status, WarehouseModel _warehouse)
    {
        setCode(_code);
        setShippingAddress(_shippingAddress);
        setStatus(_status);
        setWarehouse(_warehouse);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsignmentModel(String _code, DeliveryModeModel _deliveryMode, Date _namedDeliveryDate, AbstractOrderModel _order, ItemModel _owner, SAPOrderModel _sapOrder, AddressModel _shippingAddress, ConsignmentStatus _status, WarehouseModel _warehouse)
    {
        setCode(_code);
        setDeliveryMode(_deliveryMode);
        setNamedDeliveryDate(_namedDeliveryDate);
        setOrder(_order);
        setOwner(_owner);
        setSapOrder(_sapOrder);
        setShippingAddress(_shippingAddress);
        setStatus(_status);
        setWarehouse(_warehouse);
    }


    @Accessor(qualifier = "carrier", type = Accessor.Type.GETTER)
    public String getCarrier()
    {
        return (String)getPersistenceContext().getPropertyValue("carrier");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "consignmentEntries", type = Accessor.Type.GETTER)
    public Set<ConsignmentEntryModel> getConsignmentEntries()
    {
        return (Set<ConsignmentEntryModel>)getPersistenceContext().getPropertyValue("consignmentEntries");
    }


    @Accessor(qualifier = "consignmentProcesses", type = Accessor.Type.GETTER)
    public Collection<ConsignmentProcessModel> getConsignmentProcesses()
    {
        return (Collection<ConsignmentProcessModel>)getPersistenceContext().getPropertyValue("consignmentProcesses");
    }


    @Accessor(qualifier = "deliveryHold", type = Accessor.Type.GETTER)
    public String getDeliveryHold()
    {
        return (String)getPersistenceContext().getPropertyValue("deliveryHold");
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


    @Accessor(qualifier = "sapOrder", type = Accessor.Type.GETTER)
    public SAPOrderModel getSapOrder()
    {
        return (SAPOrderModel)getPersistenceContext().getPropertyValue("sapOrder");
    }


    @Accessor(qualifier = "shippingAddress", type = Accessor.Type.GETTER)
    public AddressModel getShippingAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("shippingAddress");
    }


    @Accessor(qualifier = "shippingDate", type = Accessor.Type.GETTER)
    public Date getShippingDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("shippingDate");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public ConsignmentStatus getStatus()
    {
        return (ConsignmentStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "statusDisplay", type = Accessor.Type.GETTER)
    public String getStatusDisplay()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "statusDisplay");
    }


    @Accessor(qualifier = "trackingID", type = Accessor.Type.GETTER)
    public String getTrackingID()
    {
        return (String)getPersistenceContext().getPropertyValue("trackingID");
    }


    @Accessor(qualifier = "warehouse", type = Accessor.Type.GETTER)
    public WarehouseModel getWarehouse()
    {
        return (WarehouseModel)getPersistenceContext().getPropertyValue("warehouse");
    }


    @Accessor(qualifier = "carrier", type = Accessor.Type.SETTER)
    public void setCarrier(String value)
    {
        getPersistenceContext().setPropertyValue("carrier", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "consignmentEntries", type = Accessor.Type.SETTER)
    public void setConsignmentEntries(Set<ConsignmentEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("consignmentEntries", value);
    }


    @Accessor(qualifier = "consignmentProcesses", type = Accessor.Type.SETTER)
    public void setConsignmentProcesses(Collection<ConsignmentProcessModel> value)
    {
        getPersistenceContext().setPropertyValue("consignmentProcesses", value);
    }


    @Accessor(qualifier = "deliveryHold", type = Accessor.Type.SETTER)
    public void setDeliveryHold(String value)
    {
        getPersistenceContext().setPropertyValue("deliveryHold", value);
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


    @Accessor(qualifier = "sapOrder", type = Accessor.Type.SETTER)
    public void setSapOrder(SAPOrderModel value)
    {
        getPersistenceContext().setPropertyValue("sapOrder", value);
    }


    @Accessor(qualifier = "shippingAddress", type = Accessor.Type.SETTER)
    public void setShippingAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("shippingAddress", value);
    }


    @Accessor(qualifier = "shippingDate", type = Accessor.Type.SETTER)
    public void setShippingDate(Date value)
    {
        getPersistenceContext().setPropertyValue("shippingDate", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(ConsignmentStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "trackingID", type = Accessor.Type.SETTER)
    public void setTrackingID(String value)
    {
        getPersistenceContext().setPropertyValue("trackingID", value);
    }


    @Accessor(qualifier = "warehouse", type = Accessor.Type.SETTER)
    public void setWarehouse(WarehouseModel value)
    {
        getPersistenceContext().setPropertyValue("warehouse", value);
    }
}
