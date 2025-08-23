package de.hybris.platform.impex.distributed.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.impex.model.ImportBatchContentModel;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ImportBatchContentModelAttributeHandler extends AbstractDynamicAttributeHandler<ImportBatchContentModel, ImportBatchModel>
{
    private static final String QUERY = "SELECT {pk} FROM {ImportBatchContent} WHERE {code}=?code";
    private FlexibleSearchService flexibleSearchService;


    @Nullable
    public ImportBatchContentModel get(ImportBatchModel model)
    {
        String code = model.getImportContentCode();
        if(StringUtils.isBlank(code))
        {
            return null;
        }
        ImmutableMap immutableMap = ImmutableMap.of("code", code);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {ImportBatchContent} WHERE {code}=?code", (Map)immutableMap);
        query.setDisableCaching(true);
        SearchResult<ImportBatchContentModel> queryResult = this.flexibleSearchService.search(query);
        Preconditions.checkState((queryResult.getCount() == 1), "Expected exactly one %s with code '%s' but %s has been found.", "ImportBatchContent", code,
                        Integer.valueOf(queryResult.getCount()));
        return queryResult.getResult().get(0);
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
