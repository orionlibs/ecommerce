package de.hybris.platform.processengine.definition;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;

class EndNode extends AbstractNode
{
    private static final Logger LOG = Logger.getLogger(EndNode.class);
    private final Type type;
    private final String message;


    EndNode(String id, Type state, String message)
    {
        super(id);
        this.type = state;
        this.message = message;
    }


    public Type getType()
    {
        return this.type;
    }


    public String getMessage()
    {
        return this.message;
    }


    public void trigger(BusinessProcessModel process)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("End node of type %s with ID %s reached for process with code %s", new Object[] {getType(), getId(), process
                            .getCode()}));
        }
        ProcessState processState = ProcessState.valueOf(getType().name());
        process.setEndMessage(getMessage());
        process.setState(processState);
        ((ModelService)Registry.getApplicationContext().getBean("modelService")).save(process);
    }
}
