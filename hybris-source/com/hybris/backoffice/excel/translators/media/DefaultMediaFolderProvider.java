package com.hybris.backoffice.excel.translators.media;

import java.util.Map;

public class DefaultMediaFolderProvider implements MediaFolderProvider
{
    public String provide(Map<String, String> params)
    {
        return params.get("folder");
    }
}
