package de.hybris.platform.platformbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.PasswordPolicyService;
import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class UserPasswordPanelRenderer implements WidgetComponentRenderer<Component, CustomPanel, Object>
{
    protected static final String SCLASS_EDITOR_SPACER = "yw-editorarea-password-editor-spacer";
    protected static final String SCLASS_EDITOR_CONFIRM = "yw-editorarea-password-editor-confirm";
    private static final String SCLASS_MATCH_VALIDATION = "yw-editorarea-password-match-validation";
    private static final String SCLASS_CELL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell";
    private static final Logger LOG = LoggerFactory.getLogger(UserPasswordPanelRenderer.class);
    private UserService userService;
    private PermissionFacade permissionFacade;
    private PasswordPolicyService passwordPolicyService;


    public void render(Component component, CustomPanel abstractPanelConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(object instanceof UserModel)
        {
            Validate.notNull(getUserService(), "Cannot find bean for UserService interface");
            if(!getPermissionFacade().canReadProperty(dataType.getCode(), "password"))
            {
                return;
            }
            UserModel user = (UserModel)object;
            boolean writable = getPermissionFacade().canChangeProperty(dataType.getCode(), "password");
            Component passContainer = createPasswordContainer();
            Cell passTypeBox = new Cell();
            passTypeBox.setClass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell");
            Cell changePassBox = new Cell();
            changePassBox.setClass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell");
            YTestTools.modifyYTestId((Component)changePassBox, "user-password-box");
            Label encryptionChooserLabel = new Label(Labels.getLabel("hmc.passwordtype"));
            passTypeBox.appendChild((Component)encryptionChooserLabel);
            Combobox encryptionChooser = createEncryptionChooser(user, writable);
            passTypeBox.appendChild((Component)encryptionChooser);
            Label changePasswordLabel = new Label(Labels.getLabel("hmc.chnagepassword"));
            changePassBox.appendChild((Component)changePasswordLabel);
            Textbox passwordBox = createPasswordTextbox(writable);
            passwordBox.setPlaceholder(Labels.getLabel("hmc.newpassword"));
            passwordBox.setClass("yw-editorarea-password-editor-spacer");
            passwordBox.setInstant(true);
            changePassBox.appendChild((Component)passwordBox);
            YTestTools.modifyYTestId((Component)passwordBox, "user-password-provided");
            Textbox confirmationBox = createPasswordTextbox(writable);
            confirmationBox.setPlaceholder(Labels.getLabel("hmc.repeatpassword"));
            confirmationBox.setClass("yw-editorarea-password-editor-confirm");
            confirmationBox.setInstant(true);
            changePassBox.appendChild((Component)confirmationBox);
            YTestTools.modifyYTestId((Component)confirmationBox, "user-password-confirmed");
            Label notMatchLabel = new Label(Labels.getLabel("hmc.passwordsdonotmatch"));
            notMatchLabel.setClass("yw-editorarea-password-match-validation");
            notMatchLabel.setMultiline(true);
            notMatchLabel.setVisible(false);
            changePassBox.appendChild((Component)notMatchLabel);
            passContainer.appendChild((Component)passTypeBox);
            passContainer.appendChild((Component)changePassBox);
            passContainer.setParent(component);
            if(writable)
            {
                EventListener<InputEvent> passwordChangeListener = createPasswordChangeListener(user, passwordBox, confirmationBox, encryptionChooser, notMatchLabel, widgetInstanceManager);
                passwordBox.addEventListener("onChange", passwordChangeListener);
                confirmationBox.addEventListener("onChange", passwordChangeListener);
                EventListener clearListener = event -> {
                    passwordBox.setText(null);
                    confirmationBox.setText(null);
                };
                EditorAreaRendererUtils.setAfterCancelListener(widgetInstanceManager.getModel(), "userPasswordAfterCancel", clearListener, false);
            }
        }
        else
        {
            LOG.error("Given object is not of UserModel type, please check your editor configuration!");
        }
    }


    protected Component createPasswordContainer()
    {
        return (Component)new Hbox();
    }


    protected Combobox createEncryptionChooser(UserModel user, boolean enabled)
    {
        Collection<String> installedPasswordEncodings = getInstalledPasswordEncodings();
        String userPasswordEncoding = user.getPasswordEncoding();
        Combobox encryptionChooser = new Combobox();
        encryptionChooser.setDisabled(!enabled);
        if(CollectionUtils.isNotEmpty(installedPasswordEncodings))
        {
            Comboitem selectedItem = null;
            for(String encoding : installedPasswordEncodings)
            {
                Comboitem encodingComboItem = new Comboitem(getLocalizedEncodingName(encoding));
                encodingComboItem.setValue(encoding);
                encryptionChooser.appendChild((Component)encodingComboItem);
                if(StringUtils.equals(userPasswordEncoding, encoding))
                {
                    selectedItem = encodingComboItem;
                }
            }
            if(selectedItem != null)
            {
                encryptionChooser.setSelectedItem(selectedItem);
            }
            else
            {
                encryptionChooser.setSelectedIndex(0);
            }
        }
        return encryptionChooser;
    }


    protected Collection<String> getInstalledPasswordEncodings()
    {
        return Utilities.getInstalledPasswordEncodings();
    }


    private String getLocalizedEncodingName(String encoding)
    {
        String localizedEncoding = Labels.getLabel(String.format("hmc.encrypt_%s", new Object[] {encoding}));
        return StringUtils.isNotBlank(localizedEncoding) ? localizedEncoding : ("[" + encoding + "]");
    }


    protected Textbox createPasswordTextbox(boolean enabled)
    {
        Textbox passwordBox = new Textbox();
        passwordBox.setDisabled(!enabled);
        passwordBox.setType("password");
        return passwordBox;
    }


    protected EventListener<InputEvent> createPasswordChangeListener(UserModel user, Textbox passwordTextbox, Textbox passwordConfirmationTextbox, Combobox encryptionChooser, Label messageLabel, WidgetInstanceManager widgetInstanceManager)
    {
        return event -> {
            String password = passwordTextbox.getText();
            if(password == null)
            {
                return;
            }
            if(password.equals(passwordConfirmationTextbox.getText()))
            {
                messageLabel.setVisible(false);
                Comboitem selectedItem = encryptionChooser.getSelectedItem();
                if(selectedItem != null)
                {
                    String encryption = (String)selectedItem.getValue();
                    List<PasswordPolicyViolation> passwordPolicyViolations = getPasswordPolicyService().verifyPassword(user, password, encryption);
                    renderPasswordPolicyViolations(messageLabel, passwordPolicyViolations);
                    UserModel refreshedCurrentUser = getRefreshedCurrentUser(user, widgetInstanceManager);
                    if(passwordPolicyViolations.isEmpty() && refreshedCurrentUser != null)
                    {
                        this.userService.setPassword(refreshedCurrentUser, password, encryption);
                        widgetInstanceManager.getModel().changed();
                    }
                }
            }
            else
            {
                messageLabel.setValue(Labels.getLabel("hmc.passwordsdonotmatch"));
                messageLabel.setVisible(true);
            }
        };
    }


    private UserModel getRefreshedCurrentUser(UserModel renderedUser, WidgetInstanceManager wim)
    {
        UserModel current = (UserModel)wim.getModel().getValue("currentObject", UserModel.class);
        if(current != null)
        {
            return Objects.equals(current, renderedUser) ? current : null;
        }
        return renderedUser;
    }


    private void renderPasswordPolicyViolations(Label messageLabel, List<PasswordPolicyViolation> violations)
    {
        StringBuilder sb = new StringBuilder();
        for(PasswordPolicyViolation violation : violations)
        {
            sb.append(violation.getLocalizedMessage()).append("\n");
        }
        messageLabel.setValue(sb.toString());
        messageLabel.setVisible(true);
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return this.permissionFacade;
    }


    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    protected PasswordPolicyService getPasswordPolicyService()
    {
        return this.passwordPolicyService;
    }


    public void setPasswordPolicyService(PasswordPolicyService passwordPolicyService)
    {
        this.passwordPolicyService = passwordPolicyService;
    }
}
