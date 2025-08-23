/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.json.WidgetJSONMapper;

public class WidgetObjectMapper extends ObjectMapper
{
    private final transient WidgetJSONMapper mapper;
    private transient WidgetInstanceManager widgetInstanceManager;


    public WidgetObjectMapper(final WidgetJSONMapper mapper)
    {
        super(new WidgetJsonFactory());
        WidgetObjectMapper.this.getJsonFactory().setCodec(this);
        this.mapper = mapper;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    protected void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public <T> T fromJSONString(final String json, final Class<T> resultType)
    {
        if(widgetInstanceManager == null)
        {
            return mapper.fromJSONString(json, resultType);
        }
        else
        {
            return mapper.fromJSONString(widgetInstanceManager, json, resultType);
        }
    }


    public String toJSONString(final Object object)
    {
        if(widgetInstanceManager == null)
        {
            return mapper.toJSONString(object);
        }
        else
        {
            return mapper.toJSONString(widgetInstanceManager, object);
        }
    }


    @Override
    public JsonParser treeAsTokens(final TreeNode n)
    {
        if(getJsonFactory() instanceof WidgetJsonFactory)
        {
            return ((WidgetJsonFactory)getJsonFactory()).createJsonParser(super.treeAsTokens(n));
        }
        else
        {
            return super.treeAsTokens(n);
        }
    }


    /**
     * @deprecated since 1811, use {@link #treeAsTokens(TreeNode)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    public JsonParser treeAsTokens(final JsonNode n)
    {
        return treeAsTokens((TreeNode)n);
    }
}
