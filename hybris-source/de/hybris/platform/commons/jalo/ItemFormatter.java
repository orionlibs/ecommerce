package de.hybris.platform.commons.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import org.apache.log4j.Logger;

public abstract class ItemFormatter extends GeneratedItemFormatter
{
    private static final Logger log = Logger.getLogger(ItemFormatter.class.getName());


    public abstract Media format(Item paramItem) throws JaloBusinessException;
}
