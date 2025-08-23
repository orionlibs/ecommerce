package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WidgetParamEditorSectionConfiguration extends DefaultEditorSectionConfiguration implements CustomEditorSectionConfiguration
{
    protected ObjectType objectType;
    protected TypedObject currentObject;


    public WidgetParamEditorSectionConfiguration()
    {
    }


    public WidgetParamEditorSectionConfiguration(String qualifier)
    {
        super(qualifier);
    }


    public void initialize(EditorConfiguration config, ObjectType type, TypedObject object)
    {
        this.objectType = type;
        this.currentObject = object;
        reset();
    }


    public void allInitialized(EditorConfiguration config, ObjectType type, TypedObject currentObject)
    {
        Set<PropertyDescriptor> placedDescriptors = new HashSet<>();
        for(EditorSectionConfiguration secConf : config.getSections())
        {
            if(!equals(secConf))
            {
                for(EditorRowConfiguration rowConf : secConf.getSectionRows())
                {
                    placedDescriptors.add(rowConf.getPropertyDescriptor());
                }
            }
        }
        List<EditorRowConfiguration> rows = new ArrayList<>();
        for(EditorRowConfiguration row : getSectionRows())
        {
            if(!placedDescriptors.contains(row.getPropertyDescriptor()))
            {
                rows.add(row);
            }
        }
        setSectionRows(rows);
    }


    protected void reset()
    {
        List<EditorRowConfiguration> rows = new ArrayList<>();
        if(this.currentObject != null)
        {
            Set<PropertyDescriptor> props = new HashSet<>();
            Collection<ExtendedType> extTypes = this.currentObject.getExtendedTypes();
            for(ExtendedType extType : extTypes)
            {
                if(extType instanceof de.hybris.platform.cockpit.model.meta.impl.WidgetType)
                {
                    ArrayList<PropertyDescriptor> extTypePds = new ArrayList<>(extType.getPropertyDescriptors());
                    for(PropertyDescriptor pd : extTypePds)
                    {
                        if(props.add(pd))
                        {
                            PropertyEditorRowConfiguration rowConfiguration = new PropertyEditorRowConfiguration(pd, true, true);
                            if("DATE".equals(pd.getEditorType()))
                            {
                                DateFormat dateInstance = DateFormat.getDateInstance(3,
                                                UISessionUtils.getCurrentSession().getLocale());
                                if(dateInstance instanceof SimpleDateFormat)
                                {
                                    rowConfiguration.setParameter("dateFormat", ((SimpleDateFormat)dateInstance).toPattern());
                                }
                            }
                            rows.add(rowConfiguration);
                        }
                    }
                }
            }
        }
        setSectionRows(rows);
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


    public WidgetParamEditorSectionConfiguration clone() throws CloneNotSupportedException
    {
        WidgetParamEditorSectionConfiguration ret = (WidgetParamEditorSectionConfiguration)super.clone();
        ret.objectType = this.objectType;
        ret.currentObject = this.currentObject;
        return ret;
    }
}
