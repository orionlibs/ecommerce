package de.hybris.platform.directpersistence.selfhealing;

public interface SelfHealingService
{
    void addItemToHeal(ItemToHeal paramItemToHeal);


    boolean isEnabled();
}
