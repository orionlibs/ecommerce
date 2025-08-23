package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.StyledDiv;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.springframework.util.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.impl.XulElement;

public class NavigationAreaComponent extends Div
{
    protected static final String COCKPIT_ID_NAVIGATION_AREA_VIEW = "NavigationArea_View";


    public NavigationAreaComponent()
    {
        setWidth("100%");
        setHeight("100%");
        setStyle("background:none; text-align: left; position: relative;");
        Div north = new Div();
        north.setSclass("navigation_north");
        Div menuDiv = new Div();
        menuDiv.setParent((Component)north);
        UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        StyledDiv perspectiveChooserDiv = new StyledDiv();
        perspectiveChooserDiv.setParent((Component)north);
        perspectiveChooserDiv.setAlign("left");
        perspectiveChooserDiv.setWidth("100%");
        perspectiveChooserDiv.setSclass("perspectiveChooser");
        Div center = new Div();
        center.setParent((Component)this);
        center.setSclass("navigation_center");
        center.setAction("onmouseup: comm.sendClick(#{self},null)");
        north.setParent((Component)this);
        addEventListener("onClick", (EventListener)new Object(this));
        if(UISessionUtils.getCurrentSession().getAvailablePerspectives().size() < 2)
        {
            perspectiveChooserDiv.setVisible(false);
            UITools.modifySClass((HtmlBasedComponent)center, "perspChooserHidden", true);
        }
        SectionPanel sectionpanel = new SectionPanel();
        sectionpanel.setParent((Component)center);
        sectionpanel.setWidth("100%");
        sectionpanel.setHeight("100%");
        sectionpanel.setSclass("navigation_area");
        sectionpanel.setModel(perspective.getNavigationArea().getSectionModel());
        sectionpanel.setSectionRenderer(perspective.getNavigationArea().getSectionRenderer());
        sectionpanel.addEventListener("onSectionHide", (EventListener)new Object(this, sectionpanel));
        sectionpanel.addEventListener("onSectionShow", (EventListener)new Object(this, sectionpanel));
        sectionpanel.addEventListener("onAllSectionsShow", (EventListener)new Object(this));
        sectionpanel.addEventListener("onSectionMoved", (EventListener)new Object(this, sectionpanel));
        sectionpanel.afterCompose();
        Div south = new Div();
        south.setHeight("25px");
        south.setParent((Component)this);
        south.setSclass("navigation_area_south");
        Div contentAndInfoSlotContainer = new Div();
        contentAndInfoSlotContainer.setParent((Component)south);
        contentAndInfoSlotContainer.setSclass("navigation_queries");
        Div infoBoxComponent = new Div();
        contentAndInfoSlotContainer.appendChild((Component)infoBoxComponent);
        addEventListener("onCreate", (EventListener)new Object(this, perspective, menuDiv, perspectiveChooserDiv, infoBoxComponent));
    }


    protected void fillPerspectives(Component parent)
    {
        int index = 0;
        UISession session = UISessionUtils.getCurrentSession();
        int perspCount = session.getAvailablePerspectives().size();
        for(UICockpitPerspective p : session.getAvailablePerspectives())
        {
            String txt = Labels.getLabel(p.getLabel());
            if(txt == null)
            {
                txt = "[" + p.getLabel() + "]";
            }
            Button button = new Button(txt);
            button.setSclass("plainBtn");
            if(p.equals(session.getCurrentPerspective()) && p.isSelectable())
            {
                button.setStyle("font-weight:bold;");
                button.setDisabled(true);
            }
            Div buttonDiv = new Div();
            buttonDiv.setSclass("perspectiveButton");
            buttonDiv.appendChild((Component)button);
            UITools.applyTestID((Component)button, "NavigationArea_View_" + StringUtils.trimAllWhitespace(button.getLabel()) + "_button");
            buttonDiv.addEventListener("onPerspChangeLater", (EventListener)new Object(this, session));
            Object object = new Object(this, buttonDiv);
            if(UISessionUtils.getCurrentSession().isDragOverPerspectivesEnabled())
            {
                UITools.addDragHoverClickEventListener((XulElement)buttonDiv, null, 500, "PerspectiveDND");
            }
            if(!p.isSelectable())
            {
                button.setStyle("font-style:italic");
                button.setDisabled(true);
            }
            else
            {
                button.setAttribute("p", p);
                buttonDiv.setAttribute("p", p);
                button.addEventListener("onClick", (EventListener)object);
                if(!p.equals(session.getCurrentPerspective()))
                {
                    buttonDiv.addEventListener("onDragHoverClicked", (EventListener)object);
                }
            }
            if(++index < perspCount)
            {
                Label label = new Label("  ");
                label.setPre(true);
                label.setSclass("z-separator-ver-bar");
                label.setWidth("1px");
                buttonDiv.appendChild((Component)label);
            }
            parent.appendChild((Component)buttonDiv);
        }
    }


    protected void renderPerspectiveChooser(Component parent)
    {
        Div div = new Div();
        div.setParent(parent);
        div.setAlign("start");
        div.setSclass("sectionpaneltitle");
        fillPerspectives((Component)div);
    }
}
