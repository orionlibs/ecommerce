package de.hybris.platform.commons.translator.renderers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public class EntityRenderer extends AbstractRenderer
{
    private static final Logger LOG = Logger.getLogger(EntityRenderer.class);
    Map<String, String> changeRulesMap = null;
    InputStream entitiesReplaceFileStream;


    public String renderTextFromNode(AbstractNode node, Translator translator)
    {
        if(this.changeRulesMap == null)
        {
            this.changeRulesMap = new HashMap<>();
            Properties properties = new Properties();
            try
            {
                properties.load(this.entitiesReplaceFileStream);
            }
            catch(IOException e)
            {
                LOG.warn(e.getMessage(), e);
            }
            if(this.entitiesReplaceFileStream != null)
            {
                Enumeration<?> enumeration = properties.propertyNames();
                while(enumeration.hasMoreElements())
                {
                    String key = (String)enumeration.nextElement();
                    if(key.startsWith("from_"))
                    {
                        String textTo = properties.getProperty("to___" + key.replace("from_", ""));
                        this.changeRulesMap.put(properties.getProperty(key), textTo);
                    }
                }
            }
        }
        if(this.changeRulesMap.containsKey(node.getNodeText()))
        {
            return this.changeRulesMap.get(node.getNodeText());
        }
        return node.getNodeText();
    }


    public void setEntitiesReplaceFileStream(InputStream entitiesReplaceFileStream)
    {
        this.entitiesReplaceFileStream = entitiesReplaceFileStream;
    }
}
