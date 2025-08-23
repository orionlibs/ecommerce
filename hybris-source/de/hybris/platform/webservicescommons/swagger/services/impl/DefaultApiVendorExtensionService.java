package de.hybris.platform.webservicescommons.swagger.services.impl;

import de.hybris.platform.webservicescommons.swagger.services.ApiVendorExtensionService;
import de.hybris.platform.webservicescommons.swagger.strategies.ApiVendorExtensionStrategy;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;
import springfox.documentation.service.VendorExtension;

public class DefaultApiVendorExtensionService implements ApiVendorExtensionService
{
    private List<ApiVendorExtensionStrategy> vendorExtensionStrategyList;


    public List<VendorExtension> getAllVendorExtensions(String configPrefix)
    {
        return (List<VendorExtension>)getVendorExtensionStrategyList().stream().flatMap(strategy -> strategy.getVendorExtensions(configPrefix).stream())
                        .collect(Collectors.toList());
    }


    protected List<ApiVendorExtensionStrategy> getVendorExtensionStrategyList()
    {
        return this.vendorExtensionStrategyList;
    }


    @Required
    public void setVendorExtensionStrategyList(List<ApiVendorExtensionStrategy> vendorExtensionStrategyList)
    {
        this.vendorExtensionStrategyList = vendorExtensionStrategyList;
    }
}
