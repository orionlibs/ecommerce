/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.validation;

import com.hybris.cockpitng.components.validation.DefaultValidationResultsPopup;
import com.hybris.cockpitng.components.validation.PopupCoordinates;
import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowDefinitions;
import java.util.Map;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;

public class ConfigurableFlowValidationResultsPopup extends DefaultValidationResultsPopup
{
    protected static final String SCLASS_WIZARD_VALIDATION_POPUP = "ye-wizard-validation-popup";
    protected static final String POPUP_LEFT_POSITION = "-140px";
    private String currentCtrlAction;
    private String currentActionLabel;
    private int positionedToItemCount;


    /**
     * Creates wizard validation popup.
     *
     * @param container
     *           validation container.
     * @param resultsList
     *           list of results.
     * @param popupCtrlActionConsumer
     *           consumer of an action pressed in the popup.
     */
    public ConfigurableFlowValidationResultsPopup(final ValidatableContainer container, final Listbox resultsList,
                    final BiConsumer<String, ValidationResult> popupCtrlActionConsumer)
    {
        super(container, resultsList);
        ConfigurableFlowValidationResultsPopup.this.initOkListener(popupCtrlActionConsumer);
        UITools.modifySClass(this, SCLASS_WIZARD_VALIDATION_POPUP, true);
        ConfigurableFlowValidationResultsPopup.this.setVerticalAlignment(VerticalAlignment.TOP);
        ConfigurableFlowValidationResultsPopup.this.setHorizontalAlignment(HorizontalAlignment.RIGHT);
    }


    /**
     * Updates popup anchor with given button's parent. It enables navigation control from a popup: Ignore and next etc.
     *
     * @param button
     *           anchor component.
     * @param action
     *           id of an action which will be executed. It supports {@link ConfigurableFlowDefinitions#WIZARD_NEXT},
     *           {@link ConfigurableFlowDefinitions#WIZARD_DONE}, {@link ConfigurableFlowDefinitions#WIZARD_CUSTOM}
     */
    public void updateValidationPopupAnchor(final Button button, final String action)
    {
        if(isCtrlAction(action) && button.getParent() != null)
        {
            if(ObjectUtils.notEqual(currentCtrlAction, action))
            {
                setVisible(false);
            }
            updateValidationPopupAnchor(button.getParent());
            currentCtrlAction = action;
            currentActionLabel = button.getLabel();
        }
    }


    /**
     * Updates popup anchor with a button's parent. The method checks if maps has components for following keys in given
     * order: {@link ConfigurableFlowDefinitions#WIZARD_NEXT},{@link ConfigurableFlowDefinitions#WIZARD_DONE},
     * {@link ConfigurableFlowDefinitions#WIZARD_CUSTOM}
     * <p>
     * if component is found it is used as an anchor.
     *
     * @param validationPopupAnchors
     *           map of possible anchors.
     */
    public void updateValidationPopupAnchor(final Map<String, Button> validationPopupAnchors)
    {
        String actionId = null;
        if(currentCtrlAction != null && validationPopupAnchors.containsKey(currentCtrlAction))
        {
            actionId = currentCtrlAction;
        }
        else if(validationPopupAnchors.containsKey(ConfigurableFlowDefinitions.WIZARD_NEXT))
        {
            actionId = ConfigurableFlowDefinitions.WIZARD_NEXT;
        }
        else if(validationPopupAnchors.containsKey(ConfigurableFlowDefinitions.WIZARD_DONE))
        {
            actionId = ConfigurableFlowDefinitions.WIZARD_DONE;
        }
        else if(validationPopupAnchors.containsKey(ConfigurableFlowDefinitions.WIZARD_CUSTOM))
        {
            actionId = ConfigurableFlowDefinitions.WIZARD_CUSTOM;
        }
        else if(validationPopupAnchors.containsKey(ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP_PERSIST))
        {
            actionId = ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP_PERSIST;
        }
        if(actionId != null)
        {
            updateValidationPopupAnchor(validationPopupAnchors.get(actionId), actionId);
        }
    }


    /**
     * Updates popup anchor with given component. Since there is no action defined navigation ctrl from popup will not be
     * active (no Ignore and next etc. just close)
     */
    public void updateValidationPopupAnchor(final Component component)
    {
        setParent(component);
        currentCtrlAction = null;
        currentActionLabel = null;
    }


    /**
     * Recalculates popup's window position on the screen.
     */
    public void recalculatePosition()
    {
        if(isVisible())
        {
            setVisible(false);
            showPopup();
        }
    }


    @Override
    protected void handleCoordinates(final PopupCoordinates coordinates)
    {
        coordinates.getHorizontal().setTransition(coordinates.getHorizontal().getTransition());
        super.handleCoordinates(coordinates);
    }


    protected void initOkListener(final BiConsumer<String, ValidationResult> popupCtrlActionConsumer)
    {
        addOkListener(event -> {
            if(isCtrlAction(currentCtrlAction) && getContainer() != null && popupCtrlActionConsumer != null)
            {
                popupCtrlActionConsumer.accept(currentCtrlAction, getContainer().getCurrentValidationResult());
            }
        });
    }


    protected boolean isCtrlAction(final String actionId)
    {
        return ConfigurableFlowDefinitions.WIZARD_NEXT.equals(actionId) || ConfigurableFlowDefinitions.WIZARD_DONE.equals(actionId)
                        || ConfigurableFlowDefinitions.WIZARD_CUSTOM.equals(actionId)
                        || ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP_PERSIST.equals(actionId);
    }


    @Override
    protected String getConfirmationLabel()
    {
        if(StringUtils.isNotBlank(currentActionLabel))
        {
            return Labels.getLabel("validation.popup.button.confirm.and", new Object[]
                            {currentActionLabel});
        }
        return Labels.getLabel("validation.popup.button.close");
    }


    @Override
    protected void refreshPosition()
    {
        if(positionedToItemCount != getResults().getItemCount())
        {
            sendHeightRequestEvent();
        }
    }
}
