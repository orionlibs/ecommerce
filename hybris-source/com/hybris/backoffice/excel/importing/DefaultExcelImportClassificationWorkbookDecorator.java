package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.classification.ExcelClassificationAttributeFactory;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.importing.data.ClassificationTypeSystemRow;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelImportClassificationWorkbookDecorator extends AbstractExcelImportWorkbookDecorator
{
    private ExcelClassificationTypeSystemService excelClassificationTypeSystemService;
    private ClassificationSystemService classificationSystemService;
    private ExcelClassificationAttributeFactory excelClassificationAttributeFactory;


    protected Collection<ExcelAttribute> getExcelAttributes(Sheet sheet)
    {
        Collection<ExcelAttribute> attributes = new ArrayList<>();
        ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem typeSystem = getExcelClassificationTypeSystemService().loadTypeSystem(sheet.getWorkbook());
        Map<String, ClassificationSystemVersionModel> versionCache = new HashMap<>();
        Map<String, ClassificationClassModel> classCache = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        for(int columnIndex = headerRow.getFirstCellNum(); columnIndex <= headerRow.getLastCellNum(); columnIndex++)
        {
            String header = getExcelCellService().getCellValue(headerRow.getCell(columnIndex));
            Optional<ClassificationTypeSystemRow> row = typeSystem.findRow(header);
            if(row.isPresent())
            {
                ClassificationTypeSystemRow classificationTypeSystemRow = row.get();
                ClassificationSystemVersionModel classificationSystemVersionModel = getClassificationSystemVersionModel(versionCache, classificationTypeSystemRow);
                ClassificationClassModel classificationClassModel = getClassificationClassModel(classCache, classificationTypeSystemRow, classificationSystemVersionModel);
                List<ClassAttributeAssignmentModel> assignments = classificationClassModel.getDeclaredClassificationAttributeAssignments();
                if(CollectionUtils.isNotEmpty(assignments))
                {
                    assignments.stream()
                                    .filter(assignment -> classificationTypeSystemRow.getClassificationAttribute().equals(assignment.getClassificationAttribute().getCode()))
                                    .findFirst().ifPresent(assignment -> attributes.add(this.excelClassificationAttributeFactory.create(assignment, classificationTypeSystemRow.getIsoCode())));
                }
            }
        }
        return attributes;
    }


    private ClassificationSystemVersionModel getClassificationSystemVersionModel(Map<String, ClassificationSystemVersionModel> versionCache, ClassificationTypeSystemRow classificationTypeSystemRow)
    {
        String versionKey = String.format("%s:%s", new Object[] {classificationTypeSystemRow.getClassificationSystem(), classificationTypeSystemRow
                        .getClassificationVersion()});
        return versionCache.computeIfAbsent(versionKey, key -> getClassificationSystemService().getSystemVersion(classificationTypeSystemRow.getClassificationSystem(), classificationTypeSystemRow.getClassificationVersion()));
    }


    private ClassificationClassModel getClassificationClassModel(Map<String, ClassificationClassModel> classCache, ClassificationTypeSystemRow classificationTypeSystemRow, ClassificationSystemVersionModel classificationSystemVersionModel)
    {
        String classKey = String.format("%s:%s:%s", new Object[] {classificationTypeSystemRow.getClassificationSystem(), classificationTypeSystemRow
                        .getClassificationVersion(), classificationTypeSystemRow.getClassificationClass()});
        return classCache.computeIfAbsent(classKey, key -> getClassificationSystemService().getClassForCode(classificationSystemVersionModel, classificationTypeSystemRow.getClassificationClass()));
    }


    public ClassificationSystemService getClassificationSystemService()
    {
        return this.classificationSystemService;
    }


    @Required
    public void setClassificationSystemService(ClassificationSystemService classificationSystemService)
    {
        this.classificationSystemService = classificationSystemService;
    }


    @Required
    public void setExcelClassificationAttributeFactory(ExcelClassificationAttributeFactory excelClassificationAttributeFactory)
    {
        this.excelClassificationAttributeFactory = excelClassificationAttributeFactory;
    }


    public ExcelClassificationTypeSystemService getExcelClassificationTypeSystemService()
    {
        return this.excelClassificationTypeSystemService;
    }


    @Required
    public void setExcelClassificationTypeSystemService(ExcelClassificationTypeSystemService excelClassificationTypeSystemService)
    {
        this.excelClassificationTypeSystemService = excelClassificationTypeSystemService;
    }
}
