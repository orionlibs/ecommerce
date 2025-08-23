/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import org.zkoss.zk.ui.Component;

/**
 * Interface of object capable of transferring focus from one editor to other. It is used to select editor for specified
 * path.
 */
public interface ValidationFocusTransferHandler
{
    /**
     * Event indicating that focus transfer has been requested due to validation
     */
    String ON_FOCUS_TRANSFER_REQUESTED = "onFocusTransferRequested";
    /**
     * Event indicating that focus transfer has been made due to validation
     */
    String ON_FOCUS_TRANSFERRED = "onFocusTransferred";
    /**
     * Focus has bean successfully transferred
     */
    int TRANSFER_SUCCESS = 0;
    /**
     * Focus could not be transferred, as no field for provided path could be found
     */
    int TRANSFER_ERROR_UNKNOWN_PATH = 1;
    /**
     * Some other error occurred during focus transfer
     */
    int TRANSFER_ERROR_OTHER = 2;


    /**
     * Tries to transfer focus to field bound to specified path
     *
     * @param parent
     *           parent component of fields
     * @param path
     *           path to be focused
     * @return result of transfer
     * @see #TRANSFER_SUCCESS
     * @see #TRANSFER_ERROR_UNKNOWN_PATH
     * @see #TRANSFER_ERROR_OTHER
     */
    int focusValidationPath(final Component parent, final String path);
}
