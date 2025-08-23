package de.hybris.platform.acceleratorservices.cartfileupload.data;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import java.io.Serializable;
import java.util.List;

public class SavedCartFileUploadReportData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CommerceCartModification> errorModificationList;
    private Integer successCount;
    private Integer partialImportCount;
    private Integer failureCount;


    public void setErrorModificationList(List<CommerceCartModification> errorModificationList)
    {
        this.errorModificationList = errorModificationList;
    }


    public List<CommerceCartModification> getErrorModificationList()
    {
        return this.errorModificationList;
    }


    public void setSuccessCount(Integer successCount)
    {
        this.successCount = successCount;
    }


    public Integer getSuccessCount()
    {
        return this.successCount;
    }


    public void setPartialImportCount(Integer partialImportCount)
    {
        this.partialImportCount = partialImportCount;
    }


    public Integer getPartialImportCount()
    {
        return this.partialImportCount;
    }


    public void setFailureCount(Integer failureCount)
    {
        this.failureCount = failureCount;
    }


    public Integer getFailureCount()
    {
        return this.failureCount;
    }
}
