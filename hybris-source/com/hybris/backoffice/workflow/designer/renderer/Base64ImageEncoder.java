/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Encodes given svg image as string to base64-encoded image declaration for HTML components
 */
public class Base64ImageEncoder
{
    public String encode(final String imageAsStringToEncode)
    {
        return "data:image/svg+xml;base64,"
                        + Base64.getEncoder().encodeToString(imageAsStringToEncode.getBytes(StandardCharsets.UTF_8));
    }
}
