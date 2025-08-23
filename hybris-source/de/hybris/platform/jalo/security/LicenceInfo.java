package de.hybris.platform.jalo.security;

public class LicenceInfo
{
    private final Status status;
    private final String currentValue;
    private final String allowedValue;
    private final String message;


    public LicenceInfo(Status st, String cValue, String aValue, String mes)
    {
        this.status = st;
        this.allowedValue = aValue;
        this.currentValue = cValue;
        this.message = mes;
    }


    public String toString()
    {
        return "" + this.status + ": current value: \"" + this.status + "\"; allowed value: \"" + this.currentValue + "\"; message: \"" + this.allowedValue + "\"";
    }


    public String getAllowedValue()
    {
        return this.allowedValue;
    }


    public String getCurrentValue()
    {
        return this.currentValue;
    }


    public String getMessage()
    {
        return this.message;
    }


    public String getStatus()
    {
        return this.status.toString();
    }
}
