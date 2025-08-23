package de.hybris.bootstrap.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class RequirementSolver
{
    public static List<List<? extends RequirementHolder>> solveParallel(Collection<? extends RequirementHolder> elements) throws RequirementSolverException
    {
        List<? extends RequirementHolder> orig = solve(elements);
        List<List<? extends RequirementHolder>> result = new LinkedList<>();
        if(!orig.isEmpty())
        {
            result.add(new LinkedList<>());
        }
        for(RequirementHolder h : orig)
        {
            List<RequirementHolder> zwi = ((LinkedList)result).getLast();
            boolean dep = false;
            for(RequirementHolder zwH : zwi)
            {
                if(h.getRequirements().contains(zwH))
                {
                    dep = true;
                }
            }
            if(dep)
            {
                zwi = new ArrayList();
                zwi.add(h);
                result.add(zwi);
                continue;
            }
            zwi.add(h);
        }
        return result;
    }


    public static <T extends RequirementHolder> List<T> solve(Collection<T> elements) throws RequirementSolverException
    {
        Digraph graph1 = new Digraph();
        for(RequirementHolder rh : elements)
        {
            graph1.addVertex(rh);
        }
        for(RequirementHolder source : elements)
        {
            for(RequirementHolder required : source.getRequirements())
            {
                graph1.addEdge(required, source);
            }
        }
        graph1.freeze();
        Digraph graph2 = new Digraph();
        List<?> resortedVertexes = graph1.idsByDFSFinishTime(false);
        for(Object o : resortedVertexes)
        {
            graph2.addVertex(o);
        }
        for(Object o : resortedVertexes)
        {
            for(RequirementHolder required : ((RequirementHolder)o).getRequirements())
            {
                graph2.addEdge(o, required);
            }
        }
        graph2.freeze();
        List<?> sortedExtensionList = graph2.idsByDFSFinishTime(true);
        if(graph2.containsCycles())
        {
            throw new RequirementSolverException("Definition shows a cycle dependency.");
        }
        return Collections.unmodifiableList(new ArrayList<>((Collection)sortedExtensionList));
    }
}
