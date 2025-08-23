package de.hybris.bootstrap.ddl.tools;

import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Table;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DeepClonerTest
{
    @Test
    public void shouldCloneSerializableObject() throws Exception
    {
        DeepCloner<Table> deepCloner = new DeepCloner();
        Table table = new Table();
        table.setName("fooBar");
        Table clonedTable = (Table)deepCloner.cloneDeeply(table);
        Assertions.assertThat(table).isNotSameAs(clonedTable);
        Assertions.assertThat(table).isEqualTo(clonedTable);
    }


    @Test
    public void shouldCloneNotSerializableObject() throws Exception
    {
        DeepCloner<ForeignKey> deepCloner = new DeepCloner();
        ForeignKey foreignKey = new ForeignKey();
        foreignKey.setName("fooBar");
        ForeignKey clonedForeignKey = (ForeignKey)deepCloner.cloneDeeply(foreignKey);
        Assertions.assertThat(foreignKey).isNotSameAs(clonedForeignKey);
        Assertions.assertThat(foreignKey).isEqualTo(clonedForeignKey);
    }
}
