package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloBusinessException;
import java.util.Set;

public class EJBItemNotFoundException extends JaloBusinessException
{
    private Set<PK> invalidPKs;


    public EJBItemNotFoundException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
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
