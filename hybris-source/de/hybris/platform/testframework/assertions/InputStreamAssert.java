package de.hybris.platform.testframework.assertions;

import com.google.common.io.CountingInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

@Deprecated(since = "2011", forRemoval = true)
public final class InputStreamAssert extends GenericAssert<InputStreamAssert, InputStream>
{
    private InputStreamAssert(InputStream actual)
    {
        super(InputStreamAssert.class, actual);
    }


    public static InputStreamAssert assertThat(InputStream actual)
    {
        return new InputStreamAssert(actual);
    }


    public InputStreamAssert hasSize(long size)
    {
        CountingInputStream cis = null;
        try
        {
            cis = new CountingInputStream((InputStream)this.actual);
            Assertions.assertThat(cis.getCount()).isEqualTo(size);
        }
        finally
        {
            if(cis != null)
            {
                IOUtils.closeQuietly((InputStream)cis);
            }
        }
        return this;
    }


    public InputStreamAssert hasSameDataAs(byte[] expected)
    {
        try
        {
            byte[] byteArray = IOUtils.toByteArray((InputStream)this.actual);
            Assertions.assertThat(byteArray).isEqualTo(expected);
        }
        catch(IOException e)
        {
            fail(e.getMessage(), e);
        }
        finally
        {
            if(this.actual != null)
            {
                IOUtils.closeQuietly((InputStream)this.actual);
            }
        }
        return this;
    }


    public InputStreamAssert hasSameDataAs(InputStream expected)
    {
        try
        {
            Assertions.assertThat(IOUtils.contentEquals((InputStream)this.actual, expected)).isTrue();
        }
        catch(IOException e)
        {
            fail(e.getMessage(), e);
        }
        finally
        {
            if(this.actual != null)
            {
                IOUtils.closeQuietly((InputStream)this.actual);
            }
        }
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
            fail(e.getMessage(), e);
        }
        return this;
    }


    private long getStreamNumBytesForStream(InputStream stream) throws IOException
    {
        CountingInputStream cis = null;
        try
        {
            cis = new CountingInputStream(stream);
            return cis.getCount();
        }
        finally
        {
            if(cis != null)
            {
                cis.close();
            }
        }
    }
}
