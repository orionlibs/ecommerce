package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationCatalogVersionValueProviderTest
{
    private static final String CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME = "fieldName1";
    private static final String CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME_2 = "fieldName2";
    private final PK classificationSystemVersionPK = PK.fromLong(1L);
    private final PK classificationSystemVersionPK2 = PK.fromLong(2L);
    @InjectMocks
    private ClassificationCatalogVersionValueProvider classificationCatalogVersionValueProvider;
    @Mock
    private FieldNameProvider fieldNameProvider;
    @Mock
    private IndexConfig indexConfig;
    @Mock
    private IndexedProperty indexedProperty;
    @Mock
    private ProductModel product;
    @Mock
    private ClassificationSystemVersionModel classificationSystemVersion;
    @Mock
    private ClassificationSystemVersionModel classificationSystemVersion2;
    @Mock
    private ClassificationClassModel classificationClass;


    @Test
    public void shouldReturnAllClassificationSystemVersionsOfProduct() throws FieldValueProviderException
    {
        Mockito.when(this.product.getCatalogVersion()).thenReturn(this.classificationSystemVersion);
        Mockito.when(this.classificationClass.getCatalogVersion()).thenReturn(this.classificationSystemVersion2);
        List<ClassificationClassModel> classificationClasses = Collections.singletonList(this.classificationClass);
        Mockito.when(this.product.getClassificationClasses()).thenReturn(classificationClasses);
        Mockito.when(this.fieldNameProvider.getFieldNames((IndexedProperty)Matchers.same(this.indexedProperty), (String)Matchers.any())).thenReturn(Arrays.asList(new String[] {"fieldName1", "fieldName2"}));
        Mockito.when(this.classificationSystemVersion.getPk()).thenReturn(this.classificationSystemVersionPK);
        Mockito.when(this.classificationSystemVersion2.getPk()).thenReturn(this.classificationSystemVersionPK2);
        Collection<FieldValue> classificationSystemVersionValues = this.classificationCatalogVersionValueProvider.getFieldValues(this.indexConfig, this.indexedProperty, this.product);
        Assertions.assertThat(classificationSystemVersionValues).hasSize(4);
        Assertions.assertThat(classificationSystemVersionValues).extracting(new Function[] {FieldValue::getFieldName, FieldValue::getValue}).contains((Object[])new Tuple[] {new Tuple(new Object[] {"fieldName1", Long.valueOf(1L)})});
        Assertions.assertThat(classificationSystemVersionValues).extracting(new Function[] {FieldValue::getFieldName, FieldValue::getValue}).contains((Object[])new Tuple[] {new Tuple(new Object[] {"fieldName1", Long.valueOf(2L)})});
        Assertions.assertThat(classificationSystemVersionValues).extracting(new Function[] {FieldValue::getFieldName, FieldValue::getValue}).contains((Object[])new Tuple[] {new Tuple(new Object[] {"fieldName2", Long.valueOf(1L)})});
        Assertions.assertThat(classificationSystemVersionValues).extracting(new Function[] {FieldValue::getFieldName, FieldValue::getValue}).contains((Object[])new Tuple[] {new Tuple(new Object[] {"fieldName2", Long.valueOf(2L)})});
    }
}
