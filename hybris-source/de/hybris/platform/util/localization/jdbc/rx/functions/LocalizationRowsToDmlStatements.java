package de.hybris.platform.util.localization.jdbc.rx.functions;

import com.google.common.base.Preconditions;
import de.hybris.platform.util.localization.jdbc.LocalizableRowWithName;
import de.hybris.platform.util.localization.jdbc.LocalizableRowWithNameAndDescription;
import de.hybris.platform.util.localization.jdbc.LocalizationInfo;
import de.hybris.platform.util.localization.jdbc.StatementWithParams;
import rx.Observable;
import rx.functions.Func1;

public class LocalizationRowsToDmlStatements
{
    private static final String INSERT_ONE_PROPERTY_TEMPLATE = "insert into %s(ITEMPK, LANGPK, ITEMTYPEPK, %s) values(?, ?, ?, ?)";
    private static final String UPDATE_ONE_PROPERTY_TEMPLATE = "update %s set %s=? where ITEMPK=? and LANGPK=?";
    private static final String INSERT_TWO_PROPERTIES_TEMPLATE = "insert into %s(ITEMPK, LANGPK, ITEMTYPEPK, %s, %s) values(?, ?, ?, ?, ?)";
    private static final String UPDATE_TWO_PROPERTIES_TEMPLATE = "update %s set %s=?, %s=? where ITEMPK=? and LANGPK=?";
    private final LocalizationInfo localizationInfo;


    public LocalizationRowsToDmlStatements(LocalizationInfo localizationInfo)
    {
        Preconditions.checkNotNull(localizationInfo, "localizationInfo can't be null");
        this.localizationInfo = localizationInfo;
    }


    public Func1<Iterable<LocalizableRowWithName>, Observable<StatementWithParams>> withName()
    {
        return (Func1<Iterable<LocalizableRowWithName>, Observable<StatementWithParams>>)new FromLocalizableRowsWithNameToDmlStatements(this.localizationInfo);
    }


    public Func1<Iterable<LocalizableRowWithNameAndDescription>, Observable<StatementWithParams>> withNameAndDescription()
    {
        return (Func1<Iterable<LocalizableRowWithNameAndDescription>, Observable<StatementWithParams>>)new FromLocalizableRowsWithNameAndDescriptionToDmlStatements(this.localizationInfo);
    }


    private static StatementWithParams getUpdateStatementForTwoProperties(String tableName, String firstColumnName, String secondColumnName, long itemPK, long langPK, String firstLocalizedValue, String secondLocalizedValue)
    {
        if(firstLocalizedValue == null && secondLocalizedValue == null)
        {
            return StatementWithParams.NONE;
        }
        if(firstLocalizedValue == null || secondLocalizedValue == null)
        {
            boolean firstIsNull = (firstLocalizedValue == null);
            if(firstIsNull)
            {
                return getUpdateStatementForOneProperty(tableName, secondColumnName, itemPK, langPK, secondLocalizedValue);
            }
            return getUpdateStatementForOneProperty(tableName, firstColumnName, itemPK, langPK, firstLocalizedValue);
        }
        String sql = String.format("update %s set %s=?, %s=? where ITEMPK=? and LANGPK=?", new Object[] {tableName, firstColumnName, secondColumnName});
        Object[] params = {firstLocalizedValue, secondLocalizedValue, Long.valueOf(itemPK), Long.valueOf(langPK)};
        return new StatementWithParams(sql, params);
    }


    private static StatementWithParams getInsertStatementForTwoProperties(String tableName, String firstColumnName, String secondColumnName, long itemPK, long langPK, long typePK, String firstLocalizedValue, String secondLocalizedValue)
    {
        if(firstLocalizedValue == null && secondLocalizedValue == null)
        {
            return StatementWithParams.NONE;
        }
        if(firstLocalizedValue == null || secondLocalizedValue == null)
        {
            boolean firstIsNull = (firstLocalizedValue == null);
            if(firstIsNull)
            {
                return getInsertStatementForOneProperty(tableName, secondColumnName, itemPK, langPK, typePK, secondLocalizedValue);
            }
            return getInsertStatementForOneProperty(tableName, firstColumnName, itemPK, langPK, typePK, firstLocalizedValue);
        }
        String sql = String.format("insert into %s(ITEMPK, LANGPK, ITEMTYPEPK, %s, %s) values(?, ?, ?, ?, ?)", new Object[] {tableName, firstColumnName, secondColumnName});
        Object[] params = {Long.valueOf(itemPK), Long.valueOf(langPK), Long.valueOf(typePK), firstLocalizedValue, secondLocalizedValue};
        return new StatementWithParams(sql, params);
    }


    private static StatementWithParams getUpdateStatementForOneProperty(String tableName, String columnName, long itemPK, long langPK, String localizedValue)
    {
        if(localizedValue == null)
        {
            return StatementWithParams.NONE;
        }
        String sql = String.format("update %s set %s=? where ITEMPK=? and LANGPK=?", new Object[] {tableName, columnName});
        Object[] params = {localizedValue, Long.valueOf(itemPK), Long.valueOf(langPK)};
        return new StatementWithParams(sql, params);
    }


    private static StatementWithParams getInsertStatementForOneProperty(String tableName, String columnName, long itemPK, long langPK, long typePK, String localizedValue)
    {
        if(localizedValue == null)
        {
            return StatementWithParams.NONE;
        }
        String sql = String.format("insert into %s(ITEMPK, LANGPK, ITEMTYPEPK, %s) values(?, ?, ?, ?)", new Object[] {tableName, columnName});
        Object[] params = {Long.valueOf(itemPK), Long.valueOf(langPK), Long.valueOf(typePK), localizedValue};
        return new StatementWithParams(sql, params);
    }
}
