package de.hybris.platform.jalo.c2l;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.util.localization.Localization;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class C2LItem extends GeneratedC2LItem
{
    private static final long serialVersionUID = -6264491020119395143L;


    @Deprecated(since = "ages", forRemoval = false)
    public Map getAllNames(SessionContext ctx)
    {
        return getAllName(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setAllNames(SessionContext ctx, Map names)
    {
        setAllName(ctx, names);
    }


    @ForceJALO(reason = "consistency check")
    public void setActive(SessionContext ctx, Boolean active) throws ConsistencyCheckException
    {
        checkConsistencyActive(active, getComposedType().getCode());
        super.setActive(ctx, active);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public String getIsoCode()
    {
        return getIsocode(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public String getIsoCode(SessionContext ctx)
    {
        return getIsocode(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public void setIsoCode(String iso) throws ConsistencyCheckException
    {
        setIsoCode(getSession().getSessionContext(), iso);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public void setIsoCode(SessionContext ctx, String iso) throws ConsistencyCheckException
    {
        setIsocode(ctx, iso);
    }


    @ForceJALO(reason = "consistency check")
    public void setIsocode(SessionContext ctx, String value) throws ConsistencyCheckException
    {
        checkConsistencyIsocode(value, null, getComposedType().getCode());
        super.setIsocode(ctx, value);
    }


    @ForceJALO(reason = "something else")
    public Boolean isActive(SessionContext ctx)
    {
        Boolean result = super.isActive(ctx);
        return (result == null) ? Boolean.FALSE : result;
    }


    protected void checkConsistencyIsocode(String newIsoCode, Country country, String composedTypeCode) throws ConsistencyCheckException
    {
        Map<String, Object> params = new HashMap<>();
        params.put("isocode", newIsoCode);
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(PK).append("} FROM {").append(composedTypeCode).append("} WHERE {").append("isocode")
                        .append("}=?isocode");
        List<C2LItem> result = FlexibleSearch.getInstance().search(query.toString(), params, Collections.singletonList(C2LItem.class), true, true, 0, 1).getResult();
        if(result.size() > 1)
        {
            throw new ConsistencyCheckException(null,
                            Localization.getLocalizedString("exception.core.duplicateisocode", new Object[] {newIsoCode, result}), 0);
        }
        if(result.size() == 1)
        {
            C2LItem other = result.iterator().next();
            if(!other.getPK().equals(getPK()))
            {
                throw new ConsistencyCheckException(null,
                                Localization.getLocalizedString("exception.core.duplicateisocode", new Object[] {newIsoCode, other}), 0);
            }
        }
    }


    protected void checkConsistencyActive(Boolean active, String composedTypeCode) throws ConsistencyCheckException
    {
        if(active == null || !active.booleanValue())
        {
            if(!isFirstTypeSpecificC2LItemCreated(composedTypeCode) && getActiveCount(composedTypeCode) <= 1)
            {
                throw new ConsistencyCheckException(null,
                                Localization.getLocalizedString("exception.core.lastactivec2litem", new Object[] {getIsocode()}), 0);
            }
        }
    }


    private boolean isFirstTypeSpecificC2LItemCreated(String composedTypeCode)
    {
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(PK).append("} FROM {").append(composedTypeCode).append('}');
        return
                        (FlexibleSearch.getInstance().search(query.toString(), null, Collections.singletonList(C2LItem.class), true, true, 0, 1).getResult().size() == 1);
    }


    private int getActiveCount(String composedTypeCode)
    {
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(PK).append("} FROM {").append(composedTypeCode).append("} WHERE {").append("active")
                        .append("}=?flag");
        return FlexibleSearch.getInstance().search(query.toString(), Collections.singletonMap("flag", Boolean.TRUE),
                        Collections.singletonList(C2LItem.class), true, true, 0, 1).getResult().size();
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return super.toString() + "->" + super.toString();
    }
}
