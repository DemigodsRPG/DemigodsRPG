package com.demigodsrpg.game.registry;

import com.censoredsoftware.library.util.StringUtil2;
import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.ability.Ability;
import com.demigodsrpg.game.ability.AbilityMetaData;
import com.demigodsrpg.game.ability.AbilityResult;
import com.demigodsrpg.game.aspect.Aspect;
import com.demigodsrpg.game.aspect.Aspects;
import com.demigodsrpg.game.model.PlayerModel;
import com.demigodsrpg.game.util.ZoneUtil;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class AbilityRegistry implements Listener {
    // FIXME Do we really need two collections for the same data? This is expensive...

    private static final ConcurrentMap<String, AbilityMetaData> REGISTERED_COMMANDS = new ConcurrentHashMap<>();
    private static final Multimap<String, AbilityMetaData> REGISTERED_ABILITIES = Multimaps.newListMultimap(new ConcurrentHashMap<>(), () -> new ArrayList<>(0));

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEvent(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK:
            case LEFT_CLICK_AIR:
                return;
        }
        for (AbilityMetaData ability : REGISTERED_ABILITIES.get(event.getClass().getName())) {
            try {
                PlayerModel model = DGGame.PLAYER_R.fromPlayer(event.getPlayer());
                if (processAbility1(model, ability)) {
                    Object rawResult = ability.getMethod().invoke(ability.getAspect(), event);
                    processAbility2(event.getPlayer(), model, ability, rawResult);
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
        for (AbilityMetaData ability : REGISTERED_ABILITIES.get(event.getClass().getName())) {
            try {
                if (event.getDamager() instanceof Player) {
                    Player player = (Player) event.getDamager();
                    PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
                    if (processAbility1(model, ability)) {
                        Object rawResult = ability.getMethod().invoke(ability.getAspect(), event);
                        processAbility2(player, model, ability, rawResult);
                    }
                }
            } catch (Exception oops) {
                oops.printStackTrace();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEvent(FurnaceSmeltEvent event) {
        for (AbilityMetaData ability : REGISTERED_ABILITIES.get(event.getClass().getName())) {
            for (PlayerModel model : DGGame.PLAYER_R.fromAspect(ability.getAspect())) {
                try {
                    if (model.getOnline() && model.getLocation().getWorld().equals(event.getBlock().getWorld()) && model.getLocation().distance(event.getBlock().getLocation()) < (int) Math.round(20 * Math.pow(model.getExperience(Aspects.CRAFTING_ASPECT_I), 0.15))) {
                        if (processAbility1(model, ability)) {
                            Object rawResult = ability.getMethod().invoke(ability.getAspect(), event);
                            processAbility2(null, model, ability, rawResult);
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
            // Process the command
            try {
                if (args.length == 2 && "info".equals(args[1])) {
                    if (abilityInfo(player, args[0].toLowerCase())) {
                        DGGame.CONSOLE.info(event.getPlayer().getName() + " used the command: /" + message);
                        event.setCancelled(true);
                        return;
                    }
                }
                if (bindAbility(player, args[0].toLowerCase())) {
                    DGGame.CONSOLE.info(event.getPlayer().getName() + " used the command: /" + message);
                    event.setCancelled(true);
                }
            } catch (Exception errored) {
                // Not a command
                errored.printStackTrace();
            }
        }
    }

    boolean abilityInfo(Player player, String command) {
        for (AbilityMetaData ability : REGISTERED_ABILITIES.values()) {
            if (ability.getCommand().equals(command)) {
                player.sendMessage(StringUtil2.chatTitle(ability.getName()));
                player.sendMessage(" - Aspect: " + ability.getAspect().getGroup().getColor() + ability.getAspect().getGroup().getName() + " " + ability.getAspect().getTier().name());
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

    boolean bindAbility(Player player, String command) {
        // Is this a correct command?
        if (!REGISTERED_COMMANDS.keySet().contains(command)) {
            return false;
        }

        // Can't bind to air.
        if (player.getItemInHand() == null || Material.AIR.equals(player.getItemInHand().getType())) {
            abilityInfo(player, command);
            return true;
        }

        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        Material material = player.getItemInHand().getType();
        AbilityMetaData bound = model.getBound(material);
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
            AbilityMetaData ability = fromCommand(command);
            if (ability.getCommand().equals(command) && model.getAspects().contains(ability.getAspect().getGroup().getName() + " " + ability.getAspect().getTier().name()) && ability.getCommand().equals(command)) {
                model.bind(ability, material);
                player.sendMessage(ChatColor.YELLOW + ability.getName() + " has been bound to " + StringUtil2.beautify(material.name()) + ".");
                return true;
            }
        }
        return false;
    }

    boolean processAbility1(PlayerModel model, AbilityMetaData ability) {
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
            if (DGGame.SERV_R.contains(model.getMojangId(), ability + ":delay")) {
                return false;
            }
            if (DGGame.SERV_R.contains(model.getMojangId(), ability + ":cooldown")) {
                model.getOfflinePlayer().getPlayer().sendMessage(ChatColor.YELLOW + ability.getName() + " is on a cooldown.");
                return false;
            }
        }
        return true;
    }

    void processAbility2(Player player, PlayerModel model, AbilityMetaData ability, @Nullable Object rawResult) {
        // Check for result
        if (rawResult == null) {
            throw new NullPointerException("An ability returned null while casting.");
        }

        try {
            // Process result
            AbilityResult result = (AbilityResult) rawResult;
            switch (result) {
                case SUCCESS: {
                    break;
                }
                case NO_TARGET_FOUND: {
                    player.sendMessage(ChatColor.YELLOW + "No target found.");
                    return;
                }
                case OTHER_FAILURE: {
                    return;
                }
            }

            // Process ability
            double cost = ability.getCost();
            long delay = ability.getDelay();
            long cooldown = ability.getCooldown();

            if (delay > 0) {
                DGGame.SERV_R.put(model.getMojangId(), ability + ":delay", true, delay, TimeUnit.MILLISECONDS);
            }
            if (cooldown > 0) {
                DGGame.SERV_R.put(model.getMojangId(), ability + ":cooldown", true, cooldown, TimeUnit.MILLISECONDS);
            }
            if (cost > 0) {
                model.setFavor(model.getFavor() - cost);
            }
        } catch (Exception ignored) {
        }
    }

    public List<AbilityMetaData> getAbilities(Aspect aspect) {
        Class<? extends Aspect> deityClass = aspect.getClass();
        List<AbilityMetaData> abilityMetaDatas = new ArrayList<>();
        for (Method method : deityClass.getMethods()) {
            if (method.isAnnotationPresent(Ability.class)) {
                Ability ability = method.getAnnotation(Ability.class);
                abilityMetaDatas.add(new AbilityMetaData(aspect, method, ability));
            }
        }
        return abilityMetaDatas;
    }

    public void registerAbilities() {
        for (Aspect aspect : Aspects.values()) {
            Class<? extends Aspect> deityClass = aspect.getClass();
            for (Method method : deityClass.getMethods()) {
                if (method.isAnnotationPresent(Ability.class)) {
                    Ability ability = method.getAnnotation(Ability.class);
                    register(aspect, method, ability);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    void register(Aspect aspect, Method method, Ability ability) {
        if (ability.placeholder()) return;
        Class<?>[] paramaters = method.getParameterTypes();
        try {
            if (paramaters.length < 1) {
                DGGame.CONSOLE.severe("An ability (" + ability.name() + ") tried to register without any parameters.");
                return;
            }
            Class<? extends Event> eventClass = (Class<? extends Event>) paramaters[0];
            AbilityMetaData data = new AbilityMetaData(aspect, method, ability);
            REGISTERED_ABILITIES.put(eventClass.getName(), data);
            if (!"".equals(data.getCommand())) {
                REGISTERED_COMMANDS.put(data.getCommand(), data);
            }
        } catch (Exception oops) {
            oops.printStackTrace();
        }
    }

    public AbilityMetaData fromCommand(String commandName) {
        if (REGISTERED_COMMANDS.containsKey(commandName)) {
            return REGISTERED_COMMANDS.get(commandName);
        }
        return null;
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

            if (Aspects.hasAspect(player, Aspects.BLOODLUST_ASPECT_HERO)) {
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

            if (Aspects.hasAspect(player, Aspects.LIGHTNING_ASPECT_II)) {
                if (EntityDamageEvent.DamageCause.FALL.equals(event.getCause())) {
                    event.setCancelled(true);
                }
            }

            if (Aspects.hasAspect(player, Aspects.WATER_ASPECT_II)) {
                if (EntityDamageEvent.DamageCause.DROWNING.equals(event.getCause())) {
                    event.setCancelled(true);
                    player.setRemainingAir(player.getMaximumAir());
                }
            }

            if (Aspects.hasAspect(player, Aspects.FIRE_ASPECT_I)) {
                if (EntityDamageEvent.DamageCause.FIRE.equals(event.getCause()) || EntityDamageEvent.DamageCause.FIRE_TICK.equals(event.getCause())) {
                    event.setCancelled(true);
                    player.setRemainingAir(player.getMaximumAir());
                }
            }
        }
    }

    /**
     * Various player move abilities, these must be done by hand, and directly in this method.
     *
     * @param event The move event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (ZoneUtil.inNoDGCZone(event.getPlayer().getLocation())) return;

        Player player = event.getPlayer();

        if (Aspects.hasAspect(player, Aspects.WATER_ASPECT_II)) {
            Material locationMaterial = player.getLocation().getBlock().getType();
            if (player.isSneaking() && (locationMaterial.equals(Material.STATIONARY_WATER) || locationMaterial.equals(Material.WATER))) {
                Vector victor = (player.getPassenger() != null && player.getLocation().getDirection().getY() > 0 ? player.getLocation().getDirection().clone().setY(0) : player.getLocation().getDirection()).normalize().multiply(1.3D);
                player.setVelocity(victor);
            }
        }
    }

    /**
     * Various entity target events, these must be done by hand, and directly in this method.
     *
     * @param event The target event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEvent(EntityTargetEvent event) {
        Entity entity = event.getEntity();

        // Demon Aspect III
        if (entity instanceof LivingEntity) {
            if ((entity instanceof Zombie) || (entity instanceof Skeleton)) {
                if (!DGGame.PLAYER_R.fromPlayer((Player) event.getTarget()).hasAspect(Aspects.DEMON_ASPECT_III)) return;
                event.setCancelled(true);
            }
        }
    }
}
