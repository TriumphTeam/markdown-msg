package me.mattstudios.mfmsg

import org.bukkit.plugin.java.JavaPlugin
import me.mattstudios.mf.base.CommandManager
import me.mattstudios.mfmsg.Cmd

class TestPlugin : JavaPlugin() {

    override fun onEnable() {
        val commandManager = CommandManager(this)
        commandManager.register(Cmd(this))
    }

}