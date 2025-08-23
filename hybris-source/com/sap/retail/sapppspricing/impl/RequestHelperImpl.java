/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.retail.sapppspricing.impl;

import com.sap.retail.opps.v1.dto.ARTSCommonHeaderType;
import com.sap.retail.opps.v1.dto.ARTSCommonHeaderType.ActionCodeEnum;
import com.sap.retail.opps.v1.dto.ARTSCommonHeaderType.MessageTypeEnum;
import com.sap.retail.opps.v1.dto.BusinessUnitCommonData;
import com.sap.retail.opps.v1.dto.BusinessUnitCommonData.TypeCodeEnum;
import com.sap.retail.opps.v1.dto.DateTimeCommonData;
import com.sap.retail.opps.v1.dto.HeaderDateTime;
import com.sap.retail.opps.v1.dto.IDCommonData;
import com.sap.retail.opps.v1.dto.ItemID;
import com.sap.retail.opps.v1.dto.LineItemDomainSpecific;
import com.sap.retail.opps.v1.dto.MessageID;
import com.sap.retail.opps.v1.dto.PriceCalculate;
import com.sap.retail.opps.v1.dto.PriceCalculateBase;
import com.sap.retail.opps.v1.dto.QuantityCommonData;
import com.sap.retail.opps.v1.dto.RetailPriceModifierDomainSpecific;
import com.sap.retail.opps.v1.dto.SaleBase;
import com.sap.retail.opps.v1.dto.ShoppingBasketBase;
import com.sap.retail.sapppspricing.RequestHelper;
import com.sap.retail.sapppspricing.exception.PPSRuntimeException;
import com.sap.retail.sapppspricing.opps.ObjectFactory;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.UUID;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class RequestHelperImpl implements RequestHelper
{
    private ObjectFactory objectFactory;
    private DatatypeFactory datatypeFactory;


    @Override
    public PriceCalculate createCalculateRequestSkeleton(final String businessUnitId, final GregorianCalendar priceDate)
    {
        final ObjectFactory fac = getObjectFactory();
        final PriceCalculate priceCalculate = fac.createPriceCalculate();
        priceCalculate.setInternalMajorVersion(1);
        priceCalculate.setInternalMinorVersion(0);
        final ARTSCommonHeaderType artsHeader = getObjectFactory().createARTSCommonHeaderType();
        priceCalculate.setArTSHeader(artsHeader);
        artsHeader.setActionCode(ActionCodeEnum.CALCULATE);
        artsHeader.setMessageType(MessageTypeEnum.REQUEST);
        HeaderDateTime dateTime = fac.createARTSCommonHeaderTypeDateTime();
        final GregorianCalendar cal = new GregorianCalendar();
        final XMLGregorianCalendar xmlCal = getDataTypeFactory().newXMLGregorianCalendar(cal);
        dateTime.setValue(xmlCal.toString());
        artsHeader.addDateTimeItem(dateTime);
        final BusinessUnitCommonData bu = fac.createBusinessUnitCommonData();
        bu.setValue(businessUnitId);
        // By default use retail stores
        bu.setTypeCode(TypeCodeEnum.RETAILSTORE);
        artsHeader.getBusinessUnit().add(bu);
        final MessageID msgId = fac.createARTSCommonHeaderTypeMessageID();
        msgId.setValue(UUID.randomUUID());
        artsHeader.setMessageID(msgId);
        final PriceCalculateBase priceCalculateBase = fac.createPriceCalculateBase();
        priceCalculate.getPriceCalculateBody().add(priceCalculateBase);
        final ShoppingBasketBase basket = fac.createShoppingBasketBase();
        priceCalculateBase.setShoppingBasket(basket);
        final IDCommonData tid = fac.createIDCommonData();
        priceCalculateBase.setTransactionID(tid);
        final DateTimeCommonData dateTime2 = fac.createDateTimeCommonData();
        final XMLGregorianCalendar xmlCal2 = getDataTypeFactory().newXMLGregorianCalendar(priceDate);
        dateTime2.value(xmlCal2.toString());
        priceCalculateBase.setDateTime(dateTime2);
        return priceCalculate;
    }


    private DatatypeFactory getDataTypeFactory()
    {
        if(datatypeFactory == null)
        {
            try
            {
                datatypeFactory = DatatypeFactory.newInstance();
            }
            catch(final DatatypeConfigurationException e)
            {
                throw new PPSRuntimeException("Could not create DatatypeFactory", e);
            }
        }
        return datatypeFactory;
    }


    @Override
    public LineItemDomainSpecific createSaleLineItem(final int sequenceNumber, final String itemIdentifier,
                    final String uomCode, final BigDecimal quantity)
    {
        final ObjectFactory fac = getObjectFactory();
        final LineItemDomainSpecific lineItem = fac.createLineItemDomainSpecific();
        lineItem.addSequenceNumberItem(sequenceNumber);
        final SaleBase sale = fac.createSaleBase();
        lineItem.setSale(sale);
        final QuantityCommonData qty = fac.createQuantityCommonData();
        sale.addQuantityItem(qty);
        qty.setUnitOfMeasureCode(uomCode);
        qty.setUnits(BigDecimal.valueOf(1));
        qty.setValue(quantity);
        final ItemID itemID = fac.createItemBaseItemID();
        itemID.setValue(itemIdentifier);
        sale.addItemIDItem(itemID);
        sale.setNonDiscountableFlag(Boolean.FALSE);
        return lineItem;
    }


    @Override
    public boolean isDistributed(final RetailPriceModifierDomainSpecific priceModifier)
    {
        return priceModifier.getItemLink() != null && !priceModifier.getItemLink().isEmpty();
    }


    /**
     * Get the object factory
     *
     * @return Object Factory
     */
    public ObjectFactory getObjectFactory()
    {
        return objectFactory;
    }


    /**
     * Set the object factory
     *
     * @param objectFactory
     *            Object Factory to set
     */
    public void setObjectFactory(final ObjectFactory objectFactory)
    {
        this.objectFactory = objectFactory;
    }


    @Override
    public SaleBase getLineItemContent(final LineItemDomainSpecific lineItem)
    {
        return lineItem.getSale();
    }
}
