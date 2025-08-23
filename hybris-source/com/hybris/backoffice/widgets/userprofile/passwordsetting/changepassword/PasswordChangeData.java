/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.userprofile.passwordsetting.changepassword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordChangeData
{
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private boolean validationPassed;
    private List<String> validations;


    public PasswordChangeData()
    {
        oldPassword = "";
        newPassword = "";
        confirmPassword = "";
        validations = new ArrayList<>();
    }


    public String getOldPassword()
    {
        return oldPassword;
    }


    public void setOldPassword(final String oldPassword)
    {
        this.oldPassword = oldPassword;
    }


    public String getNewPassword()
    {
        return newPassword;
    }


    public void setNewPassword(final String newPassword)
    {
        this.newPassword = newPassword;
    }


    public String getConfirmPassword()
    {
        return confirmPassword;
    }


    public void setConfirmPassword(final String confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }


    public boolean isValidationPassed()
    {
        return validationPassed;
    }


    public void setValidationPassed(final boolean validationPassed)
    {
        this.validationPassed = validationPassed;
    }


    public List<String> getValidations()
    {
        return Collections.unmodifiableList(validations);
    }


    public void setValidations(final List<String> validations)
    {
        if(validations != null)
        {
            this.validations.clear();
            this.validations.addAll(validations);
        }
    }


    public void clearValidations()
    {
        this.validations.clear();
    }


    public void clear()
    {
        oldPassword = "";
        newPassword = "";
        confirmPassword = "";
        validationPassed = false;
        validations.clear();
    }
}
