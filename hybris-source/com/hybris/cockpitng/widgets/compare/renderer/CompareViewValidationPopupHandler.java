/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.impl.DefaultValidationContext;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

public class CompareViewValidationPopupHandler
{
    private static final String SCLASS_VALIDATION_POPUP = "ye-validation-results-popup";
    private static final String SCLASS_POPUP = "y-popup";
    private static final String SCLASS_SECONDARY_BUTTON = "y-btn-secondary";
    private static final String SCLASS_RESULTS_LIST = "ye-validation-results-list";
    private static final String LABEL_VALIDATION_POPUP_BUTTON_CLOSE = "validation.popup.button.close";
    private static final String LABEL_VALIDATION_POPUP_BUTTON_CONFIRM = "validation.popup.button.confirm";
    private static final String LABEL_VALIDATION_POPUP_TITLE = "validation.popup.title";
    private static final String POPUP_POSITION = "parent";
    private static final String SCRIPT_ADJUST_VALIDATION_POPUP_POSITION = "PopupUtils.adjustCompareViewValidationPopupPosition('%s', '%s');";
    private final ValidationHandler validationHandler;
    private final LabelService labelService;
    private final TypeFacade typeFacade;
    private final CockpitLocaleService cockpitLocaleService;
    private Window validationResultsWindow = new Window();
    private Listbox validationListbox = new Listbox();
    private Button closeButton = new Button();
    private Vlayout layout = new Vlayout();
    private Component container;


    public CompareViewValidationPopupHandler(final ValidationHandler validationHandler, final LabelService labelService,
                    final TypeFacade typeFacade, final CockpitLocaleService cockpitLocaleService)
    {
        this.validationHandler = validationHandler;
        this.labelService = labelService;
        this.typeFacade = typeFacade;
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public void prepareValidationPopup(final Component container)
    {
        setContainer(container);
        UITools.modifySClass(validationResultsWindow, SCLASS_VALIDATION_POPUP, true);
        UITools.modifySClass(validationResultsWindow, SCLASS_POPUP, true);
        UITools.modifySClass(closeButton, SCLASS_SECONDARY_BUTTON, true);
        UITools.modifySClass(validationListbox, SCLASS_RESULTS_LIST, true);
        validationListbox.setParent(validationResultsWindow);
        layout.setSpacing("auto");
        layout.appendChild(validationListbox);
        layout.appendChild(closeButton);
        layout.setParent(validationResultsWindow);
        validationResultsWindow.setVisible(false);
        validationResultsWindow.setClosable(true);
        validationResultsWindow.setPosition(POPUP_POSITION);
        container.appendChild(validationResultsWindow);
    }


    public boolean doValidate(final List<Object> validationObjects, Consumer<List<Object>> confirmActionConsumer)
    {
        container.appendChild(validationResultsWindow);
        final List<Object> validationPopupModel = new ArrayList<>();
        var validationInfosCount = 0;
        String highestNotConfirmedSeverity = null;
        for(Object validationObject : validationObjects)
        {
            final List<ValidationInfo> validationInfos = validationHandler.validate(validationObject,
                            new DefaultValidationContext());
            if(!validationInfos.isEmpty())
            {
                validationPopupModel.add(validationObject);
                final List<ValidationInfo> errorResults = validationInfos.stream()
                                .filter(i -> ValidationSeverity.ERROR.equals(i.getValidationSeverity())).collect(Collectors.toList());
                final List<ValidationInfo> warnResults = validationInfos.stream()
                                .filter(i -> ValidationSeverity.WARN.equals(i.getValidationSeverity())).collect(Collectors.toList());
                final List<ValidationInfo> infoResults = validationInfos.stream()
                                .filter(i -> ValidationSeverity.INFO.equals(i.getValidationSeverity())).collect(Collectors.toList());
                highestNotConfirmedSeverity = infoResults.isEmpty() ? highestNotConfirmedSeverity : ValidationSeverity.INFO.getCode();
                highestNotConfirmedSeverity = warnResults.isEmpty() ? highestNotConfirmedSeverity : ValidationSeverity.WARN.getCode();
                highestNotConfirmedSeverity = errorResults.isEmpty() ? highestNotConfirmedSeverity
                                : ValidationSeverity.ERROR.getCode();
                validationPopupModel.addAll(errorResults);
                validationPopupModel.addAll(warnResults);
                validationPopupModel.addAll(infoResults);
                validationInfosCount += validationInfos.size();
            }
        }
        if(validationPopupModel.isEmpty() || highestNotConfirmedSeverity == null)
        {
            return true;
        }
        addCloseButtonEventListener(highestNotConfirmedSeverity, confirmActionConsumer, validationObjects);
        validationListbox.setModel(new ListModelList<>(validationPopupModel));
        validationListbox
                        .setItemRenderer(new CompareViewValidationResultsListitemRenderer(labelService, typeFacade, cockpitLocaleService));
        UITools.modifySClass(validationListbox, SCLASS_RESULTS_LIST, true);
        validationResultsWindow.setVisible(true);
        validationResultsWindow.setTitle(Labels.getLabel(LABEL_VALIDATION_POPUP_TITLE, new Object[]
                        {validationInfosCount}));
        validationResultsWindow.doOverlapped();
        Clients.evalJavaScript(String.format(SCRIPT_ADJUST_VALIDATION_POPUP_POSITION, validationResultsWindow.getUuid(),
                        validationResultsWindow.getParent().getUuid()));
        return highestNotConfirmedSeverity.equals(ValidationSeverity.INFO.getCode());
    }


    private void addCloseButtonEventListener(final String highestNotConfirmedSeverity,
                    final Consumer<List<Object>> confirmActionConsumer, final List<Object> validationObjects)
    {
        removeComponentEventListener(closeButton, Events.ON_CLICK);
        String closeButtonLabel;
        if(highestNotConfirmedSeverity.equals(ValidationSeverity.WARN.getCode()))
        {
            closeButtonLabel = Labels.getLabel(LABEL_VALIDATION_POPUP_BUTTON_CONFIRM);
            closeButton.addEventListener(Events.ON_CLICK, e -> {
                confirmActionConsumer.accept(validationObjects);
                validationResultsWindow.setVisible(false);
                removeComponentEventListener(closeButton, Events.ON_CLICK);
            });
        }
        else
        {
            closeButtonLabel = Labels.getLabel(LABEL_VALIDATION_POPUP_BUTTON_CLOSE);
            closeButton.addEventListener(Events.ON_CLICK, e -> {
                validationResultsWindow.setVisible(false);
                removeComponentEventListener(closeButton, Events.ON_CLICK);
            });
        }
        closeButton.setLabel(closeButtonLabel);
    }


    private void removeComponentEventListener(final Component component, final String eventName)
    {
        component.getEventListeners(eventName).forEach(eventListener -> component.removeEventListener(eventName, eventListener));
    }


    public Window getValidationResultsWindow()
    {
        return this.validationResultsWindow;
    }


    public void setValidationResultsWindow(Window validationResultsWindow)
    {
        this.validationResultsWindow = validationResultsWindow;
    }


    public void setValidationListbox(Listbox validationListbox)
    {
        this.validationListbox = validationListbox;
    }


    public Button getCloseButton()
    {
        return this.closeButton;
    }


    public void setCloseButton(Button closeButton)
    {
        this.closeButton = closeButton;
    }


    public void setLayout(Vlayout layout)
    {
        this.layout = layout;
    }


    public void setContainer(Component container)
    {
        this.container = container;
    }
}
