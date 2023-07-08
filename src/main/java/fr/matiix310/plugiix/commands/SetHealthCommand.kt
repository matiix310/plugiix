package fr.matiix310.plugiix.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.logging.Level

class SetHealthCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val player: Player?
        val health: Double?

        when(args?.size) {
            1 -> {
                if (sender !is Player) return false
                player = sender as Player
                health = args[0].toDoubleOrNull()
            }
            2 -> {
                player = Bukkit.getServer().getPlayer(args[0])
                health = args[1].toDoubleOrNull()

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

        if (health == null) {
            sender.sendMessage(Component.text("Invalid health number!").color(NamedTextColor.RED))
            return true
        }

        val maxHealth: Double = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.defaultValue

        if (health !in 0.0..maxHealth) {
            sender.sendMessage(Component.text("The health number must be between 0.0 and $maxHealth").color(NamedTextColor.RED))
            return true
        }

        player.health = health;
        sender.sendMessage(
            Component.text("Set the health of ")
                .append(player.displayName().decorate(TextDecoration.BOLD))
                .append(Component.text(" to $health."))
                .color(NamedTextColor.GREEN))

        return true
    }
}