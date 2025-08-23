package de.hybris.e2e.transport.jalo.decorator;

import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;
import java.io.File;
import java.util.Map;

public class E2EMediaPathDecorator extends AbstractImpExCSVCellDecorator
{
    public static final String MEDIA_PACKAGE_DIR_ENV_VAR = "MediaPackageDir";


    public String decorate(int position, Map srcLine)
    {
        String path = (String)srcLine.get(Integer.valueOf(position));
        return "file:" + System.getenv("MediaPackageDir") + File.separator + path;
    }
}
