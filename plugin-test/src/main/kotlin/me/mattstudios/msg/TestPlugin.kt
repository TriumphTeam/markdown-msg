package me.mattstudios.msg

import me.mattstudios.mf.base.CommandManager
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {

    override fun onEnable() {
        val commandManager = CommandManager(this)
        commandManager.register(Cmd(this))
    }

}