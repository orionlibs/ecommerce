/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.handler.login.impl;

import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.handler.login.LoginInformationConfigData;
import com.hybris.cockpitng.handler.login.LoginInformationHandler;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLoginInformationHandler implements LoginInformationHandler, Serializable
{
    private static final transient Logger LOG = LoggerFactory.getLogger(DefaultLoginInformationHandler.class);
    private final TypedSettingsMap loginInformation;
    private List<LoginInformationConfigData> configuration;


    public DefaultLoginInformationHandler(final TypedSettingsMap loginTimeInformation)
    {
        this.loginInformation = loginTimeInformation;
    }


    @Override
    public TypedSettingsMap getLoginInformation()
    {
        return loginInformation;
    }


    @Override
    public List<LoginInformationConfigData> getConfiguration()
    {
        return configuration;
    }


    public void setConfiguration(final List<LoginInformationConfigData> configuration)
    {
        sortByIndex(configuration);
        this.configuration = Collections.unmodifiableList(configuration);
    }


    private void sortByIndex(List<LoginInformationConfigData> configuration)
    {
        Collections.sort(configuration, new Comparator<LoginInformationConfigData>()
        {
            @Override
            public int compare(final LoginInformationConfigData leftSide, final LoginInformationConfigData rightSide)
            {
                int ret = 0;
                if(leftSide != null && rightSide != null)
                {
                    ret = leftSide.getIndex() - rightSide.getIndex();
                }
                else if(leftSide == null && rightSide == null)
                {
                    ret = 0;
                }
                else if(leftSide == null)
                {
                    ret = -1;
                }
                else
                {
                    ret = 1;
                }
                return ret;
            }
        });
    }


    @Override
    public Object getLoginInformation(final String key)
    {
        return loginInformation.get(key);
    }


    public void init()
    {
        for(final LoginInformationConfigData loginData : getConfiguration())
        {
            putLoginInformation(loginData.getKey(), loginData.getValue(), loginData.getType());
        }
    }


    @Override
    public void putLoginInformation(final String key, final Object value)
    {
        loginInformation.put(key, value);
    }


    protected void putLoginInformation(final String key, final String value, final String type)
    {
        String internalValue = value;
        if("Boolean".equalsIgnoreCase(type) || "java.lang.Boolean".equalsIgnoreCase(type))
        {
            Boolean typedValue = Boolean.FALSE;
            if(!StringUtils.isBlank(internalValue))
            {
                typedValue = Boolean.valueOf(internalValue);
            }
            loginInformation.put(key, typedValue);
        }
        else if("Integer".equalsIgnoreCase(type) || "java.lang.Integer".equalsIgnoreCase(type))
        {
            Integer typedValue = Integer.valueOf(0);
            if(!StringUtils.isBlank(internalValue))
            {
                try
                {
                    typedValue = Integer.valueOf(internalValue);
                }
                catch(final NumberFormatException e)
                {
                    LOG.warn("Invalid integer value: " + internalValue);
                }
            }
            loginInformation.put(key, typedValue);
        }
        else if("Double".equalsIgnoreCase(type) || "java.lang.Double".equalsIgnoreCase(type))
        {
            Double typedValue = Double.valueOf(0.0);
            if(!StringUtils.isBlank(internalValue))
            {
                try
                {
                    typedValue = Double.valueOf(internalValue);
                }
                catch(final NumberFormatException e)
                {
                    LOG.warn("Invalid double value: " + internalValue);
                }
            }
            loginInformation.put(key, typedValue);
        }
        else if("String".equalsIgnoreCase(type) || "java.lang.String".equalsIgnoreCase(type) || StringUtils.isBlank(type))
        {
            if(StringUtils.isBlank(internalValue))
            {
                internalValue = StringUtils.EMPTY;
            }
            loginInformation.put(key, internalValue);
        }
        else if(type.trim().startsWith("ENUM("))
        {
            String availableValuesString = type.trim();
            if(!availableValuesString.isEmpty() && availableValuesString.charAt(availableValuesString.length() - 1) == ')')
            {
                availableValuesString = availableValuesString.substring("ENUM(".length(), availableValuesString.length() - 1);
                final String[] availableValues = availableValuesString.split("\\s*,\\s*");
                loginInformation.setAvailableValues(key, Arrays.asList(availableValues));
                loginInformation.put(key, internalValue);
            }
        }
    }
}
