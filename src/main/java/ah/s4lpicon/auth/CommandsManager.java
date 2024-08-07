package ah.s4lpicon.auth;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class CommandsManager implements CommandExecutor {

    LoginManager lg;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verifica si el comando es /logueo
        if (command.getName().equalsIgnoreCase("logueo")) {
            // Verifica si el sender es un jugador
            if (sender instanceof Player) {
                Player player = (Player) sender;
                lg = new LoginManager();
                // Envía un mensaje al jugador
                lg.abrirInvLog(player, null);
                player.sendMessage("¡Has ejecutado el comando /logueo!");
                return true;
            } else {
                // Envía un mensaje al sender si no es un jugador
                sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
                return false;
            }
        }
        return false;
    }
}
