/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.services;

/**
 * Allows to retrieve information about user perspectives.
 */
public interface PerspectiveInfoService
{
    /**
     * Tests whether service stores identifier of the perspective selected by the user.
     *
     * @return {@code true} whether service stores identifier, otherwise {@code false}.
     */
    boolean hasSelectedId();


    /**
     * Returns identifier of the perspective selected by the user or {@code null} if service does not store that
     * information.
     *
     * @return selected perspective identifier or {@code null}.
     * @see #hasSelectedId()
     */
    String getSelectedId();


    /**
     * Sets identifier of the perspective selected by the user. This method does not change state for view or model.
     *
     * @param id the chosen perspective id.
     */
    void setSelectedId(String id);
}
