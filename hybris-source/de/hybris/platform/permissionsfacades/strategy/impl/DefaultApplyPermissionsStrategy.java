package de.hybris.platform.permissionsfacades.strategy.impl;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.permissionsfacades.data.PermissionValuesData;
import de.hybris.platform.permissionsfacades.data.TypePermissionsData;
import de.hybris.platform.permissionsfacades.data.TypePermissionsDataList;
import de.hybris.platform.permissionsfacades.strategy.ApplyPermissionsStrategy;
import de.hybris.platform.permissionsfacades.validation.strategy.TypePermissionsListValidationStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

public class DefaultApplyPermissionsStrategy implements ApplyPermissionsStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultApplyPermissionsStrategy.class);
    private final FlexibleSearchService flexibleSearchService;
    private final SessionService sessionService;
    private final TransactionTemplate transactionTemplate;
    private final PermissionManagementService permissionManagementService;
    private final TypeService typeService;
    private final List<TypePermissionsListValidationStrategy> validationStrategies;


    public DefaultApplyPermissionsStrategy(@NotNull FlexibleSearchService flexibleSearchService, @NotNull PermissionManagementService permissionManagementService, @NotNull TypeService typeService, @NotNull SessionService sessionService, @NotNull TransactionTemplate transactionTemplate,
                    @NotNull List<TypePermissionsListValidationStrategy> validationStrategies)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("flexibleSearchService", flexibleSearchService);
        ServicesUtil.validateParameterNotNullStandardMessage("permissionManagementService", permissionManagementService);
        ServicesUtil.validateParameterNotNullStandardMessage("typeService", typeService);
        ServicesUtil.validateParameterNotNullStandardMessage("sessionService", sessionService);
        ServicesUtil.validateParameterNotNullStandardMessage("transactionTemplate", transactionTemplate);
        ServicesUtil.validateParameterNotNullStandardMessage("validationStrategies", validationStrategies);
        this.flexibleSearchService = flexibleSearchService;
        this.sessionService = sessionService;
        this.transactionTemplate = transactionTemplate;
        this.permissionManagementService = permissionManagementService;
        this.typeService = typeService;
        this.validationStrategies = Collections.unmodifiableList(validationStrategies);
    }


    public void apply(@NotNull TypePermissionsDataList permissionsList)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionsList", permissionsList);
        LOG.debug("Applying permissions '{}'", permissionsList);
        getValidationStrategies().forEach(v -> v.validate(permissionsList));
        PrincipalModel principal = getPrincipal(permissionsList.getPrincipalUid());
        applyInTransaction(principal, permissionsList);
    }


    protected void applyInTransaction(PrincipalModel principal, TypePermissionsDataList typePermissionsDataList)
    {
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, typePermissionsDataList, principal));
    }


    protected PrincipalModel getPrincipal(String principalUid)
    {
        PrincipalModel example = new PrincipalModel();
        example.setUid(principalUid);
        return (PrincipalModel)getFlexibleSearchService().getModelByExample(example);
    }


    protected void addPermission(PrincipalModel principal, TypePermissionsData typePermission)
    {
        ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(typePermission.getType());
        LOG.debug("Setting permissions for principal '{}' and type '{}'", principal.getUid(), composedType.getCode());
        Collection<PermissionAssignment> permissionAssignments = createPermissionAssignments(principal, typePermission.getPermissions());
        getPermissionManagementService().addTypePermissions(composedType, permissionAssignments);
    }


    protected Collection<PermissionAssignment> createPermissionAssignments(PrincipalModel principal, PermissionValuesData permissions)
    {
        List<PermissionAssignment> assignments = new ArrayList<>();
        if(permissions.getChange() != null)
        {
            assignments.add(createPermissionsAssignment("change", principal, permissions.getChange().booleanValue()));
        }
        if(permissions.getCreate() != null)
        {
            assignments.add(createPermissionsAssignment("create", principal, permissions.getCreate().booleanValue()));
        }
        if(permissions.getRead() != null)
        {
            assignments.add(createPermissionsAssignment("read", principal, permissions.getRead().booleanValue()));
        }
        if(permissions.getRemove() != null)
        {
            assignments.add(createPermissionsAssignment("remove", principal, permissions.getRemove().booleanValue()));
        }
        if(permissions.getChangerights() != null)
        {
            assignments.add(createPermissionsAssignment("changerights", principal, permissions.getChangerights().booleanValue()));
        }
        return assignments;
    }


    protected PermissionAssignment createPermissionsAssignment(String permission, PrincipalModel principal, boolean granted)
    {
        LOG.debug("Principal '{}' has change permission set to {}", principal.getUid(), Boolean.valueOf(granted));
        return new PermissionAssignment(permission, principal, !granted);
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    protected TransactionTemplate getTransactionTemplate()
    {
        return this.transactionTemplate;
    }


    protected List<TypePermissionsListValidationStrategy> getValidationStrategies()
    {
        return this.validationStrategies;
    }


    protected PermissionManagementService getPermissionManagementService()
    {
        return this.permissionManagementService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }
}
