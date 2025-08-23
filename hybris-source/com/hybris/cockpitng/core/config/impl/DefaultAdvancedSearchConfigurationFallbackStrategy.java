/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationFallbackStrategy;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.ConnectionOperatorType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldListType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.labels.LabelService;
import java.util.Collections;
import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAdvancedSearchConfigurationFallbackStrategy implements CockpitConfigurationFallbackStrategy<AdvancedSearch>
{
    public static final String DE_HYBRIS_PLATFORM_CORE_PK = "de.hybris.platform.core.PK";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBaseConfigFallbackStrategy.class);
    private TypeFacade typeFacade;
    private LabelService labelService;
    private DataType loadedType;


    @Override
    public AdvancedSearch loadFallbackConfiguration(final ConfigContext context, final Class<AdvancedSearch> configurationType)
    {
        final AdvancedSearch search = new AdvancedSearch();
        try
        {
            final String typeCode = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
            loadedType = loadType(typeCode, context, configurationType);
            if(loadedType == null)
            {
                return null;
            }
            final FieldListType fieldList = new FieldListType();
            fieldList.setDisableAttributesComparator(true);
            fieldList.setDisableSubtypesCheckbox(true);
            fieldList.setIncludeSubtypes(false);
            search.setFieldList(fieldList);
            search.setConnectionOperator(ConnectionOperatorType.OR);
            for(final DataAttribute att : loadedType.getAttributes())
            {
                final DataType type = att.getValueType();
                if((att.isMandatory() || isAtomic(type) || isEnum(type)) && att.isSearchable())
                {
                    final FieldType field = new FieldType();
                    field.setName(att.getQualifier());
                    field.setSelected(att.isMandatory());
                    fieldList.getField().add(field);
                    final DataType valueType = att.getValueType();
                    if(valueType != null && DE_HYBRIS_PLATFORM_CORE_PK.equals(valueType.getClazz().getCanonicalName()))
                    {
                        field.setEditor(Long.class.getCanonicalName());
                    }
                }
            }
            // sort fields by their names
            Collections.sort(fieldList.getField(), new FieldListNameComparator());
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error(e.getMessage(), e);
            return null;
        }
        return search;
    }


    private static boolean isAtomic(final DataType type)
    {
        return type != null && type.isAtomic();
    }


    private static boolean isEnum(final DataType type)
    {
        return type != null && type.getClazz() != null && type.getClazz().isEnum();
    }


    protected DataType loadType(final String type, final ConfigContext context, final Class<AdvancedSearch> configurationType) throws TypeNotFoundException
    {
        return typeFacade.load(type);
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    /**
     * Provides fields order by their localized names (ascending).
     */
    protected class FieldListNameComparator implements Comparator<FieldType>
    {
        @Override
        public int compare(final FieldType first, final FieldType second)
        {
            final String fName = StringUtils.defaultIfBlank(getAttributeLabel(first.getName()), StringUtils.EMPTY);
            final String sName = StringUtils.defaultIfBlank(getAttributeLabel(second.getName()), StringUtils.EMPTY);
            return fName.compareToIgnoreCase(sName);
        }


        protected String getAttributeLabel(final String qualifier)
        {
            final String typeCode = loadedType.getCode();
            if(StringUtils.isNotBlank(typeCode) && typeCode.indexOf('.') == -1)
            {
                final String locAttributeLabel = labelService.getObjectLabel(typeCode + '.' + qualifier);
                if(locAttributeLabel != null)
                {
                    return locAttributeLabel;
                }
            }
            return qualifier;
        }
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }
}
