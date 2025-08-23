package de.hybris.platform.cockpit.util;

import java.util.List;
import org.zkoss.zk.ui.Component;

public interface LazyLoader extends Component
{
    void setLazyLoadParent(Component paramComponent);


    void setLazyLoadChildren(List<Component> paramList);


    void loadComponents();
}
