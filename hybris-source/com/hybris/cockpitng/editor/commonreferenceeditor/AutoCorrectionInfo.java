/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

/**
 * Marker class to be used to recognize list entry that is used to show auto correction.
 */
public class AutoCorrectionInfo
{
    private final String autoCorrection;


    public AutoCorrectionInfo(final String autoCorrection)
    {
        this.autoCorrection = autoCorrection;
    }


    public String getValue()
    {
        return autoCorrection;
    }
}
