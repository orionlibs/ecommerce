package de.hybris.platform.platformbackoffice.facades.impl;

import com.hybris.backoffice.compare.BackofficeItemComparisonFacade;
import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.GroupDescriptor;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import de.hybris.platform.platformbackoffice.services.BackofficeClassificationService;
import de.hybris.platform.platformbackoffice.widgets.compare.model.BackofficeComparisonResult;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationDescriptor;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationGroupDescriptor;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationValuesContainer;
import de.hybris.platform.platformbackoffice.widgets.compare.model.FeatureDescriptor;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationAwareBackofficeItemComparisonFacade extends BackofficeItemComparisonFacade
{
    private BackofficeClassificationService backofficeClassificationService;
    private ModelService modelService;
    private String classificationGroupName;


    public <T> Optional<ComparisonResult> getCompareViewResult(T referenceObject, Collection<T> compareObjects, Collection<GroupDescriptor> groupDescriptors)
    {
        return getCompareViewResult(referenceObject, compareObjects, groupDescriptors, null);
    }


    public <T> Optional<ComparisonResult> getCompareViewResult(T referenceObject, Collection<T> compareObjects, Collection<GroupDescriptor> groupDescriptors, Supplier objectsReferenceSupplier)
    {
        Optional<ComparisonResult> optionalBaseComparisonResult = super.getCompareViewResult(referenceObject, compareObjects, groupDescriptors);
        if(!(referenceObject instanceof ProductModel))
        {
            return optionalBaseComparisonResult;
        }
        List<Object> objectsToCompare = (List<Object>)compareObjects.stream().distinct().collect(Collectors.toList());
        objectsToCompare.remove(referenceObject);
        ProductModel referenceProduct = (ProductModel)referenceObject;
        List<ClassificationDescriptor> classificationDescriptors = getBackofficeClassificationService().getClassificationDescriptors(Collections.singleton(referenceProduct));
        List<ClassificationGroupDescriptor> classificationGroupDescriptors = Collections.singletonList(new ClassificationGroupDescriptor(
                        getClassificationGroupName(), Collections.emptyList(), classificationDescriptors));
        List<ClassificationValuesContainer> featureValuesContainers = prepareFeatureValuesContainerList(classificationGroupDescriptors, compareObjects, objectsReferenceSupplier);
        if(CollectionUtils.isNotEmpty(featureValuesContainers))
        {
            ClassificationValuesContainer refObjectWithFeatures = createObjectFeaturesValueContainer(classificationGroupDescriptors, referenceProduct, objectsReferenceSupplier);
            BackofficeComparisonResult backofficeComparisonResult = computeCompareViewResult(refObjectWithFeatures, featureValuesContainers, new HashSet<>(classificationGroupDescriptors));
            optionalBaseComparisonResult
                            .ifPresent(baseComparisonResult -> featureComparisonResult.merge(baseComparisonResult, null));
            return (Optional)Optional.of(backofficeComparisonResult);
        }
        return Optional.empty();
    }


    protected <T> T reloadObject(T object)
    {
        if(object instanceof ItemModel)
        {
            object = (T)getModelService().get(((ItemModel)object).getPk());
        }
        return object;
    }


    protected List<ClassificationValuesContainer> prepareFeatureValuesContainerList(Collection<ClassificationGroupDescriptor> classificationGroupDescriptors, Collection<?> compareObjects, Supplier objectsReferenceSupplier)
    {
        return (List<ClassificationValuesContainer>)compareObjects.stream().map(compareObject -> createObjectFeaturesValueContainer(classificationGroupDescriptors, compareObject, objectsReferenceSupplier))
                        .collect(Collectors.toList());
    }


    protected ClassificationValuesContainer createObjectFeaturesValueContainer(Collection<ClassificationGroupDescriptor> groupDescriptors, Object object, Supplier<Map> objectsReferenceSupplier)
    {
        Object objectId = getObjectFacade().getObjectId(object);
        ClassificationValuesContainer classificationValuesContainer = new ClassificationValuesContainer(objectId);
        List<ClassificationDescriptor> classificationDescriptors = (List<ClassificationDescriptor>)groupDescriptors.stream().flatMap(group -> group.getClassificationDescriptors().stream()).collect(Collectors.toList());
        Map<String, Map<FeatureDescriptor, ClassificationInfo>> featureValues = new HashMap<>();
        if(object instanceof ProductModel)
        {
            if(objectsReferenceSupplier != null && objectsReferenceSupplier.get() != null && objectsReferenceSupplier
                            .get() instanceof Map && ((Map)objectsReferenceSupplier.get()).containsKey(object))
            {
                Map objectsReferenceMap = objectsReferenceSupplier.get();
                featureValues.putAll(getBackofficeClassificationService().getFeatureValuesFromFeatureList(classificationDescriptors, (FeatureList)objectsReferenceMap
                                .get(object)));
            }
            else
            {
                featureValues.putAll(
                                getBackofficeClassificationService().getFeatureValues((ProductModel)object, classificationDescriptors));
            }
        }
        classificationValuesContainer.getFeatureValues().putAll(featureValues);
        return classificationValuesContainer;
    }


    protected BackofficeComparisonResult computeCompareViewResult(ClassificationValuesContainer referenceContainer, List<ClassificationValuesContainer> compareContainers, Set<ClassificationGroupDescriptor> classificationGroupDescriptors)
    {
        Map<ClassificationValuesContainer, Set<FeatureDescriptor>> featureDifferences = getDifferencesForFeatures(referenceContainer, compareContainers, classificationGroupDescriptors);
        return new BackofficeComparisonResult(referenceContainer.getObjectId(), Collections.emptyMap(), Collections.emptySet(), featureDifferences, classificationGroupDescriptors);
    }


    protected Map<ClassificationValuesContainer, Set<FeatureDescriptor>> getDifferencesForFeatures(ClassificationValuesContainer referenceContainer, List<ClassificationValuesContainer> compareContainers, Collection<ClassificationGroupDescriptor> classificationGroupDescriptors)
    {
        Map<ClassificationValuesContainer, Set<FeatureDescriptor>> differences = new HashMap<>();
        classificationGroupDescriptors.stream().map(ClassificationGroupDescriptor::getClassificationDescriptors)
                        .flatMap(Collection::stream).filter(classificationDescriptor -> classificationDescriptor.getCatalogDescriptor().canRead())
                        .forEach(classificationDescriptor -> classificationDescriptor.getFeatures().forEach(()));
        return differences;
    }


    protected boolean isEqualFeatureValue(ClassificationInfo firstFeatureValue, ClassificationInfo secondFeatureValue)
    {
        Object firstValue = null;
        if(firstFeatureValue != null)
        {
            firstValue = firstFeatureValue.getValue();
        }
        Object secondValue = null;
        if(secondFeatureValue != null)
        {
            secondValue = secondFeatureValue.getValue();
        }
        return getObjectAttributeComparator().isEqual(firstValue, secondValue);
    }


    protected BackofficeClassificationService getBackofficeClassificationService()
    {
        return this.backofficeClassificationService;
    }


    @Required
    public void setBackofficeClassificationService(BackofficeClassificationService backofficeClassificationService)
    {
        this.backofficeClassificationService = backofficeClassificationService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected String getClassificationGroupName()
    {
        return this.classificationGroupName;
    }


    @Required
    public void setClassificationGroupName(String classificationGroupName)
    {
        this.classificationGroupName = classificationGroupName;
    }
}
