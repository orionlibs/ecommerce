package de.hybris.platform.warehousing.sourcing.util;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.util.Builder;
import java.util.HashMap;
import java.util.Map;

public class SourcingLocationBuilder implements Builder<SourcingLocation>
{
    public static final Double DEFAULT_DISTANCE = Double.valueOf(100.0D);
    public static final Integer DEFAULT_PRIORITY = Integer.valueOf(1);
    public static final Integer DEFAULT_INITIAL_ID = Integer.valueOf(0);
    public static final String DEFAULT_SKU = "SKU1";
    public static final Long DEFAULT_SKU_QUANTITY = Long.valueOf(10L);
    private static Integer uniqueId = DEFAULT_INITIAL_ID;
    private Double distance = DEFAULT_DISTANCE;
    private Integer priority = DEFAULT_PRIORITY;
    private SourcingContext context;
    private Map<ProductModel, Long> availability;
    private WarehouseModel warehouse;
    private ProductModel productModel;


    public SourcingLocationBuilder()
    {
        this.availability = new HashMap<>();
    }


    public static SourcingLocationBuilder aSourcingLocation()
    {
        return new SourcingLocationBuilder();
    }


    public SourcingLocation build()
    {
        if(this.availability.isEmpty())
        {
            withCustomAvailabilitySkuQuantity("SKU1", DEFAULT_SKU_QUANTITY);
        }
        if(this.warehouse.getCode().equals(""))
        {
            withDefaultWarehouse();
        }
        SourcingLocation sourcingLoc = new SourcingLocation();
        sourcingLoc.setContext(this.context);
        sourcingLoc.setDistance(this.distance);
        sourcingLoc.setWarehouse(this.warehouse);
        sourcingLoc.setAvailability(this.availability);
        sourcingLoc.setPriority(this.priority);
        return sourcingLoc;
    }


    public SourcingLocationBuilder withWarehouse(WarehouseModel warehouse)
    {
        this.warehouse = warehouse;
        return this;
    }


    public SourcingLocationBuilder withDistance(Double distance)
    {
        this.distance = distance;
        return this;
    }


    public SourcingLocationBuilder withContext(SourcingContext context)
    {
        this.context = context;
        return this;
    }


    public SourcingLocationBuilder withWarehouseCode(String id)
    {
        this.warehouse = new WarehouseModel();
        this.warehouse.setCode(id);
        return this;
    }


    public SourcingLocationBuilder withAvailability(Map<ProductModel, Long> availability)
    {
        this.availability = availability;
        return this;
    }


    public SourcingLocationBuilder withPriority(Integer priority)
    {
        this.priority = priority;
        return this;
    }


    public SourcingLocationBuilder withDefaultAvailabilityQuantity(Long quantity)
    {
        withCustomAvailabilitySkuQuantity("SKU1", quantity);
        return this;
    }


    public SourcingLocationBuilder withCustomAvailabilitySkuQuantity(String SKUId, Long quantity)
    {
        this.productModel = new ProductModel();
        this.productModel.setCode(SKUId);
        this.availability.put(this.productModel, quantity);
        return this;
    }


    public SourcingLocationBuilder withCustomAvailabilityProductQuantity(ProductModel productModel, Long quantity)
    {
        this.availability.put(productModel, quantity);
        return this;
    }


    private SourcingLocationBuilder withDefaultWarehouse()
    {
        this.warehouse = new WarehouseModel();
        this.warehouse.setCode(generateId());
        return this;
    }


    private String generateId()
    {
        Integer integer = uniqueId;
        uniqueId = Integer.valueOf(uniqueId.intValue() + 1);
        return "loc" + uniqueId;
    }
}
