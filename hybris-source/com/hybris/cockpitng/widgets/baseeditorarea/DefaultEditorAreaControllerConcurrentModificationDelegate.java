/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.common.messagebox.WidgetModalMessageBox;
import org.apache.commons.lang3.BooleanUtils;

/**
 * Delegate for handling concurrent modification of object edited in editor area.
 */
public class DefaultEditorAreaControllerConcurrentModificationDelegate
{
    protected static final String ID_CONCURRENT_MODIFICATION_MSG_BOX = "concurrentModificationMsgBox";
    protected static final String MODEL_CONCURRENT_MODIFICATION = "concurrentModification";
    private final DefaultEditorAreaController controller;


    public DefaultEditorAreaControllerConcurrentModificationDelegate(final DefaultEditorAreaController controller)
    {
        this.controller = controller;
    }


    /**
     * Shows concurrent modification messagebox
     */
    public void showConcurrentModificationMessagebox()
    {
        if(isConcurrentModification())
        {
            return;
        }
        showConcurrentModificationInternal();
    }


    protected void showConcurrentModificationInternal()
    {
        controller.setValue(MODEL_CONCURRENT_MODIFICATION, true);
        getMessageBoxBuilder()//
                        .withCancel(getLabel("concurrent.modification.btn.continue"),
                                        () -> controller.setValue(MODEL_CONCURRENT_MODIFICATION, false))//
                        .withConfirm(getLabel("concurrent.modification.btn.update"), this::onConcurrentModificationReload)//
                        .withHeader(getLabel("concurrent.modification.msg.header"))//
                        .withMessage(getLabel("concurrent.modification.msg.text"))//
                        .withFooter(getLabel("concurrent.modification.msg.footer"), WidgetModalMessageBox.SCLASS_WARNING)//
                        .withWarningTitle()//
                        .build()//
                        .show(controller.getWidgetslot());
    }


    /**
     * Shows concurrent modification messagebox if in model it's enabled.
     */
    public void initializeConcurrentModificationMessageboxVisibility()
    {
        if(isConcurrentModification())
        {
            showConcurrentModificationInternal();
        }
    }


    /**
     * Hides concurrent modification message box and resets flag in the model.
     */
    public void resetConcurrentModificationState()
    {
        getMessageBoxBuilder().build().closeExisting(controller.getWidgetslot());
        controller.setValue(MODEL_CONCURRENT_MODIFICATION, false);
    }


    protected boolean isConcurrentModification()
    {
        return BooleanUtils.isTrue(controller.getValue(MODEL_CONCURRENT_MODIFICATION, Boolean.class));
    }


    protected void onConcurrentModificationReload()
    {
        controller.cancelObjectModification();
        controller.setValue(MODEL_CONCURRENT_MODIFICATION, false);
    }


    protected WidgetModalMessageBox.Builder getMessageBoxBuilder()
    {
        return new WidgetModalMessageBox.Builder().withId(ID_CONCURRENT_MODIFICATION_MSG_BOX);
    }


    protected String getLabel(final String key)
    {
        return controller.getLabel(key);
    }
}
