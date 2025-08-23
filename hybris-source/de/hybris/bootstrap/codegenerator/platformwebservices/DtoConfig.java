package de.hybris.bootstrap.codegenerator.platformwebservices;

import de.hybris.bootstrap.typesystem.YComposedType;

@Deprecated(since = "1818", forRemoval = true)
public interface DtoConfig
{
    YComposedType getType();


    String getDtoClassName();


    String getDtoClassSimpleName();


    String getModelClassName();


    String getModelClassSimpleName();


    String getSingular();


    String getPlural();


    String getDtoPackage();
}
