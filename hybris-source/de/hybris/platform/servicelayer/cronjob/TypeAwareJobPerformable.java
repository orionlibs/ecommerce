package de.hybris.platform.servicelayer.cronjob;

public interface TypeAwareJobPerformable
{
    String getType();


    default boolean createDefaultJob()
    {
        return true;
    }
}
