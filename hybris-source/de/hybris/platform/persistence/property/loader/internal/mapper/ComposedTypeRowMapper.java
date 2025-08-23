package de.hybris.platform.persistence.property.loader.internal.mapper;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.jalo.type.ViewType;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.persistence.property.loader.internal.TypePKInfoProvider;
import de.hybris.platform.persistence.property.loader.internal.dto.ComposedTypeDTO;
import de.hybris.platform.spring.CGLibUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ComposedTypeRowMapper implements RowMapper<ComposedTypeDTO>
{
    private static final Logger LOG = LoggerFactory.getLogger(ComposedTypeRowMapper.class);
    private final JDBCValueMappings.ValueReader longReader = JDBCValueMappings.getInstance().getValueReader(Long.class);
    private final JDBCValueMappings.ValueReader stringReader = JDBCValueMappings.getInstance().getValueReader(String.class);
    private final JDBCValueMappings.ValueReader intReader = JDBCValueMappings.getInstance().getValueReader(Integer.class);
    private final JDBCValueMappings.ValueReader booleanReader = JDBCValueMappings.getInstance().getValueReader(Boolean.class);
    private final Map<Long, String> pkToJndiName;
    private final Map<Long, String> pkToJaloClassName;
    private final Map<String, ItemDeployment> deploymentInfo;


    public ComposedTypeRowMapper(TypePKInfoProvider typePKInfoProvider, Map<String, ItemDeployment> deploymentInfo)
    {
        this.pkToJndiName = typePKInfoProvider.getPkToJndiName();
        this.pkToJaloClassName = typePKInfoProvider.getPkToJaloClassName();
        this.deploymentInfo = deploymentInfo;
    }


    public ComposedTypeDTO mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        long pk = this.longReader.getLong(rs, "PK");
        String typePkString = (String)this.stringReader.getValue(rs, "TypePkString");
        Boolean isJaloOnly = Boolean.valueOf(this.booleanReader.getBoolean(rs, "p_jaloonly"));
        String jaloClassName = (String)this.stringReader.getValue(rs, "jaloClassName");
        String textTypeCode = (String)this.stringReader.getValue(rs, "InternalCode");
        int itemTypeCode = this.intReader.getInt(rs, "itemTypeCode");
        Long superTypePk = Long.valueOf(this.longReader.getLong(rs, "SuperTypePK"));
        String jndiName = (String)this.stringReader.getValue(rs, "itemJNDIName");
        Boolean propertyTableStatus = Boolean.valueOf(this.booleanReader.getBoolean(rs, "propertyTableStatus"));
        ItemDeployment deployment = this.deploymentInfo.get(jndiName);
        ItemDeployment superDeployment = getDeploymentForTypePK(superTypePk);
        boolean hasOwnDeployment = hasOwnDeployment(deployment, superDeployment);
        Boolean isTypeAbstract = Boolean.valueOf((itemTypeCode <= 0));
        boolean isAbstract = (isJaloOnly.booleanValue() || (isTypeAbstract.booleanValue() && (!hasOwnDeployment || deployment.isAbstract())));
        Boolean isViewType = Boolean.valueOf(isViewType(typePkString));
        int modifiers = calculateModifiers(isJaloOnly, Boolean.valueOf(isAbstract), isViewType);
        boolean isRelation = isRelation(typePkString, isAbstract);
        String propsTable = getPropsTable(deployment);
        String itemTableName = (deployment == null) ? null : deployment.getDatabaseTableName();
        String auditTableName = (deployment == null) ? null : deployment.getAuditTableName();
        ComposedTypeDTO composedType = new ComposedTypeDTO();
        composedType.setPk(pk);
        composedType.setSuperTypePk(superTypePk.longValue());
        composedType.setItemTypeCode(escapeItemTypeCode(itemTypeCode));
        if(isTypeAbstract.booleanValue())
        {
            composedType.setAbstractTypeCodeFlag();
        }
        composedType.setTypePkString(typePkString);
        composedType.setJaloOnly(isJaloOnly);
        composedType.setJaloClassName(jaloClassName);
        composedType.setTypeCode(textTypeCode);
        composedType.setDeployment(deployment);
        composedType.setSuperDeployment(superDeployment);
        composedType.setAbstract(Boolean.valueOf(isAbstract));
        composedType.setViewType(isViewType);
        composedType.setRelation(Boolean.valueOf(isRelation));
        composedType.setPropsTable(propsTable);
        composedType.setItemTableName(itemTableName);
        composedType.setAuditTableName(auditTableName);
        composedType.setModifiers(modifiers);
        composedType.setPropertyTableStatus(propertyTableStatus);
        return composedType;
    }


    private String getPropsTable(ItemDeployment deployment)
    {
        return (deployment == null) ? null : deployment.getDumpPropertyTableName();
    }


    private boolean isRelation(String typePkString, boolean isAbstract)
    {
        return (!isAbstract && "de.hybris.platform.jalo.type.RelationType".equals(this.pkToJaloClassName
                        .get(Long.valueOf(typePkString))));
    }


    private int escapeItemTypeCode(int itemTypeCode)
    {
        return (itemTypeCode < 0) ? (itemTypeCode * -1) : itemTypeCode;
    }


    private boolean isViewType(String typePkString)
    {
        String className = this.pkToJaloClassName.get(Long.valueOf(typePkString));
        Class<?> metaTypeJaloClass = getDeclaredJaloClass(className);
        return (metaTypeJaloClass != null && ViewType.class.isAssignableFrom(metaTypeJaloClass));
    }


    private Class getDeclaredJaloClass(String className)
    {
        try
        {
            return (className != null) ? CGLibUtils.getOriginalClass(Class.forName(className)) : null;
        }
        catch(ClassNotFoundException e)
        {
            LOG.error("Failed to load declared jalo class", e);
            return null;
        }
    }


    private ItemDeployment getDeploymentForTypePK(Long superTypePk)
    {
        ItemDeployment superDeployment = null;
        if(superTypePk != null)
        {
            String superJndi = this.pkToJndiName.get(superTypePk);
            if(superJndi != null)
            {
                superDeployment = this.deploymentInfo.get(superJndi);
            }
        }
        return superDeployment;
    }


    private boolean hasOwnDeployment(ItemDeployment deployment, ItemDeployment superDeployment)
    {
        if(superDeployment == null)
        {
            return (deployment != null);
        }
        return (superDeployment.getTypeCode() != deployment.getTypeCode());
    }


    private int calculateModifiers(Boolean isJaloOnly, Boolean isAbstract, Boolean isViewType)
    {
        int modifiers = 0;
        if(isAbstract.booleanValue())
        {
            modifiers |= 0x1;
        }
        if(isJaloOnly.booleanValue())
        {
            modifiers |= 0x4;
        }
        if(isViewType.booleanValue())
        {
            modifiers |= 0x2;
        }
        return modifiers;
    }
}
