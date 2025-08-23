package de.hybris.platform.cockpit.session;

import java.util.List;

public interface PageableBrowserModel extends AdvancedBrowserModel
{
    void updateItems(int paramInt);


    boolean addPageSize(int paramInt);


    void setPageSize(int paramInt);


    int getPageSize();


    List<Integer> getPageSizes();


    void setCurrentPage(int paramInt);


    int getCurrentPage();


    int getLastPage();


    void setOffset(int paramInt);


    int getOffset();


    boolean isSimplePaging();


    void setSimplePaging(boolean paramBoolean);
}
