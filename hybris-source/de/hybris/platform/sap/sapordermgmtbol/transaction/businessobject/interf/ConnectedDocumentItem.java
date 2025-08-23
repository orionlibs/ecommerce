/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapordermgmtbol.transaction.businessobject.interf;

import de.hybris.platform.sap.core.common.TechKey;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents the ConnectedDocumentItem object, which is a ConnectedObject on item level.<br>
 *
 */
public interface ConnectedDocumentItem extends ConnectedObject
{
    /**
     * Returns the document key.<br>
     *
     * @return Document key
     */
    TechKey getDocumentKey();


    /**
     * Sets the document key.<br>
     *
     * @param documentKey
     *           Document key
     */
    void setDocumentKey(TechKey documentKey);


    /**
     * Set the Position Number of the connected Item.<br>
     *
     * @param posNumber
     *           position number as String
     */
    void setPosNumber(String posNumber);


    /**
     * Returns the Position Number of the connected Item.<br>
     *
     * @return position number as String
     */
    String getPosNumber();


    /**
     * Returns the date of the item (e.g. delivery date).<br>
     *
     * @return Date of the item
     */
    Date getDate();


    /**
     * Sets the date of the item.<br>
     *
     * @param date
     *           Date of the item
     */
    void setDate(Date date);


    /**
     * Returns the quantity of the item (e.g. delivered quantity).<br>
     *
     * @return Quantity
     */
    BigDecimal getQuantity();


    /**
     * Sets the quantity of the item (e.g. delivered quantity).<br>
     *
     * @param quant
     *           Quantity
     */
    void setQuantity(BigDecimal quant);


    /**
     * Returns the quantity unit.<br>
     *
     * @return Quantity Unit
     */
    String getUnit();


    /**
     * Sets the quantity unit.<br>
     *
     * @param quantUnit
     *           Quantity Unit
     */
    void setUnit(String quantUnit);


    /**
     * Returns the origin of the document.<br>
     *
     * @return Origin of the document
     */
    @Override
    String getDocumentOrigin();


    /**
     * Sets the origin of the document.<br>
     *
     * @param string
     *           Origin of the document
     */
    @Override
    void setDocumentOrigin(String string);


    /**
     * Returns the application type.<br>
     *
     * @return Application type
     */
    String getAppTyp();


    /**
     * Sets the application type
     *
     * @param string
     *           Application type
     */
    void setAppTyp(String string);


    /**
     * Returns the tracking URL for the delivery doc flow entry.<br>
     *
     * @return Tracking URL
     */
    String getTrackingURL();


    /**
     * Sets the tracking URL for delivery doc flow entry.<br>
     *
     * @param string
     *           Tracking URL
     */
    void setTrackingURL(String string);
}