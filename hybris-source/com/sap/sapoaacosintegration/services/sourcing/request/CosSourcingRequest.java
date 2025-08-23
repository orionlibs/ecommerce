/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapoaacosintegration.services.sourcing.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;

/**
 *
 */
public class CosSourcingRequest
{
    private boolean createReservation;
    private String strategyId;
    private String reservationId;
    private CosDestinationCoordinates destinationCoordinates;
    private List<CosSourcingItem> cosSourcingItems;


    /**
     * @return the createReservation
     */
    @JsonGetter("createReservation")
    public boolean isCreateReservation()
    {
        return createReservation;
    }


    /**
     * @param createReservation
     *           the createReservation to set
     */
    @JsonSetter("createReservation")
    public void setCreateReservation(final boolean createReservation)
    {
        this.createReservation = createReservation;
    }


    /**
     * @return the destinationCoordinates
     */
    @JsonGetter("destinationCoordinates")
    public CosDestinationCoordinates getDestinationCoordinates()
    {
        return destinationCoordinates;
    }


    /**
     * @param destinationCoordinates
     *           the destinationCoordinates to set
     */
    @JsonSetter("destinationCoordinates")
    public void setDestinationCoordinates(final CosDestinationCoordinates destinationCoordinates)
    {
        this.destinationCoordinates = destinationCoordinates;
    }


    /**
     * @return the strategyId
     */
    @JsonGetter("strategyId")
    public String getStrategyId()
    {
        return strategyId;
    }


    /**
     * @param strategyId
     *           the strategyId to set
     */
    @JsonSetter("strategyId")
    public void setStrategyId(final String strategyId)
    {
        this.strategyId = strategyId;
    }


    /**
     * @return the reservationId
     */
    @JsonGetter("reservationId")
    public String getReservationId()
    {
        return reservationId;
    }


    /**
     * @param reservationId
     *           the reservationId to set
     */
    @JsonSetter("reservationId")
    public void setReservationId(final String reservationId)
    {
        this.reservationId = reservationId;
    }


    /**
     * @return the items
     */
    @JsonGetter("items")
    public List<CosSourcingItem> getItems()
    {
        return cosSourcingItems;
    }


    /**
     * @param cosSourcingItems
     *           the items to set
     */
    @JsonSetter("items")
    public void setItems(final List<CosSourcingItem> cosSourcingItems)
    {
        this.cosSourcingItems = cosSourcingItems;
    }
}
