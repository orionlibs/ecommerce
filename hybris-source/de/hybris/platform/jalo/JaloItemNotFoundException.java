package de.hybris.platform.jalo;

import de.hybris.platform.core.PK;
import java.util.Set;

public class JaloItemNotFoundException extends JaloSystemException
{
    private Set<PK> invalidPKs;


    public JaloItemNotFoundException(String message, int vendorCode)
    {
        super(null, message, vendorCode);
    }


    public JaloItemNotFoundException(Throwable nested, int vendorCode)
    {
        super(nested, nested.getMessage(), vendorCode);
    }


    public Set<PK> getInvalidPKs()
    {
        return this.invalidPKs;
    }


    public void setInvalidPKs(Set<PK> invalidPKs)
    {
        this.invalidPKs = invalidPKs;
    }
}
