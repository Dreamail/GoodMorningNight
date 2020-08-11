package top.fan2tao.mirai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.console.plugins.PluginBase;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import top.fan2tao.mirai.threads.Days;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class GMN extends PluginBase {
    File config = new File(getDataFolder(), "config.json");
    private JSONObject configJson;
    private Days days;
    private static int times = 1;
    private static List<Member> hasSentM = new LinkedList<>();
    private static List<Member> hasSentN = new LinkedList<>();

    @Override
    public void onDisable() {
        this.days.mstop();
        this.days = null;
    }

    @Override
    public void onEnable() {
        this.days = new Days();
        this.days.start();
        String botName = configJson.getString("BotName");
        getEventListener().subscribeAlways(GroupMessageEvent.class, event -> {
            String msg = event.getMessage().contentToString();
            if (Pattern.matches("^Ôç\\S*$", msg)) {
                if (!hasSentM.contains(event.getSender())) {
                    String morningReply = Pattern.compile("\\$TIMES\\$")
                            .matcher(configJson.getJSONArray("MorningReply")
                                    .getString(new Random().nextInt(configJson.getJSONArray("MorningReply").size())))
                            .replaceAll(String.valueOf(times));
                    event.getGroup().sendMessage(new At(event.getSender()).plus(morningReply));
                    hasSentM.add(event.getSender());
                    times++;
                } else {
                    String morningAlready = configJson.getJSONArray("MorningAlready")
                            .getString(new Random().nextInt(configJson.getJSONArray("MorningAlready").size()));
                    event.getGroup().sendMessage(new At(event.getSender()).plus(morningAlready));
                }
            }
            if (Pattern.matches("^Íí°²\\S*$", msg)) {
                if (!hasSentN.contains(event.getSender())) {
                    String nightReply = Pattern.compile("\\$TIMES\\$")
                            .matcher(configJson.getJSONArray("NightReply")
                                    .getString(new Random().nextInt(configJson.getJSONArray("NightReply").size())))
                            .replaceAll(String.valueOf(times));
                    event.getGroup().sendMessage(new At(event.getSender()).plus(nightReply));
                    hasSentN.add(event.getSender());
                } else {
                    String nightAlready = configJson.getJSONArray("NightAlready")
                            .getString(new Random().nextInt(configJson.getJSONArray("NightAlready").size()));
                    event.getGroup().sendMessage(new At(event.getSender()).plus(nightAlready));
                }
            }
        });
    }

    public static synchronized void setNewDay() {
        times = 1;
        hasSentM.clear();
        hasSentN.clear();
    }

    @Override
    public void onLoad() {
        try {
            if (!config.exists()) {
                config.createNewFile();
                InputStream is = this.getClass().getResourceAsStream("/config.json");
                inputStream2File(is, config);
                is.close();
            }
            FileReader in = new FileReader(config);
            char byt[] = new char[(int)config.length()];
            in.read(byt);
            this.configJson = JSON.parseObject(new String(byt));
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void inputStream2File(InputStream is, File targetFile) throws IOException {
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        Files.write(targetFile.toPath(), buffer);
    }
}