package de.hybris.platform.commercewebservices.core.user.data;

import java.io.Serializable;

public class PasswordRestoreResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    private boolean success;


    public void setSuccess(boolean success)
    {
        this.success = success;
    }


    public boolean isSuccess()
    {
        return this.success;
    }
}
