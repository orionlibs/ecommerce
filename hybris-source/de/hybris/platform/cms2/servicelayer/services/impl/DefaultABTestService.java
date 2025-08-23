package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.enums.ABTestScopes;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;
import de.hybris.platform.cms2.servicelayer.services.ABTestService;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultABTestService extends AbstractCMSService implements ABTestService
{
    private static final Logger LOG = Logger.getLogger(DefaultABTestService.class);


    public SimpleCMSComponentModel getRandomCmsComponent(ABTestCMSComponentContainerModel container)
    {
        int rand;
        List<SimpleCMSComponentModel> elements = container.getSimpleCMSComponents();
        if(elements == null || elements.isEmpty())
        {
            return null;
        }
        ABTestScopes scope = container.getScope();
        String scopeCode = null;
        if(scope != null)
        {
            scopeCode = scope.getCode();
        }
        if(GeneratedCms2Constants.Enumerations.ABTestScopes.REQUEST.equals(scopeCode))
        {
            SecureRandom random = new SecureRandom();
            rand = random.nextInt(elements.size());
        }
        else if(GeneratedCms2Constants.Enumerations.ABTestScopes.SESSION.equals(scopeCode))
        {
            rand = getRandFromSession(container, elements.size());
        }
        else
        {
            LOG.warn("Unkown a/b container scope: [" + scope + "]");
            rand = 0;
        }
        return elements.get(rand);
    }


    public List<SimpleCMSComponentModel> getRandomCMSComponents(ABTestCMSComponentContainerModel container)
    {
        SimpleCMSComponentModel single = getRandomCmsComponent(container);
        return (single != null) ? Collections.<SimpleCMSComponentModel>singletonList(single) : Collections.<SimpleCMSComponentModel>emptyList();
    }


    protected int getRandFromSession(ABTestCMSComponentContainerModel container, int size)
    {
        String key = container.getPk().toString() + "_ABTestRand";
        Integer rand = (Integer)getSessionService().getAttribute(key);
        if(rand == null)
        {
            SecureRandom random = new SecureRandom();
            rand = Integer.valueOf(random.nextInt(size));
            getSessionService().setAttribute(key, rand);
        }
        return rand.intValue();
    }
}
