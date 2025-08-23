package de.hybris.bootstrap.ddl.pk.impl;

import com.google.common.collect.Lists;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.ddl.dbtypesystem.NumberSeries;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YEnumValue;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.platform.core.PK;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPkFactoryTest
{
    private static final int TEST_CLUSER_ID = 0;
    @Mock
    private NumberSeries numberSeries1;
    @Mock
    private NumberSeries numberSeries2;
    @Mock
    private NumberSeries numberSeries3;
    @Mock
    private YComposedType yComposedType;
    @Mock
    private YComposedType metaType;
    @Mock
    private YAttributeDescriptor yAttributeDescriptor;
    @Mock
    private YEnumValue yEnumValue;
    @Mock
    private YEnumType yEnumType;
    @Mock
    private YAtomicType yAtomicType;
    @Mock
    private YMapType yMapType;
    @Mock
    private YCollectionType yCollectionType;
    @Mock
    private YDeployment yDeployment;


    @Before
    public void setUp()
    {
        BDDMockito.given(this.numberSeries1.getSeriesKey()).willReturn("pk_1");
        BDDMockito.given(Long.valueOf(this.numberSeries1.getValue())).willReturn(Long.valueOf(getCurrentCounter(10L)));
        BDDMockito.given(this.numberSeries2.getSeriesKey()).willReturn("pk_2");
        BDDMockito.given(Long.valueOf(this.numberSeries2.getValue())).willReturn(Long.valueOf(getCurrentCounter(15L)));
        BDDMockito.given(this.numberSeries3.getSeriesKey()).willReturn("pk_3");
        BDDMockito.given(Long.valueOf(this.numberSeries3.getValue())).willReturn(Long.valueOf(getCurrentCounter(20L)));
    }


    @Test
    public void shouldReturnMapOfCurrentNumberSeries()
    {
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        Map<String, Long> currentNumberSeries = defaultPkFactory.getCurrentNumberSeries();
        Assertions.assertThat(currentNumberSeries).hasSize(3);
        Assertions.assertThat(currentNumberSeries.get("pk_1")).isEqualTo(10L);
        Assertions.assertThat(currentNumberSeries.get("pk_2")).isEqualTo(15L);
        Assertions.assertThat(currentNumberSeries.get("pk_3")).isEqualTo(20L);
    }


    @Test
    public void mapOfCurrentNumberSeriesShouldBeUnmodifable()
    {
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        try
        {
            Map<String, Long> currentNumberSeries = defaultPkFactory.getCurrentNumberSeries();
            currentNumberSeries.put("pk_1", Long.valueOf(200L));
            Assertions.fail("Should throw UnsupportedOperationException");
        }
        catch(UnsupportedOperationException unsupportedOperationException)
        {
        }
    }


    @Test
    public void shouldGeneratePksForNewTypesBasedOnNewlyInitializedCounter()
    {
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPK = defaultPkFactory.createNewPK(getTypeCode(5));
        Assertions.assertThat((Comparable)newPK).isNotNull();
        PkAssert.assertThat(newPK).hasTypeAndCounter(getTypeCode(5), getCurrentCounter(1L));
    }


    @Test
    public void shouldGeneratePksBasedOnCurrentCounterForExistingTypeCode()
    {
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPK = defaultPkFactory.createNewPK(getTypeCode(1));
        PK newPK2 = defaultPkFactory.createNewPK(getTypeCode(1));
        Assertions.assertThat((Comparable)newPK).isNotNull();
        Assertions.assertThat((Comparable)newPK2).isNotNull();
        PkAssert.assertThat(newPK).hasTypeAndCounter(getTypeCode(1), getCurrentCounter(10L));
        PkAssert.assertThat(newPK2).hasTypeAndCounter(getTypeCode(1), getCurrentCounter(11L));
    }


    @Test
    public void shouldThrowNullPointerExceptionWhenDeploymentForComposedTypeIsNull()
    {
        BDDMockito.given(this.yComposedType.getDeployment()).willReturn(null);
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        try
        {
            defaultPkFactory.createNewPK(this.yComposedType);
            Assertions.fail("Should throw NullPointerException");
        }
        catch(NullPointerException nullPointerException)
        {
        }
    }


    @Test
    public void shouldGeneratePkForComposedType()
    {
        BDDMockito.given(this.yComposedType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(1)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPK = defaultPkFactory.createNewPK(this.yComposedType);
        Assertions.assertThat((Comparable)newPK).isNotNull();
        PkAssert.assertThat(newPK).hasTypeAndCounter(getTypeCode(1), getCurrentCounter(10L));
    }


    @Test
    public void shouldCreatePkForGivenComposedType()
    {
        BDDMockito.given(this.yComposedType.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.yComposedType.getCode()).willReturn("TestItem");
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yComposedType);
        Assertions.assertThat((Comparable)newPk).isNotNull();
        PkAssert.assertThat(newPk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
    }


    @Test
    public void shouldReturnExistingPkForGivenComposedTypeWhenItWasAlreadyCreated()
    {
        BDDMockito.given(this.yComposedType.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.yComposedType.getCode()).willReturn("TestItem");
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yComposedType);
        PK pk = defaultPkFactory.getOrCreatePK(this.yComposedType);
        Assertions.assertThat((Comparable)pk).isNotNull();
        PkAssert.assertThat(newPk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
        Assertions.assertThat((Comparable)pk).isEqualTo(newPk);
    }


    @Test
    public void shouldCreatePkForGivenEnumValue()
    {
        BDDMockito.given(this.yEnumValue.getEnumTypeCode()).willReturn("Foo");
        BDDMockito.given(this.yEnumValue.getCode()).willReturn("Bar");
        BDDMockito.given(this.yEnumValue.getEnumType()).willReturn(this.yEnumType);
        BDDMockito.given(this.yEnumType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yEnumValue);
        Assertions.assertThat((Comparable)newPk).isNotNull();
        PkAssert.assertThat(newPk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
    }


    @Test
    public void shouldReturnExistingPkForGivenEnumValueWhenItWasAlreadyCreated()
    {
        BDDMockito.given(this.yEnumValue.getEnumTypeCode()).willReturn("Foo");
        BDDMockito.given(this.yEnumValue.getCode()).willReturn("Bar");
        BDDMockito.given(this.yEnumValue.getEnumType()).willReturn(this.yEnumType);
        BDDMockito.given(this.yEnumType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yEnumValue);
        PK pk = defaultPkFactory.getOrCreatePK(this.yEnumValue);
        Assertions.assertThat((Comparable)pk).isNotNull();
        PkAssert.assertThat(pk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
        Assertions.assertThat((Comparable)pk).isEqualTo(newPk);
    }


    @Test
    public void shouldCreatePkForGivenAtomicType()
    {
        BDDMockito.given(this.yAtomicType.getCode()).willReturn("TestAtomicType");
        BDDMockito.given(this.yAtomicType.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yAtomicType);
        Assertions.assertThat((Comparable)newPk).isNotNull();
        PkAssert.assertThat(newPk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
    }


    @Test
    public void shouldReturnExistingPkForGivenAtomicTypeWhenItWasAlreadyCreated()
    {
        BDDMockito.given(this.yAtomicType.getCode()).willReturn("TestAtomicType");
        BDDMockito.given(this.yAtomicType.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yAtomicType);
        PK pk = defaultPkFactory.getOrCreatePK(this.yAtomicType);
        Assertions.assertThat((Comparable)pk).isNotNull();
        PkAssert.assertThat(pk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
        Assertions.assertThat((Comparable)pk).isEqualTo(newPk);
    }


    @Test
    public void shouldCreatePkForGivenMapType()
    {
        BDDMockito.given(this.yMapType.getCode()).willReturn("TestMapType");
        BDDMockito.given(this.yMapType.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yMapType);
        Assertions.assertThat((Comparable)newPk).isNotNull();
        PkAssert.assertThat(newPk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
    }


    @Test
    public void shouldReturnExistingPkForGivenMapTypeWhenItWasAlreadyCreated()
    {
        BDDMockito.given(this.yMapType.getCode()).willReturn("TestMapType");
        BDDMockito.given(this.yMapType.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yMapType);
        PK pk = defaultPkFactory.getOrCreatePK(this.yMapType);
        Assertions.assertThat((Comparable)pk).isNotNull();
        PkAssert.assertThat(pk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
        Assertions.assertThat((Comparable)pk).isEqualTo(newPk);
    }


    @Test
    public void shouldCreatePkForGivenCollectionType()
    {
        BDDMockito.given(this.yCollectionType.getCode()).willReturn("TestCollectionType");
        BDDMockito.given(this.yCollectionType.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yCollectionType);
        Assertions.assertThat((Comparable)newPk).isNotNull();
        PkAssert.assertThat(newPk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
    }


    @Test
    public void shouldReturnExistingPkForGivenCollectionTypeWhenItWasAlreadyCreated()
    {
        BDDMockito.given(this.yCollectionType.getCode()).willReturn("TestCollectionType");
        BDDMockito.given(this.yCollectionType.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yCollectionType);
        PK pk = defaultPkFactory.getOrCreatePK(this.yCollectionType);
        Assertions.assertThat((Comparable)pk).isNotNull();
        PkAssert.assertThat(pk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
        Assertions.assertThat((Comparable)pk).isEqualTo(newPk);
    }


    @Test
    public void shouldCreatePkForGivenComposedTypeAndAttributeDescriptor()
    {
        BDDMockito.given(this.yComposedType.getCode()).willReturn("TestItem");
        BDDMockito.given(this.yAttributeDescriptor.getQualifier()).willReturn("fooBar");
        BDDMockito.given(this.yAttributeDescriptor.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yComposedType, this.yAttributeDescriptor);
        Assertions.assertThat((Comparable)newPk).isNotNull();
        PkAssert.assertThat(newPk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
    }


    @Test
    public void shouldReturnExistingPkForGivenComposedTypeAndAttributeDescriptorWhenItWasAlreadyCreated()
    {
        BDDMockito.given(this.yComposedType.getCode()).willReturn("TestItem");
        BDDMockito.given(this.yAttributeDescriptor.getQualifier()).willReturn("fooBar");
        BDDMockito.given(this.yAttributeDescriptor.getMetaType()).willReturn(this.metaType);
        BDDMockito.given(this.metaType.getDeployment()).willReturn(this.yDeployment);
        BDDMockito.given(Integer.valueOf(this.yDeployment.getItemTypeCode())).willReturn(Integer.valueOf(getTypeCode(4)));
        DefaultPkFactory defaultPkFactory = new DefaultPkFactory(Lists.newArrayList((Object[])new NumberSeries[] {this.numberSeries1, this.numberSeries2, this.numberSeries3}, ), 0);
        PK newPk = defaultPkFactory.getOrCreatePK(this.yComposedType, this.yAttributeDescriptor);
        PK pk = defaultPkFactory.getOrCreatePK(this.yComposedType, this.yAttributeDescriptor);
        Assertions.assertThat((Comparable)pk).isNotNull();
        PkAssert.assertThat(pk).hasTypeAndCounter(getTypeCode(4), getCurrentCounter(1L));
        Assertions.assertThat((Comparable)pk).isEqualTo(newPk);
    }


    private int getTypeCode(int typeCode)
    {
        return typeCode;
    }


    private long getCurrentCounter(long currentCounter)
    {
        return currentCounter;
    }
}
