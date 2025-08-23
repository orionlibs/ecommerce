/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import java.util.Map;

/**
 * An interface that defines contract for performing the custom logic for the configurable flow widget. It is used
 * together with {@link com.hybris.cockpitng.config.jaxb.wizard.CustomType} in the navigation section for the
 * aforementioned widget.
 * Note: Here is an example:
 *
 * <pre>
 *     {@code <navigation id="stepId">
 *	 	<custom align="left" handler="spring-bean-id" label="custom"/>
 *	  </navigation>
 *     }
 * </pre>
 *
 * Note: handler="spring-bean-id" refers to the bean defined in the spring application context and it is of type
 * {@link FlowActionHandler}
 */
public interface FlowActionHandler
{
    /**
     * Method that is invoked when the custom button in the configurable flow widget is clicked - this method should
     * contain custom logic.
     *
     * @param customType given step configuration
     * @param adapter adapter that controls the configurable widget flow
     * @param parameters current parameters
     */
    void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters);
}
