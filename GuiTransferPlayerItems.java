package net.minecraft.src;

import java.awt.FontMetrics;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiTransferPlayerItems extends GuiContainer{
	EntityMyPerson player;
	EntityPlayer thePlayer;
	public GuiTransferPlayerItems(EntityMyPerson otherPlayer, EntityPlayer mainPlayer) {
		super(new ContainerTransferPlayerItems(mainPlayer, otherPlayer));
		player = otherPlayer;
		thePlayer = mainPlayer;
		xSize = 176;
		ySize = 212;
	}
	
	

    @Override
	protected void actionPerformed(GuiButton gb) {
		if(gb.id==10){
			mc.thePlayer.addExperience(player.experienceTotal);
			player.experience = 0;
			player.experienceLevel = 0;
			player.experienceTotal = 0;
			player.experienceValue = 0;
		}else if(gb.id==11){
			type.displayString = player.changeState();
		}else if(gb.id==12){
			player.getItems = !player.getItems;
			grabItems.displayString = "Get Nearby Items: " + (player.getItems?"Yes":"No");
		}else if(gb.id == 13){
			player.sprintEnemy = !player.sprintEnemy;
			sprintAfterMonster.displayString = "Sprint At Enemy: " + (player.sprintEnemy?"Yes":"No");
		}else if(gb.id == 14){
			player.retaliate = !player.retaliate;
			retaliate.displayString = "Retaliate: " + (player.retaliate?"Yes":"No");
		}else if(gb.id == 15){
			player.lumber = !player.lumber;
			lumber.displayString = "Lumber: " + (player.lumber?"Yes":"No");
		}else if(gb.id == 16){
			player.mine = !player.mine;
			mine.displayString = "Mine: " + (player.mine?"Yes":"No");
		}
	}

    GuiButton type, grabItems, sprintAfterMonster, retaliate, mine, lumber;
    GuiTextField name;
	@Override
	public void initGui() {
    	controlList.clear();
    	int bottom = height-20;
    	controlList.add(new GuiButton(10, 5, bottom-5, 100, 20, "Transfer XP"));
    	controlList.add(type = new GuiButton(11, 5, bottom-30, 100, 20, player.getState()));
    	name = new GuiTextField(this, mc.fontRenderer, 5, bottom-55, 100, 20, player.username);
    	controlList.add(grabItems = new GuiButton(12, 5, bottom-80, 120, 20, "Get Nearby Items: " + (player.getItems?"Yes":"No")));
		controlList.add(sprintAfterMonster = new GuiButton(13, 5, bottom-105, 120, 20, "Sprint At Enemy: " + (player.sprintEnemy?"Yes":"No")));
		controlList.add(retaliate = new GuiButton(14, 5, bottom-130, 120, 20, "Retaliate: " + (player.retaliate?"Yes":"No")));
		controlList.add(lumber = new GuiButton(15, 5, bottom-155, 120, 20, "Lumber: " + (player.lumber?"Yes":"No")));
		controlList.add(mine = new GuiButton(16, 5, bottom-180, 120, 20, "Mine: " + (player.mine?"Yes":"No")));
		super.initGui();
	}



	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Tranfer Items", ((xSize/2)-(this.fontRenderer.getStringWidth("Tranfer Items")/2)), -8, 0xc0c0c0);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int k = mc.renderEngine.getTexture("/MyPeople/transferplayeritems.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = guiLeft;
        int i1 = guiTop;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        drawFood();
        drawXP();
        name.drawTextBox();
    }
    
    

	@Override
	protected void mouseClicked(int i, int j, int k) {
		name.mouseClicked(i, j, k);
		super.mouseClicked(i, j, k);
	}
	

	protected void keyTyped(char c, int i) {
        if (i == 1)
        {
            mc.thePlayer.closeScreen();
        }
		name.textboxKeyTyped(c, i);
	}

	public void updateScreen() {
		super.updateScreen();
		name.updateCursorCounter();
	}

	private void drawXP() {
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (width-182)/2;
		int y = height-10;
		float xpPercent = player.experienceTotal;
		drawTexturedModalRect(x, y, 0, 64, 182, 5);
		drawTexturedModalRect(x, y, 0, 69, (int)Math.round(182f*xpPercent), 5);
		String s = "" + player.experienceTotal;
		int k3 = (x+91)-((fontRenderer.getStringWidth(s)) / 2);
		mc.fontRenderer.drawString(s, k3, height-12, 0x80ff20);
	}

	private void drawFood() {
		
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/gui/icons.png"));
		int x = 5;
		int y = (height-20)-200;
		int across = 10;
		int down = 1;
		this.drawRect(x-1, y-1, x+(across*9)+1, y+(down*9)+1, 0x50dddddd);
		double d = player.foodStats.getFoodLevel();
		int add = 0;
		if (player.isPotionActive(Potion.hunger))
        {
            add = 36;
        }
		double d1 = (d==0?0:d/2d);
		for(int a = 0; a < across; a++){
			for(int b = 0; b < down; b++){
				double level = 10-(b*5+a);
				int x1 = x + a*9;
				int y1 = y + b*9;
				byte type = 0;
				if(d1 > level-1 && d1 < level){
					type = 1;
				}else if(d1 >= level){
					type = 0;
				}else if(d1 < level){
					type = -1;
				}
				drawTexturedModalRect(x1, y1, 16, 27, 9, 9);
				if(type >= 0)drawTexturedModalRect(x1, y1, 52+(type*9)+add, 27, 9, 9);
			}
			player.foodStats.getFoodLevel();
		}
		
	}
	
	
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if(!player.username.equals(name.getText())){
			player.changeSkin(name.getText());
		}

	}



	public boolean doesGuiPauseGame() {
		return true;
	}
	
    public void handleKeyboardInput()
    {
    	if(name.isFocused){
            if (Keyboard.getEventKeyState())
            {
                if (Keyboard.getEventKey() == 87)
                {
                    mc.toggleFullscreen();
                    return;
                }
                keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
            }
    	}else{
    		super.handleKeyboardInput();
    	}

    }

}
