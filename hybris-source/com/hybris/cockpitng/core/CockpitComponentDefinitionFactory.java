/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

/**
 * Factory responsible for creation of component definition based on component info object (as provided by component
 * loader).
 */
public interface CockpitComponentDefinitionFactory
{
    /**
     * <P>
     * Creates a component definition out of information about it. Information itself may be altered, if any new meta data
     * becomes available in process of creating component definition (yet none of already existing information should be
     * lost).
     * </P>
     * <P>
     * Definition may not be yet initialized. After all definitions are created, they should be additionally initialized by
     * calling factory's {@link #initialize(CockpitComponentInfo, AbstractCockpitComponentDefinition)} method.
     * </P>
     *
     * @param info
     *           information about component
     * @return definition created
     */
    AbstractCockpitComponentDefinition create(CockpitComponentInfo info);


    /**
     * <P>
     * Initializes component previously created by {@link #create(CockpitComponentInfo)} method.
     * </P>
     * <P>
     * Method should not be called before all required definitions were created.
     * </P>
     *
     * @param info
     *           information which was base for component to be initialized
     * @param definition
     *           component definition to be initialized
     */
    default void initialize(final CockpitComponentInfo info, final AbstractCockpitComponentDefinition definition)
    {
    }
}
