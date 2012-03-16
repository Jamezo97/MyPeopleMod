package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiCentrifuge extends GuiContainer{

	TileEntityCentrifuge te = null;
	
	public GuiCentrifuge(EntityPlayer p, TileEntityCentrifuge teC) {
		super(new ContainerCentrifuge(p.inventory, teC));
		te = teC;
		
	}
    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Centrifuge", 8, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 106) + 2, 0x404040);
        if(te.isBurning()){
        	int k = mc.renderEngine.getTexture("/MyPeople/centrifuge.png");
        	GL11.glEnable(GL11.GL_BLEND);
        	GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
            mc.renderEngine.bindTexture(k);
        	int minus = (int)Math.round(14d*te.getFuelDone());
        	drawTexturedModalRect(81, 30+(14-minus), 177, 1+(14-minus), 14, minus);
        	GL11.glDisable(GL11.GL_BLEND);
        }
    }
    
    protected void handleMouseClick(Slot slot, int i, int j, boolean flag)
    {
        if (slot != null)
        {
        	int b = slot.slotNumber;
        	if(te.isSpinning() && (b == 0 || b == 1 || b == 2 || b == 3)){
        		return;
        	}
            i = slot.slotNumber;
        }
        mc.playerController.windowClick(inventorySlots.windowId, i, j, flag, mc.thePlayer);
    }
    
    

    @Override
	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 0){
			if(te.canSpin() && te.getItemBurnTime(te.items[4])>0){
				te.spinPressed = true;
			}
		}
	}
	@Override
	public void updateScreen() {
		spin.enabled = !te.spinPressed;
	}
	GuiButton spin;
	public void initGui() {
		super.initGui();
		controlList.clear();
		controlList.add(spin = new GuiButton(0, guiLeft+100, guiTop+6, 72, 20, "Spin"));
		spin.enabled = te.spinPressed;;
	}
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	int k = mc.renderEngine.getTexture("/MyPeople/centrifuge.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        drawTexturedModalRect(8+l, 72+i1, 0, 166, (int)Math.round(160d*te.getPercentDone()), 6);
    }

}
