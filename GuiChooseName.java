package net.minecraft.src;

public class GuiChooseName extends GuiScreen{
	int i, j, k;
	public GuiChooseName(int x, int y, int z){
		this.i = x;
		this.j = y;
		this.k = z;
	}
	
	
	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		this.drawCenteredString(mc.fontRenderer, "Choose Name", width/2, 20, 0xdd0000);
		field.drawTextBox();
		super.drawScreen(i, j, f);
	}

	@Override
	protected void keyTyped(char c, int i) {
		if(i == 1){
			dontSpawn = true;
			mc.displayGuiScreen(null);
		}else if(c == '\n'){
			mc.displayGuiScreen(null);
		}else{
			field.textboxKeyTyped(c, i);
			super.keyTyped(c, i);
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		field.mouseClicked(i, j, k);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 0){
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public void initGui() {
		controlList.clear();
		controlList.add(new GuiButton(0, (width-200)/2, height - 50, "Done"));
		field = new GuiTextField(this, mc.fontRenderer, (width - 200)/2, 50, 200, 20, mod_MyPeople.lastName);
	}
	GuiTextField field;
	@Override
	public void updateScreen() {
		field.updateCursorCounter();
	}

	@Override
	public void onGuiClosed() {
		mod_MyPeople.lastName = field.getText();
		if(!dontSpawn){
			EntityMyPerson person = new EntityMyPerson(mc.theWorld);
			person.changeSkin(mod_MyPeople.lastName);
			person.setLocationAndAngles((double)i + 0.5D, j, (double)k + 0.5D, 0.0F, 0.0F);
			mc.theWorld.spawnEntityInWorld(person);
			
		}
	}

	boolean dontSpawn = false;
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	

}
