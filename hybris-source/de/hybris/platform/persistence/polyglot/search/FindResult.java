package de.hybris.platform.persistence.polyglot.search;

import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import java.util.stream.Stream;

public interface FindResult
{
    int getCount();


    int getTotalCount();


    Stream<ItemStateView> getResult();
}
