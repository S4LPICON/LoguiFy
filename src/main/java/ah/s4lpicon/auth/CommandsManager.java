package ah.s4lpicon.auth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("logueo")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                // Asumiendo que Manager.abrirInv() es un método estático
                Manager.abrirInv(player);
                player.sendMessage("¡Has ejecutado el comando /logueo!");
                return true;
            } else {
                sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
                return false;
            }
        }

        if (command.getName().equalsIgnoreCase("debuginfo")) {
            // Mostrar información de depuración
            sender.sendMessage("Holis");

            if (sender instanceof Player) {
                Player player = (Player) sender;
                // Asumiendo que Manager.allDebugInfo() es un método estático
                player.sendMessage("Debug info: " + Manager.allDebugInfo(player.getName()));
                return true;
            } else {
                sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
                return false;
            }
        }

        return false;
    }
}
