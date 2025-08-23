package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InitialAttrEditorSectionConfiguration extends DefaultEditorSectionConfiguration implements CustomEditorSectionConfiguration
{
    private ObjectType objectType;
    private TypedObject currentObject;


    public void initialize(EditorConfiguration config, ObjectType type, TypedObject object)
    {
        this.objectType = type;
        this.currentObject = object;
    }


    public void allInitialized(EditorConfiguration config, ObjectType objectType, TypedObject currentObject)
    {
        Set<PropertyDescriptor> initialDescriptors = getInitialPropertyDescriptors();
        Set<PropertyDescriptor> defaultValueProperties = getPropertiesWithDefaultValues(objectType);
        for(PropertyDescriptor pd : defaultValueProperties)
        {
            initialDescriptors.remove(pd);
        }
        Map<PropertyDescriptor, EditorRowConfiguration> initialRows = new HashMap<>();
        List<EditorRowConfiguration> configuredInitialRows = new ArrayList<>();
        for(EditorSectionConfiguration secConf : config.getSections())
        {
            List<EditorRowConfiguration> rows = secConf.getSectionRows();
            List<EditorRowConfiguration> modifiedRows = new ArrayList<>(rows.size());
            boolean excluded = false;
            for(EditorRowConfiguration rowConf : rows)
            {
                if(!initialDescriptors.contains(rowConf.getPropertyDescriptor()))
                {
                    modifiedRows.add(rowConf);
                    continue;
                }
                initialRows.put(rowConf.getPropertyDescriptor(), rowConf);
                configuredInitialRows.add(rowConf);
                rowConf.setEditable(true);
                excluded = true;
            }
            if(excluded)
            {
                secConf.setSectionRows(modifiedRows);
            }
        }
        for(PropertyDescriptor pd : initialDescriptors)
        {
            boolean readable = getUIAccessRightService().isReadable(objectType, pd, true);
            if(readable && !initialRows.containsKey(pd))
            {
                PropertyEditorRowConfiguration propertyEditorRowConfiguration = new PropertyEditorRowConfiguration(pd, true, true);
                propertyEditorRowConfiguration.setEditable(true);
                configuredInitialRows.add(propertyEditorRowConfiguration);
            }
        }
        setSectionRows(configuredInitialRows);
    }


    protected Set<PropertyDescriptor> getPropertiesWithDefaultValues(ObjectType type)
    {
        ObjectTemplate template = null;
        if(type instanceof ObjectTemplate)
        {
            template = (ObjectTemplate)type;
        }
        else if(type instanceof BaseType)
        {
            template = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(type.getCode());
        }
        Set<PropertyDescriptor> props = Collections.EMPTY_SET;
        if(template != null)
        {
            Set<String> languageIsos = UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos();
            Map<PropertyDescriptor, Object> defaultValues = TypeTools.getAllDefaultValues(UISessionUtils.getCurrentSession()
                            .getTypeService(), template, languageIsos);
            if(!defaultValues.isEmpty())
            {
                props = new HashSet<>(defaultValues.size());
                for(PropertyDescriptor pd : defaultValues.keySet())
                {
                    props.add(pd);
                }
            }
        }
        return props;
    }


    protected Set<PropertyDescriptor> getInitialPropertyDescriptors()
    {
        Set<PropertyDescriptor> all = new HashSet<>();
        BaseType baseType = null;
        if(this.currentObject != null)
        {
            baseType = this.currentObject.getType();
        }
        else if(this.objectType != null)
        {
            if(this.objectType instanceof BaseType)
            {
                baseType = (BaseType)this.objectType;
            }
            else if(this.objectType instanceof ObjectTemplate)
            {
                baseType = ((ObjectTemplate)this.objectType).getBaseType();
            }
        }
        if(baseType != null)
        {
            for(PropertyDescriptor pd : baseType.getPropertyDescriptors())
            {
                if(TypeTools.isMandatory(pd, true))
                {
                    all.add(pd);
                }
            }
        }
        if(this.currentObject != null)
        {
            for(ExtendedType extType : this.currentObject.getExtendedTypes())
            {
                for(PropertyDescriptor pd : extType.getPropertyDescriptors())
                {
                    if(TypeTools.isMandatory(pd, true))
                    {
                        all.add(pd);
                    }
                }
            }
        }
        return all;
    }


    public List<EditorSectionConfiguration> getAdditionalSections()
    {
        return Collections.EMPTY_LIST;
    }


    public SectionRenderer getCustomRenderer()
    {
        return null;
    }


    public void loadValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    public void saveValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    public InitialAttrEditorSectionConfiguration clone() throws CloneNotSupportedException
    {
        InitialAttrEditorSectionConfiguration ret = (InitialAttrEditorSectionConfiguration)super.clone();
        ret.objectType = this.objectType;
        ret.currentObject = this.currentObject;
        return ret;
    }


    protected UIAccessRightService getUIAccessRightService()
    {
        return UISessionUtils.getCurrentSession().getUiAccessRightService();
    }
}
