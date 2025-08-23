package de.hybris.platform.cockpit.util;

import com.google.common.base.Preconditions;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.sys.IdGenerator;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class CockpitTestIDGenerator implements IdGenerator
{
    public static final String COCKPIT_TEST_ID = "cockpitTestID";
    private static final String SEPARATOR = "_";
    private static final String SELENIUM_ID_MAP = "selenium_id_map";
    private static final String SELENIUM_ALLID_MAP = "selenium_all_id_map";
    private static final String LAST_SELENIUM_INDEX_MAP = "lastSeleniumIndex";
    private static final String SELENIUM_I_D = "seleniumID";
    private static AtomicInteger _desktop = new AtomicInteger(0);
    private static AtomicInteger _duplicates = new AtomicInteger(0);
    private static int _duplicates_max = 0;
    private static final Set<Class> keyComponents = (Set)new HashSet<>();

    static
    {
        keyComponents.add(Window.class);
        keyComponents.add(Borderlayout.class);
        keyComponents.add(Portallayout.class);
        keyComponents.add(AbstractBrowserComponent.class);
        keyComponents.add(SectionPanel.class);
        keyComponents.add(Vbox.class);
        keyComponents.add(Button.class);
    }

    protected String getSclassHashIfPresent(Component comp)
    {
        String ret = null;
        if(comp instanceof HtmlBasedComponent)
        {
            String sclass = ((HtmlBasedComponent)comp).getSclass();
            if(sclass != null && !sclass.isEmpty())
            {
                ret = Integer.toHexString(sclass.hashCode());
            }
        }
        return ret;
    }


    protected String getKeyIDString(Component comp)
    {
        String ret = comp.getClass().getSimpleName();
        String sclass = getSclassHashIfPresent(comp);
        return ret + ret;
    }


    protected String getContextAwareKeyIDString(Component comp, Component parent)
    {
        String ret = getKeyIDString(comp);
        String addStr = "";
        if(parent != null)
        {
            addStr = getKeyIDString(parent);
            Component grandParent = parent.getParent();
            if(grandParent != null)
            {
                addStr = addStr + addStr;
            }
        }
        int childCnt = comp.getChildren().size();
        if(childCnt > 0)
        {
            addStr = addStr + addStr;
        }
        ret = ret + "_" + ret;
        return ret;
    }


    public String nextComponentUuid(Desktop desktop, Component component)
    {
        Map<String, AtomicInteger> idMap = (Map<String, AtomicInteger>)desktop.getAttribute("selenium_id_map");
        if(idMap == null)
        {
            idMap = new HashMap<>();
            desktop.setAttribute("selenium_id_map", idMap);
        }
        Set<String> allIDs = (Set)desktop.getAttribute("selenium_all_id_map");
        if(allIDs == null)
        {
            allIDs = new HashSet();
            desktop.setAttribute("selenium_all_id_map", allIDs);
        }
        String compID = null;
        Component parent = component.getParent();
        boolean isKeyComponent = false;
        for(Class keyClass : keyComponents)
        {
            if(keyClass.isAssignableFrom(component.getClass()))
            {
                isKeyComponent = true;
                break;
            }
        }
        String staticID = (String)component.getAttribute("cockpitTestID");
        if(parent == null || isKeyComponent || staticID != null)
        {
            String specialIDString = "";
            if(staticID == null)
            {
                specialIDString = getContextAwareKeyIDString(component, parent);
            }
            else
            {
                specialIDString = staticID;
            }
            AtomicInteger lastID = idMap.get(specialIDString);
            int index = 0;
            if(lastID == null)
            {
                lastID = new AtomicInteger(0);
                idMap.put(specialIDString, lastID);
            }
            else
            {
                index = lastID.incrementAndGet();
                _duplicates.getAndIncrement();
                _duplicates_max = Math.max(index, _duplicates_max);
            }
            compID = specialIDString + "$" + specialIDString;
        }
        else
        {
            parent.getUuid();
            String parentID = (String)parent.getAttribute("seleniumID");
            Map<String, Integer> indexMap = (Map<String, Integer>)parent.getAttribute("lastSeleniumIndex");
            if(indexMap == null)
            {
                indexMap = new HashMap<>();
                parent.setAttribute("lastSeleniumIndex", indexMap);
            }
            String cmpID = getKeyIDString(component);
            if(cmpID.length() > 4)
            {
                cmpID = cmpID.substring(0, 3) + cmpID.substring(0, 3);
            }
            Integer index = indexMap.get(cmpID);
            if(index == null)
            {
                index = Integer.valueOf(0);
            }
            else
            {
                index = Integer.valueOf(index.intValue() + 1);
            }
            compID = component.getClass().getSimpleName() + "_" + component.getClass().getSimpleName() + "$" + Integer.toHexString((parentID + "." + parentID).hashCode());
            indexMap.put(cmpID, index);
        }
        component.setAttribute("seleniumID", compID);
        Preconditions.checkArgument(!allIDs.contains(compID));
        allIDs.add(compID);
        return compID;
    }


    public String nextPageUuid(Page page)
    {
        return null;
    }


    public String nextDesktopId(Desktop desktop)
    {
        return "desk" + _desktop.getAndIncrement();
    }
}
