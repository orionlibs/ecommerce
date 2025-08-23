package de.hybris.platform.personalizationintegration.mapping;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaultTolerantConverter<SOURCE, TARGET> extends AbstractPopulatingConverter<SOURCE, TARGET>
{
    private static final Logger LOG = LoggerFactory.getLogger(FaultTolerantConverter.class);


    public void populate(SOURCE source, TARGET target)
    {
        List<Populator<SOURCE, TARGET>> list = getPopulators();
        if(list == null)
        {
            return;
        }
        for(Populator<SOURCE, TARGET> populator : list)
        {
            if(populator != null)
            {
                try
                {
                    populator.populate(source, target);
                }
                catch(RuntimeException e)
                {
                    LOG.warn("Populator [" + populator.getClass().getName() + "] in converter [" + getMyBeanName() + "] thrown an exception:", e);
                }
            }
        }
    }
}
