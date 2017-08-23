package ru.stormcraft.adamagemodifier;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamagedAllTypes implements Listener {
	
	@EventHandler
	public void damaged(EntityDamageEvent event){
		if(Main.debug){    System.out.println("ADMGM-DEBUG: Entity type: "+event.getEntityType().toString());    } //debug
		
		if(Main.Ents.containsKey(event.getEntityType())){ 
			
			if(Main.debug){    System.out.println("ADMGM-DEBUG: Damage Cause: "+event.getCause().toString());    } //debug
			
			if(Main.Causes.containsKey(event.getCause())){
				
				double dmg = event.getDamage()*Main.Causes.get(event.getCause())*Main.Ents.get(event.getEntityType());
				if(dmg<0){   dmg *= -1.0;   }
				if(Main.debug){    System.out.println("ADMGM-DEBUG: Applying damage: "+ dmg);    }
				
				event.setDamage(dmg);
				
				
				
			}
		}
	}
	
}
