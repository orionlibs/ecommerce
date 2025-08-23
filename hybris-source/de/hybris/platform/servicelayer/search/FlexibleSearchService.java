package de.hybris.platform.servicelayer.search;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.SessionContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface FlexibleSearchService
{
    default <T> void processSearchRows(FlexibleSearchQuery searchQuery, Consumer<T> rowConsumer)
    {
        search(searchQuery).getResult().forEach(row -> rowConsumer.accept(row));
    }


    default <T> void processSearchRows(FlexibleSearchQuery searchQuery, Predicate<T> rowConsumer)
    {
        Optional<T> ignore = search(searchQuery).getResult().stream().map(row -> row).filter(rowConsumer.negate()).findFirst();
    }


    default Optional<Boolean> isReadOnlyDataSourceEnabled()
    {
        return Optional.empty();
    }


    default Optional<Boolean> isReadOnlyDataSourceEnabled(SessionContext ctx)
    {
        return Optional.empty();
    }


    <T> T getModelByExample(T paramT);


    <T> List<T> getModelsByExample(T paramT);


    <T> SearchResult<T> search(FlexibleSearchQuery paramFlexibleSearchQuery);


    <T> SearchResult<T> search(String paramString);


    <T> SearchResult<T> search(String paramString, Map<String, ? extends Object> paramMap);


    <T> SearchResult<T> searchRelation(ItemModel paramItemModel, String paramString, int paramInt1, int paramInt2);


    <T> SearchResult<T> searchRelation(RelationQuery paramRelationQuery);


    <T> T searchUnique(FlexibleSearchQuery paramFlexibleSearchQuery);


    TranslationResult translate(FlexibleSearchQuery paramFlexibleSearchQuery);
}
