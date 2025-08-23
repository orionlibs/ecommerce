package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQueryLanguageResolver;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSearchQueryLanguageResolver implements SearchQueryLanguageResolver
{
    private CommonI18NService commonI18NService;
    private UserService userService;


    public LanguageModel resolveLanguage(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        UserModel user = this.userService.getCurrentUser();
        if(user.getSessionLanguage() == null)
        {
            return this.commonI18NService.getCurrentLanguage();
        }
        return user.getSessionLanguage();
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
