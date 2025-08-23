package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.values.ObjectAttributeComparator;
import de.hybris.platform.cockpit.services.values.ObjectCompareService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.PropertyComparisonInfo;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultObjectCompareService extends AbstractServiceImpl implements ObjectCompareService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultObjectCompareService.class);
    private ObjectAttributeComparator attributeComparator = null;


    private ObjectValueContainer createCompleteValueContainer(TypedObject object)
    {
        Set<PropertyDescriptor> propertyDescriptors = new HashSet<>(object.getType().getPropertyDescriptors());
        for(ExtendedType extType : object.getExtendedTypes())
        {
            propertyDescriptors.addAll(extType.getPropertyDescriptors());
        }
        return createValueContainer(object, propertyDescriptors);
    }


    private ObjectValueContainer createValueContainer(TypedObject object, Set<PropertyDescriptor> propertyDescriptors)
    {
        return TypeTools.createValueContainer(object, propertyDescriptors, UISessionUtils.getCurrentSession().getSystemService()
                        .getAvailableLanguageIsos());
    }


    public Map<PropertyDescriptor, PropertyComparisonInfo> getComparedAttributes(TypedObject referenceObject, List<TypedObject> compareObjects)
    {
        return getComparedAttributes(referenceObject, compareObjects, getAttributeComparator());
    }


    public Map<PropertyDescriptor, PropertyComparisonInfo> getComparedAttributes(TypedObject referenceObject, List<TypedObject> compareObjects, ObjectAttributeComparator comparator)
    {
        ObjectValueContainer refContainer = createCompleteValueContainer(referenceObject);
        List<ObjectValueContainer> compareContainers = new ArrayList<>();
        for(TypedObject compObject : compareObjects)
        {
            compareContainers.add(createCompleteValueContainer(compObject));
        }
        return getComparedAttributes(refContainer, compareContainers, comparator);
    }


    public Map<PropertyDescriptor, PropertyComparisonInfo> getComparedAttributes(ObjectValueContainer referenceValueContainer, List<ObjectValueContainer> compareValueContainers)
    {
        return getComparedAttributes(referenceValueContainer, compareValueContainers, getAttributeComparator());
    }


    public Map<PropertyDescriptor, PropertyComparisonInfo> getComparedAttributes(ObjectValueContainer referenceValueContainer, List<ObjectValueContainer> compareValueContainers, ObjectAttributeComparator comparator)
    {
        Map<PropertyDescriptor, PropertyComparisonInfo> result = new HashMap<>();
        Set<PropertyDescriptor> mergedDescriptorSet = new HashSet<>();
        mergedDescriptorSet.addAll(referenceValueContainer.getPropertyDescriptors());
        for(ObjectValueContainer valCont : compareValueContainers)
        {
            mergedDescriptorSet.addAll(valCont.getPropertyDescriptors());
        }
        for(PropertyDescriptor descriptor : mergedDescriptorSet)
        {
            PropertyComparisonInfoImpl comparisonInfo = new PropertyComparisonInfoImpl(descriptor);
            result.put(descriptor, comparisonInfo);
            if(descriptor.isLocalized())
            {
                for(String langiso : UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos())
                {
                    if(!addValuesToComparisonInfo(comparisonInfo, referenceValueContainer, descriptor, langiso, comparator, compareValueContainers))
                    {
                        return Collections.EMPTY_MAP;
                    }
                }
                continue;
            }
            if(!addValuesToComparisonInfo(comparisonInfo, referenceValueContainer, descriptor, null, comparator, compareValueContainers))
            {
                return Collections.EMPTY_MAP;
            }
        }
        return result;
    }


    private boolean addValuesToComparisonInfo(PropertyComparisonInfoImpl comparisonInfo, ObjectValueContainer referenceValueContainer, PropertyDescriptor descriptor, String langIso, ObjectAttributeComparator comparator, List<ObjectValueContainer> compareValueContainers)
    {
        ObjectValueContainer.ObjectValueHolder referenceValueHolder = referenceValueContainer.hasProperty(descriptor) ? referenceValueContainer.getValue(descriptor, langIso) : null;
        TypedObject refObject = extractTypedObject(referenceValueContainer);
        if(refObject == null)
        {
            LOG.error("Value containers object '" + referenceValueContainer.getObject() + "' is not a Typed Object. Compare operation aborted.");
            return false;
        }
        for(ObjectValueContainer valCont : compareValueContainers)
        {
            TypedObject typedObject = extractTypedObject(valCont);
            if(typedObject == null)
            {
                LOG.error("Value containers object '" + valCont.getObject() + "' is not a Typed Object. Compare operation aborted.");
                return false;
            }
            ObjectValueContainer.ObjectValueHolder compareValueHolder = valCont.hasProperty(descriptor) ? valCont.getValue(descriptor, langIso) : null;
            comparisonInfo.addValueHolderEntry(langIso, referenceValueHolder, typedObject, compareValueHolder, (referenceValueHolder == null || compareValueHolder == null ||
                            !comparator.isEqual(new ObjectValuePair(refObject, referenceValueHolder), new ObjectValuePair(typedObject, compareValueHolder))));
        }
        return true;
    }


    private TypedObject extractTypedObject(ObjectValueContainer valueContainer)
    {
        Object object = valueContainer.getObject();
        if(object != null)
        {
            return UISessionUtils.getCurrentSession().getTypeService().wrapItem(object);
        }
        return null;
    }


    public void setAttributeComparator(ObjectAttributeComparator attributeComparator)
    {
        this.attributeComparator = attributeComparator;
    }


    public ObjectAttributeComparator getAttributeComparator()
    {
        if(this.attributeComparator == null)
        {
            this.attributeComparator = (ObjectAttributeComparator)new DefaultObjectAttributeComparator();
        }
        return this.attributeComparator;
    }
}
