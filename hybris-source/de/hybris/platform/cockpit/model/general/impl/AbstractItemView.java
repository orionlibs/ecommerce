package de.hybris.platform.cockpit.model.general.impl;

import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;

public abstract class AbstractItemView extends Div implements UIItemView
{
    public static final String ON_CONTINUE_LOADING = "onContinueLoading";
    private static final String RENDER_PACKET_SIZE = "de.hybris.platform.cockpit.browserview.renderPacketSize";
    private static final String SIMPLE_SELECTION_MODE = "listview.simpleselectionmode";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractItemView.class);
    private DragAndDropContext ddContext;
    private int lazyloadPackageSize = 30;
    private int initialPackageSize = 30;
    private boolean simpleSelectionMode = false;
    protected LoadingProgressContainer loadingProgressContainer;


    public AbstractItemView()
    {
        String simpleSelectionModeString = UITools.getCockpitParameter("listview.simpleselectionmode", Executions.getCurrent()
                        .getDesktop());
        if(!StringUtils.isBlank(simpleSelectionModeString))
        {
            this.simpleSelectionMode = Boolean.parseBoolean(simpleSelectionModeString);
        }
        String zkPreference = UITools.getZKPreference("de.hybris.platform.cockpit.browserview.renderPacketSize");
        if(zkPreference != null)
        {
            try
            {
                if(zkPreference.contains(","))
                {
                    String[] split = zkPreference.split(",");
                    this.initialPackageSize = Integer.parseInt(split[0]);
                    this.lazyloadPackageSize = Integer.parseInt(split[1]);
                }
                else
                {
                    this.lazyloadPackageSize = Integer.parseInt(zkPreference);
                    this.initialPackageSize = this.lazyloadPackageSize;
                }
            }
            catch(Exception e)
            {
                LOG.warn("Wrong zk preference value for key 'de.hybris.platform.cockpit.browserview.renderPacketSize', using default value " + this.lazyloadPackageSize);
            }
        }
    }


    public void setupLazyLoading(int initialPackageSize, int lazyloadPackageSize)
    {
        this.initialPackageSize = initialPackageSize;
        this.lazyloadPackageSize = lazyloadPackageSize;
    }


    protected DragAndDropContext getDDContext()
    {
        return this.ddContext;
    }


    public void setDDContext(DragAndDropContext context)
    {
        this.ddContext = context;
    }


    public int getLazyloadPackageSize()
    {
        return this.lazyloadPackageSize;
    }


    public int getInitialPackageSize()
    {
        return this.initialPackageSize;
    }


    public boolean isSimpleSelectionMode()
    {
        return this.simpleSelectionMode;
    }


    public void setSimpleSelectionMode(boolean simpleSelectionMode)
    {
        this.simpleSelectionMode = simpleSelectionMode;
    }


    protected void delayLazyload()
    {
        String key = "default.lazyloadDelay";
        String cockpitParameter = UITools.getCockpitParameter("default.lazyloadDelay", Executions.getCurrent());
        if(cockpitParameter != null)
        {
            try
            {
                Thread.sleep(Integer.parseInt(cockpitParameter));
            }
            catch(NumberFormatException e)
            {
                LOG.error("Wrong number format in configuration for key '*cockpit.default.lazyloadDelay'");
            }
            catch(InterruptedException e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
