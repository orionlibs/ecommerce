package de.hybris.platform.ordercancel;

import de.hybris.platform.ordercancel.dao.OrderCancelDaoTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({OrderCancelDaoTest.class, OrderCancelRecordsHandlerTest.class, OrderCancelPossibilityTest.class, OrderCancelCompleteTest.class, OrderCancelPartialTest.class, OrderCancelHistoryEntriesTest.class})
public class OrderCancelTestSuite
{
}
