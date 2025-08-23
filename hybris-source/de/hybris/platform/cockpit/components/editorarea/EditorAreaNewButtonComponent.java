package de.hybris.platform.cockpit.components.editorarea;

import de.hybris.platform.cockpit.components.StyledDiv;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.TemplateListEntry;
import de.hybris.platform.cockpit.util.UITools;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Menupopup;

public class EditorAreaNewButtonComponent extends StyledDiv
{
    public EditorAreaNewButtonComponent()
    {
        setWidth("100%");
        setSclass("newItemGroupbox");
        getContainerDiv().setWidth("100%");
        getContainerDiv().setAlign("right");
        Button toolbarButton = new Button(Labels.getLabel("editorarea.button.createnewitem"));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "CreateNewItemBtn_EA_";
            UITools.applyTestID((Component)toolbarButton, "CreateNewItemBtn_EA_");
        }
        toolbarButton.setParent((Component)getContainerDiv());
        toolbarButton.setSclass("btncreatenew");
        toolbarButton.setDisabled(false);
        toolbarButton.setStyle("position: relative; right: 5px; top: 0px");
        toolbarButton.setTooltiptext(Labels.getLabel("editorarea.button.createnewitem.tooltip"));
        toolbarButton.addEventListener("onClick", (EventListener)new Object(this));
        Menupopup menupopup = new Menupopup();
        menupopup.setParent((Component)getContainerDiv());
        UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        boolean hasEntry = false;
        if(perspective instanceof BaseUICockpitPerspective)
        {
            if(((BaseUICockpitPerspective)perspective).generateCreateMenuitems(menupopup, false))
            {
                hasEntry = true;
            }
            else
            {
                List<TemplateListEntry> entries = ((BaseUICockpitPerspective)perspective).getTemplateList();
                if(entries != null)
                {
                    for(TemplateListEntry entry : entries)
                    {
                        if(((BaseUICockpitPerspective)perspective).canCreate(entry) && !entry.isAbstract())
                        {
                            hasEntry = true;
                            break;
                        }
                    }
                }
            }
        }
        toolbarButton.setVisible(hasEntry);
    }
}
