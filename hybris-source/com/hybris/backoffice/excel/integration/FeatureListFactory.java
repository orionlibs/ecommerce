package com.hybris.backoffice.excel.integration;

import com.google.common.collect.Lists;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class FeatureListFactory
{
    static final String SINGLE_NUMBER_WITHOUT_UNIT = "singleNumberWithoutUnit";
    static final String SINGLE_BOOLEAN = "singleBoolean";
    static final String MULTIPLE_BOOLEAN = "multipleBoolean";
    static final String SINGLE_NUMBER_WITH_UNIT = "singleNumberWithUnit";
    static final String MULTIPLE_NUMBER_WITH_UNIT = "multipleNumberWithUnit";
    static final String RANGE_SINGLE_NUMBER_WITHOUT_UNIT = "rangeSingleNumberWithoutUnit";
    static final String RANGE_MULTIPLE_NUMBER_WITH_UNIT = "rangeMultipleNumberWithUnit";
    static final String SINGLE_DATE = "singleDate";
    static final String SINGLE_RANGE_DATE = "singleRangeDate";
    static final String SINGLE_STRING = "singleString";
    static final String MULTI_STRING = "multiString";
    static final String SINGLE_LOCALIZED_STRING = "singleLocalizedString";
    static final String SINGLE_ENUM = "singleEnum";
    static final String SINGLE_REFERENCE = "singleReference";


    public static FeatureList create(ModelService modelService, TypeService typeService, ClassificationClassModel classificationClass, CatalogVersionModel catalogVersionModel, ClassificationSystemVersionModel classificationSystemVersionModel)
    {
        return getFeatureList(modelService, typeService, classificationClass, catalogVersionModel, classificationSystemVersionModel);
    }


    private static <T extends FeatureTestBuilder.Builder> T getBuilder(FeatureTestBuilder builder, Class<T> builderClass, String attributeName, ModelService modelService, ClassificationClassModel classificationClass, ClassificationSystemVersionModel classificationSystemVersionModel)
    {
        return (T)builder
                        .of(builderClass, modelService, classificationClass, classificationSystemVersionModel, attributeName)
                        .orElseThrow(InstantiationError::new);
    }


    private static FeatureList getFeatureList(ModelService modelService, TypeService typeService, ClassificationClassModel classificationClass, CatalogVersionModel catalogVersionModel, ClassificationSystemVersionModel classificationSystemVersionModel)
    {
        FeatureTestBuilder builder = new FeatureTestBuilder();
        Feature singleBoolean = ((FeatureTestBuilder.BooleanBuilder)getBuilder(builder, FeatureTestBuilder.BooleanBuilder.class, "singleBoolean", modelService, classificationClass, classificationSystemVersionModel)).values(new Boolean[] {Boolean.valueOf(true)}).build();
        Feature multiBoolean = ((FeatureTestBuilder.BooleanBuilder)getBuilder(builder, FeatureTestBuilder.BooleanBuilder.class, "multipleBoolean", modelService, classificationClass, classificationSystemVersionModel)).multivalue()
                        .values(new Boolean[] {Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true)}).build();
        Feature singleNumberWithoutUnit = ((FeatureTestBuilder.NumberBuilder)getBuilder(builder, FeatureTestBuilder.NumberBuilder.class, "singleNumberWithoutUnit", modelService, classificationClass, classificationSystemVersionModel)).values(new Number[] {Double.valueOf(3.53D)}).build();
        Feature singleNumberWithUnit = ((FeatureTestBuilder.NumberBuilder)getBuilder(builder, FeatureTestBuilder.NumberBuilder.class, "singleNumberWithUnit", modelService, classificationClass, classificationSystemVersionModel)).values(new Number[] {Double.valueOf(4.53D)})
                        .unit(createUnit(modelService, classificationSystemVersionModel, "kg")).build();
        Feature multipleNumberWithUnit = ((FeatureTestBuilder.NumberBuilder)getBuilder(builder, FeatureTestBuilder.NumberBuilder.class, "multipleNumberWithUnit", modelService, classificationClass, classificationSystemVersionModel)).multivalue()
                        .values(new Number[] {Double.valueOf(4.53D), Double.valueOf(3.276D), Double.valueOf(3.21D)}).unit(createUnit(modelService, classificationSystemVersionModel, "g")).build();
        Feature rangeSingleNumberWithoutUnit = ((FeatureTestBuilder.NumberBuilder)getBuilder(builder, FeatureTestBuilder.NumberBuilder.class, "rangeSingleNumberWithoutUnit", modelService, classificationClass, classificationSystemVersionModel)).range()
                        .values(new Number[] {Double.valueOf(2.53D), Double.valueOf(3.77D)}).build();
        Feature rangeMultipleNumberWithUnit = ((FeatureTestBuilder.NumberBuilder)getBuilder(builder, FeatureTestBuilder.NumberBuilder.class, "rangeMultipleNumberWithUnit", modelService, classificationClass, classificationSystemVersionModel)).multivalue().range()
                        .values(new Number[] {Double.valueOf(1.53D), Double.valueOf(1.58D), Double.valueOf(2.01D), Double.valueOf(2.53D)}).unit(createUnit(modelService, classificationSystemVersionModel, "m")).build();
        Feature singleDate = ((FeatureTestBuilder.DateBuilder)getBuilder(builder, FeatureTestBuilder.DateBuilder.class, "singleDate", modelService, classificationClass, classificationSystemVersionModel)).values(new Date[] {Date.from(LocalDateTime.of(2018, 3, 3, 10, 0).toInstant(ZoneOffset.UTC))})
                        .build();
        Feature singleRangeDate = ((FeatureTestBuilder.DateBuilder)getBuilder(builder, FeatureTestBuilder.DateBuilder.class, "singleRangeDate", modelService, classificationClass, classificationSystemVersionModel)).range()
                        .values(new Date[] {Date.from(LocalDateTime.of(2018, 3, 3, 10, 0).toInstant(ZoneOffset.UTC)), Date.from(LocalDateTime.of(2019, 3, 3, 12, 0).toInstant(ZoneOffset.UTC))}).build();
        Feature singleString = ((FeatureTestBuilder.StringBuilder)getBuilder(builder, FeatureTestBuilder.StringBuilder.class, "singleString", modelService, classificationClass, classificationSystemVersionModel)).unlocalizedValues(new Object[] {"some string"}).build();
        Feature multiString = ((FeatureTestBuilder.StringBuilder)getBuilder(builder, FeatureTestBuilder.StringBuilder.class, "multiString", modelService, classificationClass, classificationSystemVersionModel)).multivalue().unlocalizedValues(new Object[] {"x1", "x2", "x3"}).build();
        Feature singleLocalizedString = ((FeatureTestBuilder.StringBuilder)getBuilder(builder, FeatureTestBuilder.StringBuilder.class, "singleLocalizedString", modelService, classificationClass, classificationSystemVersionModel)).localized(Locale.ENGLISH)
                        .localizedValues(new Pair[] {(Pair)ImmutablePair.of(Locale.ENGLISH, Lists.newArrayList(new Object[] {"thanks"})), (Pair)ImmutablePair.of(Locale.GERMAN, Lists.newArrayList(new Object[] {"danke"}))}).build();
        Feature singleEnum = ((FeatureTestBuilder.EnumBuilder)getBuilder(builder, FeatureTestBuilder.EnumBuilder.class, "singleEnum", modelService, classificationClass, classificationSystemVersionModel)).values(new Object[] {ArticleApprovalStatus.CHECK}).build();
        ProductModel productRef = new ProductModel();
        productRef.setCode("productRef");
        productRef.setCatalogVersion(catalogVersionModel);
        modelService.save(productRef);
        Feature singleReference = ((FeatureTestBuilder.ReferenceBuilder)getBuilder(builder, FeatureTestBuilder.ReferenceBuilder.class, "singleReference", modelService, classificationClass, classificationSystemVersionModel)).referenceType(typeService.getComposedTypeForCode("Product"))
                        .unlocalizedValues(new Object[] {productRef}).build();
        return new FeatureList(new Feature[] {
                        singleBoolean, multiBoolean, singleNumberWithoutUnit, singleNumberWithUnit, multipleNumberWithUnit, rangeSingleNumberWithoutUnit, rangeMultipleNumberWithUnit, singleDate, singleRangeDate, singleString,
                        multiString, singleLocalizedString, singleEnum, singleReference});
    }


    private static ClassificationAttributeUnitModel createUnit(ModelService modelService, ClassificationSystemVersionModel classificationSystemVersionModel, String symbol)
    {
        ClassificationAttributeUnitModel unit = (ClassificationAttributeUnitModel)modelService.create(ClassificationAttributeUnitModel.class);
        unit.setCode(symbol);
        unit.setName(symbol);
        unit.setSymbol(symbol);
        unit.setUnitType(symbol);
        unit.setSystemVersion(classificationSystemVersionModel);
        modelService.save(unit);
        return unit;
    }
}
