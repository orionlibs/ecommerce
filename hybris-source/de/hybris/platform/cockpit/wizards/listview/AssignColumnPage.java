package de.hybris.platform.cockpit.wizards.listview;

import de.hybris.platform.cockpit.components.duallistbox.impl.ColumnDescriptorDualListboxEditor;
import de.hybris.platform.cockpit.components.duallistbox.impl.DefaultSimpleDualListboxEditor;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;

public class AssignColumnPage extends AbstractGenericItemPage
{
    protected DefaultSimpleDualListboxEditor editor;


    public Component createRepresentationItself()
    {
        UITools.detachChildren((Component)this.pageContent);
        Div container = new Div();
        Map<String, Object> predef = getWizard().getPredefinedValues();
        List<ColumnDescriptor> visibleColumns = (List<ColumnDescriptor>)predef.get("visibleColumns");
        List<ColumnDescriptor> allColumns = (List<ColumnDescriptor>)predef.get("allColumns");
        this.editor = (DefaultSimpleDualListboxEditor)new ColumnDescriptorDualListboxEditor(visibleColumns, allColumns);
        Map<String, Object> parameters = new HashMap<>();
        String maxResults = UITools.getCockpitParameter("default.duallistbox.maxResults", Executions.getCurrent());
        if(StringUtils.isNotBlank(maxResults))
        {
            parameters.put("maxResults", maxResults);
        }
        this.editor.setSingleSelector(false);
        container.appendChild((Component)this.editor.createComponentView(parameters));
        container.setParent((Component)this.pageContent);
        return (Component)container;
    }


    protected DefaultSimpleDualListboxEditor getEditor()
    {
        return this.editor;
    }
}
