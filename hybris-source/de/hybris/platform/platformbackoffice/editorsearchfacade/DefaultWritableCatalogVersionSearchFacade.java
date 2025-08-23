package de.hybris.platform.platformbackoffice.editorsearchfacade;

import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorSearchFacade;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWritableCatalogVersionSearchFacade implements ReferenceEditorSearchFacade<CatalogVersionModel>
{
    private UserService userService;
    private CatalogVersionService catalogVersionService;


    public Pageable<CatalogVersionModel> search(SearchQueryData searchQueryData)
    {
        UserModel currentUser = getUserService().getCurrentUser();
        Collection<CatalogVersionModel> writableCatalogVersions = getAllWritableCatalogVersions(currentUser);
        List<CatalogVersionModel> filteredCatalogVersions = filterCatalogVersionsBySearchText(searchQueryData
                        .getSearchQueryText(), writableCatalogVersions);
        return (Pageable<CatalogVersionModel>)new PageableList(filteredCatalogVersions, searchQueryData.getPageSize());
    }


    private Collection<CatalogVersionModel> getAllWritableCatalogVersions(UserModel currentUser)
    {
        return getUserService().isAdmin(currentUser) ? getCatalogVersionService().getAllCatalogVersions() :
                        getCatalogVersionService().getAllWritableCatalogVersions((PrincipalModel)currentUser);
    }


    private List<CatalogVersionModel> filterCatalogVersionsBySearchText(String searchText, Collection<CatalogVersionModel> catalogVersions)
    {
        if(StringUtils.isNotEmpty(searchText))
        {
            return (List<CatalogVersionModel>)catalogVersions.stream()
                            .filter(catalogVersion -> (StringUtils.containsIgnoreCase(catalogVersion.getVersion(), searchText) || StringUtils.containsIgnoreCase(catalogVersion.getCatalog().getName(), searchText)))
                            .collect(Collectors.toList());
        }
        return new ArrayList<>(catalogVersions);
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
