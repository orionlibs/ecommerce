/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultpassword;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import java.util.Objects;
import java.util.Optional;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

public class DefaultPasswordEditor extends AbstractCockpitEditorRenderer<String>
{
    protected static final String PASSWORD_TYPE = "password";
    protected static final String HINT_LABEL = "default.password.editor.hint";
    protected static final String CONFIRM_LABEL = "default.password.editor.confirm";
    protected static final String ENTER_PASSWORD = "default.password.editor.enter";
    protected static final String YE_PASSWORD_NO_MATCH = "ye-password-no-match";
    private static final String SCLASS_MATCH_VALIDATION = "yw-editorarea-password-match-validation";
    protected static final String SETTING_WITH_CONFIRMATION = "withConfirmation";
    public static final String PASSWORDS_NOT_MATCH_LABEL = "default.password.editor.notmatch";


    @Override
    public void render(final Component parent, final EditorContext<String> context, final EditorListener<String> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Div divContainer = new Div();
        final Vlayout vlayout = new Vlayout();
        divContainer.appendChild(vlayout);
        final Popup passwordHint = new Popup();
        passwordHint.appendChild(new Label(getL10nDecorator(context, HINT_LABEL, HINT_LABEL)));
        final Textbox password = new Textbox();
        password.setType(PASSWORD_TYPE);
        password.setPlaceholder(context.getLabel(ENTER_PASSWORD));
        password.setTooltip(passwordHint);
        YTestTools.modifyYTestId(password, "password_input");
        vlayout.appendChild(password);
        final Label notMatchLabel = new Label(context.getLabel(PASSWORDS_NOT_MATCH_LABEL));
        notMatchLabel.setClass(SCLASS_MATCH_VALIDATION);
        notMatchLabel.setMultiline(true);
        notMatchLabel.setVisible(false);
        final boolean isReadOnly = isReadOnly(parent);
        password.setReadonly(isReadOnly);
        if(withConfirmation(context))
        {
            final Textbox passwordVerification = new Textbox();
            passwordVerification.setPlaceholder(context.getLabel(CONFIRM_LABEL));
            passwordVerification.setType(PASSWORD_TYPE);
            passwordVerification.setTooltip(passwordHint);
            vlayout.appendChild(passwordVerification);
            YTestTools.modifyYTestId(passwordVerification, "password_input_verify");
            password.setInstant(false);
            password.addEventListener(Events.ON_CHANGE, event -> DefaultPasswordEditor.this.onPasswordChangeAttempt(password,
                            passwordVerification, listener, notMatchLabel));
            passwordVerification.setInstant(false);
            passwordVerification.addEventListener(Events.ON_CHANGE, event -> DefaultPasswordEditor.this
                            .onPasswordChangeAttempt(passwordVerification, password, listener, notMatchLabel));
            passwordVerification.setReadonly(isReadOnly);
        }
        else
        {
            password.setInstant(true);
            password.addEventListener(Events.ON_CHANGE, event -> listener.onValueChanged(password.getValue()));
        }
        vlayout.appendChild(notMatchLabel);
        vlayout.appendChild(passwordHint);
        divContainer.setParent(parent);
    }


    protected void onPasswordChangeAttempt(final Textbox passwordChanged, final Textbox password,
                    final EditorListener<String> listener)
    {
        if(Objects.equals(passwordChanged.getValue(), password.getValue()))
        {
            UITools.modifySClass(passwordChanged, YE_PASSWORD_NO_MATCH, false);
            UITools.modifySClass(password, YE_PASSWORD_NO_MATCH, false);
            listener.onValueChanged(passwordChanged.getValue());
        }
        else
        {
            UITools.modifySClass(passwordChanged, YE_PASSWORD_NO_MATCH, true);
            UITools.modifySClass(password, YE_PASSWORD_NO_MATCH, true);
        }
    }


    protected void onPasswordChangeAttempt(final Textbox passwordChanged, final Textbox password,
                    final EditorListener<String> listener, final Label noMatchPasswordLabel)
    {
        final boolean passwordsMatch = Objects.equals(passwordChanged.getValue(), password.getValue());
        noMatchPasswordLabel.setVisible(!passwordsMatch);
        onPasswordChangeAttempt(passwordChanged, password, listener);
    }


    protected boolean isReadOnly(final Component parent)
    {
        return Optional.of(parent).filter(Editor.class::isInstance).map(Editor.class::cast).map(Editor::isReadOnly).orElse(false);
    }


    protected boolean withConfirmation(final EditorContext<String> context)
    {
        return context.getParameterAsBoolean(SETTING_WITH_CONFIRMATION, true);
    }
}
