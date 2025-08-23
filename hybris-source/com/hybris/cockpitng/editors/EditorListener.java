/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors;

/**
 * Receives updates when an {@link CockpitEditorRenderer editor}'s value has changed in some way. The general purpose is
 * to hide editor specific implementation details from the user e.g. which events are relevant etc.
 */
public interface EditorListener<T>
{
    EditorListener NULL = new EditorListener()
    {
        @Override
        public void onValueChanged(final Object value)
        {
            //NOOP
        }


        @Override
        public void onEditorEvent(final String eventCode)
        {
            //NOOP
        }


        @Override
        public void sendSocketOutput(final String outputId, final Object data)
        {
            //NOOP
        }
    };
    String ENTER_PRESSED = "enter_pressed";
    String ESCAPE_PRESSED = "escape_pressed";
    String OPEN_EXTERNAL_CLICKED = "open_external_clicked";
    String CANCEL_CLICKED = "cancel_clicked";
    String FORCE_SAVE_CLICKED = "force_save_clicked";
    String FOCUS_LOST = "focus_lost";
    String INVALID_INPUT = "invalid_input";
    String INVALID_INPUT_CLEARED = "invalid_input_cleared";


    /**
     * Called when an {@link CockpitEditorRenderer editor}'s value has changed.
     *
     * @param value
     *           the new value
     */
    void onValueChanged(T value);


    /**
     * Called when an editor event occurred (like enter pressed, escape pressed, focus lost etc.)
     *
     * @param eventCode
     *           the unique event code
     */
    void onEditorEvent(String eventCode);


    /**
     * Called to request the listener to send the given data to the output socket (with the given id) of the containing
     * widget.
     *
     * @param outputId
     *           the socket output ID to send the data to
     * @param data
     *           the data to send
     */
    void sendSocketOutput(String outputId, Object data);
}
