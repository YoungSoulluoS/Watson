package eu.minemania.watson.analysis;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;

import eu.minemania.watson.Watson;
import eu.minemania.watson.chat.ChatMessage;
import eu.minemania.watson.config.Configs;
import eu.minemania.watson.data.DataManager;
import eu.minemania.watson.db.TimeStamp;
import fi.dy.masa.malilib.gui.Message.MessageType;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;

public class ServerTime extends Analysis
{
    protected static final int MINUTES_TO_MILLISECONDS = 60 * 1000;
    protected HashMap<String, Integer> _localMinusServerMinutes = new HashMap<>();
    protected boolean _echoNextNoResults = true;
    protected boolean _showServerTime = false;
    private static final ServerTime INSTANCE = new ServerTime();

    public static ServerTime getInstance()
    {
        return INSTANCE;
    }

    public int getLocalMinusServerMinutes()
    {
        String serverIP = DataManager.getServerIP();
        if (serverIP == null)
        {
            return 0;
        }
        else
        {
            Integer offsetMinutes = _localMinusServerMinutes.get(serverIP);
            return offsetMinutes != null ? offsetMinutes : 0;
        }
    }

    public void queryServerTime(boolean showServerTime)
    {
        String serverIP = DataManager.getServerIP();
        if (serverIP != null)
        {
            if (_localMinusServerMinutes.get(serverIP) == null)
            {
                if (Configs.Generic.PLUGIN.getOptionListValue().getStringValue().equals("LogBlock"))
                {
                    Calendar pastTime = getPastTime();
                    String date = String.format("%d,%d,%d", pastTime.get(Calendar.DAY_OF_MONTH), pastTime.get(Calendar.MONTH) + 1, pastTime.get(Calendar.YEAR));
                    String query = String.format("/lb player %s since %s 00:00:00 before %s 00:00:01 limit 1", MinecraftClient.getInstance().player.getName().getString(), date, date);
                    if (Configs.Generic.DEBUG.getBooleanValue())
                    {
                        Watson.logger.info("Server time query for " + serverIP + ": " + query);
                    }
                    _showServerTime = showServerTime;
                    ChatMessage.sendToServerChat(query);
                }
                else
                {
                    InfoUtils.showInGameMessage(MessageType.INFO, "watson.message.info.no_logblock");
                }
            }
            else if (showServerTime)
            {
                showCurrentServerTime();
            }
        }
    }

    public ServerTime()
    {
        addMatchedChatHandler(Configs.Analysis.LB_HEADER_TIME_CHECK, (chat, m) -> {
            lbHeaderTimeCheck(chat, m);
            return false;
        });
        addMatchedChatHandler(Configs.Analysis.LB_HEADER_NO_RESULTS, this::lbHeaderNoResults);
    }

    void lbHeaderTimeCheck(MutableText chat, Matcher m)
    {
        String serverIP = DataManager.getServerIP();
        if (serverIP != null && _localMinusServerMinutes.get(serverIP) == null)
        {
            int serverMinutes = Integer.parseInt(m.group(1));
            Calendar now = Calendar.getInstance();
            Calendar pastTime = getPastTime();
            int localMinutes = (int) ((now.getTimeInMillis() - pastTime.getTimeInMillis()) / MINUTES_TO_MILLISECONDS);
            int localMinusServer = localMinutes - serverMinutes;
            _localMinusServerMinutes.put(serverIP, localMinusServer);
            if (Configs.Generic.DEBUG.getBooleanValue())
            {
                Watson.logger.info("Past time was " + serverMinutes + " minutes ago on the server and " + localMinutes + " minutes ago on the client.");
                Watson.logger.info("Client is " + localMinusServer + " minutes ahead of the server.");
            }
            if (_showServerTime)
            {
                showCurrentServerTime();
                _showServerTime = false;
            }
            _echoNextNoResults = false;
        }
    }

    boolean lbHeaderNoResults(MutableText chat, Matcher m)
    {
        boolean echo = _echoNextNoResults;
        _echoNextNoResults = true;
        return echo;
    }

    private void showCurrentServerTime()
    {
        String serverIP = DataManager.getServerIP();
        Integer localMinusServerMinutes = _localMinusServerMinutes.get(serverIP);
        long serverMillis = System.currentTimeMillis() - localMinusServerMinutes * MINUTES_TO_MILLISECONDS;
        ChatMessage.localOutput(TimeStamp.formatMonthDayTime(serverMillis), true);
    }

    private Calendar getPastTime()
    {
        Calendar pastTime = Calendar.getInstance();
        pastTime.add(Calendar.DAY_OF_MONTH, -2);
        pastTime.add(Calendar.HOUR_OF_DAY, 0);
        pastTime.add(Calendar.MINUTE, 0);
        return pastTime;
    }
}