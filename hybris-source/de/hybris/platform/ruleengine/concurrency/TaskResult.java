package de.hybris.platform.ruleengine.concurrency;

import java.io.Serializable;

public interface TaskResult extends Serializable
{
    State getState();
}
