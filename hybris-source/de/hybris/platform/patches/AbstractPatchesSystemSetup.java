package de.hybris.platform.patches;

import com.google.common.base.Joiner;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.patches.internal.JspContextHolder;
import de.hybris.platform.patches.internal.logger.PatchLogger;
import de.hybris.platform.patches.internal.logger.PatchLoggerFactory;
import de.hybris.platform.patches.model.PatchExecutionModel;
import de.hybris.platform.patches.organisation.StructureState;
import de.hybris.platform.patches.service.PatchExecutionService;
import de.hybris.platform.patches.utils.StructureStateUtils;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SystemSetupUtils;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractPatchesSystemSetup
{
    private static final PatchLogger LOG = PatchLoggerFactory.getLogger(AbstractPatchesSystemSetup.class);
    private static final String PATCHES_PREFIX_PROPERTY_KEY = "patches";
    private static final String HIDE_POSTFIX_PROPERTY_KEY = "hidden";
    private static final String HIDE_ALL_POSTFIX_PROPERTY_KEY = "hideAll";
    private static final String NOT_SELECTED_POSTFIX_PROPERTY_KEY = "notSelected";
    private static final String RERUNABLE_PATCHES_POSTFIX_PROPERTY_KEY = "allow.rerun.patches";
    private static final String PATCH = "Patch ";
    private static final Joiner DOT_JOINER = Joiner.on('.').skipNulls();
    private PatchExecutionService patchExecutionService;
    private List<Patch> patches = new ArrayList<>();
    protected static final String BOOLEAN_TRUE = "yes";
    protected static final String BOOLEAN_FALSE = "no";


    public List<SystemSetupParameter> getInitializationOptions()
    {
        List<SystemSetupParameter> params = new ArrayList<>();
        if(isPatchExtensionInitialized() && SystemSetupUtils.isHAC())
        {
            boolean isInit = SystemSetupUtils.isInit(null);
            boolean rerunableEnabled = isPatchRerunableFunctionalityEnabled();
            for(Patch patch : this.patches)
            {
                String patchId = patch.getPatchId();
                if(!isPatchHidden(patchId))
                {
                    if(isInit)
                    {
                        params.add(createSystemSetupParameterBasedOnNotSelectedProperty(patch));
                        continue;
                    }
                    params.add(createSystemSetupParameterBasedOnExecutionInfo(patch, rerunableEnabled));
                }
            }
        }
        return params;
    }


    protected void createEssentialData(SystemSetupContext setupContext)
    {
        JspContextHolder.setJspContext(setupContext);
        executePatches(setupContext, SystemSetup.Type.ESSENTIAL);
        JspContextHolder.removeJspContext();
    }


    protected void createProjectData(SystemSetupContext setupContext)
    {
        JspContextHolder.setJspContext(setupContext);
        executePatches(setupContext, SystemSetup.Type.PROJECT);
        JspContextHolder.removeJspContext();
    }


    protected SystemSetupParameter createFalseBooleanValueSystemSetupParameter(String key, String label)
    {
        SystemSetupParameter syncProductsParam = new SystemSetupParameter(key);
        syncProductsParam.setLabel(label);
        syncProductsParam.addValue("no", false);
        return syncProductsParam;
    }


    protected SystemSetupParameter createSystemSetupParameterBasedOnNotSelectedProperty(Patch patch)
    {
        String patchId = patch.getPatchId();
        String patchName = patch.getPatchName();
        if(isPatchNotSelected(patchId))
        {
            return createBooleanSystemSetupParameter(patchId, patchName, false);
        }
        return createBooleanSystemSetupParameter(patchId, patchName, true);
    }


    protected SystemSetupParameter createBooleanSystemSetupParameter(String key, String label, boolean defaultValue)
    {
        SystemSetupParameter syncProductsParam = new SystemSetupParameter(key);
        syncProductsParam.setLabel(label);
        syncProductsParam.addValue("yes", defaultValue);
        syncProductsParam.addValue("no", !defaultValue);
        return syncProductsParam;
    }


    protected void executePatchBasedOnNotSelectedProperty(SystemSetup.Type type, Patch patch)
    {
        if(isPatchSelected(patch.getPatchId()))
        {
            executePatchBasedOnDataType(type, patch);
        }
    }


    protected Boolean getSystemSetupParameter(SystemSetupContext context, String key)
    {
        String parameterValue = context.getParameter(context.getExtensionName() + "_" + context.getExtensionName());
        if(parameterValue != null)
        {
            if("yes".equals(parameterValue))
            {
                return Boolean.TRUE;
            }
            if("no".equals(parameterValue))
            {
                return Boolean.FALSE;
            }
        }
        return null;
    }


    protected SystemSetupParameter createSystemSetupParameterBasedOnExecutionInfo(Patch patch, boolean rerunableEnabled)
    {
        String patchId = patch.getPatchId();
        String patchName = patch.getPatchName();
        boolean rerunablePatch = patch instanceof Rerunnable;
        if(rerunablePatch || this.patchExecutionService.getPatchExecutionById(patchId) == null)
        {
            return createSystemSetupParameterBasedOnNotSelectedProperty(patch);
        }
        if(rerunableEnabled)
        {
            return createBooleanSystemSetupParameter(patchId, patchName, false);
        }
        return createFalseBooleanValueSystemSetupParameter(patchId, patchName);
    }


    protected boolean isPatchHidden(String patchId)
    {
        Boolean isPatchHidden = getBooleanValueForGivenKey(
                        buildPropertyKey("patches", "hidden", patchId));
        if(isPatchHidden == null)
        {
            return Boolean.TRUE.equals(
                            getBooleanValueForGivenKey(
                                            buildPropertyKey("patches", "hideAll", null)));
        }
        return isPatchHidden.booleanValue();
    }


    protected boolean isPatchNotSelected(String patchId)
    {
        Boolean isPatchNotSelected = getBooleanValueForGivenKey(
                        buildPropertyKey("patches", "notSelected", patchId));
        return Boolean.TRUE.equals(isPatchNotSelected);
    }


    protected boolean isPatchSelected(String patchId)
    {
        Boolean isPatchNotSelected = getBooleanValueForGivenKey(
                        buildPropertyKey("patches", "notSelected", patchId));
        return BooleanUtils.isNotTrue(isPatchNotSelected);
    }


    protected boolean isPatchRerunableFunctionalityEnabled()
    {
        Boolean isPatchRerunableEnabled = getBooleanValueForGivenKey(
                        buildPropertyKey("patches", "allow.rerun.patches", null));
        return Boolean.TRUE.equals(isPatchRerunableEnabled);
    }


    protected boolean isPatchRerunable(Patch patch)
    {
        return (isPatchRerunableFunctionalityEnabled() || patch instanceof Rerunnable);
    }


    protected Boolean getBooleanValueForGivenKey(String propertyKey)
    {
        String valueForGivenKey = Config.getParameter(propertyKey);
        if("true".equalsIgnoreCase(valueForGivenKey))
        {
            return Boolean.TRUE;
        }
        if("false".equalsIgnoreCase(valueForGivenKey))
        {
            return Boolean.FALSE;
        }
        return null;
    }


    private String buildPropertyKey(String prefix, String postfix, String patchId)
    {
        String[] keyElements = {prefix, patchId, postfix};
        return DOT_JOINER.join((Object[])keyElements);
    }


    protected void executePatches(SystemSetupContext setupContext, SystemSetup.Type type)
    {
        if(isPatchExtensionInitialized())
        {
            try
            {
                if(SystemSetupUtils.isHAC())
                {
                    executePatchesBasedOnSetupContext(setupContext, type);
                }
                else
                {
                    executePatchesBasedOnConfiguration(type, SystemSetupUtils.isInit(setupContext));
                }
            }
            catch(Throwable t)
            {
                LOG.error(PatchLogger.LoggingMode.HAC_CONSOLE, "Fatal error during executing patches - {}", t.getMessage());
                try
                {
                    this.patchExecutionService.registerPatchExecutionError(t.getMessage());
                }
                catch(Exception e)
                {
                    LOG.error(PatchLogger.LoggingMode.CONSOLE, "Problem with error tracking has occurred", e);
                }
                throw t;
            }
        }
    }


    protected void executePatchesBasedOnSetupContext(SystemSetupContext setupContext, SystemSetup.Type type)
    {
        boolean isInit = SystemSetupUtils.isInit(setupContext);
        for(Patch patch : this.patches)
        {
            Boolean isPatchSetupConfigured = getSystemSetupParameter(setupContext, patch.getPatchId());
            if(isPatchSetupConfigured != null)
            {
                if(Boolean.TRUE.equals(isPatchSetupConfigured))
                {
                    executePatchBasedOnDataType(type, patch);
                }
                continue;
            }
            if(isPatchHidden(patch.getPatchId()))
            {
                executePatchBasedOnConfiguration(type, patch, isInit);
                continue;
            }
            LOG.warn(PatchLogger.LoggingMode.CONSOLE, "There was no BooleanSystemSetupParameter in the SystemSetupContext and Patch is not hidden");
        }
    }


    protected void executePatchesBasedOnConfiguration(SystemSetup.Type type, boolean isInit)
    {
        for(Patch patch : this.patches)
        {
            executePatchBasedOnConfiguration(type, patch, isInit);
        }
    }


    protected void executePatchBasedOnConfiguration(SystemSetup.Type type, Patch patch, boolean isInit)
    {
        if(isInit)
        {
            executePatchBasedOnNotSelectedProperty(type, patch);
        }
        else if(this.patchExecutionService.getPatchExecutionById(patch.getPatchId()) != null)
        {
            boolean rerunableEnabled = isPatchRerunable(patch);
            if(isPatchSelected(patch.getPatchId()) && rerunableEnabled)
            {
                executePatchBasedOnDataType(type, patch);
            }
            else
            {
                LOG.info(PatchLogger.LoggingMode.CONSOLE, "{} {}::Patch already executed. Skipped!", "Patch ", patch.getPatchName());
            }
        }
        else
        {
            executePatchBasedOnNotSelectedProperty(type, patch);
        }
    }


    protected void executePatchBasedOnDataType(SystemSetup.Type type, Patch patch)
    {
        if(type.equals(SystemSetup.Type.ESSENTIAL))
        {
            LOG.info(PatchLogger.LoggingMode.HAC_CONSOLE, "{} {}::Updating essential data", "Patch ", patch.getPatchName());
            patch.createEssentialData();
        }
        else if(type.equals(SystemSetup.Type.PROJECT))
        {
            LOG.info(PatchLogger.LoggingMode.HAC_CONSOLE, "Patch " + patch.getPatchName() + "::Updating project data");
            PatchExecutionModel patchExecution = this.patchExecutionService.createPatchExecution(patch);
            executeVersionOnPatch(patch);
            this.patchExecutionService.registerPatchExecution(patchExecution);
        }
    }


    protected void executeVersionOnPatch(Patch patch)
    {
        StructureState structureState = patch.getStructureState();
        List<Patch> previousPatches = this.patches.subList(0, this.patches.indexOf(patch));
        if(CollectionUtils.isNotEmpty(previousPatches))
        {
            Patch previousPatch = previousPatches.get(previousPatches.size() - 1);
            List<StructureState> structureStateGapsList = StructureStateUtils.getStructureStateGap(structureState, previousPatch
                            .getStructureState());
            for(StructureState oldVersion : structureStateGapsList)
            {
                for(Patch oldPatch : previousPatches)
                {
                    oldPatch.createProjectData(oldVersion);
                }
            }
        }
        patch.createProjectData(structureState);
    }


    private boolean isPatchExtensionInitialized()
    {
        return Utilities.isTypeInitialized("PatchExecution");
    }


    @Required
    public void setPatches(List<Patch> patches)
    {
        this.patches = patches;
    }


    @Required
    public void setPatchExecutionService(PatchExecutionService patchExecutionService)
    {
        this.patchExecutionService = patchExecutionService;
    }
}
