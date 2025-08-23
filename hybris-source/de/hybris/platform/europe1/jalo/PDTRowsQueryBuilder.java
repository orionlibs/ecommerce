package de.hybris.platform.europe1.jalo;

import de.hybris.platform.core.PK;
import java.util.Map;

public interface PDTRowsQueryBuilder
{
    PDTRowsQueryBuilder withAnyProduct();


    PDTRowsQueryBuilder withProduct(PK paramPK);


    PDTRowsQueryBuilder withProductGroup(PK paramPK);


    PDTRowsQueryBuilder withProductId(String paramString);


    PDTRowsQueryBuilder withAnyUser();


    PDTRowsQueryBuilder withUser(PK paramPK);


    PDTRowsQueryBuilder withUserGroup(PK paramPK);


    QueryWithParams build();


    static PDTRowsQueryBuilder defaultBuilder(String type)
    {
        return (PDTRowsQueryBuilder)new DefaultPDTRowsQueryBuilder(type);
    }


    static PDTRowsQueryBuilder defaultBuilder(String type, Map<String, Class<?>> attributes)
    {
        return (PDTRowsQueryBuilder)new DefaultPDTRowsQueryBuilder(type, attributes);
    }
}
