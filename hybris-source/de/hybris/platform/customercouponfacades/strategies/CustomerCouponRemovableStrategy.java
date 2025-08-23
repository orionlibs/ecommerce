package de.hybris.platform.customercouponfacades.strategies;

public interface CustomerCouponRemovableStrategy
{
    default boolean checkRemovable(String couponCode)
    {
        return true;
    }
}
