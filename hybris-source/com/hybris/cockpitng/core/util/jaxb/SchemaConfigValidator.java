/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.jaxb;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.core.util.jaxb.impl.GenericValidationEventCollector;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <p>
 * Defines schema based validator for marshaling/unmarshalling xml based configuration.
 * Note: Please see {@link com.hybris.cockpitng.core.config.CockpitConfigurationService},
 * {@link com.hybris.cockpitng.core.persistence.WidgetPersistenceService} for details.
 */
public class SchemaConfigValidator
{
    private static final Logger LOG = LoggerFactory.getLogger(SchemaConfigValidator.class);
    private static final String SCHEMA_SUFFIX = ".xsd";
    private static final String MESSAGE_ERROR_FILE_PROCESSING = "Errors occurred while processing configuration file";
    private SchemaLocationRegistry schemaLocationRegistry;
    private CockpitResourceLoader widgetResourceReader;


    /**
     * Checks whether given xml content is valid against all schemas available from schemaLocationRegistry
     *
     * @param content
     *           xml config that is being validated
     * @param context
     *           context that is being used
     * @return true if the given content is valid otherwise false
     */
    public SchemaValidationStatus validate(final InputStream content, final JAXBContext context)
    {
        SchemaValidationStatus status = SchemaValidationStatus.success();
        final List<InputStream> inputStreams = Lists.newArrayList();
        Schema mergedSchema = null;
        try
        {
            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaFactory.setResourceResolver((type, namespaceURI, publicId, systemId, baseURI) -> {
                LSInput lsInput = null;
                try
                {
                    lsInput = (LSInput)Class.forName("com.sun.org.apache.xerces.internal.dom.DOMInputImpl").getDeclaredConstructor().newInstance();
                }
                catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e)
                {
                    LOG.error("Errors occurred while creating lsInput", e);
                }
                String identifier = StringUtils.remove(systemId, SCHEMA_SUFFIX);
                if(identifier.contains(String.valueOf(IOUtils.DIR_SEPARATOR_UNIX)))
                {
                    identifier = StringUtils.substringAfterLast(identifier, String.valueOf(IOUtils.DIR_SEPARATOR_UNIX));
                }
                final String schemaLocation = schemaLocationRegistry.getSchemaLocation(identifier);
                if(StringUtils.isNoneBlank(schemaLocation) && Objects.nonNull(lsInput))
                {
                    final InputStream stream = widgetResourceReader.getResourceAsStream(schemaLocation);
                    inputStreams.add(stream);
                    lsInput.setPublicId(publicId);
                    lsInput.setSystemId(systemId);
                    lsInput.setBaseURI(baseURI);
                    lsInput.setByteStream(stream);
                }
                else
                {
                    LOG.error("Unable to determine schema for: {}", identifier);
                }
                return lsInput;
            });
            inputStreams.addAll(schemaLocationRegistry.getSchemaLocations().stream()
                            .map(schemaLocation -> widgetResourceReader.getResourceAsStream(schemaLocation)).collect(Collectors.toList()));
            final List<Source> streamSources = inputStreams.stream().map(StreamSource::new).collect(Collectors.toList());
            mergedSchema = schemaFactory.newSchema(streamSources.toArray(new Source[] {}));
        }
        catch(final SAXParseException e)
        {
            status = processParseException(e, "schemas");
        }
        catch(final SAXException e)
        {
            LOG.error("Errors occurred while loading schemas", e);
            if(e.getCause() instanceof SAXParseException)
            {
                status = processParseException((SAXParseException)e.getCause(), "schemas");
            }
            else
            {
                LOG.error(MESSAGE_ERROR_FILE_PROCESSING, e);
                status = SchemaValidationStatus.error(e.getCause().getLocalizedMessage());
            }
        }
        finally
        {
            inputStreams.forEach(IOUtils::closeQuietly);
        }
        if(status.isSuccess())
        {
            try
            {
                final XMLInputFactory xif = XMLInputFactory.newFactory();
                xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
                xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
                final Unmarshaller unmarshaller = context.createUnmarshaller();
                unmarshaller.setSchema(mergedSchema);
                final GenericValidationEventCollector validationEventCollector = new GenericValidationEventCollector();
                unmarshaller.setEventHandler(validationEventCollector);
                unmarshaller.unmarshal(xif.createXMLStreamReader(content));
                if(validationEventCollector.hasEvents())
                {
                    final boolean hasErrors = Arrays.stream(validationEventCollector.getEvents())
                                    .anyMatch(each -> each.getSeverity() > ValidationEvent.WARNING);
                    final String schemaValidationMessage = validationEventCollector.getSchemaValidationMessage();
                    if(hasErrors)
                    {
                        LOG.warn("Warnings occurred while processing configuration file:\n{}", schemaValidationMessage);
                    }
                    status = SchemaValidationStatus.warning(schemaValidationMessage);
                }
            }
            catch(final UnmarshalException e)
            {
                if(e.getCause() instanceof SAXParseException)
                {
                    status = processParseException((SAXParseException)e.getCause(), "configuration file");
                }
                else
                {
                    LOG.error(MESSAGE_ERROR_FILE_PROCESSING, e);
                    status = SchemaValidationStatus.error(e.getCause().getLocalizedMessage());
                }
            }
            catch(final Exception e)
            {
                LOG.error(MESSAGE_ERROR_FILE_PROCESSING, e);
                status = SchemaValidationStatus.error(e.getCause().getLocalizedMessage());
            }
            finally
            {
                inputStreams.forEach(IOUtils::closeQuietly);
            }
        }
        return status;
    }


    protected SchemaValidationStatus processParseException(final SAXParseException e, final String fileType)
    {
        final ValidationEventLocator locator = new ValidationEventLocatorImpl(e);
        final ValidationEvent event = new ValidationEventImpl(ValidationEvent.FATAL_ERROR, e.getLocalizedMessage(), locator, e);
        final String schemaValidationMessage = GenericValidationEventCollector.getSchemaValidationMessage(event);
        LOG.error("Errors occurred while parsing {}:\n{}", fileType, schemaValidationMessage);
        return SchemaValidationStatus.error(schemaValidationMessage);
    }


    @Required
    public void setSchemaLocationRegistry(final SchemaLocationRegistry schemaLocationRegistry)
    {
        this.schemaLocationRegistry = schemaLocationRegistry;
    }


    public void setWidgetResourceReader(final CockpitResourceLoader widgetResourceReader)
    {
        this.widgetResourceReader = widgetResourceReader;
    }
}
