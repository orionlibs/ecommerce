package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class LanguageQualifierProvider implements QualifierProvider
{
    private CommonI18NService commonI18NService;
    private final Set<Class<?>> supportedTypes;


    public LanguageQualifierProvider()
    {
        Set<Class<?>> types = new HashSet<>();
        types.add(LanguageModel.class);
        types.add(Locale.class);
        this.supportedTypes = Collections.unmodifiableSet(types);
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


    public Set<Class<?>> getSupportedTypes()
    {
        return this.supportedTypes;
    }


    public Collection<Qualifier> getAvailableQualifiers(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        Objects.requireNonNull(facetSearchConfig, "facetSearchConfig is null");
        Objects.requireNonNull(indexedType, "indexedType is null");
        List<Qualifier> qualifiers = new ArrayList<>();
        for(LanguageModel language : facetSearchConfig.getIndexConfig().getLanguages())
        {
            Locale locale = this.commonI18NService.getLocaleForLanguage(language);
            LanguageQualifier languageQualifier = new LanguageQualifier(language, locale);
            qualifiers.add(languageQualifier);
        }
        return Collections.unmodifiableList(qualifiers);
    }


    public boolean canApply(IndexedProperty indexedProperty)
    {
        Objects.requireNonNull(indexedProperty, "indexedProperty is null");
        return indexedProperty.isLocalized();
    }


    public void applyQualifier(Qualifier qualifier)
    {
        Objects.requireNonNull(qualifier, "qualifier is null");
        if(!(qualifier instanceof LanguageQualifier))
        {
            throw new IllegalArgumentException("provided qualifier is not of expected type");
        }
        this.commonI18NService.setCurrentLanguage(((LanguageQualifier)qualifier).getLanguage());
    }


    public Qualifier getCurrentQualifier()
    {
        LanguageModel language = this.commonI18NService.getCurrentLanguage();
        if(language == null)
        {
            return null;
        }
        Locale locale = this.commonI18NService.getLocaleForLanguage(language);
        return (Qualifier)new LanguageQualifier(language, locale);
    }
}
