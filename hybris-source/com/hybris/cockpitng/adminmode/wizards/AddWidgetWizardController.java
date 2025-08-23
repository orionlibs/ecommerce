/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode.wizards;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import com.hybris.cockpitng.util.WidgetUtils;
import com.hybris.cockpitng.util.YTestTools;
import java.io.IOException;
import java.text.Collator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

public class AddWidgetWizardController extends ViewAnnotationAwareComposer
{
    private static final long serialVersionUID = -4402456932679596003L;
    private static final transient Comparator<WidgetDefinition> WIDGET_DEFINITION_COMPARATOR = new Comparator<WidgetDefinition>()
    {
        @Override
        public int compare(final WidgetDefinition widef1, final WidgetDefinition widef2)
        {
            if(widef1 != null && widef2 != null)
            {
                String widget1 = widef1.getName();
                if(StringUtils.isEmpty(widget1))
                {
                    widget1 = widef1.getCode();
                }
                String widget2 = widef2.getName();
                if(StringUtils.isEmpty(widget2))
                {
                    widget2 = widef2.getCode();
                }
                return widget1.compareTo(widget2);
            }
            else
            {
                return widef1 == null ? -1 : 1;
            }
        }
    };
    private final transient ListitemRenderer<?> categoryListitemRenderer = new ListitemRenderer<Object>()
    {
        @Override
        public void render(final Listitem item, final Object data, final int index) throws Exception
        {
            final Listcell cell = new Listcell();
            cell.setSclass("widgetCell");
            item.appendChild(cell);
            final Div cntDiv = new Div();
            cntDiv.setSclass("z-panel-header");
            cell.appendChild(cntDiv);
            final Div nameDiv = new Div();
            final String groupName = String.valueOf(data);
            YTestTools.modifyYTestId(nameDiv, prepareYTestId(groupName));
            nameDiv.setSclass("widgetName");
            nameDiv.appendChild(new Label(groupName + " (" + widgetCategories.get(data).size() + ")"));
            item.setValue(data);
            cntDiv.appendChild(nameDiv);
        }
    };
    private final transient ListitemRenderer<?> widgetListitemRenderer = new ListitemRenderer<Object>()
    {
        @Override
        public void render(final Listitem item, final Object data, final int index) throws Exception
        {
            final WidgetDefinition model = ((WidgetDefinition)data);
            final Listcell cell = new Listcell();
            cell.setSclass("widgetCell");
            item.appendChild(cell);
            final Div cntDiv = new Div();
            cntDiv.setSclass("z-panel-header");
            cell.appendChild(cntDiv);
            final Div fallbackImg = new Div();
            fallbackImg.setSclass("widgetFallbackImg");
            final Div img = new Div();
            String locationPath = model.getLocationPath();
            if(StringUtils.startsWith(locationPath, "/"))
            {
                locationPath = locationPath.substring(1);
            }
            img.setStyle("background: url('" + locationPath + "/favicon.gif" + "')");
            fallbackImg.appendChild(img);
            final Hbox hbox = new Hbox();
            cntDiv.appendChild(hbox);
            hbox.appendChild(fallbackImg);
            final String modelName = model.getName();
            final Div nameDiv = new Div();
            if(modelName != null)
            {
                YTestTools.modifyYTestId(nameDiv, prepareYTestId(modelName));
            }
            nameDiv.setSclass("widgetName");
            nameDiv.appendChild(new Label(modelName));
            cell.setTooltiptext(model.getDescription());
            item.setValue(data);
            hbox.appendChild(nameDiv);
        }
    };
    private transient CockpitComponentDefinitionService widgetDefinitionService;
    private transient CockpitAdminService cockpitAdminService;
    private transient WidgetUtils widgetUtils;
    private transient WidgetLibUtils widgetLibUtils;
    private Button backButton2;
    private Listbox categoryListbox;
    private Listbox widgetListbox;
    private Div widgetDescriptionContainer;
    private Div mainContainer;
    private String filter = StringUtils.EMPTY;
    private transient Map<String, List<WidgetDefinition>> widgetCategories;


    private String prepareYTestId(final String name)
    {
        return "addWidgetWizard_" + Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}\\s]+", "");
    }


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        reloadCategories();
        categoryListbox.setItemRenderer(categoryListitemRenderer);
        widgetListbox.setItemRenderer(widgetListitemRenderer);
    }


    @ViewEvent(eventName = Events.ON_SELECT, componentID = "categoryListbox")
    public void categorySelected()
    {
        if(categoryListbox.getSelectedItem() != null)
        {
            final SimpleListModel<WidgetDefinition> widgetList = new SimpleListModel<WidgetDefinition>(
                            Lists.newArrayList(widgetCategories.get(categoryListbox.getSelectedItem().getValue())));
            widgetList.sort(WIDGET_DEFINITION_COMPARATOR, true);
            widgetListbox.setModel(widgetList);
        }
    }


    private void reloadCategories()
    {
        widgetCategories = getWidgetCategories();
        final SimpleListModel widgetCategoryList = new SimpleListModel(Lists.newArrayList(widgetCategories.keySet()));
        widgetCategoryList.sort(Collator.getInstance(), true);
        categoryListbox.setModel(widgetCategoryList);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "refreshButton")
    public void refreshLibrary()
    {
        widgetUtils.refreshWidgetLibrary();
        reloadCategories();
    }


    @ViewEvent(eventName = Events.ON_UPLOAD, componentID = "uploadButton")
    public void uploadJar(final Event event) throws IOException
    {
        if(event instanceof UploadEvent)
        {
            final Media media = ((UploadEvent)event).getMedia();
            if(media.getName() != null && media.getName().endsWith("jar"))
            {
                widgetLibUtils.uploadJarFromStream(media.getName(), media.getStreamData());
                refreshLibrary();
            }
            else
            {
                Messagebox.show("Not a jar file");
            }
        }
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "backButton1")
    public void backOneClicked()
    {
        categoryListbox.setSelectedItem(null);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "backButton2")
    public void backTwoClicked()
    {
        widgetListbox.setSelectedItem(null);
    }


    @ViewEvent(eventName = Events.ON_CHANGING, componentID = "widgetFilter")
    public void filterChanged(final InputEvent event)
    {
        filter = event.getValue();
        reloadCategories();
        categorySelected();
    }


    @ViewEvent(eventName = Events.ON_SELECT, componentID = "widgetListbox")
    public void widgetSelected()
    {
        if(widgetListbox.getSelectedItem() != null)
        {
            backButton2.setLabel("\u00ab " + categoryListbox.getSelectedItem().getValue());
            widgetDescriptionContainer.getChildren().clear();
            renderWidgetDefinitionDetails(widgetDescriptionContainer, (WidgetDefinition)widgetListbox.getSelectedItem().getValue());
        }
    }


    protected Map<String, List<WidgetDefinition>> getWidgetCategories()
    {
        final Map<String, List<WidgetDefinition>> ret = new HashMap<String, List<WidgetDefinition>>();
        final List<WidgetDefinition> allWidgetTypes = widgetDefinitionService
                        .getComponentDefinitionsByClass(WidgetDefinition.class);
        for(final WidgetDefinition widgetDefinitionModel : allWidgetTypes)
        {
            if(widgetDefinitionModel.isStubWidget())
            {
                continue;
            }
            final String key = StringUtils.isBlank(widgetDefinitionModel.getCategoryTag()) ? "Uncategorized"
                            : widgetDefinitionModel.getCategoryTag();
            List<WidgetDefinition> list = ret.get(key);
            if(list == null)
            {
                list = new ArrayList<WidgetDefinition>();
                ret.put(key, list);
            }
            if(StringUtils.containsIgnoreCase(key, filter)
                            || StringUtils.containsIgnoreCase(widgetDefinitionModel.getName(), filter))
            {
                list.add(widgetDefinitionModel);
            }
        }
        final List<String> toRemove = new ArrayList<String>();
        toRemove.add("_hidden");
        for(final Map.Entry<String, List<WidgetDefinition>> entry : ret.entrySet())
        {
            if(entry.getValue().isEmpty())
            {
                toRemove.add(entry.getKey());
            }
        }
        for(final String rem : toRemove)
        {
            ret.remove(rem);
        }
        return ret;
    }


    private void renderWidgetDefinitionDetails(final Component parent, final WidgetDefinition wiDef)
    {
        cockpitAdminService.renderWidgetDefinitionInfo(parent, wiDef);
        final Div useBtnDiv = new Div();
        useBtnDiv.setSclass("addBtn");
        parent.appendChild(useBtnDiv);
        final Button btnAddConnect = new Button("Add & connect");
        btnAddConnect.setId("connect_button");
        final Component mainWin = parent.getRoot();
        useBtnDiv.appendChild(btnAddConnect);
        btnAddConnect.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                addWidgetToSlot(event, wiDef);
                final Object attribute = Executions.getCurrent().getAttribute("lastCreatedWidget");
                if(attribute instanceof Widget)
                {
                    cockpitAdminService.showWidgetMultiConnectionWizard((Widget)attribute, mainWin);
                }
            }
        });
        final Button btnAddClose = new Button("Add & close");
        btnAddClose.setId("add_close_button");
        useBtnDiv.appendChild(btnAddClose);
        btnAddClose.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                addWidgetToSlot(event, wiDef);
            }
        });
    }


    private void addWidgetToSlot(final Event event, final WidgetDefinition wiDef) throws Exception
    {
        try
        {
            final Event evt = new Event("onWidgetSelect", event.getTarget(), wiDef);
            final Object attribute = mainContainer.getParent().getAttribute("selectListener");
            if(attribute instanceof EventListener)
            {
                ((EventListener<Event>)attribute).onEvent(evt);
            }
        }
        finally
        {
            mainContainer.getParent().detach();
        }
    }
}
