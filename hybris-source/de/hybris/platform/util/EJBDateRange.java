package de.hybris.platform.util;

import java.io.Serializable;
import java.util.Date;

public interface EJBDateRange extends Serializable
{
    boolean encloses(Date paramDate);
}
