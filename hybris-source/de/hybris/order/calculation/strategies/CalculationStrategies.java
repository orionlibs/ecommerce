package de.hybris.order.calculation.strategies;

public class CalculationStrategies
{
    private RoundingStrategy roundingStrategy;
    private RoundingStrategy taxRondingStrategy;


    public void setRoundingStrategy(RoundingStrategy roundingStrategy)
    {
        this.roundingStrategy = roundingStrategy;
    }


    public RoundingStrategy getRoundingStrategy()
    {
        return this.roundingStrategy;
    }


    public void setTaxRoundingStrategy(RoundingStrategy taxRondingStrategy)
    {
        this.taxRondingStrategy = taxRondingStrategy;
    }


    public RoundingStrategy getTaxRondingStrategy()
    {
        return this.taxRondingStrategy;
    }
}
