/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.splitlayoutchooser;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Popup;

/**
 * Renderer class responsible for displaying popup with available split layout's compositions.
 */
public class SplitLayoutChooserActionRenderer extends DefaultActionRenderer<String, String>
{
    protected static final String LAYOUTS_KEY = "layouts";
    protected static final String DEFAULT_LAYOUT_KEY = "defaultLayout";
    protected static final String LABEL_PREFIX = "com.hybris.cockpitng.action.splitlayoutchooser.";
    protected static final String CSS_CHOOSER_BUTTON = "yw-split-layout-chooser-button";
    protected static final String CSS_CHOOSER_BUTTON_WITH_LABEL = "yw-split-layout-chooser-button-with-label";
    protected static final String CSS_CHOOSER_POPUP = "yw-split-layout-chooser-popup";
    protected static final String ICON_URI_KEY = "iconUri";
    protected static final String DEFAULT_LAYOUT = "triple";
    protected static final String DEFAULT_FOUNT_ICON_CLASS = "cng-action-icon cng-font-icon";
    protected static final String FONT_ICON_PREFIX = "font-icon--";


    @Override
    public void render(final Component parent, final CockpitAction<String, String> action, final ActionContext<String> context,
                    final boolean updateMode, final ActionListener<String> listener)
    {
        final String chosenLayout = findChosenLayout(context);
        context.setParameter(ICON_URI_KEY, chosenLayout);
        super.render(parent, action, context, updateMode, listener);
        Component actionContainer = parent.getLastChild();
        Button currentLayout = getCurrentLayoutBtn(actionContainer);
        updateChosenLayoutButton(currentLayout, chosenLayout);
        registerHoverEventsOnContainer((HtmlBasedComponent)actionContainer, currentLayout, action.canPerform(context));
        addEventListener(actionContainer, createEventListener(action, context, listener));
    }


    protected String findChosenLayout(final ActionContext<String> context)
    {
        final String chosenLayoutFromActionsProperty = context.getData();
        if(StringUtils.isNotBlank(chosenLayoutFromActionsProperty))
        {
            return chosenLayoutFromActionsProperty;
        }
        String chosenLayout = loadFromParentModel(context, SplitLayoutChooserAction.SELECTED_LAYOUT_KEY, String.class);
        if(StringUtils.isBlank(chosenLayout))
        {
            chosenLayout = (String)context.getParameter(DEFAULT_LAYOUT_KEY);
            if(StringUtils.isBlank(chosenLayout))
            {
                chosenLayout = DEFAULT_LAYOUT;
            }
        }
        return chosenLayout;
    }


    @Override
    public EventListener<? extends Event> createEventListener(final CockpitAction<String, String> action,
                    final ActionContext<String> context, final ActionListener<String> actionListener)
    {
        return event -> renderPopup((HtmlBasedComponent)event.getTarget(), action, context, actionListener);
    }


    protected void renderPopup(final HtmlBasedComponent target, final CockpitAction<String, String> action,
                    final ActionContext<String> context, final ActionListener<String> listener)
    {
        final Popup popup = new Popup();
        popup.setSclass(CSS_CHOOSER_POPUP);
        final Component popupParent;
        popupParent = (target instanceof Button) ? target.getParent() : target;
        popupParent.appendChild(popup);
        for(final String layoutName : findAvailableLayouts(context))
        {
            final Button layoutButton = createButton(layoutName, context);
            popup.appendChild(layoutButton);
            layoutButton.addEventListener(Events.ON_CLICK, event -> {
                updateChosenLayoutButton(getCurrentLayoutBtn(popupParent), layoutName);
                context.setParameter(SplitLayoutChooserAction.SELECTED_LAYOUT_KEY, layoutName);
                storeInParentModel(layoutName, context, SplitLayoutChooserAction.SELECTED_LAYOUT_KEY);
                perform(action, context, listener);
                popup.close();
                popupParent.getChildren().remove(popup);
            });
        }
        popup.open(popupParent, "overlap");
    }


    protected Button getCurrentLayoutBtn(Component component)
    {
        Button currentLayout = null;
        if(component instanceof Button)
        {
            currentLayout = (Button)component;
        }
        else
        {
            final List<Component> children = component.getChildren();
            for(final Component child : children)
            {
                if(child instanceof Button)
                {
                    currentLayout = (Button)child;
                    break;
                }
            }
        }
        if(Objects.isNull(currentLayout))
        {
            currentLayout = new Button();
            component.appendChild(currentLayout);
        }
        return currentLayout;
    }


    protected void updateChosenLayoutButton(Button currentLayout, final String layoutName)
    {
        if(Objects.nonNull(currentLayout))
        {
            currentLayout.setSclass(DEFAULT_FOUNT_ICON_CLASS);
            currentLayout.addSclass(FONT_ICON_PREFIX + layoutName);
        }
    }


    protected Button createButton(final String layoutName, final ActionContext<String> context)
    {
        final Button layoutButton = new Button();
        layoutButton.setSclass(DEFAULT_FOUNT_ICON_CLASS + " " + CSS_CHOOSER_BUTTON);
        layoutButton.addSclass(FONT_ICON_PREFIX + layoutName);
        YTestTools.modifyYTestId(layoutButton, layoutName);
        if(context.isShowText())
        {
            UITools.modifySClass(layoutButton, CSS_CHOOSER_BUTTON_WITH_LABEL, true);
            layoutButton.setLabel(findLayoutLabel(layoutName, context));
        }
        return layoutButton;
    }


    protected String findLayoutLabel(final String layoutName, final ActionContext<String> context)
    {
        return context.getLabel(String.format("%s%s", LABEL_PREFIX, layoutName));
    }


    protected List<String> findAvailableLayouts(final ActionContext<String> context)
    {
        String availableLayouts = (String)context.getParameter(LAYOUTS_KEY);
        if(StringUtils.isBlank(availableLayouts))
        {
            availableLayouts = findChosenLayout(context);
        }
        return Arrays.stream(availableLayouts.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
