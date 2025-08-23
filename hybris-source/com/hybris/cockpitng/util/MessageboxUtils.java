/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.zkoss.zul.Messagebox.Button;

public final class MessageboxUtils
{
    /**
     * Constant array of {@link Button#NO}, {@link Button#YES} buttons in the standard order.
     *
     * @see #order(Button...)
     * @see Button#NO
     * @see Button#YES
     */
    public static final Button[] NO_YES_OPTION = order(Button.NO, Button.YES);


    private MessageboxUtils()
    {
        // blocks the possibility of create a new instance
    }


    /**
     * <p>
     * Returns buttons in a standard order. The standard order is as follow:
     * </p>
     * <ol>
     * <li>{@link Button#NO}</li>
     * <li>{@link Button#CANCEL}</li>
     * <li>{@link Button#ABORT}</li>
     * <li>{@link Button#IGNORE}</li>
     * <li>{@link Button#RETRY}</li>
     * <li>{@link Button#YES}</li>
     * <li>{@link Button#OK}</li>
     * </ol>
     *
     * @param buttons
     *           the buttons to order.
     * @return an array with buttons in the standard order.
     */
    public static Button[] order(final Button... buttons)
    {
        final List<Button> list = new ArrayList<>(Arrays.asList(buttons));
        list.sort(Comparator.comparingInt(MessageboxUtils::getPosition));
        return list.toArray(new Button[0]);
    }


    static int getPosition(final Button button)
    {
        switch(button)
        {
            case NO:
                return 1;
            case CANCEL:
                return 2;
            case ABORT:
                return 3;
            case IGNORE:
                return 4;
            case RETRY:
                return 5;
            case YES:
                return 6;
            case OK:
                return 7;
            default:
                throw new UnsupportedOperationException(String.format("Button %s is unsupported!", button));
        }
    }
}
