package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImpExError;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Consumer;
import org.apache.commons.io.IOUtils;

class ImpExErrorSpliterator extends Spliterators.AbstractSpliterator<ImpExError>
{
    private final ImpExErrorReader impExErrorReader;
    private final InputStream unresolvedLines;


    ImpExErrorSpliterator(InputStream unresolvedLines)
    {
        super(Long.MAX_VALUE, 256);
        this.unresolvedLines = Objects.<InputStream>requireNonNull(unresolvedLines, "unresolvedLines is required");
        this.impExErrorReader = getErrorReader(unresolvedLines);
    }


    private ImpExErrorReader getErrorReader(InputStream unresolvedLines)
    {
        try
        {
            return new ImpExErrorReader(this, unresolvedLines);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new SystemException(e);
        }
    }


    public boolean tryAdvance(Consumer<? super ImpExError> action)
    {
        ImpExError nextError = this.impExErrorReader.getNextError();
        if(nextError == null)
        {
            IOUtils.closeQuietly(this.unresolvedLines);
            return false;
        }
        action.accept(nextError);
        return true;
    }
}
