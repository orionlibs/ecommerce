/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.ssc;

import com.sap.custdev.projects.fbs.slc.cfg.client.ITextDescription;
import de.hybris.platform.sap.productconfig.runtime.interf.TextConverterBase;

/**
 * Text Converter class for converting SSC/backend texts from ITF format into plain text format to be used within
 * hybris.
 */
public interface TextConverter extends TextConverterBase
{
    /**
     * Converts the text array we got from SSC into a text. Also takes care of different sections of the text in the
     * modeling environment. Removes all meta text elements (bold, underline, etc.)
     *
     * @param textDescriptionArray
     * @return Dependency text
     */
    String convertDependencyText(ITextDescription[] textDescriptionArray);
}
