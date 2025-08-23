/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapoaacosintegration.services.sourcing.impl;

import com.sap.sapoaacosintegration.services.common.util.CosServiceUtils;
import com.sap.sapoaacosintegration.services.config.CosConfigurationService;
import com.sap.sapoaacosintegration.services.sourcing.CosSourcingRequestMapper;
import com.sap.sapoaacosintegration.services.sourcing.CosSourcingResultHandler;
import com.sap.sapoaacosintegration.services.sourcing.request.CosDestinationCoordinates;
import com.sap.sapoaacosintegration.services.sourcing.request.CosSourcingItem;
import com.sap.sapoaacosintegration.services.sourcing.request.CosSourcingRequest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class DefaultCosSourcingRequestMapper implements CosSourcingRequestMapper
{
    private CosServiceUtils cosServiceUtils;
    private CosConfigurationService configurationService;
    private CosSourcingResultHandler cosSourcingResultHandler;
    private Converter<AddressModel, AddressData> addressConverter;


    /**
     * prepares a {@link CosSourcingRequest} that would be used in sourcing request
     */
    @Override
    public CosSourcingRequest prepareCosSourcingRequest(final AbstractOrderModel orderModel)
    {
        final CosSourcingRequest cosSourcingRequest = new CosSourcingRequest();
        final List<CosSourcingItem> cosSourcingItems = new ArrayList<>();
        addCartItems(orderModel, cosSourcingItems);
        cosSourcingRequest.setItems(cosSourcingItems);
        cosSourcingRequest.setStrategyId(getConfigurationService().getCosCasStrategyId());
        //setting coordinates for sourcing
        setDestinationCoordinates(cosSourcingRequest, orderModel);
        //decide sourcing with reservation to be done or not
        setReservationId(orderModel, cosSourcingRequest);
        return cosSourcingRequest;
    }


    protected void addCartItems(final AbstractOrderModel orderModel, final List<CosSourcingItem> cosSourcingItems)
    {
        for(final AbstractOrderEntryModel entryModel : orderModel.getEntries())
        {
            String itemNumberString = entryModel.getCosOrderItemId();
            if(null == itemNumberString || itemNumberString.isEmpty())
            {
                itemNumberString = getCosServiceUtils().generateItemNumber();
                entryModel.setCosOrderItemId(itemNumberString);
            }
            if(entryModel.getDeliveryPointOfService() != null)
            {
                //The above condition indicates the item is for pickUp, click and collect scenario.
                entryModel.setSapSource(entryModel.getDeliveryPointOfService());
                getCosSourcingResultHandler().populateScheduleLinesForPickupProduct(entryModel);
                continue;
            }
            final CosSourcingItem cosSourcingItem = new CosSourcingItem();
            cosSourcingItem.setItemId(itemNumberString);
            cosSourcingItem.setProductId(entryModel.getProduct().getCode());
            cosSourcingItem.setUnit(entryModel.getUnit().getSapCode());
            cosSourcingItem.setQuantity(entryModel.getQuantity());
            cosSourcingItems.add(cosSourcingItem);
        }
    }


    public void setDestinationCoordinates(final CosSourcingRequest cosSourcingRequest, final AbstractOrderModel orderModel)
    {
        if(orderModel.getDeliveryAddress() != null)
        {
            final GPS gps = getCosServiceUtils()
                            .fetchDestinationCoordinates(getAddressConverter().convert(orderModel.getDeliveryAddress()));
            final CosDestinationCoordinates cosDestinationCoordinates = new CosDestinationCoordinates();
            if(gps != null)
            {
                cosDestinationCoordinates.setLatitude(gps.getDecimalLatitude());
                cosDestinationCoordinates.setLongitude(gps.getDecimalLongitude());
            }
            cosSourcingRequest.setDestinationCoordinates(cosDestinationCoordinates);
        }
    }


    /**
     *
     */
    protected void setReservationId(final AbstractOrderModel orderModel, final CosSourcingRequest cosSourcingRequest)
    {
        if(orderModel.getCosReservationId() != null && StringUtils.isNotEmpty(orderModel.getCosReservationId()))
        {
            cosSourcingRequest.setCreateReservation(false);
            cosSourcingRequest.setReservationId(orderModel.getCosReservationId());
        }
        else
        {
            cosSourcingRequest.setCreateReservation(true);
        }
    }


    /**
     * @return the cosServiceUtils
     */
    public CosServiceUtils getCosServiceUtils()
    {
        return cosServiceUtils;
    }


    /**
     * @param cosServiceUtils
     *           the cosServiceUtils to set
     */
    public void setCosServiceUtils(final CosServiceUtils cosServiceUtils)
    {
        this.cosServiceUtils = cosServiceUtils;
    }


    /**
     * @return the configurationService
     */
    public CosConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    /**
     * @param configurationService
     *           the configurationService to set
     */
    public void setConfigurationService(final CosConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    /**
     * @return the cosSourcingResultHandler
     */
    public CosSourcingResultHandler getCosSourcingResultHandler()
    {
        return cosSourcingResultHandler;
    }


    /**
     * @param cosSourcingResultHandler
     *           the cosSourcingResultHandler to set
     */
    public void setCosSourcingResultHandler(final CosSourcingResultHandler cosSourcingResultHandler)
    {
        this.cosSourcingResultHandler = cosSourcingResultHandler;
    }


    /**
     * @return the addressConverter
     */
    public Converter<AddressModel, AddressData> getAddressConverter()
    {
        return addressConverter;
    }


    /**
     * @param addressConverter
     *           the addressConverter to set
     */
    public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
    {
        this.addressConverter = addressConverter;
    }
}