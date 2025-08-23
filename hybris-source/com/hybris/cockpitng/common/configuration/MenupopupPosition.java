/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.configuration;

public enum MenupopupPosition
{
    START_BEFORE("start_before"),
    BEFORE_START("before_start"),
    TOP_LEFT("top_left"),
    BEFORE_CENTER("before_center"),
    TOP_CENTER("top_center"),
    BEFORE_END("before_end"),
    TOP_RIGHT("top_right"),
    END_BEFORE("end_before"),
    START_CENTER("start_center"),
    MIDDLE_LEFT("middle_left"),
    MIDDLE_CENTER("middle_center"),
    MIDDLE_RIGHT("middle_right"),
    END_CENTER("end_center"),
    START_AFTER("start_after"),
    BOTTOM_LEFT("bottom_left"),
    AFTER_START("after_start"),
    BOTTOM_CENTER("bottom_center"),
    AFTER_CENTER("after_center"),
    BOTTOM_RIGHT("bottom_right"),
    END_AFTER("end_after"),
    AFTER_END("after_end");
    private String name;


    MenupopupPosition(final String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return name;
    }
}
