/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.cps;

/**
 * Parses conflict texts from configuration engine into its display ready form
 */
public interface CPSConflictTextParser
{
    /**
     * Parses conflict texts from CPS service
     *
     * @param rawText
     *           Text from CPS service
     * @return Parsed text. Is returned as empty string in case input is null
     */
    String parseConflictText(final String rawText);
}
