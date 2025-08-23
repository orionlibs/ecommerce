package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationTypeSystemSheetCompressor
{
    protected static final String MERGING_SYMBOL = "#&#";
    protected static final BiFunction<String, String, String> MERGING_STRATEGY;
    private CollectionFormatter collectionFormatter;

    static
    {
        MERGING_STRATEGY = ((left, right) -> left + "#&#" + left);
    }

    public Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>> compress(@Nonnull Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>> rows)
    {
        return (Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>>)((Map)rows.stream()
                        .collect(Collectors.toMap(ClassificationTypeSystemSheetCompressor::groupByClassificationAttributeAndClassificationClass,
                                        Function.identity(), ClassificationTypeSystemSheetCompressor::mergeRows)))
                        .values()
                        .stream()
                        .map(this::mapRowToCollectionFormat)
                        .collect(Collectors.toList());
    }


    private static String groupByClassificationAttributeAndClassificationClass(Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> row)
    {
        return String.format("%s%s", new Object[] {row.get(ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_ATTRIBUTE), row
                        .get(ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_CLASS)});
    }


    private Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> mapRowToCollectionFormat(Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> row)
    {
        Function<String, String> collectionFormattingMapper = cellValue -> cellValue.contains("#&#") ? this.collectionFormatter.formatToString(cellValue.split("#&#")) : cellValue;
        return (Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>)row.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> (String)collectionFormattingMapper.apply((String)entry.getValue())));
    }


    private static Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> mergeRows(Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> left, Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> right)
    {
        left.merge(ExcelTemplateConstants.ClassificationTypeSystemColumns.FULL_NAME, right
                        .get(ExcelTemplateConstants.ClassificationTypeSystemColumns.FULL_NAME), MERGING_STRATEGY);
        left.merge(ExcelTemplateConstants.ClassificationTypeSystemColumns.ATTRIBUTE_LOC_LANG, right
                        .get(ExcelTemplateConstants.ClassificationTypeSystemColumns.ATTRIBUTE_LOC_LANG), MERGING_STRATEGY);
        return left;
    }


    @Required
    public void setCollectionFormatter(CollectionFormatter collectionFormatter)
    {
        this.collectionFormatter = collectionFormatter;
    }
}
