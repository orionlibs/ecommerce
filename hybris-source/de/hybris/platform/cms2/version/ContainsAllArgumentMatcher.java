package de.hybris.platform.cms2.version;

import java.util.Collection;
import org.mockito.ArgumentMatcher;

public class ContainsAllArgumentMatcher<T> implements ArgumentMatcher<Collection>
{
    private T[] elems;


    public ContainsAllArgumentMatcher(T... elems)
    {
        this.elems = elems;
    }


    public boolean matches(Collection collection)
    {
        return (collection.size() == this.elems.length &&
                        containsAll(collection));
    }


    protected boolean containsAll(Collection collection)
    {
        for(T singleElem : this.elems)
        {
            if(!collection.contains(singleElem))
            {
                return false;
            }
        }
        return true;
    }
}
