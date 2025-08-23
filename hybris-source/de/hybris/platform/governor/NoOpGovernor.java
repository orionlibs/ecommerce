package de.hybris.platform.governor;

final class NoOpGovernor implements ResourceGovernor
{
    private static final NoOpContext NO_OP_CONTEXT = new NoOpContext();
    private static final NoOpExecutionInformationBuilder NO_OP_EI_BUILDER = new NoOpExecutionInformationBuilder();


    public ExecutionContext beginExecution(ExecutionInformation executionData)
    {
        return (ExecutionContext)NO_OP_CONTEXT;
    }


    public NoOpExecutionInformationBuilder fromCurrentOperationInfo(String operationType)
    {
        return NO_OP_EI_BUILDER;
    }
}
