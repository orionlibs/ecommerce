package de.hybris.datasupplier.data;

public class DSSendResult
{
    private final SupplyStatus supplyStatus;
    private final String payload;


    public DSSendResult(SupplyStatus supplyStatus)
    {
        this(supplyStatus, null);
    }


    public DSSendResult(SupplyStatus supplyStatus, String payload)
    {
        this.supplyStatus = supplyStatus;
        this.payload = payload;
    }


    public String getPayload()
    {
        return this.payload;
    }


    public SupplyStatus getSupplyStatus()
    {
        return this.supplyStatus;
    }
}
