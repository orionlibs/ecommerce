/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.devtools;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.modules.HotDeploymentService;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Executions;

public class RedeployAction extends DevToolbarAction<Object, Object>
{
    private static final Logger LOG = Logger.getLogger(RedeployAction.class.getName());
    @Resource
    private HotDeploymentService hotDeploymentService;


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        LOG.info("Performing hot deploy");
        hotDeploymentService.hotDeploy();
        Executions.sendRedirect(null);
        return new ActionResult<>(ActionResult.SUCCESS);
    }
}
