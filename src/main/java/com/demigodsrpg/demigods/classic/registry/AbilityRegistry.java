package com.demigodsrpg.demigods.classic.registry;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.ability.Ability;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import com.demigodsrpg.demigods.classic.util.ZoneUtil;
import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

// TODO Is this safe? Regardless it might be a better idea to scrap reflection... but mah annotations...

public class AbilityRegistry implements Listener {
    protected static final ConcurrentMap<String, Data> REGISTERED_COMMANDS = new ConcurrentHashMap<>();
    protected static final Multimap<Class<? extends Event>, Data> REGISTERED_ABILITIES = Multimaps.newMultimap(new ConcurrentHashMap<Class<? extends Event>, Collection<Data>>(), new Supplier<Collection<Data>>() {
        @Override
        public Collection<Data> get() {
            return new HashSet<>();
        }
    });

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEvent(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK:
            case LEFT_CLICK_AIR:
                return;
        }
        for (Data ability : REGISTERED_ABILITIES.get(event.getClass())) {
            try {
                PlayerModel model = DGClassic.PLAYER_R.fromPlayer(event.getPlayer());
                if (processAbility(model, ability)) {
                    ability.getMethod().invoke(ability.getDeity().getParentObject(), ability.eventClass.cast(event));
                    event.setCancelled(true);
                    return;
                }
            } catch (Exception oops) {
                oops.printStackTrace();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEvent(EntityDamageByEntityEvent event) {
        for (Data ability : REGISTERED_ABILITIES.get(event.getClass())) {
            try {
                if (event.getDamager() instanceof Player) {
                    Player player = (Player) event.getDamager();
                    PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);
                    if (processAbility(model, ability)) {
                        ability.getMethod().invoke(ability.getDeity().getParentObject(), ability.eventClass.cast(event));
                    }
                }
            } catch (Exception oops) {
                oops.printStackTrace();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEvent(FurnaceSmeltEvent event) {
        for (Data ability : REGISTERED_ABILITIES.get(event.getClass())) {
            for (PlayerModel model : DGClassic.PLAYER_R.fromDeity(ability.getDeity())) {
                try {
                    if (model.getOnline() && model.getLocation().getWorld().equals(event.getBlock().getWorld()) && model.getLocation().distance(event.getBlock().getLocation()) < (int) Math.round(20 * Math.pow(model.getDevotion(Deity.HEPHAESTUS), 0.15))) {
                        if (processAbility(model, ability)) {
                            ability.getMethod().invoke(ability.getDeity().getParentObject(), ability.eventClass.cast(event));
                            return; // TODO
                        }
                    }
                } catch (Exception oops) {
                    oops.printStackTrace();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void bindCommands(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        message = message.substring(1);
        String[] args = message.split("\\s+");
        Player player = event.getPlayer();

        if (!ZoneUtil.inNoDGCZone(event.getPlayer().getLocation())) {
            if (event.getPlayer().getItemInHand().getType().equals(Material.AIR)) return;
            // Process the command
            try {
                if (args.length == 2 && "info".equals(args[1])) {
                    if (abilityInfo(player, args[0].toLowerCase())) {
                        DGClassic.CONSOLE.info(event.getPlayer().getName() + " used the command: /" + message);
                        event.setCancelled(true);
                        return;
                    }
                }
                if (bindAbility(player, args[0].toLowerCase())) {
                    DGClassic.CONSOLE.info(event.getPlayer().getName() + " used the command: /" + message);
                    event.setCancelled(true);
                }
            } catch (Exception errored) {
                // Not a command
                errored.printStackTrace();
            }
        }
    }

    public boolean abilityInfo(Player player, String command) {
        for (Data ability : REGISTERED_ABILITIES.values()) {
            if (ability.getCommand().equals(command)) {
                player.sendMessage(StringUtil2.chatTitle(ability.getName()));
                player.sendMessage(" - Deity: " + ability.getDeity().getColor() + ability.getDeity().getDeityName());
                player.sendMessage(" - Type: " + StringUtil2.beautify(ability.getType().name()));
                if (!ability.getType().equals(Ability.Type.PASSIVE)) {
                    player.sendMessage(" - Cost: " + ability.getCost());
                }
                if (ability.getCooldown() > 0) {
                    player.sendMessage(" - Cooldown (ms): " + ability.getCooldown());
                }
                player.sendMessage(ability.getInfo());
                return true;
            }
        }
        return false;
    }

    public boolean bindAbility(Player player, String command) {
        // Is this a correct command?
        if (!REGISTERED_COMMANDS.keySet().contains(command)) {
            return false;
        }
        PlayerModel model = DGClassic.PLAYER_R.fromPlayer(player);
        Material material = player.getItemInHand().getType();
        Data bound = model.getBound(material);
        if (bound != null) {
            if (!bound.getCommand().equals(command)) {
                player.sendMessage(ChatColor.RED + "This item already has /" + bound.getCommand() + " bound to it.");
                return true;
            } else {
                model.unbind(bound);
                player.sendMessage(ChatColor.YELLOW + bound.getName() + " has been unbound.");
                return true;
            }
        } else {
            Data ability = fromCommand(command);
            if (ability.getCommand().equals(command) && (model.getMajorDeity().equals(ability.getDeity()) || model.getContractedDeities().contains(ability.getDeity()) && ability.getCommand().equals(command))) {
                model.bind(ability, material);
                player.sendMessage(ChatColor.YELLOW + ability.getName() + " has been bound to " + StringUtil2.beautify(material.name()) + ".");
                return true;
            }
        }
        return false;
    }

    public boolean processAbility(PlayerModel model, Data ability) {
        if (ZoneUtil.isNoDGCWorld(model.getLocation().getWorld())) return false;
        if (!ability.getType().equals(Ability.Type.PASSIVE)) {
            if ((ability.getType().equals(Ability.Type.OFFENSIVE) || ability.getType().equals(Ability.Type.ULTIMATE)) && ZoneUtil.inNoPvpZone(model.getLocation())) {
                return false;
            }
            if (model.getBound(ability) == null) {
                return false;
            }
            if (!model.getOfflinePlayer().getPlayer().getItemInHand().getType().equals(model.getBound(ability))) {
                return false;
            }

            double cost = ability.getCost();
            if (model.getFavor() < cost) {
                model.getOfflinePlayer().getPlayer().sendMessage(ChatColor.YELLOW + ability.getName() + " requires more favor.");
                return false;
            }
            if (DGClassic.SERV_R.exists(model.getMojangId().toString(), ability + ":delay")) {
                return false;
            }
            if (DGClassic.SERV_R.exists(model.getMojangId().toString(), ability + ":cooldown")) {
                model.getOfflinePlayer().getPlayer().sendMessage(ChatColor.YELLOW + ability.getName() + " is on a cooldown.");
                return false;
            }

            // Process it
            long delay = ability.getDelay();
            long cooldown = ability.getCooldown();

            if (delay > 0) {
                DGClassic.SERV_R.put(model.getMojangId().toString(), ability + ":delay", true, delay, TimeUnit.MILLISECONDS);
            }
            if (cooldown > 0) {
                DGClassic.SERV_R.put(model.getMojangId().toString(), ability + ":cooldown", true, delay, TimeUnit.MILLISECONDS);
            }
            if (cost > 0) {
                model.setFavor(model.getFavor() - cost);
            }
        }
        return true;
    }

    public void registerAbilities() {
        for (Deity deity : Deity.values()) {
            Class<? extends IDeity> deityClass = deity.getParentObjectClass();
            for (Method method : deityClass.getMethods()) {
                if (method.isAnnotationPresent(Ability.class)) {
                    Ability ability = method.getAnnotation(Ability.class);
                    register(deity, method, ability);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void register(Deity deity, Method method, Ability ability) {
        if (Ability.Type.PLACEHOLDER.equals(ability.type())) return;
        Class<?>[] paramaters = method.getParameterTypes();
        try {
            if (paramaters.length < 1) {
                DGClassic.CONSOLE.severe("An ability (" + ability.name() + ") tried to register without any parameters.");
                return;
            }
            Class<? extends Event> eventClass = (Class<? extends Event>) paramaters[0];
            Data data = new Data(deity, method, ability, eventClass);
            REGISTERED_ABILITIES.put(eventClass, data);
            if (!"".equals(data.getCommand())) {
                REGISTERED_COMMANDS.put(data.getCommand(), data);
            }
        } catch (Exception oops) {
            oops.printStackTrace();
        }
    }

    public Data fromCommand(String commandName) {
        if (REGISTERED_COMMANDS.containsKey(commandName)) {
            return REGISTERED_COMMANDS.get(commandName);
        }
        return null;
    }

    public static class Data {
        private Deity deity;
        private Method method;
        private Ability ability;
        private Class<? extends Event> eventClass;

        public Data(Deity deity, Method method, Ability ability, Class<? extends Event> eventClass) {
            this.deity = deity;
            this.method = method;
            this.ability = ability;
            this.eventClass = eventClass;
        }

        public Deity getDeity() {
            return deity;
        }

        public String getName() {
            return ability.name();
        }

        public String getCommand() {
            return ability.command();
        }

        public String[] getInfo() {
            return ability.info();
        }

        public Ability.Type getType() {
            return ability.type();
        }

        public double getCost() {
            return ability.cost();
        }

        public long getDelay() {
            return ability.delay();
        }

        public long getCooldown() {
            return ability.cooldown();
        }

        public EventPriority getEventPriority() {
            return ability.priority();
        }

        public Method getMethod() {
            return method;
        }

        public Ability getAbility() {
            return ability;
        }
    }

    /**
     * Various no damage abilities, these must be done by hand, and directly in this method.
     *
     * @param event The damage event.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void onNoDamageAbilities(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            switch (DGClassic.PLAYER_R.fromPlayer(player).getMajorDeity()) {
                case CRONUS: {
                    if (player.getHealth() <= event.getDamage()) {
                        switch (event.getCause()) {
                            case ENTITY_ATTACK:
                                break;
                            case PROJECTILE:
                                break;
                            case CUSTOM:
                                break;
                            default:
                                event.setDamage(player.getHealth() - 1);
                        }
                    }
                    event.setDamage(event.getDamage() / 2);
                }
                case ZEUS: {
                    if (EntityDamageEvent.DamageCause.FALL.equals(event.getCause())) {
                        event.setDamage(0.0);
                    }
                }
            }
        }
    }
}
