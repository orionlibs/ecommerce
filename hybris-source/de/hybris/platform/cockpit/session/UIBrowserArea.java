package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ContextAreaActionColumnConfigurationRegistry;
import de.hybris.platform.cockpit.session.impl.BrowserAreaListener;
import java.util.List;
import java.util.Map;
import org.zkoss.zkex.zul.Borderlayout;

public interface UIBrowserArea extends UICockpitArea, FocusablePerspectiveArea
{
    BrowserModel getFocusedBrowser();


    List<BrowserModel> getBrowsers();


    List<BrowserModel> getVisibleBrowsers();


    void replaceBrowser(BrowserModel paramBrowserModel1, BrowserModel paramBrowserModel2);


    void show(BrowserModel paramBrowserModel);


    void hide(BrowserModel paramBrowserModel);


    void close(BrowserModel paramBrowserModel);


    void closeOthers(BrowserModel paramBrowserModel);


    void saveQuery(BrowserModel paramBrowserModel);


    boolean isSaveQueryAvailable();


    void updateActiveItems();


    void updateSelectedItems(BrowserModel paramBrowserModel);


    void updateActivation(BrowserModel paramBrowserModel);


    void addBrowserAreaListener(BrowserAreaListener paramBrowserAreaListener);


    void removeBrowserAreaListener(BrowserAreaListener paramBrowserAreaListener);


    boolean addVisibleBrowser(int paramInt, BrowserModel paramBrowserModel);


    boolean addVisibleBrowser(BrowserModel paramBrowserModel, boolean paramBoolean);


    void duplicateBrowser(BrowserModel paramBrowserModel);


    void setFocusedBrowser(BrowserModel paramBrowserModel);


    void initBrowsers(Borderlayout paramBorderlayout);


    boolean isMinimizable(BrowserModel paramBrowserModel);


    boolean isClosable(BrowserModel paramBrowserModel);


    boolean isBrowserMinimized(BrowserModel paramBrowserModel);


    boolean isSplittable();


    void setSplittable(boolean paramBoolean);


    boolean isSplitModeActive();


    void setSplitModeActive(boolean paramBoolean);


    BrowserModel getPreviousBrowser();


    ActionColumnConfiguration getMultiSelectActions();


    void setMultiSelectActions(ActionColumnConfiguration paramActionColumnConfiguration);


    ActionColumnConfiguration getMultiSelectContextActions();


    void setMultiSelectContextActions(ActionColumnConfiguration paramActionColumnConfiguration);


    ContextAreaActionColumnConfigurationRegistry getMultiSelectContextActionsRegistry();


    void setMultiSelectContextActionsRegistry(ContextAreaActionColumnConfigurationRegistry paramContextAreaActionColumnConfigurationRegistry);


    Map<String, String> getDefaultBrowserViewMapping();
}
