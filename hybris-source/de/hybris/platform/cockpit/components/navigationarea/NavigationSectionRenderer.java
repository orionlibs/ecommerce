package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

public class NavigationSectionRenderer implements SectionRenderer
{
    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        NavigationPanelSection navsection = (NavigationPanelSection)section;
        if(navsection.getRenderer() != null)
        {
            navsection.getRenderer().render(panel, parent, captionComponent, (Section)navsection);
        }
        else
        {
            Vbox vBox = new Vbox();
            vBox.setHeight("100px");
            vBox.setWidth("100%");
            vBox.appendChild((Component)new Label("Navigation Section"));
            vBox.appendChild((Component)new Label("- not implemented -"));
            vBox.setStyle("background:white");
            parent.appendChild((Component)vBox);
        }
    }
}
