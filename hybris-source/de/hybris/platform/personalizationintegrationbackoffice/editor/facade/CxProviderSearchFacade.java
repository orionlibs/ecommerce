package de.hybris.platform.personalizationintegrationbackoffice.editor.facade;

import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorSearchFacade;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.personalizationintegration.segment.CxProvider;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

public class CxProviderSearchFacade implements ReferenceEditorSearchFacade<String>
{
    private List<String> providers = Collections.emptyList();


    public Pageable<String> search(SearchQueryData searchQueryData)
    {
        return (Pageable<String>)new PageableList(this.providers, searchQueryData.getPageSize());
    }


    @Autowired(required = false)
    public void setProviders(List<CxProvider> providers)
    {
        this.providers = (List<String>)providers.stream().map(CxProvider::getProviderId).distinct().collect(Collectors.toList());
    }
}
