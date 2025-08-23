package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSortField;
import de.hybris.platform.solrfacetsearch.model.SolrSortFieldModel;
import de.hybris.platform.solrfacetsearch.model.SolrSortModel;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexedTypeSortPopulator implements Populator<SolrSortModel, IndexedTypeSort>
{
    private I18NService i18NService;
    private Converter<SolrSortFieldModel, IndexedTypeSortField> indexedTypeSortFieldConverter;


    public void populate(SolrSortModel source, IndexedTypeSort target)
    {
        target.setSort(source);
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setLocalizedName(buildNameLocalizationMap(source));
        target.setApplyPromotedItems(source.isUseBoost());
        target.setFields(this.indexedTypeSortFieldConverter.convertAll(source.getFields()));
    }


    protected Map<String, String> buildNameLocalizationMap(SolrSortModel source)
    {
        Set<Locale> supportedLocales = this.i18NService.getSupportedLocales();
        return (Map<String, String>)supportedLocales.stream().filter(locale -> StringUtils.isNotBlank(source.getName(locale)))
                        .collect(Collectors.toMap(Locale::toString, locale -> source.getName(locale)));
    }


    public Converter<SolrSortFieldModel, IndexedTypeSortField> getIndexedTypeSortFieldConverter()
    {
        return this.indexedTypeSortFieldConverter;
    }


    @Required
    public void setIndexedTypeSortFieldConverter(Converter<SolrSortFieldModel, IndexedTypeSortField> indexedTypeSortFieldConverter)
    {
        this.indexedTypeSortFieldConverter = indexedTypeSortFieldConverter;
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
