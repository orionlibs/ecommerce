package de.hybris.platform.workflow.services.internal;

public interface WorkflowFactory<ROOT, SOURCE, TARGET>
{
    TARGET create(ROOT paramROOT, SOURCE paramSOURCE);
}
