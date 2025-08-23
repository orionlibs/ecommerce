/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.editor.localized.LocalizedEditor;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

/**
 * Renders validation icon above editors and dedicated popup
 */
public class DefaultValidationRenderer implements ValidationRenderer
{
    protected static final String YE_EXCLAMATION_ICON = "ye-validation-exclamation-icon";
    protected static final String YE_EDITOR_VALIDATION_POPUP = "ye-editor-validation-popup";
    protected static final String YE_EDITOR_VALIDATION_POPUP_INVISIBLE = "ye-editor-validation-popup-invisible";
    protected static final String YE_EDITOR_VALIDATION_POPUP_CONTENT = "ye-editor-validation-popup-content";
    protected static final String YE_EDITOR_VALIDATION_POPUP_CNT = "ye-validation-popup-row-cnt";
    protected static final String Y_ICON = LocalizedEditor.Y_ICON;
    private static final String VALIDATION_POPUP_HEIGHT_REQUEST = "onValidationPopupHeightRequest";
    private String validationErrorKey;
    private String validationWarningKey;
    private String validationInfoKey;
    private String validationNoneKey;
    private NotificationService notificationService;


    @Override
    public Window createValidationViolationsPopup(final ValidatableContainer container, final EventListener<Event> listener)
    {
        final Listbox list = createValidationViolationsList(container);
        final DefaultValidationResultsPopup popup = new DefaultValidationResultsPopup(container, list);
        if(listener != null)
        {
            popup.addOkListener(listener);
        }
        return popup;
    }


    @Override
    public Listbox createValidationViolationsList(final ValidatableContainer container)
    {
        return new DefaultValidationResultsList(container, this, notificationService);
    }


    @Override
    public String getLabel(final Object object, final String path)
    {
        return path;
    }


    @Override
    public Component createValidationMessageBtn(final ValidationResult validations)
    {
        return createValidationMessageBtn(validations, false);
    }


    @Override
    public Component createValidationMessageBtn(final ValidationResult validations, final boolean validationExpanded)
    {
        final Div popupContainer = new Div();
        if(validations == null)
        {
            return popupContainer;
        }
        if(ValidationSeverity.NONE.isLowerThan(validations.getHighestSeverity()))
        {
            UITools.modifySClass(popupContainer, YE_EXCLAMATION_ICON, true);
            UITools.modifySClass(popupContainer, Y_ICON, true);
            final Window validationPopup = createAdjustedWindow();
            UITools.modifySClass(validationPopup, YE_EDITOR_VALIDATION_POPUP, true);
            UITools.modifySClass(validationPopup, YE_EDITOR_VALIDATION_POPUP_INVISIBLE, true);
            popupContainer.appendChild(validationPopup);
            final Vlayout vlayout = new Vlayout();
            UITools.modifySClass(vlayout, YE_EDITOR_VALIDATION_POPUP_CONTENT, true);
            validationPopup.appendChild(vlayout);
            validations.getAll().forEach(validationInfo -> {
                final Div labelContainer = new Div();
                UITools.modifySClass(labelContainer, YE_EDITOR_VALIDATION_POPUP_CNT, true);
                UITools.modifySClass(labelContainer, getSeverityStyleClass(validationInfo.getValidationSeverity()), true);
                final Label label = new Label(validationInfo.getValidationMessage());
                labelContainer.appendChild(label);
                vlayout.appendChild(labelContainer);
                UITools.modifySClass(label, getSeverityStyleClass(validationInfo.getValidationSeverity()), true);
            });
            popupContainer.addEventListener(Events.ON_CLICK, ev -> showValidationPopup(validationPopup));
            validationPopup.addEventListener(Events.ON_OPEN, new EventListener<OpenEvent>()
            {
                @Override
                public void onEvent(final OpenEvent event) throws Exception
                {
                    if(event.isOpen())
                    {
                        Clients.evalJavaScript("if($(\"#" + validationPopup.getUuid() + "\").is(\":visible\")) CockpitNG.sendEvent(\"#"
                                        + validationPopup.getUuid() + "\",\"" + VALIDATION_POPUP_HEIGHT_REQUEST + "\",$(\"#"
                                        + validationPopup.getUuid() + "\").height());");
                    }
                }
            });
            validationPopup.addEventListener(VALIDATION_POPUP_HEIGHT_REQUEST, new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event) throws Exception
                {
                    validationPopup.setTop(String.format("-%spx", event.getData()));
                    validationPopup.invalidate();
                    UITools.modifySClass(validationPopup, YE_EDITOR_VALIDATION_POPUP_INVISIBLE, false);
                }
            });
            if(validationExpanded)
            {
                showValidationPopup(validationPopup);
            }
        }
        return popupContainer;
    }


    private Window createAdjustedWindow()
    {
        final Window window = new Window();
        window.setLeft("-250px");
        window.setTop("0px");
        window.setPosition("parent");
        window.setBorder(true);
        return window;
    }


    private void showValidationPopup(final Window validationPopup)
    {
        validationPopup.doPopup();
        Events.postEvent(new OpenEvent("onOpen", validationPopup, true));
    }


    @Override
    public String getIconStyleClass(final ValidationSeverity severity)
    {
        return YE_EXCLAMATION_ICON;
    }


    @Override
    public String getSeverityStyleClass(final ValidationSeverity severity)
    {
        if(severity == null)
        {
            return StringUtils.EMPTY;
        }
        switch(severity)
        {
            case ERROR:
                return validationErrorKey;
            case WARN:
                return validationWarningKey;
            case INFO:
                return validationInfoKey;
            case NONE:
                return validationNoneKey;
            default:
                return StringUtils.EMPTY;
        }
    }


    public void cleanAllValidationCss(final HtmlBasedComponent parent)
    {
        UITools.modifySClass(parent, getSeverityStyleClass(ValidationSeverity.ERROR), false);
        UITools.modifySClass(parent, getSeverityStyleClass(ValidationSeverity.WARN), false);
        UITools.modifySClass(parent, getSeverityStyleClass(ValidationSeverity.INFO), false);
        UITools.modifySClass(parent, getSeverityStyleClass(ValidationSeverity.NONE), false);
    }


    @Required
    public void setValidationErrorKey(final String validationErrorKey)
    {
        this.validationErrorKey = validationErrorKey;
    }


    @Required
    public void setValidationWarningKey(final String validationWarningKey)
    {
        this.validationWarningKey = validationWarningKey;
    }


    @Required
    public void setValidationInfoKey(final String validationInfoKey)
    {
        this.validationInfoKey = validationInfoKey;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    @Required
    public void setValidationNoneKey(final String validationNoneKey)
    {
        this.validationNoneKey = validationNoneKey;
    }
}
