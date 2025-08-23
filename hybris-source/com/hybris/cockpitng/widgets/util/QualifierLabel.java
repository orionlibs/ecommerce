/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.util;

import com.hybris.cockpitng.dataaccess.services.PropertyReadResult;

/**
 * POJO representing a result of nested qualifiers value's label evaluation.
 * <P>
 * Class contains information whether evaluation was finished successfully and if so, then what label was a result of
 * this evaluation.
 * </P>
 */
public class QualifierLabel
{
    /**
     * Indicates that evaluation has finished successfully and a label is available at {@link #getLabel()} method
     */
    public static final byte STATUS_SUCCESS = PropertyReadResult.READ_STATUS_SUCCESS;
    /**
     * Indicates that during evaluation a restricted property has been requested - current user has not enough privileges
     * to evaluate qualifier. Label is unavailable and {@link #getLabel()} will return appropriate message.
     */
    public static final byte STATUS_RESTRICTED = PropertyReadResult.READ_STATUS_RESTRICTED;
    /**
     * An error occurred during evaluation. Most probably a qualifier provided is not valid SpEL expression or properties
     * requested does not exist. Label is unavailable and {@link #getLabel()} will return errors message.
     */
    public static final byte STATUS_ERROR = PropertyReadResult.READ_STATUS_ERROR;
    private final byte status;
    private final String label;


    public QualifierLabel(final String label)
    {
        this(STATUS_SUCCESS, label);
    }


    public QualifierLabel(final byte status, final String label)
    {
        this.status = status;
        this.label = label;
    }


    /**
     * @return <code>true</code> if qualifier's value label evaluation was finished successfully
     */
    public boolean isSuccessful()
    {
        return status == STATUS_SUCCESS;
    }


    /**
     * Returns a label for evaluated nested qualifier's value
     *
     * @return qualifier's value label evaluation result
     */
    public String getLabel()
    {
        return label;
    }


    /**
     *
     * @return status of evaluation
     * @see #STATUS_SUCCESS
     * @see #STATUS_RESTRICTED
     * @see #STATUS_ERROR
     */
    public byte getStatus()
    {
        return status;
    }
}
