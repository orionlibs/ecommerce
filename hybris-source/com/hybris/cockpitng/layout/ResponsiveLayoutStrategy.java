/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.layout;

import java.util.Map;
import org.zkoss.zk.ui.event.ClientInfoEvent;

/**
 * Classes implementing this interface may be used to make decisions about the need of saving horizontal/vertical space. The
 * decision should be based on current screen data (resolution) and current Desktop's size.
 *
 * @see org.zkoss.zk.ui.Desktop
 */
public interface ResponsiveLayoutStrategy
{
    /**
     * @param info    current screen data
     * @param context evaluation context with optional data that may be used by the strategy to compute the answer. By design it
     *                   may be the claaing widget's settings.
     * @return true if the calling widget should consider saving some horizontal space
     */
    boolean isSaveSpaceHorizontally(final ClientInfoEvent info, final Map<String, Object> context);


    /**
     * @param info    current screen data
     * @param context evaluation context with optional data that may be used by the strategy to compute the answer. By design it
     *                   may be the claaing widget's settings.
     * @return true if the calling widget should consider saving some vertical space
     */
    boolean isSaveSpaceVertically(final ClientInfoEvent info, final Map<String, Object> context);
}
