package fr.matiix310.plugiix

import fr.matiix310.plugiix.commands.FeedCommand
import fr.matiix310.plugiix.commands.HealCommand
import fr.matiix310.plugiix.commands.SetFoodCommand
import fr.matiix310.plugiix.commands.SetHealthCommand
import org.bukkit.plugin.java.JavaPlugin

class Plugiix : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        getCommand("feed")?.setExecutor(FeedCommand())
        getCommand("setfood")?.setExecutor(SetFoodCommand())
        getCommand("heal")?.setExecutor(HealCommand())
        getCommand("sethealth")?.setExecutor(SetHealthCommand())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
