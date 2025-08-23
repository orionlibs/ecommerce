/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.listeners;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.editor.instant.labelprovider.InstantEditorLabelProvider;
import com.hybris.cockpitng.editor.instant.labelprovider.function.LabelUpdateFunction;
import com.hybris.cockpitng.editors.EditorListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * Confirm button click listener for Instant Editor.
 */
public class InstantEditorConfirmButtonClickListener extends AbstractInstantEditorMouseEventListener
{
    /**
     * @deprecated since 6.6, not used anymore
     */
    @Deprecated(since = "6.6", forRemoval = true)
    private final InstantEditorLabelProvider labelProvider;
    /**
     * @deprecated since 6.6, not used anymore
     */
    @Deprecated(since = "6.6", forRemoval = true)
    private final LabelUpdateFunction labelUpdateFunction;
    private final EditorListener<Object> listener;
    private final Executable labelUpdate;


    /**
     * @param labelContainer
     *           label component containing string representation of the data
     * @param editorContainer
     *           container component for the underlying editor
     * @param editor
     *           instant editor's underlying editor
     * @param listener
     *           listener for events related to editor
     * @param labelUpdate
     *           executable that updates label with new value
     */
    public InstantEditorConfirmButtonClickListener(final HtmlBasedComponent labelContainer, final Div editorContainer, final Editor editor,
                    final EditorListener<Object> listener, final Executable labelUpdate)
    {
        super(labelContainer, editorContainer, editor);
        this.listener = listener;
        this.labelUpdate = labelUpdate;
        this.labelProvider = new InstantEditorLabelProvider()
        {
            // TODO Please remove with field removal


            @Override
            public boolean canHandle(final String editorType)
            {
                return false;
            }


            @Override
            public String getLabel(final String editorType, final Object value)
            {
                return StringUtils.EMPTY;
            }


            @Override
            public int getOrder()
            {
                return Ordered.LOWEST_PRECEDENCE;
            }
        };
        this.labelUpdateFunction = (label1, editor1, provider) -> {
            // TODO Please remove with field removal
        };
    }


    /**
     * @deprecated since 6.6
     * @see #InstantEditorConfirmButtonClickListener(HtmlBasedComponent, Div, Editor, EditorListener, Executable)
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public InstantEditorConfirmButtonClickListener(final Label label, final Div editorContainer, final Editor editor,
                    final EditorListener<Object> listener, final InstantEditorLabelProvider labelProvider,
                    final LabelUpdateFunction labelUpdater)
    {
        super(label, editorContainer, editor);
        this.labelProvider = labelProvider;
        this.listener = listener;
        this.labelUpdateFunction = labelUpdater;
        labelUpdate = () -> labelUpdateFunction.update(label, editor, labelProvider);
    }


    @Override
    public void onEvent(final MouseEvent event) throws Exception
    {
        getEditor().removeParameter(KEY_OLD_VALUE);
        switchToLabel();
        sendValueChangedEvent();
    }


    @Override
    protected void switchToLabel()
    {
        getLabelComponentUpdate().execute();
        super.switchToLabel();
    }


    protected void sendValueChangedEvent()
    {
        if(listener != null)
        {
            listener.onValueChanged(getEditor().getValue());
        }
    }


    /**
     * @deprecated since 6.6
     * @see com.hybris.cockpitng.editor.instant.DefaultInstantEditorLabelRenderer
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected InstantEditorLabelProvider getLabelProvider()
    {
        return labelProvider;
    }


    /**
     * @deprecated since 6.6
     * @see #getLabelComponentUpdate()
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected LabelUpdateFunction getLabelUpdater()
    {
        return labelUpdateFunction;
    }


    protected Executable getLabelComponentUpdate()
    {
        return labelUpdate;
    }
}
