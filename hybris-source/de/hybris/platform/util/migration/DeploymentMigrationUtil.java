package de.hybris.platform.util.migration;

import de.hybris.platform.core.DeploymentImpl;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.property.DBPersistenceManager;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.HierarchieTypeRemote;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.typesystem.TypeSystemUtils;
import de.hybris.platform.util.typesystem.YDeploymentJDBC;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public final class DeploymentMigrationUtil
{
    private static final Logger LOG = Logger.getLogger(DeploymentMigrationUtil.class);


    public static void migrateDeployments(String migrationExtensionName)
    {
        LOG.info(" ");
        LOG.info("*****************************************************");
        LOG.info("* Start migrating deployments of extension " + migrationExtensionName);
        try
        {
            boolean deploymentsUpdated = false;
            LOG.info("*  Loading new deployments");
            DeploymentImpl depls = TypeSystemUtils.loadDeploymentsForMigration(migrationExtensionName, true, false);
            LOG.info("*  Updating deployments");
            for(String deplName : depls.getBeanIDs())
            {
                ItemDeployment newDepl = depls.getItemDeployment(deplName);
                ItemDeployment oldDepl = Registry.getPersistenceManager().getItemDeployment(newDepl.getTypeCode());
                if(needsUpdate(oldDepl, newDepl))
                {
                    LOG.info("*    Updating deployment " + oldDepl);
                    if(deploymentNameHasChanged(oldDepl, newDepl))
                    {
                        LOG.info("*      An system update is needed with deployment.check set to false");
                    }
                    YDeploymentJDBC.updateDeploymentByTypecode(newDepl, migrationExtensionName);
                    deploymentsUpdated = true;
                }
                if(tableNameHasChanged(oldDepl, newDepl))
                {
                    LOG.info("*    Renaming table " + oldDepl.getDatabaseTableName() + " to " + newDepl.getDatabaseTableName());
                    renameTable(oldDepl.getDatabaseTableName(), newDepl.getDatabaseTableName());
                }
            }
            if(deploymentsUpdated)
            {
                LOG.info("*  Reloading deployments (Errors concerning the updated deployments can be ignored)");
                ((DBPersistenceManager)Registry.getPersistenceManager()).reloadPersistenceInfos();
            }
        }
        catch(Exception e)
        {
            LOG.error("Error while migrating deployments of extension " + migrationExtensionName, e);
        }
        LOG.info("* Finished migrating deployments of extension " + migrationExtensionName);
        LOG.info("*****************************************************");
        LOG.info(" ");
    }


    public static void migrateGeneralizedDuplicatedDeployments(String migrationExtensionName, String... deploymentNames)
    {
        LOG.info(" ");
        LOG.info("*****************************************************");
        LOG.info("* Start migrating duplicated deployments of extension " + migrationExtensionName + " for deployment names: " +
                        StringUtils.join((Object[])deploymentNames, ","));
        try
        {
            if(deploymentNames == null || deploymentNames.length == 0)
            {
                LOG.info("* There is no given deployment names to migrate, aborting.");
                return;
            }
            Map<String, ComposedType> composedTypeMapping = new HashMap<>();
            for(String deploymentName : deploymentNames)
            {
                String typeName = getTypeCodeForDeployment(deploymentName);
                try
                {
                    composedTypeMapping.put(deploymentName, TypeManager.getInstance().getComposedType(typeName));
                }
                catch(JaloItemNotFoundException jinfe)
                {
                    LOG.info("* Given type " + typeName + " can not be found, this type will not be migrated");
                }
            }
            boolean deploymentsUpdated = false;
            LOG.info("*  Loading duplicated deployments");
            Map<Integer, ItemDeployment> duplicatedDeploymentsMap = Registry.getPersistenceManager().getDuplicatedItemDeployments();
            LOG.info("*  Updating deployments");
            for(Map.Entry<String, ComposedType> mapTypeEntry : composedTypeMapping.entrySet())
            {
                Integer typeCode = Integer.valueOf(((ComposedType)mapTypeEntry.getValue()).getItemTypeCode());
                ComposedType ctype = mapTypeEntry.getValue();
                ItemDeployment oldDepl = duplicatedDeploymentsMap.get(typeCode);
                if(oldDepl != null)
                {
                    if(!composedTypeMapping.keySet().contains(oldDepl.getName()))
                    {
                        LOG.info("*    Found duplicated deployment " + oldDepl + " is not matching any of given " +
                                        StringUtils.join((Object[])deploymentNames, ",") + ", will be omited.");
                        continue;
                    }
                    ItemDeployment newDepl = Registry.getPersistenceManager().getItemDeployment(typeCode.intValue());
                    if(needsUpdate(oldDepl, newDepl))
                    {
                        LOG.info("*    Updating deployment " + oldDepl);
                        YDeploymentJDBC.updateDeploymentByTypecode(newDepl, migrationExtensionName);
                        deploymentsUpdated = true;
                    }
                    ComposedTypeRemote composedType = null;
                    if((composedType = getComposedType(ctype)) != null)
                    {
                        recreateComposedType(composedType);
                    }
                    if(isManyToManyRelationType(ctype))
                    {
                        recreateRelation(ctype, newDepl);
                    }
                }
            }
            if(deploymentsUpdated)
            {
                LOG.info("*  Reloading deployments (Errors concerning the updated deployments can be ignored)");
                ((DBPersistenceManager)Registry.getPersistenceManager()).reloadPersistenceInfos();
            }
        }
        catch(Exception e)
        {
            LOG.error("Error while migrating deployments of extension " + migrationExtensionName, e);
        }
        LOG.info("* Finished migrating duplicated deployments of extension " + migrationExtensionName);
        LOG.info("*****************************************************");
        LOG.info(" ");
    }


    private static void recreateComposedType(ComposedTypeRemote composedType)
    {
        LOG.info("*    Cleaning item deployment name for " + composedType.getCode());
        composedType.reinitializeType(composedType.getSuperType(), null, composedType.getJaloClassName(), null);
    }


    private static void recreateRelation(ComposedType ctype, ItemDeployment newDepl) throws JaloItemNotFoundException
    {
        LOG.info("*    Given composed type " + ctype.getCode() + " seems to be a many to many relation type, recreating it using new deployment");
        ComposedTypeRemote relationMetaData = getAsRemote(TypeManager.getInstance().getComposedType("RelationMetaType"));
        ComposedTypeRemote relationGenericSuperType = getAsRemote(TypeManager.getInstance().getRootComposedType(7));
        ComposedTypeRemote relationComposedType = (ComposedTypeRemote)EJBTools.instantiatePK(ctype.getPK());
        relationComposedType.reinitializeType(relationGenericSuperType, newDepl, null, relationMetaData);
        LOG.info("*    Given composed type " + ctype.getCode() + " has been recreated successfully.");
    }


    private static String getTypeCodeForDeployment(String givenDeploymentName)
    {
        return givenDeploymentName.split("_")[1];
    }


    private static <T> T getAsRemote(ComposedType ctype)
    {
        return (T)EJBTools.instantiatePK(ctype.getPK());
    }


    private static boolean isManyToManyRelationType(ComposedType ctype) throws Exception
    {
        ItemRemote relationItemType = EJBTools.instantiatePK(ctype.getPK());
        if(relationItemType instanceof ComposedTypeRemote && isRelationType((ComposedTypeRemote)relationItemType) &&
                        !ctype.isAbstract())
        {
            return true;
        }
        return false;
    }


    private static boolean isRelationType(ComposedTypeRemote composedType)
    {
        TypeManagerEJB typeManagerEjb = SystemEJB.getInstance().getTypeManager();
        return typeManagerEjb.isRelationType(composedType);
    }


    private static ComposedTypeRemote getComposedType(ComposedType ctype) throws Exception
    {
        ItemRemote composedType = EJBTools.instantiatePK(ctype.getPK());
        if(composedType instanceof ComposedTypeRemote && !isRelationType((ComposedTypeRemote)composedType))
        {
            return (ComposedTypeRemote)composedType;
        }
        return null;
    }


    public static void migrateDeploymentManually(int typecode, String oldTableName)
    {
        ItemDeployment depl = Registry.getPersistenceManager().getItemDeployment(typecode);
        if(depl != null && !YDeploymentJDBC.existsDeployment(depl))
        {
            LOG.info("Renaming table " + oldTableName + " to " + depl.getDatabaseTableName());
            renameTable(oldTableName, depl.getDatabaseTableName());
        }
    }


    private static boolean needsUpdate(ItemDeployment oldDepl, ItemDeployment newDepl)
    {
        if(newDepl == null || oldDepl == null)
        {
            return false;
        }
        if(deploymentNameHasChanged(oldDepl, newDepl))
        {
            return true;
        }
        if(tableNameHasChanged(oldDepl, newDepl))
        {
            return true;
        }
        return false;
    }


    private static boolean deploymentNameHasChanged(ItemDeployment oldDepl, ItemDeployment newDepl)
    {
        if(newDepl == null || oldDepl == null)
        {
            return false;
        }
        if(oldDepl.getName() != null && !oldDepl.getName().equalsIgnoreCase(newDepl.getName()))
        {
            return true;
        }
        return false;
    }


    private static boolean tableNameHasChanged(ItemDeployment oldDepl, ItemDeployment newDepl)
    {
        if(newDepl == null || oldDepl == null)
        {
            return false;
        }
        if(oldDepl.getDatabaseTableName() != null &&
                        !oldDepl.getDatabaseTableName().equalsIgnoreCase(newDepl.getDatabaseTableName()))
        {
            return true;
        }
        return false;
    }


    private static void renameTable(String oldName, String newName)
    {
        StringBuilder query = null;
        try
        {
            query = new StringBuilder();
            if(Config.getDatabase().equalsIgnoreCase("sqlserver"))
            {
                query.append("EXEC sp_rename " + oldName + ", " + newName);
            }
            else
            {
                query.append("ALTER TABLE " + oldName + " RENAME TO " + newName);
            }
            JdbcTemplate alterTemplate = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
            alterTemplate.execute(query.toString());
        }
        catch(DataAccessException e)
        {
            LOG.error("Error while renaming table, query was " + query + ", SQLException was: " + e);
        }
    }


    private static boolean hasAddMissingYDeploymentEntry(ItemDeployment oldDepl, ComposedTypeRemote type, DeploymentImpl depls)
    {
        if(oldDepl == null)
        {
            ItemDeployment newDepl = getDeployment(depls, type.getItemTypeCode());
            boolean isDeployment = YDeploymentJDBC.existsDeployment(newDepl);
            return !isDeployment;
        }
        return false;
    }


    private static TypeManagerEJB getTypeManagerEJB()
    {
        TypeManager localTypeManager = new TypeManager();
        localTypeManager.init();
        return (TypeManagerEJB)localTypeManager.getRemote();
    }


    public static void migrateSelectedDeployments(String migrationExtensionName, String... typeCodes)
    {
        try
        {
            DeploymentImpl depls = TypeSystemUtils.loadDeploymentsForMigration(migrationExtensionName, true, false);
            TypeManagerEJB localTypeManagerEJB = getTypeManagerEJB();
            for(String typeCode : typeCodes)
            {
                ComposedTypeRemote type = null;
                try
                {
                    type = localTypeManagerEJB.getComposedType(typeCode);
                }
                catch(EJBItemNotFoundException eJBItemNotFoundException)
                {
                }
                catch(JaloItemNotFoundException jaloItemNotFoundException)
                {
                }
                catch(JaloSystemException jaloSystemException)
                {
                }
                if(type != null)
                {
                    ItemDeployment oldDepl = Registry.getPersistenceManager().getItemDeployment(type.getItemJNDIName());
                    boolean missingYDeplEntry = hasAddMissingYDeploymentEntry(oldDepl, type, depls);
                    if(oldDepl != null || missingYDeplEntry)
                    {
                        ItemDeployment newDepl = getDeployment(depls, type.getItemTypeCode());
                        if(missingYDeplEntry || (newDepl != null && oldDepl.getTypeCode() != 0))
                        {
                            if(missingYDeplEntry || (oldDepl.getName() != null &&
                                            !oldDepl.getName().equalsIgnoreCase(newDepl.getName())))
                            {
                                if(missingYDeplEntry)
                                {
                                    LOG.info("*    Inserting new deployment " + newDepl);
                                    YDeploymentJDBC.insertDeployment(newDepl, migrationExtensionName);
                                }
                                else
                                {
                                    LOG.info("*    Updating deployment " + oldDepl + " to " + newDepl);
                                    YDeploymentJDBC.updateDeploymentByTypecode(newDepl, migrationExtensionName);
                                }
                                LOG.info("*    Updating deployment for " + type.getCode());
                                updateJndiName(type, newDepl);
                                for(Object subTypeObject : localTypeManagerEJB.getAllSubTypes((HierarchieTypeRemote)type))
                                {
                                    ComposedTypeRemote subType = (ComposedTypeRemote)subTypeObject;
                                    if(subType.getItemTypeCode() == type.getItemTypeCode())
                                    {
                                        updateJndiName(subType, newDepl);
                                    }
                                }
                                LOG.info("*    Updating deployment for: " + type.getCode() + " done.");
                            }
                        }
                    }
                }
            }
            ((DBPersistenceManager)Registry.getPersistenceManager()).reloadPersistenceInfos();
        }
        catch(Exception e)
        {
            LOG.error("Error while migrating deployments of extension " + migrationExtensionName, e);
        }
    }


    private static void updateJndiName(ComposedTypeRemote ejb, ItemDeployment newDepl) throws Exception
    {
        ejb.reinitializeType(ejb.getSuperType(), newDepl, ejb.getJaloClassName(), ejb.getComposedType());
    }


    private static ItemDeployment getDeployment(DeploymentImpl depls, int typeCode)
    {
        for(String deplName : depls.getBeanIDs())
        {
            ItemDeployment depl = depls.getItemDeployment(deplName);
            if(depl.getTypeCode() == typeCode)
            {
                return depl;
            }
        }
        return null;
    }
}
