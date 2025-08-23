package de.hybris.platform.commons.jalo.renderer;

import java.io.Reader;
import java.io.Writer;

@Deprecated(since = "ages", forRemoval = false)
public interface Renderer
{
    void render(RendererTemplate paramRendererTemplate, Object paramObject, Reader paramReader, Writer paramWriter);
}
