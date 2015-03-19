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
import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.living.monster.Skeleton;
import org.spongepowered.api.entity.living.monster.Zombie;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.block.data.FurnaceSmeltItemEvent;
import org.spongepowered.api.event.entity.EntityChangeHealthEvent;
import org.spongepowered.api.event.entity.EntityTargetEntityEvent;
import org.spongepowered.api.event.entity.living.player.PlayerChangeHealthEvent;
import org.spongepowered.api.event.entity.living.player.PlayerChatEvent;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.event.entity.living.player.PlayerMoveEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.event.Event;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;
import org.spongepowered.api.world.World;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class AbilityRegistry {
    // FIXME Do we really need two collections for the same data? This is expensive...

    private static final ConcurrentMap<String, AbilityMetaData> REGISTERED_COMMANDS = new ConcurrentHashMap<>();
    private static final Multimap<String, AbilityMetaData> REGISTERED_ABILITIES = Multimaps.newListMultimap(new ConcurrentHashMap<>(), () -> new ArrayList<>(0));

    @Subscribe(order = Order.FIRST)
    private void onEvent(PlayerInteractEvent event) {
        EntityInteractionType type = event.getInteractionType();
        if (EntityInteractionTypes.ATTACK.equals(type) || EntityInteractionTypes.USE.equals(type)) {
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
    }

    @Subscribe(order = Order.FIRST)
    private void onEvent(EntityChangeHealthEvent event) {
        if (event.getCause().isPresent() && event.getCause().get().getCause() instanceof Entity) {
            for (AbilityMetaData ability : REGISTERED_ABILITIES.get(event.getClass().getName())) {
                try {
                    if (event.getCause().get().getCause() instanceof Player) {
                        Player player = (Player) event.getCause().get().getCause();
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
    }

    @Subscribe(order = Order.EARLY)
    private void onEvent(FurnaceSmeltItemEvent event) {
        for (AbilityMetaData ability : REGISTERED_ABILITIES.get(event.getClass().getName())) {
            for (PlayerModel model : DGGame.PLAYER_R.fromAspect(ability.getAspect())) {
                try {
                    if (model.getOnline() && model.getLocation().getExtent().equals(event.getBlock().getExtent()) && model.getLocation().getBlock().getPosition().distance(event.getBlock().getPosition()) < (int) Math.round(20 * Math.pow(model.getExperience(Aspects.CRAFTING_ASPECT_I), 0.15))) {
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

    @Subscribe(order = Order.LATE)
    private void bindCommands(PlayerChatEvent event) {
        String message = event.getMessage().toLegacy(); // TODO Does this work?

        if (message.startsWith("/")) {
            message = message.substring(1);
            String[] args = message.split("\\s+");
            Player player = event.getPlayer();

            if (!ZoneUtil.inNoDGZone(event.getPlayer().getLocation())) {
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
        if (player.getItemInHand().isPresent()) {
            abilityInfo(player, command);
            return true;
        }

        PlayerModel model = DGGame.PLAYER_R.fromPlayer(player);
        ItemType material = player.getItemInHand().get().getItem();
        AbilityMetaData bound = model.getBound(material);
        if (bound != null) {
            if (!bound.getCommand().equals(command)) {
                player.sendMessage(TextColors.RED + "This item already has /" + bound.getCommand() + " bound to it.");
                return true;
            } else {
                model.unbind(bound);
                player.sendMessage(TextColors.YELLOW + bound.getName() + " has been unbound.");
                return true;
            }
        } else {
            AbilityMetaData ability = fromCommand(command);
            if (ability.getCommand().equals(command) && model.getAspects().contains(ability.getAspect().getGroup().getName() + " " + ability.getAspect().getTier().name()) && ability.getCommand().equals(command)) {
                model.bind(ability, material);
                player.sendMessage(TextColors.YELLOW + ability.getName() + " has been bound to " + StringUtil2.beautify(material.getId()) + ".");
                return true;
            }
        }
        return false;
    }

    boolean processAbility1(PlayerModel model, AbilityMetaData ability) {
        if (ZoneUtil.isNoDGWorld((World) model.getLocation().getExtent())) return false;
        if (!ability.getType().equals(Ability.Type.PASSIVE)) {
            if ((ability.getType().equals(Ability.Type.OFFENSIVE) || ability.getType().equals(Ability.Type.ULTIMATE)) && ZoneUtil.inNoPvpZone(model.getLocation())) {
                return false;
            }
            if (model.getBound(ability) == null) {
                return false;
            }
            if (!model.getPlayer().getItemInHand().isPresent()) {
                return false;
            }
            if (!model.getPlayer().getItemInHand().get().getItem().equals(model.getBound(ability))) {
                return false;
            }

            double cost = ability.getCost();
            if (model.getFavor() < cost) {
                model.getPlayer().sendMessage(TextColors.YELLOW + ability.getName() + " requires more favor.");
                return false;
            }
            if (DGGame.MISC_R.contains(model.getMojangId(), ability + ":delay")) {
                return false;
            }
            if (DGGame.MISC_R.contains(model.getMojangId(), ability + ":cooldown")) {
                model.getPlayer().sendMessage(TextColors.YELLOW + ability.getName() + " is on a cooldown.");
                return false;
            }
        }
        return true;
    }

    void processAbility2(Player player, PlayerModel model, AbilityMetaData ability, Object rawResult) {
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
                    player.sendMessage(TextColors.YELLOW + "No target found.");
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
                DGGame.MISC_R.put(model.getMojangId(), ability + ":delay", true, delay, TimeUnit.MILLISECONDS);
            }
            if (cooldown > 0) {
                DGGame.MISC_R.put(model.getMojangId(), ability + ":cooldown", true, cooldown, TimeUnit.MILLISECONDS);
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
                DGGame.CONSOLE.error("An ability (" + ability.name() + ") tried to register without any parameters.");
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
    @Subscribe(order = Order.FIRST)
    private void onNoDamageAbilities(PlayerChangeHealthEvent event) {
        Player player = event.getEntity();

        if (!event.getCause().isPresent()) {
            return;
        }

        Object cause = event.getCause().get().getCause();

        if (Aspects.hasAspect(player, Aspects.BLOODLUST_ASPECT_HERO)) {
            double damage = player.getHealth() - event.getOldHealth();
            if (player.getHealth() <= damage) {
                if (cause instanceof Entity) {
                    event.setNewHealth(player.getHealth() - 1);
                }
            }
        }

        /* FIXME Other damage causes aren't in Sponge yet
        if (Aspects.hasAspect(player, Aspects.LIGHTNING_ASPECT_II)) {
            if (event.getCause()) { // Fall damage
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
        */
    }

    /**
     * Various player move abilities, these must be done by hand, and directly in this method.
     *
     * @param event The move event.
     */
    @Subscribe(order = Order.LATE)
    private void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (ZoneUtil.inNoDGZone(event.getPlayer().getLocation())) return;

        Player player = event.getPlayer();

        if (Aspects.hasAspect(player, Aspects.WATER_ASPECT_II)) {
            BlockType locationMaterial = player.getLocation().getBlock().getType();
            if (player.isSneaking() && locationMaterial.equals(BlockTypes.WATER)) {
                Vector3d position = player.getLocation().getPosition();
                Vector3d victor = (player.getPassenger().isPresent() && position.getY() > 0 ? new Vector3d(position.getX(), 0, position.getZ()) : position.normalize().mul(1.3D));
                player.setVelocity(victor);
            }
        }
    }

    /**
     * Various entity target events, these must be done by hand, and directly in this method.
     *
     * @param event The target event.
     */
    @Subscribe(order = Order.LATE)
    private void onEvent(EntityTargetEntityEvent event) {
        Entity entity = event.getEntity();

        if (event.getTargetedEntity().get() instanceof Player) {
            // Demon Aspect III
            if (entity instanceof Zombie || entity instanceof Skeleton) {
                if (DGGame.PLAYER_R.fromPlayer((Player) event.getTargetedEntity().get()).hasAspect(Aspects.DEMON_ASPECT_III)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
