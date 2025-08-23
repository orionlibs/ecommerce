package de.hybris.platform.admincockpit.services;

public interface ResourceConfiguration<Key, Resource>
{
    Key getKey();


    Resource getResource();
}
