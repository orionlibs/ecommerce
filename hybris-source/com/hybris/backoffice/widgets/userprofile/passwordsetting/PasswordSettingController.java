/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.userprofile.passwordsetting;

import com.hybris.backoffice.masterdetail.MDDetailLogic;
import com.hybris.backoffice.masterdetail.MasterDetailService;
import com.hybris.backoffice.masterdetail.SettingButton;
import com.hybris.backoffice.masterdetail.SettingButton.TypesEnum;
import com.hybris.backoffice.masterdetail.SettingItem;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.userprofile.passwordsetting.changepassword.ChangePasswordRendererUtil;
import com.hybris.backoffice.widgets.userprofile.passwordsetting.changepassword.PasswordChangeData;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class PasswordSettingController extends DefaultWidgetController implements MDDetailLogic
{
    protected static final String ENABLE_CHANGE_PASSWORD_PROPERTY = "backoffice.accountsettings.changepassword.enabled";
    protected static final String ENABLE_CHANGE_PASSWORD_SETTING = "changePasswordEnabled";
    private static final String CHANGEPASSWORD_TITLE = "userprofile.changepassword.title";
    private static final String CHANGEPASSWORD_NOTICE = "userprofile.changepassword.notice";
    protected static final String CHANGEPASSWORD_OLD_PWD_LABEL = "userprofile.changepassword.old.label";
    protected static final String CHANGEPASSWORD_NEW_PWD_LABEL = "userprofile.changepassword.new.label";
    protected static final String CHANGEPASSWORD_CONFIRM_PWD_LABEL = "userprofile.changepassword.confirm.label";
    private static final String SCLASS_PASSWORD_SETTING_LINE_TITLE = "yw-password-setting-line-title";
    private static final String SCLASS_PASSWORD_SETTING_LINE_NOTICE = "yw-password-setting-line-notice";
    private static final String NOTIFICATION_AREA = "changePassword";
    private static final String NOTIFICATION_TYPE_PASSWORD_CHANGED = "passwordChanged";
    private transient UserService userService;
    private transient NotificationService notificationService;
    private transient ModelService modelService;
    private ChangePasswordRendererUtil changePasswordRendererUtil;
    private MasterDetailService userProfileSettingService;
    private Textbox oldInput;
    private Textbox pwdInput;
    private Textbox confInput;
    private PasswordChangeData passwordChangeData = new PasswordChangeData();
    private SettingItem settingItem;
    private boolean isDataChanged = false;


    @Override
    public void preInitialize(final Component comp)
    {
        super.preInitialize(comp);
        getWidgetSettings().put(ENABLE_CHANGE_PASSWORD_SETTING, isChangePasswordEnabled());
    }


    @Override
    public void initialize(final Component comp)
    {
        if(!isChangePasswordEnabled())
        {
            return;
        }
        getUserProfileSettingService().registerDetail(this);
        final Div container = new Div();
        container.setParent(comp);
        final Label noticeTitle = getChangePasswordRendererUtil().createNoticeLabel(container, CHANGEPASSWORD_TITLE);
        noticeTitle.setSclass(SCLASS_PASSWORD_SETTING_LINE_TITLE);
        final Label noticeDescription = getChangePasswordRendererUtil().createNoticeLabel(container, CHANGEPASSWORD_NOTICE);
        noticeDescription.setSclass(SCLASS_PASSWORD_SETTING_LINE_NOTICE);
        oldInput = changePasswordRendererUtil.createPasswordLine(container, CHANGEPASSWORD_OLD_PWD_LABEL);
        pwdInput = changePasswordRendererUtil.createPasswordLine(container, CHANGEPASSWORD_NEW_PWD_LABEL);
        confInput = changePasswordRendererUtil.createPasswordLine(container, CHANGEPASSWORD_CONFIRM_PWD_LABEL);
        getChangePasswordRendererUtil().createValidationInfoLine(container);
        final UserModel currentUser = getUserService().getCurrentUser();
        oldInput.addEventListener(Events.ON_CHANGE,
                        event -> changePasswordRendererUtil.onOldPasswordTextChanged(oldInput, passwordChangeData, this::onDataChanged));
        pwdInput.addEventListener(Events.ON_CHANGE, event -> changePasswordRendererUtil.onNewPasswordTextChanged(currentUser,
                        pwdInput, passwordChangeData, this::onDataChanged));
        confInput.addEventListener(Events.ON_CHANGE, event -> changePasswordRendererUtil.onConfirmPwdTextChanged(currentUser,
                        confInput, passwordChangeData, this::onDataChanged));
    }


    private void onDataChanged()
    {
        this.isDataChanged = true;
        getUserProfileSettingService().enableSave(passwordChangeData.isValidationPassed());
    }


    protected boolean isChangePasswordEnabled()
    {
        return Config.getBoolean(ENABLE_CHANGE_PASSWORD_PROPERTY, true);
    }


    public boolean isDataChanged()
    {
        return isDataChanged;
    }


    @Override
    public boolean needRefreshUI()
    {
        return false;
    }


    public boolean save()
    {
        final UserModel currentUser = getUserService().getCurrentUser();
        if(getChangePasswordRendererUtil().validateOldPassword(currentUser, passwordChangeData))
        {
            getUserService().setPassword(currentUser, passwordChangeData.getConfirmPassword());
            getModelService().save(currentUser);
            getNotificationService().notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_PASSWORD_CHANGED,
                            NotificationEvent.Level.SUCCESS);
            getUserProfileSettingService().enableSave(false);
            isDataChanged = false;
            changePasswordRendererUtil.clearValidation();
            return true;
        }
        return false;
    }


    public void reset()
    {
        oldInput.setValue("");
        pwdInput.setValue("");
        confInput.setValue("");
        isDataChanged = false;
        passwordChangeData.clear();
        changePasswordRendererUtil.clearValidation();
    }


    public SettingItem getSettingItem()
    {
        if(settingItem == null)
        {
            settingItem = new SettingItem("backoffice-password-view", "private", Labels.getLabel("userprofile.changepassword.title"),
                            "******", false,
                            Arrays.asList(new SettingButton.Builder().setType(TypesEnum.SAVE).setDisabled(true).build(),
                                            new SettingButton.Builder().setType(TypesEnum.SAVE_AND_CLOSE).setDisabled(true).build(),
                                            new SettingButton.Builder().setType(TypesEnum.CANCEL).build()),
                            20);
        }
        return settingItem;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setChangePasswordRendererUtil(final ChangePasswordRendererUtil changePasswordRendererUtil)
    {
        this.changePasswordRendererUtil = changePasswordRendererUtil;
    }


    protected ChangePasswordRendererUtil getChangePasswordRendererUtil()
    {
        return changePasswordRendererUtil;
    }


    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    protected UserService getUserService()
    {
        return userService;
    }


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
    public void setUserProfileSettingService(final MasterDetailService userProfileSettingService)
    {
        this.userProfileSettingService = userProfileSettingService;
    }


    protected MasterDetailService getUserProfileSettingService()
    {
        return userProfileSettingService;
    }


    public void setOldInput(final Textbox oldInput)
    {
        this.oldInput = oldInput;
    }


    protected Textbox getOldInput()
    {
        return oldInput;
    }


    public void setPwdInput(final Textbox pwdInput)
    {
        this.pwdInput = pwdInput;
    }


    protected Textbox getPwdInput()
    {
        return pwdInput;
    }


    public void setConfInput(final Textbox confInput)
    {
        this.confInput = confInput;
    }


    protected Textbox getConfInput()
    {
        return confInput;
    }
}
