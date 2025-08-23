/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.saprevenuecloudcustomer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
@JsonPropertyOrder(
                {"fistName", "lastName"})
public class PersonalInfo implements Serializable
{
    private static final Logger LOGGER = LogManager.getLogger(PersonalInfo.class);
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;


    @Override
    public String toString()
    {
        final ObjectMapper objectMapper = new ObjectMapper();
        String value = null;
        try
        {
            value = objectMapper.writeValueAsString(this);
        }
        catch(final JsonProcessingException e)
        {
            LOGGER.error(e);
        }
        return value;
    }


    /**
     * @return the firstName
     */
    @JsonProperty("firstName")
    public String getFirstName()
    {
        return firstName;
    }


    /**
     * @param firstName
     *           the firstName to set
     */
    @JsonProperty("firstName")
    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }


    /**
     * @return the lastName
     */
    @JsonProperty("lastName")
    public String getLastName()
    {
        return lastName;
    }


    /**
     * @param lastName
     *           the lastName to set
     */
    @JsonProperty("lastName")
    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }
}
