package de.hybris.platform.cockpit.wizards.queries;

import de.hybris.platform.cockpit.components.duallistbox.impl.DefaultReferenceDualListboxEditor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;

public class AssignQueryPermissionsPage extends AbstractGenericItemPage
{
    private TypeService typeService;
    protected DefaultReferenceDualListboxEditor editor;


    public Component createRepresentationItself()
    {
        UITools.detachChildren((Component)this.pageContent);
        Div lalbelContainer = new Div();
        lalbelContainer.setParent((Component)this.pageContent);
        Map<String, Object> predef = getWizard().getPredefinedValues();
        List<PrincipalModel> usersWithRights = (List<PrincipalModel>)predef.get("rightUusers");
        this
                        .editor = new DefaultReferenceDualListboxEditor(UISessionUtils.getCurrentSession().getTypeService().wrapItems(usersWithRights));
        Map<String, Object> parameters = new HashMap<>();
        String maxResults = UITools.getCockpitParameter("default.duallistbox.maxResults", Executions.getCurrent());
        if(StringUtils.isNotBlank(maxResults))
        {
            parameters.put("maxResults", maxResults);
        }
        parameters.put("searchTypeCode", "User");
        lalbelContainer.appendChild((Component)this.editor.createComponentView(parameters));
        return (Component)lalbelContainer;
    }


    public DefaultReferenceDualListboxEditor getEditor()
    {
        return this.editor;
    }


    protected void setEditor(DefaultReferenceDualListboxEditor editor)
    {
        this.editor = editor;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
