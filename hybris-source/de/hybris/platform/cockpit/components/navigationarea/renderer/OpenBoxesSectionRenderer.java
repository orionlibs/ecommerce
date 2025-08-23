package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SimpleRenderer;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Vbox;

public class OpenBoxesSectionRenderer extends AbstractNavigationAreaSectionRenderer implements SimpleRenderer
{
    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        render(parent);
    }


    public AbstractNavigationAreaModel getSectionPanelModel()
    {
        return (AbstractNavigationAreaModel)super.getSectionPanelModel();
    }


    public void render(Component parent)
    {
        List<BrowserModel> tasks = (getNavigationArea().getPerspective() != null) ? getNavigationArea().getPerspective().getBrowserArea().getBrowsers() : new ArrayList<>();
        Listbox taskList = createList("navigation_openbrowserlist", tasks, (ListitemRenderer)new BrowserTaskRenderer(this));
        if(taskList != null)
        {
            for(int i = 0; i < tasks.size(); i++)
            {
                if(((BrowserModel)tasks.get(i)).isFocused())
                {
                    taskList.setSelectedIndex(i);
                    break;
                }
            }
        }
        Vbox vbox = new Vbox();
        vbox.setSclass("navigation_queries");
        if(taskList != null)
        {
            taskList.addEventListener("onSelect", (EventListener)new Object(this));
            vbox.appendChild((Component)taskList);
            taskList.setFixedLayout(true);
            taskList.setWidth("100%");
        }
        parent.appendChild((Component)vbox);
    }
}
