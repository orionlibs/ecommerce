package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.components.contentbrowser.ListSectionComponent;
import de.hybris.platform.cockpit.session.ListSectionModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class EditorListSectionComponent extends ListSectionComponent
{
    public EditorListSectionComponent(ListSectionModel sectionModel)
    {
        super(sectionModel);
        setSclass("editorListViewSection");
    }


    protected AdvancedGroupbox createSectionView()
    {
        AdvancedGroupbox groupBox = new AdvancedGroupbox();
        groupBox.setSclass("editorListViewSectionGroupbox");
        groupBox.setMold("default");
        this.groupBoxContent = new Div();
        this.groupBoxContent.setSclass("browserSectionContent");
        groupBox.appendChild((Component)this.groupBoxContent);
        this.listView = loadListView();
        if(this.listView == null)
        {
            this.groupBoxContent.appendChild((Component)new Label("Nothing to display"));
        }
        else
        {
            this.groupBoxContent.appendChild((Component)this.listView);
        }
        return groupBox;
    }
}
