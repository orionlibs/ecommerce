package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class DefaultOrphanedMediaDao extends DefaultMediaDao implements OrphanedMediaDao
{
    private final StringBuilder query = new StringBuilder("SELECT {" + Item.PK + "}FROM {Media} WHERE {" + Media.PK + "}= ?myPK OR {dataPK}=?myPK");


    public Collection findOrphanedMedias(int start, int count, Map<String, Object> values)
    {
        FlexibleSearchQuery fsq = new FlexibleSearchQuery(this.query.toString(), values);
        fsq.setCount(count);
        fsq.setStart(start);
        SearchResult res = getFlexibleSearchService().search(fsq);
        return (res.getTotalCount() == 0) ? Collections.EMPTY_LIST : res.getResult();
    }


    public int getMediasCount()
    {
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("select {PK} from {Media}", Collections.EMPTY_MAP);
        fsq.setCount(-1);
        fsq.setStart(-1);
        return getFlexibleSearchService().search(fsq).getTotalCount();
    }
}
