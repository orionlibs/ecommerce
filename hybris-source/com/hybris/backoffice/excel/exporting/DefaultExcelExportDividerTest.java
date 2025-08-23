package com.hybris.backoffice.excel.exporting;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.template.mapper.ExcelMapper;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.JUnitSoftAssertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelExportDividerTest
{
    @Rule
    public JUnitSoftAssertions soft = new JUnitSoftAssertions();
    @Mock
    private ExcelMapper<ComposedTypeModel, AttributeDescriptorModel> mapper;
    @Mock
    private TypeService typeService;
    @Mock
    private CommonI18NService commonI18NService;
    @Mock
    private PermissionCRUDService permissionCRUDService;
    @Mock
    private ModelService modelService;
    @Spy
    @InjectMocks
    private DefaultExcelExportDivider divider;
    private static final String PRODUCT = "Product";
    private static final String CATALOG = "Catalog";


    @Before
    public void setUp()
    {
        LanguageModel lang = (LanguageModel)Mockito.mock(LanguageModel.class);
        Mockito.when(lang.getIsocode()).thenReturn(Locale.ENGLISH.toString());
        BDDMockito.given(this.commonI18NService.getCurrentLanguage()).willReturn(lang);
        Mockito.lenient().when(Boolean.valueOf(this.permissionCRUDService.canReadAttribute((AttributeDescriptorModel)Matchers.any(AttributeDescriptorModel.class)))).thenReturn(Boolean.valueOf(true));
    }


    @Test
    public void shouldGroupItemsByType()
    {
        int noOfProducts = 2;
        int noOfCatalogs = 1;
        List<ItemModel> mocks = generateItemModelMocks(2, 1);
        Map<String, Set<ItemModel>> map = this.divider.groupItemsByType(mocks);
        this.soft.assertThat(map.size()).isEqualTo(2);
        this.soft.assertThat(map.keySet()).containsOnly((Object[])new String[] {"Product", "Catalog"});
        this.soft.assertThat(((Set)map.get("Product")).size()).isEqualTo(2);
        this.soft.assertThat(((Set)map.get("Catalog")).size()).isEqualTo(1);
    }


    @Test
    public void shouldAvoidAlreadySelectedAttributes()
    {
        ComposedTypeModel cp = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        List<AttributeDescriptorModel> ads = generateAttributeDescriptorMocks(
                        Lists.newArrayList((Object[])new Descriptor[] {new Descriptor(this, false, false, true, "NOT PK", "qual0"), new Descriptor(this, false, false, true, "ALSO NOT PK", "qual1"), new Descriptor(this, true, false, true, "Identifier", "name")}));
        BDDMockito.given(this.mapper.apply(cp)).willReturn(ads);
        Set<SelectedAttribute> result = this.divider.getMissingRequiredAndUniqueAttributes(cp, Sets.newHashSet((Object[])new String[] {"name"}));
        this.soft.assertThat(result.size()).isEqualTo(2);
        this.soft.assertThat(result.stream().anyMatch(e -> e.getAttributeDescriptor().getName().equals("pk"))).isFalse();
    }


    @Test
    public void shouldNotDuplicateTheseSameQualifiers()
    {
        ComposedTypeModel cp = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        List<AttributeDescriptorModel> ads = generateAttributeDescriptorMocks(
                        Lists.newArrayList((Object[])new Descriptor[] {new Descriptor(this, false, false, true, "any", "thesame"), new Descriptor(this, false, false, true, "any", "thesame"), new Descriptor(this, false, false, true, "any", "thesame")}));
        BDDMockito.given(this.mapper.apply(cp)).willReturn(ads);
        Set<SelectedAttribute> result = this.divider.getMissingRequiredAndUniqueAttributes(cp, new HashSet());
        Assertions.assertThat(result.size()).isEqualTo(1);
    }


    @Test
    public void shouldUseAttributeDescriptorsForGivenType()
    {
        String product = "Product";
        String shoe = "Shoe";
        String code = "code";
        AttributeDescriptorModel productCodeAttributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        AttributeDescriptorModel shoeCodeAttributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        List<SelectedAttribute> selectedAttributes = List.of(new SelectedAttribute(productCodeAttributeDescriptor));
        BDDMockito.given(productCodeAttributeDescriptor.getQualifier()).willReturn("code");
        BDDMockito.given(shoeCodeAttributeDescriptor.getQualifier()).willReturn("code");
        BDDMockito.given(this.typeService.getAttributeDescriptor("Product", "code")).willReturn(productCodeAttributeDescriptor);
        BDDMockito.given(this.typeService.getAttributeDescriptor("Shoe", "code")).willReturn(shoeCodeAttributeDescriptor);
        ((DefaultExcelExportDivider)Mockito.doReturn(Collections.emptySet()).when(this.divider)).getMissingRequiredAndUniqueAttributes((ComposedTypeModel)Matchers.any(), (Set)Matchers.any());
        ((DefaultExcelExportDivider)Mockito.doReturn(Collections.emptyList()).when(this.divider)).filterByPermissions((Collection)Matchers.any());
        Map<String, Set<SelectedAttribute>> result = this.divider.groupAttributesByType(Set.of("Product", "Shoe"), selectedAttributes);
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get("Product")).hasOnlyOneElementSatisfying(selectedAttribute -> Assertions.assertThat(selectedAttribute.getAttributeDescriptor()).isEqualTo(productCodeAttributeDescriptor));
        Assertions.assertThat(((SelectedAttribute)((Set<SelectedAttribute>)result.get("Shoe")).iterator().next()).getAttributeDescriptor()).isEqualTo(shoeCodeAttributeDescriptor);
    }


    private List<ItemModel> generateItemModelMocks(int numberOfProducts, int numberOfCatalogs)
    {
        return
                        (List<ItemModel>)Stream.concat(
                                                        IntStream.range(0, numberOfProducts).mapToObj(idx -> generateItemModelMock("Product")),
                                                        IntStream.range(0, numberOfCatalogs).mapToObj(idx -> generateItemModelMock("Catalog")))
                                        .collect(Collectors.toList());
    }


    private ItemModel generateItemModelMock(String itemType)
    {
        ItemModel itemModel = (ItemModel)Mockito.mock(ItemModel.class);
        Mockito.lenient().when(itemModel.getItemtype()).thenReturn(itemType);
        BDDMockito.given(this.modelService.getModelType(itemModel)).willReturn(itemType);
        return itemModel;
    }


    private List<AttributeDescriptorModel> generateAttributeDescriptorMocks(List<Descriptor> descriptors)
    {
        return (List<AttributeDescriptorModel>)descriptors.stream().map(descriptor -> {
            AttributeDescriptorModel ad = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
            BDDMockito.given(ad.getName()).willReturn(descriptor.getName());
            BDDMockito.given(ad.getLocalized()).willReturn(Boolean.valueOf(descriptor.isLocalized()));
            Mockito.lenient().when(ad.getOptional()).thenReturn(Boolean.valueOf(descriptor.isOptional()));
            Mockito.lenient().when(ad.getUnique()).thenReturn(Boolean.valueOf(descriptor.isUnique()));
            BDDMockito.given(ad.getQualifier()).willReturn(descriptor.getQualifier());
            return ad;
        }).collect(Collectors.toList());
    }
}
