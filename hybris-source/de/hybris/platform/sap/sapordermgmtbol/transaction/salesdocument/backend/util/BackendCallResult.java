/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapordermgmtbol.transaction.salesdocument.backend.util;

/**
 * Stores the result of a call to the backend. Is evaluated in backend layer implementations e.g. SalesDocumentERP,
 * SalesDocumentCRM
 *
 */
public class BackendCallResult
{
    /**
     * The constants for the result of the back end call. <br
     */
    public enum Result
    {
        /**
         * Call executed successfully
         */
        success,
        /**
         * Call execution failed
         */
        failure
    }


    private final Result result;


    /**
     * Stores the result of the back end call. <br>
     *
     * @param result
     *           the enum value for the success or failure
     */
    public BackendCallResult(final Result result)
    {
        this.result = result;
    }


    /**
     * The standard constructor will initialize to 'success'!
     */
    public BackendCallResult()
    {
        result = Result.success;
    }


    /**
     * @return result the enum value for the success or failure
     */
    public Result getResult()
    {
        return result;
    }


    /**
     * @return true if the get back end call was failure
     */
    public boolean isFailure()
    {
        return result.equals(Result.failure);
    }
}
