package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassAttrEditorSectionConfiguration extends DefaultEditorSectionConfiguration implements CustomEditorSectionConfiguration
{
    private ObjectType objectType;
    private TypedObject currentObject;


    public ClassAttrEditorSectionConfiguration()
    {
    }


    public ClassAttrEditorSectionConfiguration(String qualifier)
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
                if(extType instanceof de.hybris.platform.cockpit.model.meta.impl.ClassificationType)
                {
                    ArrayList<PropertyDescriptor> extTypePds = new ArrayList<>(extType.getPropertyDescriptors());
                    Collections.sort(extTypePds, (Comparator<? super PropertyDescriptor>)new Object(this));
                    for(PropertyDescriptor pd : extTypePds)
                    {
                        if(props.add(pd))
                        {
                            rows.add(new PropertyEditorRowConfiguration(pd, true, true));
                        }
                    }
                }
            }
        }
        setSectionRows(rows);
    }


    protected int compareClassDescriptors(ClassAttributePropertyDescriptor pd1, ClassAttributePropertyDescriptor pd2)
    {
        ClassificationClass cc1 = pd1.getClassificationClass();
        ClassificationClass cc2 = pd2.getClassificationClass();
        int classCompare = compareClassClasses(cc1, cc2) * 1000;
        int pos1 = (pd1.getPosition() == null) ? 0 : pd1.getPosition().intValue();
        int pos2 = (pd2.getPosition() == null) ? 0 : pd2.getPosition().intValue();
        if(pos1 == pos2)
        {
            return classCompare;
        }
        return classCompare + pos1 - pos2;
    }


    private int compareClassClasses(ClassificationClass cc1, ClassificationClass cc2)
    {
        int parentDist = getParentDistance(cc2, cc1);
        if(parentDist >= 0)
        {
            return parentDist;
        }
        parentDist = getParentDistance(cc1, cc2);
        if(parentDist >= 0)
        {
            return -1 * parentDist;
        }
        return 0;
    }


    private int getParentDistance(ClassificationClass cclass, ClassificationClass parent)
    {
        List<ClassificationClass> parents = new ArrayList<>();
        parents.add(cclass);
        int counter = 0;
        int distance = -1;
        while(!parents.isEmpty() && counter < 50)
        {
            if(parents.contains(parent))
            {
                distance = counter;
                break;
            }
            List<ClassificationClass> newParents = new ArrayList<>();
            for(ClassificationClass cc : parents)
            {
                newParents.addAll(cc.getSuperClasses());
            }
            parents = newParents;
            counter++;
        }
        return distance;
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


    public ClassAttrEditorSectionConfiguration clone() throws CloneNotSupportedException
    {
        ClassAttrEditorSectionConfiguration ret = (ClassAttrEditorSectionConfiguration)super.clone();
        ret.objectType = this.objectType;
        ret.currentObject = this.currentObject;
        return ret;
    }
}
