package de.hybris.platform.core.system;

public class MSSQLServerTransactionParameters
{
    private final boolean isReadCommittedSnapshotOn;
    private final boolean isSnapshotIsolationStateOn;


    public MSSQLServerTransactionParameters(boolean isReadCommittedSnapshotOn, boolean isSnapshotIsolationStateOn)
    {
        this.isReadCommittedSnapshotOn = isReadCommittedSnapshotOn;
        this.isSnapshotIsolationStateOn = isSnapshotIsolationStateOn;
    }


    public boolean isSnapshotIsolationStateOn()
    {
        return this.isSnapshotIsolationStateOn;
    }


    public boolean isReadCommittedSnapshotOn()
    {
        return this.isReadCommittedSnapshotOn;
    }
}
