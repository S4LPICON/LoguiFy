package ah.s4lpicon.auth;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class Manager implements Listener{

    //LoginManager lg;
    private static ArrayList<LoginManager> users = new ArrayList<>();
    public static String allDebugInfo(String name){
        String txt = "All Users: " + users.toString() +
                "\n Esta Logueado?: "+ users.get(buscarUsu(name)).isLogueado() +
                "\n Esta iniciando sesion?: " + users.get(buscarUsu(name)).isItsLogin();
        return txt;
    }

    public static void abrirInv(Player player) {
        int index = buscarUsu(player.getName());
        if (index != -1 && index < users.size()) {
            users.get(index).abrirInvLog(player, null);
        } else {
            // Manejar el caso cuando el jugador no está en la lista
            player.kick();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LoginManager lg = new LoginManager(player);

        users.add(lg);



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

        users.get(buscarUsu(player.getName())).abrirInvLog(event.getPlayer(), null);
    }

    public static int buscarUsu(String name){
        for (LoginManager user: users){
            if (Objects.equals(user.nombreJugador(), name)){
                return users.indexOf(user);
            }
        }
        return -1;
    }

    public void eliminarUser(String name){
        users.remove(buscarUsu(name));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        player.getInventory().clear();
        if(buscarUsu(player.getName()) != -1 ){
            if(users.get(buscarUsu(player.getName())).isLogueado()) {
                users.remove(buscarUsu(player.getName()));
            }
        }


    }

    public void pereza(int x, int pos){
        users.get(pos).aniadirDigito(x);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Aquí puedes realizar las acciones necesarias cuando el jugador sale
        int userIndex = buscarUsu(player.getName());
        if (userIndex != -1) {
            users.remove(userIndex);
        }
        // También puedes limpiar inventarios, efectos de poción, etc.
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // Asegúrate de que el inventario clickeado es el correcto
        if (event.getView().getTitle() == "Seleciona Una opcion"
                || event.getView().getTitle() == "Estas Iniciando Sesion"
                || event.getView().getTitle() == "Estas Registrandote" ) {
            // Obtén el jugador que hizo clic
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true); // Evita que los ítems sean movidos

            int pos = buscarUsu(player.getName());
            int x = event.getSlot();
            if (x == 18 || x == 19 || x == 27 || x == 28) {
                //Iniciar Sesion
                users.get(pos).setItsLogin(true);
                users.get(pos).cambioDeInv(player, "Estas Iniciando Sesion");
            } else if (x == 21 || x == 22 || x == 30 || x == 31) {
                //Registrarse
                users.get(pos).setItsLogin(false);
                users.get(pos).cambioDeInv(player, "Estas Registrandote");
            }
            if (event.getView().getTitle().equals("Estas Iniciando Sesion") || event.getView().getTitle().equals("Estas Registrandote")) {

                // Obtén el ítem clickeado
                ItemStack clickedItem = event.getCurrentItem();

                // Envía un mensaje al jugador
                if (clickedItem != null && clickedItem.getType() != null) {

                    switch (event.getSlot()) {
                        case 15:
                            pereza(1, pos);
                            break;
                        case 16:
                            pereza(2, pos);
                            break;
                        case 17:
                            pereza(3, pos);
                            break;
                        case 24:
                            pereza(4, pos);
                            break;
                        case 25:
                            pereza(5, pos);
                            break;
                        case 26:
                            pereza(6, pos);
                            break;
                        case 33:
                            pereza(7, pos);
                            break;
                        case 34:
                            pereza(8, pos);
                            break;
                        case 35:
                            pereza(9, pos);
                            break;
                        case 6:
                            users.get(pos).eliminarDigito();
                            break;
                        case 7:
                            pereza(0, pos);
                            break;
                        case 8:
                            if(users.get(pos).enviarYverificarContrasenia(player)) {
                                eliminarUser(player.getName());
                            }
                            break;
                        default:
                            // Código a ejecutar si variable no coincide con ninguno de los casos anteriores
                            break;
                    }

                }
            }
        }
    }



}

