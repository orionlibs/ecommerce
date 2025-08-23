package de.hybris.platform.personalizationintegrationbackoffice.editor.facade;

import de.hybris.platform.personalizationintegration.segment.CxProvider;
import de.hybris.platform.personalizationintegration.segment.UserSegmentsProvider;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSegmentsProviderSearchFacade
{
    private List<String> providers = Collections.emptyList();


    public List<String> getProviders()
    {
        return this.providers;
    }


    @Autowired(required = false)
    public void setProviders(List<UserSegmentsProvider> providers)
    {
        this.providers = (List<String>)providers.stream().map(CxProvider::getProviderId).distinct().collect(Collectors.toList());
    }
}
