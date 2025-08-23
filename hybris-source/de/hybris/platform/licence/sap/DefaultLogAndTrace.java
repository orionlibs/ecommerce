package de.hybris.platform.licence.sap;

import com.sap.security.core.server.likey.LogAndTrace;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class DefaultLogAndTrace implements LogAndTrace
{
    private static final Logger LOG = Logger.getLogger(DefaultLogAndTrace.class);
    private final List<String> traceMessages;
    private final List<String> errorMessages;
    private final Level traceLevel;
    private final Level errorLevel;


    public DefaultLogAndTrace()
    {
        this(Level.INFO, Level.ERROR);
    }


    public DefaultLogAndTrace(Level traceLevel)
    {
        this(traceLevel, Level.ERROR);
    }


    public DefaultLogAndTrace(Level traceLevel, Level errorLevel)
    {
        this.traceLevel = traceLevel;
        this.errorLevel = errorLevel;
        this.traceMessages = new LinkedList<>();
        this.errorMessages = new LinkedList<>();
    }


    public void writeError(String message)
    {
        this.errorMessages.add(message);
        if(LOG.isEnabledFor((Priority)this.errorLevel))
        {
            LOG.log((Priority)this.errorLevel, message);
        }
    }


    public void writeTrace(String message)
    {
        this.traceMessages.add(message);
        if(LOG.isEnabledFor((Priority)this.traceLevel))
        {
            LOG.log((Priority)this.traceLevel, message);
        }
    }


    public String getAndClearErrorMessages()
    {
        String messages = buildMessageFromContainer(this.errorMessages);
        this.errorMessages.clear();
        return messages;
    }


    public String getAndClearTraceMessages()
    {
        String messages = buildMessageFromContainer(this.traceMessages);
        this.traceMessages.clear();
        return messages;
    }


    private String buildMessageFromContainer(List<String> messages)
    {
        if(messages.isEmpty())
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(String message : messages)
        {
            sb.append(message);
        }
        return sb.toString();
    }
}
