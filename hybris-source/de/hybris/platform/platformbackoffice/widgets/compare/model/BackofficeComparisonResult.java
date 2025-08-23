package de.hybris.platform.platformbackoffice.widgets.compare.model;

import com.hybris.cockpitng.compare.model.CompareAttributeDescriptor;
import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.GroupDescriptor;
import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class BackofficeComparisonResult extends ComparisonResult
{
    private final Set<Object> objectsWithDifferentFeatures;
    private final Map<FeatureDescriptor, Collection<ClassificationValuesContainer>> differentObjectsForFeatures;
    private final Set<ClassificationDescriptor> differentClassifications;
    private final Map<ClassificationDescriptor, Set<FeatureDescriptor>> featuresByClassificationDescriptor;
    private final Map<String, List<ClassificationDescriptor>> classificationsByGroupName;


    public BackofficeComparisonResult(Object referenceObjectId, Map<ObjectAttributesValueContainer, Set<CompareAttributeDescriptor>> differences, Set<GroupDescriptor> groupDescriptors, Map<ClassificationValuesContainer, Set<FeatureDescriptor>> featureDifferences,
                    Set<ClassificationGroupDescriptor> classificationGroupDescriptors)
    {
        super(referenceObjectId, differences, groupDescriptors);
        Set<FeatureDescriptor> differentFeatures = new HashSet<>();
        Objects.requireNonNull(differentFeatures);
        featureDifferences.values().forEach(differentFeatures::addAll);
        this
                        .objectsWithDifferentFeatures = (Set<Object>)featureDifferences.keySet().stream().map(ObjectAttributesValueContainer::getObjectId).collect(Collectors.toSet());
        this.differentObjectsForFeatures = createDifferentObjectsForFeatures(featureDifferences);
        this.classificationsByGroupName = createClassificationByGroupNameMap(classificationGroupDescriptors);
        this.featuresByClassificationDescriptor = createFeaturesByClassificationMap(classificationGroupDescriptors);
        this.differentClassifications = createDifferentClassifications(differentFeatures);
        this.differentGroups.addAll(getDifferentClassificationGroupNames(classificationGroupDescriptors));
        this.differentObjectsIds.addAll(this.objectsWithDifferentFeatures);
    }


    protected final Map<String, List<ClassificationDescriptor>> createClassificationByGroupNameMap(Set<ClassificationGroupDescriptor> classificationGroupDescriptors)
    {
        Map<String, List<ClassificationDescriptor>> classificationsByGroupNames = new HashMap<>();
        classificationGroupDescriptors.forEach(classificationGroupDescriptor -> classificationsByGroupNames.put(classificationGroupDescriptor.getName(), classificationGroupDescriptor.getClassificationDescriptors()));
        return classificationsByGroupNames;
    }


    protected final Map<ClassificationDescriptor, Set<FeatureDescriptor>> createFeaturesByClassificationMap(Set<ClassificationGroupDescriptor> classificationGroupDescriptors)
    {
        Map<ClassificationDescriptor, Set<FeatureDescriptor>> featuresByClassificationMap = new HashMap<>();
        classificationGroupDescriptors.forEach(groupDescriptor -> groupDescriptor.getClassificationDescriptors().forEach(()));
        return featuresByClassificationMap;
    }


    protected final Set<ClassificationDescriptor> createDifferentClassifications(Set<FeatureDescriptor> differentFeatures)
    {
        return (Set<ClassificationDescriptor>)this.featuresByClassificationDescriptor.keySet().stream()
                        .filter(classification -> !Collections.disjoint(classification.getFeatures(), differentFeatures))
                        .collect(Collectors.toSet());
    }


    protected final Set<String> getDifferentClassificationGroupNames(Set<ClassificationGroupDescriptor> classificationGroupDescriptors)
    {
        return (Set<String>)classificationGroupDescriptors.stream()
                        .filter(group -> !Collections.disjoint(group.getClassificationDescriptors(), this.differentClassifications))
                        .map(GroupDescriptor::getName).collect(Collectors.toSet());
    }


    protected final Map<FeatureDescriptor, Collection<ClassificationValuesContainer>> createDifferentObjectsForFeatures(Map<ClassificationValuesContainer, Set<FeatureDescriptor>> featureDifferences)
    {
        Set<ImmutablePair<FeatureDescriptor, ClassificationValuesContainer>> featureDescriptorWithValuesContainers = new HashSet<>();
        featureDifferences.forEach((key, value) -> value.forEach(()));
        Map<FeatureDescriptor, Collection<ClassificationValuesContainer>> differentObjectsForFeaturesMap = new HashMap<>();
        featureDescriptorWithValuesContainers.forEach(featureDescriptorWithValuesContainer -> {
            FeatureDescriptor featureDescriptor = (FeatureDescriptor)featureDescriptorWithValuesContainer.getLeft();
            differentObjectsForFeaturesMap.putIfAbsent(featureDescriptor, new HashSet());
            ClassificationValuesContainer valuesContainer = (ClassificationValuesContainer)featureDescriptorWithValuesContainer.getRight();
            ((Collection<ClassificationValuesContainer>)differentObjectsForFeaturesMap.get(featureDescriptor)).add(valuesContainer);
        });
        return differentObjectsForFeaturesMap;
    }


    public synchronized boolean merge(ComparisonResult addition, Object comparedObjectId)
    {
        boolean result = super.merge(addition, comparedObjectId);
        if(result && addition instanceof BackofficeComparisonResult && getReferenceObjectId().equals(addition.getReferenceObjectId()))
        {
            BackofficeComparisonResult classificationAddition = (BackofficeComparisonResult)addition;
            updateObjectsForFeaturesDifferences(classificationAddition, comparedObjectId);
            updateDifferentObjects();
            updateClassificationDifferences(classificationAddition);
            updateGroupsDifferences(classificationAddition);
            getClassificationsByGroupName().putAll(classificationAddition.getClassificationsByGroupName());
            return true;
        }
        return false;
    }


    protected void updateObjectsForFeaturesDifferences(BackofficeComparisonResult addition, Object comparedObjectId)
    {
        Set<FeatureDescriptor> keysToRemove = new HashSet<>();
        this.differentObjectsForFeatures.forEach((key, value) -> {
            value.removeIf(());
            if(value.isEmpty())
            {
                keysToRemove.add(key);
            }
        });
        Objects.requireNonNull(this.differentObjectsForFeatures);
        keysToRemove.forEach(this.differentObjectsForFeatures::remove);
        addition.differentObjectsForFeatures
                        .forEach((key, value) -> this.differentObjectsForFeatures.merge(key, new HashSet<>(value), CollectionUtils::union));
    }


    protected void updateDifferentObjects()
    {
        super.updateDifferentObjects();
        Set<Object> newDifferentObjectsIds = (Set<Object>)this.differentObjectsForFeatures.values().stream().flatMap(Collection::stream).map(ObjectAttributesValueContainer::getObjectId).collect(Collectors.toSet());
        this.differentObjectsIds.addAll(newDifferentObjectsIds);
    }


    protected void updateClassificationDifferences(BackofficeComparisonResult backofficeAddition)
    {
        Predicate<ClassificationDescriptor> isClassificationRemovalNeeded = classification -> {
            Objects.requireNonNull(this.differentObjectsForFeatures);
            return classification.getFeatures().stream().map(this.differentObjectsForFeatures::get).filter(Objects::nonNull).allMatch(Collection::isEmpty);
        };
        this.differentClassifications.removeIf(isClassificationRemovalNeeded);
        this.differentClassifications.addAll(backofficeAddition.getDifferentClassifications());
    }


    protected void updateGroupsDifferences(ComparisonResult addition)
    {
        Predicate<String> noAttributeDifferences = group -> ((List)this.groupAttributesByGroupNames.getOrDefault(group, Collections.emptyList())).stream().map(()).filter(Objects::nonNull).allMatch(Collection::isEmpty);
        Predicate<String> noFeatureDifferences = group -> {
            Objects.requireNonNull(this.differentObjectsForFeatures);
            return ((List)this.classificationsByGroupName.getOrDefault(group, Collections.emptyList())).stream().map(ClassificationDescriptor::getFeatures).flatMap(Collection::stream).map(this.differentObjectsForFeatures::get).filter(Objects::nonNull).allMatch(Collection::isEmpty);
        };
        Predicate<String> isGroupRemovalNeeded = Stream.<Predicate<String>>of((Predicate<String>[])new Predicate[] {noAttributeDifferences, noFeatureDifferences}).reduce(Predicate::and).orElse(x -> true);
        this.differentGroups.removeIf(isGroupRemovalNeeded);
        this.differentGroups.addAll(addition.getGroupsWithDifferences());
    }


    public Set<Object> getObjectsWithDifferentFeatures()
    {
        return this.objectsWithDifferentFeatures;
    }


    public Map<FeatureDescriptor, Collection<ClassificationValuesContainer>> getDifferentObjectsForFeatures()
    {
        return this.differentObjectsForFeatures;
    }


    public Set<ClassificationDescriptor> getDifferentClassifications()
    {
        return this.differentClassifications;
    }


    public Map<ClassificationDescriptor, Set<FeatureDescriptor>> getFeaturesByClassificationDescriptor()
    {
        return this.featuresByClassificationDescriptor;
    }


    public Map<String, List<ClassificationDescriptor>> getClassificationsByGroupName()
    {
        return this.classificationsByGroupName;
    }
}
