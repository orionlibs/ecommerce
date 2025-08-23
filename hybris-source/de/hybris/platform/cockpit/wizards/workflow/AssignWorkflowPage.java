package de.hybris.platform.cockpit.wizards.workflow;

import de.hybris.platform.cockpit.components.duallistbox.impl.CockpitUsersDualListboxEditor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;

public class AssignWorkflowPage extends AbstractGenericItemPage
{
    protected CockpitUsersDualListboxEditor editor;
    private final List<TypedObject> availableValuesList = new ArrayList<>();


    public Component createRepresentationItself()
    {
        UITools.detachChildren((Component)this.pageContent);
        Div lalbelContainer = new Div();
        Map<String, Object> predef = getWizard().getPredefinedValues();
        List<PrincipalModel> assignedValuesList = (List<PrincipalModel>)predef.get("assignedUsers");
        UserGroupModel groupModel = (UserGroupModel)predef.get("groupModel");
        prepareAvailableValuesList((PrincipalModel)groupModel);
        this
                        .editor = new CockpitUsersDualListboxEditor(UISessionUtils.getCurrentSession().getTypeService().wrapItems(assignedValuesList), this.availableValuesList);
        Map<String, Object> parameters = new HashMap<>();
        String maxResults = UITools.getCockpitParameter("default.duallistbox.maxResults", Executions.getCurrent());
        if(StringUtils.isNotBlank(maxResults))
        {
            parameters.put("maxResults", maxResults);
        }
        parameters.put("searchTypeCode", "User");
        this.editor.setSingleSelector(true);
        lalbelContainer.appendChild((Component)this.editor.createComponentView(parameters));
        lalbelContainer.setParent((Component)this.pageContent);
        return (Component)lalbelContainer;
    }


    private void prepareAvailableValuesList(PrincipalModel parentPrincipal)
    {
        if(parentPrincipal instanceof UserGroupModel)
        {
            UserGroupModel model = (UserGroupModel)parentPrincipal;
            TypedObject modelObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(model);
            this.availableValuesList.add(modelObject);
            for(PrincipalModel principalModel : model.getMembers())
            {
                prepareAvailableValuesList(principalModel);
            }
        }
        else if(parentPrincipal instanceof UserModel)
        {
            UserModel model = (UserModel)parentPrincipal;
            TypedObject modelObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(model);
            this.availableValuesList.add(modelObject);
        }
    }


    public CockpitUsersDualListboxEditor getEditor()
    {
        return this.editor;
    }
}
