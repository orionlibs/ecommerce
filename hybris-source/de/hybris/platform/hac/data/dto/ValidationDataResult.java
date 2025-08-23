package de.hybris.platform.hac.data.dto;

public class ValidationDataResult
{
    private boolean valid;
    private String message;


    public boolean isValid()
    {
        return this.valid;
    }


    public void setValid(boolean valid)
    {
        this.valid = valid;
    }


    public String getMessage()
    {
        return this.message;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }
}
