package com.hybris.datahub.dto.publication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hybris.datahub.dto.item.ErrorData;
import com.hybris.datahub.runtime.domain.PublicationActionStatusType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicationResult
{
    private List<ErrorData> exportErrorDatas = initialErrors();
    private String crashReport;


    public static PublicationResult createFor(@Nonnull Throwable e)
    {
        PublicationResult result = new PublicationResult();
        if(e.getMessage() != null)
        {
            result.setCrashReport(e.getMessage());
            if(e.getCause() != null)
            {
                result.setCrashReport(result.getCrashReport() + "; Caused by " + result.getCrashReport());
            }
        }
        else
        {
            result.setCrashReport("Unknown error occurred during publication");
        }
        return result;
    }


    private static List<ErrorData> initialErrors()
    {
        return new ArrayList<>(0);
    }


    public List<ErrorData> getExportErrorDatas()
    {
        return Collections.unmodifiableList(this.exportErrorDatas);
    }


    public void setExportErrorDatas(List<ErrorData> errors)
    {
        this.exportErrorDatas = (errors != null) ? errors : initialErrors();
    }


    public String getCrashReport()
    {
        return this.crashReport;
    }


    public void setCrashReport(String report)
    {
        if(report == null || !report.trim().isEmpty())
        {
            this.crashReport = report;
        }
    }


    public PublicationActionStatusType getStatus()
    {
        if(isCompleteWithErrors())
        {
            return PublicationActionStatusType.COMPLETE_W_ERRORS;
        }
        if(isFailure())
        {
            return PublicationActionStatusType.FAILURE;
        }
        return PublicationActionStatusType.SUCCESS;
    }


    public boolean isCompleteWithErrors()
    {
        return !getExportErrorDatas().isEmpty();
    }


    public boolean isFailure()
    {
        return (getCrashReport() != null);
    }


    public boolean isSuccess()
    {
        return (!isCompleteWithErrors() && !isFailure());
    }


    public String toString()
    {
        return "PublicationResult{status=" + getStatus() + ", crashReport=" + this.crashReport + "}";
    }
}
