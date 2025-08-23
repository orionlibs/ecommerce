/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.devtools;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import java.util.logging.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

public class ReloadLabelsAction extends DevToolbarAction<Object, Object>
{
    private static final Logger LOG = Logger.getLogger(ReloadLabelsAction.class.getName());


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        LOG.info("Reloading labels");
        Labels.reset();
        Executions.sendRedirect(null);
        return new ActionResult<>(ActionResult.SUCCESS);
    }
}
