package de.hybris.platform.persistence.property.loader.internal;

import de.hybris.platform.core.Tenant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class TypePKInfoProvider
{
    private static final String PK_JNDI_JALO_CLASS_QRY = "select PK, itemJndiName, jaloClassName from %s";
    private static final String PK_JAVA_CLASS_QRY = "select PK, JavaClassName from %s";
    private final Map<Long, String> pkToJndiName = new HashMap<>();
    private final Map<Long, String> pkToJaloClassName = new HashMap<>();
    private final Map<Long, String> atomicPkToJavaClassName = new HashMap<>();
    private final String atomicTypesTable;
    private final String composedTypesTable;


    private TypePKInfoProvider(Tenant tenant, String atomicTypesTable, String composedTypesTable)
    {
        this.atomicTypesTable = atomicTypesTable;
        this.composedTypesTable = composedTypesTable;
        populatePkToJndiAndJaloClassMaps(tenant);
        populateAtomicPkToJavaClassNameMap(tenant);
    }


    public static TypePKInfoProvider buildForTenant(Tenant tenant, String atomicTypesTable, String composedTypesTable)
    {
        return new TypePKInfoProvider(tenant, atomicTypesTable, composedTypesTable);
    }


    public Map<Long, String> getPkToJndiName()
    {
        return this.pkToJndiName;
    }


    public Map<Long, String> getPkToJaloClassName()
    {
        return this.pkToJaloClassName;
    }


    public Map<Long, String> getAtomicPkToJavaClassName()
    {
        return this.atomicPkToJavaClassName;
    }


    private void populatePkToJndiAndJaloClassMaps(Tenant tenant)
    {
        List<ComposedTypeInfo> composedTypeInfos = (new JdbcTemplate((DataSource)tenant.getDataSource())).query(String.format("select PK, itemJndiName, jaloClassName from %s", new Object[] {this.composedTypesTable}), (RowMapper)new ComposedTypeInfoMapper());
        for(ComposedTypeInfo composedTypeInfo : composedTypeInfos)
        {
            this.pkToJaloClassName.put(composedTypeInfo.getPk(), composedTypeInfo.getJaloClassName());
            this.pkToJndiName.put(composedTypeInfo.getPk(), composedTypeInfo.getItemJndiName());
        }
    }


    private void populateAtomicPkToJavaClassNameMap(Tenant tenant)
    {
        List<AtomicTypeInfo> atomicTypeInfos = (new JdbcTemplate((DataSource)tenant.getDataSource())).query(String.format("select PK, JavaClassName from %s", new Object[] {this.atomicTypesTable}), (RowMapper)new AtomicTypeInfoMapper());
        for(AtomicTypeInfo atomicTypeInfo : atomicTypeInfos)
        {
            this.atomicPkToJavaClassName.put(atomicTypeInfo.getPk(), atomicTypeInfo.getJavaClassName());
        }
    }
}
