package fr.matiix310.plugiix.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HealCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val player: Player?

        when(args?.size) {
            1 -> player = Bukkit.getPlayer(args[0])
            0 -> {
                if (sender !is Player) return false
                player = sender as Player
            }
            else -> return false
        }

        if (player == null) {
            sender.sendMessage(
                Component.text("Can't find the player ")
                    .append(Component.text(args[0]).decorate(TextDecoration.BOLD))
                    .append(Component.text("!"))
                    .color(NamedTextColor.RED)
            )
            return true
        }

        player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.defaultValue
        sender.sendMessage(
            Component.text("The player ")
                .append(player.displayName().decorate(TextDecoration.BOLD))
                .append(Component.text(" has been heald!"))
                .color(NamedTextColor.GREEN)
        )
        return true;
    }
}