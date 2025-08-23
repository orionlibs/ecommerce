package de.hybris.platform.core;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.Map;
import org.apache.log4j.Logger;

public class BeanShellUtils
{
    private static final Logger LOG = Logger.getLogger(BeanShellUtils.class.getName());
    private static final Object[][] STANDARD_IMPORTS = new Object[][] {{Boolean.TRUE, "de.hybris.platform.core"}, {Boolean.TRUE, "de.hybris.platform.core.model.user"}, {Boolean.FALSE, "de.hybris.platform.core.HybrisEnumValue"}, {Boolean.TRUE, "de.hybris.platform.util"},
                    {Boolean.TRUE, "de.hybris.platform.impex.jalo"}, {Boolean.TRUE, "de.hybris.platform.jalo"}, {Boolean.FALSE, Currency.class
                    .getName()}, {Boolean.TRUE, "de.hybris.platform.jalo.c2l"}, {Boolean.TRUE, "de.hybris.platform.jalo.user"}, {Boolean.TRUE, "de.hybris.platform.jalo.flexiblesearch"}};


    public static Interpreter createInterpreter()
    {
        return createInterpreter(Collections.EMPTY_MAP);
    }


    public static Interpreter createInterpreter(Map<String, Object> context)
    {
        Interpreter bsh = new Interpreter();
        NameSpace nameSpace = new NameSpace(bsh.getClassManager(), "hybris");
        for(int i = 0; i < STANDARD_IMPORTS.length; i++)
        {
            Object[] objects = STANDARD_IMPORTS[i];
            if(!Boolean.TRUE.equals(objects[0]))
            {
                nameSpace.importClass((String)objects[1]);
            }
            else
            {
                nameSpace.importPackage((String)objects[1]);
            }
        }
        bsh.setNameSpace(nameSpace);
        try
        {
            bsh.set("jaloSession", JaloSession.getCurrentSession());
            if(context != null)
            {
                for(Map.Entry<String, Object> e : context.entrySet())
                {
                    bsh.set(e.getKey(), e.getValue());
                }
            }
        }
        catch(EvalError e)
        {
            throw new JaloSystemException(e, "error initializing bean shell: " + e.getMessage(), 0);
        }
        return bsh;
    }


    public static void checkSLMode()
    {
        boolean SLModeON = Config.getBoolean("impex.servicelayer.mode", false);
        if(SLModeON)
        {
            LOG.warn("warning! 'impex' bean shell variable is used and ServiceLayer is on! Please use 'importer' bean shell variable when ServiceLayer mode is on.");
        }
    }
}
