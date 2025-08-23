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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnassignedEditorSectionConfiguration extends DefaultEditorSectionConfiguration implements CustomEditorSectionConfiguration
{
    private ObjectType objectType;
    private TypedObject currentObject;


    public UnassignedEditorSectionConfiguration()
    {
    }


    public UnassignedEditorSectionConfiguration(String qualifier)
    {
        super(qualifier);
    }


    public void initialize(EditorConfiguration config, ObjectType objectType, TypedObject object)
    {
        this.objectType = objectType;
        this.currentObject = object;
    }


    public void allInitialized(EditorConfiguration config, ObjectType objectType, TypedObject currentObject)
    {
        Set<PropertyDescriptor> allDescriptors = getAllPropertyDescriptors();
        for(EditorSectionConfiguration secConf : config.getSections())
        {
            if(!(secConf instanceof UnassignedEditorSectionConfiguration))
            {
                for(EditorRowConfiguration rowConf : secConf.getSectionRows())
                {
                    allDescriptors.remove(rowConf.getPropertyDescriptor());
                }
            }
        }
        List<EditorRowConfiguration> rows = new ArrayList<>();
        BaseType thisBaseType = null;
        if(currentObject != null)
        {
            thisBaseType = currentObject.getType();
        }
        else if(objectType != null)
        {
            if(objectType instanceof ObjectTemplate)
            {
                thisBaseType = ((ObjectTemplate)objectType).getBaseType();
            }
            else
            {
                thisBaseType = (BaseType)objectType;
            }
        }
        for(PropertyDescriptor pd : allDescriptors)
        {
            boolean readable = getUIAccessRightService().isReadable((ObjectType)thisBaseType, pd, (this.currentObject == null));
            if(readable)
            {
                rows.add(new PropertyEditorRowConfiguration(pd, true, true));
            }
        }
        setSectionRows(rows);
    }


    protected Set<PropertyDescriptor> getAllPropertyDescriptors()
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
            all.addAll(baseType.getPropertyDescriptors());
        }
        if(this.currentObject != null)
        {
            for(ExtendedType extType : this.currentObject.getExtendedTypes())
            {
                all.addAll(extType.getPropertyDescriptors());
            }
        }
        return all;
    }


    public SectionRenderer getCustomRenderer()
    {
        return null;
    }


    public List<EditorSectionConfiguration> getAdditionalSections()
    {
        return Collections.EMPTY_LIST;
    }


    public void loadValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    public void saveValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    public UnassignedEditorSectionConfiguration clone() throws CloneNotSupportedException
    {
        UnassignedEditorSectionConfiguration ret = (UnassignedEditorSectionConfiguration)super.clone();
        ret.objectType = this.objectType;
        ret.currentObject = this.currentObject;
        return ret;
    }


    protected UIAccessRightService getUIAccessRightService()
    {
        return UISessionUtils.getCurrentSession().getUiAccessRightService();
    }
}
