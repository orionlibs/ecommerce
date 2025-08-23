package de.hybris.platform.embeddedserver.base;

import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.WebExtensionModule;
import de.hybris.platform.embeddedserver.api.EmbeddedApplication;
import java.io.File;
import java.nio.file.Path;

public class EmbeddedExtension implements EmbeddedApplication
{
    private String context;
    private final Path webRootPath;


    public EmbeddedExtension(ExtensionInfo extensionInfo)
    {
        if(extensionInfo == null)
        {
            throw new IllegalArgumentException("ExtensionInfo can't be null.");
        }
        WebExtensionModule webModule = extensionInfo.getWebModule();
        if(webModule == null)
        {
            throw new IllegalArgumentException("Extension doesn't contain web module.");
        }
        withContext(webModule.getWebRoot());
        this.webRootPath = (new File(extensionInfo.getExtensionDirectory(), "/web/webroot/")).toPath();
    }


    public EmbeddedExtension withContext(String context)
    {
        if(context == null)
        {
            throw new IllegalArgumentException("Context can't be null");
        }
        String contextToSet = context.startsWith("/") ? context : ("/" + context);
        this.context = contextToSet;
        return this;
    }


    public String getContext()
    {
        return this.context;
    }


    public Path getWebRootPath()
    {
        return this.webRootPath;
    }
}
