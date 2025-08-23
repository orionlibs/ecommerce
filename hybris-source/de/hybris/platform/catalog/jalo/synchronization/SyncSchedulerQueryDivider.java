package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.jalo.type.ComposedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class SyncSchedulerQueryDivider
{
    private final String header;
    private final String footer;
    private final Function<String, String> queryForType;
    private final ComposedType type;


    SyncSchedulerQueryDivider(ComposedType type, String header, String footer, Function<String, String> queryForType)
    {
        this.type = type;
        this.header = header;
        this.footer = footer;
        this.queryForType = queryForType;
    }


    public static SyncSchedulerQueryDividerBuilder buildForCoreQuery(Function<String, String> queryForType)
    {
        return new SyncSchedulerQueryDividerBuilder(queryForType);
    }


    public String generateQueryWithUnion()
    {
        List<String> singleQueryList = generateQueryListToConnectWithUnion();
        return generateUnionAllQueryForSingleQueries(singleQueryList);
    }


    List<String> generateQueryListToConnectWithUnion()
    {
        List<String> simpleQueries = new ArrayList<>();
        List<ComposedType> composedTypesToGenerateQuery = new ArrayList<>();
        if(this.type.getAllConcreteItemTypeCodes().isEmpty() || this.type.getSubTypes().isEmpty())
        {
            simpleQueries.add(this.queryForType.apply(this.type.getCode()));
        }
        else
        {
            findComposedTypesToConnectWithUnion(this.type, composedTypesToGenerateQuery, false);
            composedTypesToGenerateQuery.stream().forEach(ct -> simpleQueries.add(this.queryForType.apply(ct.getCode() + "^")));
        }
        return simpleQueries;
    }


    String generateUnionAllQueryForSingleQueries(List<String> queryList)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.header);
        Iterator<String> it = queryList.iterator();
        while(it.hasNext())
        {
            builder.append("{{" + it.next() + "}}");
            if(it.hasNext())
            {
                builder.append(" UNION ALL ");
            }
        }
        builder.append(this.footer);
        return builder.toString();
    }


    private void findComposedTypesToConnectWithUnion(ComposedType c, List<ComposedType> composedTypesToGenerteQuery, boolean typeForQueryInBranchIsDefined)
    {
        if(c.hasOwnDeployment() || (!typeForQueryInBranchIsDefined &&
                        verifyIfAnyNonAbstractChildNodeExistWithoutDeployment(c)))
        {
            composedTypesToGenerteQuery.add(c);
            typeForQueryInBranchIsDefined = true;
        }
        if(c.getSubTypes().isEmpty() || (checkIfSubtypesHaveNoDeployment(c) && typeForQueryInBranchIsDefined == true))
        {
            return;
        }
        for(ComposedType cc : c.getSubTypes())
        {
            findComposedTypesToConnectWithUnion(cc, composedTypesToGenerteQuery, typeForQueryInBranchIsDefined);
        }
    }


    private boolean verifyIfAnyNonAbstractChildNodeExistWithoutDeployment(ComposedType c)
    {
        for(ComposedType child : c.getSubTypes())
        {
            if(!child.isAbstract() && !child.hasOwnDeployment())
            {
                return true;
            }
            if(child.isAbstract() && !child.hasOwnDeployment())
            {
                return verifyIfAnyNonAbstractChildNodeExistWithoutDeployment(child);
            }
        }
        return false;
    }


    private boolean checkIfSubtypesHaveNoDeployment(ComposedType c)
    {
        return !c.getAllSubTypes().stream().anyMatch(ComposedType::hasOwnDeployment);
    }
}
