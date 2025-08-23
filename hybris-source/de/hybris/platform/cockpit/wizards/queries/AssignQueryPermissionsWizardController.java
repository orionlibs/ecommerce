package de.hybris.platform.cockpit.wizards.queries;

import de.hybris.platform.cockpit.components.duallistbox.impl.DefaultReferenceDualListboxEditor;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
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

public class AssignQueryPermissionsWizardController extends DefaultPageController
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
        UICollectionQuery query = (UICollectionQuery)wizardContext.getAttribute("query");
        ObjectCollection objCollection = query.getObjectCollection();
        List<ItemModel> usersWithRights = UISessionUtils.getCurrentSession().getTypeService().unwrapItems(assignedValuesList);
        if("READ".equals(accessRight))
        {
            saveReadUsers(usersWithRights, objCollection);
        }
        if("WRITE".equals(accessRight))
        {
            saveWriteUsers(usersWithRights, objCollection);
        }
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().update();
        setNotification("collection.collections", "collection.successfully.shared.nousername", new Object[0]);
    }


    private void saveReadUsers(List<ItemModel> usersWithRights, ObjectCollection objCollection)
    {
        List<PrincipalModel> readSavedUsersForCollection = getObjectCollectionService().getReadUsersForCollection(objCollection);
        for(ItemModel user : usersWithRights)
        {
            if(user instanceof PrincipalModel)
            {
                PrincipalModel principalModel = (PrincipalModel)user;
                if(!readSavedUsersForCollection.contains(principalModel))
                {
                    getObjectCollectionService().addReadUser(principalModel, objCollection);
                }
            }
        }
        List<ItemModel> usersToRemove = new ArrayList<>();
        usersToRemove.addAll(readSavedUsersForCollection);
        usersToRemove.removeAll(usersWithRights);
        for(ItemModel userToRemove : usersToRemove)
        {
            getObjectCollectionService().removeReadUser((PrincipalModel)userToRemove, objCollection);
        }
    }


    private void saveWriteUsers(List<ItemModel> usersWithRights, ObjectCollection objCollection)
    {
        List<PrincipalModel> writeSavedUsersForCollection = getObjectCollectionService().getWriteUsersForCollection(objCollection);
        for(ItemModel user : usersWithRights)
        {
            if(user instanceof PrincipalModel)
            {
                PrincipalModel principalModel = (PrincipalModel)user;
                if(!writeSavedUsersForCollection.contains(principalModel))
                {
                    getObjectCollectionService().addWriteUser(principalModel, objCollection);
                }
            }
        }
        List<ItemModel> usersToRemove = new ArrayList<>();
        usersToRemove.addAll(writeSavedUsersForCollection);
        usersToRemove.removeAll(usersWithRights);
        for(ItemModel userToRemove : usersToRemove)
        {
            getObjectCollectionService().removeWriteUser((PrincipalModel)userToRemove, objCollection);
        }
    }


    private void setNotification(String captionKey, String messageKey, Object... messageAttrs)
    {
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier()
                        .setNotification(new Notification(Labels.getLabel(captionKey), Labels.getLabel(messageKey, messageAttrs)));
    }


    private ObjectCollectionService getObjectCollectionService()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().getObjectCollectionService();
    }
}
