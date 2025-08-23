package de.hybris.platform.hac.dao;

import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;

public interface CreditCardPaymentInfoDao
{
    SearchResult<List<Object>> getAllCreditCardsRawData(int paramInt1, int paramInt2, int paramInt3, SortDirection paramSortDirection);
}
