package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.DefaultSection;
import de.hybris.platform.cockpit.components.sectionpanel.DefaultSectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.RowlayoutSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRow;
import de.hybris.platform.cockpit.session.EditorAreaController;
import de.hybris.platform.cockpit.util.UITools;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

public class EditorSectionRenderer extends DefaultSectionRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(EditorSectionRenderer.class);
    private final EditorAreaController editorAreaController;
    private static final String CUSTOMIZE_MODE_LOCKED_IMG_URI = "/cockpit/images/icon_func_customizemode_locked.png";
    private static final String CUSTOMIZE_MODE_UNLOCKED_IMG_URI = "/cockpit/images/icon_func_customizemode_unlocked.png";
    private static final String CUSTOMIZE_MODE_ACTIVE_IMG_URI = "/cockpit/images/icon_func_customizemode_confirm.png";


    public EditorSectionRenderer(EditorAreaController editorAreaController)
    {
        this.editorAreaController = editorAreaController;
    }


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        SectionPanel.SectionComponent secComp = (SectionPanel.SectionComponent)UITools.getNextParentOfType(SectionPanel.SectionComponent.class, parent, 10);
        if(panel.isLazyLoad() && !section.isInitialOpen() && secComp != null && !secComp.isInitialized())
        {
            secComp.addEventListener("onOpen", (EventListener)new Object(this, secComp, panel, parent, captionComponent, section));
        }
        else
        {
            renderLater(panel, parent, captionComponent, section);
        }
    }


    protected void renderLater(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(section instanceof CustomEditorSection && ((CustomEditorSection)section).getCustomRenderer() != null)
        {
            ((CustomEditorSection)section).getCustomRenderer().render(panel, parent, captionComponent, section);
        }
        else if(!section.isTabbed())
        {
            super.render(panel, parent, captionComponent, section);
            if(panel.isEditMode())
            {
                Hbox hbox = new Hbox();
                hbox.setStyle("width:50px;; height:100%; text-align: right; float: right;");
                Div imgDiv = new Div();
                Image img = new Image("/cockpit/images/icon_func_customizemode_locked.png");
                img.setStyle("cursor: pointer; height: 20px; width: 20px; position: relative;");
                img.setSclass("sectionEditButton");
                img.setTooltiptext(Labels.getLabel("section.button.unlock.tooltip"));
                imgDiv.appendChild((Component)img);
                Div checkDiv = new Div();
                Image checkImg = new Image("/cockpit/images/icon_func_customizemode_confirm.png");
                checkImg.setStyle("cursor: pointer; height: 20px; width: 20px; position: relative;");
                checkImg.setSclass("sectionEditButton");
                checkImg.setTooltiptext(Labels.getLabel("section.button.confirm.tooltip"));
                checkDiv.appendChild((Component)checkImg);
                Div propertyArea = new Div();
                propertyArea.setSclass("sectionPropertyArea");
                propertyArea.setId("property_edit_container");
                propertyArea.setAction("onshow:anima.slideDown(#{self});onhide:anima.slideUp(#{self})");
                propertyArea.setVisible(((DefaultSection)section).isInEditMode());
                parent.insertBefore((Component)propertyArea, parent.getFirstChild());
                if(propertyArea.isVisible())
                {
                    UITools.modifySClass((HtmlBasedComponent)parent, "editable_section", true);
                }
                else
                {
                    UITools.modifySClass((HtmlBasedComponent)parent, "editable_section", false);
                }
                updatePropertyArea((Component)propertyArea, panel.getModel(), section);
                imgDiv.addEventListener("onClick", (EventListener)new Object(this, propertyArea, section, imgDiv, img, parent));
                hbox.appendChild((Component)imgDiv);
                checkDiv.addEventListener("onClick", (EventListener)new Object(this, panel));
                hbox.appendChild((Component)checkDiv);
                captionComponent.appendChild((Component)hbox);
            }
        }
        else if(panel.getModel() instanceof RowlayoutSectionPanelModel)
        {
            Hbox hbox = new Hbox();
            hbox.setWidths("none, 100%");
            hbox.setStyle("margin-left:1em");
            RowlayoutSectionPanelModel model = (RowlayoutSectionPanelModel)panel.getModel();
            List<SectionRow> sectionRows = model.getRows(section);
            Combobox combo = new Combobox();
            combo.setSclass("section_header_combo");
            for(SectionRow sectionRow : sectionRows)
            {
                Div div = new Div();
                div.setParent(parent);
                div.setSclass("tabbed_section_row");
                SectionPanel.SectionRowComponent sectionRowComponent = panel.createRowComponent(section, sectionRow, (Component)div, parent, "0");
                sectionRowComponent.setSclass("sectionRowComponent");
                Comboitem comboItem = combo.appendItem(sectionRow.getLabel());
                comboItem.setValue(div);
                div.setVisible(false);
            }
            if(!sectionRows.isEmpty())
            {
                combo.setSelectedIndex(0);
            }
            ((Div)combo.getSelectedItem().getValue()).setVisible(true);
            combo.setReadonly(true);
            combo.addEventListener("onSelect", (EventListener)new Object(this, combo));
            hbox.appendChild((Component)combo);
            captionComponent.appendChild((Component)hbox);
        }
        else
        {
            LOG.error("Could not render section '" + section.getLabel() + "'. Model is not an instance of RowlayoutSectionPanelModel.");
        }
    }


    public void updatePropertyArea(Component propertyArea, SectionPanelModel model, Section section)
    {
        UITools.detachChildren(propertyArea);
        Div hiddenRowsDiv = new Div();
        Label komma = null;
        for(SectionRow row : ((RowlayoutSectionPanelModel)model).getRows(section))
        {
            if(!row.isVisible())
            {
                if(komma != null)
                {
                    hiddenRowsDiv.appendChild((Component)komma);
                }
                komma = new Label(",");
                Toolbarbutton toolbarButton = new Toolbarbutton(row.getLabel());
                toolbarButton.setSclass("section_property_attribute_entry");
                toolbarButton.addEventListener("onClick", (EventListener)new Object(this, section, row, model));
                toolbarButton.setParent((Component)hiddenRowsDiv);
            }
        }
        if(komma == null)
        {
            hiddenRowsDiv.appendChild((Component)new Label(Labels.getLabel("section.editor.hiddenattributes.empty")));
        }
        propertyArea.appendChild((Component)hiddenRowsDiv);
    }


    protected EditorAreaController getEditorAreaController()
    {
        return this.editorAreaController;
    }


    protected Component getOnLaterComponent()
    {
        return getEditorAreaController().getOnLaterComponent();
    }
}
