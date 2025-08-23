package de.hybris.bootstrap.codegenerator.platformwebservices;

import java.util.Collection;

@Deprecated(since = "1818", forRemoval = true)
public interface ResourceConfig
{
    DtoConfig getDTOConfig();


    String getClassName();


    String getSimpleClassName();


    String getPackageName();


    boolean isPostSupport();


    boolean isGetSupport();


    boolean isPutSupport();


    boolean isDeleteSupport();


    Collection<ResourceConfig> getSubResources();
}
