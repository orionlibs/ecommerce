/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapcustomerb2bbackoffice.widgets;

import com.hybris.cockpitng.util.DefaultWidgetController;
import com.sap.hybris.sapcustomerb2bbackoffice.services.Sapcustomerb2bbackofficeService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;

public class Sapcustomerb2bbackofficeController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    private Label label;
    @WireVariable
    private transient Sapcustomerb2bbackofficeService sapcustomerb2bbackofficeService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        label.setValue(sapcustomerb2bbackofficeService.getHello() + " Sapcustomerb2bbackofficeController");
    }
}
