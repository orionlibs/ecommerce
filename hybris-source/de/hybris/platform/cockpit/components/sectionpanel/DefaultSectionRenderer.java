package de.hybris.platform.cockpit.components.sectionpanel;

import de.hybris.platform.cockpit.util.UITools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class DefaultSectionRenderer implements SectionRenderer
{
    private static final Logger log = LoggerFactory.getLogger(DefaultSectionRenderer.class.getName());


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(panel.getModel() instanceof RowlayoutSectionPanelModel)
        {
            RowlayoutSectionPanelModel model = (RowlayoutSectionPanelModel)panel.getModel();
            Div div = new Div();
            parent.appendChild((Component)div);
            for(SectionRow sectionRow : model.getRows(section))
            {
                HtmlBasedComponent rowDiv = panel.createRowComponent(section, sectionRow, (Component)div, parent);
                rowDiv.setSclass("sectionRowComponent");
                rowDiv.setVisible(sectionRow.isVisible());
            }
            UITools.applyLazyload((HtmlBasedComponent)div);
        }
        else
        {
            log.error("Could not render section '" + section.getLabel() + "'. Model is not an instance of RowlayoutSectionPanelModel.");
        }
    }
}
