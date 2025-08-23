package de.hybris.platform.patches.aop;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.patches.actions.PatchAction;
import de.hybris.platform.patches.actions.data.PatchActionData;
import de.hybris.platform.patches.data.ImpexHeaderOption;
import de.hybris.platform.patches.data.ImpexImportUnit;
import de.hybris.platform.patches.data.ImpexImportUnitResult;
import de.hybris.platform.patches.enums.ExecutionStatus;
import de.hybris.platform.patches.enums.ExecutionUnitType;
import de.hybris.platform.patches.exceptions.PatchActionException;
import de.hybris.platform.patches.exceptions.PatchImportException;
import de.hybris.platform.patches.internal.logger.PatchLogger;
import de.hybris.platform.patches.internal.logger.PatchLoggerFactory;
import de.hybris.platform.patches.model.PatchExecutionModel;
import de.hybris.platform.patches.model.PatchExecutionUnitModel;
import de.hybris.platform.patches.service.PatchExecutionService;
import de.hybris.platform.patches.service.PatchImportService;
import de.hybris.platform.patches.strategies.PatchExecutionUnitTrackingStrategy;
import de.hybris.platform.patches.utils.PatchExecutionUnitUtils;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Required;

@Aspect
public class PatchExecutionUnitAspect
{
    private static final PatchLogger LOG = PatchLoggerFactory.getLogger(PatchExecutionUnitAspect.class);
    private static final String OPTIONS_DELIMITER = ":::";
    private TimeService timeService;
    private ModelService modelService;
    private PatchExecutionService patchExecutionService;
    private PatchImportService patchImportService;
    private Collection<String> ownAspectsList;
    private PatchExecutionUnitTrackingStrategy defaultPatchExecutionUnitTrackingStrategy;
    private Map<String, PatchExecutionUnitTrackingStrategy> patchExecutionUnitTrackingStrategyMap;


    @Around("execution(* de.hybris.platform.patches.service.impl.DefaultPatchImportService.importImpexUnit(..))")
    public ImpexImportUnitResult handleImportPatch(ProceedingJoinPoint joinPoint) throws Throwable
    {
        PatchExecutionUnitModel patchExecutionUnit;
        ImpexImportUnit impexImportUnit = (ImpexImportUnit)joinPoint.getArgs()[0];
        try
        {
            patchExecutionUnit = handleBeforeImportPatch(impexImportUnit);
        }
        catch(IOException ioex)
        {
            throw new PatchImportException(ioex);
        }
        try
        {
            ImpexImportUnitResult result = (ImpexImportUnitResult)joinPoint.proceed();
            handleAfterImportPatch(patchExecutionUnit, result);
            return result;
        }
        catch(PatchImportException ex)
        {
            patchExecutionUnit.setExecutionStatus(ExecutionStatus.ERROR);
            patchExecutionUnit.setErrorLog(ex.getMessage());
            patchExecutionUnit.setExecutionTime(this.timeService.getCurrentTime());
            this.modelService.save(patchExecutionUnit);
            throw ex;
        }
    }


    @Around("execution(* de.hybris.platform.patches.actions.PatchAction.perform(..))")
    public void handlePerformActionPatch(ProceedingJoinPoint joinPoint) throws Throwable
    {
        String targetClassName = joinPoint.getSourceLocation().getWithinType().getName();
        String simpleClassName = joinPoint.getSourceLocation().getWithinType().getSimpleName();
        PatchActionData patchActionData = (PatchActionData)joinPoint.getArgs()[0];
        PatchAction actionToBePerformed = (PatchAction)joinPoint.getTarget();
        String customizedActionName = actionToBePerformed.getCustomizedName(patchActionData);
        patchActionData.setName(customizedActionName);
        PatchExecutionUnitModel patchExecutionUnit = null;
        PatchExecutionUnitTrackingStrategy patchExecutionUnitTrackingStrategy = null;
        PatchActionException trackedException = null;
        try
        {
            if(!hasOwnAspectsList(targetClassName))
            {
                LOG.info(PatchLogger.LoggingMode.HAC_CONSOLE, "Executing {}: {}", simpleClassName, patchActionData.getName());
                patchExecutionUnitTrackingStrategy = getPatchExecutionUnitTrackingStrategy(targetClassName);
                patchExecutionUnit = patchExecutionUnitTrackingStrategy.trackBeforePerform(patchActionData);
            }
            joinPoint.proceed();
        }
        catch(PatchActionException ex)
        {
            LOG.error(PatchLogger.LoggingMode.HAC_CONSOLE, "Executing " + simpleClassName + ": " + patchActionData.getName() + " failed", (Throwable)ex);
            trackedException = ex;
        }
        if(patchExecutionUnitTrackingStrategy != null)
        {
            patchExecutionUnitTrackingStrategy.trackAfterPerform(patchExecutionUnit, patchActionData, (Throwable)trackedException);
        }
    }


    private PatchExecutionUnitTrackingStrategy getPatchExecutionUnitTrackingStrategy(String className)
    {
        PatchExecutionUnitTrackingStrategy patchExecutionUnitTrackingStrategy = this.patchExecutionUnitTrackingStrategyMap.get(className);
        if(patchExecutionUnitTrackingStrategy != null)
        {
            return patchExecutionUnitTrackingStrategy;
        }
        return this.defaultPatchExecutionUnitTrackingStrategy;
    }


    private boolean hasOwnAspectsList(String className)
    {
        return this.ownAspectsList.contains(className);
    }


    private PatchExecutionUnitModel handleBeforeImportPatch(ImpexImportUnit impexImportUnit) throws IOException
    {
        PatchExecutionModel patchExecution = this.patchExecutionService.getSessionPatchExecution();
        String name = buildIdFromImpexImportUnit(impexImportUnit);
        if(patchExecution != null)
        {
            PatchExecutionUnitModel patchExecutionUnit = new PatchExecutionUnitModel();
            patchExecutionUnit.setPatch(patchExecution);
            String idHash = PatchExecutionUnitUtils.generateHashId(name, impexImportUnit
                            .getOrganisationUnitRepresentation(), patchExecution);
            patchExecutionUnit.setIdHash(idHash);
            patchExecutionUnit.setName(name);
            patchExecutionUnit.setOrderNumber(Integer.valueOf(PatchExecutionUnitUtils.getNextUnitNumber(patchExecution)));
            patchExecutionUnit.setOrganisation(impexImportUnit.getOrganisationUnitRepresentation());
            patchExecutionUnit.setExecutionType(ExecutionUnitType.IMPEX_IMPORT);
            try
            {
                String contentHash = buildImpexContentHash(impexImportUnit);
                patchExecutionUnit.setContentHash(contentHash);
            }
            catch(IOException ex)
            {
                patchExecutionUnit.setErrorLog("Problem while generating content hash for item " + name + ".\n" + ex
                                .getMessage());
                patchExecutionUnit.setExecutionTime(this.timeService.getCurrentTime());
                patchExecutionUnit.setExecutionStatus(ExecutionStatus.ERROR);
                throw ex;
            }
            finally
            {
                this.modelService.save(patchExecutionUnit);
            }
            return patchExecutionUnit;
        }
        throw new IllegalStateException("PatchExecution not found in jalo session.");
    }


    private void handleAfterImportPatch(PatchExecutionUnitModel patchExecutionUnit, ImpexImportUnitResult result)
    {
        patchExecutionUnit.setExecutionStatus(ExecutionStatus.ERROR);
        if(result != null && result.getImportResult() != null)
        {
            ImportResult importResult = result.getImportResult();
            if(importResult.isSuccessful())
            {
                patchExecutionUnit.setExecutionStatus(ExecutionStatus.SUCCESS);
            }
            else
            {
                patchExecutionUnit.setErrorLog(importResult.getUnresolvedLines().getPreview());
            }
            if(importResult.getCronJob() != null)
            {
                patchExecutionUnit.setCronjob((CronJobModel)importResult.getCronJob());
            }
        }
        patchExecutionUnit.setExecutionTime(this.timeService.getCurrentTime());
        this.modelService.save(patchExecutionUnit);
    }


    private String buildImpexContentHash(ImpexImportUnit impexImportUnit) throws IOException
    {
        String contentHash = "";
        InputStream stream = this.patchImportService.getStreamForImpexImportUnit(impexImportUnit);
        if(stream != null)
        {
            contentHash = DigestUtils.md5Hex(stream);
            stream.close();
        }
        return contentHash;
    }


    private String buildIdFromImpexImportUnit(ImpexImportUnit impexImportUnit)
    {
        StringBuilder sb = new StringBuilder(impexImportUnit.getImpexDataFile().getFilePath());
        ImpexHeaderOption[] impexHeaderOptions = impexImportUnit.getImpexHeaderOptions();
        if(impexHeaderOptions != null)
        {
            for(ImpexHeaderOption option : impexHeaderOptions)
            {
                sb.append(":::");
                sb.append(option.getMacro());
            }
        }
        return sb.toString();
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setPatchExecutionService(PatchExecutionService patchExecutionService)
    {
        this.patchExecutionService = patchExecutionService;
    }


    @Required
    public void setPatchImportService(PatchImportService patchImportService)
    {
        this.patchImportService = patchImportService;
    }


    @Required
    public void setOwnAspectsList(Collection<String> ownAspectsList)
    {
        this.ownAspectsList = ownAspectsList;
    }


    @Required
    public void setDefaultPatchExecutionUnitTrackingStrategy(PatchExecutionUnitTrackingStrategy defaultPatchExecutionUnitTrackingStrategy)
    {
        this.defaultPatchExecutionUnitTrackingStrategy = defaultPatchExecutionUnitTrackingStrategy;
    }


    @Required
    public void setPatchExecutionUnitTrackingStrategyMap(Map<String, PatchExecutionUnitTrackingStrategy> patchExecutionUnitTrackingStrategyMap)
    {
        this.patchExecutionUnitTrackingStrategyMap = patchExecutionUnitTrackingStrategyMap;
    }
}
