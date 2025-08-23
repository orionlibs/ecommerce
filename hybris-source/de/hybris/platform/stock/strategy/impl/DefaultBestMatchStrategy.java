package de.hybris.platform.stock.strategy.impl;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.strategy.BestMatchStrategy;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DefaultBestMatchStrategy implements BestMatchStrategy
{
    private Date farFarWayDate = null;


    public WarehouseModel getBestMatchOfQuantity(Map<WarehouseModel, Integer> map)
    {
        WarehouseModel bestOne = null;
        int highestSoFar = 0;
        for(Map.Entry<WarehouseModel, Integer> entry : map.entrySet())
        {
            WarehouseModel warehouse = entry.getKey();
            int quantity = ((Integer)entry.getValue()).intValue();
            if(quantity > highestSoFar)
            {
                highestSoFar = quantity;
                bestOne = warehouse;
            }
        }
        return bestOne;
    }


    public WarehouseModel getBestMatchOfAvailability(Map<WarehouseModel, Date> map)
    {
        WarehouseModel bestOne = null;
        Date earliestSoFar = farFarWay();
        for(Map.Entry<WarehouseModel, Date> entry : map.entrySet())
        {
            WarehouseModel warehouse = entry.getKey();
            Date date = entry.getValue();
            if(date.before(earliestSoFar))
            {
                earliestSoFar = date;
                bestOne = warehouse;
            }
        }
        return bestOne;
    }


    private Date farFarWay()
    {
        if(this.farFarWayDate == null)
        {
            int year = 3000;
            int month = 0;
            int date = 1;
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(1, 3000);
            cal.set(2, 0);
            cal.set(5, 1);
            this.farFarWayDate = cal.getTime();
        }
        return this.farFarWayDate;
    }
}
