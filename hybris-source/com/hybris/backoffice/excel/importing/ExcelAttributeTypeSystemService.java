package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.populator.typesheet.TypeSystemRow;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public class ExcelAttributeTypeSystemService implements ExcelTypeSystemService<ExcelAttributeTypeSystemService.ExcelTypeSystem>
{
    private static final String LANG_GROUP_NAME = "lang";
    private static final Pattern LANG_EXTRACT_PATTERN = Pattern.compile(".+\\[(?<lang>.+)]");
    private ExcelCellService cellService;
    private DefaultCommonI18NService commonI18NService;
    private CollectionFormatter collectionFormatter;


    public ExcelTypeSystem loadTypeSystem(Workbook workbook)
    {
        ExcelTypeSystem typeSystem = new ExcelTypeSystem();
        Sheet typeSheet = workbook.getSheet(ExcelTemplateConstants.UtilitySheet.TYPE_SYSTEM.getSheetName());
        for(int rowIndex = 1; rowIndex <= typeSheet.getLastRowNum(); rowIndex++)
        {
            Row row = typeSheet.getRow(rowIndex);
            List<TypeSystemRow> typeSystemRows = createTypeSystemRows(row);
            typeSystemRows
                            .forEach(typeSystemRow -> typeSystem.putRow(typeSystemRow.getAttrDisplayName(), typeSystemRow));
        }
        return typeSystem;
    }


    private List<TypeSystemRow> createTypeSystemRows(Row row)
    {
        ArrayList<TypeSystemRow> typeSystemRows = new ArrayList<>();
        if(row != null)
        {
            for(String fullName : decompressCellValue(
                            getCellValue(row, ExcelTemplateConstants.TypeSystem.ATTR_DISPLAYED_NAME)))
            {
                typeSystemRows.add(createTypeSystemRow(row, fullName));
            }
        }
        return typeSystemRows;
    }


    private TypeSystemRow createTypeSystemRow(Row row, String fullName)
    {
        TypeSystemRow typeSystemRow = new TypeSystemRow();
        typeSystemRow.setTypeCode(getCellValue(row, ExcelTemplateConstants.TypeSystem.TYPE_CODE));
        typeSystemRow.setTypeName(getCellValue(row, ExcelTemplateConstants.TypeSystem.TYPE_NAME));
        typeSystemRow.setAttrQualifier(getCellValue(row, ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER));
        typeSystemRow.setAttrName(getCellValue(row, ExcelTemplateConstants.TypeSystem.ATTR_NAME));
        typeSystemRow.setAttrOptional(Boolean.valueOf("true".equalsIgnoreCase(getCellValue(row, ExcelTemplateConstants.TypeSystem.ATTR_OPTIONAL))));
        typeSystemRow.setAttrTypeCode(getCellValue(row, ExcelTemplateConstants.TypeSystem.ATTR_TYPE_CODE));
        typeSystemRow.setAttrTypeItemType(getCellValue(row, ExcelTemplateConstants.TypeSystem.ATTR_TYPE_ITEMTYPE));
        typeSystemRow
                        .setAttrLocalized(Boolean.valueOf("true".equalsIgnoreCase(getCellValue(row, ExcelTemplateConstants.TypeSystem.ATTR_LOCALIZED))));
        typeSystemRow.setAttrLocLang(extractIsoCode(fullName));
        typeSystemRow.setAttrDisplayName(fullName);
        typeSystemRow.setAttrUnique(Boolean.valueOf("true".equalsIgnoreCase(getCellValue(row, ExcelTemplateConstants.TypeSystem.ATTR_UNIQUE))));
        typeSystemRow.setAttrReferenceFormat(getCellValue(row, ExcelTemplateConstants.TypeSystem.REFERENCE_FORMAT));
        return typeSystemRow;
    }


    private Collection<String> decompressCellValue(String value)
    {
        Set<String> decompressedValues = this.collectionFormatter.formatToCollection(value);
        if(decompressedValues.isEmpty())
        {
            decompressedValues.add(value);
        }
        return decompressedValues;
    }


    private String extractIsoCode(String fullName)
    {
        Matcher matcher = LANG_EXTRACT_PATTERN.matcher(fullName);
        if(matcher.find())
        {
            List<LanguageModel> supportLang = this.commonI18NService.getAllLanguages();
            LanguageModel language = supportLang.stream().filter(lang -> lang.getIsocode().equals(matcher.group("lang"))).findFirst().orElse(null);
            if(language != null)
            {
                return matcher.group("lang");
            }
        }
        return "";
    }


    private String getCellValue(Row row, ExcelTemplateConstants.TypeSystem column)
    {
        return this.cellService.getCellValue(row.getCell(column.getIndex()));
    }


    @Required
    public void setCellService(ExcelCellService cellService)
    {
        this.cellService = cellService;
    }


    private DefaultCommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(DefaultCommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setCollectionFormatter(CollectionFormatter collectionFormatter)
    {
        this.collectionFormatter = collectionFormatter;
    }
}
