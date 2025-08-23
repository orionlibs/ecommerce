package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.model.YColumn;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.ddlutils.model.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DatabaseModelVerifier
{
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseModelVerifier.class);


    static void verifyCorrectnessOfReusedColumnForAttribute(DatabaseVerifierParams verifierParams)
    {
        YColumn omittedColumn = createOmittedColumn(verifierParams);
        YColumn reusedColumn = verifierParams.getExistingColumn();
        if(checkIfColumnsDiffer((Column)omittedColumn, (Column)reusedColumn))
        {
            String infoMsg = createInformationAboutDifference(verifierParams, omittedColumn, reusedColumn);
            LOG.info(infoMsg);
        }
    }


    private static String createInformationAboutDifference(DatabaseVerifierParams verifierParams, YColumn omittedColumn, YColumn reusedColumn)
    {
        String targetTableName = verifierParams.getTargetTableName();
        YAttributeDescriptor attributeToVerify = verifierParams.getAttributeToVerify();
        return String.format("Column: %s for table: %s with definition %s was already defined for attributes: %s and will be reused for attribute: %s where definition for this attribute is different: %s. Adjust this attributes in *items.xml to match each other", new Object[] {reusedColumn
                        .getName(), targetTableName, getInfoAboutColumn(reusedColumn),
                        getAttributesInfo(reusedColumn),
                        getYAttributeDescriptorInfo(attributeToVerify), getInfoAboutColumn(omittedColumn)});
    }


    private static YColumn createOmittedColumn(DatabaseVerifierParams verifierParams)
    {
        DatabaseModelGenerator modelGenerator = verifierParams.getModelGenerator();
        return (YColumn)modelGenerator.createColumn(verifierParams.getAttributeToVerify(), verifierParams.getColumnName(), verifierParams
                        .getTargetTableName());
    }


    private static String getAttributesInfo(YColumn reusedColumn)
    {
        return reusedColumn.getAttributeDescriptors()
                        .stream()
                        .map(DatabaseModelVerifier::getYAttributeDescriptorInfo)
                        .collect(Collectors.joining(", "));
    }


    private static String getYAttributeDescriptorInfo(YAttributeDescriptor desc)
    {
        return desc.getEnclosingTypeCode() + "." + desc.getEnclosingTypeCode() + "(" + desc.getQualifier() + ")";
    }


    private static String getInfoAboutColumn(YColumn column)
    {
        return String.format("type:%s, size:%s, customColumnDefinition:%s", new Object[] {prettyNullString(column.getType()), prettyNullString(column
                        .getSize()), prettyNullString(column.getCustomColumnDefinition())});
    }


    private static String prettyNullString(String valueToFormat)
    {
        return (valueToFormat == null) ? "'-'" : String.format("'%s'", new Object[] {valueToFormat});
    }


    private static boolean checkIfColumnsDiffer(Column omittedColumn, Column reusedColumn)
    {
        return !Objects.equals(customColumnDefinitionOf(omittedColumn), customColumnDefinitionOf(reusedColumn));
    }


    private static String customColumnDefinitionOf(Column column)
    {
        if(column instanceof YColumn)
        {
            return ((YColumn)column).getCustomColumnDefinition();
        }
        return null;
    }
}
