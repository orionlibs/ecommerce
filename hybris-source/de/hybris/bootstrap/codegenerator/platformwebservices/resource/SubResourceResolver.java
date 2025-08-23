package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YType;
import java.util.Map;

@Deprecated(since = "1818", forRemoval = true)
public interface SubResourceResolver
{
    Map<? extends YType, String> getAllSubResources(YComposedType paramYComposedType, WebservicesConfig paramWebservicesConfig);
}
