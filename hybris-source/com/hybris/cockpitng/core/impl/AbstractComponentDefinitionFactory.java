/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CNG;
import com.hybris.cockpitng.core.CockpitComponentDefinitionFactory;
import com.hybris.cockpitng.core.CockpitComponentInfo;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.impl.jaxb.ComponentDefinition;
import com.hybris.cockpitng.core.impl.jaxb.Forward;
import com.hybris.cockpitng.core.impl.jaxb.Setting;
import com.hybris.cockpitng.core.impl.jaxb.Settings;
import com.hybris.cockpitng.core.impl.jaxb.Socket;
import com.hybris.cockpitng.core.impl.jaxb.Sockets;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.core.util.jaxb.JAXBContextFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base component definition factory managing some common properties like the location path, settings etc.
 */
public abstract class AbstractComponentDefinitionFactory implements CockpitComponentDefinitionFactory
{
    public static final String PROPERTY_COMPONENT_SETTINGS = "component-settings";
    public static final String PROPERTY_WIDGET_ID = "widget-id";
    public static final String PROPERTY_WIDGET_PARENT = "widget-parent";
    public static final String PROPERTY_WIDGET_INPUTS = "widget-inputs";
    public static final String PROPERTY_WIDGET_OUTPUTS = "widget-outputs";
    public static final String PROPERTY_WIDGET_IO_FORWARDS = "widget-io-forwards";
    public static final int ARRAY_CAPACITY = 3;
    /**
     * Path separator.
     */
    protected static final String PATH_SEPARATOR = "/";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentDefinitionFactory.class);
    private JAXBContextFactory jaxbContextFactory;
    private String directoryURI;


    @Override
    public final AbstractCockpitComponentDefinition create(final CockpitComponentInfo info)
    {
        final AbstractCockpitComponentDefinition definition = createDefinition(info);
        setCommonProperties(info, definition);
        return definition;
    }


    public abstract AbstractCockpitComponentDefinition createDefinition(final CockpitComponentInfo info);


    private void setCommonProperties(final CockpitComponentInfo info, final AbstractCockpitComponentDefinition definition)
    {
        final String widgetPath = info.getRootPath();
        if(StringUtils.isNotBlank(widgetPath))
        {
            final String resourcePath = (widgetPath.charAt(0) == '/' ? widgetPath.substring(1) : widgetPath);
            definition.setResourcePath(resourcePath);
            final String locationPath = resourcePath.startsWith(WidgetLibConstants.ROOT_NAME_COCKPIT_RESOURCES)
                            ? resourcePath.replaceFirst(WidgetLibConstants.ROOT_NAME_COCKPIT_RESOURCES, StringUtils.EMPTY) : resourcePath;
            definition.setLocationPath(PATH_SEPARATOR + WidgetLibConstants.CLASSPATH_RESOURCES_PATH_PREFIX + PATH_SEPARATOR
                            + StringUtils.removeStart(StringUtils.removeEnd(locationPath, PATH_SEPARATOR), PATH_SEPARATOR));
        }
        else
        {
            definition.setResourcePath(StringUtils.EMPTY);
            definition.setLocationPath(
                            PATH_SEPARATOR + WidgetLibConstants.JAR_RESOURCES_PATH_PREFIX + PATH_SEPARATOR + definition.getCode());
        }
        final Properties properties = info.getProperties();
        TypedSettingsMap settings = (TypedSettingsMap)properties.get(PROPERTY_COMPONENT_SETTINGS);
        if(StringUtils.isNotBlank(info.getExternalLocation()))
        {
            if(settings == null)
            {
                settings = new TypedSettingsMap();
            }
            settings.put("__externalLocationPath", info.getExternalLocation());
            definition.setDeclaringModule(info.getExternalLocation());
        }
        definition.setDefaultSettings(settings);
        assignSockets(info, definition);
    }


    private static String appendSlash(final String path)
    {
        return StringUtils.appendIfMissing(path, PATH_SEPARATOR);
    }


    private void assignSockets(final CockpitComponentInfo info, final AbstractCockpitComponentDefinition definition)
    {
        // inputs, outputs
        final List<WidgetSocket> outputSockets = new ArrayList<>();
        final List<WidgetSocket> inputSockets = new ArrayList<>();
        final String outputs = info.getProperties().getProperty(PROPERTY_WIDGET_OUTPUTS);
        if(StringUtils.isNotBlank(outputs))
        {
            final List<String[]> parsedSocketInfo = parseSocketInfo(outputs);
            for(final String[] strings : parsedSocketInfo)
            {
                final WidgetSocket socket = createSocket(strings[0], strings[1], strings[2], false);
                outputSockets.add(socket);
            }
        }
        final String inputs = info.getProperties().getProperty(PROPERTY_WIDGET_INPUTS);
        if(StringUtils.isNotBlank(inputs))
        {
            final List<String[]> parsedSocketInfo = parseSocketInfo(inputs);
            for(final String[] strings : parsedSocketInfo)
            {
                final WidgetSocket socket = createSocket(strings[0], strings[1], strings[2], true);
                inputSockets.add(socket);
            }
        }
        definition.setOutputs(outputSockets);
        definition.setInputs(inputSockets);
        // socket io forwards
        final String forwards = info.getProperties().getProperty(PROPERTY_WIDGET_IO_FORWARDS);
        if(StringUtils.isNotBlank(forwards))
        {
            final Map<String, String> forwardMap = new HashMap<>();
            final List<String[]> parseForwardInfo = parseSocketInfo(forwards);
            for(final String[] strings : parseForwardInfo)
            {
                forwardMap.put(strings[0], strings[1]);
            }
            definition.setForwardMap(forwardMap);
        }
    }


    protected List<String[]> parseSocketInfo(final String propsStr)
    {
        final List<String[]> ret = new ArrayList<>();
        final String[] split = StringUtils.split(propsStr, ";");
        for(final String string : split)
        {
            String singleSocketInfo = string.trim();
            if(singleSocketInfo.charAt(0) == '[' && singleSocketInfo.endsWith("]"))
            {
                singleSocketInfo = singleSocketInfo.substring(1, singleSocketInfo.length() - 1);
            }
            final String[] split2 = StringUtils.split(singleSocketInfo, ",");
            final String[] entry = new String[ARRAY_CAPACITY];
            entry[0] = split2[0];
            if(split2.length > 1 && StringUtils.isNotBlank(split2[1]))
            {
                entry[1] = split2[1];
            }
            if(split2.length > 2)
            {
                entry[2] = split2[2];
            }
            ret.add(entry);
        }
        return ret;
    }


    private static WidgetSocket createSocket(final String id, final String typeCode, final String multiplicity,
                    final boolean inputSocket)
    {
        final WidgetSocket socket = new WidgetSocket(null, inputSocket ? CNG.SOCKET_IN : CNG.SOCKET_OUT);
        if(id.trim().contains(":external"))
        {
            socket.setId(id.replace(":external", "").trim());
            socket.setVisibility(WidgetSocket.SocketVisibility.EXTERNAL);
        }
        else if(id.trim().contains(":internal"))
        {
            socket.setId(id.replace(":internal", "").trim());
            socket.setVisibility(WidgetSocket.SocketVisibility.INTERNAL);
        }
        else if(id.trim().contains(":hidden"))
        {
            socket.setId(id.replace(":hidden", "").trim());
            socket.setVisibility(WidgetSocket.SocketVisibility.HIDDEN);
        }
        else
        {
            socket.setId(id);
        }
        String localTypeCode = typeCode;
        WidgetSocket.Multiplicity enumVal = null;
        if(typeCode != null && typeCode.contains("("))
        {
            if(typeCode.contains("Collection("))
            {
                localTypeCode = typeCode.replace("Collection(", "").replace(")", "");
                enumVal = WidgetSocket.Multiplicity.COLLECTION;
            }
            else if(typeCode.contains("Set("))
            {
                localTypeCode = typeCode.replace("Set(", "").replace(")", "");
                enumVal = WidgetSocket.Multiplicity.SET;
            }
            else if(typeCode.contains("List("))
            {
                localTypeCode = typeCode.replace("List(", "").replace(")", "");
                enumVal = WidgetSocket.Multiplicity.LIST;
            }
        }
        else
        {
            if("collection".equalsIgnoreCase(multiplicity))
            {
                enumVal = WidgetSocket.Multiplicity.COLLECTION;
            }
            else if("list".equalsIgnoreCase(multiplicity))
            {
                enumVal = WidgetSocket.Multiplicity.LIST;
            }
            else if("set".equalsIgnoreCase(multiplicity))
            {
                enumVal = WidgetSocket.Multiplicity.SET;
            }
        }
        if(localTypeCode == null)
        {
            localTypeCode = "java.lang.Object";
        }
        else if(localTypeCode.contains("["))
        {
            localTypeCode = localTypeCode.replace('[', '<').replace(']', '>');
        }
        socket.setDataType(localTypeCode);
        socket.setDataMultiplicity(enumVal);
        return socket;
    }


    protected <T> T getXMLDefinition(final String path, final CockpitComponentInfo info, final Class<T> defClass)
    {
        String widgetsXml = StringUtils.trimToEmpty(path);
        widgetsXml = appendSlash(widgetsXml) + "definition.xml";
        if(widgetsXml.charAt(0) == '/')
        {
            widgetsXml = widgetsXml.substring(1);
        }
        T result = null;
        final InputStream stream = getInfoResourceAsStream(info, widgetsXml);
        if(stream != null)
        {
            try
            {
                final Unmarshaller unmarshaller = createUnmarshaller(defClass,
                                com.hybris.cockpitng.core.impl.jaxb.ComponentDefinition.class);
                result = (T)unmarshaller.unmarshal(stream);
            }
            catch(final JAXBException e)
            {
                Throwable originalException = e;
                if(e.getLinkedException() != null)
                {
                    originalException = e.getLinkedException();
                }
                LOG.error("loading component definition failed", originalException);
                return null;
            }
            finally
            {
                IOUtils.closeQuietly(stream);
            }
        }
        return result;
    }


    private Unmarshaller createUnmarshaller(final Class<?>... classes) throws JAXBException
    {
        return getJAXBContext(classes).createUnmarshaller();
    }


    private JAXBContext getJAXBContext(final Class<?>... classes) throws JAXBException
    {
        if(this.jaxbContextFactory == null)
        {
            return JAXBContext.newInstance(classes);
        }
        else
        {
            return this.jaxbContextFactory.createContext(classes);
        }
    }


    protected InputStream getInfoResourceAsStream(final CockpitComponentInfo info, final String filename)
    {
        if(getDirectoryURI() == null)
        {
            return info.getClassLoader().getResourceAsStream(filename);
        }
        else
        {
            final File file = new File(getDirectoryURI(), filename);
            if(file.exists())
            {
                try
                {
                    return new FileInputStream(file);
                }
                catch(final FileNotFoundException e)
                {
                    LOG.error("Could not read widget definition file '" + file + "', reason was ", e);
                }
            }
            return null;
        }
    }


    protected void loadProperties(final Properties properties, final ComponentDefinition xmlDef)
    {
        // ID
        setProperty(properties, PROPERTY_WIDGET_ID, xmlDef.getId());
        setProperty(properties, PROPERTY_WIDGET_PARENT, xmlDef.getParent());
        // inputs, outputs
        // socket io forwards
        final Sockets sockets = xmlDef.getSockets();
        if(sockets != null)
        {
            setProperty(properties, PROPERTY_WIDGET_INPUTS, socketsToString(sockets.getInput()));
            setProperty(properties, PROPERTY_WIDGET_OUTPUTS, socketsToString(sockets.getOutput()));
            setProperty(properties, PROPERTY_WIDGET_IO_FORWARDS, forwardsToString(sockets.getForward()));
        }
        // settings
        final Settings settings = xmlDef.getSettings();
        if(settings != null && !settings.getSetting().isEmpty())
        {
            final TypedSettingsMap settingMap = new TypedSettingsMap();
            for(final Setting setting : settings.getSetting())
            {
                final String type = setting.getType();
                final String value = setting.getDefaultValue();
                final String key = setting.getKey();
                if("Boolean".equalsIgnoreCase(type) || "java.lang.Boolean".equalsIgnoreCase(type))
                {
                    Boolean typedValue = Boolean.FALSE;
                    if(!StringUtils.isBlank(value))
                    {
                        typedValue = "null".equalsIgnoreCase(value) ? null : Boolean.valueOf(value);
                    }
                    settingMap.put(key, typedValue, Boolean.class);
                }
                else if("Integer".equalsIgnoreCase(type) || "java.lang.Integer".equalsIgnoreCase(type))
                {
                    Integer typedValue = Integer.valueOf(0);
                    if(!StringUtils.isBlank(value))
                    {
                        try
                        {
                            typedValue = "null".equalsIgnoreCase(value) ? null : Integer.valueOf(value);
                        }
                        catch(final NumberFormatException e)
                        {
                            LOG.warn("Invalid integer value: " + value);
                        }
                    }
                    settingMap.put(key, typedValue, Integer.class);
                }
                else if("Double".equalsIgnoreCase(type) || "java.lang.Double".equalsIgnoreCase(type))
                {
                    Double typedValue = Double.valueOf(0.0);
                    if(!StringUtils.isBlank(value))
                    {
                        try
                        {
                            typedValue = "null".equalsIgnoreCase(value) ? null : Double.valueOf(value);
                        }
                        catch(final NumberFormatException e)
                        {
                            LOG.warn("Invalid double value: " + value);
                        }
                    }
                    settingMap.put(key, typedValue, String.class);
                }
                else if("String".equalsIgnoreCase(type) || "java.lang.String".equalsIgnoreCase(type) || StringUtils.isBlank(type))
                {
                    settingMap.put(key, StringUtils.isBlank(value) ? "" : value, String.class);
                }
                else if(type.trim().startsWith("ENUM("))
                {
                    String availableValuesString = type.trim();
                    if(!availableValuesString.isEmpty() && availableValuesString.charAt(availableValuesString.length() - 1) == ')')
                    {
                        availableValuesString = availableValuesString.substring("ENUM(".length(), availableValuesString.length() - 1);
                        final String[] availableValues = availableValuesString.split(",");
                        settingMap.setAvailableValues(key, Arrays.asList(availableValues));
                        settingMap.put(key, value, String.class);
                    }
                    else
                    {
                        LOG.warn("Could not read setting '{}' for widget '{}', wrong enum specification '{}'.", key, xmlDef.getId(),
                                        type);
                    }
                }
                else if(value != null)
                {
                    LOG.warn("Could not read setting '{}' for widget '{}', values of type '{}' not supported.", key, xmlDef.getId(),
                                    value.getClass().getName());
                }
            }
            properties.put(PROPERTY_COMPONENT_SETTINGS, settingMap);
        }
    }


    private static String forwardsToString(final List<Forward> forwards)
    {
        final StringBuilder forwardsString = new StringBuilder();
        if(CollectionUtils.isNotEmpty(forwards))
        {
            boolean first = true;
            for(final Forward forward : forwards)
            {
                if(first)
                {
                    first = false;
                }
                else
                {
                    forwardsString.append(";");
                }
                forwardsString.append("[").append(forward.getInput()).append(",").append(forward.getOutput()).append("]");
            }
        }
        return forwardsString.toString();
    }


    private static String socketsToString(final List<Socket> sockets)
    {
        final StringBuilder inputs = new StringBuilder();
        if(CollectionUtils.isNotEmpty(sockets))
        {
            boolean first = true;
            for(final Socket socket : sockets)
            {
                if(first)
                {
                    first = false;
                }
                else
                {
                    inputs.append(";");
                }
                inputs.append(socket.getId());
                if(!StringUtils.isBlank(socket.getType()))
                {
                    inputs.append(",").append(socket.getType());
                }
                if(socket.getMultiplicity() != null)
                {
                    inputs.append(",").append(socket.getMultiplicity().value());
                }
            }
        }
        return inputs.toString();
    }


    protected void setProperty(final Properties properties, final String key, final String value)
    {
        if(key != null && value != null)
        {
            properties.setProperty(key, value);
        }
    }


    protected String getWidgetSimpleID(final String id)
    {
        final int lastDot = id.lastIndexOf('.');
        return id.substring(lastDot + 1);
    }


    protected boolean checkIfFileIsAbsent(final String viewURI, final CockpitComponentInfo info)
    {
        String uri = viewURI.trim();
        if(uri.startsWith(PATH_SEPARATOR))
        {
            uri = uri.substring(1);
        }
        InputStream is = null;
        try
        {
            is = getInfoResourceAsStream(info, uri);
            return is == null;
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
    }


    public String getDirectoryURI()
    {
        return directoryURI;
    }


    public void setDirectoryURI(final String directoryURI)
    {
        this.directoryURI = directoryURI;
    }


    public void setJaxbContextFactory(final JAXBContextFactory jaxbContextFactory)
    {
        this.jaxbContextFactory = jaxbContextFactory;
    }
}
