package de.hybris.platform.cockpit.reports.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class CompiledJasperMedia extends GeneratedCompiledJasperMedia
{
    private static final Logger LOG = LoggerFactory.getLogger(CompiledJasperMedia.class.getName());


    @Deprecated
    public void setData(InputStream stream, String originalName, String mimeType, MediaFolder folder)
    {
        super.setData(stream, originalName, mimeType, folder);
        saveCompiledCounterpart();
    }


    @Deprecated
    public void setData(Media media)
    {
        super.setData(media);
        saveCompiledCounterpart();
    }


    @Deprecated
    private void saveCompiledCounterpart()
    {
        if(getCompiledReport() == null)
        {
            setCompiledReport((Media)createCompiledReport(getCode() + " (compiled)"));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            getCompiledReport().moveMedia(getFolder());
            JasperCompileManager.compileReportToStream(new ByteArrayInputStream(getData()), out);
            getCompiledReport().setData(out.toByteArray());
        }
        catch(JaloBusinessException e)
        {
            LOG.error("Can't move compiled report: " + getCompiledReport().getCode() + " or set it's data.", (Throwable)e);
        }
        catch(Throwable t)
        {
            LOG.error("Can't compile report: " + getCode(), t);
        }
    }


    @Deprecated
    private JasperMedia createCompiledReport(String code)
    {
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put(Item.PK, null);
            params.put("code", code);
            params.put("folder", getFolder());
            ComposedType adjustedType = TypeManager.getInstance().getComposedType(JasperMedia.class);
            return (JasperMedia)adjustedType.newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }
}
