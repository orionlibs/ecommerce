package de.hybris.platform.jalo.flexiblesearch.hints.impl;

import com.google.common.base.Suppliers;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.flexiblesearch.hints.PreparedStatementHint;
import de.hybris.platform.util.Config;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

public class DBSpecificStreamingHints implements PreparedStatementHint
{
    private static final int DISABLE_FETCHSIZE = -1;
    private static final Supplier<DBSpecificStreamingHints> instance = (Supplier<DBSpecificStreamingHints>)Suppliers.memoize(() -> new DBSpecificStreamingHints(Registry.getCurrentTenant().getDataSource().getDatabaseName()));
    private final Integer fetchType;
    private boolean disableAutoCommmit = false;
    private int fetchSize;


    private DBSpecificStreamingHints(String databaseName)
    {
        setDbSpecificOptions(Config.DatabaseName.fromString(databaseName));
        this.fetchType = Integer.valueOf(1000);
    }


    public static DBSpecificStreamingHints getInstance()
    {
        return instance.get();
    }


    public PreparedStatement apply(PreparedStatement ps) throws SQLException
    {
        if(this.fetchSize != -1)
        {
            ps.setFetchSize(this.fetchSize);
        }
        ps.setFetchDirection(this.fetchType.intValue());
        return ps;
    }


    private void setDbSpecificOptions(Config.DatabaseName dbName)
    {
        switch(null.$SwitchMap$de$hybris$platform$util$Config$DatabaseName[dbName.ordinal()])
        {
            case 1:
                this.fetchSize = Integer.MIN_VALUE;
                this.disableAutoCommmit = false;
                return;
            case 2:
                this.fetchSize = -1;
                this.disableAutoCommmit = false;
                return;
            case 3:
                this.fetchSize = 1;
                this.disableAutoCommmit = true;
                return;
            case 4:
            case 5:
                this.fetchSize = 1000;
                this.disableAutoCommmit = true;
                return;
            case 6:
                this.fetchSize = 100;
                this.disableAutoCommmit = true;
                return;
        }
        this.fetchSize = 1;
        this.disableAutoCommmit = true;
    }


    public boolean needsAutoCommitDisabled()
    {
        return this.disableAutoCommmit;
    }
}
