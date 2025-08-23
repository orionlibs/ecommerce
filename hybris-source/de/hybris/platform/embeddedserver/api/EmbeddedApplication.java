package de.hybris.platform.embeddedserver.api;

import java.nio.file.Path;

public interface EmbeddedApplication
{
    String getContext();


    Path getWebRootPath();
}
