/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.permissionmanagement;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.dataaccess.facades.permissions.Permission;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionInfo;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionManagementFacade;
import com.hybris.cockpitng.dataaccess.facades.permissions.impl.DefaultPermissionInfo;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class PermissionManagementController extends DefaultWidgetController
{
    protected static final String SOCKET_IN_INPUTOBJECT = "inputObject";
    protected static final String SOCKET_IN_PERMISSION_INFO = "permissionInfo";
    protected static final String SOCKET_IN_CREATE_PERMISSION = "createPermission";
    protected static final String SOCKET_IN_REMOVE_PERMISSIONS = "removePermissions";
    protected static final String SOCKET_IN_TYPE_OR_PRINCIPAL_FILTER = "typeOrPrincipalFilter";
    protected static final String SOCKET_IN_FIELD_FILTER = "fieldFilter";
    private static final Logger LOG = LoggerFactory.getLogger(PermissionManagementController.class);
    private static final long serialVersionUID = -6158344814231249897L;
    private static final String SETTING_TYPE_TYPECODE = "type_typeCode";
    private static final String SETTING_PRINCIPAL_TYPECODE = "principal_typeCode";
    private static final String SETTING_TYPE_ID_ACCESSOR = "type_id_attribute";
    private static final String SETTING_PRINCIPAL_ID_ACCESSOR = "principal_id_attribute";
    public static final String SETTING_PAGE_SIZE = "pageSize";
    private static final String SOCKET_OUT_TYPE_PERMISSION_INFOS = "typePermissionInfos";
    private static final String SOCKET_OUT_PRINCIPAL_PERMISSION_INFOS = "principalPermissionInfos";
    private static final String SOCKET_OUT_FIELD_PERMISSION_INFOS = "fieldPermissionInfos";
    private static final String SOCKET_OUT_TYPE_PERMISSIONINFOTYPE = "permissionInfoType";
    private static final String MODEL_PRINCIPAL_ID = "principal_ID";
    private static final String MODEL_TYPE_ID = "type_ID";
    private static final String MODEL_FILTER_PRINCIPAL_OR_TYPE_TEXT = "filterPrincipalOrTypeText";
    private static final String MODEL_FILTER_FIELDS_TEXT = "filterFieldsText";
    private static final String MODEL_CACHED_ALL_FIELD_PERMISSIONS = "cachedAllFieldPermissions";
    private static final PermissionInfoComparator permissionInfoComparator = new PermissionInfoComparator();
    private static final int DEFAULT_PAGE_SIZE = 20;
    @WireVariable
    private transient ExpressionResolverFactory expressionResolverFactory;
    @WireVariable
    private transient PermissionManagementFacade permissionManagementFacade;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient LabelService labelService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        initializeDataTypeFromSetting(SETTING_TYPE_TYPECODE);
        initializeDataTypeFromSetting(SETTING_PRINCIPAL_TYPECODE);
    }


    @SocketEvent(socketId = SOCKET_IN_INPUTOBJECT)
    public void showPermissionsForInputObject(final Object input)
    {
        if(input == null)
        {
            return;
        }
        final String typeCode = getTypeFacade().getType(input);
        if(StringUtils.isNotEmpty(typeCode))
        {
            try
            {
                final DataType dataType = getTypeFacade().load(typeCode);
                final Class<?> clazz = dataType.getClazz();
                if(isPrincipal(clazz))
                {
                    handlePrincipal(input);
                }
                else if(isType(clazz))
                {
                    handleType(input);
                }
                else
                {
                    LOG.error("Input object is neither principal nor type, ignoring.");
                }
            }
            catch(final TypeNotFoundException e)
            {
                LOG.error("Unknownw type for input object.", e);
            }
        }
    }


    @SocketEvent(socketId = SOCKET_IN_CREATE_PERMISSION)
    public void addNewPermissionAssignment(final Object data)
    {
        if(data != null)
        {
            final List<PermissionInfo> existingPermissionsWithNewOne = new ArrayList<>();
            final Collection<PermissionInfo> existingPermissions;
            String principalId = getValue(MODEL_PRINCIPAL_ID, String.class);
            String typeId = getValue(MODEL_TYPE_ID, String.class);
            final PermissionInfo.PermissionInfoType newPermissionType;
            if(isType(data.getClass()) && StringUtils.isNotBlank(principalId))
            {
                typeId = getObjectId(data, SETTING_TYPE_ID_ACCESSOR);
                existingPermissions = getPermissionManagementFacade().getTypePermissionInfosForPrincipal(principalId);
                newPermissionType = PermissionInfo.PermissionInfoType.TYPE;
            }
            else if(isPrincipal(data.getClass()) && StringUtils.isNotBlank(typeId))
            {
                principalId = getObjectId(data, SETTING_PRINCIPAL_ID_ACCESSOR);
                existingPermissions = getPermissionManagementFacade().getPrincipalsWithPermissionAssignment(typeId);
                newPermissionType = PermissionInfo.PermissionInfoType.PRINCIPAL;
            }
            else
            {
                throw new IllegalArgumentException("Input data is neither type or principal");
            }
            final PermissionInfo newPermissionInfo = initializeNewPermissionInfo(principalId, typeId, newPermissionType);
            existingPermissionsWithNewOne.addAll(existingPermissions);
            sortPermissionInfoCollection(existingPermissionsWithNewOne);
            if(!contains(existingPermissionsWithNewOne, newPermissionInfo))
            {
                existingPermissionsWithNewOne.add(0, newPermissionInfo);
            }
            filterCollection(existingPermissionsWithNewOne, getModel().getValue(MODEL_FILTER_PRINCIPAL_OR_TYPE_TEXT, String.class));
            sendOutput(SOCKET_OUT_TYPE_PERMISSION_INFOS, createPageable(existingPermissionsWithNewOne));
        }
    }


    // Alternatively the equals method could be overridden in PermissionInfo.
    private static boolean contains(final Collection<PermissionInfo> existing, final PermissionInfo toBeCheckedFor)
    {
        for(final PermissionInfo permissionInfo : existing)
        {
            if(toBeCheckedFor.getTypeCode().equals(permissionInfo.getTypeCode())
                            && toBeCheckedFor.getPrincipal().equals(permissionInfo.getPrincipal()))
            {
                return true;
            }
        }
        return false;
    }


    @SocketEvent(socketId = SOCKET_IN_PERMISSION_INFO)
    public void showFieldPermissions(final PermissionInfo permissionInfo)
    {
        if(permissionInfo != null)
        {
            final PermissionInfo.PermissionInfoType permissionInfoType = permissionInfo.getPermissionInfoType();
            String typeCode = null;
            String principalId = null;
            if(PermissionInfo.PermissionInfoType.TYPE.equals(permissionInfoType))
            {
                typeCode = permissionInfo.getTypeCode();
                principalId = getValue(MODEL_PRINCIPAL_ID, String.class);
            }
            else if(PermissionInfo.PermissionInfoType.PRINCIPAL.equals(permissionInfoType))
            {
                typeCode = getValue(MODEL_TYPE_ID, String.class);
                principalId = permissionInfo.getPrincipal();
            }
            if(StringUtils.isNotBlank(principalId) && StringUtils.isNotBlank(typeCode))
            {
                // TODO maybe we should add Collection<PermissionInfo> getFieldPermissionInfos(String principal, String
                // typeCode) method to PermissionManagementFacade API
                final List<PermissionInfo> fieldPermissionInfos = getFieldPermissionInfos(principalId, typeCode);
                getModel().put(MODEL_CACHED_ALL_FIELD_PERMISSIONS, new ArrayList<>(fieldPermissionInfos));
                filterCollection(fieldPermissionInfos, getModel().getValue(MODEL_FILTER_FIELDS_TEXT, String.class));
                sendOutput(SOCKET_OUT_FIELD_PERMISSION_INFOS, createPageable(Lists.newArrayList(fieldPermissionInfos)));
            }
        }
    }


    @SocketEvent(socketId = SOCKET_IN_REMOVE_PERMISSIONS)
    public void handlePermissionsDeleted(final List<PermissionInfo> permissionsToRemove)
    {
        if(permissionsToRemove != null)
        {
            for(final PermissionInfo permissionToRemove : permissionsToRemove)
            {
                for(final Permission p : permissionToRemove.getPermissions())
                {
                    permissionManagementFacade.deletePermission(p);
                }
                resetFieldPermissions(permissionToRemove);
            }
            sendCurrentPermissions();
            sendOutput(SOCKET_OUT_FIELD_PERMISSION_INFOS, createPageable(Lists.newArrayList()));
        }
    }


    @SocketEvent(socketId = SOCKET_IN_TYPE_OR_PRINCIPAL_FILTER)
    public void handleTypeOrPrincipalFilterChanged(final String filterContextInput)
    {
        getModel().put(MODEL_FILTER_PRINCIPAL_OR_TYPE_TEXT, filterContextInput);
        sendCurrentPermissions();
        sendOutput(SOCKET_OUT_FIELD_PERMISSION_INFOS, createPageable(Lists.newArrayList()));
    }


    @SocketEvent(socketId = SOCKET_IN_FIELD_FILTER)
    public void handleFieldFilterChanged(final String filter)
    {
        getModel().put(MODEL_FILTER_FIELDS_TEXT, filter);
        final List<PermissionInfo> cachedValue = getModel().getValue(MODEL_CACHED_ALL_FIELD_PERMISSIONS, List.class);
        if(cachedValue != null)
        {
            final List<PermissionInfo> copyOfCachedValue = new ArrayList<>(cachedValue);
            filterCollection(copyOfCachedValue, filter);
            sendOutput(SOCKET_OUT_FIELD_PERMISSION_INFOS, createPageable(copyOfCachedValue));
        }
    }


    private PageableList createPageable(final List<PermissionInfo> permissionInfos)
    {
        return new PageableList<>(permissionInfos,
                        ObjectUtils.defaultIfNull(getWidgetSettings().getInt(SETTING_PAGE_SIZE), DEFAULT_PAGE_SIZE),
                        DefaultPermissionInfo.class.getName());
    }


    private void resetFieldPermissions(final PermissionInfo permissionToRemove)
    {
        final Collection<PermissionInfo> fieldPermissions = getFieldPermissionInfos(permissionToRemove.getPrincipal(),
                        permissionToRemove.getTypeCode());
        for(final PermissionInfo fieldPermission : fieldPermissions)
        {
            for(final Permission permission : fieldPermission.getPermissions())
            {
                if(!permission.isInherited())
                {
                    permissionManagementFacade.deletePermission(permission);
                }
            }
        }
    }


    protected void sendCurrentPermissions()
    {
        final String principalId = getValue(MODEL_PRINCIPAL_ID, String.class);
        final String typeId = getValue(MODEL_TYPE_ID, String.class);
        if(StringUtils.isNotBlank(principalId))
        {
            retrieveTypesForUser(principalId);
        }
        else if(StringUtils.isNotBlank(typeId))
        {
            retrieveUsersForType(typeId);
        }
    }


    protected void retrieveTypesForUser(final String principalId)
    {
        final List<PermissionInfo> permissionInfos = new ArrayList(
                        permissionManagementFacade.getTypePermissionInfosForPrincipal(principalId));
        filterCollection(permissionInfos, getModel().getValue(MODEL_FILTER_PRINCIPAL_OR_TYPE_TEXT, String.class));
        sortPermissionInfoCollection(permissionInfos);
        sendOutput(SOCKET_OUT_TYPE_PERMISSION_INFOS, createPageable(permissionInfos));
    }


    protected void retrieveUsersForType(final String typeCode)
    {
        final List<PermissionInfo> permissionInfos = new ArrayList<>(
                        permissionManagementFacade.getPrincipalsWithPermissionAssignment(typeCode));
        filterCollection(permissionInfos, getModel().getValue(MODEL_FILTER_PRINCIPAL_OR_TYPE_TEXT, String.class));
        sortPermissionInfoCollection(permissionInfos);
        sendOutput(SOCKET_OUT_PRINCIPAL_PERMISSION_INFOS, createPageable(permissionInfos));
    }


    protected void filterCollection(final Collection<PermissionInfo> collection, final String filter)
    {
        if(StringUtils.isNotEmpty(filter))
        {
            final Iterator<PermissionInfo> iterator = collection.iterator();
            while(iterator.hasNext())
            {
                final PermissionInfo next = iterator.next();
                if(!filterMatch(next.getLabel(), filter))
                {
                    iterator.remove();
                }
            }
        }
    }


    protected boolean filterMatch(final String value, final String filter)
    {
        return value != null && filter != null && StringUtils.containsIgnoreCase(value, filter);
    }


    protected void sortPermissionInfoCollection(final List<PermissionInfo> collection)
    {
        Collections.sort(collection, permissionInfoComparator);
    }


    private static class PermissionInfoComparator implements Comparator<PermissionInfo>
    {
        @Override
        public int compare(final PermissionInfo o1, final PermissionInfo o2)
        {
            return o1.getLabel().compareToIgnoreCase(o2.getLabel());
        }
    }


    protected String getObjectId(final Object input, final String accessorMethodSetting)
    {
        final Object accessorMethod = getWidgetSettings().get(accessorMethodSetting);
        if(accessorMethod instanceof String)
        {
            final ExpressionResolver resolver = expressionResolverFactory.createResolver();
            final Object objectId = resolver.getValue(input, (String)accessorMethod);
            if(objectId instanceof String)
            {
                return (String)objectId;
            }
        }
        LOG.error("Invalid value for widget setting [{}]. Provide a string representing a getter method", accessorMethodSetting);
        return StringUtils.EMPTY;
    }


    private void initializeDataTypeFromSetting(final String dataTypeSetting)
    {
        final Object settingObject = getWidgetSettings().get(dataTypeSetting);
        if(settingObject instanceof String)
        {
            try
            {
                final DataType dataType = getTypeFacade().load((String)settingObject);
                setValue(dataTypeSetting, dataType);
            }
            catch(final TypeNotFoundException e)
            {
                LOG.error("Widget [" + this.getClass().getName() + "] not properly initialized.\n\r"
                                + "Provide valid data type code in widget settings for : \n\r " + "\t- " + dataTypeSetting, e);
            }
        }
        else
        {
            LOG.error("Widget [{}] not properly initialized.\n\rProvide valid data type code in widget settings for : \n\r \t- {}",
                            this.getClass().getName(), dataTypeSetting);
        }
    }


    private PermissionInfo initializeNewPermissionInfo(final String principalId, final String typeCode,
                    final PermissionInfo.PermissionInfoType permissionType)
    {
        final PermissionInfo inheritedPermissionInfo = getPermissionManagementFacade().getTypePermissionInfo(principalId, typeCode);
        final List<Permission> inheritedPermissions = inheritedPermissionInfo.getPermissions();
        final Map<String, Permission> inheritedPermissionsMap = new HashMap<>(inheritedPermissions.size());
        for(final Permission inheritedPermission : inheritedPermissions)
        {
            inheritedPermissionsMap.put(inheritedPermission.getName(), inheritedPermission);
        }
        final DefaultPermissionInfo permissionInfo = new DefaultPermissionInfo(permissionType, principalId, typeCode,
                        inheritedPermissionsMap);
        permissionInfo.setPersisted(false);
        if(permissionType == PermissionInfo.PermissionInfoType.TYPE)
        {
            permissionInfo.setLabel(getLabelService().getObjectLabel(permissionInfo.getTypeCode()));
        }
        return permissionInfo;
    }


    private List<PermissionInfo> getFieldPermissionInfos(final String principalId, final String typeCode)
    {
        final List<PermissionInfo> result = new ArrayList<>();
        try
        {
            final DataType dataType = getTypeFacade().load(typeCode);
            for(final DataAttribute attribute : dataType.getAttributes())
            {
                final PermissionInfo fieldPermissionInfo = getPermissionManagementFacade().getFieldPermissionInfo(principalId,
                                typeCode, attribute.getQualifier());
                if(fieldPermissionInfo != null)
                {
                    result.add(fieldPermissionInfo);
                }
            }
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error(e.getMessage(), e);
        }
        sortPermissionInfoCollection(result);
        return result;
    }


    private void handleType(final Object input)
    {
        final Class<?> typeClass = getValue(SETTING_TYPE_TYPECODE, DataType.class).getClazz();
        final String typeId = getObjectId(typeClass.cast(input), SETTING_TYPE_ID_ACCESSOR);
        setValue(MODEL_TYPE_ID, typeId);
        sendOutput(SOCKET_OUT_TYPE_PERMISSIONINFOTYPE, getWidgetSettings().get(SETTING_PRINCIPAL_TYPECODE));
        sendOutput(SOCKET_OUT_FIELD_PERMISSION_INFOS, createPageable(Lists.newArrayList()));
        retrieveUsersForType(typeId);
    }


    private void handlePrincipal(final Object input)
    {
        final Class<?> principalClass = getValue(SETTING_PRINCIPAL_TYPECODE, DataType.class).getClazz();
        final String principalId = getObjectId(principalClass.cast(input), SETTING_PRINCIPAL_ID_ACCESSOR);
        setValue(MODEL_PRINCIPAL_ID, principalId);
        sendOutput(SOCKET_OUT_TYPE_PERMISSIONINFOTYPE, getWidgetSettings().get(SETTING_TYPE_TYPECODE));
        sendOutput(SOCKET_OUT_FIELD_PERMISSION_INFOS, createPageable(Lists.newArrayList()));
        retrieveTypesForUser(principalId);
    }


    private boolean isType(final Class<?> clazz)
    {
        final DataType dataType = getValue(SETTING_TYPE_TYPECODE, DataType.class);
        return dataType != null && dataType.getClazz().isAssignableFrom(clazz);
    }


    private boolean isPrincipal(final Class<?> clazz)
    {
        final DataType dataType = getValue(SETTING_PRINCIPAL_TYPECODE, DataType.class);
        return dataType != null && dataType.getClazz().isAssignableFrom(clazz);
    }


    @Required
    public void setPermissionManagementFacade(final PermissionManagementFacade permissionManagementFacade)
    {
        this.permissionManagementFacade = permissionManagementFacade;
    }


    protected PermissionManagementFacade getPermissionManagementFacade()
    {
        return permissionManagementFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setExpressionResolverFactory(final ExpressionResolverFactory expressionResolverFactory)
    {
        this.expressionResolverFactory = expressionResolverFactory;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
