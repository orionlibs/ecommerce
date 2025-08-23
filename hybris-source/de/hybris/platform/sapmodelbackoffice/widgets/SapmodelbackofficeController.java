/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sapmodelbackoffice.widgets;

import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.sapmodelbackoffice.services.SapmodelbackofficeService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;

public class SapmodelbackofficeController extends DefaultWidgetController
{
    private Label label;
    @WireVariable
    private transient SapmodelbackofficeService sapmodelbackofficeService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        label.setValue(sapmodelbackofficeService.getHello() + " SapmodelbackofficeController");
    }
}
