package ah.s4lpicon.auth;

/*
hola buenas, tengo un problema con un plugin que estoy haciendo de logueo con inventarios, toda la logica ya esta implementada y sirve como lo esperaba pero, la contraseña de los jugadores
 */

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginManager implements Listener {

    private boolean logueado;
    private boolean itsLogin;

    private PasswordManager pswm;
    private Inventory chestInventory;

    private ArrayList<Integer> pasword;
    private List<Integer> ItemSlots = new ArrayList<>(Arrays.asList(
            15, 16, 17, 18, 19, 21, 22, 24, 25, 26,
            27, 28, 30, 31, 33, 34, 35, 6, 7, 8
    ));

    private List<Integer> CustomModelData = new ArrayList<>(Arrays.asList(
            15, 16, 17, 18, 19, 21, 22, 24, 25, 26,
            27, 28, 30, 31, 33, 34, 35, 6, 7, 8
    ));

    private List<String> Nombres = new ArrayList<>(Arrays.asList(
            "1", "2", "3", "Iniciar Sesion", "Iniciar Sesion",
            "Registrarse", "Registrarse", "4", "5", "6",
            "Iniciar Sesion", "Iniciar Sesion", "Registrarse", "Registrarse", "7",
            "8", "9", "Eliminar Digito", "0", "Enviar"
    ));


    public LoginManager(){
        pasword = new ArrayList<>();
        pswm = new PasswordManager();
    }

    public void aniadirDigito(int num) {
        if (pasword.size() < 5) {
            pasword.add(num);
            actualizarVisualizacionPasword();
        }
    }

    public void cambioDeInv(Player player, String name) {
        pasword.clear();
        // Cerrar el inventario antiguo
        player.closeInventory();
        // Crear un nuevo inventario con el nuevo nombre (de una sola línea)
        abrirInvLog(player, name);
    }

    public void eliminarDigito() {
        if (pasword.size()>0 && pasword.get(0) != null) pasword.remove(pasword.size() - 1);
        chestInventory.clear(pasword.size() + 2);
    }

    public void actualizarVisualizacionPasword() {
        for (int i = 0; i < pasword.size(); i++) {
            if (pasword.get(i) != null) {
                ItemStack item = new ItemStack(Material.PRISMARINE_SHARD, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.WHITE + "" + pasword.get(i));
                meta.setCustomModelData(CustomModelData.get(i));
                item.setItemMeta(meta);
                chestInventory.setItem(i + 2, item);
            }
        }
    }

    public void enviarYverificarContrasenia(Player player) {
        if (pasword.size() == 5) {
            //envia y verifica la contraseña

            if (itsLogin){
                //se esta logueando
                if(pswm.verifyPassword(player.getName(), pasword.toString())) {
                    player.sendMessage("Logueado Correctamente");
                    pasword.clear();
                }
                else player.sendMessage("Incorrecto");

            }else {
                //se esta registrando
                //intento de verificar si el usuario ya existe para no registrarlo nuevamente
                if (!(pswm.existPlayer(player.getName()))){
                    pswm.addPassword(player.getName(), pasword.toString());
                    player.sendMessage("Password to string"+pasword.toString());
                }else {
                    player.sendMessage("Este Jugador ya ha sido registrado");
                }

            }


            chestInventory.close();
        } else {
            player.sendMessage("La contraseña debe tener 5 digitos.");
        }
    }

    public void abrirInvLog(Player player, String name) {
        if (name == null) {
            name = "Seleciona Una opcion";
        }
        chestInventory = Bukkit.createInventory(null, 9, name);
        // Abrir una tolva (hopper) para el jugador
        //Inventory hopperInventory = Bukkit.createInventory(null, InventoryType.HOPPER, "Logueo");


        player.openInventory(chestInventory);

        for (int i = 0; i < ItemSlots.size(); i++) {
            ItemStack item = new ItemStack(Material.PRISMARINE_SHARD, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + Nombres.get(i));
            meta.setCustomModelData(CustomModelData.get(i));
            item.setItemMeta(meta);
            player.getInventory().setItem(ItemSlots.get(i), item); // Coloca la espada en la primera ranura
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryView inventoryView = event.getView();
        Inventory inventory = inventoryView.getTopInventory();
        Player player = (Player) event.getPlayer();

        // Verificar si el inventario cerrado es el que queremos manejar
        // Eliminar los ítems que agregamos anteriormente
        for (int slot : ItemSlots) {
            player.getInventory().clear(slot);
        }

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

            int x = event.getSlot();
            if (x == 18 || x == 19 || x == 27 || x == 28) {
                //Iniciar Sesion
                itsLogin = true;
                cambioDeInv(player, "Estas Iniciando Sesion");
            } else if (x == 21 || x == 22 || x == 30 || x == 31) {
                //Registrarse
                itsLogin = false;
                cambioDeInv(player, "Estas Registrandote");
            }
            if (event.getView().getTitle().equals("Estas Iniciando Sesion") || event.getView().getTitle().equals("Estas Registrandote")) {

                // Obtén el ítem clickeado
                ItemStack clickedItem = event.getCurrentItem();

                // Envía un mensaje al jugador
                if (clickedItem != null && clickedItem.getType() != null) {

                    switch (event.getSlot()) {
                        case 15:
                            aniadirDigito(1);
                            break;
                        case 16:
                            aniadirDigito(2);
                            break;
                        case 17:
                            aniadirDigito(3);
                            break;
                        case 24:
                            aniadirDigito(4);
                            break;
                        case 25:
                            aniadirDigito(5);
                            break;
                        case 26:
                            aniadirDigito(6);
                            break;
                        case 33:
                            aniadirDigito(7);
                            break;
                        case 34:
                            aniadirDigito(8);
                            break;
                        case 35:
                            aniadirDigito(9);
                            break;
                        case 6:
                            eliminarDigito();
                            break;
                        case 7:
                            aniadirDigito(0);
                            break;
                        case 8:
                            enviarYverificarContrasenia(player);
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