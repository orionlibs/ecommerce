package de.hybris.platform.configurablebundlecockpits.admincockpit.services.security.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.security.impl.DefaultUIAccessRightService;
import de.hybris.platform.configurablebundlecockpits.admincockpit.services.security.data.UIAccessRightDependency;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class BundleUIAccessRightService extends DefaultUIAccessRightService
{
    private static final Logger LOG = Logger.getLogger(BundleUIAccessRightService.class);
    private List<UIAccessRightDependency> dependencies;
    private Map<String, UIAccessRightDependency> dependencyMap;


    public boolean isWritable(ObjectType type, TypedObject item, PropertyDescriptor propDescr, boolean creationMode)
    {
        if(this.dependencyMap == null)
        {
            fillDependencyMap();
        }
        if(MapUtils.isNotEmpty(this.dependencyMap) && isDependencyMapContainsQualifier(propDescr))
        {
            boolean isWritable = isAttributeWritable(item, this.dependencyMap.get(propDescr.getQualifier().toLowerCase()));
            if(!isWritable)
            {
                return false;
            }
        }
        return super.isWritable(type, item, propDescr, creationMode);
    }


    private boolean isDependencyMapContainsQualifier(PropertyDescriptor propDescr)
    {
        return (propDescr != null && propDescr.getQualifier() != null && this.dependencyMap
                        .containsKey(propDescr.getQualifier().toLowerCase(Locale.getDefault())));
    }


    protected boolean isAttributeWritable(TypedObject item, UIAccessRightDependency dependency)
    {
        if(!(item.getObject() instanceof ItemModel))
        {
            return true;
        }
        ItemModel itemModel = (ItemModel)item.getObject();
        Object attributeValue = getModelService().getAttributeValue(itemModel, dependency.getDependentOnAttributeName());
        if(Boolean.FALSE.equals(dependency.getIsNull()) && attributeValue == null)
        {
            return false;
        }
        if(Boolean.TRUE.equals(dependency.getIsNull()) && attributeValue != null)
        {
            return false;
        }
        return true;
    }


    protected void fillDependencyMap()
    {
        if(CollectionUtils.isEmpty(getDependencies()))
        {
            this.dependencyMap = Collections.emptyMap();
            return;
        }
        if(MapUtils.isEmpty(this.dependencyMap))
        {
            if(this.dependencyMap == null)
            {
                this.dependencyMap = new HashMap<>();
            }
            for(UIAccessRightDependency dependency : getDependencies())
            {
                fillDependency(dependency);
            }
        }
    }


    private void fillDependency(UIAccessRightDependency dependency)
    {
        String qualifier = dependency.getTypeCode() + "." + dependency.getTypeCode();
        PropertyDescriptor propDescKey = getTypeService().getPropertyDescriptor(qualifier);
        String dependentOnQualifier = dependency.getTypeCode() + "." + dependency.getTypeCode();
        PropertyDescriptor propDescDependentOnKey = getTypeService().getPropertyDescriptor(dependentOnQualifier);
        if(propDescKey == null || propDescDependentOnKey == null)
        {
            LOG.error("Cannot find attribute '" + ((propDescKey == null) ? qualifier : dependentOnQualifier) + "'; check configuration of dependencies in spring config");
            return;
        }
        int pos = propDescKey.getQualifier().indexOf('.');
        dependency.setAttributeName(propDescKey.getQualifier().substring(pos + 1));
        pos = propDescDependentOnKey.getQualifier().indexOf('.');
        dependency.setDependentOnAttributeName(propDescDependentOnKey.getQualifier().substring(pos + 1));
        if(dependency.getIsNull() == null)
        {
            dependency.setIsNull(Boolean.FALSE);
        }
        this.dependencyMap.put(qualifier.toLowerCase(), dependency);
        fillSubTypesDependencies(dependency, qualifier);
    }


    private void fillSubTypesDependencies(UIAccessRightDependency dependency, String qualifier)
    {
        ObjectType type = getTypeService().getObjectTypeFromPropertyQualifier(qualifier);
        Set<ObjectType> subTypes = getTypeService().getAllSubtypes(type);
        for(ObjectType subType : subTypes)
        {
            String subQualifier = subType.getCode() + "." + subType.getCode();
            if(this.dependencyMap.containsKey(subQualifier.toLowerCase()))
            {
                continue;
            }
            UIAccessRightDependency subTypeDependency = new UIAccessRightDependency();
            subTypeDependency.setTypeCode(subType.getCode());
            subTypeDependency.setAttributeName(dependency.getAttributeName());
            subTypeDependency.setDependentOnAttributeName(dependency.getDependentOnAttributeName());
            subTypeDependency.setIsNull(dependency.getIsNull());
            this.dependencyMap.put(subQualifier.toLowerCase(), subTypeDependency);
        }
    }


    protected List<UIAccessRightDependency> getDependencies()
    {
        return this.dependencies;
    }


    @Required
    public void setDependencies(List<UIAccessRightDependency> dependencies)
    {
        this.dependencies = dependencies;
    }
}
