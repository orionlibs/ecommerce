package de.hybris.platform.productcockpit.components.sectionpanel;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelLabelRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import de.hybris.platform.cockpit.services.dragdrop.impl.DefaultDraggedItem;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.ListActionHelper;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.productcockpit.session.impl.AbstractProductCockpitPerspective;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;

public class DefaultSectionPanelRenderer implements SectionPanelLabelRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSectionPanelRenderer.class);
    private static final String EDITOR_AREA_ACTIONS_DIV = "editorAreaActionsDiv";
    private static final String EDITOR_AREA_STATUS_DIV = "editorAreaStatusDiv";
    private final UIEditorArea editorArea;
    private TypedObject currentObject;


    public DefaultSectionPanelRenderer(UIEditorArea editorArea)
    {
        if(editorArea == null)
        {
            throw new IllegalArgumentException("Editor area can not be null.");
        }
        this.editorArea = editorArea;
    }


    public void render(String label, String imageUrl, Component parent)
    {
        String name = "";
        String code = "";
        String catv = "";
        String[] labels = label.split("#-#");
        if(labels.length >= 3)
        {
            name = labels[0];
            code = labels[1];
            catv = labels[2];
        }
        else
        {
            name = label;
        }
        Div box = new Div();
        UITools.maximize((HtmlBasedComponent)box);
        Div labelDiv = new Div();
        labelDiv.setSclass("labelContainer");
        String prefix = "#base#";
        if(name.startsWith("#base#"))
        {
            name = name.substring("#base#".length());
            Label labelComp = new Label(name);
            labelComp.setSclass("itemBaseName");
            labelDiv.appendChild((Component)labelComp);
        }
        else
        {
            labelDiv.appendChild((Component)new Label(name));
        }
        Div hbox = new Div();
        Label codeLabel = new Label(code);
        codeLabel.setSclass("editor_label_code");
        hbox.appendChild((Component)codeLabel);
        Label catLabel = new Label(catv);
        catLabel.setSclass("editor_label_catalogversion");
        hbox.appendChild((Component)catLabel);
        labelDiv.appendChild((Component)hbox);
        box.setDroppable("PerspectiveDND");
        box.addEventListener("onDrop", (EventListener)new Object(this));
        String displayImgUrl = "cockpit/images/stop_klein.jpg";
        if(StringUtils.isNotBlank(imageUrl))
        {
            displayImgUrl = UITools.getAdjustedUrl(imageUrl);
        }
        Div imgDiv = new Div();
        imgDiv.setSclass("section-label-image");
        Image img = new Image(displayImgUrl);
        img.setSclass("section-label-image");
        imgDiv.appendChild((Component)img);
        Popup imgPopup = new Popup();
        Image tooltipImg = new Image(displayImgUrl);
        imgPopup.appendChild((Component)tooltipImg);
        imgDiv.appendChild((Component)imgPopup);
        img.setTooltip(imgPopup);
        box.appendChild((Component)imgDiv);
        Div descriptionContainer = new Div();
        descriptionContainer.setSclass("descriptionContainer");
        box.appendChild((Component)descriptionContainer);
        Div firstRow = new Div();
        firstRow.setSclass("statusActionContainer");
        descriptionContainer.appendChild((Component)firstRow);
        if(getCurrentObject() != null)
        {
            Div stausDiv = new Div();
            stausDiv.setClass("editorAreaStatusDiv");
            renderStatus((Component)stausDiv, getCurrentObject());
            firstRow.appendChild((Component)stausDiv);
        }
        if(getCurrentObject() != null)
        {
            Div actionsDiv = new Div();
            actionsDiv.setClass("editorAreaActionsDiv");
            renderActions((Component)actionsDiv, getCurrentObject());
            firstRow.appendChild((Component)actionsDiv);
        }
        descriptionContainer.appendChild((Component)labelDiv);
        DragAndDropWrapper wrapper = getEditorArea().getPerspective().getDragAndDropWrapperService().getWrapper();
        wrapper.attachDraggedItem((DraggedItem)new DefaultDraggedItem(getEditorArea().getCurrentObject()), (Component)img);
        img.setDraggable("PerspectiveDND");
        parent.appendChild((Component)box);
        if(getEditorArea().getCurrentObject() != null)
        {
            if(UISessionUtils.getCurrentSession().getCurrentPerspective() instanceof AbstractProductCockpitPerspective && (
                            (AbstractProductCockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective())
                            .isEditorAreaBrowsingEnabled())
            {
                Toolbar browseToolbar = new Toolbar();
                browseToolbar.setSclass("editorLabelNavigation");
                Toolbarbutton leftBtn = new Toolbarbutton("", "productcockpit/images//button_browse_back_available_i.png");
                leftBtn.setSclass("editorarea_browse_btn");
                leftBtn.addEventListener("onClick", (EventListener)new Object(this));
                Toolbarbutton browseBtn = new Toolbarbutton("", "productcockpit/images//button_browse_available_i.png");
                browseBtn.setSclass("editorarea_browse_btn");
                UITools.addBusyListener((Component)browseBtn, "onClick", (EventListener)new Object(this), null, null);
                Toolbarbutton rightBtn = new Toolbarbutton("", "productcockpit/images//button_browse_forward_available_i.png");
                rightBtn.setSclass("editorarea_browse_btn");
                rightBtn.addEventListener("onClick", (EventListener)new Object(this));
                browseToolbar.appendChild((Component)leftBtn);
                browseToolbar.appendChild((Component)browseBtn);
                browseToolbar.appendChild((Component)rightBtn);
                descriptionContainer.appendChild((Component)browseToolbar);
            }
        }
    }


    protected void renderActions(Component parent, TypedObject item)
    {
        ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(item);
        GridViewConfiguration config = (GridViewConfiguration)UISessionUtils.getCurrentSession().getUiConfigurationService().getComponentConfiguration(template, "gridView", GridViewConfiguration.class);
        ActionColumnConfiguration actionConfiguration = getActionConfiguration(config);
        ListActionHelper.renderActions(parent, item, actionConfiguration, "editorAreaActionImg",
                        Collections.singletonMap("inEditorAreaSectionPanel", Boolean.TRUE));
    }


    protected void renderStatus(Component parent, TypedObject item)
    {
        ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(item);
        GridViewConfiguration config = (GridViewConfiguration)UISessionUtils.getCurrentSession().getUiConfigurationService().getComponentConfiguration(template, "gridView", GridViewConfiguration.class);
        ActionColumnConfiguration actionConfiguration = getStatusConfiguration(config);
        ListActionHelper.renderActions(parent, item, actionConfiguration, "editorAreaActionImg");
    }


    public ActionColumnConfiguration getActionConfiguration(GridViewConfiguration config)
    {
        if(config != null)
        {
            String actionSpringBeanID = config.getActionSpringBeanID();
            if(actionSpringBeanID != null)
            {
                return (ActionColumnConfiguration)SpringUtil.getBean(actionSpringBeanID);
            }
        }
        return null;
    }


    public ActionColumnConfiguration getStatusConfiguration(GridViewConfiguration config)
    {
        if(config != null)
        {
            String specialActionSpringBeanID = config.getSpecialactionSpringBeanID();
            if(specialActionSpringBeanID != null)
            {
                return (ActionColumnConfiguration)SpringUtil.getBean(specialActionSpringBeanID);
            }
        }
        return null;
    }


    protected UIEditorArea getEditorArea()
    {
        return this.editorArea;
    }


    public TypedObject getCurrentObject()
    {
        return this.currentObject;
    }


    public void setCurrentObject(TypedObject currentObject)
    {
        this.currentObject = currentObject;
    }
}
