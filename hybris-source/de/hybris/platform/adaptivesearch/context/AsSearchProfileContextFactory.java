package de.hybris.platform.adaptivesearch.context;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;
import java.util.Map;

@Deprecated(since = "2205", forRemoval = true)
public interface AsSearchProfileContextFactory
{
    AsSearchProfileContext createContext(String paramString1, String paramString2, List<CatalogVersionModel> paramList, List<CategoryModel> paramList1);


    AsSearchProfileContext createContext(String paramString1, String paramString2, List<CatalogVersionModel> paramList, List<CategoryModel> paramList1, LanguageModel paramLanguageModel, CurrencyModel paramCurrencyModel);


    AsSearchProfileContext createContext(String paramString1, String paramString2, List<CatalogVersionModel> paramList, List<CategoryModel> paramList1, LanguageModel paramLanguageModel, CurrencyModel paramCurrencyModel, Map<String, List<String>> paramMap);


    AsSearchProfileContext createContext(String paramString1, String paramString2, List<CatalogVersionModel> paramList1, List<CatalogVersionModel> paramList2, List<CategoryModel> paramList, LanguageModel paramLanguageModel, CurrencyModel paramCurrencyModel);
}
