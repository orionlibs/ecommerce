package de.hybris.platform.media.services;

import de.hybris.platform.util.Config;

public interface MediaStorageInitializer
{
    void onInitialize();


    void onUpdate();


    default boolean failOnInitUpdateError()
    {
        return Config.getBoolean("media.default.storage.strategy.failoninitupdateerror", false);
    }


    default void checkStorageConnection()
    {
    }
}
