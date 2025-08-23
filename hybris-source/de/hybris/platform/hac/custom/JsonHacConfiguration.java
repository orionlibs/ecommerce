package de.hybris.platform.hac.custom;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

public class JsonHacConfiguration implements CustomTabProvider
{
    private static final Logger LOG = Logger.getLogger(JsonHacConfiguration.class);
    private Resource resource;
    private List<CustomTabInfo> customTabs;


    @PostConstruct
    public void initialize()
    {
        try
        {
            InputStream inputStream = this.resource.getInputStream();
            try
            {
                ObjectMapper mapper = new ObjectMapper();
                this.customTabs = (List<CustomTabInfo>)mapper.readValue(inputStream, (JavaType)mapper
                                .getTypeFactory().constructCollectionType(List.class, CustomTabInfo.class));
                if(inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch(Throwable throwable)
            {
                if(inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(Exception e)
        {
            LOG.error("Failed to parse hAC json configuration file: " + this.resource.getFilename(), e);
            throw new IllegalStateException("Failed to parse hAC json configuration file: " + this.resource.getFilename(), e);
        }
    }


    public List<CustomTabInfo> getInfo()
    {
        return this.customTabs;
    }


    @Required
    public void setResource(Resource resource)
    {
        this.resource = resource;
    }
}
