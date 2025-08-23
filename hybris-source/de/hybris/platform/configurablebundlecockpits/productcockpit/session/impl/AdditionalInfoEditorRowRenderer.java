package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRow;
import de.hybris.platform.cockpit.session.impl.EditorPropertyRow;
import de.hybris.platform.cockpit.session.impl.EditorRowRenderer;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

public class AdditionalInfoEditorRowRenderer extends EditorRowRenderer
{
    protected static final String ADDITIONAL_INFO_TEXT_SCLASS = "additionalInfoText";


    public void render(SectionPanel panel, Component parent, SectionRow row, Map<String, Object> ctx)
    {
        String additionalRowInfo = getAdditionalRowInfo(row);
        if(additionalRowInfo == null)
        {
            super.render(panel, parent, row, ctx);
        }
        else
        {
            super.render(panel, parent, row, ctx);
            Label label = new Label(additionalRowInfo);
            label.setStyle("float: left; color: #999; margin: 1px; margin-bottom: 8px; text-align: left");
            Component rowContainer = getRowContainer(parent);
            rowContainer.insertBefore((Component)label, rowContainer.getFirstChild());
        }
    }


    protected String getAdditionalRowInfo(SectionRow row)
    {
        if(row instanceof EditorPropertyRow)
        {
            return ((EditorPropertyRow)row).getRowConfiguration().getParameter("additionalPropertyInfo");
        }
        return null;
    }


    protected Component getRowContainer(Component parent)
    {
        if(parent.getParent() instanceof org.zkoss.zul.Box && parent.getParent().getParent() != null)
        {
            return parent.getParent().getParent();
        }
        return parent;
    }
}
