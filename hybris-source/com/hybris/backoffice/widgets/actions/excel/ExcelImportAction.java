/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.excel;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;

public class ExcelImportAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<String, String>
{
    protected static final String SOCKET_OUT_TYPE_CODE = "typeCode";
    @Resource
    private TypeService typeService;


    @Override
    public ActionResult<String> perform(final ActionContext<String> ctx)
    {
        final ActionResult<String> result = new ActionResult<>(ActionResult.ERROR);
        if(ctx != null && StringUtils.isNotEmpty(ctx.getData()))
        {
            sendOutput(SOCKET_OUT_TYPE_CODE, ctx.getData());
            result.setData(ctx.getData());
            result.setResultCode(ActionResult.SUCCESS);
        }
        return result;
    }


    @Override
    public boolean canPerform(final ActionContext<String> ctx)
    {
        return StringUtils.isNotEmpty(ctx.getData()) && typeService.isAssignableFrom(ItemModel._TYPECODE, ctx.getData());
    }


    public TypeService getTypeService()
    {
        return typeService;
    }
}
