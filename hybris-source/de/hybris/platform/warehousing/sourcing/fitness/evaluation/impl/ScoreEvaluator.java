package de.hybris.platform.warehousing.sourcing.fitness.evaluation.impl;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluator;

public class ScoreEvaluator implements FitnessEvaluator
{
    public Double evaluate(SourcingLocation sourcingLocation)
    {
        WarehouseModel warehouse = sourcingLocation.getWarehouse();
        return Double.valueOf((warehouse != null && warehouse.getScore() != null) ? warehouse.getScore().doubleValue() : Double.NaN);
    }
}
