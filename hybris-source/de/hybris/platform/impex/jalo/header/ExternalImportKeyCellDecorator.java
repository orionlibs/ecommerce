package de.hybris.platform.impex.jalo.header;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.jalo.imp.ValueLineMetaData;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.logging.Logs;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class ExternalImportKeyCellDecorator extends AbstractImpExCSVCellDecorator
{
    private static final Logger LOG = Logger.getLogger(ExternalImportKeyCellDecorator.class.getName());


    public String decorate(int position, Map<Integer, String> srcLine)
    {
        String externalImportKey = srcLine.get(Integer.valueOf(position));
        String systemID = getColumnDescriptor().getDescriptorData().getModifier("sourceSystemId");
        FlexibleSearchService flexibleSearchService = (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService");
        Map<String, String> params = new HashMap<>();
        params.put("sourceKey", externalImportKey);
        params.put("systemID", systemID);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {targetPK} FROM {ExternalImportKey} WHERE {sourceKey}=?sourceKey AND {sourceSystemID}=?systemID", params);
        List<ItemModel> result = flexibleSearchService.search(query).getResult();
        if(result.size() > 1)
        {
            Logs.debug(LOG, () -> "Given ExternalImportKey (" + externalImportKey + ") is not unique.");
            throw new AmbiguousIdentifierException("Given ExternalImportKey (" + externalImportKey + ") is not unique.");
        }
        if(result.isEmpty())
        {
            Logs.debug(LOG, () -> "Given ExternalImportKey (" + externalImportKey + ") not found.");
        }
        else
        {
            PK processedItemPk = ((ItemModel)result.get(0)).getPk();
            Logs.debug(LOG, () -> "Given ExternalImportKey (" + externalImportKey + ") points to local PK (" + processedItemPk.getLongValueAsString() + ").");
            ValueLineMetaData metaData = ValueLineMetaData.builder().withProcessedItemPK(processedItemPk).build();
            srcLine.put(Integer.valueOf(0), metaData.dump());
        }
        if(externalImportKey.startsWith("<ignore>"))
        {
            return externalImportKey;
        }
        return "<ignore>" + externalImportKey;
    }
}
