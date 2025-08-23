package de.hybris.platform.processengine.definition;

import com.google.common.base.MoreObjects;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class NodeExecutionContext
{
    private static final Set<String> DEFAULT_CHOICES = Collections.emptySet();
    private final BusinessProcessModel businessProcessModel;
    private Set<String> choices = DEFAULT_CHOICES;
    private ProcessTaskModel processTaskModel;


    public NodeExecutionContext(BusinessProcessModel businessProcessModel)
    {
        Objects.requireNonNull(businessProcessModel, "businessProcessModel can't be null");
        this.businessProcessModel = businessProcessModel;
    }


    public NodeExecutionContext withChoices(Set<String> choices)
    {
        this.choices = (choices == null) ? DEFAULT_CHOICES : Collections.<String>unmodifiableSet(choices);
        return this;
    }


    public NodeExecutionContext withProcessTaskModel(ProcessTaskModel taskModel)
    {
        this.processTaskModel = taskModel;
        return this;
    }


    public BusinessProcessModel getBusinessProcessModel()
    {
        return this.businessProcessModel;
    }


    public Set<String> getChoices()
    {
        return this.choices;
    }


    public boolean hasAnyChoice()
    {
        return !this.choices.isEmpty();
    }


    public ProcessTaskModel getProcessTaskModel()
    {
        return this.processTaskModel;
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("businessProcessModel", this.businessProcessModel).add("choices", this.choices)
                        .toString();
    }
}
