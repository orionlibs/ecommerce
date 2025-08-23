package de.hybris.platform.cmscockpit.components.contentbrowser.message;

import de.hybris.platform.cockpit.components.IdSpaceDiv;
import de.hybris.platform.cockpit.components.sectionpanel.Message;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

public class ContentEditorMessageRenderer implements MessageRenderer
{
    public void render(Component parent, Message message)
    {
        if(message == null || !message.isVisible())
        {
            Component component = parent.getFirstChild();
            if(component instanceof StatusPanelComponent)
            {
                UITools.detachChildren(component);
                component.setVisible(false);
            }
            return;
        }
        IdSpaceDiv idSpaceDiv = new IdSpaceDiv();
        switch(message.getLevel())
        {
            case 3:
                idSpaceDiv.setSclass("section_message_error");
                break;
            case 2:
                idSpaceDiv.setSclass("section_message_warning");
                break;
            case 0:
                idSpaceDiv.setSclass("section_message_info");
                break;
        }
        Label messageLabel = new Label(message.getText());
        messageLabel.setParent((Component)idSpaceDiv);
        idSpaceDiv.setStyle("position:relative");
        idSpaceDiv.setTooltiptext(message.getTooltip());
        Component statusPanel = parent.getFirstChild();
        if(statusPanel instanceof StatusPanelComponent)
        {
            UITools.detachChildren(statusPanel);
            statusPanel.appendChild((Component)idSpaceDiv);
            statusPanel.setVisible(true);
        }
    }
}
