package de.hybris.platform.configurablebundlebackoffice.widgets.searchadapters;

import com.google.common.collect.Sets;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.configurablebundlebackoffice.factory.ExplorerTreeSimpleNodeFactory;
import de.hybris.platform.core.PK;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;

public abstract class BundleTemplateConditionAdapterTestBase
{
    public static final PK STAGED_PK = PK.fromLong(1L);
    private final ExplorerTreeSimpleNodeFactory nodeFactory = new ExplorerTreeSimpleNodeFactory();
    @Mock
    protected CatalogModel catalog;
    @Mock
    protected CatalogVersionModel staged;


    @Before
    public void setUp()
    {
        Mockito.lenient().when(this.catalog.getCatalogVersions()).thenReturn(Sets.newHashSet((Object[])new CatalogVersionModel[] {this.staged}));
        Mockito.lenient().when(this.staged.getPk()).thenReturn(STAGED_PK);
    }


    protected ExplorerTreeSimpleNodeFactory getNodeFactory()
    {
        return this.nodeFactory;
    }
}
