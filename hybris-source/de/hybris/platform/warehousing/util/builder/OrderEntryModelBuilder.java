package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.TaxValue;
import java.util.Collection;

public class OrderEntryModelBuilder
{
    private final OrderEntryModel model;


    private OrderEntryModelBuilder()
    {
        this.model = new OrderEntryModel();
    }


    private OrderEntryModelBuilder(OrderEntryModel source)
    {
        this.model = source;
    }


    private OrderEntryModel getModel()
    {
        return this.model;
    }


    public static OrderEntryModelBuilder aModel()
    {
        return new OrderEntryModelBuilder();
    }


    public static OrderEntryModelBuilder fromModel(OrderEntryModel source)
    {
        return new OrderEntryModelBuilder(source);
    }


    public OrderEntryModel build()
    {
        return getModel();
    }


    public OrderEntryModelBuilder withProduct(ProductModel product)
    {
        getModel().setProduct(product);
        return this;
    }


    public OrderEntryModelBuilder withQuantity(Long quantity)
    {
        getModel().setQuantity(quantity);
        return this;
    }


    public OrderEntryModelBuilder withUnit(UnitModel unit)
    {
        getModel().setUnit(unit);
        return this;
    }


    public OrderEntryModelBuilder withConsignmentEntries(ConsignmentEntryModel... entries)
    {
        getModel().setConsignmentEntries(Sets.newHashSet((Object[])entries));
        return this;
    }


    public OrderEntryModelBuilder withDeliveryPointOfService(PointOfServiceModel deliveryPointOfService)
    {
        getModel().setDeliveryPointOfService(deliveryPointOfService);
        return this;
    }


    public OrderEntryModelBuilder withGiveAway(Boolean giveAway)
    {
        getModel().setGiveAway(giveAway);
        return this;
    }


    public OrderEntryModelBuilder withCalculated(Boolean calculated)
    {
        getModel().setCalculated(calculated);
        return this;
    }


    public OrderEntryModelBuilder withRejected(Boolean rejected)
    {
        getModel().setRejected(rejected);
        return this;
    }


    public OrderEntryModelBuilder withBasePrice(Double basePrice)
    {
        getModel().setBasePrice(basePrice);
        return this;
    }


    public OrderEntryModelBuilder withTotalPrice(Double totalPrice)
    {
        getModel().setTotalPrice(totalPrice);
        return this;
    }


    public OrderEntryModelBuilder withTaxes(Collection<TaxValue> taxes)
    {
        getModel().setTaxValues(taxes);
        return this;
    }
}
