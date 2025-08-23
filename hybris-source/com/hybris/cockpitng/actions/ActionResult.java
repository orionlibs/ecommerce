/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Result object of a cockpit action.
 *
 * @param <O> output type of the action
 */
public class ActionResult<O>
{
    /**
     * Result code for success.
     * Means that the action was performed successfully.
     */
    public static final String SUCCESS = "success";
    /**
     * Result code for error.
     * Means that the action was not performed successfully because of an error.
     */
    public static final String ERROR = "error";
    private O data;
    private String resultCode;
    private String resultMessage;
    private EnumSet<StatusFlag> statusFlags = EnumSet.noneOf(StatusFlag.class);
    private final transient Map<String, Object> socketOutputs = new LinkedHashMap<String, Object>(0);
    private final transient List<BackgroundOperationDefinition> socketAfterOperation = new ArrayList<BackgroundOperationDefinition>();


    /**
     * Constructor with a result code.
     *
     * @param resultCode the result code of the action
     * @see #SUCCESS
     * @see #ERROR
     */
    public ActionResult(final String resultCode)
    {
        this(resultCode, null);
    }


    /**
     * Constructor with a result code and data.
     *
     * @param resultCode the result code of the action
     * @param data data to be returned
     * @see #SUCCESS
     * @see #ERROR
     */
    public ActionResult(final String resultCode, final O data)
    {
        this.resultCode = resultCode;
        this.data = data;
    }


    /**
     * Returns the output data returned by the performed action.
     *
     * @return output data returned by the performed action
     */
    public O getData()
    {
        return data;
    }


    /**
     * Sets the output data of the performed action.
     *
     * @param data output data of the performed action
     */
    public void setData(final O data)
    {
        this.data = data;
    }


    /**
     * Returns the result code of the action.
     *
     * @return result code of the action
     * @see #SUCCESS
     * @see #ERROR
     */
    public String getResultCode()
    {
        return resultCode;
    }


    /**
     * Sets the result code of the action.
     *
     * @param resultCode result code of the action
     * @see #SUCCESS
     * @see #ERROR
     */
    public void setResultCode(final String resultCode)
    {
        this.resultCode = resultCode;
    }


    /**
     * Returns the result message.
     *
     * @return result message
     */
    public String getResultMessage()
    {
        return resultMessage;
    }


    /**
     * Sets the result message.
     *
     * @param resultMessage result message
     */
    public void setResultMessage(final String resultMessage)
    {
        this.resultMessage = resultMessage;
    }


    /**
     * Returns socket outputs and data to be sent after the action is has performed.
     *
     * @return socket outputs and data to be sent after the action is has performed. Key of the map is the socket ID,
     *         value contains the data to be sent.
     */
    public Map<String, Object> getOutputsToSend()
    {
        return socketOutputs;
    }


    /**
     * Adds data to be sent to the output socket with the given ID after the action has performed.
     *
     * @param outputId output ID of the socket through which the data should be sent. It is a socket of the widget the
     *           action
     *           resides in.
     * @param outputData the data to be sent
     */
    public void addOutputSocketToSend(final String outputId, final Object outputData)
    {
        this.socketOutputs.put(outputId, outputData);
    }


    /**
     * Registers a background operation.
     *
     * @param definition background operation
     */
    public void addSocketAfterOperation(final BackgroundOperationDefinition definition)
    {
        socketAfterOperation.add(definition);
    }


    /**
     * Retuns all background operations.
     *
     * @return all background operations
     */
    public List<BackgroundOperationDefinition> getSocketAfterOperation()
    {
        return socketAfterOperation;
    }


    public EnumSet<StatusFlag> getStatusFlags()
    {
        return statusFlags;
    }


    public void setStatusFlags(final EnumSet<StatusFlag> statusFlags)
    {
        this.statusFlags = statusFlags;
    }


    public enum StatusFlag
    {
        OBJECT_MODIFIED, OBJECT_PERSISTED, OBJECT_DELETED
    }
}
