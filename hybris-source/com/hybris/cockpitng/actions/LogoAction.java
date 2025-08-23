/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;

public class LogoAction extends DefaultActionRenderer<Object, Object> implements CockpitAction<Object, Object>
{
    public static final String DEFAULT_HYPERLINK = "https://cx.sap.com";
    /**
     * @see #DEFAULT_HYPERLINK
     * @deprecated since 1905
     */
    @Deprecated(since = "1905", forRemoval = true)
    public static final String DEFAULT_HYPERLINK_HYBRIS_COM = DEFAULT_HYPERLINK;
    public static final String DEFAULT_TARGET_BLANK = "_blank";
    public static final String DEFAULT_LOGO_BUTTON_S_CLASS = "yw-logo-button";
    public static final String DEFAULT_IMAGE_URL = "icons/headline_ycon.png";
    public static final String DEFAULT_HOVER_IMAGE_URL = "icons/headline_ycon_hover.png";
    public static final String PARAM_IMAGE_URL = "imageUrl";
    public static final String PARAM_HOVER_IMAGE_URL = "hoverImageUrl";
    public static final String PARAM_HYPERLINK = "hyperlink";
    public static final String PARAM_TARGET = "target";
    public static final String PARAM_BUTTON_SCLASS = "buttonsclass";


    /**
     * Render the logo button. There's a possibility to specify an imageUrl, hyperlink, target, and a buttonsclass.
     * If you don't specify any of these parameters in the cockpit-config.xml some default-values will be set.
     */
    @Override
    public void render(final Component parent, final CockpitAction<Object, Object> action, final ActionContext<Object> context,
                    final boolean updateMode, final ActionListener<Object> listener)
    {
        final Button logo = new Button();
        final Object imageUrl = context.getParameter(PARAM_IMAGE_URL);
        final Object hoverImageUrl = context.getParameter(PARAM_HOVER_IMAGE_URL);
        final Object hyperlink = context.getParameter(PARAM_HYPERLINK);
        final Object target = context.getParameter(PARAM_TARGET);
        final Object buttonSClass = context.getParameter(PARAM_BUTTON_SCLASS);
        if(hyperlink instanceof String)
        {
            logo.setHref((String)hyperlink);
        }
        else
        {
            logo.setHref(DEFAULT_HYPERLINK);
        }
        if(imageUrl instanceof String && StringUtils.isNotEmpty((String)imageUrl))
        {
            logo.setImage((String)imageUrl);
            if(hoverImageUrl instanceof String && StringUtils.isNotBlank((String)hoverImageUrl))
            {
                logo.setHoverImage((String)hoverImageUrl);
            }
        }
        else
        {
            logo.setImage(context.getParameter(CockpitWidgetEngine.COMPONENT_ROOT_PARAM) + "/" + DEFAULT_IMAGE_URL);
            logo.setHoverImage(context.getParameter(CockpitWidgetEngine.COMPONENT_ROOT_PARAM) + "/" + DEFAULT_HOVER_IMAGE_URL);
        }
        if(target instanceof String)
        {
            logo.setTarget((String)target);
        }
        else
        {
            logo.setTarget(DEFAULT_TARGET_BLANK);
        }
        if(buttonSClass instanceof String && StringUtils.isNotEmpty((String)buttonSClass))
        {
            final String sClass = (String)buttonSClass;
            logo.setSclass(sClass);
            if(!DEFAULT_LOGO_BUTTON_S_CLASS.equals(sClass))
            {
                UITools.modifySClass(logo, DEFAULT_LOGO_BUTTON_S_CLASS, true);
            }
        }
        else
        {
            logo.setSclass(DEFAULT_LOGO_BUTTON_S_CLASS);
        }
        logo.setParent(parent);
    }


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        return null;
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        return true;
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Object> ctx)
    {
        return false;
    }


    @Override
    public String getConfirmationMessage(final ActionContext<Object> ctx)
    {
        throw new UnsupportedOperationException("No confirmation is needed! This method should not be called.");
    }
}
