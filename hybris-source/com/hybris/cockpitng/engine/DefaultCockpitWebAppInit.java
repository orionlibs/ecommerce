/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;
import org.zkoss.zul.Messagebox;

public class DefaultCockpitWebAppInit implements WebAppInit
{
    @Override
    public void init(final WebApp wapp) throws Exception
    {
        Messagebox.setTemplate("/cng/messagebox.zul");
    }
}
