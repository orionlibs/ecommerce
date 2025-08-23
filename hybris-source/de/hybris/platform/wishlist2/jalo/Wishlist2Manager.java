package de.hybris.platform.wishlist2.jalo;

import de.hybris.platform.core.Registry;

public class Wishlist2Manager extends GeneratedWishlist2Manager
{
    public static Wishlist2Manager getInstance()
    {
        return (Wishlist2Manager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("wishlist");
    }
}
