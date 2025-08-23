package de.hybris.bootstrap.loader.metrics;

import java.util.List;

public class ClasspathReorderingResult
{
    private final List<String> classpath;
    private final int classpathTraversalCost;
    private final List<String> optimizedClasspath;
    private final int optimizedClasspathTraversalCost;


    public ClasspathReorderingResult(List<String> classpath, int classpathTraversalCost, List<String> optimizedClasspath, int optimizedClasspathTraversalCost)
    {
        this.classpath = classpath;
        this.classpathTraversalCost = classpathTraversalCost;
        this.optimizedClasspath = optimizedClasspath;
        this.optimizedClasspathTraversalCost = optimizedClasspathTraversalCost;
    }


    public List<String> getClasspath()
    {
        return this.classpath;
    }


    public int getClasspathTraversalCost()
    {
        return this.classpathTraversalCost;
    }


    public List<String> getOptimizedClasspath()
    {
        return this.optimizedClasspath;
    }


    public int getOptimizedClasspathTraversalCost()
    {
        return this.optimizedClasspathTraversalCost;
    }


    public String toString()
    {
        return "ClasspathReorderingResult{classpath=" + this.classpath + ", classpathTraversalCost=" + this.classpathTraversalCost + ", optimizedClasspath=" + this.optimizedClasspath + ", optimizedClasspathTraversalCost=" + this.optimizedClasspathTraversalCost + "}";
    }
}
