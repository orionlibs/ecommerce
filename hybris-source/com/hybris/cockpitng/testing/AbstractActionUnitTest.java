/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.util.impl.ReflectionUtils;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.engine.impl.ComponentWidgetAdapter;
import com.hybris.cockpitng.testing.util.internal.TestUtilInternal;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractActionUnitTest<T extends CockpitAction<?, ?>>
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractActionUnitTest.class);
    private static final ActionContext NULL_ACTION_CONTEXT = new ActionContext(null, null, Collections.emptyMap(),
                    Collections.emptyMap());
    @Mock
    protected ComponentWidgetAdapter componentWidgetAdapter;


    public abstract T getActionInstance();


    @Before
    public void setUpBaseActionUnitTest()
    {
        MockitoAnnotations.initMocks(this);
        if(getActionInstance() instanceof AbstractComponentWidgetAdapterAware)
        {
            ((AbstractComponentWidgetAdapterAware)getActionInstance()).initialize(componentWidgetAdapter, StringUtils.EMPTY);
        }
    }


    @Test
    public void testDefaultOrNoParameterConstructor()
    {
        Assertions.assertThat(TestUtilInternal.hasDefaultOrNoParameterConstructor(getActionType())).isTrue();
    }


    @Test
    public void testNullSafeAction()
    {
        if(TestUtilInternal.isNullsafe(this.getClass()))
        {
            if(this.getActionInstance().canPerform(NULL_ACTION_CONTEXT))
            {
                this.getActionInstance().perform(NULL_ACTION_CONTEXT);
            }
            if(this.getActionInstance().needsConfirmation(NULL_ACTION_CONTEXT))
            {
                try
                {
                    this.getActionInstance().getConfirmationMessage(NULL_ACTION_CONTEXT);
                }
                catch(final IllegalArgumentException iae)
                {
                    // NOOP some validation might have happened
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Error occurred", iae);
                    }
                }
            }
        }
    }


    protected Class<? extends T> getActionType()
    {
        return ReflectionUtils.extractGenericParameterType(getClass(), 0);
    }
}
