/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.adapter.converters;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Parameter;

public class DefaultAttributeToAttributeConverter implements
                CompareViewConverter<com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute, com.hybris.cockpitng.config.compareview.jaxb.Attribute>
{
    private static final String OBJECT_TO_CONVERT_CAN_NOT_BE_NULL = "Object to convert can not be null";


    @Override
    public com.hybris.cockpitng.config.compareview.jaxb.Attribute convert(
                    final com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute editorAreaAttribute)
    {
        if(editorAreaAttribute == null)
        {
            throw new IllegalArgumentException(OBJECT_TO_CONVERT_CAN_NOT_BE_NULL);
        }
        final com.hybris.cockpitng.config.compareview.jaxb.Attribute compareViewAttribute = new com.hybris.cockpitng.config.compareview.jaxb.Attribute();
        compareViewAttribute.setLabel(editorAreaAttribute.getLabel());
        compareViewAttribute.setQualifier(editorAreaAttribute.getQualifier());
        compareViewAttribute.setPosition(editorAreaAttribute.getPosition());
        compareViewAttribute.setEditor(editorAreaAttribute.getEditor());
        compareViewAttribute.setReadonly(editorAreaAttribute.isReadonly());
        editorAreaAttribute.getEditorParameter().stream().map(this::convertParameters)
                        .forEach(compareViewAttribute.getParameter()::add);
        return compareViewAttribute;
    }


    private com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter convertParameters(final Parameter parameter)
    {
        final com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter result = new com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter();
        result.setName(parameter.getName());
        result.setValue(parameter.getValue());
        return result;
    }
}
