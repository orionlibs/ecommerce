package de.hybris.platform.platformbackoffice.widgets.workflowactionadapter;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@Deprecated(since = "6.6", forRemoval = true)
public class WorkflowActionAdapterController extends AbstractInitAdvancedSearchAdapter
{
    protected static final String BACKOFFICE_WFL_INBOX_ID = "hmc_backoffice-workflow-inbox";
    @Deprecated(since = "6.5", forRemoval = true)
    protected static final String WORKFLOW_ACTION_TYPE_CODE = "WorkflowAction";
    protected static final String WORKFLOW_ACTION_PRINCIPAL_ASSIGNED_ATTR = "principalAssigned";
    protected static final String SOCKET_INPUT_STATUSES = "statuses";
    protected static final String SOCKET_OUTPUT_CONTEXT = "context";
    protected static final String MODEL_SELECTED_STATUSES = "selectedStatuses";
    protected static final String MODEL_NAVIGATION_NODE = "navigationNode";
    protected transient CockpitUserService cockpitUserService;
    protected transient UserService userService;


    @SocketEvent(socketId = "nodeSelected")
    public void createAdvancedSearchInitContext(NavigationNode navigationNode)
    {
        setValue("navigationNode", navigationNode);
        super.createAdvancedSearchInitContext(navigationNode);
    }


    @SocketEvent(socketId = "statuses")
    public void setSelectedStatuses(Set<Object> statuses)
    {
        setValue("selectedStatuses", statuses);
        NavigationNode navigationNode = (NavigationNode)getValue("navigationNode", NavigationNode.class);
        if(navigationNode != null)
        {
            super.createAdvancedSearchInitContext(navigationNode);
        }
    }


    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> optional)
    {
        addUserCondition(searchData);
        addStatusCondition(searchData);
    }


    protected void addUserCondition(AdvancedSearchData searchData)
    {
        clearConditionsForAttribute(searchData, "principalAssigned");
        UserModel currentUser = getCurrentUser();
        FieldType fieldType = createFieldType("principalAssigned");
        List<SearchConditionData> conditions = new ArrayList<>();
        SearchConditionData currentUserCondition = new SearchConditionData(fieldType, currentUser, ValueComparisonOperator.EQUALS);
        conditions.add(currentUserCondition);
        Set<UserGroupModel> groups = getUserService().getAllUserGroupsForUser(currentUser);
        for(UserGroupModel currentGroup : groups)
        {
            SearchConditionData userGroupCondition = new SearchConditionData(fieldType, currentGroup, ValueComparisonOperator.EQUALS);
            conditions.add(userGroupCondition);
        }
        searchData.addFilterQueryRawConditionsList(ValueComparisonOperator.OR, conditions);
    }


    protected void clearConditionsForAttribute(AdvancedSearchData searchData, String attribute)
    {
        List<SearchConditionData> conditions = searchData.getConditions(attribute);
        if(CollectionUtils.isNotEmpty(conditions))
        {
            conditions.clear();
        }
    }


    protected FieldType createFieldType(String fieldName)
    {
        FieldType fieldType = new FieldType();
        fieldType.setDisabled(Boolean.TRUE);
        fieldType.setSelected(Boolean.TRUE);
        fieldType.setName(fieldName);
        return fieldType;
    }


    protected void addStatusCondition(AdvancedSearchData searchData)
    {
        clearConditionsForAttribute(searchData, "status");
        Set<?> statuses = (Set)getValue("selectedStatuses", Set.class);
        if(CollectionUtils.isNotEmpty(statuses))
        {
            FieldType fieldType = createFieldType("status");
            List<SearchConditionData> conditions = new ArrayList<>();
            for(Object status : statuses)
            {
                conditions.add(new SearchConditionData(fieldType, status, ValueComparisonOperator.EQUALS));
            }
            searchData.addFilterQueryRawConditionsList(ValueComparisonOperator.OR, conditions);
        }
    }


    protected UserModel getCurrentUser()
    {
        UserModel userModel = null;
        String userId = getCockpitUserService().getCurrentUser();
        if(StringUtils.isNotBlank(userId))
        {
            userModel = getUserService().getUserForUID(userId);
        }
        return userModel;
    }


    public String getTypeCode()
    {
        return "WorkflowAction";
    }


    public String getNavigationNodeId()
    {
        return "hmc_backoffice-workflow-inbox";
    }


    protected String getOutputSocketName()
    {
        return "context";
    }


    protected CockpitUserService getCockpitUserService()
    {
        return this.cockpitUserService;
    }


    @WireVariable
    public void setCockpitUserService(CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @WireVariable
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
