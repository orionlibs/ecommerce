package de.hybris.bootstrap.ddl;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import de.hybris.bootstrap.ddl.model.YDbTableProvider;
import de.hybris.bootstrap.ddl.model.YTable;
import java.util.Arrays;
import java.util.Set;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;

public class DbModelAdjuster
{
    private final YDbTableProvider dbModelChanges;


    public DbModelAdjuster(YDbTableProvider yDbModel)
    {
        Preconditions.checkNotNull(yDbModel);
        this.dbModelChanges = yDbModel;
    }


    public Database adjust(Database existingDB)
    {
        for(YTable desiredTable : this.dbModelChanges.getTables().values())
        {
            String prefixedTableName = desiredTable.getName();
            Table possiblyExistingTable = existingDB.findTable(prefixedTableName, false);
            if(possiblyExistingTable != null)
            {
                adjust(possiblyExistingTable, desiredTable);
                continue;
            }
            existingDB.addTable((Table)desiredTable);
        }
        return existingDB;
    }


    private Table adjust(Table existingTable, YTable yDbModelTable) throws TableColumnAdjustmentException
    {
        adjustColumns(existingTable, yDbModelTable);
        adjustIndices(existingTable, yDbModelTable);
        return existingTable;
    }


    private void adjustColumns(Table existingTable, YTable yDbModelTable)
    {
        for(Column desiredColumn : yDbModelTable.getColumns())
        {
            Column existingColumn = existingTable.findColumn(desiredColumn.getName(), false);
            if(existingColumn == null)
            {
                existingTable.addColumn(desiredColumn);
            }
        }
    }


    void adjustIndices(Table existingTable, YTable yDbModelTable)
    {
        Index[] existingTableIndices = existingTable.getIndices();
        Index[] desiredTableIndices = yDbModelTable.getIndices();
        if(desiredTableIndices.length == 0)
        {
            dropAllIndices(existingTable);
        }
        else
        {
            Set<Equivalence.Wrapper<Index>> existingIndicesWrap = wrapIndices(existingTableIndices);
            Set<Equivalence.Wrapper<Index>> desiredIndicesWrap = wrapIndices(desiredTableIndices);
            Sets.SetView<Equivalence.Wrapper<Index>> indicesToAdd = Sets.difference(desiredIndicesWrap, existingIndicesWrap);
            Sets.SetView<Equivalence.Wrapper<Index>> indicesToDrop = Sets.difference(existingIndicesWrap, desiredIndicesWrap);
            addIndicesToTable(existingTable, indicesToAdd);
            dropIndicesFromTable(existingTable, indicesToDrop);
        }
    }


    private void dropAllIndices(Table table)
    {
        for(Index idx : table.getIndices())
        {
            table.removeIndex(idx);
        }
    }


    private Set<Equivalence.Wrapper<Index>> wrapIndices(Index[] indices)
    {
        return Sets.newHashSet(Iterables.transform(Arrays.asList(indices), (Function)new Object(this)));
    }


    private void addIndicesToTable(Table existingTable, Sets.SetView<Equivalence.Wrapper<Index>> indicesToAdd)
    {
        for(UnmodifiableIterator<Equivalence.Wrapper<Index>> unmodifiableIterator = indicesToAdd.iterator(); unmodifiableIterator.hasNext(); )
        {
            Equivalence.Wrapper<Index> idxWrapper = unmodifiableIterator.next();
            existingTable.addIndex((Index)idxWrapper.get());
        }
    }


    private void dropIndicesFromTable(Table existingTable, Sets.SetView<Equivalence.Wrapper<Index>> indicesToDrop)
    {
        for(UnmodifiableIterator<Equivalence.Wrapper<Index>> unmodifiableIterator = indicesToDrop.iterator(); unmodifiableIterator.hasNext(); )
        {
            Equivalence.Wrapper<Index> idxWrapper = unmodifiableIterator.next();
            existingTable.removeIndex((Index)idxWrapper.get());
        }
    }
}
