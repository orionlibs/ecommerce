/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.devtools;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;
import com.hybris.cockpitng.core.persistence.impl.XMLWidgetPersistenceService;
import java.util.logging.Logger;
import javax.annotation.Resource;

public class ResetEverythingAction extends DevToolbarAction<Object, Object>
{
    private static final Logger LOG = Logger.getLogger(ResetEverythingAction.class.getName());
    @Resource
    private DefaultCockpitConfigurationService cockpitConfigurationService;
    @Resource
    private CockpitAdminService cockpitAdminService;
    @Resource
    private XMLWidgetPersistenceService widgetPersistenceService;


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        LOG.info("Performing reset everything");
        widgetPersistenceService.resetToDefaults();
        cockpitConfigurationService.resetToDefaults();
        cockpitAdminService.refreshCockpit();
        return new ActionResult<>(ActionResult.SUCCESS);
    }
}
