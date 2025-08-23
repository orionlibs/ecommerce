package de.hybris.platform.hac.dao.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.hac.dao.CreditCardPaymentInfoDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCardPaymentInfoDao implements CreditCardPaymentInfoDao
{
    private static final Logger LOG = Logger.getLogger(DefaultCardPaymentInfoDao.class);
    private static final int MAX_CREDIT_CARD_LENGTH = 21;
    private FlexibleSearchService flexibleSearchService;


    public SearchResult<List<Object>> getAllCreditCardsRawData(int start, int count, int orderByColNum, CreditCardPaymentInfoDao.SortDirection sortDirection)
    {
        StringBuilder stringBuilder = new StringBuilder("SELECT {");
        stringBuilder.append("pk").append("}, {");
        stringBuilder.append("type").append("}, {");
        stringBuilder.append("number").append("} FROM {");
        stringBuilder.append("CreditCardPaymentInfo").append("} ");
        stringBuilder.append("WHERE {").append("number").append("} IS NOT NULL AND ");
        addLengthConditionToNumberField(stringBuilder);
        addOrderByClause(stringBuilder, orderByColNum, sortDirection);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Unencrypted credit cards search query: " + stringBuilder);
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
        query.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class, CreditCardType.class, String.class}));
        query.setFailOnUnknownFields(true);
        query.setStart(start);
        query.setCount(count);
        query.setNeedTotal(true);
        return this.flexibleSearchService.search(query);
    }


    private void addLengthConditionToNumberField(StringBuilder stringBuilder)
    {
        if(Config.isSQLServerUsed())
        {
            stringBuilder.append("LEN({").append("number").append("})");
        }
        else
        {
            stringBuilder.append("LENGTH({").append("number").append("})");
        }
        stringBuilder.append(" <= ").append(21);
    }


    private void addOrderByClause(StringBuilder stringBuilder, int orderByColNum, CreditCardPaymentInfoDao.SortDirection sortDirection)
    {
        stringBuilder.append(" ORDER BY {");
        stringBuilder.append(ColMapping.getColumnName(orderByColNum).getCode());
        stringBuilder.append("} ");
        stringBuilder.append(sortDirection.getCode());
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
