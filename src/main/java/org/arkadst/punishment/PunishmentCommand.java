package org.arkadst.punishment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PunishmentCommand implements CommandExecutor {

    Main main;

    public PunishmentCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "reload":
                    main.reloadConfig();
                    Main.config = main.getConfig();
                    sender.sendMessage(Component.text("[Punishment] Configuration file was reloaded successfully").color(NamedTextColor.GREEN));
                    break;
                default:
                    sender.sendMessage(Component.text("[Punishment] No such command.").color(NamedTextColor.RED));
                    break;
            }

        } else {
            sender.sendMessage(Component.text("[Punishment] No such command.").color(NamedTextColor.RED));
        }
        return true;
    }
}
