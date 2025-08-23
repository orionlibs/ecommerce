package de.hybris.platform.webservicescommons.swagger.services;

import java.util.List;
import springfox.documentation.service.VendorExtension;

public interface ApiVendorExtensionService
{
    List<VendorExtension> getAllVendorExtensions(String paramString);
}
