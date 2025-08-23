package de.hybris.platform.governor;

import com.google.common.annotations.Beta;
import java.io.Closeable;

@Beta
public interface ExecutionContext extends Closeable
{
    void close();
}
