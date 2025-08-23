package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.SQLSearchResult;
import java.util.Collections;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

public class FlexibleSearchParser
{
    private static final Logger LOG = Logger.getLogger(FlexibleSearchParser.class.getName());
    private final String COMMA = String.valueOf("'");
    private final String DYNAMIC_HOOK = ":DYNA_HOOK:";


    public String doParseFlexQuery(JaloSession session, String flexSyntaxQuery) throws JaloInvalidParameterException
    {
        if(session != null)
        {
            if(session.getUser().isAnonymousCustomer())
            {
                throw new IllegalStateException("Session has been already invalidated, please relogin ");
            }
            SessionContext localSessionContext = session.createLocalSessionContext();
            try
            {
                localSessionContext.setAttribute("disableExecution", Boolean.TRUE);
                localSessionContext.setAttribute("disableRestrictions", null);
                SQLSearchResult<? extends Item> res = null;
                res = prepareResult(flexSyntaxQuery, localSessionContext);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("query before ::");
                    LOG.debug(res.getSQLForPreparedStatement());
                    LOG.debug(res.getValuesForPreparedStatement());
                }
                String queryWithWildCard = res.getSQLForPreparedStatement();
                for(Object replaceor : res.getValuesForPreparedStatement())
                {
                    queryWithWildCard = queryWithWildCard.replaceFirst("\\?", getFormattedReplaceor(replaceor));
                }
                queryWithWildCard = queryWithWildCard.replaceAll(":DYNA_HOOK:", "?");
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("query after ::");
                    LOG.debug(queryWithWildCard);
                }
                return queryWithWildCard;
            }
            finally
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        throw new JaloInvalidParameterException("Provided session is null", 0);
    }


    private String getFormattedReplaceor(Object replacer)
    {
        if(replacer instanceof String)
        {
            StringBuilder stringBuilder = new StringBuilder(((String)replacer).length() + 4);
            stringBuilder.append(this.COMMA).append(StringEscapeUtils.escapeSql(String.valueOf(replacer))).append(this.COMMA);
            return stringBuilder.toString();
        }
        if(replacer instanceof java.sql.Date || replacer instanceof java.util.Date)
        {
            StringBuffer stringBuffer = new StringBuffer(20);
            stringBuffer.append(this.COMMA).append(String.valueOf(replacer)).append(this.COMMA);
            return stringBuffer.toString();
        }
        if(replacer instanceof ItemPropertyValue)
        {
            return String.valueOf(((ItemPropertyValue)replacer).getPK());
        }
        return String.valueOf(replacer);
    }


    private SQLSearchResult<? extends Item> prepareResult(String pquery, SessionContext sessionCtx)
    {
        String query = pquery;
        query = query.replaceAll("\\?", ":DYNA_HOOK:");
        SQLSearchResult<? extends Item> res = (SQLSearchResult<? extends Item>)getFlexibleSearchInstance().search(sessionCtx, query, Collections.EMPTY_MAP, Item.class);
        return res;
    }


    private FlexibleSearch getFlexibleSearchInstance()
    {
        return FlexibleSearch.getInstance();
    }
}
