package de.hybris.platform.cockpit.components.editorarea;

import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class BasePopupEditorComponent extends Div
{
    private static final Logger LOG = LoggerFactory.getLogger(BasePopupEditorComponent.class);
    private final Div divBack = new Div();
    private final UIEditorArea popupEditorArea;


    public BasePopupEditorComponent()
    {
        setWidth("100%");
        setAlign("right");
        setHeight("100%");
        UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        this.popupEditorArea = perspective.getPopupEditorArea();
        try
        {
            if(Boolean.parseBoolean(UITools.getCockpitParameter("default.popUpEditor.allowOverlap", Executions.getCurrent())))
            {
                this.divBack.setParent((Component)this);
                this.divBack.setStyle("background-color:white; width: 100%; height: 5%");
                this.divBack.appendChild(createStack(this.popupEditorArea));
                appendChild((Component)this.divBack);
                setBackPanelVisible(false);
            }
        }
        catch(Exception e)
        {
            LOG.warn("Cannot read popupEditorOverlapping property");
        }
        SectionPanel sectionpanel = new SectionPanel();
        sectionpanel.setParent((Component)this);
        sectionpanel.setWidth("100%");
        sectionpanel.setHeight("90%");
        sectionpanel.setFlatSectionLayout(true);
        sectionpanel.setRowLabelWidth("11em");
        sectionpanel.setLazyLoad(true);
        sectionpanel.setModel(this.popupEditorArea.getEditorAreaController().getSectionPanelModel());
        sectionpanel.setSectionRenderer(this.popupEditorArea.getEditorAreaController().getSectionRenderer());
        sectionpanel.setSectionRowRenderer(this.popupEditorArea.getEditorAreaController().getSectionRowRenderer());
        sectionpanel.addEventListener("onAllSectionsShow", (EventListener)new Object(this));
        sectionpanel.addEventListener("onRowShow", (EventListener)new Object(this));
        sectionpanel.addEventListener("onRowHide", (EventListener)new Object(this));
        sectionpanel.addEventListener("onRowMoved", (EventListener)new Object(this));
        sectionpanel.addEventListener("onSectionHide", (EventListener)new Object(this));
        sectionpanel.addEventListener("onSectionShow", (EventListener)new Object(this));
        sectionpanel.addEventListener("onMessageClicked", (EventListener)new Object(this));
        sectionpanel.addEventListener("onLater", (EventListener)new Object(this));
        sectionpanel.afterCompose();
    }


    public final void setBackPanelVisible(boolean isVisible)
    {
        this.divBack.setVisible(isVisible);
    }


    private Component createStack(UIEditorArea editorArea)
    {
        Button backButton = new Button("back");
        backButton.setStyle("position: absolute; left: 10px; top: 30px;");
        backButton.addEventListener("onClick", (EventListener)new Object(this, editorArea));
        return (Component)backButton;
    }
}
