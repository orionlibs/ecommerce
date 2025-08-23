/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.itemcomments;

public enum PopupPosition
{
    BEFORE_START("before_start"), BEFORE_END("before_end"), END_BEFORE("end_before"), END_AFTER("end_after"), AFTER_END(
                "after_end"), AFTER_START("after_start"), START_AFTER("start_after"), START_BEFORE("start_before"), OVERLAP(
                "overlap"), OVERLAP_END("overlap_end"), OVERLAP_BEFORE(
                "overlap_before"), OVERLAP_AFTER("overlap_after"), AT_POINTER("at_pointer"), AFTER_POINTER("after_pointer");
    private String position;


    PopupPosition(final String position)
    {
        this.position = position;
    }


    public String getPosition()
    {
        return position;
    }


    public static PopupPosition forName(final String position)
    {
        return PopupPosition.valueOf(position.toUpperCase());
    }
}
