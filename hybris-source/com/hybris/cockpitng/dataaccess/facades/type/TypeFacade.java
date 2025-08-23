/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.type;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.Locale;
import org.slf4j.LoggerFactory;

/**
 * Facade for loading types.
 */
public interface TypeFacade
{
    /**
     * Convenience method, calls <b> load(qualifier, null)</b>
     * See {@link #load(String, Context)}.
     *
     * @param qualifier
     *           A unique qualifier identifying the type.
     * @return The type instance for given id loaded with attributes defined in context.
     * @throws TypeNotFoundException
     *            if there is no type available for the given code.
     */
    DataType load(String qualifier) throws TypeNotFoundException;


    /**
     * Loads a {@link DataType} for the given code, if it exists.
     *
     * @param qualifier
     *           A unique qualifier identifying the type.
     * @param ctx
     *           Context specifying the loaded attributes of the returned type as well as application settings.
     * @return The type instance for given id loaded with attributes defined in context.
     * @throws TypeNotFoundException
     *            if there is no type available for the given code.
     */
    DataType load(String qualifier, Context ctx) throws TypeNotFoundException;


    /**
     * Gets type of given object
     *
     * @param object
     *           a object for which type qualifier should be returned
     * @return type qualifier for the given object. If there is no custom type system or the object doesn't belong to it, it
     *         returns the java class name.
     */
    String getType(Object object);


    /**
     * Gets a attribute of given object for given qualifier.
     * <P>
     * As a qualifier may be given any expression compatible with SpEL specification, yet more sophisticated expressions may
     * strongly affect performance. If expression may not be interpreted as item's attribute, a expressions value class is
     * returned.
     * </P>
     *
     * @param typeCode
     *           code of the type for which the attribute's should be fetched
     * @param attributeQualifier
     *           attribute qualifier or SpEL
     * @return evaluation's result - attribute which is pointed by an expression or <code>null</code> if expression does not
     *         point an attribute straightforward or is outside of platform domain
     */
    default DataAttribute getAttribute(final String typeCode, final String attributeQualifier)
    {
        try
        {
            final DataType dataType = load(typeCode);
            return dataType.getAttribute(attributeQualifier);
        }
        catch(final TypeNotFoundException ignore)
        {
            LoggerFactory.getLogger(DataAttribute.class).debug("Type not found", ignore);
            return null;
        }
    }


    /**
     * Gets a attribute of given object for given qualifier.
     * <P>
     * As a qualifier may be given any expression compatible with SpEL specification, yet more sophisticated expressions may
     * strongly affect performance. If expression may not be interpreted as item's attribute, a expressions value class is
     * returned.
     * </P>
     *
     * @param object
     *           object, which attribute is to be extracted
     * @param attributeQualifier
     *           attribute qualifier or SpEL
     * @return evaluation's result - attribute which is pointed by an expression or <code>null</code> if expression does not
     *         point an attribute straightforward or is outside of platform domain
     */
    default DataAttribute getAttribute(final Object object, final String attributeQualifier)
    {
        final String type = getType(object);
        return getAttribute(type, attributeQualifier);
    }


    /**
     * Gets attribute description for the given attribute
     *
     * @param type
     *           code of the type for which the attribute's description should be fetched
     * @param attribute
     *           qualifier of the attribute for which the description should be fetched
     * @return description of the attribute
     */
    String getAttributeDescription(String type, String attribute);


    /**
     * Gets attribute description for the given attribute
     *
     * @param type
     *           code of the type for which the attribute's description should be fetched
     * @param attribute
     *           qualifier of the attribute for which the description should be fetched
     * @param locale
     *           prefered locale - if available the description should be returned in the prefered locale
     * @return description of the attribute
     */
    String getAttributeDescription(String type, String attribute, Locale locale);
}
