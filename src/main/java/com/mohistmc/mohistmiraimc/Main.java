package com.mohistmc.mohistmiraimc;

import com.google.common.base.Strings;
import com.mohistmc.api.ServerAPI;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static Logger LOGGER = LogManager.getLogger();
    public static Main plugin;
    private Bot mirai;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        saveDefaultConfig();
        reloadConfig();
        ServerAPI.registerBukkitEvents(this, this);
        long account = getConfig().getLong("login.account");
        String password = getConfig().getString("login.password");

        BotConfiguration botConfiguration = new BotConfiguration() {
            {
                fileBasedDeviceInfo("device.json");
            }
        };


        if (account == 0L || Strings.isNullOrEmpty(password)) {
            LOGGER.error("您没有设置账户和密码。请在配置文件设置后重新启动。");
            System.exit(0);
        } else {
            this.mirai = BotFactory.INSTANCE.newBot(account, password, botConfiguration);
            LOGGER.info("尝试登入QQ...");
            this.mirai.login();
            new Thread(this.mirai::join, "Bot").start();
        }
    }

    public void onDisable() {
    }
}
