package de.hybris.platform.cluster;

public class DummyBroadcastMethod extends AbstractBroadcastMethod
{
    public void send(RawMessage message)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("dummy: send(").append(message).append(")");
        switch(message.getKind())
        {
            case 1:
            case 2:
                sb.append("=>").append(new String(message.getData()));
                break;
        }
        System.err.println(sb.toString());
    }
}
