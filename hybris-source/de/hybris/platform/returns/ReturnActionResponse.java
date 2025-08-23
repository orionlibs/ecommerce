package de.hybris.platform.returns;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.List;

public class ReturnActionResponse extends ReturnActionRequest
{
    private ResponseStatus responseStatus;


    public ReturnActionResponse(ReturnRequestModel returnRequest, List<ReturnActionEntry> returnActionEntries)
    {
        super(returnRequest, returnActionEntries);
        this.responseStatus = ResponseStatus.partial;
    }


    public ReturnActionResponse(ReturnRequestModel returnRequest, List<ReturnActionEntry> returnActionEntries, ResponseStatus status, String statusMessage)
    {
        super(returnRequest, returnActionEntries, statusMessage);
        this.responseStatus = status;
    }


    public ReturnActionResponse(ReturnRequestModel returnRequest)
    {
        super(returnRequest);
        this.responseStatus = ResponseStatus.full;
    }


    public ReturnActionResponse(ReturnRequestModel returnRequest, ResponseStatus status, String statusMessage)
    {
        super(returnRequest, (HybrisEnumValue)CancelReason.NA, statusMessage);
        this.responseStatus = ResponseStatus.full;
        this.responseStatus = status;
    }


    public ResponseStatus getResponseStatus()
    {
        return this.responseStatus;
    }
}
