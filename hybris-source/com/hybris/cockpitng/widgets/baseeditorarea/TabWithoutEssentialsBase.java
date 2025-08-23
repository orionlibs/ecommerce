/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Essentials;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab;
import java.math.BigInteger;
import java.util.List;

/**
 * Tab without essentials implementation.
 */
public class TabWithoutEssentialsBase extends Tab
{
    private final Tab target;


    TabWithoutEssentialsBase(final Tab target)
    {
        this.target = target;
        TabWithoutEssentialsBase.this.setPosition(getIncrementedPosition(target.getPosition()));
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


    public static BigInteger getIncrementedPosition(final BigInteger position)
    {
        return position != null ? BigInteger.valueOf(position.longValue() + 1) : null;
    }
}
