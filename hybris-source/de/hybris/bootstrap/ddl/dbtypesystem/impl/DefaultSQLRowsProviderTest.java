package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

@IntegrationTest
public class DefaultSQLRowsProviderTest extends ServicelayerBaseTest
{
    private RowsProvider rowsProvider;
    @Resource
    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() throws Exception
    {
        this.rowsProvider = (RowsProvider)new DefaultSQLRowsProvider(this.jdbcTemplate, "junit_", "DEFAULT");
    }


    @Test
    public void testGetNumberSeriesRows() throws Exception
    {
        Iterable<NumberSeriesRow> rows = this.rowsProvider.getNumberSeriesRows();
        Assertions.assertThat(rows).isNotEmpty();
        for(NumberSeriesRow row : rows)
        {
            Assertions.assertThat(row.getSeriesKey()).startsWith("pk_");
            Assertions.assertThat(row.getSeriesType()).isEqualTo(1);
        }
    }


    @Test
    public void testTypeRowMapper()
    {
        Iterable<TypeRow> rows = this.rowsProvider.getTypeRows();
        Assertions.assertThat(rows).isNotEmpty();
    }
}
