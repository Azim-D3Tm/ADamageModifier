package ru.stormcraft.adamagemodifier;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamagedAllTypes implements Listener {
	@EventHandler
	public void damaged(EntityDamageEvent event){
		Main.debug("ADMGM-DEBUG: Entity type: "+event.getEntityType().toString());
		
		if(Main.Ents.containsKey(event.getEntityType())){ 
			
			Main.debug("ADMGM-DEBUG: Damage Cause: "+event.getCause().toString());
			
			if(Main.Causes.containsKey(event.getCause())){
				
				double dmg = event.getDamage()*Main.Causes.get(event.getCause())*Main.Ents.get(event.getEntityType());
				if(dmg<0){   dmg *= -1.0;   }
				Main.debug("ADMGM-DEBUG: Applying damage: "+ dmg);
				
				event.setDamage(dmg);
				
			}
		}
	}
	
}
