/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapcommonbol.transaction.util.impl;

/**
 * Helper class in order to have a nice output of fields, e.g. for toString()
 *
 * @version 1.0
 */
public class PrettyPrinter
{
    StringBuilder output;


    /**
     * Constructor
     *
     * @param start
     */
    public PrettyPrinter(final String start)
    {
        output = new StringBuilder(start);
    }


    PrettyPrinter(final StringBuilder output)
    {
        this.output = output;
    }


    @Override
    public String toString()
    {
        return output.toString();
    }


    /**
     * Appends the fieldName and the value to the string builder
     *
     * @param o
     * @param fieldName
     */
    public void add(final Object o, final String fieldName)
    {
        if(o != null)
        {
            doAppend(o, fieldName);
        }
    }


    private void doAppend(final Object o, final String fieldName)
    {
        output.append("\n" + fieldName + "=[" + o + "]");
    }
}
