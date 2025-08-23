/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.util;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.util.ComponentMarkingUtils;

/**
 * Utilities used to render information about/in widgets
 */
public interface WidgetRenderingUtils extends ComponentMarkingUtils
{
    /**
     * Checks how a attribute's value should be shown to end user.
     * <P>
     * An attribute may be qualified with a SpEL expression regarding provided object. On basis of provided qualifier and
     * object, a value of attribute is extracted and its String representation is prepared.
     * </P>
     *
     * @param object
     *           item which attribute id to be rendered
     * @param dataType
     *           item's type description or <code>null</code> if an object does not belong do hybris Platform domain
     * @param qualifier
     *           full attribute qualifier or SpEL expression
     * @return evaluation result
     */
    QualifierLabel getAttributeLabel(final Object object, final DataType dataType, String qualifier);


    /**
     * Checks how particular value of an attribute should be shown to end user.
     *
     * @param value
     *           value of an attribute
     * @param attribute
     *           attribute which value is to presented
     * @return end user representation of attribute value
     */
    default QualifierLabel getAttributeLabel(final Object value, final DataAttribute attribute)
    {
        return new QualifierLabel(QualifierLabel.STATUS_ERROR, "Not implemented");
    }
}
