package de.hybris.platform.testframework.assertions.assertj;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.input.CountingInputStream;
import org.assertj.core.api.AbstractInputStreamAssert;
import org.assertj.core.api.Assertions;

public final class InputStreamAssert extends AbstractInputStreamAssert<InputStreamAssert, InputStream>
{
    private InputStreamAssert(InputStream actual)
    {
        super(actual, InputStreamAssert.class);
    }


    static InputStreamAssert assertThat(InputStream actual)
    {
        return new InputStreamAssert(actual);
    }


    public InputStreamAssert hasSize(long size)
    {
        try
        {
            CountingInputStream cis = new CountingInputStream((InputStream)this.actual);
            try
            {
                Assertions.assertThat(cis.getByteCount()).isEqualTo(size);
                cis.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    cis.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            failWithMessage(e.getMessage(), new Object[] {e});
        }
        return this;
    }


    public InputStreamAssert hasSameContentAs(byte[] expected)
    {
        InputStream expectedInputStream = new ByteArrayInputStream(expected);
        hasSameContentAs(expectedInputStream);
        return this;
    }


    public InputStreamAssert hasSameSizeAs(InputStream other)
    {
        try
        {
            Assertions.assertThat(getStreamNumBytesForStream((InputStream)this.actual)).isEqualTo(getStreamNumBytesForStream(other));
        }
        catch(IOException e)
        {
            failWithMessage(e.getMessage(), new Object[] {e});
        }
        return this;
    }


    private long getStreamNumBytesForStream(InputStream stream) throws IOException
    {
        CountingInputStream cis = new CountingInputStream(stream);
        try
        {
            long l = cis.getCount();
            cis.close();
            return l;
        }
        catch(Throwable throwable)
        {
            try
            {
                cis.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }
}
