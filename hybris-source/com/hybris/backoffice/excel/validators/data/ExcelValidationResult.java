package com.hybris.backoffice.excel.validators.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExcelValidationResult implements Serializable, Comparable<ExcelValidationResult>
{
    public static final ExcelValidationResult SUCCESS = new ExcelValidationResult(new ArrayList<>());
    private ValidationMessage header;
    private List<ValidationMessage> validationErrors;
    private boolean workbookValidationResult;


    public ExcelValidationResult(ValidationMessage header, List<ValidationMessage> validationErrors)
    {
        this.header = header;
        this.validationErrors = validationErrors;
    }


    public ExcelValidationResult(List<ValidationMessage> validationErrors)
    {
        this.validationErrors = validationErrors;
    }


    public ExcelValidationResult(ValidationMessage validationError)
    {
        this.validationErrors = new ArrayList<>();
        this.validationErrors.add(validationError);
    }


    public void addValidationError(ValidationMessage validationMessage)
    {
        this.validationErrors.add(validationMessage);
    }


    public boolean hasErrors()
    {
        return !this.validationErrors.isEmpty();
    }


    public ValidationMessage getHeader()
    {
        return this.header;
    }


    public void setHeader(ValidationMessage header)
    {
        this.header = header;
    }


    public List<ValidationMessage> getValidationErrors()
    {
        return this.validationErrors;
    }


    public void setValidationErrors(List<ValidationMessage> validationErrors)
    {
        this.validationErrors = validationErrors;
    }


    public boolean isWorkbookValidationResult()
    {
        return this.workbookValidationResult;
    }


    public void setWorkbookValidationResult(boolean workbookValidationResult)
    {
        this.workbookValidationResult = workbookValidationResult;
    }


    public int compareTo(ExcelValidationResult another)
    {
        return this.header.compareTo(another.getHeader());
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        ExcelValidationResult that = (ExcelValidationResult)o;
        if((this.header != null) ? !this.header.equals(that.header) : (that.header != null))
        {
            return false;
        }
        return (this.validationErrors != null) ? this.validationErrors.equals(that.validationErrors) : ((that.validationErrors == null));
    }


    public int hashCode()
    {
        int result = (this.header != null) ? this.header.hashCode() : 0;
        result = 31 * result + ((this.validationErrors != null) ? this.validationErrors.hashCode() : 0);
        return result;
    }
}
