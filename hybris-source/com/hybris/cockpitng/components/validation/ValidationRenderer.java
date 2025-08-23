/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

/**
 * Returns css classes for validation results and render validation popup for editor
 */
public interface ValidationRenderer
{
    /**
     * Creates a list, that displays all violations from provided validation result
     *
     * @param container
     *           container that will be validated
     * @return fully prepared list
     * @see #createValidationViolationsPopup(ValidatableContainer, EventListener)
     */
    Listbox createValidationViolationsList(ValidatableContainer container);


    /**
     * Creates a popup, that displays all violations from provided validation result.
     * <P>
     * Popup also implements possibility of closing, confirming warnings, etc.
     *
     * @param container
     *           container that will be validated
     * @param listener
     *           listener for confirmation - listener is notified after user clicks <code>close</code>/
     *           <code>confirm</code> button and a window is hidden
     * @return fully prepared popup
     * @see #createValidationViolationsList(ValidatableContainer)
     * @see org.zkoss.zk.ui.event.Events#ON_OK
     */
    Window createValidationViolationsPopup(ValidatableContainer container, EventListener<Event> listener);


    /**
     * Creates an icon indicating violation severity level. It may also i.e. attach onClick event to icon, to show
     * details of violation in popup.
     *
     * @param validations
     *           violations to be represented by button
     * @param validationExpanded
     *           determines whether validation info popup is displayed on init
     * @return button
     */
    Component createValidationMessageBtn(ValidationResult validations, boolean validationExpanded);


    /**
     * Creates an icon indicating violation severity level. It may also i.e. attach <code>onClick</code> event to icon,
     * to show details of violation in popup.
     *
     * @param validations
     *           violations to be represented by button
     * @return button
     */
    Component createValidationMessageBtn(ValidationResult validations);


    /**
     * Gets a label for violated path of specified object
     *
     * @param object
     *           object being validated
     * @param path
     *           violated path
     * @return user-friendly presentation of path
     */
    String getLabel(Object object, String path);


    /**
     * Gets a css class of icon representing provided severity level
     *
     * @param severity
     *           severity level to be represented
     * @return css class for icon
     */
    String getIconStyleClass(ValidationSeverity severity);


    /**
     * Gets a css class for components to be marked with provided severity level
     *
     * @param severity
     *           severity level to be represented
     * @return css class for component
     */
    String getSeverityStyleClass(ValidationSeverity severity);


    /**
     * Removes all css classes from specified component that are related to validation violation
     *
     * @param container
     *           component to be cleared
     */
    void cleanAllValidationCss(HtmlBasedComponent container);
}
