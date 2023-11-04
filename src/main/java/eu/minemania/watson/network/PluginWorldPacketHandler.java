package eu.minemania.watson.network;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import eu.minemania.watson.data.DataManager;
import fi.dy.masa.malilib.network.IPluginChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.List;

public class PluginWorldPacketHandler implements IPluginChannelHandler
{
    public static final List<Identifier> CHANNELS = ImmutableList.of(new Identifier("watson:world"));

    public static final PluginWorldPacketHandler INSTANCE = new PluginWorldPacketHandler();

    private boolean registered;

    public void reset()
    {
        registered = false;
        DataManager.setWorldPlugin("");
    }

    @Override
    public List<Identifier> getChannels()
    {
        return CHANNELS;
    }

    @Override
    public void onPacketReceived(PacketByteBuf buf)
    {
        if (!buf.toString(Charsets.UTF_8).isEmpty())
        {
            this.registered = true;
        }

        if (this.registered)
        {
            DataManager.setWorldPlugin(buf.toString(Charsets.UTF_8));
        }
    }
}
