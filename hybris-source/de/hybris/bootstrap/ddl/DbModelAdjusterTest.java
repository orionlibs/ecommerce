package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.ddl.model.YDbTableProvider;
import de.hybris.bootstrap.ddl.model.YTable;
import de.hybris.bootstrap.ddl.sql.DBAwareNonUniqueIndexExtended;
import de.hybris.bootstrap.ddl.sql.DbAwareUniqueIndexExtended;
import de.hybris.bootstrap.ddl.sql.ExtendedAwareIndex;
import de.hybris.bootstrap.ddl.sql.ExtendedParamsForIndex;
import de.hybris.bootstrap.ddl.sql.MSSqlExtendedParamsForIndex;
import de.hybris.platform.testframework.Assert;
import java.util.Objects;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.Table;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DbModelAdjusterTest
{
    @Mock
    private YDbTableProvider tableProvider;


    @Test
    public void allInstancesOfIndexEquivalenceShouldAlwaysBeEqual()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Objects.requireNonNull(adjuster);
        DbModelAdjuster.IndexEquivalence equivalence1 = new DbModelAdjuster.IndexEquivalence(adjuster);
        Objects.requireNonNull(adjuster);
        DbModelAdjuster.IndexEquivalence equivalence2 = new DbModelAdjuster.IndexEquivalence(adjuster);
        Assert.assertEquals(equivalence1, equivalence2);
    }


    @Test
    public void shouldDropAllIndicesFromExistingTable()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = (YTable)Mockito.mock(YTable.class);
        DBAwareNonUniqueIndexExtended existingExtended = new DBAwareNonUniqueIndexExtended();
        existingExtended.addColumn(new IndexColumn("col1"));
        existingTable.addIndex((Index)existingExtended);
        Mockito.when(desiredTable.getIndices()).thenReturn(new Index[0]);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).isEmpty();
    }


    @Test
    public void shouldReturnExistingIndexForExistingTableWhenNothingChanged()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        Index existingExtended = createIndex(Pair.of(new String[] {"col1"}, new String[0]));
        existingTable.addIndex(existingExtended);
        Index desiredExtended = createIndex(Pair.of(new String[] {"col1"}, new String[0]));
        desiredTable.addIndex(desiredExtended);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).isNotEmpty();
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).contains((Object[])new Index[] {existingExtended});
    }


    @Test
    public void shouldReturnExistingIndexForExistingTableWhenNothingChangedExceptColumnNameCase()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        Index existingExtended = createIndex(Pair.of(new String[] {"col1"}, new String[0]));
        existingTable.addIndex(existingExtended);
        Index desiredExtended = createIndex(Pair.of(new String[] {"COL1"}, new String[0]));
        desiredTable.addIndex(desiredExtended);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).isNotEmpty();
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).contains((Object[])new Index[] {existingExtended});
    }


    @Test
    public void shouldReturnExistingIndexForExistingTableWithIncludeSectionWhenNothingChangedExceptColumnNameCaseForInclude()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        Index existingExtended = createIndex(
                        Pair.of(new String[] {"col1"}, new String[] {"inclCol1", "inclCol2"}));
        existingTable.addIndex(existingExtended);
        Index desiredExtended = createIndex(
                        Pair.of(new String[] {"COL1"}, new String[] {"INCLCol1", "INCLCol2"}));
        desiredTable.addIndex(desiredExtended);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).isNotEmpty();
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).contains((Object[])new Index[] {existingExtended});
    }


    @Test
    public void shouldReturnExistingIndexForExistingTableWhenNothingChangedExceptColumnOrderForIncludeSection()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        Index existingExtended = createIndex(
                        Pair.of(new String[] {"col1"}, new String[] {"inclCol1", "inclCol2"}));
        existingTable.addIndex(existingExtended);
        Index desiredExtended = createIndex(
                        Pair.of(new String[] {"COL1"}, new String[] {"inclCol2", "inclCol1"}));
        desiredTable.addIndex(desiredExtended);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).isNotEmpty();
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).contains((Object[])new Index[] {existingExtended});
    }


    @Test
    public void shouldReturnNewIndexForExistingTableWhenIncludeColumnsForIndexChanged()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        Index existingExtended = createIndex(
                        Pair.of(new String[] {"col1"}, new String[] {"inclCol1", "inclCol2"}));
        existingTable.addIndex(existingExtended);
        Index desiredExtended = createIndex(
                        Pair.of(new String[] {"col1"}, new String[] {"inclCol1", "inclCol2", "inclCol3"}));
        desiredTable.addIndex(desiredExtended);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).isNotEmpty();
        Assertions.assertThat((Object[])existingTable.getIndices()).contains((Object[])new Index[] {desiredExtended});
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).doesNotContain((Object[])new Index[] {existingExtended});
    }


    @Test
    public void shouldReturnDesiredIndexForExistingTableWhenIncludeColumnsAreRemoved()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        Index existingExtended = createIndex(
                        Pair.of(new String[] {"col1"}, new String[] {"inclCol1", "inclCol2"}));
        existingTable.addIndex(existingExtended);
        Index desiredExtended = createIndex(Pair.of(new String[] {"COL1"}, new String[0]));
        desiredTable.addIndex(desiredExtended);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).isNotEmpty();
        Assertions.assertThat((Object[])existingTable.getIndices()).contains((Object[])new Index[] {desiredExtended});
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).doesNotContain((Object[])new Index[] {existingExtended});
    }


    @Test
    public void shouldReturnDesiredIndexForExistingTableWhenIncludeColumnsAreRemoved2()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        Index existingIndex = createIndex(Pair.of(new String[] {"col1"}, new String[] {"inclCol1", "inclCol2"}));
        existingTable.addIndex(existingIndex);
        Index desiredIndex = createIndex(Pair.of(new String[] {"COL1"}, new String[0]));
        desiredTable.addIndex(desiredIndex);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).isNotEmpty();
        Assertions.assertThat((Object[])existingTable.getIndices()).contains((Object[])new Index[] {desiredIndex});
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).doesNotContain((Object[])new Index[] {existingIndex});
    }


    @Test
    public void shouldReturnDesiredIndexForExistingTableWhenUniquenessChanged()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        Index existingIndex = createIndex(Pair.of(new String[] {"col1"}, new String[0]));
        existingTable.addIndex(existingIndex);
        Index desiredIndex = createIndex(Pair.of(new String[] {"col1"}, new String[0]), true);
        desiredTable.addIndex(desiredIndex);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).isNotEmpty();
        Assertions.assertThat((Object[])existingTable.getIndices()).contains((Object[])new Index[] {desiredIndex});
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).doesNotContain((Object[])new Index[] {existingIndex});
    }


    @Test
    public void shouldReturnDesiredIndexWithIncludeForExistingTableWhenUniquenessChanged()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        Index existingIndex = createIndex(Pair.of(new String[] {"col1"}, new String[] {"col2"}));
        existingTable.addIndex(existingIndex);
        Index desiredIndex = createIndex(Pair.of(new String[] {"col1"}, new String[] {"col2"}), true);
        desiredTable.addIndex(desiredIndex);
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).contains((Object[])new Index[] {desiredIndex});
        Assertions.assertThat((Object[])existingTable.getIndices()).doesNotContain((Object[])new Index[] {existingIndex});
    }


    @Test
    public void shouldRemoveExistingIndexIfNotFoundInDesiredTable()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        existingTable.addIndex(createIndex("ex1", Pair.of(new String[] {"col1"}, new String[] {"col2"})));
        existingTable.addIndex(createIndex("ex2", Pair.of(new String[] {"col1"}, new String[] {"col3"})));
        desiredTable.addIndex(createIndex("des3", Pair.of(new String[] {"col1"}, new String[] {"col2"})));
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(1);
        Assertions.assertThat((Object[])existingTable.getIndices()).extracting(Index::getName).doesNotContain((Object[])new String[] {"ex2"});
        Assertions.assertThat((Object[])existingTable.getIndices()).extracting(Index::getName).contains((Object[])new String[] {"ex1"});
    }


    @Test
    public void shouldReturnExistingIndicesIfAllAreFoundInDesiredTable()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        existingTable.addIndex(createIndex("ex1", Pair.of(new String[] {"col1"}, new String[] {"col2"})));
        existingTable.addIndex(createIndex("ex2", Pair.of(new String[] {"col1"}, new String[] {"col3"})));
        desiredTable.addIndex(createIndex("des3", Pair.of(new String[] {"col1"}, new String[] {"col2"})));
        desiredTable.addIndex(createIndex("des4", Pair.of(new String[] {"col1"}, new String[] {"col3"})));
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(2);
        Assertions.assertThat((Object[])existingTable.getIndices()).extracting(Index::getName).contains((Object[])new String[] {"ex1", "ex2"});
    }


    @Test
    public void shouldReturnAdditionalDesiredIndexIfNotExistInExistingTable()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        existingTable.addIndex(createIndex("ex1", Pair.of(new String[] {"col1"}, new String[] {"col2"})));
        desiredTable.addIndex(createIndex("des2", Pair.of(new String[] {"col1"}, new String[] {"col2"})));
        desiredTable.addIndex(createIndex("des3", Pair.of(new String[] {"col1"}, new String[] {"col3"})));
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(2);
        Assertions.assertThat((Object[])existingTable.getIndices()).extracting(Index::getName).contains((Object[])new String[] {"ex1", "des3"});
    }


    @Test
    public void shouldReturnExistingIndicesIfAllAreFoundInDesiredTableAndIncludeColumnsAreMixedWihtCoreIndexColumns()
    {
        DbModelAdjuster adjuster = new DbModelAdjuster(this.tableProvider);
        Table existingTable = new Table();
        YTable desiredTable = new YTable("test");
        existingTable.addIndex(createIndex("ex1", Pair.of(new String[] {"col1"}, new String[] {"col2"})));
        existingTable.addIndex(createIndex("ex2", Pair.of(new String[] {"col2"}, new String[] {"col1"})));
        desiredTable.addIndex(createIndex("des3", Pair.of(new String[] {"col1"}, new String[] {"col2"})));
        desiredTable.addIndex(createIndex("des4", Pair.of(new String[] {"col2"}, new String[] {"col1"})));
        adjuster.adjustIndices(existingTable, desiredTable);
        Assertions.assertThat((Object[])existingTable.getIndices()).hasSize(2);
        Assertions.assertThat((Object[])existingTable.getIndices()).extracting(Index::getName).contains((Object[])new String[] {"ex1", "ex2"});
    }


    private Index createIndex(String indexName, Pair<String[], String[]> params, boolean unique)
    {
        DBAwareNonUniqueIndexExtended dBAwareNonUniqueIndexExtended;
        if(unique)
        {
            DbAwareUniqueIndexExtended dbAwareUniqueIndexExtended = new DbAwareUniqueIndexExtended();
        }
        else
        {
            dBAwareNonUniqueIndexExtended = new DBAwareNonUniqueIndexExtended();
        }
        dBAwareNonUniqueIndexExtended.setName(indexName);
        for(String coreCol : (String[])params.getKey())
        {
            dBAwareNonUniqueIndexExtended.addColumn(new IndexColumn(coreCol));
        }
        MSSqlExtendedParamsForIndex extendedParamsForIndex = new MSSqlExtendedParamsForIndex();
        for(String inclCol : (String[])params.getValue())
        {
            extendedParamsForIndex.addColumn(new IndexColumn(inclCol));
        }
        ((ExtendedAwareIndex)dBAwareNonUniqueIndexExtended).setExtendedParams((ExtendedParamsForIndex)extendedParamsForIndex);
        return (Index)dBAwareNonUniqueIndexExtended;
    }


    private Index createIndex(Pair<String[], String[]> params, boolean unique)
    {
        return createIndex("dummyIndexName", params, unique);
    }


    private Index createIndex(String indexName, Pair<String[], String[]> params)
    {
        return createIndex(indexName, params, false);
    }


    private Index createIndex(Pair<String[], String[]> params)
    {
        return createIndex(params, false);
    }
}
