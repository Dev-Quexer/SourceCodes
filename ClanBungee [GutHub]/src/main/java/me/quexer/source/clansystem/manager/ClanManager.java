package me.quexer.source.clansystem.manager;

import com.mongodb.client.model.Filters;
import me.quexer.source.clansystem.ClanSystem;
import me.quexer.source.clansystem.entity.Clan;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class ClanManager {

    private HashMap<String, Clan> clans = new HashMap<>();

    public void createClan(ProxiedPlayer creator, String name, Consumer<Clan> consumer) {
        if(getClans().containsKey(name)) {
            consumer.accept(getClans().get(name));
            creator.sendMessage(ClanSystem.getPrefix()+"§cDieser §eClan §cexistiert bereits§8!");
            return;
        }

        ClanSystem.getMongoManager().getClans().find(Filters.eq("name",  name)).first((result, t) -> {
            if(result == null) {
                Clan clan = new Clan();
                clan.setLeader(creator.getUniqueId().toString());
                clan.setMembers(Arrays.asList(creator.getUniqueId().toString()));
                clan.setModerator(new ArrayList<>());
                clan.setMotd("Nicht Vorhanden!");
                clan.setName(name);

                result = ClanSystem.getGson().fromJson(ClanSystem.getGson().toJson(clan), Document.class);

                ClanSystem.getMongoManager().getClans().insertOne(result, (result1, t1) ->{
                    consumer.accept(clan);
                    clans.put(clan.getName(), clan);
                    creator.sendMessage(ClanSystem.getPrefix()+"§7Der Clan §e"+clan.getName()+" §7wurde §aerfolgreich §7erstellt§8.");
                    creator.sendMessage(ClanSystem.getPrefix()+"§7UUID§8: §e"+clan.getName());
                });
                return;

            } else {
                Clan clan = ClanSystem.getGson().fromJson(result.toJson(), Clan.class);
                consumer.accept(clan);
                clans.put(clan.getName(), clan);
                creator.sendMessage(ClanSystem.getPrefix()+"§cDieser §eClan §cexistiert bereits§8!");
                return;
            }
        });

    }

    public void getClan(String name, Consumer<Clan> consumer) {
        if(getClans().containsKey(name)) {
            consumer.accept(getClans().get(name));
            return;
        }

        ClanSystem.getMongoManager().getClans().find(Filters.eq("name", name)).first((result, t) -> {
            if(result == null) {
                consumer.accept(null);
                return;
            } else {
                Clan clan = ClanSystem.getGson().fromJson(result.toJson(), Clan.class);
                consumer.accept(clan);
                clans.put(clan.getName(), clan);
                return;
            }
        });

    }

    public void updateClan(Clan clan, Consumer<Clan> consumer) {
        Document document = ClanSystem.getGson().fromJson(ClanSystem.getGson().toJson(clan), Document.class);

        ClanSystem.getMongoManager().getClans()
                .replaceOne(Filters.eq("uuid", clan.getName()), document, (result, t) -> {

                    getClans().put(clan.getName(), clan);

                    consumer.accept(clan);
                });
    }

    public HashMap<String, Clan> getClans() {
        return clans;
    }
}
