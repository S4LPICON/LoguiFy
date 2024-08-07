package ah.s4lpicon.auth;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Manager implements Listener{

    LoginManager lg;
    private ArrayList<LoginManager> users = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        lg = new LoginManager();

        Player player = event.getPlayer();

        player.setWalkSpeed(0f); // Desactivar movimiento
        player.setFlySpeed(0f);  // Desactivar vuelo

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));



        // Evitar que se envíe el mensaje predeterminado de que un jugador ha entrado
        event.setJoinMessage(null);

        // Coordenadas específicas a las que quieres teletransportar al jugador
        double x = 8.5;
        double y = -58.0;
        double z = 7.5;
        World world = event.getPlayer().getWorld(); // Obtener el mundo actual del jugador

        // Crear una nueva ubicación con las coordenadas especificadas
        Location location = new Location(world, x, y, z);

        // Teletransportar al jugador a la nueva ubicación
        event.getPlayer().teleport(location);



        lg.abrirInvLog(event.getPlayer(), null);

    }
}