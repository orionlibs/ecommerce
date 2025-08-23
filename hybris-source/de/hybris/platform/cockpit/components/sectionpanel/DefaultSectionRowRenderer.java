package de.hybris.platform.cockpit.components.sectionpanel;

import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Textbox;

public class DefaultSectionRowRenderer implements SectionRowRenderer
{
    public void render(SectionPanel panel, Component parent, SectionRow row, Map<String, Object> ctx)
    {
        Textbox textBox = new Textbox("no value");
        textBox.setWidth("98%");
        textBox.setParent(parent);
        ctx.put("focusableComponent", textBox);
    }
}
