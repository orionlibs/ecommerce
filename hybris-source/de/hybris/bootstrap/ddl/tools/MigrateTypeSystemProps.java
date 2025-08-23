package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;
import java.util.Objects;

public class MigrateTypeSystemProps
{
    private final PersistenceInformation persistenceInfo;
    private final String deploymentsTableName;
    private final String typeSystemPropsSrcTable;
    private final String typeSystemPropsDstTable;
    private final String composedTypesTable;
    private static final String TYPE_SYSTEM_TYPES = "'ComposedType', 'AtomicType', 'CollectionType', 'MapType', 'AttributeDescriptor', 'EnumerationValue'";
    private static final String TYPE_SYSTEM_JNDI_NAMES = "'de.hybris.platform.persistence.type.ComposedType', 'de.hybris.platform.persistence.type.AtomicType', 'de.hybris.platform.persistence.type.CollectionType', 'de.hybris.platform.persistence.type.MapType', 'de.hybris.platform.persistence.type.AttributeDescriptor', 'de.hybris.platform.persistence.enumeration.EnumerationValue'";


    public MigrateTypeSystemProps(PersistenceInformation persistenceInfo)
    {
        this.persistenceInfo = Objects.<PersistenceInformation>requireNonNull(persistenceInfo);
        this.deploymentsTableName = persistenceInfo.toRealTableName("ydeployments");
        this.typeSystemPropsSrcTable = persistenceInfo.toRealTableName("props");
        this.typeSystemPropsDstTable = persistenceInfo.toRealTableName("typesystemprops");
        this.composedTypesTable = persistenceInfo.toRealTableName("composedtypes");
    }


    public Iterable<SqlStatement> getStatementsToExecute()
    {
        if(containsTypeSystemPropsTable())
        {
            return Iterables.concat(new Iterable[] {(Iterable)ImmutableList.of(updateDeploymenTableWithTypeSystemProps(),
                            copyPropsEntriesIntoTypeSystemProps(), deleteOldPropsEntries())});
        }
        return Iterables.concat(createTypeSystemPropsTable(), (Iterable)ImmutableList.of(updateDeploymenTableWithTypeSystemProps(),
                        copyPropsEntriesIntoTypeSystemProps(),
                        deleteOldPropsEntries()));
    }


    private Iterable<SqlStatement> createTypeSystemPropsTable()
    {
        CopyTableOperation copyPropsOperation = CopyTableOperation.withoutData(this.typeSystemPropsSrcTable, this.typeSystemPropsDstTable, false);
        return (new TablesStructureCopier(this.persistenceInfo)).getCopyStatements((Iterable)ImmutableList.of(copyPropsOperation));
    }


    private boolean containsTypeSystemPropsTable()
    {
        return this.persistenceInfo.containsTypeSystemPropsTable();
    }


    private SqlStatement updateDeploymenTableWithTypeSystemProps()
    {
        DMLStatement stmt = new DMLStatement(
                        "update " + this.deploymentsTableName + " set propstableName ='typesystemprops' where propstablename NOT LIKE '%typesystemprops%' AND name IN ('ComposedType', 'AtomicType', 'CollectionType', 'MapType', 'AttributeDescriptor', 'EnumerationValue') AND typesystemname='"
                                        + this.persistenceInfo.getTypeSystemName() + "'");
        stmt.setStopIfEmpty(true);
        return (SqlStatement)stmt;
    }


    private SqlStatement copyPropsEntriesIntoTypeSystemProps()
    {
        return (SqlStatement)new DMLStatement("insert into " + this.typeSystemPropsDstTable + " select * from " + this.typeSystemPropsSrcTable + " where itemtypePK IN ( select PK from " + this.composedTypesTable
                        + " where itemJNDIName IN ('de.hybris.platform.persistence.type.ComposedType', 'de.hybris.platform.persistence.type.AtomicType', 'de.hybris.platform.persistence.type.CollectionType', 'de.hybris.platform.persistence.type.MapType', 'de.hybris.platform.persistence.type.AttributeDescriptor', 'de.hybris.platform.persistence.enumeration.EnumerationValue'))");
    }


    private SqlStatement deleteOldPropsEntries()
    {
        return (SqlStatement)new DMLStatement("delete from " + this.typeSystemPropsSrcTable + " where itemtypePK IN ( select PK from " + this.composedTypesTable
                        + " where itemJNDIName IN ('de.hybris.platform.persistence.type.ComposedType', 'de.hybris.platform.persistence.type.AtomicType', 'de.hybris.platform.persistence.type.CollectionType', 'de.hybris.platform.persistence.type.MapType', 'de.hybris.platform.persistence.type.AttributeDescriptor', 'de.hybris.platform.persistence.enumeration.EnumerationValue'))");
    }
}
