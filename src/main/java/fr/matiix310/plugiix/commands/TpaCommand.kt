package fr.matiix310.plugiix.commands

import fr.matiix310.plugiix.Plugiix
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TpaCommand : CommandExecutor {

    companion object {
        // toTeleport: location
        var waitingPlayers: MutableList<Pair<String, String>> = mutableListOf()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        // revoke all non Player sender
        if (sender !is Player) {
            sender.sendMessage(Component.text("You can't use this command! Seriously, teleporting a console?!").color(NamedTextColor.RED))
            return true
        }

        val player: Player = sender

        return when(label) {
            "tpa" -> tpaCommand(command, label, args, player)
            "tpaccept" -> tpacceptCommand(command, label, args, player)
            "tpdecline" -> tpdeclineCommand(command, label, args, player)
            else -> false
        }
    }

    @Suppress("SameReturnValue")
    private fun tpaCommand(command: Command, label: String, args: Array<out String>?, player: Player): Boolean {

        // get the target
        if (args?.size != 1) {
            player.sendMessage(Component.text("You have to correctly specify a player (eg: /tpa Matiix310)!").color(NamedTextColor.RED))
            return true
        }

        val target: Player? = Bukkit.getPlayer(args[0])

        if (target == null) {
            player.sendMessage(
                Component.text("Can't find the player: ")
                    .append(Component.text(args[0]).decorate(TextDecoration.BOLD))
                    .append(Component.text("!"))
                    .color(NamedTextColor.RED))
            return true
        }

        if (player.name == target.name) {
            player.sendMessage(Component.text("You can't teleport to yourself!").color(NamedTextColor.RED))
            return true
        }

        val teleportRequest: Pair<String, String> = Pair(player.name, target.name)

        if (waitingPlayers.contains(teleportRequest)) {
            player.sendMessage(Component.text("A request to this player is still pending!").color(NamedTextColor.RED))
            return true
        }

        waitingPlayers.add(teleportRequest)

        // send the message to the target
        target.sendMessage(
            Component.empty()
                .appendNewline()
                .append(Component.text("The player "))
                .append(target.displayName().decorate(TextDecoration.BOLD))
                .append(Component.text(" wants to teleport to your location. You have 30s to accept or decline the request."))
                .appendNewline()
                .append(
                    Component.text("[ACCEPT]")
                        .color(NamedTextColor.GREEN)
                        .clickEvent(
                            ClickEvent.runCommand("/tpaccept " + player.name)
                        ).hoverEvent(
                            HoverEvent.showText(Component.text("Click to accept!"))
                        )
                        .decorate(TextDecoration.BOLD))
                .appendSpace()
                .append(
                    Component.text("[DECLINE]")
                        .color(NamedTextColor.RED)
                        .clickEvent(
                            ClickEvent.runCommand("/tpdecline " + player.name)
                        ).hoverEvent(
                            HoverEvent.showText(Component.text("Click to decline!"))
                        )
                        .decorate(TextDecoration.BOLD))
                .appendNewline()
        )

        player.sendMessage(Component.text("Request sent!").color(NamedTextColor.GREEN))

        object: BukkitRunnable() {
            override fun run() {
                if (waitingPlayers.contains(teleportRequest)) {
                    waitingPlayers.remove(teleportRequest)

                    player.sendMessage(Component.text("The teleportation request to ")
                        .append(target.displayName().decorate(TextDecoration.BOLD))
                        .append(Component.text(" has expired!"))
                        .color(NamedTextColor.RED))
                }
            }
        }.runTaskLater(Plugiix.instance, 30 * 20L)

        return true
    }

    private fun tpacceptCommand(command: Command, label: String, args: Array<out String>?, player: Player): Boolean {

        // get the target
        if (args?.size != 1) {
            player.sendMessage(Component.text("You have to correctly specify a player (eg: /tpaccept Matiix310)!").color(NamedTextColor.RED))
            return true
        }

        val target: Player? = Bukkit.getPlayer(args[0])

        if (target == null) {
            player.sendMessage(
                Component.text("Can't find the player: ")
                    .append(Component.text(args[0]).decorate(TextDecoration.BOLD))
                    .append(Component.text("!"))
                    .color(NamedTextColor.RED))
            return true
        }

        val teleportRequest: Pair<String, String> = Pair(target.name, player.name)

        if (!waitingPlayers.contains(teleportRequest)) {
            player.sendMessage(
                Component.text("You have no request made by ")
                    .append(target.displayName().decorate(TextDecoration.BOLD))
                    .append(Component.text("!"))
                    .color(NamedTextColor.RED)
            )
            return true
        }

        waitingPlayers.remove(teleportRequest)

        player.sendMessage(
            Component.text("Teleporting ")
                .append(target.displayName().decorate(TextDecoration.BOLD))
                .append(Component.text(" to your location!"))
                .color(NamedTextColor.GREEN)
        )

        target.sendMessage(
            Component.text("You will be teleported to ")
                .append(player.displayName().decorate(TextDecoration.BOLD))
                .append(Component.text(" in 3s, DON'T MOVE!"))
        )

        val location: Location = target.location

        object: BukkitRunnable() {
            override fun run() {
                if (location == target.location) {
                    target.teleport(player)
                } else {
                    target.sendMessage(
                        Component.text("Teleportation canceled, because you moved!")
                            .color(NamedTextColor.RED))
                    player.sendMessage(
                        Component.text("Teleportation canceled, because ")
                            .append(target.displayName().decorate(TextDecoration.BOLD))
                            .append(Component.text(" moved!"))
                            .color(NamedTextColor.RED))
                }
            }
        }.runTaskLater(Plugiix.instance, 3 * 20L)

        return true
    }

    private fun tpdeclineCommand(command: Command, label: String, args: Array<out String>?, player: Player): Boolean {

        // get the target
        if (args?.size != 1) {
            player.sendMessage(Component.text("You have to correctly specify a player (eg: /tpdecline Matiix310)!").color(NamedTextColor.RED))
            return true
        }

        val target: Player? = Bukkit.getPlayer(args[0])

        if (target == null) {
            player.sendMessage(
                Component.text("Can't find the player: ")
                    .append(Component.text(args[0]).decorate(TextDecoration.BOLD))
                    .append(Component.text("!"))
                    .color(NamedTextColor.RED))
            return true
        }

        val teleportRequest: Pair<String, String> = Pair(target.name, player.name)

        if (!waitingPlayers.contains(teleportRequest)) {
            player.sendMessage(
                Component.text("You have no request made by ")
                    .append(target.displayName().decorate(TextDecoration.BOLD))
                    .append(Component.text("!"))
                    .color(NamedTextColor.RED)
            )
            return true
        }

        waitingPlayers.remove(teleportRequest)

        player.sendMessage(
            Component.text("The teleportation request from ")
                .append(target.displayName().decorate(TextDecoration.BOLD))
                .append(Component.text(" has been declined!"))
                .color(NamedTextColor.RED))

        target.sendMessage(
            Component.text("The teleportation request to ")
                .append(player.displayName().decorate(TextDecoration.BOLD))
                .append(Component.text(" has been declined!"))
                .color(NamedTextColor.RED))

        return true
    }
}