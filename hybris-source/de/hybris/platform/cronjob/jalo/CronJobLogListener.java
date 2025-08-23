package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLoggingEvent;
import java.util.Stack;
import org.apache.log4j.Level;

public class CronJobLogListener implements HybrisLogListener
{
    private static final ThreadLocal<Stack<CronJobLogContext>> CURRENT_CRONJOB_LOG_CTX = new ThreadLocal<>();


    public static void setCurrentContext(CronJobLogContext ctx)
    {
        Stack<CronJobLogContext> stack = CURRENT_CRONJOB_LOG_CTX.get();
        if(stack == null)
        {
            CURRENT_CRONJOB_LOG_CTX.set(stack = new Stack<>());
        }
        stack.push(ctx);
    }


    public static void unsetsetCurrentContext()
    {
        Stack<CronJobLogContext> stack = CURRENT_CRONJOB_LOG_CTX.get();
        if(stack != null && !stack.isEmpty())
        {
            stack.pop();
        }
    }


    public static CronJobLogContext getCurrentContext()
    {
        Stack<CronJobLogContext> stack = CURRENT_CRONJOB_LOG_CTX.get();
        CronJobLogContext logContext = (stack != null && !stack.isEmpty()) ? stack.peek() : null;
        return logContext;
    }


    public void log(HybrisLoggingEvent event)
    {
        CronJobLogContext currentContext = getCurrentContext();
        if(currentContext != null)
        {
            currentContext.writeLog(event.getRenderedMessage(), event.getLevel());
        }
    }


    public boolean isEnabledFor(Level level)
    {
        CronJobLogContext currentContext = getCurrentContext();
        return (currentContext != null && currentContext.isEnabledFor(level));
    }
}
