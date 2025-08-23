package de.hybris.platform.util.typesystem;

import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YTypeSystemLoader;
import de.hybris.bootstrap.typesystem.dto.DeploymentDTO;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.util.Config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

public class YDeploymentJDBC
{
    private static final Logger log = Logger.getLogger(YDeploymentJDBC.class);
    private static final String TYPECODE = "Typecode";
    private static final String TABLENAME = "TableName";
    private static final String PROPSTABLENAME = "PropsTableName";
    private static final String NAME = "Name";
    private static final String PACKAGENAME = "PackageName";
    private static final String SUPERNAME = "SuperName";
    private static final String EXTENSIONNAME = "ExtensionName";
    private static final String MODIFIERS = "Modifiers";
    private static final String TYPESYSTEMNAME = "TypeSystemName";
    private static final String AUDITTABLENAME = "AuditTableName";
    private static final byte ABSTRACT = 0;
    private static final byte GENERIC = 1;
    private static final byte FINAL = 3;
    private static final byte NONITEM = 4;


    private static final String getTableName()
    {
        return Config.getString("db.tableprefix", "") + "ydeployments";
    }


    private static final String getTypeSystemName()
    {
        return Config.getString("db.type.system.name", "DEFAULT");
    }


    private static final String getInsertQuery()
    {
        return "INSERT INTO " + getTableName() + " (Typecode,TableName,PropsTableName,Name,PackageName,SuperName,ExtensionName,Modifiers,TypeSystemName,AuditTableName) VALUES ( ?,?,?,?,?,?,?,?,?,? )";
    }


    private static final String getUpdateByTypecodeQuery()
    {
        return "UPDATE " + getTableName() + " SET TableName=?, PropsTableName=?, Name=?, PackageName=?, SuperName=?, ExtensionName=?, Modifiers=?, TypeSystemName=? AuditTableName=? WHERE Typecode=? and TypeSystemName=?";
    }


    private static final String getUpdateByNameQuery()
    {
        return "UPDATE " + getTableName() + " SET TableName=?, PropsTableName=?, Typecode=?, PackageName=?, SuperName=?, ExtensionName=?, Modifiers=?, TypeSystemName=? AuditTableName=? WHERE Name=? and TypeSystemName=?";
    }


    private static final void fillInsertQuery(PreparedStatement stmt, YItemDeploymentWrapper wrapper, String extension) throws SQLException
    {
        YDeployment depl = wrapper.getOriginal();
        JDBCValueMappings.getInstance().getValueWriter(Integer.class).setValue(stmt, 1, Integer.valueOf(depl.getItemTypeCode()));
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 2, depl.getTableName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 3, depl.getPropsTableName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 4, depl.getName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 5, depl.getPackageName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 6, (depl.getSuperDeployment() == null) ? null :
                        depl.getSuperDeployment().getFullName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 7, extension);
        JDBCValueMappings.getInstance().getValueWriter(Integer.class).setValue(stmt, 8, Integer.valueOf(getModifiers(wrapper)));
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 9, getTypeSystemName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 10, depl.getAuditTableName());
    }


    private static final void fillUpdateByTypecodeQuery(PreparedStatement stmt, YItemDeploymentWrapper wrapper, String extension) throws SQLException
    {
        YDeployment depl = wrapper.getOriginal();
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 1, depl.getTableName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 2, depl.getPropsTableName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 3, depl.getName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 4, depl.getPackageName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 5, (depl.getSuperDeployment() == null) ? null :
                        depl.getSuperDeployment().getFullName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 6, extension);
        JDBCValueMappings.getInstance().getValueWriter(Integer.class).setValue(stmt, 7, Integer.valueOf(getModifiers(wrapper)));
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 8, getTypeSystemName());
        JDBCValueMappings.getInstance().getValueWriter(Integer.class).setValue(stmt, 9, Integer.valueOf(depl.getItemTypeCode()));
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 10, getTypeSystemName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 11, depl.getAuditTableName());
    }


    private static final void fillDeleteByNameQuery(PreparedStatement stmt, String name) throws SQLException
    {
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 1, name);
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 2, getTypeSystemName());
    }


    private static final void fillUpdateByNameQuery(PreparedStatement stmt, YItemDeploymentWrapper wrapper, String extension) throws SQLException
    {
        YDeployment depl = wrapper.getOriginal();
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 1, depl.getTableName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 2, depl.getPropsTableName());
        JDBCValueMappings.getInstance().getValueWriter(Integer.class).setValue(stmt, 3, Integer.valueOf(depl.getItemTypeCode()));
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 4, depl.getPackageName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 5, (depl.getSuperDeployment() == null) ? null :
                        depl.getSuperDeployment().getFullName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 6, extension);
        JDBCValueMappings.getInstance().getValueWriter(Integer.class).setValue(stmt, 7, Integer.valueOf(getModifiers(wrapper)));
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 8, getTypeSystemName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 9, depl.getName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 10, getTypeSystemName());
        (JDBCValueMappings.getInstance()).STRING_WRITER.setValue(stmt, 11, depl.getAuditTableName());
    }


    private static final String getSelectByExtensionQuery()
    {
        return "SELECT * FROM " + getTableName() + " WHERE ExtensionName=? and TypeSystemName =?";
    }


    private static final String getSelectAllQuery()
    {
        return "SELECT * FROM " + getTableName() + " WHERE TypeSystemName =?";
    }


    private static final String getSelectByNameQuery()
    {
        return "SELECT Name FROM " + getTableName() + " WHERE Name =? and TypeSystemName =?";
    }


    private static final String getDeleteByNameQuery()
    {
        return "DELETE FROM " + getTableName() + " WHERE Name =? and TypeSystemName =?";
    }


    public static final void loadDeployments(YExtension ext, YTypeSystemLoader loader)
    {
        try
        {
            if(checkIfTestDeploymentExists())
            {
                Object object = new Object(loader);
                object.executeQuery(getSelectByExtensionQuery(), new Object[] {ext
                                .getExtensionName(), getTypeSystemName()});
            }
        }
        catch(DataAccessException e)
        {
            log.error("error reading persisted deployments entries " + e, (Throwable)e);
        }
    }


    public static final void loadDeployments(YTypeSystemLoader loader)
    {
        try
        {
            if(checkIfTestDeploymentExists())
            {
                Object object = new Object(loader);
                object.executeQuery(getSelectAllQuery(), new Object[] {getTypeSystemName()});
            }
        }
        catch(DataAccessException e)
        {
            log.error("error reading persisted deployments entries " + e, (Throwable)e);
        }
    }


    private static boolean checkIfTestDeploymentExists()
    {
        try
        {
            JdbcTemplateExecutor checkTableExistsExecutor = new JdbcTemplateExecutor();
            checkTableExistsExecutor.executeQuery(getSelectByNameQuery(), new Object[] {"test",
                            getTypeSystemName()});
            return true;
        }
        catch(DataAccessException e)
        {
            if(log.isDebugEnabled())
            {
                log.debug("deployment table is not existent: " + e.getMessage());
            }
            return false;
        }
    }


    public static final boolean existsDeployment(ItemDeployment depl)
    {
        if(!(depl instanceof YItemDeploymentWrapper))
        {
            return false;
        }
        try
        {
            if(checkIfTestDeploymentExists())
            {
                Object object = new Object();
                return ((Boolean)object.executeQuery(getSelectByNameQuery(), new Object[] {((YItemDeploymentWrapper)depl)
                                .getOriginal().getName(), getTypeSystemName()})).booleanValue();
            }
        }
        catch(DataAccessException e)
        {
            log.error("error reading persisted deployments entries " + e, (Throwable)e);
        }
        return false;
    }


    public static final boolean deleteDeploymentByName(ItemDeployment depl)
    {
        if(!(depl instanceof YItemDeploymentWrapper))
        {
            return false;
        }
        try
        {
            if(checkIfTestDeploymentExists())
            {
                Object object = new Object(depl);
                int changed = object.executePreparedStatement(getDeleteByNameQuery());
                if(changed > 1)
                {
                    log.warn("Removed more than one (" + changed + ") deployment for name  " + ((YItemDeploymentWrapper)depl)
                                    .getOriginal()
                                    .getName() + " with query " + getDeleteByNameQuery());
                }
                return (changed == 1);
            }
        }
        catch(DataAccessException e)
        {
            log.error("error deleting persisted deployment " + depl.getName(), (Throwable)e);
        }
        return false;
    }


    public static final void insertDeployment(ItemDeployment depl, String extension)
    {
        try
        {
            if(depl instanceof YItemDeploymentWrapper)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Adding deployment " + depl.getName() + " to database");
                }
                if(!existsDeployment(depl))
                {
                    Object object = new Object(depl, extension);
                    int changed = object.executePreparedStatement(getInsertQuery());
                    if(changed != 1)
                    {
                        throw new EJBInternalException(null, "unexpected result count " + changed + " for deployment " + depl + " , query = " +
                                        getInsertQuery(), 0);
                    }
                }
            }
            else
            {
                log.warn("Skip adding deployment " + depl.getName() + " to database");
            }
        }
        catch(DataAccessException e)
        {
            log.error("sql error saving modified deployments , query = " + getInsertQuery() + ", SQLException was: " + e, (Throwable)e);
        }
    }


    public static final void updateDeploymentByTypecode(ItemDeployment newDepl, String extension)
    {
        try
        {
            if(newDepl instanceof YItemDeploymentWrapper)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Updating deployment " + newDepl.getName() + " to database");
                }
                if(checkIfTestDeploymentExists())
                {
                    Object object = new Object(newDepl, extension);
                    int changed = object.executePreparedStatement(getUpdateByTypecodeQuery());
                    if(changed != 1)
                    {
                        throw new EJBInternalException(null, "unexpected result count " + changed + " for deployment " + newDepl + " , query = " +
                                        getUpdateByTypecodeQuery(), 0);
                    }
                }
            }
            else
            {
                log.warn("Skip updating deployment " + newDepl.getName() + " to database");
            }
        }
        catch(DataAccessException e)
        {
            log.error("sql error saving modified deployments , query = " +
                            getUpdateByTypecodeQuery() + ", SQLException was: " + e, (Throwable)e);
        }
    }


    public static final void updateDeploymentByName(ItemDeployment newDepl, String extension)
    {
        try
        {
            if(newDepl instanceof YItemDeploymentWrapper)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Updating deployment " + newDepl.getName() + " to database");
                }
                if(checkIfTestDeploymentExists())
                {
                    Object object = new Object(newDepl, extension);
                    int changed = object.executePreparedStatement(getUpdateByNameQuery());
                    if(changed != 1)
                    {
                        throw new EJBInternalException(null, "unexpected result count " + changed + " for deployment " + newDepl + " , query = " +
                                        getUpdateByNameQuery(), 0);
                    }
                }
            }
            else
            {
                log.warn("Skip updating deployment " + newDepl.getName() + " to database");
            }
        }
        catch(DataAccessException e)
        {
            log.error("sql error saving modified deployments , query = " +
                            getUpdateByNameQuery() + ", SQLException was: " + e, (Throwable)e);
        }
    }


    private static final void readRow(ResultSet resultSet, YTypeSystemLoader loader) throws SQLException
    {
        JDBCValueMappings.ValueReader typeCodeVR = JDBCValueMappings.getInstance().getValueReader(Integer.class);
        JDBCValueMappings.ValueReader<String, ?> tableNameVR = JDBCValueMappings.getInstance().getValueReader(String.class);
        JDBCValueMappings.ValueReader<String, ?> propsTableNameVR = JDBCValueMappings.getInstance().getValueReader(String.class);
        JDBCValueMappings.ValueReader<String, ?> nameVR = JDBCValueMappings.getInstance().getValueReader(String.class);
        JDBCValueMappings.ValueReader<String, ?> packageNameVR = JDBCValueMappings.getInstance().getValueReader(String.class);
        JDBCValueMappings.ValueReader<String, ?> superNameVR = JDBCValueMappings.getInstance().getValueReader(String.class);
        JDBCValueMappings.ValueReader<String, ?> extensionNameVR = JDBCValueMappings.getInstance().getValueReader(String.class);
        JDBCValueMappings.ValueReader modifierVR = JDBCValueMappings.getInstance().getValueReader(Integer.class);
        try
        {
            String extension = (String)extensionNameVR.getValue(resultSet, "ExtensionName");
            int typeCode = typeCodeVR.getInt(resultSet, "Typecode");
            String tableName = (String)tableNameVR.getValue(resultSet, "TableName");
            String propsTabeName = (String)propsTableNameVR.getValue(resultSet, "PropsTableName");
            String name = (String)nameVR.getValue(resultSet, "Name");
            String packageName = (String)packageNameVR.getValue(resultSet, "PackageName");
            String superName = (String)superNameVR.getValue(resultSet, "SuperName");
            int modifiers = modifierVR.getInt(resultSet, "Modifiers");
            boolean isAbstract = isModifierSet(modifiers, (byte)0);
            boolean isGeneric = isModifierSet(modifiers, (byte)1);
            boolean isFinal = isModifierSet(modifiers, (byte)3);
            boolean isNonItem = isModifierSet(modifiers, (byte)4);
            if(log.isDebugEnabled())
            {
                log.debug("Load deployment " + name);
            }
            loader.addExtension(extension, null);
            loader.loadDeployment(new DeploymentDTO(extension, packageName, name, superName, typeCode, isAbstract, isGeneric, isFinal, tableName, propsTabeName, isNonItem));
        }
        catch(SQLException e)
        {
            log.error("error reading entry: " + e.getMessage(), e);
            throw e;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    private static boolean isModifierSet(int modifiers, byte modifier)
    {
        return ((modifiers & 1 << modifier) > 0);
    }


    private static int getModifiers(YItemDeploymentWrapper depl)
    {
        int result = 0;
        if(depl.isAbstract())
        {
            result |= 0x1;
        }
        if(depl.isGeneric())
        {
            result |= 0x2;
        }
        if(depl.isFinal())
        {
            result |= 0x8;
        }
        if(depl.isNonItemDeployment())
        {
            result |= 0x10;
        }
        return result;
    }
}
