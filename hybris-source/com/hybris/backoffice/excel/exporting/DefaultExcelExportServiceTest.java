package com.hybris.backoffice.excel.exporting;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.variants.BackofficeVariantsService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.ListUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelExportServiceTest
{
    @Mock
    private ExcelExportDivider excelExportDivider;
    @Mock
    private ExcelTranslatorRegistry excelTranslatorRegistry;
    @Mock
    private ExcelTemplateService excelTemplateService;
    @Mock
    private TypeService typeService;
    @Mock
    private ModelService modelService;
    @Mock
    private BackofficeVariantsService backofficeVariantsService;
    @Mock
    private CommonI18NService commonI18NService;
    @Mock
    private ExcelSheetService excelSheetService;
    @Mock
    private ExcelWorkbookService excelWorkbookService;
    @InjectMocks
    @Spy
    private DefaultExcelExportService excelExportService;


    @Test
    public void shouldExportTemplateForTypeWithSubtypes()
    {
        String product = "Product";
        String shoes = "Shoes";
        String jeans = "Jeans";
        ComposedTypeModel productType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(this.typeService.getComposedTypeForCode("Product")).thenReturn(productType);
        ComposedTypeModel shoesSubtype = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(shoesSubtype.getAbstract()).thenReturn(Boolean.valueOf(false));
        Mockito.when(shoesSubtype.getCode()).thenReturn("Shoes");
        ComposedTypeModel jeansSubtype = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(jeansSubtype.getAbstract()).thenReturn(null);
        Mockito.when(jeansSubtype.getCode()).thenReturn("Jeans");
        Collection<ComposedTypeModel> subTypes = Lists.newArrayList((Object[])new ComposedTypeModel[] {shoesSubtype, jeansSubtype});
        Mockito.when(productType.getAllSubTypes()).thenReturn(subTypes);
        Mockito.lenient().when(this.excelExportDivider.groupAttributesByType((Set)Matchers.any(), (List)Matchers.any())).thenReturn(Collections.emptyMap());
        Workbook excel = (Workbook)Mockito.mock(Workbook.class);
        ((DefaultExcelExportService)Mockito.doReturn(excel).when(this.excelExportService)).exportData((Map)Matchers.any(Map.class), (List)Matchers.any(List.class));
        Workbook ret = this.excelExportService.exportTemplate("Product");
        Assertions.assertThat((Iterable)ret).isSameAs(excel);
        ArgumentCaptor<Map<String, Set<ItemModel>>> mapCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<List<SelectedAttribute>> listCaptor = ArgumentCaptor.forClass(List.class);
        ((DefaultExcelExportService)Mockito.verify(this.excelExportService)).exportData((Map)mapCaptor.capture(), (List)listCaptor.capture());
        Assertions.assertThat((Map)mapCaptor.getValue()).hasSize(3);
        Assertions.assertThat((Map)mapCaptor.getValue()).containsEntry("Product", Collections.emptySet());
        Assertions.assertThat((Map)mapCaptor.getValue()).containsEntry("Shoes", Collections.emptySet());
        Assertions.assertThat((Map)mapCaptor.getValue()).containsEntry("Jeans", Collections.emptySet());
        Assertions.assertThat((List)listCaptor.getValue()).isEmpty();
    }


    @Test
    public void shouldFilterOutAbstractTypesWhenExportingTemplate()
    {
        String product = "Product";
        String variantProduct = "VariantProduct";
        ComposedTypeModel productType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(this.typeService.getComposedTypeForCode("Product")).thenReturn(productType);
        ComposedTypeModel variantProductSubtype = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(variantProductSubtype.getAbstract()).thenReturn(Boolean.valueOf(true));
        Mockito.lenient().when(variantProductSubtype.getCode()).thenReturn("VariantProduct");
        Collection<ComposedTypeModel> subTypes = Lists.newArrayList((Object[])new ComposedTypeModel[] {variantProductSubtype});
        Mockito.when(productType.getAllSubTypes()).thenReturn(subTypes);
        Mockito.lenient().when(this.excelExportDivider.groupAttributesByType((Set)Matchers.any(), (List)Matchers.any())).thenReturn(Collections.emptyMap());
        Workbook excel = (Workbook)Mockito.mock(Workbook.class);
        ((DefaultExcelExportService)Mockito.doReturn(excel).when(this.excelExportService)).exportData((Map)Matchers.any(Map.class), (List)Matchers.any(List.class));
        Workbook ret = this.excelExportService.exportTemplate("Product");
        Assertions.assertThat((Iterable)ret).isSameAs(excel);
        ArgumentCaptor<Map<String, Set<ItemModel>>> mapCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<List<SelectedAttribute>> listCaptor = ArgumentCaptor.forClass(List.class);
        ((DefaultExcelExportService)Mockito.verify(this.excelExportService)).exportData((Map)mapCaptor.capture(), (List)listCaptor.capture());
        Assertions.assertThat((Map)mapCaptor.getValue()).hasSize(1);
        Assertions.assertThat((Map)mapCaptor.getValue()).containsEntry("Product", Collections.emptySet());
        Assertions.assertThat((List)listCaptor.getValue()).isEmpty();
    }


    @Test
    public void shouldRefreshItemsAndFilterOutAlreadyRemovedItems()
    {
        ProductModel firstProduct = new ProductModel();
        ProductModel secondProduct = new ProductModel();
        ProductModel thirdProduct = new ProductModel();
        ((ModelService)Mockito.doThrow(JaloObjectNoLongerValidException.class).when(this.modelService)).refresh(secondProduct);
        List<ItemModel> refreshedItems = this.excelExportService.refreshSelectedItems(Arrays.asList(new ItemModel[] {(ItemModel)firstProduct, (ItemModel)secondProduct, (ItemModel)thirdProduct}));
        Assertions.assertThat(refreshedItems).hasSize(2);
        Assertions.assertThat(refreshedItems).containsOnly((Object[])new ItemModel[] {(ItemModel)firstProduct, (ItemModel)thirdProduct});
    }


    @Test
    public void shouldGetItemAttributeForNonVariantNonLocalizedType()
    {
        ItemModel itemModel = (ItemModel)Mockito.mock(ItemModel.class);
        SelectedAttribute selectedAttribute = (SelectedAttribute)Mockito.mock(SelectedAttribute.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(selectedAttribute.getAttributeDescriptor()).willReturn(attributeDescriptorModel);
        BDDMockito.given(attributeDescriptorModel.getQualifier()).willReturn("simpleAttribute");
        BDDMockito.given(Boolean.valueOf(selectedAttribute.isLocalized())).willReturn(Boolean.valueOf(false));
        BDDMockito.given(this.modelService.getAttributeValue(Matchers.any(), (String)Matchers.any())).willReturn("simpleAttributeValue");
        Object result = this.excelExportService.getItemAttribute(itemModel, selectedAttribute);
        Assertions.assertThat(result).isEqualTo("simpleAttributeValue");
        ((ModelService)BDDMockito.then(this.modelService).should()).getAttributeValue(itemModel, "simpleAttribute");
    }


    @Test
    public void shouldGetItemAttributeForNonVariantLocalizedType()
    {
        ItemModel itemModel = (ItemModel)Mockito.mock(ItemModel.class);
        SelectedAttribute selectedAttribute = (SelectedAttribute)Mockito.mock(SelectedAttribute.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(selectedAttribute.getAttributeDescriptor()).willReturn(attributeDescriptorModel);
        BDDMockito.given(attributeDescriptorModel.getQualifier()).willReturn("simpleLocalizedAttribute");
        BDDMockito.given(Boolean.valueOf(selectedAttribute.isLocalized())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(selectedAttribute.getIsoCode()).willReturn("en");
        BDDMockito.given(this.commonI18NService.getLocaleForIsoCode("en")).willReturn(Locale.ENGLISH);
        BDDMockito.given(this.modelService.getAttributeValue(Matchers.any(), (String)Matchers.any(), (Locale)Matchers.any())).willReturn("simpleLocalizedAttributeValue");
        Object result = this.excelExportService.getItemAttribute(itemModel, selectedAttribute);
        Assertions.assertThat(result).isEqualTo("simpleLocalizedAttributeValue");
        ((ModelService)BDDMockito.then(this.modelService).should()).getAttributeValue(itemModel, "simpleLocalizedAttribute", Locale.ENGLISH);
    }


    @Test
    public void shouldGetItemAttributeForVariantNonLocalizedType()
    {
        VariantProductModel itemModel = (VariantProductModel)Mockito.mock(VariantProductModel.class);
        SelectedAttribute selectedAttribute = (SelectedAttribute)Mockito.mock(SelectedAttribute.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(selectedAttribute.getAttributeDescriptor()).willReturn(attributeDescriptorModel);
        BDDMockito.given(attributeDescriptorModel.getQualifier()).willReturn("variantAttribute");
        BDDMockito.given(Boolean.valueOf(selectedAttribute.isLocalized())).willReturn(Boolean.valueOf(false));
        BDDMockito.given(this.backofficeVariantsService.getVariantAttributeValue((VariantProductModel)Matchers.any(), (String)Matchers.any())).willReturn("variantAttributeValue");
        Object result = this.excelExportService.getItemAttribute((ItemModel)itemModel, selectedAttribute);
        Assertions.assertThat(result).isEqualTo("variantAttributeValue");
        ((BackofficeVariantsService)BDDMockito.then(this.backofficeVariantsService).should()).getVariantAttributeValue(itemModel, "variantAttribute");
    }


    @Test
    public void shouldGetItemAttributeForVariantLocalizedType()
    {
        VariantProductModel itemModel = (VariantProductModel)Mockito.mock(VariantProductModel.class);
        SelectedAttribute selectedAttribute = (SelectedAttribute)Mockito.mock(SelectedAttribute.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(selectedAttribute.getAttributeDescriptor()).willReturn(attributeDescriptorModel);
        BDDMockito.given(attributeDescriptorModel.getQualifier()).willReturn("variantLocalizedAttribute");
        BDDMockito.given(Boolean.valueOf(selectedAttribute.isLocalized())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(selectedAttribute.getIsoCode()).willReturn("en");
        BDDMockito.given(this.commonI18NService.getLocaleForIsoCode("en")).willReturn(Locale.ENGLISH);
        Map<Locale, Object> locales = new HashMap<>();
        locales.put(Locale.ENGLISH, "variantLocalizedAttributeValue");
        BDDMockito.given(this.backofficeVariantsService.getLocalizedVariantAttributeValue((VariantProductModel)Matchers.any(), (String)Matchers.any())).willReturn(locales);
        Object result = this.excelExportService.getItemAttribute((ItemModel)itemModel, selectedAttribute);
        Assertions.assertThat(result).isEqualTo("variantLocalizedAttributeValue");
        ((BackofficeVariantsService)BDDMockito.then(this.backofficeVariantsService).should()).getLocalizedVariantAttributeValue(itemModel, "variantLocalizedAttribute");
    }


    @Test
    public void shouldCreateCorrectTypeSheet()
    {
        this.excelExportService.setTypePredicates(new HashSet());
        String product = "Product";
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        Map<String, Set<SelectedAttribute>> attributesByType = new HashMap<>();
        HashSet<SelectedAttribute> selectedAttributeHashSet = new HashSet<>();
        Map<String, Set<ItemModel>> itemsByType = new HashMap<>();
        HashSet<ItemModel> itemModels = new HashSet<>();
        ProductModel productModel = (ProductModel)Mockito.mock(ProductModel.class);
        List<SelectedAttribute> selectedAttributes = ListUtils.EMPTY_LIST;
        selectedAttributeHashSet.add((SelectedAttribute)Mockito.mock(SelectedAttribute.class));
        attributesByType.put("Product", selectedAttributeHashSet);
        itemModels.add(productModel);
        itemsByType.put("Product", itemModels);
        BDDMockito.given(this.excelWorkbookService.createWorkbook((InputStream)Matchers.any())).willReturn(workbook);
        BDDMockito.given(this.excelExportDivider.groupAttributesByType((Collection)Matchers.any(Set.class), (Collection)Matchers.any(Collection.class))).willReturn(attributesByType);
        ((DefaultExcelExportService)Mockito.doReturn(Mockito.mock(InputStream.class)).when(this.excelExportService)).loadExcelTemplate();
        ((DefaultExcelExportService)Mockito.doNothing().when(this.excelExportService)).addHeader((Sheet)Matchers.any(), (Set)Matchers.any());
        ((DefaultExcelExportService)Mockito.doNothing().when(this.excelExportService)).addValues((Map)Matchers.any(), (String)Matchers.any(), (Set)Matchers.any(), (Sheet)Matchers.any());
        this.excelExportService.exportData(itemsByType, selectedAttributes);
        ((ExcelSheetService)Mockito.verify(this.excelSheetService)).createOrGetTypeSheet(workbook, "Product");
    }
}
