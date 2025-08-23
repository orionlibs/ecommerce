package de.hybris.platform.servicelayer.cronjob.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.CronJobHistory;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryDao;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryInclude;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.FlexibleSearchUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class DefaultCronJobHistoryDao extends DefaultGenericDao implements CronJobHistoryDao
{
    public static final String COMP_EQ = "=";
    public static final String COMP_GT_EQ = ">=";
    public static final String COMP_LT_EQ = "<=";
    private TypeService typeService;


    public DefaultCronJobHistoryDao()
    {
        super("CronJobHistory");
    }


    public List<CronJobHistoryModel> findCronJobHistoryBy(String cronJobCode)
    {
        Preconditions.checkArgument((cronJobCode != null), "Code must not be null!");
        Map<String, String> arguments = new HashMap<>();
        arguments.put("cronJobCode", cronJobCode);
        return find(arguments);
    }


    public List<CronJobHistoryModel> findCronJobHistoryBy(String userUid, String jobCode)
    {
        Map<String, String> arguments = new HashMap<>();
        if(StringUtils.isNotBlank(userUid))
        {
            arguments.put("userUid", userUid);
        }
        if(StringUtils.isNotBlank(jobCode))
        {
            arguments.put("jobCode", jobCode);
        }
        return find(arguments);
    }


    public List<CronJobHistoryModel> findCronJobHistoryBy(String userUid, String jobTypeCode, Date startDate, Date finishDate)
    {
        return findCronJobHistoryBy(userUid, jobTypeCode, startDate, finishDate, null, null);
    }


    public List<CronJobHistoryModel> findCronJobHistoryBy(String userUid, String jobTypeCode, Date startDate, Date finishDate, CronJobStatus theStatus)
    {
        return findCronJobHistoryBy(userUid, jobTypeCode, startDate, finishDate, theStatus, null);
    }


    public List<CronJobHistoryModel> findCronJobHistoryBy(String userUid, String jobTypeCode, Date startDate, Date finishDate, CronJobResult theResult)
    {
        return findCronJobHistoryBy(userUid, jobTypeCode, startDate, finishDate, null, theResult);
    }


    public List<CronJobHistoryModel> findCronJobHistoryBy(List<String> cronJobCodes)
    {
        ServicesUtil.validateParameterNotNull(cronJobCodes, "Code must not be null!");
        Map<String, Object> arguments = new HashMap<>();
        if(CollectionUtils.isNotEmpty(cronJobCodes))
        {
            arguments.put("codes", cronJobCodes);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT ");
        queryBuilder.append(" { ").append("pk").append(" } ");
        queryBuilder.append(" FROM {").append("CronJobHistory").append(" } ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{cronJobCode } in (?codes)", "codes", "OR", cronJobCodes, arguments));
        SearchResult<CronJobHistoryModel> res = getFlexibleSearchService().search(queryBuilder.toString(), arguments);
        return res.getResult();
    }


    public List<CronJobHistoryModel> findLastCronJobHistoryByCronJobs(List<CronJobModel> cronJobs, int limit)
    {
        List<PK> cronJobPks = (List<PK>)cronJobs.stream().map(AbstractItemModel::getPk).collect(Collectors.toList());
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("cronJobs", cronJobPks);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT ");
        queryBuilder.append(" { ").append("pk").append(" } ");
        queryBuilder.append(" FROM {").append("CronJobHistory").append(" } ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{cronJob } in (?cronJobs)", "cronJobs", "OR", cronJobPks, arguments));
        queryBuilder.append(" ORDER BY {").append(CronJobHistory.CREATION_TIME).append("} DESC");
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(queryBuilder);
        flexibleSearchQuery.setDisableCaching(true);
        flexibleSearchQuery.setCount(limit);
        flexibleSearchQuery.addQueryParameters(arguments);
        SearchResult<CronJobHistoryModel> res = getFlexibleSearchService().search(flexibleSearchQuery);
        return res.getResult();
    }


    protected List<CronJobHistoryModel> findCronJobHistoryBy(String userUid, String jobTypeCode, Date startDate, Date finishDate, CronJobStatus theStatus, CronJobResult theResult)
    {
        Set<CronJobHistoryInclude> includes = new HashSet<>();
        includes.add(new CronJobHistoryInclude(null, jobTypeCode, null));
        return findCronJobHistoryBy(includes, userUid, startDate, finishDate, theStatus, theResult);
    }


    public List<CronJobHistoryModel> findCronJobHistoryBy(Set<CronJobHistoryInclude> includes, String userUid, Date startDate, Date finishDate, CronJobStatus theStatus, CronJobResult theResult)
    {
        if(CollectionUtils.isEmpty(includes))
        {
            return Collections.emptyList();
        }
        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT ");
        queryBuilder.append("history.").append("pk").append(", history.").append("startTime")
                        .append(" FROM (");
        Iterator<CronJobHistoryInclude> iterator = includes.iterator();
        Map<String, Object> arguments = new HashMap<>();
        while(iterator.hasNext())
        {
            CronJobHistoryInclude include = iterator.next();
            queryBuilder.append("{{");
            queryBuilder.append(prepareQuery(userUid, startDate, finishDate, theStatus, theResult, include, arguments));
            queryBuilder.append("}}");
            if(iterator.hasNext())
            {
                queryBuilder.append(" UNION ALL ");
            }
        }
        queryBuilder.append(") history ORDER BY history.").append("startTime");
        return getFlexibleSearchService().search(queryBuilder.toString(), arguments).getResult();
    }


    protected String prepareQuery(String userUid, Date startDate, Date finishDate, CronJobStatus theStatus, CronJobResult theResult, CronJobHistoryInclude include, Map<String, Object> arguments)
    {
        assureTypeAssignable(include.getJobTypeCode(), "Job");
        assureTypeAssignable(include.getCronJobTypeCode(), "CronJob");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT ");
        queryBuilder.append(" { cjh.").append("pk").append(" }, ");
        queryBuilder.append(" { cjh.")
                        .append("startTime")
                        .append(" } as ")
                        .append("startTime");
        queryBuilder.append(" FROM {").append("CronJobHistory").append(" as cjh  ");
        if(StringUtils.isNotBlank(include.getJobTypeCode()))
        {
            queryBuilder.append(" LEFT JOIN ").append(include.getJobTypeCode()).append(" as j ON {cjh:jobCode}={j:code} ");
        }
        if(StringUtils.isNotBlank(include.getCronJobTypeCode()))
        {
            queryBuilder.append(" LEFT JOIN ")
                            .append(include.getCronJobTypeCode())
                            .append(" as c ON {cjh:cronJobCode}={c:code} ");
        }
        queryBuilder.append("}");
        StringBuilder whereClause = new StringBuilder();
        addCondition(whereClause, arguments, "cjh.".concat("userUid"), userUid, "=");
        addCondition(whereClause, arguments, "cjh.".concat("jobCode"), include.getJobCodes());
        addCondition(whereClause, arguments, "cjh.".concat("startTime"), startDate, ">=");
        addCondition(whereClause, arguments, "cjh.".concat("endTime"), finishDate, "<=");
        addCondition(whereClause, arguments, "cjh.".concat("status"), theStatus, "=");
        addCondition(whereClause, arguments, "cjh.".concat("result"), theResult, "=");
        if(StringUtils.isNotEmpty(whereClause.toString()))
        {
            queryBuilder.append(" WHERE ");
            queryBuilder.append(whereClause);
        }
        return queryBuilder.toString();
    }


    protected void addCondition(StringBuilder whereClause, Map<String, Object> arguments, String fieldName, Collection<?> inValues)
    {
        if(inValues == null)
        {
            return;
        }
        if(inValues.size() > 1)
        {
            if(whereClause.length() > 0)
            {
                whereClause.append(" AND ");
            }
            String argName = "arg" + arguments.size();
            whereClause.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{ " + fieldName + " } in (?" + argName + ")", argName, "OR", inValues, arguments));
        }
        else
        {
            addCondition(whereClause, arguments, fieldName, inValues.iterator().next(), "=");
        }
    }


    protected void addCondition(StringBuilder whereClause, Map<String, Object> arguments, String fieldName, Object fieldValue, String comparator)
    {
        if(fieldValue != null)
        {
            if(whereClause.length() > 0)
            {
                whereClause.append(" AND ");
            }
            String argName = "arg" + arguments.size();
            arguments.put(argName, fieldValue);
            whereClause.append("{ ").append(fieldName).append(" } ").append(comparator).append(" ?").append(argName);
        }
    }


    protected void assureTypeAssignable(String cronJobTypeCode, String typeCode)
    {
        String normalizedCronJobTypeCode = StringUtils.removeEnd(cronJobTypeCode, "!");
        if(StringUtils.isNotBlank(normalizedCronJobTypeCode) && !this.typeService.isAssignableFrom(typeCode, normalizedCronJobTypeCode))
        {
            throw new IllegalArgumentException(String.format("Given %s must be type of Job or its subtype !!!", new Object[] {typeCode}));
        }
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
