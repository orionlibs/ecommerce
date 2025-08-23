/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.permissionmanagement;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class PermissionManagementGroupController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(PermissionManagementGroupController.class);
    protected static final String SOCKET_IN_INPUTOBJECT = "inputObjectInput";
    protected static final String SOCKET_OUT_OBJECTFORWARD = "inputObjectInputForward";
    @WireVariable
    private transient LabelService labelService;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient CockpitLocaleService cockpitLocaleService;


    @SocketEvent(socketId = "inputObjectInput")
    public void showPermissionsForInputObject(final Object input)
    {
        final String code = getTypeFacade().getType(input);
        if(StringUtils.isNotEmpty(code))
        {
            try
            {
                final DataType dataType = getTypeFacade().load(code);
                final Locale locale = cockpitLocaleService.getCurrentLocale();
                final String type = dataType.getLabel(locale);
                final String label = labelService.getObjectLabel(input);
                updateWidgetTitle(type, label);
            }
            catch(final TypeNotFoundException e)
            {
                LOG.error("Unknown type for input object.", e);
            }
        }
    }


    protected void updateWidgetTitle(final String type, final String label)
    {
        if(label != null)
        {
            final String lbl = getLabel("permissionmanagement.title", new Object[]
                            {type, label});
            if(lbl != null)
            {
                getWidgetInstanceManager().setTitle(lbl);
            }
            else
            {
                getWidgetInstanceManager().setTitle(String.format("Permission Management - %s (%s)", type, label));
            }
        }
    }


    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }
}
