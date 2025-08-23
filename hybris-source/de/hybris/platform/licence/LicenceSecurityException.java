package de.hybris.platform.licence;

import org.apache.log4j.Logger;

public class LicenceSecurityException extends SecurityException
{
    static final Logger log = Logger.getLogger(LicenceSecurityException.class.getName());
    private final int vendorCode;


    public LicenceSecurityException(String message, int aVendorCode)
    {
        super(message);
        this.vendorCode = aVendorCode;
    }


    public String getMessage()
    {
        return super.getMessage();
    }


    public int getVendorCode()
    {
        return this.vendorCode;
    }


    public String toString()
    {
        String s = getClass().getName();
        s = s + ": ";
        if(getMessage() != null)
        {
            s = s + s;
        }
        s = s + "[VC:" + s + "]";
        return s;
    }
}
