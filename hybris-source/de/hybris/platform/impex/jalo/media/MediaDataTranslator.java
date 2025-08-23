package de.hybris.platform.impex.jalo.media;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.Stack;
import org.apache.log4j.Logger;

public class MediaDataTranslator extends AbstractSpecialValueTranslator
{
    private static final Logger LOG = Logger.getLogger(MediaDataTranslator.class);
    private static final ThreadLocal<Stack<MediaDataHandler>> HANDLERS = new ThreadLocal<>();
    private SpecialColumnDescriptor myCd = null;


    public static synchronized void setMediaDataHandler(MediaDataHandler handler)
    {
        if(handler == null)
        {
            throw new IllegalStateException("handler cannot be null");
        }
        Stack<MediaDataHandler> stack = HANDLERS.get();
        if(stack == null)
        {
            stack = new Stack<>();
            HANDLERS.set(stack);
        }
        stack.push(handler);
    }


    public static final synchronized void unsetMediaDataHandler()
    {
        Stack<MediaDataHandler> stack = HANDLERS.get();
        if(stack != null && !stack.isEmpty())
        {
            MediaDataHandler handler = stack.pop();
            if(handler != null)
            {
                handler.cleanUp();
            }
        }
    }


    public static final boolean hasHandler()
    {
        Stack<MediaDataHandler> stack = HANDLERS.get();
        return (stack != null && !stack.isEmpty());
    }


    public static final MediaDataHandler getHandler()
    {
        Stack<MediaDataHandler> stack = HANDLERS.get();
        if(stack == null || stack.isEmpty())
        {
            throw new IllegalStateException("current handler is not set");
        }
        return stack.peek();
    }


    public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.init(columnDescriptor);
        this.myCd = columnDescriptor;
    }


    public void validate(String expr) throws HeaderValidationException
    {
        super.validate(expr);
        if(!TypeManager.getInstance().getComposedType(Media.class).isAssignableFrom((Type)this.myCd.getHeader().getConfiguredComposedType()))
        {
            throw new HeaderValidationException("illegal composed type " + this.myCd.getHeader().getConfiguredComposedType().getCode() + " for MediaDataTranslator - must be Media or any of its subtypes", 0);
        }
    }


    public String performExport(Item processedItem) throws ImpExException
    {
        MediaDataHandler handler = getHandler();
        if(handler != null && processedItem != null && processedItem.isAlive())
        {
            return handler.exportData((Media)processedItem);
        }
        if(handler == null)
        {
            throw new ImpExException("No MediaDataHandler is set for special column with qualifier " + this.myCd.getQualifier() + "!");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Can not export media data cause processed item is not alive ");
        }
        return null;
    }


    public void performImport(String cellValue, Item processedItem) throws ImpExException
    {
        MediaDataHandler handler = getHandler();
        if(handler != null && cellValue != null && cellValue.length() > 0 && processedItem != null && processedItem.isAlive())
        {
            handler.importData((Media)processedItem, cellValue);
        }
        else
        {
            if(handler == null)
            {
                throw new ImpExException("No MediaDataHandler is set for special column with qualifier " + this.myCd.getQualifier() + "!");
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Can not import data to media cause processed item is not alive ");
            }
        }
    }
}
