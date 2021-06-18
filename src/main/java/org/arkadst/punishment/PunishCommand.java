package org.arkadst.punishment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PunishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length >= 1){
            Player player = Bukkit.getPlayer(args[0]);
            if (player.isOnline()){
                player.getWorld().strikeLightning(player.getLocation());
                if (args.length > 1){
                    StringBuilder message = new StringBuilder();
                    for (int x = 1; x < args.length; x++){
                        message.append(args[x]);
                        if (x != args.length - 1){
                            message.append(" ");
                        }
                    }

                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message.toString()));
                }
            } else {
                sender.sendMessage(Component.text("Player " + player.getName() + " is not online.").color(NamedTextColor.RED));
            }

            return true;
        } else {
            return false;
        }
    }
}
