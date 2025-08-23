package de.hybris.platform.jalo;

import de.hybris.platform.core.PK;
import java.util.Objects;

public class JaloObjectNoLongerValidException extends JaloSystemException
{
    private final PK pk;
    private final Item item;


    public JaloObjectNoLongerValidException(PK pk, Exception exception, String message, int vendorCode)
    {
        super(exception, message, vendorCode);
        this.item = null;
        this.pk = Objects.<PK>requireNonNull(pk);
    }


    public JaloObjectNoLongerValidException(Item item, Exception exception, String message, int vendorCode)
    {
        super(exception, message, vendorCode);
        this.item = item;
        this.pk = (item == null) ? PK.NULL_PK : item.getPK();
    }


    public Item getJaloObject()
    {
        return this.item;
    }


    public PK getJaloObjectPK()
    {
        return this.pk;
    }


    public String getMessage()
    {
        return super.getMessage() + ": object no longer valid";
    }


    public String toString()
    {
        return getMessage();
    }
}
