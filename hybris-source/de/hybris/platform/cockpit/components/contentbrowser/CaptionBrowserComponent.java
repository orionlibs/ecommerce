package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

public class CaptionBrowserComponent extends AbstractBrowserComponent
{
    protected transient Groupbox mainGroupbox = null;
    protected transient Toolbarbutton closeBtn = null;
    protected transient Popup navigationPathTooltip = null;
    protected transient Label navigationPathLabel = null;


    public CaptionBrowserComponent(BrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            setWidth("100%");
            getChildren().clear();
            this.mainGroupbox = createGroupbox();
            appendChild((Component)this.mainGroupbox);
            this.mainGroupbox.appendChild((Component)createCaption());
            this.mainGroupbox.appendChild((Component)createAdvancedArea());
            this.initialized = true;
        }
        return this.initialized;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            if(this.closeBtn != null)
            {
                this.closeBtn.setVisible(getModel().getArea().isClosable(getModel()));
            }
            updateLabels();
            success = true;
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties, Object reason)
    {
        super.updateItem(item, modifiedProperties, reason);
        updateLabels();
    }


    protected void updateLabels()
    {
        if(this.initialized)
        {
            if(this.navigationPathLabel != null)
            {
                this.navigationPathLabel.setValue(getModel().getLabel());
            }
            if(this.navigationPathTooltip != null && CollectionUtils.isNotEmpty(this.navigationPathTooltip.getChildren()))
            {
                this.navigationPathTooltip.getChildren().clear();
                HtmlBasedComponent tooltipContent = createCaptionLabelTooltipContent(getModel().getExtendedLabel());
                if(tooltipContent != null)
                {
                    this.navigationPathTooltip.appendChild((Component)tooltipContent);
                }
            }
        }
    }


    public void setActiveItem(TypedObject activeItem)
    {
    }


    public void updateActiveItems()
    {
    }


    public void updateSelectedItems()
    {
    }


    protected Groupbox createGroupbox()
    {
        Groupbox groupBox = new Groupbox();
        groupBox.addEventListener("onClick", (EventListener)new Object(this));
        groupBox.setMold("3d");
        groupBox.setOpen(getModel().isAdvancedHeaderDropdownVisible());
        groupBox.setWidth("100%");
        groupBox.setClosable(true);
        groupBox.setSclass("contentBrowserGroupbox");
        if(getModel().isAdvancedHeaderDropdownSticky())
        {
            UITools.modifySClass((HtmlBasedComponent)groupBox, "contentBrowserStickyGroupbox", true);
        }
        return groupBox;
    }


    protected HtmlBasedComponent createAdvancedArea()
    {
        return (HtmlBasedComponent)new Div();
    }


    protected Caption createCaption()
    {
        Caption caption = new Caption();
        caption.setSclass("hiddenLabel");
        caption.setAction("onclick: var nop = 0;");
        Vbox captionVbox = new Vbox();
        caption.appendChild((Component)captionVbox);
        captionVbox.setWidth("100%");
        Hbox captionHbox = new Hbox();
        captionHbox.setWidth("100%");
        HtmlBasedComponent leftCaptionContent = createLeftCaptionContent();
        UITools.modifySClass(leftCaptionContent, "caption_left", true);
        HtmlBasedComponent rightCaptionContent = createRightCaptionContent();
        UITools.modifySClass(rightCaptionContent, "caption_right", true);
        captionHbox.appendChild((Component)leftCaptionContent);
        captionHbox.appendChild((Component)rightCaptionContent);
        HtmlBasedComponent captionLabelComponent = createCaptionLabelComponent();
        UITools.modifySClass(captionLabelComponent, "caption_label", true);
        captionVbox.appendChild((Component)captionHbox);
        captionVbox.appendChild((Component)captionLabelComponent);
        return caption;
    }


    protected HtmlBasedComponent createLeftCaptionContent()
    {
        Div leftCaptionDiv = new Div();
        leftCaptionDiv.setAlign("left");
        leftCaptionDiv.setWidth("100%");
        return (HtmlBasedComponent)leftCaptionDiv;
    }


    protected void createAdditionalRightCaptionComponents(Hbox hbox)
    {
    }


    protected HtmlBasedComponent createRightCaptionContent()
    {
        Div rightCaptionDiv = new Div();
        rightCaptionDiv.setAlign("right");
        Hbox innerCaptionRightHbox = new Hbox();
        rightCaptionDiv.appendChild((Component)innerCaptionRightHbox);
        innerCaptionRightHbox.setAlign("center");
        UIBrowserArea area = getModel().getArea();
        createAdditionalRightCaptionComponents(innerCaptionRightHbox);
        innerCaptionRightHbox.appendChild((Component)new Space());
        if(getModel().getArea().isSplittable())
        {
            Toolbarbutton splitBtn = new Toolbarbutton("", area.isSplitModeActive() ? "/cockpit/images/button_view_splitmode_available_a.png" : "/cockpit/images/button_view_splitmode_available_i.png");
            splitBtn.setTooltiptext(Labels.getLabel(area.isSplitModeActive() ? "browser.button.splitmode.active.tooltip" :
                            "browser.button.splitmode.inactive.tooltip"));
            innerCaptionRightHbox.appendChild((Component)splitBtn);
            splitBtn.setSclass("plainBtn");
            splitBtn.setDisabled(false);
            UITools.addBusyListener((Component)splitBtn, "onClick", (EventListener)new Object(this, area), null, null);
        }
        return (HtmlBasedComponent)rightCaptionDiv;
    }


    protected HtmlBasedComponent createCaptionLabelComponent()
    {
        Div captionLabelDiv = new Div();
        captionLabelDiv.setSclass("navigation_path_div");
        captionLabelDiv.setHeight("1.3em");
        this.navigationPathTooltip = new Popup();
        appendChild((Component)this.navigationPathTooltip);
        String extendedLabel = getModel().getExtendedLabel();
        if(extendedLabel == null || extendedLabel.isEmpty())
        {
            extendedLabel = getModel().getLabel();
        }
        HtmlBasedComponent tooltipContent = createCaptionLabelTooltipContent(extendedLabel);
        if(tooltipContent != null)
        {
            this.navigationPathTooltip.appendChild((Component)tooltipContent);
        }
        this.navigationPathLabel = new Label(getModel().getLabel());
        captionLabelDiv.appendChild((Component)this.navigationPathLabel);
        this.navigationPathLabel.setSclass("navigation_path_label");
        this.navigationPathLabel.setTooltip(this.navigationPathTooltip);
        return (HtmlBasedComponent)captionLabelDiv;
    }


    protected HtmlBasedComponent createCaptionLabelTooltipContent(String string)
    {
        Vbox vbox;
        HtmlBasedComponent ret = null;
        if(string != null)
        {
            int querySeparatorIndex = string.indexOf('|');
            if(querySeparatorIndex < 0)
            {
                Label label = new Label(string.trim());
            }
            else
            {
                String queryString = string.substring(0, querySeparatorIndex).trim();
                String[] catalogItems = (querySeparatorIndex < 0) ? string.split(", ") : string.substring(querySeparatorIndex + 1).split(", ");
                Vbox vbox1 = new Vbox();
                vbox1.appendChild((Component)new Label("\"" + queryString + "\""));
                Separator sep = new Separator();
                sep.setBar(true);
                sep.setParent((Component)vbox1);
                for(String catItemStr : catalogItems)
                {
                    vbox1.appendChild((Component)new Label(catItemStr));
                }
                vbox = vbox1;
            }
        }
        return (HtmlBasedComponent)vbox;
    }


    public void resize()
    {
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
    }
}
