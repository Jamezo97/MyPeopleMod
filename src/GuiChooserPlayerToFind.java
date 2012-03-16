package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class GuiChooserPlayerToFind extends GuiScreen {

	GuiListBox box;
	ArrayList<EntityMyPerson> entities;
	int x = 0, xSize = 240;
	int y = 0, ySize = 224;
	@Override
	public void initGui() {
		controlList.clear();
		entities = getEntitiesFromList(mc.theWorld.playerEntities);
		
		//if(entities.size()>0){
			x = (width-xSize)/2;
			y = (height-ySize)/2;
			int idSelected = -1;
			for(int a = 0; a < entities.size(); a++){
				if(entities.get(a).equals(mod_MyPeople.entityToFind)){
					idSelected = a;
					break;
				}
			}
			String[][] data = getInfoFromPlayers(entities);
			controlList.add(box = new GuiListBox(0, x+8, y+8, 160, 206, data, 4));
			controlList.add(new GuiButton(1, x+178, y+10, 50, 20, "Clear"));
			controlList.add(new GuiButton(2, x+178, y+35, 50, 20, "Done"));
			controlList.add(new GuiButton(3, x+178, y+60, 50, 20, "Universal"));
			box.idSelected = idSelected;
		//}

	}
	
	public String[][] getInfoFromPlayers(ArrayList<EntityMyPerson> entities) {
		String[][] data = new String[entities.size()][3];
		for(int a = 0; a < entities.size(); a++){
			EntityMyPerson e = entities.get(a);
			data[a][0] = e.name + "  (Health: " + e.health + "/"+ e.getMaxHealth() + ")";
			data[a][1] = "X: " + Math.round(e.posX) + "   Y: " + Math.round(e.posY) + "   Z: " + Math.round(e.posZ);
			data[a][2] = "Status: " + (e.entityToAttack!=null?"Attacking " + e.entityToAttack.getEntityString():(e.pathToEntity != null?"Walking":"Waiting"));
		}
		return data;
	}

	public ArrayList<EntityMyPerson> getEntitiesFromList(List l){
		ArrayList<EntityMyPerson> l2 = new ArrayList<EntityMyPerson>();
		for(int a = 0; a < l.size(); a++){
			Entity e = (Entity)l.get(a);
			if(e.getClass().equals(EntityMyPerson.class)){
				l2.add((EntityMyPerson)e);
			}
		}
		return l2;
	}

	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/MyPeople/findplayers.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		super.drawScreen(i, j, f);
	}
	
	
	
	

	@Override
	protected void actionPerformed(GuiButton gb) {
		if(gb.id == 1){
			if(box != null){
				box.idSelected = -1;
			}
		}else if(gb.id == 2){
			mc.displayGuiScreen(null);
		}else if(gb.id == 3){
			mc.displayGuiScreen(new GuiGlobalPeopleOptions());
		}
	}

	@Override
	public void onGuiClosed() {
		if(box != null){
			int id = box.idSelected;
			if(id > -1 && id < entities.size()){
				Entity e = entities.get(box.idSelected);
				if(e != null){
					mod_MyPeople.entityToFind = e;
				}
			}else{
				mod_MyPeople.entityToFind = null;
			}
		}
	}

	@Override
	public void updateScreen() {
		entities = getEntitiesFromList(mc.theWorld.playerEntities);
		if(entities.size()>0){
			String[][] data = getInfoFromPlayers(entities);
			box.set(data);
		}
	}
	
	

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	
	
	

}
