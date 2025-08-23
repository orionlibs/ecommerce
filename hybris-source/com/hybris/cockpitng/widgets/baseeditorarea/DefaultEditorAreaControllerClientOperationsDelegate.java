/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import org.zkoss.zk.ui.util.Clients;

public class DefaultEditorAreaControllerClientOperationsDelegate
{
    private DefaultEditorAreaControllerClientOperationsDelegate()
    {
        // blocks the possibility of create a new instance
    }


    public static void fireAttributesDivWidthRequest(final String uuid)
    {
        final String js = "if($(\"#" + uuid + "\").is(\":visible\")) {" + //
                        "var _w = $(\"#" + uuid + "\").width();" + //
                        "if($(\"#" + uuid + " .yw-editorarea-tabbox-tabpanels\").first().index() >= 0) {" + //
                        "_w = $(\"#" + uuid + " .yw-editorarea-tabbox-tabpanels\").first().width();" + //
                        "}" + //
                        "CockpitNG.sendEvent(\"#" + uuid + "\",\"" + DefaultEditorAreaController.EDITOR_AREA_WIDTH_REQUEST
                        + "\",Math.round(_w));" + "}";
        Clients.evalJavaScript(js);
    }
}
