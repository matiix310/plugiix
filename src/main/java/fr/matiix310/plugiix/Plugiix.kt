package fr.matiix310.plugiix

import fr.matiix310.plugiix.commands.*
import org.bukkit.plugin.java.JavaPlugin

class Plugiix : JavaPlugin() {

    override fun onEnable() {
        instance = this
        // Plugin startup logic
        getCommand("feed")?.setExecutor(FeedCommand())
        getCommand("setfood")?.setExecutor(SetFoodCommand())
        getCommand("heal")?.setExecutor(HealCommand())
        getCommand("sethealth")?.setExecutor(SetHealthCommand())
        getCommand("tpa")?.setExecutor(TpaCommand())
        getCommand("tpaccept")?.setExecutor(TpaCommand())
        getCommand("tpdecline")?.setExecutor(TpaCommand())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {
        lateinit var instance: Plugiix
    }
}
