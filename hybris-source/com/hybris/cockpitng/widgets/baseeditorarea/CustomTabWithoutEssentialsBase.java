/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Essentials;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Parameter;
import java.math.BigInteger;
import java.util.List;

/**
 * CustomTab without essentials implementation.
 */
public class CustomTabWithoutEssentialsBase extends CustomTab
{
    private final CustomTab target;


    CustomTabWithoutEssentialsBase(final CustomTab target)
    {
        this.target = target;
        CustomTabWithoutEssentialsBase.this.setPosition(getIncrementedPosition(target.getPosition()));
    }


    @Override
    public BigInteger getPosition()
    {
        // explicitly override for compatibility tests
        return this.position;
    }


    @Override
    public boolean isInitiallyOpened()
    {
        // explicitly override for compatibility tests
        return target.isInitiallyOpened();
    }


    @Override
    public boolean isDisplayEssentialSectionIfPresent()
    {
        return false;
    }


    @Override
    public Essentials getEssentials()
    {
        return null;
    }


    @Override
    public List<AbstractSection> getCustomSectionOrSection()
    {
        return target.getCustomSectionOrSection();
    }


    @Override
    public String getName()
    {
        return target.getName();
    }


    @Override
    public String getMergeMode()
    {
        return target.getMergeMode();
    }


    @Override
    public String getTooltipText()
    {
        return target.getTooltipText();
    }


    @Override
    public List<Parameter> getRenderParameter()
    {
        return target.getRenderParameter();
    }


    @Override
    public String getClazz()
    {
        return target.getClazz();
    }


    @Override
    public String getSpringBean()
    {
        return target.getSpringBean();
    }


    public static BigInteger getIncrementedPosition(final BigInteger position)
    {
        return position != null ? BigInteger.valueOf(position.longValue() + 1) : null;
    }
}
