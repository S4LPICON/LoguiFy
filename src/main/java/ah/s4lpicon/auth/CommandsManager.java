package ah.s4lpicon.auth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("login")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                // Assuming Manager.openInventory() is a static method
                Manager.openInventory(player);
                player.sendMessage("You have executed the /login command!");
                return true;
            } else {
                sender.sendMessage("This command can only be executed by a player.");
                return false;
            }
        }

        if (command.getName().equalsIgnoreCase("debuginfo")) {
            // Display debug information
            sender.sendMessage("Hello!");

            if (sender instanceof Player) {
                Player player = (Player) sender;
                // Assuming Manager.allDebugInfo() is a static method
                player.sendMessage("Debug info: " + Manager.getAllDebugInfo(player.getName()));
                return true;
            } else {
                sender.sendMessage("This command can only be executed by a player.");
                return false;
            }
        }

        return false;
    }
}
