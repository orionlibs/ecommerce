/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

/**
 * Interface highlights that object can contains some desktop sensitive data. {@link org.zkoss.zk.ui.Desktop}
 *
 */
public interface DesktopAware
{
    /**
     * Method that is fired when desktop is changed i.e. this method is called only when the
     * desktop is changed on the client side.
     * <p>
     * Note:
     *
     * This method isn't invoked by framework i.e. it has to be called within custom code.
     * </p>
     */
    void afterDesktopChanged();
}
