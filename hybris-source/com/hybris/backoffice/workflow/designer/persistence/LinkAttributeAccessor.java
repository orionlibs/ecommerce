/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Accessor for {@link LinkModel}'s attributes
 */
public class LinkAttributeAccessor
{
    private static final Logger LOG = LoggerFactory.getLogger(LinkAttributeAccessor.class);
    public static final String AND_CONNECTION_TEMPLATE_PROPERTY = "andConnectionTemplate";
    public static final String AND_CONNECTION_PROPERTY = "andConnection";


    private LinkAttributeAccessor()
    {
        throw new AssertionError("This class should not be instantiated");
    }


    /**
     * Retrieves {@link #AND_CONNECTION_PROPERTY} or {@link #AND_CONNECTION_TEMPLATE_PROPERTY} from {@link LinkModel}
     *
     * @param linkModel
     *           model with and connection attribute
     * @return and connection attribute value or false when model is not saved
     */
    public static boolean getAndConnectionAttribute(final LinkModel linkModel)
    {
        return getBooleanAttribute(linkModel, AND_CONNECTION_TEMPLATE_PROPERTY)
                        || getBooleanAttribute(linkModel, AND_CONNECTION_PROPERTY);
    }


    private static boolean getBooleanAttribute(final LinkModel linkModel, final String attribute)
    {
        return getAttribute(linkModel, attribute, Boolean.class).orElse(false);
    }


    /**
     * Retrieves attribute value from {@link LinkModel}
     *
     * @param linkModel
     *           model with attributes
     * @param attributeQualifier
     *           identifier of the attribute
     * @param attributeType
     *           type of the attribute (the value will be casted)
     * @param <T>
     *           generic type that represents attribute type
     * @return casted attribute value or empty if the attribute does not exist or cannot be casted
     */
    public static <T> Optional<T> getAttribute(final LinkModel linkModel, final String attributeQualifier,
                    final Class<T> attributeType)
    {
        final ItemModelContext context = linkModel.getItemModelContext();
        if(context == null)
        {
            return Optional.empty();
        }
        if(context.isNew())
        {
            return Optional.empty();
        }
        final Link link = (Link)context.getSource();
        try
        {
            return Optional.ofNullable(link.getAttribute(attributeQualifier)).filter(attributeType::isInstance)
                            .map(attributeType::cast);
        }
        catch(final JaloSecurityException | JaloSystemException e)
        {
            LOG.debug(String.format("Could not get attribute %s for type %s", attributeQualifier, attributeType.getCanonicalName()),
                            e);
            return Optional.empty();
        }
    }
}
