package de.hybris.platform.commerceservices.backoffice.editor;

import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaLogicHandler;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.services.OrgUnitService;
import de.hybris.platform.commerceservices.organization.strategies.OrgUnitAuthorizationStrategy;
import de.hybris.platform.commerceservices.organization.utils.OrgUtils;
import de.hybris.platform.commerceservices.util.CommerceSearchUtils;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrgUnitEmployeeLogicHandler extends DefaultEditorAreaLogicHandler
{
    private static final Logger LOG = Logger.getLogger(DefaultOrgUnitEmployeeLogicHandler.class);
    private static final String ORG_ROLES = "orgRoles";
    private static final String ORG_UNITS = "orgUnits";
    private OrgUnitService orgUnitService;
    private ModelService modelService;
    private UserService userService;
    private OrgUnitAuthorizationStrategy orgUnitAuthorizationStrategy;


    public void beforeEditorAreaRender(WidgetInstanceManager widgetInstanceManager, Object currentObject)
    {
        EmployeeModel employeeModel = (EmployeeModel)currentObject;
        Set<PrincipalGroupModel> orgRoles = new HashSet<>();
        Set<PrincipalGroupModel> orgUnits = new HashSet<>();
        for(PrincipalGroupModel group : employeeModel.getGroups())
        {
            if(OrgUtils.getRoleUids().contains(group.getUid()))
            {
                orgRoles.add(group);
                continue;
            }
            if(group instanceof OrgUnitModel)
            {
                orgUnits.add(group);
            }
        }
        widgetInstanceManager.getModel().setValue("orgRoles", orgRoles);
        widgetInstanceManager.getModel().setValue("orgUnits", orgUnits);
    }


    public Object performSave(WidgetInstanceManager widgetInstanceManager, Object currentObject) throws ObjectSavingException
    {
        EmployeeModel employeeModel = (EmployeeModel)currentObject;
        Set<UserGroupModel> orgRoles = (Set<UserGroupModel>)widgetInstanceManager.getModel().getValue("orgRoles", Set.class);
        Set<OrgUnitModel> orgUnits = (Set<OrgUnitModel>)widgetInstanceManager.getModel().getValue("orgUnits", Set.class);
        Set<PrincipalGroupModel> userGroups = new HashSet<>(employeeModel.getGroups());
        if(getUserService().getCurrentUser() instanceof EmployeeModel)
        {
            EmployeeModel currentUser = (EmployeeModel)getUserService().getCurrentUser();
            if(!OrgUtils.isAdmin(currentUser) && OrgUtils.isAdmin(employeeModel))
            {
                throw new ObjectSavingException(employeeModel.getUid(), "Only an admin of an organization can edit another admin.", null);
            }
            if(!OrgUtils.isAdmin(currentUser) && OrgUtils.containsOrgAdminGroup(orgRoles))
            {
                throw new ObjectSavingException(employeeModel.getUid(), "Only an admin of an organization can assign an admin role.", null);
            }
            if(!OrgUtils.isAdmin(currentUser) && !getOrgUnitAuthorizationStrategy().canEditUnit((UserModel)currentUser))
            {
                throw new ObjectSavingException(employeeModel.getUid(), "Only an admin or edit permission granted group of an organization can edit.", null);
            }
        }
        else
        {
            throw new ObjectSavingException(employeeModel.getUid(), "Only an employee of an organization can edit other employees.", null);
        }
        try
        {
            DefaultContext defaultContext = new DefaultContext();
            defaultContext.addAttribute("suppress_event", Boolean.TRUE);
            Set<OrgUnitModel> orgUnitsToRemove = new HashSet<>();
            Set<OrgUnitModel> orgUnitsToKeep = new HashSet<>();
            for(PrincipalGroupModel group : employeeModel.getGroups())
            {
                if(OrgUtils.getRoleUids().contains(group.getUid()) && !orgRoles.contains(group))
                {
                    userGroups.remove(group);
                    continue;
                }
                if(group instanceof OrgUnitModel)
                {
                    if(orgUnits.contains(group))
                    {
                        orgUnitsToKeep.add((OrgUnitModel)group);
                        continue;
                    }
                    orgUnitsToRemove.add((OrgUnitModel)group);
                }
            }
            userGroups.addAll(orgRoles);
            employeeModel.setGroups(userGroups);
            getModelService().save(employeeModel);
            for(PrincipalGroupModel principalGroupModel : orgUnitsToRemove)
            {
                getOrgUnitService().removeMembers(OrgUtils.createOrgUnitMemberParameter(principalGroupModel.getUid(),
                                Collections.singleton(employeeModel), EmployeeModel.class, CommerceSearchUtils.getAllOnOnePagePageableData()));
            }
            for(OrgUnitModel orgUnitModel : orgUnits)
            {
                if(!orgUnitsToKeep.contains(orgUnitModel))
                {
                    getOrgUnitService().addMembers(
                                    OrgUtils.createOrgUnitMemberParameter(orgUnitModel.getUid(), Collections.singleton(employeeModel), EmployeeModel.class,
                                                    CommerceSearchUtils.getAllOnOnePagePageableData()));
                }
            }
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
            }
            throw new ObjectSavingException(employeeModel.getUid(), e);
        }
        return currentObject;
    }


    protected OrgUnitService getOrgUnitService()
    {
        return this.orgUnitService;
    }


    @Required
    public void setOrgUnitService(OrgUnitService orgUnitService)
    {
        this.orgUnitService = orgUnitService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected OrgUnitAuthorizationStrategy getOrgUnitAuthorizationStrategy()
    {
        return this.orgUnitAuthorizationStrategy;
    }


    @Required
    public void setOrgUnitAuthorizationStrategy(OrgUnitAuthorizationStrategy orgUnitAuthorizationStrategy)
    {
        this.orgUnitAuthorizationStrategy = orgUnitAuthorizationStrategy;
    }
}
