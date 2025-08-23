package de.hybris.platform.jalo.extension;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.persistence.extension.ExtensionEJB;
import de.hybris.platform.util.JspContext;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class Extension extends Manager
{
    private static final Logger LOG = Logger.getLogger(Extension.class.getName());


    public void onFirstSessionCreation()
    {
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
        super.checkBeforeItemRemoval(ctx, item);
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        super.notifyItemRemoval(ctx, item);
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc) throws Exception
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc) throws Exception
    {
    }


    public final void createSampleData(Map<String, String> params, JspContext jspc) throws Exception
    {
    }


    public void notifyInitializationEnd(Map<String, String> params, JspContext ctx) throws Exception
    {
    }


    public void notifyInitializationStart(Map<String, String> params, JspContext ctx) throws Exception
    {
    }


    public void checkBeforeInitialization(JspContext ctx, boolean forceInit) throws Exception
    {
    }


    public Collection<String> getCreatorParameterNames()
    {
        return Collections.EMPTY_SET;
    }


    public String getCreatorParameterDefault(String param)
    {
        return null;
    }


    public List<String> getCreatorParameterPossibleValues(String param)
    {
        return null;
    }


    public String getCreatorName()
    {
        return getName();
    }


    public String getCreatorDescription()
    {
        return null;
    }


    public boolean isCreatorDisabled()
    {
        return false;
    }


    public ExtensionEJB getRemote()
    {
        return getTenant().getSystemEJB().getExtensionManager().getExtension(getName());
    }


    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        return Collections.EMPTY_MAP;
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new ExtensionDTO(this);
    }


    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }


    public abstract String getName();
}
