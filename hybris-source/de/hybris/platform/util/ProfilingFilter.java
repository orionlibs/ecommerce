package de.hybris.platform.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ProfilingFilter implements Filter
{
    private static final String TIMER_STACK = "__timer_timerStack";
    private static final String onOffInitParameter = "activate.param";
    private static final String defaultToOnInitParameter = "autostart";
    private static String onOffParameter = "profile.filter";
    private static boolean filterOn = true;
    private FilterConfig filterConfig;


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        String paramValue = request.getParameter(onOffParameter);
        if(paramValue != null)
        {
            if("on".equals(paramValue) || "true".equals(paramValue))
            {
                turnFilterOn();
            }
            else if("off".equals(paramValue) || "off".equals(paramValue))
            {
                turnFilterOff();
            }
        }
        if(!filterOn)
        {
            chain.doFilter(request, response);
            return;
        }
        ProfilingTimerBean timer = new ProfilingTimerBean(this, (HttpServletRequest)request);
        boolean isRootTimer = false;
        Stack<ProfilingTimerBean> timerStack = null;
        if(request.getAttribute("__timer_rootTimer") == null)
        {
            request.setAttribute("__timer_rootTimer", timer);
            isRootTimer = true;
        }
        if(request.getAttribute("__timer_timerStack") != null)
        {
            timerStack = (Stack)request.getAttribute("__timer_timerStack");
        }
        else
        {
            timerStack = new Stack();
            request.setAttribute("__timer_timerStack", timerStack);
        }
        timerStack.push(timer);
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        timer.setTime(System.currentTimeMillis() - start);
        List childTimers = new ArrayList();
        while(!timerStack.empty() && timerStack.peek() != timer)
        {
            childTimers.add(0, timerStack.pop());
        }
        timer.addChildren(childTimers);
        if(isRootTimer)
        {
            System.out.println(timer.getPrintable(""));
        }
    }


    public void setFilterConfig(FilterConfig filterConfig)
    {
        this.filterConfig = filterConfig;
        if(filterConfig.getInitParameter("activate.param") != null)
        {
            System.out.println("[Filter: " + filterConfig.getFilterName() + "] Using parameter [" + filterConfig
                            .getInitParameter("activate.param") + "]");
            onOffParameter = filterConfig.getInitParameter("activate.param");
        }
        if("true".equals(filterConfig.getInitParameter("autostart")))
        {
            System.out.println("[Filter: " + filterConfig.getFilterName() + "] defaulting to on [autostart=true]");
            filterOn = true;
        }
    }


    public FilterConfig getFilterConfig()
    {
        return this.filterConfig;
    }


    public void init(FilterConfig filterConfig) throws ServletException
    {
        setFilterConfig(filterConfig);
    }


    public void destroy()
    {
    }


    private void turnFilterOn()
    {
        System.out.println("[Filter: " + this.filterConfig.getFilterName() + "] Turning filter on [" + onOffParameter + "=on]");
        filterOn = true;
    }


    private void turnFilterOff()
    {
        System.out.println("[Filter: " + this.filterConfig.getFilterName() + "] Turning filter off [" + onOffParameter + "=off]");
        filterOn = false;
    }
}
