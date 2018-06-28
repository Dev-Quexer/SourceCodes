package me.quexer.source.clansystem;

import com.google.gson.Gson;
import com.mongodb.async.client.FindIterable;
import me.quexer.source.clansystem.entity.Clan;
import me.quexer.source.clansystem.manager.ClanManager;
import me.quexer.source.clansystem.manager.MongoManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bson.Document;

public final class ClanSystem extends Plugin {

    private static ClanSystem clanSystem;
    private static String prefix;
    private static ClanManager clanManager;
    private static MongoManager mongoManager;
    private static Gson gson;
    private static ProxyServer bungeeCord;

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
        getBungeeCord().getScheduler().runAsync(this, () -> {
            getClanManager().getClans().forEach((uuid, clan) -> {
                getClanManager().updateClan(clan, clan1 -> {
                    getBungeeCord().getConsole().sendMessage(getPrefix() + "§7Der Clan §e" + clan1.getName() + " §7wurde §agespeichert§8!");
                });
            });
        });
    }

    private void init() {
        setPrefix("§f[§eClans§f] ");
        setClanSystem(this);
        setClanManager(new ClanManager());
        setMongoManager(new MongoManager("localhost", 27017));
        setGson(new Gson());
        setBungeeCord(this.getProxy());

        getBungeeCord().getScheduler().runAsync(this, () -> {
            FindIterable<Document> findIterable = getMongoManager().getClans().find();
            findIterable.forEach(document -> {
                getClanManager().getClans().put(getGson().fromJson(document.toJson(), Clan.class).getName(), getGson().fromJson(document.toJson(), Clan.class));
            }, (result, t) -> {
                getBungeeCord().getConsole().sendMessage(getPrefix() + "§7Es wurden §e" + getClanManager().getClans().size() + " §7Clans geladen§8!");
            });
        });


    }

    public static Gson getGson() {
        return gson;
    }

    public static void setGson(Gson gson) {
        ClanSystem.gson = gson;
    }

    public static ClanSystem getClanSystem() {
        return clanSystem;
    }

    public static void setClanSystem(ClanSystem clanSystem) {
        ClanSystem.clanSystem = clanSystem;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String prefix) {
        ClanSystem.prefix = prefix;
    }

    public static ClanManager getClanManager() {
        return clanManager;
    }

    public static void setClanManager(ClanManager clanManager) {
        ClanSystem.clanManager = clanManager;
    }

    public static MongoManager getMongoManager() {
        return mongoManager;
    }

    public static void setMongoManager(MongoManager mongoManager) {
        ClanSystem.mongoManager = mongoManager;
    }

    public static ProxyServer getBungeeCord() {
        return bungeeCord;
    }

    public static void setBungeeCord(ProxyServer bungeeCord) {
        ClanSystem.bungeeCord = bungeeCord;
    }
}
