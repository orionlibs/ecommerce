/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.type;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeManagerManagedModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

/**
 * Provides localization for Types & Attributes.<br><br>
 *
 * Loads localization for all {@link TypeModel}
 * on first {@link #localizeType(TypeModel, DataType.Builder)} call. All {@link AttributeDescriptorModel} localizations
 * are loaded on first {@link #localizeAttribute(AttributeDescriptorModel, DataAttribute.Builder)}<br><br>
 * Localizations are loaded for all UI locales {@link CockpitLocaleService#getAllUILocales()}.
 * <br><br>
 *
 */
public class DefaultTypeSystemLocalizationHelper implements TypeSystemLocalizationHelper
{
    private static final String ATTRIBUTE_LOCALIZATION_QUERY = "SELECT  {t.code}, {a.qualifier}, {l.isocode}, {a.name[any]}, "
                    + "  {a.description[any]} "
                    + "	FROM {  AttributeDescriptor AS a "
                    + "  JOIN ComposedType AS t ON {a.enclosingType} = {t.PK} JOIN Language as l ON lp_t0.langpk = {l.pk} } "
                    + " WHERE {l.isocode} in (?langs)";
    private static final String TYPE_LOCALIZATION_QUERY = "SELECT {t.code}, {l.isocode}, {t.name[any]} "
                    + " FROM {  ComposedType AS t JOIN Language as l ON lp_t0.langpk = {l.pk} } "
                    + " WHERE {l.isocode} in (?langs)";
    private static final int NUMBER_OF_ATTRIBUTES_FETCHED = 2;
    private FlexibleSearchService flexibleSearchService;
    private CockpitLocaleService cockpitLocaleService;
    private Table<String, String, Table<String, Locale, String>> attributeLocalizationTable;
    private Table<String, Locale, String> typeLocalizationTable;


    @Override
    public void localizeAttribute(final AttributeDescriptorModel attributeDescriptorModel, final DataAttribute.Builder attrBuilder)
    {
        final Map<Locale, String> names = findAttributeLocalization(attributeDescriptorModel, TypeManagerManagedModel.NAME);
        attrBuilder.labels(names);
        final Map<Locale, String> descriptions = findAttributeLocalization(attributeDescriptorModel,
                        AttributeDescriptorModel.DESCRIPTION);
        attrBuilder.descriptions(descriptions);
    }


    @Override
    public void localizeType(final TypeModel platformType, final DataType.Builder typeBuilder)
    {
        final Map<Locale, String> typeLocalization = findTypeLocalization(platformType);
        typeBuilder.labels(typeLocalization);
    }


    protected Map<Locale, String> findAttributeLocalization(final AttributeDescriptorModel attributeDescriptor, final String attributeName)
    {
        synchronized(this)
        {
            if(attributeLocalizationTable == null)
            {
                attributeLocalizationTable = loadAttributeLocalization();
            }
        }
        if(attributeLocalizationTable.contains(attributeDescriptor.getEnclosingType().getCode(),
                        attributeDescriptor.getQualifier()))
        {
            return attributeLocalizationTable
                            .get(attributeDescriptor.getEnclosingType().getCode(), attributeDescriptor.getQualifier()).row(attributeName);
        }
        else
        {
            return Collections.emptyMap();
        }
    }


    protected Map<Locale, String> findTypeLocalization(final TypeModel type)
    {
        synchronized(this)
        {
            if(typeLocalizationTable == null)
            {
                typeLocalizationTable = loadTypeLocalization();
            }
        }
        if(typeLocalizationTable.containsRow(type.getCode()))
        {
            return typeLocalizationTable.row(type.getCode());
        }
        else
        {
            return Collections.emptyMap();
        }
    }


    protected Table<String, String, Table<String, Locale, String>> loadAttributeLocalization()
    {
        final Map<String, Locale> localeMap = loadLocalesMap();
        final FlexibleSearchQuery fsq = new FlexibleSearchQuery(ATTRIBUTE_LOCALIZATION_QUERY, Map.of("langs", localeMap.keySet()));
        fsq.setResultClassList(List.of(String.class, String.class, String.class, String.class, String.class));
        fsq.setDisableCaching(true);
        final SearchResult<List<String>> searchResult = getFlexibleSearchService().search(fsq);
        final Table<String, String, Table<String, Locale, String>> attributeLocalisation = HashBasedTable.create();
        for(final List<String> row : searchResult.getResult())
        {
            final String isocode = row.get(2);
            if(!localeMap.containsKey(isocode))
            {
                continue;
            }
            final String type = row.get(0);
            final String qualifier = row.get(1);
            if(!attributeLocalisation.contains(type, qualifier))
            {
                attributeLocalisation.put(type, qualifier, HashBasedTable.create(NUMBER_OF_ATTRIBUTES_FETCHED, localeMap.size()));
            }
            final Locale locale = localeMap.get(isocode);
            final String localizedName = row.get(3);
            if(localizedName != null)
            {
                attributeLocalisation.get(type, qualifier).put(TypeManagerManagedModel.NAME, locale, localizedName);
            }
            final String localizedDescription = row.get(4);
            if(localizedDescription != null)
            {
                attributeLocalisation.get(type, qualifier).put(AttributeDescriptorModel.DESCRIPTION, locale, localizedDescription);
            }
        }
        return attributeLocalisation;
    }


    protected Table<String, Locale, String> loadTypeLocalization()
    {
        final Map<String, Locale> localeMap = loadLocalesMap();
        final FlexibleSearchQuery fsq = new FlexibleSearchQuery(TYPE_LOCALIZATION_QUERY, Map.of("langs", localeMap.keySet()));
        fsq.setResultClassList(List.of(String.class, String.class, String.class));
        fsq.setDisableCaching(true);
        final SearchResult<List<String>> searchResult = getFlexibleSearchService().search(fsq);
        final Table<String, Locale, String> typeLocalization = HashBasedTable
                        .create(searchResult.getTotalCount() / localeMap.size(), localeMap.size());
        for(final List<String> row : searchResult.getResult())
        {
            final String isocode = row.get(1);
            if(!localeMap.containsKey(isocode))
            {
                continue;
            }
            final String type = row.get(0);
            typeLocalization.put(type, localeMap.get(isocode), row.get(2));
        }
        return typeLocalization;
    }


    protected Map<String, Locale> loadLocalesMap()
    {
        final List<Locale> uiLocales = getCockpitLocaleService().getAllUILocales();
        return uiLocales.stream().collect(Collectors.toMap(Locale::toString, locale -> locale));
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    @Required
    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
