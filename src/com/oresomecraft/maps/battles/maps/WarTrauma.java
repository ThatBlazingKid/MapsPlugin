package com.oresomecraft.maps.battles.maps;

import com.oresomecraft.OresomeBattles.api.BattlePlayer;
import com.oresomecraft.OresomeBattles.api.Gamemode;
import com.oresomecraft.OresomeBattles.api.InvUtils;
import com.oresomecraft.maps.MapConfig;
import com.oresomecraft.maps.battles.BattleMap;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

@MapConfig
public class WarTrauma extends BattleMap implements Listener {

    public WarTrauma() {
        super.initiate(this, name, fullName, creators, modes);
        disableDrops(new Material[]{Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.ARROW, Material.IRON_CHESTPLATE, Material.BOW, Material.IRON_SWORD, Material.LEATHER_HELMET});
        setAutoSpawnProtection(5);
    }

    String name = "trauma";
    String fullName = "War Trauma";
    String creators = "_Moist, niceman506 and psgs";
    Gamemode[] modes = {Gamemode.TDM};

    public void readyTDMSpawns() {
        redSpawns.add(new Location(w, -35, 70, 8));
        blueSpawns.add(new Location(w, -35, 70, 190));
    }

    public void readyFFASpawns() {
        FFASpawns.add(new Location(w, -35, 70, 8));
        FFASpawns.add(new Location(w, -35, 70, 190));
        defineRegion(x1, x2, y1, y2, z1, z2);
    }

    public void applyInventory(final BattlePlayer p) {
        Inventory i = p.getInventory();

        ItemStack BREAD = new ItemStack(Material.BREAD, 4);
        ItemStack BOW = new ItemStack(Material.BOW, 1);
        ItemStack GAPPLE = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemStack ARROWS = new ItemStack(Material.ARROW, 32);
        ItemStack LEATHER_HELMET = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemStack IRON_CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemStack LEATHER_PANTS = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack LEATHER_BOOTS = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemStack IRON_SWORD = new ItemStack(Material.IRON_SWORD, 1);
        ItemStack DIRT = new ItemStack(Material.DIRT, 16);
        ItemStack IRON_SHOVEL = new ItemStack(Material.IRON_SPADE, 1);

        ItemMeta bow = BOW.getItemMeta();
        bow.setDisplayName(ChatColor.RED + "Missile Launcher");

        List<String> bowLore = new ArrayList<String>();
        bowLore.add(ChatColor.BLUE + "Stand on the sponge in the tanks to shoot missiles!");
        bow.setLore(bowLore);
        BOW.setItemMeta(bow);

        ItemMeta arrows = ARROWS.getItemMeta();
        arrows.setDisplayName(ChatColor.RED + "Missiles");
        ARROWS.setItemMeta(arrows);

        InvUtils.colourArmourAccordingToTeam(p, new ItemStack[]{LEATHER_HELMET, LEATHER_PANTS, LEATHER_BOOTS});

        p.getInventory().setBoots(LEATHER_BOOTS);
        p.getInventory().setLeggings(LEATHER_PANTS);
        p.getInventory().setChestplate(IRON_CHESTPLATE);
        p.getInventory().setHelmet(LEATHER_HELMET);

        i.setItem(0, IRON_SWORD);
        i.setItem(1, BOW);
        i.setItem(2, IRON_SHOVEL);
        i.setItem(3, BREAD);
        i.setItem(8, DIRT);
        i.setItem(4, GAPPLE);
        i.setItem(29, ARROWS);
    }

    // Region. (Top corner block and bottom corner block.
    // Top left corner.
    public int x1 = -66;
    public int y1 = 117;
    public int z1 = 199;

    //Bottom right corner.
    public int x2 = -8;
    public int y2 = 56;
    public int z2 = 3;

    // <------- Spire Turrets ------->
    @EventHandler(priority = EventPriority.NORMAL)
    public void explodingArrow(ProjectileHitEvent event) {
        Entity projectile = event.getEntity();
        World w = projectile.getWorld();
        Location hit = projectile.getLocation();

        if (hit.getWorld().getName().equals(name)) {

            if (projectile instanceof Arrow) {
                Arrow arrow = (Arrow) projectile;
                ProjectileSource shooter = arrow.getShooter();

                if (shooter instanceof Player) {
                    Player player = (Player) shooter;
                    Location location = player.getLocation();
                    Block block2 = location.getBlock();
                    Block block = block2.getRelative(BlockFace.DOWN, 2);
                    Material material = block.getType();
                    ItemStack itemStack = player.getItemInHand();
                    Material material2 = itemStack.getType();

                    if (material2 == Material.BOW && material == Material.SPONGE) {
                        w.createExplosion(hit, 2);
                        Bukkit.getWorld(name).playEffect(arrow.getLocation(), Effect.STEP_SOUND, 10);

                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void bulletAway(ProjectileHitEvent event) {
        Entity player = event.getEntity();
        Location location = player.getLocation();
        Block block = location.getBlock();
        Material material = block.getType();

        if (contains(location, x1, x2, y1, y2, z1, z2)) {

            if (player instanceof Arrow) {
                Arrow arrow = (Arrow) player;
                arrow.remove();

                if (material == Material.THIN_GLASS) {
                    block.breakNaturally();
                }

            }
        }
    }
}

