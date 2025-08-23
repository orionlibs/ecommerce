package de.hybris.platform.util.localization.jdbc;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.jdbc.rows.LocalizableAttributeRow;
import de.hybris.platform.util.localization.jdbc.rows.queries.LocalizableAtomicTypeRowsQuery;
import de.hybris.platform.util.localization.jdbc.rows.queries.LocalizableAttributeRowsQuery;
import de.hybris.platform.util.localization.jdbc.rows.queries.LocalizableCollectionTypeRowsQuery;
import de.hybris.platform.util.localization.jdbc.rows.queries.LocalizableComposedTypeRowsQuery;
import de.hybris.platform.util.localization.jdbc.rows.queries.LocalizableEnumerationValueRowsQuery;
import de.hybris.platform.util.localization.jdbc.rows.queries.LocalizableMapTypeRowsQuery;
import de.hybris.platform.util.localization.jdbc.rx.functions.LocalizationRowsToDmlStatements;
import de.hybris.platform.util.localization.jdbc.rx.operators.GroupByItemPKValue;
import de.hybris.platform.util.localization.jdbc.rx.subscriptions.LocalizableRowsSubscription;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import javax.sql.DataSource;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class JdbcBasedTypeSystemLocalization
{
    static final String IS_LOCALIZATION_FILTERING_ENABLED = "init.localization.filter.enabled";
    private final ExecutorService executorService;
    private final DataSource dataSource;
    private final DbInfo dbInfo;
    private final LocalizationRowsToDmlStatements localizationRowsToDmlStatements;
    private final StatementsExecutor statementsExecutor;


    public JdbcBasedTypeSystemLocalization(ExecutorService executorService, DataSource dataSource, DbInfo dbInfo, LocalizationInfo localizationInfo, StatementsExecutor statementsExecutor)
    {
        Preconditions.checkNotNull(executorService, "executorService can't be null");
        Preconditions.checkNotNull(dataSource, "dataSource can't be null");
        Preconditions.checkNotNull(dbInfo, "dbInfo can't be null");
        Preconditions.checkNotNull(localizationInfo, "localizationInfo can't be null");
        Preconditions.checkNotNull(statementsExecutor, "statementsExecutor can't be null");
        this.executorService = executorService;
        this.dataSource = dataSource;
        this.dbInfo = dbInfo;
        this.localizationRowsToDmlStatements = new LocalizationRowsToDmlStatements(localizationInfo);
        this.statementsExecutor = statementsExecutor;
    }


    public void localizeTypeSystem()
    {
        getLocalizationStatementsToExecute().buffer(1000).toBlocking().forEach((Action1)new Object(this));
        this.statementsExecutor.flush();
        invalidateCache();
    }


    private Observable<StatementWithParams> getLocalizationStatementsToExecute()
    {
        ImmutableList immutableList = ImmutableList.of(
                        getAttributesLocalizationStatements(),
                        getComposedTypesLocalizationStatements(),
                        getAtomicTypesLocalizationStatements(),
                        getCollectionTypesLocalizationStatements(),
                        getMapTypesLocalizationStatements(),
                        getEnumerationValuesLocalizationStatements());
        return Observable.merge((Iterable)immutableList).filter(toNonEmptyStatements());
    }


    private Observable<StatementWithParams> getAttributesLocalizationStatements()
    {
        return Observable.create((Observable.OnSubscribe)createAttributesSubscription())
                        .filter(localizableAttributeRowFilters())
                        .lift((Observable.Operator)new GroupByItemPKValue())
                        .flatMap(this.localizationRowsToDmlStatements.withNameAndDescription());
    }


    private Observable<StatementWithParams> getComposedTypesLocalizationStatements()
    {
        return Observable.create((Observable.OnSubscribe)createComposedTypesSubscription()).lift((Observable.Operator)new GroupByItemPKValue())
                        .flatMap(this.localizationRowsToDmlStatements.withNameAndDescription());
    }


    private Observable<StatementWithParams> getAtomicTypesLocalizationStatements()
    {
        return Observable.create((Observable.OnSubscribe)createAtomicTypesSubscription()).lift((Observable.Operator)new GroupByItemPKValue())
                        .flatMap(this.localizationRowsToDmlStatements.withNameAndDescription());
    }


    private Observable<StatementWithParams> getCollectionTypesLocalizationStatements()
    {
        return Observable.create((Observable.OnSubscribe)createCollectionTypesSubscription()).lift((Observable.Operator)new GroupByItemPKValue())
                        .flatMap(this.localizationRowsToDmlStatements.withNameAndDescription());
    }


    private Observable<StatementWithParams> getMapTypesLocalizationStatements()
    {
        return Observable.create((Observable.OnSubscribe)createMapTypesSubscription()).lift((Observable.Operator)new GroupByItemPKValue())
                        .flatMap(this.localizationRowsToDmlStatements.withNameAndDescription());
    }


    private Observable<StatementWithParams> getEnumerationValuesLocalizationStatements()
    {
        return Observable.create((Observable.OnSubscribe)createEnumerationValuesSubscription()).lift((Observable.Operator)new GroupByItemPKValue())
                        .flatMap(this.localizationRowsToDmlStatements.withName());
    }


    private LocalizableRowsSubscription createAttributesSubscription()
    {
        return new LocalizableRowsSubscription(this.dataSource, this.executorService, (LocalizableRowsQuery)new LocalizableAttributeRowsQuery(this.dbInfo));
    }


    private LocalizableRowsSubscription createComposedTypesSubscription()
    {
        return new LocalizableRowsSubscription(this.dataSource, this.executorService, (LocalizableRowsQuery)new LocalizableComposedTypeRowsQuery(this.dbInfo));
    }


    private LocalizableRowsSubscription createAtomicTypesSubscription()
    {
        return new LocalizableRowsSubscription(this.dataSource, this.executorService, (LocalizableRowsQuery)new LocalizableAtomicTypeRowsQuery(this.dbInfo));
    }


    private LocalizableRowsSubscription createCollectionTypesSubscription()
    {
        return new LocalizableRowsSubscription(this.dataSource, this.executorService, (LocalizableRowsQuery)new LocalizableCollectionTypeRowsQuery(this.dbInfo));
    }


    private LocalizableRowsSubscription createMapTypesSubscription()
    {
        return new LocalizableRowsSubscription(this.dataSource, this.executorService, (LocalizableRowsQuery)new LocalizableMapTypeRowsQuery(this.dbInfo));
    }


    private LocalizableRowsSubscription createEnumerationValuesSubscription()
    {
        return new LocalizableRowsSubscription(this.dataSource, this.executorService, (LocalizableRowsQuery)new LocalizableEnumerationValueRowsQuery(this.dbInfo));
    }


    private Func1<StatementWithParams, Boolean> toNonEmptyStatements()
    {
        return (Func1<StatementWithParams, Boolean>)new Object(this);
    }


    private Func1 localizableAttributeRowFilters()
    {
        boolean enableAttributesFiltering = Config.getBoolean("init.localization.filter.enabled", false);
        return row -> {
            if(!(row instanceof LocalizableAttributeRow) || !enableAttributesFiltering)
            {
                return Boolean.valueOf(true);
            }
            LocalizableAttributeRow localizableAttributeRow = (LocalizableAttributeRow)row;
            Collection<LocalizableAttributeRowFilter> filters = Registry.getApplicationContext().getBeansOfType(LocalizableAttributeRowFilter.class).values();
            return Boolean.valueOf(filters.stream().allMatch(()));
        };
    }


    private void invalidateCache()
    {
        Registry.getCurrentTenant().getCache().clear();
    }
}
