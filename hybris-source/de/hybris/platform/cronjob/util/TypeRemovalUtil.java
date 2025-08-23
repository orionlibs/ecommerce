package de.hybris.platform.cronjob.util;

import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.util.RequirementSolver;
import de.hybris.bootstrap.util.RequirementSolverException;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.Trigger;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.typesystem.TypeSystemUtils;
import de.hybris.platform.util.typesystem.YDeploymentJDBC;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.collections.iterators.ReverseListIterator;
import org.apache.log4j.Logger;

public final class TypeRemovalUtil
{
    private static final Logger LOG = Logger.getLogger(TypeRemovalUtil.class);
    static final int REMOVE_CRON_JOB_MAX_WAIT = 10000;


    public static void main(String[] args)
    {
        JaloSession.getCurrentSession();
        removeOrphanedTypes(true, true);
    }


    public static Map<String, String> removeOrphanedTypes(boolean clearInstances, boolean clearDepl)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Executing orphaned types removal [flags - clearInstances: " + clearInstances + ", clearDeployments: " + clearDepl + "]");
        }
        Collection<Type> typesToRemove = getOrphanedTypes();
        LOG.info("Removing orphaned types ..");
        Map<String, String> result = removeTypes(typesToRemove, clearInstances, clearDepl);
        LOG.info("Finished removing of orphaned types.");
        return result;
    }


    public static Map<String, String> removeTypes(Collection<Type> typesToRemove, boolean clearInstances, boolean clearDepl)
    {
        LOG.info("  - Ordering types for removal");
        List<TypeRequirementHolder> holdersToRemove = new LinkedList<>();
        for(Type type : typesToRemove)
        {
            boolean doAdd = true;
            if(type instanceof MapType)
            {
                MapType mType = (MapType)type;
                Set<ComposedType> allCTypes = TypeManager.getInstance().getAllComposedTypes();
                for(ComposedType cType : allCTypes)
                {
                    if(cType instanceof RelationType)
                    {
                        RelationType rType = (RelationType)cType;
                        if(isSourceNavigable(rType) && getSourceAttributeDescriptor(rType).isLocalized() && mType
                                        .getReturnType().equals(getSourceAttributeDescriptor(rType).getAttributeType()))
                        {
                            doAdd = false;
                        }
                        if(isTargetNavigable(rType) && getTargetAttributeDescriptor(rType).isLocalized() && mType
                                        .getReturnType().equals(getTargetAttributeDescriptor(rType).getAttributeType()))
                        {
                            doAdd = false;
                        }
                    }
                }
            }
            else if(type instanceof de.hybris.platform.jalo.type.CollectionType)
            {
                Set<ComposedType> allCTypes = TypeManager.getInstance().getAllComposedTypes();
                for(ComposedType cType : allCTypes)
                {
                    if(cType instanceof RelationType)
                    {
                        RelationType rType = (RelationType)cType;
                        if(isSourceNavigable(rType) && type.equals(getSourceAttributeDescriptor(rType).getAttributeType()))
                        {
                            doAdd = false;
                        }
                        if(isTargetNavigable(rType) && type.equals(getTargetAttributeDescriptor(rType).getAttributeType()))
                        {
                            doAdd = false;
                        }
                    }
                }
            }
            if(doAdd)
            {
                holdersToRemove.add(new TypeRequirementHolder(type, typesToRemove));
            }
        }
        try
        {
            holdersToRemove = RequirementSolver.solve(holdersToRemove);
        }
        catch(RequirementSolverException e)
        {
            LOG.error("Error while determining order of types for removal", (Throwable)e);
            return Collections.EMPTY_MAP;
        }
        Map<String, String> result = new LinkedHashMap<>();
        try
        {
            JaloSession.getCurrentSession().createLocalSessionContext();
            JaloSession.getCurrentSession()
                            .getSessionContext()
                            .setAttribute("disableItemCheckBeforeRemovable", Boolean.TRUE);
            LOG.info("  - Starting removal of types");
            ReverseListIterator iter;
            for(iter = new ReverseListIterator(holdersToRemove); iter.hasNext(); )
            {
                TypeRequirementHolder req = (TypeRequirementHolder)iter.next();
                Type type = req.getType();
                String typeToString = type.getCode() + " [" + type.getCode() + "]";
                if(clearInstances && type instanceof ComposedType)
                {
                    removeRelatedCronJobInstances((ComposedType)type, result);
                    removeRelatedTriggerInstances((ComposedType)type, result);
                }
                result.put(typeToString, removeType(type, clearInstances, clearDepl, Boolean.TRUE));
            }
            for(iter = new ReverseListIterator(holdersToRemove); iter.hasNext(); )
            {
                TypeRequirementHolder req = (TypeRequirementHolder)iter.next();
                Type type = req.getType();
                String typeToString = type.getCode() + " [" + type.getCode() + "]";
                result.put(typeToString, removeType(type, clearInstances, clearDepl, Boolean.FALSE));
            }
            LOG.info("  - Starting removal of orphaned attributes");
            List<AttributeDescriptor> descs = FlexibleSearch.getInstance().search("SELECT {pk},{qualifier} FROM {AttributeDescriptor as a} WHERE SuperAttributeDescriptorPK IS NULL AND {a:attributeType} NOT IN ({{SELECT {pk} FROM {Type}}})", AttributeDescriptor.class).getResult();
            for(AttributeDescriptor descr : descs)
            {
                LOG.info("    - Removing attribute " + descr.getQualifier());
                try
                {
                    descr.remove();
                }
                catch(Exception e)
                {
                    LOG.error("Error while removing attribute", e);
                }
            }
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
        LOG.info("  - Restart registry");
        Registry.destroyAndForceStartup();
        return result;
    }


    protected static boolean isSourceNavigable(RelationType relationType)
    {
        return (getSourceAttributeDescriptor(relationType) != null);
    }


    protected static AttributeDescriptor getSourceAttributeDescriptor(RelationType relationType)
    {
        return (AttributeDescriptor)relationType.getProperty("sourceAttribute");
    }


    protected static ComposedType getSourceType(RelationType relationType)
    {
        ComposedType result = (ComposedType)relationType.getProperty("sourceType");
        if(result == null)
        {
            AttributeDescriptor desc = getSourceAttributeDescriptor(relationType);
            if(desc != null)
            {
                result = desc.getEnclosingType();
            }
        }
        return result;
    }


    protected static boolean isTargetNavigable(RelationType relationType)
    {
        return (getTargetAttributeDescriptor(relationType) != null);
    }


    protected static AttributeDescriptor getTargetAttributeDescriptor(RelationType relationType)
    {
        return (AttributeDescriptor)relationType.getProperty("targetAttribute");
    }


    protected static ComposedType getTargetType(RelationType relationType)
    {
        ComposedType result = (ComposedType)relationType.getProperty("targetType");
        if(result == null)
        {
            AttributeDescriptor desc = getTargetAttributeDescriptor(relationType);
            if(desc != null)
            {
                result = desc.getEnclosingType();
            }
        }
        return result;
    }


    private static int getRemovalTimeout()
    {
        return Registry.getMasterTenant().getConfig().getInt("remove.cronjob.maxwait", 10000);
    }


    public static List<CronJob> getRelatedCronJobInstances(ComposedType removedJobType)
    {
        SearchResult<CronJob> cronJobResult = FlexibleSearch.getInstance()
                        .search(" select  {cj." + Item.PK + "} from {" + GeneratedCronJobConstants.TC.CRONJOB + "  as  cj }, { " + GeneratedCronJobConstants.TC.JOB + " as j } where {cj.job} = {j." + Item.PK + "} and {j." + CronJob.TYPE + "} = ?jobType ",
                                        Collections.singletonMap("jobType", removedJobType.getPK()), Collections.singletonList(CronJob.class), true, true, 0, -1);
        return cronJobResult.getResult();
    }


    public static List<Trigger> getRelatedTriggersInstances(ComposedType removedJobType)
    {
        try
        {
            ComposedType triggerType = TypeManager.getInstance().getComposedType(Trigger.class);
            triggerType.getAttributeDescriptor("job");
            SearchResult<Trigger> triggersResult = FlexibleSearch.getInstance()
                            .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCronJobConstants.TC.TRIGGER + " as t}, {" + GeneratedCronJobConstants.TC.JOB + " as j} WHERE {t.job} = {j." + Item.PK + "} AND {t.cronJob} IS NULL and {j." + CronJob.TYPE + "} = ?jobType ",
                                            Collections.singletonMap("jobType", removedJobType.getPK()), Collections.singletonList(Trigger.class), true, true, 0, -1);
            return triggersResult.getResult();
        }
        catch(JaloItemNotFoundException e)
        {
            return Collections.EMPTY_LIST;
        }
    }


    private static void removeRelatedCronJobInstances(ComposedType removedJobType, Map<String, String> result)
    {
        List<CronJob> cjobInstances = getRelatedCronJobInstances(removedJobType);
        if(!cjobInstances.isEmpty())
        {
            LOG.info("    - Removing cronjob instance for already removed job type" + removedJobType.getCode());
            for(CronJob cJobInst : cjobInstances)
            {
                String code = cJobInst.getCode();
                String pk = cJobInst.getPK().getLongValueAsString();
                String message = String.format("    - Removed cronjob instance %s [%s]", new Object[] {cJobInst.getCode(), cJobInst.getPK()
                                .getLongValueAsString()});
                try
                {
                    long start = System.currentTimeMillis();
                    while(cJobInst.isRunning() && start + getRemovalTimeout() > System.currentTimeMillis())
                    {
                        try
                        {
                            Thread.sleep(1000L);
                        }
                        catch(InterruptedException e)
                        {
                            LOG.warn(" Removing cronjob instance " + cJobInst.getCode() + " [" + cJobInst.getPK() + "] interrupted while waiting for cronjob stop.");
                        }
                        start = System.currentTimeMillis();
                    }
                    if(!cJobInst.isRunning())
                    {
                        cJobInst.remove();
                        result.put(message, null);
                    }
                    else
                    {
                        result.put(removedJobType.getCode() + " [" + removedJobType.getCode() + "]", "    - Unable to remove cronjob instance " + code + " [" + pk + "], cronjob is still running ");
                    }
                }
                catch(ConsistencyCheckException e)
                {
                    message = String.format("    - Unable to remove cronjob instance %s [%s] ,cause %s", new Object[] {code, pk, e
                                    .getMessage()});
                    result.put(removedJobType.getCode() + " [" + removedJobType.getCode() + "]", message);
                }
                finally
                {
                    LOG.info(message);
                }
            }
        }
    }


    private static void removeRelatedTriggerInstances(ComposedType removedJobType, Map<String, String> result)
    {
        List<Trigger> trInstances = getRelatedTriggersInstances(removedJobType);
        if(!trInstances.isEmpty())
        {
            LOG.info("    - Removing trigger instance for already removed job type" + removedJobType.getCode());
            for(Trigger trigInst : trInstances)
            {
                String pk = trigInst.getPK().getLongValueAsString();
                String message = String.format("    - Removed trigger instance [%s]", new Object[] {trigInst.getPK().getLongValueAsString()});
                try
                {
                    trigInst.remove();
                    result.put(message, null);
                }
                catch(ConsistencyCheckException e)
                {
                    message = String.format("    - Unable to remove trigger instance [%s] ,cause %s", new Object[] {pk, e.getMessage()});
                    result.put(removedJobType.getCode() + " [" + removedJobType.getCode() + "]", message);
                }
                finally
                {
                    LOG.info(message);
                }
            }
        }
    }


    public static String removeType(Type type, boolean clearInstances, boolean clearDepl)
    {
        return removeType(type, clearInstances, clearDepl, null);
    }


    private static String removeType(Type type, boolean clearInstances, boolean clearDepl, Boolean firstPass)
    {
        String typeToString = type.getCode() + " [" + type.getCode() + "]";
        LOG.info("    - Removing " + typeToString);
        try
        {
            if(firstPass == null || firstPass.booleanValue())
            {
                if(clearInstances && type instanceof ComposedType)
                {
                    ComposedType cType = (ComposedType)type;
                    if(!cType.isJaloOnly() && !cType.isAbstract())
                    {
                        LOG.info("        - Removing instances");
                        for(Item instance : cType.getAllInstances())
                        {
                            instance.remove();
                        }
                    }
                }
            }
            if(firstPass == null || !firstPass.booleanValue())
            {
                ItemDeployment depl = null;
                if(clearDepl && type instanceof ComposedType)
                {
                    ComposedType cType = (ComposedType)type;
                    ItemDeployment tempDepl = Registry.getPersistenceManager().getItemDeployment(cType.getItemTypeCode());
                    if(tempDepl != null && YDeploymentJDBC.existsDeployment(tempDepl))
                    {
                        boolean hasOwnDeployemnt = (cType.getSuperType() == null || cType.getSuperType().getItemTypeCode() != tempDepl.getTypeCode());
                        if(hasOwnDeployemnt)
                        {
                            depl = tempDepl;
                        }
                    }
                }
                if(type instanceof de.hybris.platform.jalo.type.CollectionType && !type.isAlive())
                {
                    LOG.info("        - Skipping " + typeToString + ", seems to be a relation collection already deleted.");
                }
                else
                {
                    LOG.info("        - Removing type");
                    type.remove();
                }
                if(clearDepl)
                {
                    if(depl != null)
                    {
                        LOG.info("        - Removing deployment " + depl.getName());
                        YDeploymentJDBC.deleteDeploymentByName(depl);
                    }
                }
            }
        }
        catch(Exception e)
        {
            LOG.error("Error while removing type", e);
            return e.getMessage();
        }
        return null;
    }


    private static boolean isOrphanedType(Type type, YTypeSystem definedSystem)
    {
        boolean existsAtItemsXml = (definedSystem.getType(type.getCode()) != null);
        if(type instanceof ComposedType)
        {
            ComposedType cType = (ComposedType)type;
            boolean hasJaloClass = (cType.getDeclaredJaloClass() != null);
            boolean isSpecialClass = (cType instanceof RelationType || cType instanceof de.hybris.platform.jalo.enumeration.EnumerationType);
            return (!existsAtItemsXml && (!hasJaloClass || isSpecialClass));
        }
        return !existsAtItemsXml;
    }


    public static Set<Type> getOrphanedTypes()
    {
        LOG.info("Checking for orphaned types ..");
        LOG.info("  - Loading types from items.xml");
        YTypeSystem definedSystem = TypeSystemUtils.loadViaClassLoader(Registry.getMasterTenant()
                        .getTenantSpecificExtensionNames());
        LOG.info("  - Loading types from system");
        Set<Type> types = TypeManager.getInstance().getAllComposedTypes();
        types.addAll(TypeManager.getInstance().getAllCollectionTypes());
        types.addAll(TypeManager.getInstance().getAllMapTypes());
        types.addAll(TypeManager.getInstance().getAllAtomicTypes());
        LOG.info("  - Determining orphaned types");
        Set<Type> result = new LinkedHashSet<>();
        for(Type type : types)
        {
            if(isOrphanedType(type, definedSystem))
            {
                result.add(type);
            }
        }
        LOG.info("Finished checking for orphaned types");
        return result;
    }


    public static Map<String, Set<Type>> getOrphanedTypesPerExtension()
    {
        Set<Type> types = getOrphanedTypes();
        Map<String, Set<Type>> result = new HashMap<>();
        for(Type type : types)
        {
            String extension = type.getExtensionName();
            Set<Type> setPerExtension = result.get(extension);
            if(setPerExtension == null)
            {
                setPerExtension = new TreeSet<>((Comparator<? super Type>)new Object());
                result.put(extension, setPerExtension);
            }
            setPerExtension.add(type);
        }
        return result;
    }
}
