package com.hybris.backoffice.solrsearch.populators;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeFieldNamePostProcessor implements FieldNamePostProcessor
{
    protected static final String FROM_REGEXP_TEMPLATE = "\\%s$";
    protected static final String LANGUAGE_PREFIX = "_";
    protected static final String REPLACABLE_LOCALE_REGEXP_TEMPLATE = "([^:_]*):";
    protected static final String QUERY_VALUE_SEPARATOR = ":";
    private I18NService i18nService;
    private CommonI18NService commonI18NService;


    public String process(SearchQuery searchQuery, Locale conditionLocale, String fieldName)
    {
        if(conditionLocale != null)
        {
            LanguageModel conditionLanguage = retrieveLanguageModel(conditionLocale);
            if(conditionLanguage != null)
            {
                String searchQueryIsocode = searchQuery.getLanguage();
                String conditionIsocode = conditionLanguage.getIsocode().toLowerCase(Locale.ENGLISH);
                String replacedValue = "_".concat(searchQueryIsocode);
                if(isProcessableFieldWithoutValue(conditionIsocode, searchQueryIsocode, fieldName, replacedValue))
                {
                    String to = "_".concat(conditionIsocode);
                    return fieldName.replaceFirst(String.format("\\%s$", new Object[] {replacedValue}), to);
                }
                if(isProcessableFieldWithValue(conditionIsocode, searchQueryIsocode, fieldName))
                {
                    String replacement = conditionLanguage.getIsocode().concat(":");
                    return fieldName.replaceFirst("([^:_]*):", replacement);
                }
            }
        }
        return fieldName;
    }


    private boolean isProcessableFieldWithoutValue(String conditionIsocode, String searchQueryIsocode, String fieldName, String replacedValue)
    {
        return (!StringUtils.equalsIgnoreCase(conditionIsocode, searchQueryIsocode) && fieldName
                        .endsWith(replacedValue));
    }


    private boolean isProcessableFieldWithValue(String conditionIsocode, String searchQueryIsocode, String fieldName)
    {
        String from = "_".concat(searchQueryIsocode);
        return (!StringUtils.equalsIgnoreCase(conditionIsocode, searchQueryIsocode) &&
                        !fieldName.endsWith(from));
    }


    protected LanguageModel retrieveLanguageModel(Locale locale)
    {
        try
        {
            return this.commonI18NService.getLanguage(this.i18nService.getBestMatchingLocale(locale).toString());
        }
        catch(UnknownIdentifierException e)
        {
            return this.commonI18NService.getLanguage(locale.toString());
        }
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }
}
