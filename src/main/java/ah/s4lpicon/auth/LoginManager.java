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

    private Player jugador;
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


    @Override
    public String toString() {
        return "LoginManager{" +
                "\nlogueado=" + logueado +
                ", \nitsLogin=" + itsLogin +
                ", \npswm=" + pswm +
                ", \njugador=" + jugador +
                ", \nchestInventory=" + chestInventory +
                ", \npasword=" + pasword +
                ", \nItemSlots=" + ItemSlots +
                ", \nCustomModelData=" + CustomModelData +
                ", \nNombres=" + Nombres +
                "\n}";
    }

    public boolean isLogueado(){
        return logueado;
    }


    public boolean isItsLogin(){
        return itsLogin;
    }
    public void setItsLogin(boolean x){
        itsLogin = x;
    }

    public String nombreJugador(){
        return jugador.getName();
    }

    public LoginManager(Player player){
        pasword = new ArrayList<>();
        pswm = new PasswordManager();
        jugador = player;
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
}