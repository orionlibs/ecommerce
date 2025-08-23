package de.hybris.platform.retention.hook;

public interface ItemCleanupHook<MODEL extends de.hybris.platform.core.model.ItemModel>
{
    void cleanupRelatedObjects(MODEL paramMODEL);
}
