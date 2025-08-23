package de.hybris.platform.b2bapprovalprocessfacades.company.data;

import de.hybris.platform.b2b.enums.PermissionStatus;
import java.io.Serializable;

public class B2BPermissionResultData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private PermissionStatus status;
    private String approver;
    private String permissionToEvaluate;
    private String permissionTypeCode;
    private String approverNotes;
    private String statusDisplay;
    private B2BPermissionTypeData permissionTypeData;


    public void setStatus(PermissionStatus status)
    {
        this.status = status;
    }


    public PermissionStatus getStatus()
    {
        return this.status;
    }


    public void setApprover(String approver)
    {
        this.approver = approver;
    }


    public String getApprover()
    {
        return this.approver;
    }


    public void setPermissionToEvaluate(String permissionToEvaluate)
    {
        this.permissionToEvaluate = permissionToEvaluate;
    }


    public String getPermissionToEvaluate()
    {
        return this.permissionToEvaluate;
    }


    public void setPermissionTypeCode(String permissionTypeCode)
    {
        this.permissionTypeCode = permissionTypeCode;
    }


    public String getPermissionTypeCode()
    {
        return this.permissionTypeCode;
    }


    public void setApproverNotes(String approverNotes)
    {
        this.approverNotes = approverNotes;
    }


    public String getApproverNotes()
    {
        return this.approverNotes;
    }


    public void setStatusDisplay(String statusDisplay)
    {
        this.statusDisplay = statusDisplay;
    }


    public String getStatusDisplay()
    {
        return this.statusDisplay;
    }


    public void setPermissionTypeData(B2BPermissionTypeData permissionTypeData)
    {
        this.permissionTypeData = permissionTypeData;
    }


    public B2BPermissionTypeData getPermissionTypeData()
    {
        return this.permissionTypeData;
    }
}
