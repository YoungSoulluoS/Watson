package eu.minemania.watson.network.ledger;

import com.google.common.base.Charsets;
import eu.minemania.watson.Watson;
import eu.minemania.watson.config.Configs;
import eu.minemania.watson.data.LedgerSearch;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.List;

public class PluginRollbackPacketHandler
{
    public static final Identifier CHANNEL = new Identifier("ledger:rollback");

    public static final PluginRollbackPacketHandler INSTANCE = new PluginRollbackPacketHandler();

    public void sendPacket(List<String> action, List<String> dimension, List<String> block, List<String> entityType, List<String> item, List<String> tag, int range, String source, String timeBefore, String timeAfter, boolean restore, MinecraftClient mc)
    {
        try
        {
            ClientPlayNetworkHandler packetHandler = mc.getNetworkHandler();
            if (packetHandler == null)
            {
                return;
            }
            LedgerSearch ledgerSearch = new LedgerSearch(action, dimension, block, entityType, item, tag, range, source, timeBefore, timeAfter);
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
            packetByteBuf.writeBoolean(restore);
            packetByteBuf.writeString(ledgerSearch.getSearchData());
            if(Configs.Generic.DEBUG.getBooleanValue())
            {
                Watson.logger.info(packetByteBuf.toString(Charsets.UTF_8));
                Watson.logger.info("Restore" + (restore ? "true" : "false"));
                Watson.logger.info("action: "+ledgerSearch.getActions());
                Watson.logger.info("dimension: "+ledgerSearch.getDimensions());
                Watson.logger.info("object Block: "+ledgerSearch.getBlocks());
                Watson.logger.info("object Item: "+ledgerSearch.getItems());
                Watson.logger.info("object EntityType: "+ledgerSearch.getEntityTypes());
                Watson.logger.info("object Tag: "+ledgerSearch.getTags());
                Watson.logger.info("range: "+ledgerSearch.getRange());
                Watson.logger.info("source: "+ledgerSearch.getSources());
                Watson.logger.info("timeBefore: "+ledgerSearch.getTimeBefore());
                Watson.logger.info("timeAfter: "+ledgerSearch.getTimeAfter());
                Watson.logger.info("search: "+ledgerSearch.getSearchData());
                Watson.logger.info(CHANNEL);
            }
            ClientPlayNetworking.send(CHANNEL, packetByteBuf);
        }
        catch (Exception ignored)
        {}
    }
}
