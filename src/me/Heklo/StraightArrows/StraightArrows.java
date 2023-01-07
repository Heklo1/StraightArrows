package me.Heklo.StraightArrows;

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
    Bukkit.getPluginManager().registerEvents(this, this);
  }
  
  public void onDisable() {}
  
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
  public void onVelo(PlayerVelocityEvent e)
  {
    Player p = e.getPlayer();
    Vector velo = e.getVelocity();
    EntityDamageEvent event = p.getLastDamageCause();
    if ((event != null) && (!event.isCancelled()) && ((event instanceof EntityDamageByEntityEvent)))
    {
      Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
      if ((damager instanceof Arrow))
      {
        Arrow a = (Arrow)damager;
        if (a.getShooter().equals(p))
        {
          double speed = Math.sqrt(velo.getX() * velo.getX() + velo.getZ() * velo.getZ())+0.2;
          Vector dir = a.getLocation().getDirection().normalize();
          Vector newvelo = new Vector(dir.getX() * speed * -1.0D, velo.getY(), dir.getZ() * speed);
          e.setVelocity(newvelo);
        }
      }
    }
  }
}