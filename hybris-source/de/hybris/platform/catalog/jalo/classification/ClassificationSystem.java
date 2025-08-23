package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class ClassificationSystem extends GeneratedClassificationSystem
{
    private static final Logger LOG = Logger.getLogger(ClassificationSystem.class.getName());


    public ClassificationSystemVersion createSystemVersion(String version, Language language) throws ConsistencyCheckException
    {
        return CatalogManager.getInstance().createClassificationSystemVersion(this, version, language);
    }


    public ClassificationSystemVersion createSystemVersion(String version, Collection<Language> languages) throws ConsistencyCheckException
    {
        return CatalogManager.getInstance().createClassificationSystemVersion(this, version, languages);
    }


    @ForceJALO(reason = "something else")
    public void setCatalogVersions(SessionContext ctx, Set<CatalogVersion> catalogVersions)
    {
        if(catalogVersions != null)
        {
            for(Iterator<CatalogVersion> iter = catalogVersions.iterator(); iter.hasNext(); )
            {
                CatalogVersion catalogVersion = iter.next();
                if(!(catalogVersion instanceof ClassificationSystemVersion))
                {
                    throw new JaloInvalidParameterException("ClassificationSystem.catalogVersions only allows ClassificationSystemVersion elements : got " + catalogVersion
                                    .getClass().getName(), 0);
                }
            }
        }
        super.setCatalogVersions(ctx, catalogVersions);
    }


    public Set<ClassificationSystemVersion> getSystemVersions()
    {
        return new LinkedHashSet<>(getCatalogVersions());
    }


    @ForceJALO(reason = "something else")
    public Set<CatalogVersion> getCatalogVersions(SessionContext ctx)
    {
        Set<?> ret = super.getCatalogVersions(ctx);
        if(ret != null)
        {
            Set<?> filtered = new LinkedHashSet(ret);
            for(Iterator<CatalogVersion> iter = filtered.iterator(); iter.hasNext(); )
            {
                CatalogVersion catalogVersion = iter.next();
                if(!(catalogVersion instanceof ClassificationSystemVersion))
                {
                    iter.remove();
                }
            }
            if(!ret.equals(filtered))
            {
                super.setCatalogVersions(ctx, filtered);
                ret = filtered;
            }
        }
        return (Set)ret;
    }


    @ForceJALO(reason = "something else")
    public ClassificationSystemVersion getActiveCatalogVersion()
    {
        return getActiveCatalogVersion(getSession().getSessionContext());
    }


    @ForceJALO(reason = "something else")
    public ClassificationSystemVersion getActiveCatalogVersion(SessionContext ctx)
    {
        try
        {
            return (ClassificationSystemVersion)super.getActiveCatalogVersion(ctx);
        }
        catch(ClassCastException e)
        {
            return null;
        }
    }


    @ForceJALO(reason = "something else")
    public void setActiveCatalogVersion(SessionContext ctx, CatalogVersion catalogVersion)
    {
        super.setActiveCatalogVersion(ctx, (
                        catalogVersion != null && catalogVersion instanceof ClassificationSystemVersion) ? catalogVersion : null);
    }


    public ClassificationSystemVersion getSystemVersion(String version)
    {
        try
        {
            return (ClassificationSystemVersion)getCatalogVersion(version);
        }
        catch(ClassCastException e)
        {
            return null;
        }
    }
}
