package de.hybris.platform.cockpit.session;

public interface CockpitListComponentExt<E> extends CockpitListComponent<E>
{
    boolean isItemsRemovable();


    boolean isItemsMovable();
}
