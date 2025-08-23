/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.List;

/**
 * An interface that defines contract for performing the custom logic within {@link DefaultEditorAreaController}.
 */
public interface EditorAreaLogicHandler
{
    /**
     * This method is responsible for saving/persisting current object within {@link DefaultEditorAreaController}.
     *
     * @param widgetInstanceManager given widgetInstanceManager
     * @param currentObject         object that needs to be saved
     * @return saved object
     */
    Object performSave(final WidgetInstanceManager widgetInstanceManager, final Object currentObject) throws ObjectSavingException;


    /**
     * This method allows to hook in and trigger confirmation before save is executed.
     *
     * @param widgetInstanceManager widgetInstanceManager
     * @param save                  action to be executed if confirmation passes. It has to be executed in method impl.
     * @param currentObject         object that needs to be saved
     */
    default void executeSaveWithConfirmation(final WidgetInstanceManager widgetInstanceManager, final Executable save,
                    final Object currentObject
    )
    {
        save.execute();
    }


    /**
     * This method is responsible for refreshing/reloading current object within {@link DefaultEditorAreaController}.
     *
     * @param widgetInstanceManager given widgetInstanceManager
     * @param currentObject         object that needs to be refreshed/reload
     * @return refreshed/reload object
     */
    Object performRefresh(final WidgetInstanceManager widgetInstanceManager, final Object currentObject)
                    throws ObjectNotFoundException;


    /**
     * This method is responsible for validating current object within {@link DefaultEditorAreaController}.
     *
     * @param widgetInstanceManager given widgetInstanceManager
     * @param currentObject         object that needs to be validated
     * @param validationContext     validation context
     * @return refreshed/reload object
     */
    List<ValidationInfo> performValidation(final WidgetInstanceManager widgetInstanceManager, final Object currentObject,
                    final ValidationContext validationContext);


    /**
     * This method is responsible for validating current object for given qualifiers within {@link DefaultEditorAreaController}.
     *
     * @param widgetInstanceManager given widgetInstanceManager
     * @param currentObject         object that needs to be validated
     * @param qualifiers            qualifiers that needs to be validated
     * @param validationContext     validation context
     * @return refreshed/reload object
     */
    List<ValidationInfo> performValidation(final WidgetInstanceManager widgetInstanceManager, final Object currentObject,
                    final List<String> qualifiers, final ValidationContext validationContext);


    /**
     * This method is triggered before editor area content is rendered for given current object {@link DefaultEditorAreaController}.
     *
     * @param widgetInstanceManager given widgetInstanceManager
     * @param currentObject         object for which editor area is rendered
     */
    void beforeEditorAreaRender(final WidgetInstanceManager widgetInstanceManager, final Object currentObject);
}
