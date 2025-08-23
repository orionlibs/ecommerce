package de.hybris.platform.cockpit.components;

import java.io.IOException;
import java.io.Writer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.render.ComponentRenderer;
import org.zkoss.zk.ui.render.SmartWriter;

public class DivMacroDefault implements ComponentRenderer
{
    public void render(Component comp, Writer out) throws IOException
    {
        SmartWriter smartWriter = new SmartWriter(out);
        HtmlMacroComponent self = (HtmlMacroComponent)comp;
        smartWriter.write("<div id=\"").write(self.getUuid()).write('"');
        smartWriter.write(self.getOuterAttrs()).write(self.getInnerAttrs()).write(">");
        smartWriter.writeChildren((Component)self);
        smartWriter.write("</div>");
    }
}
