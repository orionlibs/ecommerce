package de.hybris.platform.admincockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstractNavigationAreaSectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

public class ConstraintGroupsSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(panel.getModel() instanceof de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel)
        {
            Vbox vbox = new Vbox();
            vbox.setSclass("navigation_queries");
            renderAllConstraintGroupsButton(vbox);
            addSeparatorBar(vbox);
            parent.appendChild((Component)vbox);
        }
    }


    private void renderAllConstraintGroupsButton(Vbox vbox)
    {
        renderCustomButton(vbox, "na.constrainedSection.all_constraints_groups_button", "onClick", (EventListener)new Object(this));
    }


    private void renderCustomButton(Vbox vbox, String key, String evtnm, EventListener evtListener)
    {
        Button customButton = new Button(Labels.getLabel(key));
        customButton.addEventListener(evtnm, evtListener);
        vbox.appendChild((Component)customButton);
    }


    private void addSeparatorBar(Vbox vbox)
    {
        Separator separator = new Separator();
        separator.setBar(true);
        vbox.appendChild((Component)separator);
    }


    public BaseUICockpitNavigationArea getNavigationArea()
    {
        return (BaseUICockpitNavigationArea)super.getNavigationArea();
    }


    private UIBrowserArea getBrowserArea()
    {
        return getNavigationArea().getPerspective().getBrowserArea();
    }
}
