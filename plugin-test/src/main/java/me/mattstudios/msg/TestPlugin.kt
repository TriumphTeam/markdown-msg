package me.mattstudios.msg

import org.bukkit.plugin.java.JavaPlugin
import me.mattstudios.mf.base.CommandManager

class TestPlugin : JavaPlugin() {

    override fun onEnable() {
        val commandManager = CommandManager(this)
        commandManager.register(Cmd(this))
    }

}