package org.arkadst.punishment;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class EventListener implements Listener {

    private final Main main;
    private final HashMap<UUID, Integer> candidates_for_ban;

    public EventListener (Main main){
        this.main = main;
        candidates_for_ban = new HashMap<>();
    }

    private interface UseNativePlaceholders {
        String run(String text);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event){

        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            Component chat_message = event.originalMessage();
            for (String slur : Main.config.getStringList("slurs")){
                if (chat_message.toString().toLowerCase(Locale.ROOT).contains(slur)){

                    Bukkit.getScheduler().runTask(main, () -> {
                        candidates_for_ban.putIfAbsent(player.getUniqueId(), 0);
                        candidates_for_ban.put(player.getUniqueId(), candidates_for_ban.get(player.getUniqueId()) + 1);

                        int ban_after = Main.config.getInt("ban_after");
                        int until_ban = ban_after - candidates_for_ban.get(player.getUniqueId());
                        long ban_expire_after = Main.config.getLong("ban_expire_after");

                        UseNativePlaceholders modify_text = new UseNativePlaceholders() {
                            @Override
                            public String run(String text) {
                                return text
                                        .replace("{player}", player.getName())
                                        .replace("{slur}", slur)
                                        .replace("{ban_after}", String.valueOf(ban_after))
                                        .replace("{until_ban}", String.valueOf(until_ban))
                                        .replace("{ban_expire_after}", String.valueOf(ban_expire_after));
                            }
                        };

                        String message_to_others = modify_text.run(Main.config.getString("message_to_others"));
                        String message_to_player = modify_text.run(Main.config.getString("message_to_player"));
                        String ban_reason = modify_text.run(Main.config.getString("ban_reason"));

                        if (until_ban == 0){
                            player.banPlayer(ChatColor.translateAlternateColorCodes('&', ban_reason), new Date(System.currentTimeMillis() + ban_expire_after * 60000L));
                            candidates_for_ban.remove(player.getUniqueId());
                        } else {
                            player.getWorld().strikeLightning(player.getLocation());
                            Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(message_to_others));
                            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message_to_player));
                        }
                    });
                    break;
                }
            }
        });


    }
}
