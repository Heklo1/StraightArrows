package me.heklo.straightarrows;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class StraightArrows
        extends JavaPlugin
        implements Listener
{
    public void onEnable()
    {
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginCommand("straightarrows").setExecutor(new StraightArrowsCommandExecutor(this));
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e)
    {
        Entity entity = e.getEntity();
        Vector dir = entity.getLocation().getDirection();

        Arrow a = (Arrow)e.getProjectile();
        double speed = a.getVelocity().length();
        Vector vel = dir.multiply(speed);
        a.setVelocity(vel);
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent e)
    {
        Player p = e.getPlayer();
        Vector initialVelocity = e.getVelocity();
        EntityDamageEvent event = p.getLastDamageCause();
        if ((event != null) && (!event.isCancelled()) && ((event instanceof EntityDamageByEntityEvent)))
        {
            Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
            if ((damager instanceof Arrow))
            {
                Arrow a = (Arrow)damager;
                if (a.getShooter().equals(p))
                {
                    double horizontalMultiplier = this.getConfig().getDouble("horizontal-multiplier", 1);
                    double verticalMultiplier = this.getConfig().getDouble("vertical-multiplier", 1);
                    double horizontalSpeed = Math.sqrt(initialVelocity.getX() * initialVelocity.getX() + initialVelocity.getZ() * initialVelocity.getZ()) * horizontalMultiplier;
                    double verticalSpeed = initialVelocity.getY() * verticalMultiplier;
                    Vector dir = a.getLocation().getDirection().normalize();
                    Vector newVelocity = new Vector(-dir.getX() * horizontalSpeed, initialVelocity.getY() * verticalSpeed, dir.getZ() * horizontalSpeed);
                    e.setVelocity(newVelocity);
                }
            }
        }
    }
}