package ru.stormcraft.adamagemodifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	static HashMap<DamageCause, Double> Causes;
	static HashMap<EntityType, Double> Ents;
	static boolean debug;
	EntityDamagedAllTypes listener;
	@Override
	public void onEnable() {
		loadConfiguration();
		Causes = getEnabledTps();
		Ents = getEnabledEnts();
		debug = getConfig().getBoolean("debug");
		register();
	}
	 
	@Override
	public void onDisable() {
		System.out.println("ADMGM: Disabling...");
		Causes.clear();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(label.equalsIgnoreCase("admgm")){
			if(sender instanceof Player){
				
				Player player = (Player) sender;
				if(player.hasPermission("admgm.reload")){
					if(args.length<1){
						player.sendMessage("Not enough arguments!");
						return false;
					}
					if(args[0].equalsIgnoreCase("reload")){
						reloadCfg();
						player.sendMessage("Reloaded configs!");
						return true;
					}else{
						return false;
					}
				}else{
					player.sendMessage("You don't have permission!");
					return true;
				}
			}else if(sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender){
				if(args.length<1){
					System.out.println("ADMGM: Not enough arguments!");
					return false;
				}
				reloadCfg();
			}
			
		}
		return true;
	}
	public void loadConfiguration(){

		getConfig().addDefault("debug", false);
		for(DamageCause cos : DamageCause.values()){
			getConfig().addDefault("types.damage."+cos.toString()+".enable",false);
			getConfig().addDefault("types.damage."+cos.toString()+".counter",1.0);
		}
		for(EntityType entity : EntityType.values()){
			if ((entity.isSpawnable()) && (entity.isAlive())) {
		       getConfig().addDefault("types.entity."+entity.toString()+".enable",false);
		       getConfig().addDefault("types.entity."+entity.toString()+".counter",1.0);
		    }
		}
		getConfig().options().copyDefaults(true); 
		saveConfig();
	}
	public HashMap<DamageCause, Double> getEnabledTps(){
		HashMap<DamageCause, Double> list = new HashMap<DamageCause, Double>();
		for(DamageCause cos : DamageCause.values()){
			if(getConfig().getBoolean("types.damage."+cos.toString()+".enable")){
				list.put(cos, getConfig().getDouble("types.damage."+ cos.toString() +".counter"));
			}
		}
		return list;
	}
	
	public HashMap<EntityType, Double> getEnabledEnts(){
		HashMap<EntityType, Double> list = new HashMap<EntityType, Double>();
		for(EntityType ent : EntityType.values()){
			if ((ent.isSpawnable()) && (ent.isAlive())) {
				if(getConfig().getBoolean("types.entity."+ent.toString()+".enable")){
					list.put(ent, getConfig().getDouble("types.entity."+ ent.toString() +".counter"));
				}
			}
		}
		return list;
	}
	
	public void reloadCfg(){
		Ents.clear();
		Causes.clear();
		EntityDamageEvent.getHandlerList().unregister(listener);
		reloadConfig();
		Ents = getEnabledEnts();
		Causes = getEnabledTps();
		register();
	}
	public void register(){
		if(!Causes.isEmpty()&&!Ents.isEmpty()){
			listener = new EntityDamagedAllTypes();
			getServer().getPluginManager().registerEvents(listener, this);
			System.out.println("ADMGM: Loaded "+Causes.size()+" damage types: ");
	
			Set<Map.Entry<DamageCause, Double>> set = Causes.entrySet();
			for (Entry<DamageCause, Double> me : set) {
				System.out.println("ADMGM: "+me.getKey() + " : " + me.getValue());
			}
	
			System.out.println("ADMGM: Loaded "+Causes.size()+" enteties: ");
	
			Set<Map.Entry<EntityType, Double>> set2 = Ents.entrySet();
			for (Entry<EntityType, Double> me : set2) {
				System.out.println("ADMGM: "+me.getKey() + " : " + me.getValue());
			}
		}else{
			if(Causes.isEmpty()){
				System.out.println("ADMGM: No Damage types loaded! Plugin will not work!");
			}
			if(Ents.isEmpty()){
				System.out.println("ADMGM: No Entities loaded! Plugin will not work!");
			}
			//System.out.println("ADMGM: Disabling...");
		}
	}
}
