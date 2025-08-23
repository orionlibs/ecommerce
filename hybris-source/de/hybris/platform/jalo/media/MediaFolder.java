package de.hybris.platform.jalo.media;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.media.interceptors.PreventRootMediaFolderRemovalInterceptor;
import de.hybris.platform.util.localization.Localization;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class MediaFolder extends GeneratedMediaFolder
{
    private static final Logger LOG = Logger.getLogger(MediaFolder.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("qualifier", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new MediaFolder", 0);
        }
        allAttributes.setAttributeMode("qualifier", Item.AttributeMode.INITIAL);
        checkConsistency((String)allAttributes.get("qualifier"), null);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void checkConsistency(String newQualifier, PK myPK) throws ConsistencyCheckException
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("quali", newQualifier);
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(PK).append("} FROM {").append(GeneratedCoreConstants.TC.MEDIAFOLDER).append("} WHERE {")
                        .append("qualifier").append("}=?quali");
        if(myPK != null)
        {
            query.append(" AND {").append(PK).append("} <> ?myPK");
            params.put("myPK", myPK);
        }
        List result = FlexibleSearch.getInstance().search(query.toString(), params, Collections.singletonList(MediaFolder.class), true, true, 0, 1).getResult();
        if(!result.isEmpty())
        {
            throw new JaloInvalidParameterException("Duplicate qualifier '" + newQualifier + "' for type MediaFolder", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "consistency check")
    public void setQualifier(SessionContext ctx, String value) throws JaloInvalidParameterException
    {
        try
        {
            checkConsistency(value, getPK());
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloInvalidParameterException(e, 0);
        }
        super.setQualifier(ctx, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "consistency check")
    protected void setPath(SessionContext ctx, String value)
    {
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'path' is not changeable", 0);
        }
        String newValue = FilenameUtils.normalizeNoEndSeparator(value);
        newValue = FilenameUtils.separatorsToUnix(newValue);
        if(value != null && (newValue == null || FilenameUtils.getPrefixLength(newValue) > 0))
        {
            throw new JaloInvalidParameterException(Localization.getLocalizedString("exception.core.mediafolder.path.invalid", new Object[] {value}), 0);
        }
        super.setPath(ctx, newValue);
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        PreventRootMediaFolderRemovalInterceptor.MediaRootFolderRemovalLogic removalLogic = new PreventRootMediaFolderRemovalInterceptor.MediaRootFolderRemovalLogic();
        if(removalLogic.isRootMediaFolder(getQualifier()))
        {
            if(removalLogic.canRemoveRootMediaFolder())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("removing the root folder " + this);
                }
            }
            else
            {
                throw new ConsistencyCheckException(removalLogic.getErrorMessage(), 0);
            }
        }
        super.remove(ctx);
    }


    public String toString()
    {
        return getQualifier() + "(" + getQualifier() + ")";
    }
}
