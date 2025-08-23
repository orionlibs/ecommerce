package de.hybris.platform.cockpit.model.advancedsearch;

import de.hybris.platform.cockpit.model.search.Operator;
import java.util.List;

public interface ConditionValue
{
    List<Object> getValues();


    Operator getOperator();


    boolean isLinkedByOR();
}
