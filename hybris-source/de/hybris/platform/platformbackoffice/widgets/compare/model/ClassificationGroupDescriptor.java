package de.hybris.platform.platformbackoffice.widgets.compare.model;

import com.hybris.cockpitng.compare.model.CompareAttributeDescriptor;
import com.hybris.cockpitng.compare.model.GroupDescriptor;
import java.util.ArrayList;
import java.util.List;

public class ClassificationGroupDescriptor extends GroupDescriptor
{
    final List<ClassificationDescriptor> classificationDescriptors;


    public ClassificationGroupDescriptor(String name, List<CompareAttributeDescriptor> compareAttributes)
    {
        super(name, compareAttributes);
        this.classificationDescriptors = new ArrayList<>();
    }


    public ClassificationGroupDescriptor(String name, List<CompareAttributeDescriptor> compareAttributes, List<ClassificationDescriptor> classificationDescriptors)
    {
        super(name, compareAttributes);
        this.classificationDescriptors = classificationDescriptors;
    }


    public List<ClassificationDescriptor> getClassificationDescriptors()
    {
        return this.classificationDescriptors;
    }
}
