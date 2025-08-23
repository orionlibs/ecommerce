package de.hybris.platform.ordermanagementfacades.returns.data;

import de.hybris.platform.basecommerce.enums.CancelReason;
import java.io.Serializable;

public class CancelReturnRequestData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private CancelReason cancelReason;
    private String notes;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCancelReason(CancelReason cancelReason)
    {
        this.cancelReason = cancelReason;
    }


    public CancelReason getCancelReason()
    {
        return this.cancelReason;
    }


    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    public String getNotes()
    {
        return this.notes;
    }
}
