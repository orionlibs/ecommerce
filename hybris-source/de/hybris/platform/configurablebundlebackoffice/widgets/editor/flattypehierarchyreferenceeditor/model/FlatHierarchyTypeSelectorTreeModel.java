package de.hybris.platform.configurablebundlebackoffice.widgets.editor.flattypehierarchyreferenceeditor.model;

import com.hybris.cockpitng.common.model.TypeSelectorTreeModel;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;

public class FlatHierarchyTypeSelectorTreeModel extends TypeSelectorTreeModel
{
    public FlatHierarchyTypeSelectorTreeModel(DataType root, TypeFacade typeFacade, PermissionFacade permissionFacade)
    {
        super(root, typeFacade, permissionFacade);
    }


    protected List<DataType> getVisibleDirectSubtypes(DataType dataType)
    {
        if(!getRootType().getCode().equals(dataType.getCode()))
        {
            return Collections.emptyList();
        }
        return (List<DataType>)getAllVisibleSubtypesHierarchy(dataType).stream()
                        .map(type -> CollectionUtils.isNotEmpty(type.getSubtypes()) ? cloneDataTypeExcludingSubtypes(type) : type)
                        .collect(Collectors.toList());
    }


    protected DataType cloneDataTypeExcludingSubtypes(DataType type)
    {
        DataType.Builder builder = (new DataType.Builder(type.getCode())).type(type.getType()).abstractType(type.isAbstract()).allSuperTypes(getRootType().getAllSuperTypes()).supertype(getRootType().getCode()).searchable(type.isSearchable());
        type.getAttributes().forEach(a -> builder.attribute(a));
        return builder.build();
    }


    protected List<DataType> getAllVisibleSubtypesHierarchy(DataType dataType)
    {
        List<DataType> result = Lists.newArrayList();
        List<DataType> visibleDirectSubtypes = super.getVisibleDirectSubtypes(dataType);
        result.addAll(visibleDirectSubtypes);
        for(DataType type : visibleDirectSubtypes)
        {
            result.addAll(getAllVisibleSubtypesHierarchy(type));
        }
        return result;
    }
}
