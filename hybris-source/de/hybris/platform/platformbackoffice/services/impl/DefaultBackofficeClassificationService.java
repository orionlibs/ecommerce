package de.hybris.platform.platformbackoffice.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import de.hybris.platform.platformbackoffice.services.BackofficeClassificationService;
import de.hybris.platform.platformbackoffice.widgets.compare.model.CatalogDescriptor;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationDescriptor;
import de.hybris.platform.platformbackoffice.widgets.compare.model.FeatureDescriptor;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeClassificationService implements BackofficeClassificationService
{
    protected ClassificationService classificationService;
    protected UserService userService;
    protected CatalogVersionService catalogVersionService;


    public Map<String, Map<FeatureDescriptor, ClassificationInfo>> getFeatureValues(ProductModel product, List<ClassificationDescriptor> classificationDescriptors)
    {
        return getFeatureValuesFromFeatureList(classificationDescriptors, this.classificationService.getFeatures(product));
    }


    public Map<String, Map<FeatureDescriptor, ClassificationInfo>> getFeatureValuesFromFeatureList(List<ClassificationDescriptor> classificationDescriptors, FeatureList featureList)
    {
        Map<String, Map<FeatureDescriptor, ClassificationInfo>> featureValues = new HashMap<>();
        classificationDescriptors
                        .forEach(classificationDescriptor -> classificationDescriptor.getFeatures().forEach(()));
        return featureValues;
    }


    public List<ClassificationDescriptor> getClassificationDescriptors(Set<ProductModel> products)
    {
        Objects.requireNonNull(this.classificationService);
        Set<FeatureList> featureLists = (Set<FeatureList>)products.stream().map(this.classificationService::getFeatures).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<ClassificationClassModel> classifications = getClassificationClassesFromFeatureLists(featureLists);
        Map<ClassificationClassModel, Set<FeatureDescriptor>> classificationsFeatures = (Map<ClassificationClassModel, Set<FeatureDescriptor>>)classifications.stream().collect(Collectors.toMap(Function.identity(), c -> new HashSet()));
        featureLists.forEach(featureList -> {
            List<Feature> features = featureList.getFeatures();
            features.stream().filter(()).forEach(());
        });
        Set<ClassificationDescriptor> classificationDescriptors = (Set<ClassificationDescriptor>)classificationsFeatures.entrySet().stream()
                        .map(featuresByClassificationClass -> new ClassificationDescriptor(((ClassificationClassModel)featuresByClassificationClass.getKey()).getCode(), ((ClassificationClassModel)featuresByClassificationClass.getKey()).getName(),
                                        createCatalogDescriptor(((ClassificationClassModel)featuresByClassificationClass.getKey()).getCatalogVersion()), (Set)featuresByClassificationClass.getValue())).collect(Collectors.toSet());
        return sortClassificationDescriptors(classificationDescriptors);
    }


    protected Set<ClassificationClassModel> getClassificationClassesFromFeatureLists(Set<FeatureList> featureLists)
    {
        return (Set<ClassificationClassModel>)featureLists.stream().map(FeatureList::getClassificationClasses).flatMap(Collection::stream).collect(Collectors.toSet());
    }


    protected Optional<ClassificationClassModel> getClassificationClassFromFeature(Feature feature)
    {
        return Optional.of(feature.getClassAttributeAssignment().getClassificationClass());
    }


    protected List<ClassificationDescriptor> sortClassificationDescriptors(Collection<ClassificationDescriptor> classificationDescriptors)
    {
        return (List<ClassificationDescriptor>)classificationDescriptors.stream()
                        .sorted(Comparator.nullsFirst(
                                        Comparator.comparing(descriptor -> (descriptor.getName() != null) ? descriptor.getName() : descriptor.getCode())))
                        .collect(Collectors.toList());
    }


    protected CatalogDescriptor createCatalogDescriptor(ClassificationSystemVersionModel catalogVersion)
    {
        return new CatalogDescriptor(catalogVersion.getCatalog().getId(), catalogVersion.getCatalog().getName(), catalogVersion
                        .getVersion(), this.catalogVersionService.canRead((CatalogVersionModel)catalogVersion, this.userService.getCurrentUser()), this.catalogVersionService
                        .canWrite((CatalogVersionModel)catalogVersion, this.userService.getCurrentUser()));
    }


    protected ClassificationService getClassificationService()
    {
        return this.classificationService;
    }


    @Required
    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
