package de.hybris.platform.platformbackoffice.widgets.collectionbrowser.mold.impl.listview.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.services.media.PreviewResolutionStrategy;
import com.hybris.cockpitng.util.UITools;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTypeStaticPreviewResolutionStrategy implements PreviewResolutionStrategy<Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTypeStaticPreviewResolutionStrategy.class);
    private String urlPrefix;
    private Map<String, String> typeToImageMapping;
    private TypeService typeService;
    private TypeFacade typeFacade;
    private Properties extensionsToMime;


    public boolean canResolve(Object target)
    {
        try
        {
            String entityType = this.typeFacade.getType(target);
            for(String type : this.typeToImageMapping.keySet())
            {
                if(this.typeService.isAssignableFrom(type, entityType))
                {
                    return true;
                }
            }
        }
        catch(UnknownIdentifierException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Type not found for object {}", target);
            }
        }
        return false;
    }


    public String resolvePreviewUrl(Object target)
    {
        String entityType = this.typeFacade.getType(target);
        for(Map.Entry<String, String> entry : this.typeToImageMapping.entrySet())
        {
            if(this.typeService.isAssignableFrom(entry.getKey(), entityType))
            {
                return this.urlPrefix + this.urlPrefix;
            }
        }
        throw new IllegalStateException();
    }


    public String resolveMimeType(Object target)
    {
        String url = resolvePreviewUrl(target);
        String extension = UITools.extractExtension(url);
        if(extension != null)
        {
            return (String)StringUtils.defaultIfBlank(this.extensionsToMime.getProperty(extension), "");
        }
        return "";
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setExtensionsToMime(Properties extensionsToMime)
    {
        this.extensionsToMime = extensionsToMime;
    }


    @Required
    public void setTypeToImageMapping(Map<String, String> typeToImageMapping)
    {
        this.typeToImageMapping = typeToImageMapping;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setUrlPrefix(String urlPrefix)
    {
        this.urlPrefix = urlPrefix;
    }
}
