package me.mattstudios.mfmsg;

import me.mattstudios.mf.base.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final CommandManager commandManager = new CommandManager(this);
        commandManager.register(new Cmd());
    }

}
