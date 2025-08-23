package de.hybris.platform.persistence.property.loader.internal;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.persistence.property.loader.internal.dto.AttributeDTO;
import de.hybris.platform.persistence.property.loader.internal.dto.ComposedTypeDTO;
import de.hybris.platform.persistence.property.loader.internal.mapper.AttributeRowMapper;
import de.hybris.platform.persistence.property.loader.internal.mapper.ComposedTypeRowMapper;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class BatchTypeInfoRepository
{
    private static final String SELECT_FROM_QRY = "SELECT * FROM %s";
    private final JdbcTemplate jdbcTemplate;
    private final String attributeDescriptorsTable;
    private final String composedTypesTable;


    public BatchTypeInfoRepository(Tenant tenant, String attributeDescriptorsTable, String composedTypesTable)
    {
        this.jdbcTemplate = new JdbcTemplate((DataSource)tenant.getDataSource());
        this.attributeDescriptorsTable = attributeDescriptorsTable;
        this.composedTypesTable = composedTypesTable;
    }


    public List<ComposedTypeDTO> findComposedTypes(TypePKInfoProvider typePKInfoProvider, Map<String, ItemDeployment> deploymentInfo)
    {
        ComposedTypeRowMapper composedTypeRowMapper = new ComposedTypeRowMapper(typePKInfoProvider, deploymentInfo);
        return this.jdbcTemplate.query(String.format("SELECT * FROM %s", new Object[] {this.composedTypesTable}), (RowMapper)composedTypeRowMapper);
    }


    public List<AttributeDTO> findAttributes()
    {
        return this.jdbcTemplate.query(String.format("SELECT * FROM %s", new Object[] {this.attributeDescriptorsTable}), (RowMapper)new AttributeRowMapper());
    }
}
