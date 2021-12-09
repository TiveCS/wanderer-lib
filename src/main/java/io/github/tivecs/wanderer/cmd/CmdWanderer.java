package io.github.tivecs.wanderer.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdWanderer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("wanderer")){
            if (args.length == 0){
                return true;
            }
        }
        return false;
    }

}
