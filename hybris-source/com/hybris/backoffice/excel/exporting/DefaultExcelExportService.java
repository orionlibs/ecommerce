package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.exporting.data.filter.ExcelExportAttributePredicate;
import com.hybris.backoffice.excel.exporting.data.filter.ExcelExportTypePredicate;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.variants.BackofficeVariantsService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.variants.model.VariantProductModel;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelExportService implements ExcelExportService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExcelExportService.class);
    private DefaultExcelExportDivider divider;
    private ExcelExportDivider excelExportDivider;
    private String templatePath;
    private ExcelTranslatorRegistry excelTranslatorRegistry;
    private ExcelTemplateService excelTemplateService;
    private TypeService typeService;
    private CommonI18NService commonI18NService;
    private PermissionCRUDService permissionCRUDService;
    private ModelService modelService;
    private ExcelWorkbookService excelWorkbookService;
    private ExcelSheetService excelSheetService;
    private ExcelCellService excelCellService;
    private ExcelHeaderService excelHeaderService;
    private I18NService i18NService;
    private SessionService sessionService;
    private UserService userService;
    private BackofficeVariantsService backofficeVariantsService;
    private Set<ExcelExportAttributePredicate> attributePredicates;
    private Set<ExcelExportTypePredicate> typePredicates;


    public Workbook exportTemplate(String typeCode)
    {
        Map<String, Set<ItemModel>> itemsByType = new LinkedHashMap<>();
        ComposedTypeModel type = getTypeService().getComposedTypeForCode(typeCode);
        if(BooleanUtils.isFalse(type.getAbstract()))
        {
            itemsByType.put(typeCode, Collections.emptySet());
        }
        Collection<ComposedTypeModel> subTypes = type.getAllSubTypes();
        if(subTypes != null)
        {
            subTypes.stream().filter(subType -> BooleanUtils.isNotTrue(subType.getAbstract()))
                            .forEach(subtype -> itemsByType.put(subtype.getCode(), Collections.emptySet()));
        }
        return exportData(itemsByType, Collections.emptyList());
    }


    public Workbook exportData(List<ItemModel> selectedItems, List<SelectedAttribute> selectedAttributes)
    {
        Collection<ItemModel> refreshedItems = refreshSelectedItems(selectedItems);
        Map<String, Set<ItemModel>> itemsByType = getExcelExportDivider().groupItemsByType(refreshedItems);
        return exportData(itemsByType, selectedAttributes);
    }


    protected List<ItemModel> refreshSelectedItems(List<ItemModel> selectedItems)
    {
        List<ItemModel> refreshedItems = new ArrayList<>();
        for(ItemModel itemToRefresh : selectedItems)
        {
            try
            {
                getModelService().refresh(itemToRefresh);
                refreshedItems.add(itemToRefresh);
            }
            catch(RuntimeException ex)
            {
                LOG.debug("Cannot refresh item", ex);
            }
        }
        return refreshedItems;
    }


    public Workbook exportData(String typeCode, List<SelectedAttribute> selectedAttributes)
    {
        Map<String, Set<ItemModel>> itemsByType = new HashMap<>();
        itemsByType.put(typeCode, Collections.emptySet());
        return exportData(itemsByType, selectedAttributes);
    }


    protected Workbook exportData(Map<String, Set<ItemModel>> itemsByType, List<SelectedAttribute> selectedAttributes)
    {
        Map<String, Set<ItemModel>> itemsByTypeFiltered = applyTypePredicates(itemsByType);
        Collection<SelectedAttribute> selectedAttributesFiltered = applyAttributePredicates(selectedAttributes);
        Map<String, Set<SelectedAttribute>> attributesByType = getExcelExportDivider().groupAttributesByType(itemsByTypeFiltered.keySet(), selectedAttributesFiltered);
        Workbook workbook = getExcelWorkbookService().createWorkbook(loadExcelTemplate());
        attributesByType.forEach((typeCode, attributes) -> {
            Sheet sheet = getExcelSheetService().createOrGetTypeSheet(workbook, typeCode);
            addHeader(sheet, attributes);
            addValues(itemsByTypeFiltered, typeCode, attributes, sheet);
        });
        signWorkbookFile(workbook);
        return workbook;
    }


    protected Map<String, Set<ItemModel>> applyTypePredicates(Map<String, Set<ItemModel>> itemsByType)
    {
        Predicate<Map.Entry<String, Set<ItemModel>>> applyTypePredicatesToItemByType = itemByType -> {
            String typeCode = (String)itemByType.getKey();
            ComposedTypeModel type = this.typeService.getComposedTypeForCode(typeCode);
            return this.typePredicates.stream().allMatch(());
        };
        return (Map<String, Set<ItemModel>>)itemsByType.entrySet().stream().filter(applyTypePredicatesToItemByType)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    protected List<SelectedAttribute> applyAttributePredicates(List<SelectedAttribute> selectedAttributes)
    {
        Predicate<SelectedAttribute> applyAttributePredicatesToSelectedAttribute = selectedAttribute -> this.attributePredicates.stream().allMatch(());
        return (List<SelectedAttribute>)selectedAttributes.stream().filter(applyAttributePredicatesToSelectedAttribute).collect(Collectors.toList());
    }


    protected void signWorkbookFile(Workbook workbook)
    {
        if(workbook instanceof XSSFWorkbook)
        {
            POIXMLProperties workbookProperties = ((XSSFWorkbook)workbook).getProperties();
            workbookProperties.getCustomProperties().addProperty("SAP Hybris vendor v3", "SAP Hybris vendor v3");
        }
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected List<SelectedAttribute> filterByPermissions(List<SelectedAttribute> selectedAttributes)
    {
        List<SelectedAttribute> filtered = (List<SelectedAttribute>)selectedAttributes.stream().filter(attr -> getPermissionCRUDService().canReadAttribute(attr.getAttributeDescriptor())).collect(Collectors.toList());
        Collection<SelectedAttribute> removed = CollectionUtils.removeAll(selectedAttributes, filtered);
        if(CollectionUtils.isNotEmpty(removed))
        {
            List<String> removedAttributes = (List<String>)removed.stream().map(SelectedAttribute::getQualifier).collect(Collectors.toList());
            LOG.warn("Insufficient permissions for attributes: {}", removedAttributes);
        }
        return filtered;
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected Map<String, Set<ItemModel>> filterByPermissions(Map<String, Set<ItemModel>> itemsByType)
    {
        Map<String, Set<ItemModel>> filtered = (Map<String, Set<ItemModel>>)itemsByType.entrySet().stream().filter(mapEntry -> getPermissionCRUDService().canReadType((String)mapEntry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Collection<String> removed = CollectionUtils.removeAll(itemsByType.keySet(), filtered.keySet());
        if(CollectionUtils.isNotEmpty(removed))
        {
            LOG.warn("Insufficient permissions for types: {}", removed);
        }
        return filtered;
    }


    protected InputStream loadExcelTemplate()
    {
        return getClass().getResourceAsStream(getTemplatePath());
    }


    protected void addHeader(Sheet sheet, Set<SelectedAttribute> selectedAttributes)
    {
        getExcelHeaderService().insertAttributesHeader(sheet, (Collection)selectedAttributes
                        .stream()
                        .map(attr -> new ExcelAttributeDescriptorAttribute(attr.getAttributeDescriptor(), attr.getIsoCode()))
                        .collect(Collectors.toList()));
    }


    protected void addValues(Map<String, Set<ItemModel>> itemsByType, String type, Set<SelectedAttribute> selectedAttributes, Sheet sheet)
    {
        Sheet pkSheet = getExcelSheetService().createOrGetUtilitySheet(sheet.getWorkbook(), ExcelTemplateConstants.UtilitySheet.PK
                        .getSheetName());
        ((Set)itemsByType.get(type)).forEach(item -> {
            Row row = getExcelSheetService().createEmptyRow(sheet);
            AtomicInteger cellIndex = new AtomicInteger(0);
            insertPkRow(pkSheet, row, item);
            for(SelectedAttribute selectedAttribute : selectedAttributes)
            {
                getExcelTranslatorRegistry().getTranslator(selectedAttribute.getAttributeDescriptor(), item).ifPresent(());
            }
        });
    }


    private void insertPkRow(Sheet pkSheet, Row row, ItemModel item)
    {
        Row emptyRow = getExcelSheetService().createEmptyRow(pkSheet);
        getExcelCellService().insertAttributeValue(emptyRow.createCell(0),
                        Long.valueOf(item.getPk().getLongValue()));
        getExcelCellService().insertAttributeValue(emptyRow.createCell(1), row
                        .getSheet().getSheetName());
        getExcelCellService().insertAttributeValue(emptyRow.createCell(2),
                        Integer.valueOf(row.getRowNum()));
    }


    protected Object getItemAttribute(ItemModel item, SelectedAttribute selectedAttribute)
    {
        String qualifier = selectedAttribute.getAttributeDescriptor().getQualifier();
        try
        {
            if(item instanceof VariantProductModel)
            {
                return getVariantAttribute((VariantProductModel)item, selectedAttribute, selectedAttribute
                                .getAttributeDescriptor().getQualifier());
            }
            return getModelAttribute(item, selectedAttribute, selectedAttribute.getAttributeDescriptor().getQualifier());
        }
        catch(AttributeNotSupportedException ex)
        {
            LOG.debug(String.format("Cannot get attribute [%s] value for type [%s]. Fallback with jalo.", new Object[] {qualifier, item
                            .getItemtype()}));
            return selectedAttribute.isLocalized() ?
                            item.getProperty(qualifier, getCommonI18NService().getLocaleForIsoCode(selectedAttribute.getIsoCode())) :
                            item.getProperty(qualifier);
        }
    }


    private Object getModelAttribute(ItemModel item, SelectedAttribute selectedAttribute, String qualifier)
    {
        if(selectedAttribute.isLocalized())
        {
            Locale locale = getCommonI18NService().getLocaleForIsoCode(selectedAttribute.getIsoCode());
            return getModelService().getAttributeValue(item, qualifier, locale);
        }
        return getModelService().getAttributeValue(item, qualifier);
    }


    private Object getVariantAttribute(VariantProductModel item, SelectedAttribute selectedAttribute, String qualifier)
    {
        if(selectedAttribute.isLocalized())
        {
            Locale locale = getCommonI18NService().getLocaleForIsoCode(selectedAttribute.getIsoCode());
            return getBackofficeVariantsService().getLocalizedVariantAttributeValue(item, qualifier).get(locale);
        }
        return getBackofficeVariantsService().getVariantAttributeValue(item, qualifier);
    }


    @Deprecated(since = "6.7", forRemoval = true)
    public DefaultExcelExportDivider getDivider()
    {
        return this.divider;
    }


    @Deprecated(since = "6.7", forRemoval = true)
    public void setDivider(DefaultExcelExportDivider divider)
    {
        this.divider = divider;
    }


    public ExcelExportDivider getExcelExportDivider()
    {
        return this.excelExportDivider;
    }


    @Required
    public void setExcelExportDivider(ExcelExportDivider excelExportDivider)
    {
        this.excelExportDivider = excelExportDivider;
    }


    public String getTemplatePath()
    {
        return this.templatePath;
    }


    @Required
    public void setTemplatePath(String templatePath)
    {
        this.templatePath = templatePath;
    }


    public ExcelTranslatorRegistry getExcelTranslatorRegistry()
    {
        return this.excelTranslatorRegistry;
    }


    @Required
    public void setExcelTranslatorRegistry(ExcelTranslatorRegistry excelTranslatorRegistry)
    {
        this.excelTranslatorRegistry = excelTranslatorRegistry;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ExcelTemplateService getExcelTemplateService()
    {
        return this.excelTemplateService;
    }


    @Required
    @Deprecated(since = "1808", forRemoval = true)
    public void setExcelTemplateService(ExcelTemplateService excelTemplateService)
    {
        this.excelTemplateService = excelTemplateService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Deprecated(since = "1811", forRemoval = true)
    public PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Deprecated(since = "1811", forRemoval = true)
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ExcelSheetService getExcelSheetService()
    {
        return this.excelSheetService;
    }


    @Required
    public void setExcelSheetService(ExcelSheetService excelSheetService)
    {
        this.excelSheetService = excelSheetService;
    }


    public ExcelWorkbookService getExcelWorkbookService()
    {
        return this.excelWorkbookService;
    }


    @Required
    public void setExcelWorkbookService(ExcelWorkbookService excelWorkbookService)
    {
        this.excelWorkbookService = excelWorkbookService;
    }


    public ExcelCellService getExcelCellService()
    {
        return this.excelCellService;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
    }


    public ExcelHeaderService getExcelHeaderService()
    {
        return this.excelHeaderService;
    }


    @Required
    public void setExcelHeaderService(ExcelHeaderService excelHeaderService)
    {
        this.excelHeaderService = excelHeaderService;
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public BackofficeVariantsService getBackofficeVariantsService()
    {
        return this.backofficeVariantsService;
    }


    @Required
    public void setBackofficeVariantsService(BackofficeVariantsService backofficeVariantsService)
    {
        this.backofficeVariantsService = backofficeVariantsService;
    }


    public Set<ExcelExportAttributePredicate> getAttributePredicates()
    {
        return this.attributePredicates;
    }


    @Required
    public void setAttributePredicates(Set<ExcelExportAttributePredicate> attributePredicates)
    {
        this.attributePredicates = attributePredicates;
    }


    public Set<ExcelExportTypePredicate> getTypePredicates()
    {
        return this.typePredicates;
    }


    @Required
    public void setTypePredicates(Set<ExcelExportTypePredicate> typePredicates)
    {
        this.typePredicates = typePredicates;
    }
}
