package de.hybris.platform.cms2.jalo.contents.containers;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

public class ABTestCMSComponentContainer extends GeneratedABTestCMSComponentContainer
{
    private static final Logger log = Logger.getLogger(ABTestCMSComponentContainer.class.getName());


    protected int getRandFromSession(int size)
    {
        String key = getPK().toString() + "_ABTestRand";
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Integer rand = (Integer)ctx.getAttribute(key);
        if(rand == null)
        {
            rand = Integer.valueOf(RandomUtils.nextInt(size));
            ctx.setAttribute(key, rand);
        }
        return rand.intValue();
    }


    protected SimpleCMSComponent getRandomElement()
    {
        int rand;
        String scope = getScope().getCode();
        List<SimpleCMSComponent> elements = getSimpleCMSComponents();
        if(elements == null || elements.isEmpty())
        {
            return null;
        }
        if(GeneratedCms2Constants.Enumerations.ABTestScopes.REQUEST.equals(scope))
        {
            rand = RandomUtils.nextInt(elements.size());
        }
        else if(GeneratedCms2Constants.Enumerations.ABTestScopes.SESSION.equals(scope))
        {
            rand = getRandFromSession(elements.size());
        }
        else
        {
            log.warn("Unknown a/b container scope: [" + scope + "]");
            rand = 0;
        }
        return elements.get(rand);
    }


    @Deprecated(since = "4.3")
    public List<SimpleCMSComponent> getCurrentCMSComponents(SessionContext ctx)
    {
        SimpleCMSComponent single = getRandomElement();
        return (single != null) ? Collections.<SimpleCMSComponent>singletonList(single) : Collections.<SimpleCMSComponent>emptyList();
    }
}
