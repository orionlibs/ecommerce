/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.model;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorRenderProhibitingPredicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zul.AbstractTreeModel;

/**
 * Universal Tree Model for Type Selectors
 */
public class TypeSelectorTreeModel extends AbstractTreeModel<DataType>
{
    private static final DataType ABSTRACT_ROOT = (new DataType.Builder("#__ABSTRACT_ROOT")).build();
    private static final long serialVersionUID = -1066890812640343911L;
    private static final Logger LOG = LoggerFactory.getLogger(TypeSelectorTreeModel.class);
    private final transient DataType rootType;
    private final transient TypeFacade typeFacade;
    private final transient PermissionFacade permissionFacade;
    private final transient Map<String, List<DataType>> visibleSubdirectoriesCache = new HashMap<>();
    private transient ReferenceEditorRenderProhibitingPredicate referenceEditorRenderProhibitingPredicate;
    private transient String filter = StringUtils.EMPTY;
    private transient Locale filterLocale = Locale.ENGLISH;
    private transient boolean hideTypesWithoutClazz;


    public TypeSelectorTreeModel(final DataType root, final TypeFacade typeFacade, final PermissionFacade permissionFacade)
    {
        this(root, typeFacade, permissionFacade, false);
    }


    public TypeSelectorTreeModel(final DataType root, final TypeFacade typeFacade, final PermissionFacade permissionFacade,
                    final boolean hideTypesWithoutClazz)
    {
        super(ABSTRACT_ROOT);
        this.typeFacade = typeFacade;
        this.permissionFacade = permissionFacade;
        Validate.notNull("Root type may not be null", root);
        rootType = root;
        this.hideTypesWithoutClazz = hideTypesWithoutClazz;
    }


    private static boolean isRootType(final DataType type)
    {
        return ABSTRACT_ROOT.getCode().equals(type.getCode());
    }


    private static String resolveDataTypeLabel(final DataType datatype, final Locale filterLocale)
    {
        final String label = datatype.getLabel(filterLocale);
        return label != null ? label : datatype.getCode();
    }


    private boolean hasPermissionToReadRoot()
    {
        return getPermissionFacade().canReadType(rootType.getCode());
    }


    @Override
    public boolean isLeaf(final DataType type)
    {
        if(isRootType(type))
        {
            if(hasPermissionToReadRoot() && isDataTypeFiltered(getRootType()))
            {
                return false;
            }
            return CollectionUtils.isEmpty(filterListTypes(getVisibleDirectSubtypes(getRootType())));
        }
        return CollectionUtils.isEmpty(filterListTypes(getVisibleDirectSubtypes(type)));
    }


    @Override
    public DataType getChild(final DataType type, final int index)
    {
        if(isRootType(type))
        {
            if(hasPermissionToReadRoot() && isDataTypeFiltered(getRootType()))
            {
                return rootType;
            }
            return filterListType(getVisibleDirectSubtypes(getRootType()), index);
        }
        return filterListType(getVisibleDirectSubtypes(type), index);
    }


    @Override
    public int getChildCount(final DataType type)
    {
        if(isRootType(type))
        {
            if(hasPermissionToReadRoot() && isDataTypeFiltered(getRootType()))
            {
                return 1;
            }
            return filterListTypes(getVisibleDirectSubtypes(getRootType())).size();
        }
        return filterListTypes(getVisibleDirectSubtypes(type)).size();
    }


    protected List<DataType> getVisibleDirectSubtypes(final DataType dataType)
    {
        if(getVisibleSubdirectoriesCache().containsKey(dataType.getCode()))
        {
            return getVisibleSubdirectoriesCache().get(dataType.getCode());
        }
        final List<DataType> directSubtypesWithPermission = new ArrayList<>();
        for(final String subtype : dataType.getSubtypes())
        {
            final DataType subtypeDataType = loadTypeUnchecked(subtype);
            final boolean isSubtypeFiltered = isDataTypeFiltered(subtypeDataType);
            if(isSearchableType(subtype) && isSubtypeFiltered)
            {
                directSubtypesWithPermission.add(subtypeDataType);
            }
            else
            {
                final List<DataType> visibleSubtypesForType = new ArrayList<>();
                addVisibleSubtypesForNotVisibleType(subtypeDataType, visibleSubtypesForType);
                directSubtypesWithPermission.addAll(visibleSubtypesForType);
            }
        }
        visibleSubdirectoriesCache.put(dataType.getCode(), directSubtypesWithPermission);
        return directSubtypesWithPermission;
    }


    protected DataType filterListType(final List<DataType> subTypes, final int index)
    {
        return filterListDataTypes(subTypes).get(index);
    }


    protected List<DataType> filterListDataTypes(final List<DataType> subTypes)
    {
        return subTypes.stream().filter(Predicate.not(getReferenceEditorRenderProhibitingPredicate()::isProhibited))
                        .collect(Collectors.toList());
    }


    protected List<String> filterListTypes(final List<DataType> subTypes)
    {
        return filterListDataTypes(subTypes).stream().map(DataType::getCode).collect(Collectors.toList());
    }


    private boolean isDataTypeFiltered(final DataType datatype)
    {
        if(isTypeWithoutClazzHidden(datatype))
        {
            return false;
        }
        if(filteringDisabled())
        {
            return true;
        }
        final String dataTypeLocalizedLabel = resolveDataTypeLabel(datatype, getFilterLocale()).toLowerCase(getFilterLocale());
        return dataTypeLocalizedLabel.contains(getFilter());
    }


    private boolean isTypeWithoutClazzHidden(final DataType datatype)
    {
        return hideTypesWithoutClazz && datatype.getClazz() == null;
    }


    private boolean filteringDisabled()
    {
        return StringUtils.isBlank(getFilter());
    }


    private boolean isSearchableType(final String subtype)
    {
        try
        {
            final DataType type = getTypeFacade().load(subtype);
            return type.isSearchable();
        }
        catch(final TypeNotFoundException tnfe)
        {
            LOG.warn(String.format("Type %s could not be found", subtype), tnfe);
        }
        return false;
    }


    private void addVisibleSubtypesForNotVisibleType(final DataType type, final List<DataType> currentList)
    {
        for(final String subtype : type.getSubtypes())
        {
            final DataType subtypeType = loadTypeUnchecked(subtype);
            final boolean isDataTypeFiltered = isDataTypeFiltered(subtypeType);
            if(isDataTypeFiltered && isSearchableType(subtype))
            {
                currentList.add(subtypeType);
            }
            else
            {
                addVisibleSubtypesForNotVisibleType(subtypeType, currentList);
            }
        }
    }


    private DataType loadTypeUnchecked(final String typeName)
    {
        try
        {
            return getTypeFacade().load(typeName);
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }


    public DataType getRootType()
    {
        return rootType;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public Map<String, List<DataType>> getVisibleSubdirectoriesCache()
    {
        return visibleSubdirectoriesCache;
    }


    protected String getFilter()
    {
        return filter.toLowerCase(getFilterLocale()).trim();
    }


    public void setFilter(final String filter)
    {
        getVisibleSubdirectoriesCache().clear();
        this.filter = filter;
    }


    protected Locale getFilterLocale()
    {
        return filterLocale;
    }


    public void setFilterLocale(final Locale filterLocale)
    {
        this.filterLocale = filterLocale;
    }


    protected ReferenceEditorRenderProhibitingPredicate getReferenceEditorRenderProhibitingPredicate()
    {
        if(referenceEditorRenderProhibitingPredicate == null)
        {
            referenceEditorRenderProhibitingPredicate = (ReferenceEditorRenderProhibitingPredicate)SpringUtil
                            .getApplicationContext().getBean("referenceEditorRenderProhibitingPredicate");
        }
        return referenceEditorRenderProhibitingPredicate;
    }
}
