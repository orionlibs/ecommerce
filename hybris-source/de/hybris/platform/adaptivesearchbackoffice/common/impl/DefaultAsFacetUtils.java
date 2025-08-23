package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsFacetVisibility;
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.common.AsFacetUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractFacetConfigurationEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsFacetUtils implements AsFacetUtils
{
    private SessionService sessionService;
    private CommonI18NService commonI18NService;
    private AsSearchProviderFactory asSearchProviderFactory;


    public boolean isOpen(AsFacetData facet)
    {
        return ((CollectionUtils.isNotEmpty(facet.getValues()) || CollectionUtils.isNotEmpty(facet.getSelectedValues())) && (facet
                        .getVisibility() == AsFacetVisibility.SHOW_TOP_VALUES || facet
                        .getVisibility() == AsFacetVisibility.SHOW_VALUES));
    }


    public void localizeFacets(NavigationContextData navigationContext, SearchContextData searchContext, List<? extends AbstractFacetConfigurationEditorData> facets)
    {
        if(navigationContext == null || StringUtils.isBlank(navigationContext.getIndexType()) || searchContext == null ||
                        StringUtils.isBlank(searchContext.getLanguage()))
        {
            return;
        }
        String indexType = navigationContext.getIndexType();
        String languageCode = searchContext.getLanguage();
        this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, languageCode, facets, indexType));
    }


    protected void localizeFacet(AbstractFacetConfigurationEditorData facet, Optional<AsIndexPropertyData> indexProperty)
    {
        if(indexProperty.isPresent() && !StringUtils.isBlank(((AsIndexPropertyData)indexProperty.get()).getName()))
        {
            facet.setLabel(((AsIndexPropertyData)indexProperty.get()).getName());
        }
        else
        {
            facet.setLabel("[" + facet.getIndexProperty() + "]");
        }
    }


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
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


    public AsSearchProviderFactory getAsSearchProviderFactory()
    {
        return this.asSearchProviderFactory;
    }


    @Required
    public void setAsSearchProviderFactory(AsSearchProviderFactory asSearchProviderFactory)
    {
        this.asSearchProviderFactory = asSearchProviderFactory;
    }
}
