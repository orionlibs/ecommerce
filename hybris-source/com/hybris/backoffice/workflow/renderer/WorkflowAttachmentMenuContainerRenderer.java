/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer;

import com.hybris.cockpitng.common.renderer.AbstractCustomMenuActionRenderer;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import org.zkoss.zul.impl.XulElement;

public class WorkflowAttachmentMenuContainerRenderer
                extends AbstractCustomMenuActionRenderer<XulElement, ListColumn, WorkflowItemAttachmentModel>
{
    private static final String SCLASS_MENU_POPUP = "yw-workflows-menu-popup yw-pointer-menupopup yw-pointer-menupopup-top-right";


    @Override
    protected String getMenuPopupSclass()
    {
        return SCLASS_MENU_POPUP;
    }
}
