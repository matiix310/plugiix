package fr.matiix310.plugiix.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.logging.Level

class SetFoodCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val player: Player?
        val foodLevel: Int?

        when(args?.size) {
            1 -> {
                if (sender !is Player) return false
                player = sender as Player
                foodLevel = args[0].toIntOrNull()
            }
            2 -> {
                player = Bukkit.getServer().getPlayer(args[0])
                foodLevel = args[1].toIntOrNull()

                if (player == null) {
                    sender.sendMessage(Component.text("Can't find the player ")
                        .append(Component.text(args[0]).decorate(TextDecoration.BOLD))
                        .append(Component.text("!"))
                        .color(NamedTextColor.RED))
                    return true
                }
            }
            else -> return false
        }

        if (foodLevel == null) {
            sender.sendMessage(Component.text("Invalid food level!").color(NamedTextColor.RED))
            return true
        }

        if (foodLevel !in 0..20) {
            sender.sendMessage(Component.text("The food level must be between 0 and 20!").color(NamedTextColor.RED))
            return true
        }

        player.foodLevel = foodLevel;
        sender.sendMessage(
            Component.text("Set the food level of ")
                .append(player.displayName().decorate(TextDecoration.BOLD))
                .append(Component.text(" to $foodLevel."))
                .color(NamedTextColor.GREEN))

        return true
    }
}