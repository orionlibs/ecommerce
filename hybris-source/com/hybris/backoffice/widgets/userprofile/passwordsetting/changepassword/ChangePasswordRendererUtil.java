/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.userprofile.passwordsetting.changepassword;

import com.hybris.cockpitng.core.Executable;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordPolicyService;
import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public class ChangePasswordRendererUtil
{
    private static final String SCLASS_PASSWORD_SETTING_LINE = "yw-password-setting-line";
    private static final String SCLASS_PASSWORD_SETTING_LINE_LABEL = "yw-password-setting-line-label";
    private static final String SCLASS_PASSWORD_SETTING_LINE_TEXTBOX = "yw-password-setting-line-input";
    private static final String SCLASS_PASSWORD_SETTING_VALIDATION_CONTAINER = "yw-notification-message failure";
    private static final String SCLASS_PASSWORD_SETTING_VALIDATION = "yw-password-setting-line-validation";
    private static final String TEXTBOX_TYPE = "password";
    private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordRendererUtil.class);
    private ModelService modelService;
    private PasswordPolicyService passwordPolicyService;
    private Div validationDiv;


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return modelService;
    }


    @Required
    public void setPasswordPolicyService(final PasswordPolicyService passwordPolicyService)
    {
        this.passwordPolicyService = passwordPolicyService;
    }


    protected PasswordPolicyService getPasswordPolicyService()
    {
        return passwordPolicyService;
    }


    public Label createNoticeLabel(final Component parent, final String labelKey)
    {
        final Div div = new Div();
        div.setSclass(SCLASS_PASSWORD_SETTING_LINE);
        div.setParent(parent);
        final Label label = new Label(Labels.getLabel(labelKey));
        label.setParent(div);
        return label;
    }


    public void createValidationInfoLine(final Component parent)
    {
        validationDiv = new Div();
        validationDiv.setSclass(SCLASS_PASSWORD_SETTING_VALIDATION_CONTAINER);
        validationDiv.setParent(parent);
        validationDiv.setVisible(false);
        validationDiv.addEventListener(Events.ON_CLICK, event -> validationDiv.setVisible(false));
    }


    public Textbox createPasswordLine(final Component parent, final String labelKey)
    {
        final Div passwordLine = new Div();
        passwordLine.setSclass(SCLASS_PASSWORD_SETTING_LINE);
        passwordLine.setParent(parent);
        final Label label = new Label(Labels.getLabel(labelKey));
        label.setSclass(SCLASS_PASSWORD_SETTING_LINE_LABEL);
        label.setParent(passwordLine);
        final Textbox passwordBox = new Textbox();
        passwordBox.setSclass(SCLASS_PASSWORD_SETTING_LINE_TEXTBOX);
        passwordBox.setParent(passwordLine);
        passwordBox.setType(TEXTBOX_TYPE);
        passwordBox.setInstant(true);
        return passwordBox;
    }


    public void onOldPasswordTextChanged(final InputElement oldPwdInput,
                    final PasswordChangeData passwordChangeData, final Executable dataChangedExe)
    {
        passwordChangeData.setOldPassword(oldPwdInput.getText());
        dataChangedExe.execute();
    }


    public void onNewPasswordTextChanged(final UserModel user, final InputElement newPwdInput,
                    final PasswordChangeData passwordChangeData, final Executable dataChangedExe)
    {
        passwordChangeData.setNewPassword(newPwdInput.getText());
        passwordChangeData
                        .setValidationPassed(validateNewPassword(user, passwordChangeData) && validateConfirmPassword(passwordChangeData));
        updateValidation(passwordChangeData);
        dataChangedExe.execute();
    }


    public void onConfirmPwdTextChanged(final UserModel user, final InputElement confirmPwdInput,
                    final PasswordChangeData passwordChangeData, final Executable dataChangedExe)
    {
        passwordChangeData.setConfirmPassword(confirmPwdInput.getText());
        passwordChangeData
                        .setValidationPassed(validateConfirmPassword(passwordChangeData) && validateNewPassword(user, passwordChangeData));
        updateValidation(passwordChangeData);
        dataChangedExe.execute();
    }


    protected boolean checkPassword(final User user, final String password) throws EJBPasswordEncoderNotFoundException
    {
        return UserManager.getInstance().checkPassword(user, password);
    }


    protected Div getValidationDiv()
    {
        return validationDiv;
    }


    public boolean validateOldPassword(final UserModel user, final PasswordChangeData passwordChangeData)
    {
        try
        {
            final boolean isOk = checkPassword(getModelService().getSource(user), passwordChangeData.getOldPassword());
            if(isOk)
            {
                passwordChangeData.clearValidations();
            }
            else
            {
                final List<String> validations = new ArrayList<>();
                validations.add(Labels.getLabel("userprofile.changepassword.wrong.old.password"));
                passwordChangeData.setValidations(validations);
                updateValidation(passwordChangeData);
            }
            return isOk;
        }
        catch(final EJBPasswordEncoderNotFoundException e)
        {
            LOG.error("Cannot validate the old password: ", e);
            return false;
        }
    }


    private boolean validateNewPassword(final UserModel user, final PasswordChangeData passwordChangeData)
    {
        final List<PasswordPolicyViolation> passwordPolicyViolations = getPasswordPolicyService().verifyPassword(user,
                        passwordChangeData.getNewPassword(), user.getPasswordEncoding());
        if(!passwordPolicyViolations.isEmpty())
        {
            passwordChangeData.setValidations(getPasswordPolicyViolationsMessage(passwordPolicyViolations));
            return false;
        }
        passwordChangeData.clearValidations();
        return true;
    }


    private boolean validateConfirmPassword(final PasswordChangeData passwordChangeData)
    {
        if(!StringUtils.equals(passwordChangeData.getNewPassword(), passwordChangeData.getConfirmPassword()))
        {
            final List<String> validations = new ArrayList<>();
            validations.add(Labels.getLabel("hmc.passwordsdonotmatch"));
            passwordChangeData.setValidations(validations);
            return false;
        }
        passwordChangeData.clearValidations();
        return true;
    }


    private void updateValidation(final PasswordChangeData passwordChangeData)
    {
        Components.removeAllChildren(validationDiv);
        final List<String> validations = Optional.ofNullable(passwordChangeData.getValidations()).orElse(new ArrayList<>());
        for(final String info : validations)
        {
            final Label messageLabel = new Label(info);
            messageLabel.setClass(SCLASS_PASSWORD_SETTING_VALIDATION);
            validationDiv.appendChild(messageLabel);
        }
        validationDiv.setVisible(!validations.isEmpty());
    }


    private List<String> getPasswordPolicyViolationsMessage(final List<PasswordPolicyViolation> violations)
    {
        return violations.stream().map(PasswordPolicyViolation::getLocalizedMessage).collect(Collectors.toList());
    }


    public void clearValidation()
    {
        validationDiv.setVisible(false);
    }
}
