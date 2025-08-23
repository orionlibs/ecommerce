package de.hybris.platform.core.systemsetup.datacreator.impl;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.systemsetup.datacreator.internal.CoreDataCreator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.security.ImportExportUserRightsHelper;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class UserGroupsDataCreator implements CoreDataCreator
{
    private static final String HIDE_SYSTEM_PRINCIPALS_RESTRICTION_CODE = "HideSystemPrincipals";


    public void populateDatabase()
    {
        try
        {
            UserGroup employeeGroup = createOrGetUserGroup(Constants.USER.EMPLOYEE_USERGROUP);
            addGroupToUserByClass(Employee.class, employeeGroup);
            UserGroup customerGroup = createOrGetUserGroup(Constants.USER.CUSTOMER_USERGROUP);
            addGroupToUserByClass(Customer.class, customerGroup);
            getUserManager().getAnonymousCustomer().addToGroup((PrincipalGroup)customerGroup);
            createHideSystemPrincipalsRestriction();
            createEmployeeGroupSecurityRightsForTypes();
        }
        catch(ConsistencyCheckException e)
        {
            throw new SystemException(e.getMessage(), e);
        }
    }


    private UserGroup createOrGetUserGroup(String userGroupUID) throws ConsistencyCheckException
    {
        UserGroup employeeGroup = null;
        try
        {
            employeeGroup = getUserGroupByID(userGroupUID);
        }
        catch(JaloItemNotFoundException e)
        {
            Logger.getLogger(UserGroupsDataCreator.class).trace(e);
            employeeGroup = getUserManager().createUserGroup(userGroupUID);
        }
        return employeeGroup;
    }


    private void addGroupToUserByClass(Class userClass, UserGroup userGroup)
    {
        ComposedType composedType = getTypeManager().getComposedType(userClass);
        Set<UserGroup> employeegrouplist = new LinkedHashSet();
        employeegrouplist.add(userGroup);
        composedType.getAttributeDescriptor("groups").setDefaultValue(employeegrouplist);
    }


    private void createEmployeeGroupSecurityRightsForTypes()
    {
        String filename = "/core/userrights.csv";
        InputStream inputStream = UserGroupsDataCreator.class.getResourceAsStream("/core/userrights.csv");
        InputStreamReader isr = new InputStreamReader(inputStream);
        if(inputStream != null)
        {
            ImportExportUserRightsHelper helper = new ImportExportUserRightsHelper(isr, '"', ';', ',', false);
            helper.importSecurity();
        }
        else
        {
            throw new SystemException("Ressource /core/userrights.csv in ressources/jar/ not found - aborting import and setting TypeRights");
        }
    }


    private void createHideSystemPrincipalsRestriction()
    {
        UserGroup employees = getUserGroupByID(Constants.USER.EMPLOYEE_USERGROUP);
        if(getTypeManager()
                        .getSearchRestriction(getComposedTypeForClass(Principal.class), "HideSystemPrincipals") == null)
        {
            getTypeManager().createRestriction("HideSystemPrincipals", (Principal)employees,
                            getComposedTypeForClass(Principal.class), "{uid} not in ( 'anonymous', 'admin', 'admingroup' )");
        }
    }


    private ComposedType getComposedTypeForClass(Class klass)
    {
        return getTypeManager().getComposedType(klass);
    }


    private UserGroup getUserGroupByID(String groupID)
    {
        return getUserManager().getUserGroupByGroupID(groupID);
    }


    private UserManager getUserManager()
    {
        return UserManager.getInstance();
    }


    private TypeManager getTypeManager()
    {
        return TypeManager.getInstance();
    }
}
