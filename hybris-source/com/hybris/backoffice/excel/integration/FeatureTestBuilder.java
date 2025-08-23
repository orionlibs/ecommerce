package com.hybris.backoffice.excel.integration;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.classification.features.UnlocalizedFeature;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.seed.ClassificationSystemTestDataCreator;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

public class FeatureTestBuilder
{
    public <T extends Builder> Optional<T> of(Class<T> t, ModelService modelService, ClassificationClassModel classificationClassModel, ClassificationSystemVersionModel classificationSystemVersionModel, String classificationAttributeName)
    {
        try
        {
            return Optional.of((T)t.getDeclaredConstructor(new Class[] {FeatureTestBuilder.class, ModelService.class, ClassificationClassModel.class, ClassificationSystemVersionModel.class, String.class})
                            .newInstance(new Object[] {new FeatureTestBuilder(), modelService, classificationClassModel, classificationSystemVersionModel, classificationAttributeName}));
        }
        catch(Exception e)
        {
            return Optional.empty();
        }
    }


    private static ClassAttributeAssignmentModel createClassificationAssignment(ModelService modelService, ClassificationClassModel classificationClassModel, ClassificationSystemVersionModel classificationSystemVersionModel, String classificationAttributeName)
    {
        ClassificationAttributeModel attributeModel = (new ClassificationSystemTestDataCreator(modelService)).createClassificationAttribute(classificationAttributeName, classificationSystemVersionModel);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)modelService.create(ClassAttributeAssignmentModel.class);
        classAttributeAssignmentModel.setClassificationClass(classificationClassModel);
        classAttributeAssignmentModel.setClassificationAttribute(attributeModel);
        modelService.save(classAttributeAssignmentModel);
        return classAttributeAssignmentModel;
    }


    private static <T> Feature createLocalizedFeature(ClassAttributeAssignmentModel attributeAssignmentModel, Locale currentLocale, Collection<Pair<Locale, List<T>>> localizedValues)
    {
        Supplier<Map<Locale, List<FeatureValue>>> localizedFeatures = () -> (Map)CollectionUtils.emptyIfNull(localizedValues).stream().collect(Collectors.toMap(Pair::getLeft, ()));
        return (Feature)new LocalizedFeature(attributeAssignmentModel, localizedFeatures.get(), currentLocale);
    }


    private static <T> Feature createUnlocalizedFeature(ClassAttributeAssignmentModel attributeAssignmentModel, Collection<T> unlocalizedValues)
    {
        return createUnlocalizedFeature(attributeAssignmentModel, unlocalizedValues, null);
    }


    private static <T> Feature createUnlocalizedFeature(ClassAttributeAssignmentModel attributeAssignmentModel, Collection<T> unlocalizedValues, ClassificationAttributeUnitModel unit)
    {
        Supplier<List<FeatureValue>> unlocalizedFeatures = () -> (List)CollectionUtils.emptyIfNull(unlocalizedValues).stream().map(()).collect(Collectors.toList());
        return (Feature)new UnlocalizedFeature(attributeAssignmentModel, unlocalizedFeatures.get());
    }
}
