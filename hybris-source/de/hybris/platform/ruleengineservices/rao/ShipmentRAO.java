package de.hybris.platform.ruleengineservices.rao;

public class ShipmentRAO extends AbstractRuleActionRAO
{
    private DeliveryModeRAO mode;


    public void setMode(DeliveryModeRAO mode)
    {
        this.mode = mode;
    }


    public DeliveryModeRAO getMode()
    {
        return this.mode;
    }
}
