/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapcreditcheckbackoffice.widgets;

import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.sap.sapcreditcheckbackoffice.services.SapcreditcheckbackofficeService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;

public class SapcreditcheckbackofficeController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    private Label label;
    @SuppressWarnings("squid:S1948")
    @WireVariable
    private SapcreditcheckbackofficeService sapcreditcheckbackofficeService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        label.setValue(sapcreditcheckbackofficeService.getHello() + " SapcreditcheckbackofficeController");
    }
}
