/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.type;

/**
 * Message presenting a readable result of wizard's action.
 */
public class ResultMessage
{
    private final String title;
    private final String message;
    private final ResultMessageType type;


    public ResultMessage(final String title, final String message, final ResultMessageType type)
    {
        this.title = title;
        this.message = message;
        this.type = type;
    }


    public String getTitle()
    {
        return title;
    }


    public String getMessage()
    {
        return message;
    }


    public ResultMessageType getType()
    {
        return type;
    }


    @Override
    public String toString()
    {
        return "ResultMessage [title=" + title + ", message=" + message + ", type=" + type + "]";
    }
}
