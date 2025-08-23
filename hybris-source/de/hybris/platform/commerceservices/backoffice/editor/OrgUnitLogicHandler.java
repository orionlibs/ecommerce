package de.hybris.platform.commerceservices.backoffice.editor;

import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaLogicHandler;
import de.hybris.platform.commerceservices.backoffice.exception.RemoveAdminException;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.services.OrgUnitMemberParameter;
import de.hybris.platform.commerceservices.organization.services.OrgUnitParameter;
import de.hybris.platform.commerceservices.organization.services.OrgUnitService;
import de.hybris.platform.commerceservices.organization.utils.OrgUtils;
import de.hybris.platform.commerceservices.util.CommerceSearchUtils;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class OrgUnitLogicHandler extends DefaultEditorAreaLogicHandler
{
    private static final Logger LOG = Logger.getLogger(OrgUnitLogicHandler.class);
    private OrgUnitService orgUnitService;
    private UserService userService;


    public void beforeEditorAreaRender(WidgetInstanceManager widgetInstanceManager, Object currentObject)
    {
        OrgUnitModel unit = (OrgUnitModel)currentObject;
        Optional<OrgUnitModel> optionalParentUnit = getOrgUnitService().getParent(unit);
        widgetInstanceManager.getModel().setValue("parentUnit", optionalParentUnit.isPresent() ? optionalParentUnit.get() : null);
        OrgUnitMemberParameter<EmployeeModel> param = OrgUtils.createOrgUnitMemberParameter(unit.getUid(), null, EmployeeModel.class,
                        CommerceSearchUtils.getAllOnOnePagePageableData());
        widgetInstanceManager.getModel().setValue("employeesChanged", getOrgUnitService().getMembers(param).getResults());
    }


    public Object performSave(WidgetInstanceManager widgetInstanceManager, Object currentObject) throws ObjectSavingException
    {
        OrgUnitModel unit = (OrgUnitModel)currentObject;
        try
        {
            DefaultContext defaultContext = new DefaultContext();
            defaultContext.addAttribute("suppress_event", Boolean.TRUE);
            OrgUnitModel parentUnit = (OrgUnitModel)widgetInstanceManager.getModel().getValue("parentUnit", OrgUnitModel.class);
            OrgUnitParameter param = new OrgUnitParameter();
            param.setOrgUnit(unit);
            param.setParentUnit(parentUnit);
            getOrgUnitService().updateUnit(param);
            handleSaveMembers(widgetInstanceManager, param.getOrgUnit());
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Saving org unit failed", e);
            }
            if(e instanceof ObjectSavingException)
            {
                throw e;
            }
            throw new ObjectSavingException(unit.getUid(), e.getMessage(), e);
        }
        return currentObject;
    }


    protected void handleSaveMembers(WidgetInstanceManager widgetInstanceManager, OrgUnitModel unit) throws ObjectSavingException
    {
        OrgUnitMemberParameter<EmployeeModel> empParam = OrgUtils.createOrgUnitMemberParameter(unit.getUid(), null, EmployeeModel.class,
                        CommerceSearchUtils.getAllOnOnePagePageableData());
        List<EmployeeModel> employeesBefore = getOrgUnitService().getMembers(empParam).getResults();
        List<EmployeeModel> employeesAfter = (List<EmployeeModel>)widgetInstanceManager.getModel().getValue("employeesChanged", List.class);
        Set<EmployeeModel> employeesToRemove = new HashSet<>(employeesBefore);
        employeesToRemove.removeAll(employeesAfter);
        EmployeeModel currentUser = (EmployeeModel)getUserService().getCurrentUser();
        for(EmployeeModel employeeToRemove : employeesToRemove)
        {
            if(employeeToRemove.equals(currentUser))
            {
                throw new ObjectSavingException(unit.getUid(), "Employees may not remove themselves from a unit.", null);
            }
            if(!OrgUtils.isAdmin(currentUser) && OrgUtils.isAdmin(employeeToRemove))
            {
                throw new RemoveAdminException(unit.getUid(), "Only an admin of an organization can remove another admin.", null);
            }
        }
        if(CollectionUtils.isNotEmpty(employeesToRemove))
        {
            empParam.setMembers(employeesToRemove);
            getOrgUnitService().removeMembers(empParam);
        }
        Set<EmployeeModel> employeesToAdd = new HashSet<>(employeesAfter);
        employeesToAdd.removeAll(employeesBefore);
        for(EmployeeModel employeeToAdd : employeesToAdd)
        {
            if(!OrgUtils.isAdmin(currentUser) && OrgUtils.isAdmin(employeeToAdd))
            {
                throw new ObjectSavingException(unit.getUid(), "Only an admin of an organization can add another admin.", null);
            }
        }
        if(CollectionUtils.isNotEmpty(employeesToAdd))
        {
            empParam.setMembers(new HashSet<>(employeesToAdd));
            getOrgUnitService().addMembers(empParam);
        }
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


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
