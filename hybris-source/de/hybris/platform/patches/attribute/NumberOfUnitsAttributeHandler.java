package de.hybris.platform.patches.attribute;

import de.hybris.platform.patches.model.PatchExecutionModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class NumberOfUnitsAttributeHandler extends AbstractDynamicAttributeHandler<Integer, PatchExecutionModel>
{
    private static final String SELECT_PATCH_EXECUTION_UNIT_FOR_PATCH_QUERY = "SELECT COUNT({pk}) FROM {PatchExecutionUnit} WHERE {patch}=?patch";
    private FlexibleSearchService flexibleSearchService;


    public Integer get(PatchExecutionModel model)
    {
        Objects.requireNonNull(model);
        Map<String, Object> params = new HashMap<>();
        params.put("patch", model);
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT COUNT({pk}) FROM {PatchExecutionUnit} WHERE {patch}=?patch", params);
        fsq.setResultClassList(Collections.singletonList(Integer.class));
        return this.flexibleSearchService.search(fsq).getResult().get(0);
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
