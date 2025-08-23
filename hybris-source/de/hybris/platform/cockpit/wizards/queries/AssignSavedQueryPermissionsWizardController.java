package de.hybris.platform.cockpit.wizards.queries;

import de.hybris.platform.cockpit.components.duallistbox.impl.DefaultReferenceDualListboxEditor;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.services.query.SavedQueryService;
import de.hybris.platform.cockpit.services.query.SavedQueryUserRightsService;
import de.hybris.platform.cockpit.services.query.impl.DummySavedQueryUserRightsServiceImpl;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardContext;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.resource.Labels;

public class AssignSavedQueryPermissionsWizardController extends DefaultPageController
{
    public void done(Wizard wizard, WizardPage page)
    {
        AssignQueryPermissionsPage accessRightsSelectorPage = null;
        if(page instanceof AssignQueryPermissionsPage)
        {
            accessRightsSelectorPage = (AssignQueryPermissionsPage)page;
        }
        DefaultReferenceDualListboxEditor defaultReferenceDualListboxEditor = accessRightsSelectorPage.getEditor();
        List<TypedObject> assignedValuesList = defaultReferenceDualListboxEditor.getAssignedValuesList();
        WizardContext wizardContext = wizard.getWizardContext();
        String accessRight = (String)wizardContext.getAttribute("rightName");
        UISavedQuery query = (UISavedQuery)wizardContext.getAttribute("query");
        if("READ".equals(accessRight))
        {
            List<ItemModel> usersWithRights = UISessionUtils.getCurrentSession().getTypeService().unwrapItems(assignedValuesList);
            saveReadUsers(usersWithRights, query.getSavedQuery());
            UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().update();
            setNotification("savedquery.name", "savedquery.successfully.shared.general", new Object[0]);
        }
    }


    private void saveReadUsers(List<ItemModel> usersWithRights, CockpitSavedQueryModel model)
    {
        List<PrincipalModel> readSavedUsersForCollection = getSavedQueryService().getReadUsersForSavedQuery(model);
        for(ItemModel user : usersWithRights)
        {
            if(user instanceof PrincipalModel)
            {
                PrincipalModel principalModel = (PrincipalModel)user;
                if(!readSavedUsersForCollection.contains(principalModel))
                {
                    getSavedQueryService().addReadUser(principalModel, model);
                }
            }
        }
        List<ItemModel> usersToRemove = new ArrayList<>();
        usersToRemove.addAll(readSavedUsersForCollection);
        usersToRemove.removeAll(usersWithRights);
        for(ItemModel userToRemove : usersToRemove)
        {
            getSavedQueryService().removeReadUser((PrincipalModel)userToRemove, model);
        }
    }


    private void setNotification(String captionKey, String messageKey, Object... messageAttrs)
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier()
                        .setNotification(new Notification(Labels.getLabel(captionKey), Labels.getLabel(messageKey, messageAttrs)));
    }


    private SavedQueryUserRightsService getSavedQueryService()
    {
        SavedQueryService savedQueryService = UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().getSavedQueryService();
        if(savedQueryService instanceof SavedQueryUserRightsService)
        {
            return (SavedQueryUserRightsService)savedQueryService;
        }
        return (SavedQueryUserRightsService)new DummySavedQueryUserRightsServiceImpl(savedQueryService);
    }
}
