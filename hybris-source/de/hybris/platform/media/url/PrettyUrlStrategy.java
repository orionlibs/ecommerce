package de.hybris.platform.media.url;

import java.util.Optional;

public interface PrettyUrlStrategy
{
    Optional<String> assemblePath(MediaData paramMediaData);


    ParsedPath parsePath(String paramString);


    boolean isPrettyUrlEnabled();
}
