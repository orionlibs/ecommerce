/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.attributechooser;

import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Config for attributes chooser.
 */
public class AttributesChooserConfig extends DefaultCockpitContext
{
    private boolean includeAllSupported;
    private String uniqueModelPrefix;
    private boolean openFirstMultiChildNode;
    private String emptyAttributesMessage;


    public boolean isIncludeAllSupported()
    {
        return includeAllSupported;
    }


    public void setIncludeAllSupported(final boolean includeAllSupported)
    {
        this.includeAllSupported = includeAllSupported;
    }


    public String getUniqueModelPrefix()
    {
        return uniqueModelPrefix == null ? StringUtils.EMPTY : uniqueModelPrefix;
    }


    public void setUniqueModelPrefix(final String uniqueModelPrefix)
    {
        this.uniqueModelPrefix = uniqueModelPrefix;
    }


    public boolean isOpenFirstMultiChildNode()
    {
        return openFirstMultiChildNode;
    }


    public void setOpenFirstMultiChildNode(final boolean openFirstMultiChildNode)
    {
        this.openFirstMultiChildNode = openFirstMultiChildNode;
    }


    public String getEmptyAttributesMessage()
    {
        return emptyAttributesMessage;
    }


    public void setEmptyAttributesMessage(final String emptyAttributesMessage)
    {
        this.emptyAttributesMessage = emptyAttributesMessage;
    }
}
