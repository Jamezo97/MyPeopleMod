package net.minecraft.src;

public class GuiGlobalPeopleOptions extends GuiScreen{

	int pState = 0;
	boolean getItems = true, pRetaliate = true, pSprintEnemy = true;
	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		name.drawTextBox();
		super.drawScreen(i, j, f);
	}

	@Override
	protected void keyTyped(char c, int i) {
		name.textboxKeyTyped(c, i);
		super.keyTyped(c, i);
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		name.mouseClicked(i, j, k);
		super.mouseClicked(i, j, k);
	}

	@Override
	protected void actionPerformed(GuiButton gb) {
		if(gb.id == 0){
			pState = EntityMyPerson.changeState(pState);
			type.displayString = EntityMyPerson.getState(pState);
		}else if(gb.id==1){
			setAllType(pState);
		}if(gb.id == 2){
			getItems = !getItems;
			grabItems.displayString =  "Get Nearby Items: " + (getItems?"Yes":"No");
		}else if(gb.id==3){
			setAllGrabItems(getItems);
		}if(gb.id == 4){
			pSprintEnemy = !pSprintEnemy;
			sprintAfterMonster.displayString = "Sprint At Enemy: " + (pSprintEnemy?"Yes":"No");
		}else if(gb.id==5){
			setAllSprint(pSprintEnemy);
		}if(gb.id == 6){
			pRetaliate = !pRetaliate;
			retaliate.displayString = "Retaliate: " + (pRetaliate?"Yes":"No");
		}else if(gb.id==7){
			setAllRetaliate(pRetaliate);
		}else if(gb.id==8){
			setAllName(name.getText());
		}else if(gb.id == 100){
			mc.displayGuiScreen(new GuiChooserPlayerToFind());
		}
	}
	
	private void setAllName(String s) {
		for(int a = 0; a < mc.theWorld.playerEntities.size(); a++){
			if(mc.theWorld.playerEntities.get(a).getClass().equals(net.minecraft.src.EntityMyPerson.class)){
				((EntityMyPerson)mc.theWorld.playerEntities.get(a)).username = s;
				((EntityMyPerson)mc.theWorld.playerEntities.get(a)).justLoaded = true;
				//mc.theWorld.obtainEntitySkin(((EntityMyPerson)mc.theWorld.playerEntities.get(a)));
			}
		}
	}

    private void setAllRetaliate(boolean b) {
		for(int a = 0; a < mc.theWorld.playerEntities.size(); a++){
			if(mc.theWorld.playerEntities.get(a).getClass().equals(net.minecraft.src.EntityMyPerson.class)){
				((EntityMyPerson)mc.theWorld.playerEntities.get(a)).retaliate = b;
			}
		}
	}

	private void setAllSprint(boolean b) {
		for(int a = 0; a < mc.theWorld.playerEntities.size(); a++){
			if(mc.theWorld.playerEntities.get(a).getClass().equals(net.minecraft.src.EntityMyPerson.class)){
				((EntityMyPerson)mc.theWorld.playerEntities.get(a)).sprintEnemy = b;
			}
		}
	}

	private void setAllGrabItems(boolean b) {
		for(int a = 0; a < mc.theWorld.playerEntities.size(); a++){
			if(mc.theWorld.playerEntities.get(a).getClass().equals(net.minecraft.src.EntityMyPerson.class)){
				((EntityMyPerson)mc.theWorld.playerEntities.get(a)).getItems = b;
			}
		}
	}

	private void setAllType(int i) {
		for(int a = 0; a < mc.theWorld.playerEntities.size(); a++){
			if(mc.theWorld.playerEntities.get(a).getClass().equals(net.minecraft.src.EntityMyPerson.class)){
				((EntityMyPerson)mc.theWorld.playerEntities.get(a)).state = i;
			}
		}
	}

	GuiButton type, grabItems, sprintAfterMonster, retaliate;
    GuiTextField name;
    int state;
	public void initGui() {
		int width2 = 125;
		controlList.clear();
		controlList.add(type = new GuiButton(0, (width-(width2*2+10))/2, 5, width2, 20, EntityMyPerson.getState(pState)));
		controlList.add(new GuiButton(1, (width-(width2*2+10))/2+(width2+5), 5, width2, 20, "Set Universal"));
		
		controlList.add(grabItems = new GuiButton(2, (width-(width2*2+10))/2, 30, width2, 20, "Get Nearby Items: " + (getItems?"Yes":"No")));
		controlList.add(new GuiButton(3, (width-(width2*2+10))/2+(width2+5), 30, width2, 20, "Set Universal"));
		
		controlList.add(sprintAfterMonster = new GuiButton(4, (width-(width2*2+10))/2, 55, width2, 20, "Sprint At Enemy: " + (pSprintEnemy?"Yes":"No")));
		controlList.add(new GuiButton(5, (width-(width2*2+10))/2+(width2+5), 55, width2, 20, "Set Universal"));
		
		controlList.add(retaliate = new GuiButton(6, (width-(width2*2+10))/2, 80, width2, 20, "Retaliate: " + (pRetaliate?"Yes":"No")));
		controlList.add(new GuiButton(7, (width-(width2*2+10))/2+(width2+5), 80, width2, 20, "Set Universal"));
		
		controlList.add(new GuiButton(8, (width-(width2*2+10))/2+(width2+5), 105, width2, 20, "Set Universal"));
		name = new GuiTextField(this, mc.fontRenderer, (width-(width2*2+10))/2, 105, width2, 20, "");
		
		controlList.add(new GuiButton(100, (width-200)/2, 140, "Done"));
	}

	public void updateScreen() {
		name.updateCursorCounter();
	}

	public boolean doesGuiPauseGame() {
		return true;
	}
	
	

}
