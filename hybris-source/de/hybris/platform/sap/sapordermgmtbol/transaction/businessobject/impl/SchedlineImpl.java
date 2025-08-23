/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapordermgmtbol.transaction.businessobject.impl;

import de.hybris.platform.sap.core.bol.businessobject.BusinessObjectBase;
import de.hybris.platform.sap.core.common.exceptions.ApplicationBaseRuntimeException;
import de.hybris.platform.sap.sapordermgmtbol.transaction.businessobject.interf.Schedline;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Schedule line of the Sales Document
 *
 */
@SuppressWarnings("squid:S2160")
public class SchedlineImpl extends BusinessObjectBase implements Schedline
{
    private Date committedDate;
    private BigDecimal committedQuantity = null;
    private String unit;


    /**
     * Retrieves the committed date of the schedule line
     */
    @Override
    public Date getCommittedDate()
    {
        return committedDate;
    }


    /**
     * Retrieves the committed quantity of the schedule line
     */
    @Override
    public BigDecimal getCommittedQuantity()
    {
        return committedQuantity;
    }


    /**
     * Sets the committed date of the schedule line
     */
    @Override
    public void setCommittedDate(final Date committedDate)
    {
        this.committedDate = committedDate;
    }


    /**
     * Sets the committed quantity of the schedule line
     */
    @Override
    public void setCommittedQuantity(final BigDecimal committedQuantity)
    {
        this.committedQuantity = committedQuantity;
    }


    /**
     * Performs a deep-copy of the object rather than a shallow copy.
     *
     * @return returns a deep copy
     */
    @Override
    @SuppressWarnings("squid:S2975")
    public Object clone()
    {
        SchedlineImpl myClone;
        try
        {
            // immutable fields will be handled by object clone.
            myClone = (SchedlineImpl)super.clone();
        }
        catch(final CloneNotSupportedException ex)
        {
            // should not happen, because we are clone able
            throw new ApplicationBaseRuntimeException(
                            "Failed to clone Object, check whether Cloneable Interface is still implemented", ex);
        }
        // clone mutable fields
        if(committedDate != null)
        {
            myClone.committedDate = (Date)committedDate.clone();
        }
        return myClone;
    }


    @Override
    public void setUnit(final String unit)
    {
        this.unit = unit;
    }


    @Override
    public String getUnit()
    {
        return unit;
    }
}