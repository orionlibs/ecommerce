/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.util.impl;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute.AttributeType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.services.PropertyReadResult;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.impl.DefaultComponentMarkingUtils;
import com.hybris.cockpitng.widgets.util.QualifierLabel;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class DefaultWidgetRenderingUtils extends DefaultComponentMarkingUtils implements WidgetRenderingUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetRenderingUtils.class);
    private static final String RESTRICTED_DATA = "data.restricted";
    private static final String ERRONEOUS_DATA = "data.erroneous";
    private static final String ENCRYPTED_DATA = "data.encrypted";
    private PropertyValueService propertyValueService;
    private LabelService labelService;
    private TypeFacade typeFacade;
    private CockpitLocaleService cockpitLocaleService;


    @Override
    public QualifierLabel getAttributeLabel(final Object object, final DataType dataType, final String qualifier)
    {
        final DataAttribute attribute = getDataAttribute(object, dataType, qualifier);
        final PropertyReadResult readResult = readProperty(object, dataType, qualifier, attribute);
        if(readResult != null && readResult.isSuccessful())
        {
            return computeLabel(qualifier, attribute, readResult);
        }
        else if(readResult != null && PropertyReadResult.READ_STATUS_RESTRICTED == readResult.getStatus())
        {
            return new QualifierLabel(QualifierLabel.STATUS_RESTRICTED, Labels.getLabel(RESTRICTED_DATA));
        }
        else
        {
            return new QualifierLabel(QualifierLabel.STATUS_ERROR, Labels.getLabel(ERRONEOUS_DATA));
        }
    }


    protected PropertyReadResult readProperty(final Object object, final DataType dataType, final String qualifier,
                    final DataAttribute attribute)
    {
        final PropertyReadResult readResult;
        if(attribute != null && attribute.isLocalized())
        {
            final Map<Locale, PropertyReadResult> value = getPropertyValueService().readRestrictedValue(object, qualifier,
                            Collections.singletonList(getCockpitLocaleService().getCurrentLocale()));
            readResult = value.values().iterator().next();
        }
        else if(dataType != null)
        {
            readResult = getPropertyValueService().readRestrictedValue(object, qualifier);
        }
        else
        {
            final Object value = getPropertyValueService().readValue(object, qualifier);
            readResult = new PropertyReadResult(value);
        }
        return readResult;
    }


    @Override
    public QualifierLabel getAttributeLabel(final Object value, final DataAttribute attribute)
    {
        return computeLabel(attribute.getQualifier(), attribute, new PropertyReadResult(value));
    }


    /**
     * Computes label for property described by qualifier, attribute and read result.
     *
     * @param qualifier
     *           the property qualifier.
     * @param attribute
     *           the attribute (can be {@code null}).
     * @param readResult
     *           the property read result (must be {@code successful} - see {@link PropertyReadResult#isSuccessful}).
     * @return computed label.
     */
    protected QualifierLabel computeLabel(final String qualifier, final DataAttribute attribute,
                    final PropertyReadResult readResult)
    {
        Validate.notNull("Read result may not be null", readResult);
        Validate.assertTrue("Read result must be successful", readResult.isSuccessful());
        if(attribute != null && attribute.isEncrypted())
        {
            return createEncryptedLabel();
        }
        else if(attribute != null && DataType.STRING.equals(attribute.getValueType())
                        && AttributeType.SINGLE == attribute.getAttributeType())
        {
            return new QualifierLabel(readResult.getValue());
        }
        return computeLabelInternal(qualifier, readResult.getValue());
    }


    protected QualifierLabel createEncryptedLabel()
    {
        return new QualifierLabel(QualifierLabel.STATUS_RESTRICTED, Labels.getLabel(ENCRYPTED_DATA));
    }


    QualifierLabel computeLabelInternal(final String qualifier, final Object value)
    {
        try
        {
            if(value == null || (value instanceof Collection && CollectionUtils.isEmpty((Collection)value)))
            {
                return new QualifierLabel(StringUtils.EMPTY);
            }
            return new QualifierLabel(getLabelService().getObjectLabel(value));
        }
        catch(final RuntimeException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Could not get label for field '%s'.", qualifier), e);
            }
            return new QualifierLabel(QualifierLabel.STATUS_ERROR, e.getLocalizedMessage());
        }
    }


    protected DataAttribute getDataAttribute(final Object object, final DataType dataType, final String qualifier)
    {
        DataAttribute attribute = null;
        if(dataType != null)
        {
            attribute = dataType.getAttribute(qualifier);
            if(attribute == null)
            {
                attribute = typeFacade.getAttribute(object, qualifier);
            }
        }
        return attribute;
    }


    protected PropertyValueService getPropertyValueService()
    {
        return propertyValueService;
    }


    @Required
    public void setPropertyValueService(final PropertyValueService propertyValueService)
    {
        this.propertyValueService = propertyValueService;
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
