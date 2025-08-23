package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Toolbarbutton;

public class ContextAreaReferenceCollectionUIEditor extends DefaultReferenceCollectionUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(ContextAreaReferenceCollectionUIEditor.class);
    public static final String MAX_ENTRIES = "maxEntries";
    public static final String NAME = "browserContextEditor";
    private static final String OPEN_BROWSER_EDITOR_IMG = "cockpit/images/icon_func_open_list.png";
    private static final String OPEN_REFERENCE_WIZARD_IMG = "cockpit/images/icon_func_new_available.png";
    private boolean expanded;
    protected String componentShortLabel;
    protected static final String MIN_VISIBLE_ROWS = "editor.referenceselector_min_visible_rows";
    protected static final String MAX_VISIBLE_ROWS = "editor.referenceselector_max_visible_rows";
    protected static final String MAX_ROWS_IN_LIST = "editor.referenceselector_max_rows_in_list";
    protected Toolbarbutton showMoreButton = null;
    protected TypedObject rootObject = null;
    protected String propertyInfo = null;


    protected String getSingleLabel(Object value)
    {
        String ret = null;
        if(value instanceof TypedObject)
        {
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            ret = labelService.getObjectTextLabel((TypedObject)value);
        }
        else if(value != null)
        {
            ret = value.toString();
        }
        return ret;
    }


    protected String getCollapsedCombinedLabel(Collection<Object> items, int maxEntries)
    {
        Iterator<Object> iterator = items.iterator();
        String label = getSingleLabel(iterator.next());
        int index = 2;
        while(iterator.hasNext())
        {
            if(index > maxEntries)
            {
                label = label + ", ...";
                break;
            }
            label = label + ", " + label;
            index++;
        }
        return label;
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        int maxEntries = 3;
        getModel().setParameters(parameters);
        if(initialValue != null)
        {
            if(initialValue instanceof Collection)
            {
                if(!((Collection)initialValue).isEmpty())
                {
                    Object firstItem = ((Collection)initialValue).iterator().next();
                    if(firstItem instanceof TypedObject &&
                                    UISessionUtils.getCurrentSession().getTypeService()
                                                    .getObjectType(getModel().getRootType().getCode())
                                                    .isAssignableFrom((ObjectType)((TypedObject)firstItem).getType()))
                    {
                        getModel().setCollectionItems(new ArrayList((Collection)initialValue));
                    }
                    else
                    {
                        throw new IllegalArgumentException("Initial value '" + initialValue + "' can not be assigned to root type '" +
                                        getModel().getRootType() + "'");
                    }
                }
            }
            else if(initialValue instanceof TypedObject)
            {
                getModel().setCollectionItems(Collections.singletonList(initialValue));
            }
            else
            {
                throw new IllegalArgumentException("Initial value '" + initialValue + "' not a typed object.");
            }
        }
        else
        {
            getModel().setCollectionItems(Collections.EMPTY_LIST);
        }
        if(initialValue != null)
        {
            if(initialValue instanceof Collection)
            {
                getModel().setCollectionItems(new ArrayList((Collection)initialValue));
                Collection items = getModel().getCollectionItems();
                if(!items.isEmpty())
                {
                    Object maxParam = parameters.get("maxEntries");
                    if(maxParam instanceof String)
                    {
                        try
                        {
                            maxEntries = Integer.parseInt((String)maxParam);
                        }
                        catch(NumberFormatException e)
                        {
                            LOG.warn("Could not parse maxEntries parameter");
                        }
                    }
                    this.componentShortLabel = getCollapsedCombinedLabel(items, maxEntries);
                }
            }
            else
            {
                getModel().setCollectionItems(Collections.singletonList(initialValue));
                this.componentShortLabel = getSingleLabel(initialValue);
            }
        }
        Div div = new Div();
        div.setSclass("contextReferenceCollectionEditor");
        Hbox hbox = new Hbox();
        hbox.setStyle("table-layout:fixed;");
        hbox.setWidth("100%");
        hbox.setSpacing("0");
        hbox.setWidths("none, 18px, 18px");
        Object object = new Object(this, listener);
        if(isEditable())
        {
            hbox.appendChild((Component)div);
            createViewElement(div);
        }
        else
        {
            hbox.appendChild((Component)new Label(this.componentShortLabel));
        }
        if(parameters.containsKey("currentObject"))
        {
            this.rootObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(parameters.get("currentObject"));
        }
        if(parameters.containsKey("propInfo") && parameters.get("propInfo") instanceof String)
        {
            this.propertyInfo = (String)parameters.get("propInfo");
        }
        if(this.rootObject != null)
        {
            Image imgOpenNewReferenceWizard = new Image("cockpit/images/icon_func_new_available.png");
            imgOpenNewReferenceWizard.setTooltiptext(Labels.getLabel("editorarea.button.createnewitem"));
            imgOpenNewReferenceWizard.setStyle("width:18px; height: 20px");
            String langIso = "";
            CreateContext createCtx = (CreateContext)parameters.get("createContext");
            if(createCtx != null)
            {
                langIso = createCtx.getLangIso();
            }
            if(StringUtils.isBlank(langIso))
            {
                langIso = UISessionUtils.getCurrentSession().getGlobalDataLanguageIso();
            }
            UITools.addBusyListener((Component)imgOpenNewReferenceWizard, "onClick", (EventListener)new NewReferenceItemActionEventListener(this, langIso), null, "editor.openincontext.busy");
            hbox.appendChild((Component)imgOpenNewReferenceWizard);
            Image imgOpenExternal = new Image("cockpit/images/icon_func_open_list.png");
            imgOpenExternal.setTooltiptext(Labels.getLabel("editor.button.referenceeditor.open.tooltip"));
            imgOpenExternal.setStyle("width:18px; height: 20px");
            UITools.addBusyListener((Component)imgOpenExternal, "onClick", (EventListener)object, null, "editor.openincontext.busy");
            hbox.appendChild((Component)imgOpenExternal);
        }
        Div wrapperDiv = new Div();
        wrapperDiv.appendChild((Component)hbox);
        return (HtmlBasedComponent)wrapperDiv;
    }


    public void createViewElement(Div div)
    {
        int collectionSize = getModel().getCollectionItems().size();
        int rowSize = Math.max(1, Math.min(collectionSize, getMinVisibleRowsNumber()));
        div.getChildren().clear();
        List<Object> itemsOnDisplay = null;
        if(collectionSize > getMaxRowsInList())
        {
            itemsOnDisplay = getModel().getCollectionItems().subList(0, getMaxRowsInList());
        }
        else
        {
            itemsOnDisplay = getModel().getCollectionItems();
        }
        ListModelArray model = new ListModelArray(itemsOnDisplay);
        Listbox listbox = new Listbox();
        listbox.setItemRenderer((ListitemRenderer)new Object(this));
        listbox.setModel((ListModel)model);
        listbox.setRows(rowSize);
        div.appendChild((Component)listbox);
        this.showMoreButton = new Toolbarbutton(Labels.getLabel("referenceselector.more"));
        this.showMoreButton.setVisible((getModel().getCollectionItems().size() > getMinVisibleRowsNumber()));
        this.showMoreButton.setSclass("referenceEditorButton");
        this.showMoreButton.addEventListener("onClick", (EventListener)new Object(this, listbox, rowSize));
        div.appendChild((Component)this.showMoreButton);
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        Component comp = rootEditorComponent.getFirstChild().getFirstChild();
        Events.echoEvent("onClick", comp, null);
    }


    protected int getMinVisibleRowsNumber()
    {
        return Integer.parseInt(UITools.getCockpitParameter("editor.referenceselector_min_visible_rows", Executions.getCurrent()));
    }


    protected int getMaxVisibleRowsNumber()
    {
        return Integer.parseInt(UITools.getCockpitParameter("editor.referenceselector_max_visible_rows", Executions.getCurrent()));
    }


    protected int getMaxRowsInList()
    {
        return Integer.parseInt(UITools.getCockpitParameter("editor.referenceselector_max_rows_in_list", Executions.getCurrent()));
    }
}
