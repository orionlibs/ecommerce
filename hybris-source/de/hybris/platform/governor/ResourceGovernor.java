package de.hybris.platform.governor;

import com.google.common.annotations.Beta;

@Beta
public interface ResourceGovernor
{
    @Beta
    ExecutionContext beginExecution(ExecutionInformation paramExecutionInformation) throws ExecutionRejectedException;


    @Beta
    ExecutionInformationBuilder fromCurrentOperationInfo(String paramString);
}
