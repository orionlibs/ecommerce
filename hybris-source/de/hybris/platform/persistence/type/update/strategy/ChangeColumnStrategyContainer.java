package de.hybris.platform.persistence.type.update.strategy;

import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.jdbc.DBColumn;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class ChangeColumnStrategyContainer implements ChangeColumnStrategy
{
    private List<ChangeColumnStrategy> oracleStrategies;
    private List<ChangeColumnStrategy> mySqlStrategies;
    private List<ChangeColumnStrategy> sqlServerStrategies;
    private List<ChangeColumnStrategy> hsqlDbStrategies;
    private List<ChangeColumnStrategy> commonStrategies;


    public boolean doChangeColumn(String targetDefinition, DBColumn originalColumnDef, AttributeDescriptorRemote attributeDescr)
    {
        if(Config.isOracleUsed() && tryStrategies(targetDefinition, originalColumnDef, attributeDescr, this.oracleStrategies))
        {
            return true;
        }
        if(Config.isMySQLUsed() && tryStrategies(targetDefinition, originalColumnDef, attributeDescr, this.mySqlStrategies))
        {
            return true;
        }
        if(Config.isHSQLDBUsed() && tryStrategies(targetDefinition, originalColumnDef, attributeDescr, this.hsqlDbStrategies))
        {
            return true;
        }
        if(Config.isSQLServerUsed() &&
                        tryStrategies(targetDefinition, originalColumnDef, attributeDescr, this.sqlServerStrategies))
        {
            return true;
        }
        return tryStrategies(targetDefinition, originalColumnDef, attributeDescr, this.commonStrategies);
    }


    private boolean tryStrategies(String targetDefinition, DBColumn originalColumnDef, AttributeDescriptorRemote attributeDescr, List<ChangeColumnStrategy> strategies)
    {
        if(CollectionUtils.isNotEmpty(strategies))
        {
            for(ChangeColumnStrategy strategy : strategies)
            {
                if(strategy.doChangeColumn(targetDefinition, originalColumnDef, attributeDescr))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public void setOracleStrategies(List<ChangeColumnStrategy> oracleStrategies)
    {
        this.oracleStrategies = oracleStrategies;
    }


    public void setMySqlStrategies(List<ChangeColumnStrategy> mySqlStrategies)
    {
        this.mySqlStrategies = mySqlStrategies;
    }


    public List<ChangeColumnStrategy> getHsqlDbStrategies()
    {
        return this.hsqlDbStrategies;
    }


    public List<ChangeColumnStrategy> getCommonStrategies()
    {
        return this.commonStrategies;
    }


    public void setCommonStrategies(List<ChangeColumnStrategy> commonStrategies)
    {
        this.commonStrategies = commonStrategies;
    }


    public void setSqlServerStrategies(List<ChangeColumnStrategy> sqlServerStrategies)
    {
        this.sqlServerStrategies = sqlServerStrategies;
    }
}
