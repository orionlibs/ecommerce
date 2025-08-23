/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapordermgmtb2bfacades.cart;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.user.UserModel;

/**
 * Used to write a minimal representation of the cart into the hybris persistence, and to restore it from there
 *
 */
public interface CartRestorationFacade
{
    /**
     * Restores current users's saved cart from the hybris persistence layer
     *
     * @param guid
     *           of the saved cart
     * @param currentUser
     *           is the user which is currently logged in
     * @return the restoration data needed by the calling facade
     * @throws CommerceCartRestorationException exception.
     */
    CartRestorationData restoreSavedCart(String guid, UserModel currentUser)
                    throws CommerceCartRestorationException;


    /**
     * If existing the saved cart will be removed
     */
    void removeSavedCart();


    /**
     * By each interaction with the cart e.g add product, edit product, the persisted cart is reconstructed from the
     * backend cart.
     *
     * @param newCart
     *           to be used further on
     * @throws CommerceCartModificationException
     */
    void setSavedCart(CartData newCart) throws CommerceCartModificationException;
}