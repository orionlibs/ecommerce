package de.hybris.platform.webservicescommons.swagger.strategies;

import java.util.List;
import springfox.documentation.service.VendorExtension;

public interface ApiVendorExtensionStrategy
{
    List<VendorExtension> getVendorExtensions(String paramString);
}
