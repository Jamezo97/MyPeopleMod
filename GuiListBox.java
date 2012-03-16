package net.minecraft.src;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

public class GuiListBox extends GuiButton{
	String[][] items;
	int visibleEntries;
	int arrowX;
	int arrowY1, arrowY2;
	public GuiListBox(int i, int j, int k, int l, int i1, String[][] items, int visibleEntries) {
		super(i, j, k, l, i1, "");
		this.items = items;
		this.visibleEntries = visibleEntries;
		arrowX = j + l - 11;
		arrowY1 = k;
		arrowY2 = k + i1 - 9;
	}
	public void drawButton(Minecraft mc, int i, int j){
		if(idsOfArrows == null){
			setupArrows(mc);
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.drawRect(xPosition, yPosition, xPosition+width-11, yPosition+height, new Color(100, 100, 100).getRGB());
		this.drawRect(xPosition+width-11, yPosition+9, xPosition+width, yPosition+height-9, new Color(70, 70, 70).getRGB());
		int arrowStart = (int)Math.floor(yPosition + 9 + (position*getSizePerChunk()));
		this.drawRect(xPosition+width-11, arrowStart, xPosition+width, position+visibleEntries==items.length?(yPosition+height-9):((int)Math.floor(arrowStart+getSizePerChunk())), new Color(40, 40, 40).getRGB());
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glBindTexture(3553, idsOfArrows[0]);
		this.drawTexturedModalRect(arrowX, arrowY1, 0, 0, 11, 9);
		GL11.glBindTexture(3553, idsOfArrows[1]);
		this.drawTexturedModalRect(arrowX, arrowY2, 0, 0, 11, 9);
		int poss = 0;
		for(int a = 0; a < items.length; a++){
			if(a >= position && a < position+visibleEntries){
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				if(a == idSelected){
					this.drawRect(xPosition, yPosition + (poss*(height/visibleEntries)), xPosition+width-11, yPosition + ((poss+1)*(height/visibleEntries)), new Color(230, 230, 230).getRGB());
				}else{
					this.drawRect(xPosition, yPosition + (poss*(height/visibleEntries)), xPosition+width-11, yPosition + ((poss+1)*(height/visibleEntries)), new Color(50, 50, 50).getRGB());
				}
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				this.drawRect(xPosition+1, 1 + yPosition + ((poss)*(height/visibleEntries)), xPosition+width-11-1, yPosition + ((poss+1)*(height/visibleEntries))-1, new Color(125, 125, 125).getRGB());
				int perThingo = ((int)Math.floor(height/visibleEntries))/3;
				for(int b = 0; b < 3; b++){
					if(b < items[a].length){
						GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
						this.drawCenteredString(mc.fontRenderer, items[a][b], xPosition+((int)Math.floor((width-11)/2)), 2+yPosition + poss*(height/visibleEntries) + perThingo*b, 0xffffff);
					}
				}
				poss++;
			}
		}
		if(mouseIsDown){
			mouseDragged(mc, i, j);
		}
	}
	int position = 0;
	int idSelected = -1;
	public double getSizePerChunk(){
		if(items.length>visibleEntries){
			return ((double)(height-18))/(((double)items.length+1)-((double)visibleEntries));
		}else{
			return height-18;
		}
	}

    private void setupArrows(Minecraft mc) {
    	char[] Up = ("00000000000" +
                "01111111110" +
                "01111011110" +
                "01110201110" +
                "01102220110" +
                "01022222010" +
                "01000000010" +
                "01111111110" +
                "00000000000").toCharArray();
    	BufferedImage image = new BufferedImage(11, 9, 5);
    	for(int a = 0; a < image.getWidth(); a++){
    		for(int b = 0; b < image.getHeight(); b++){
    			int rgb = 0xffffff;
    			switch(Up[(b*image.getWidth())+a]){
    			case'0': rgb = -16777216; break;
    			case'2': rgb = -11184811; break;
    			case'1': rgb = -3750202; break;
    			}
    			image.setRGB(a, b, rgb);
    		}
    	}
		char[] Down = ("00000000000" +
				"01111111110" +
				"01000000010" +
				"01022222010" +
				"01102220110" +
				"01110201110" +
				"01111011110" +
				"01111111110" +
				"00000000000").toCharArray();

		BufferedImage image2 = new BufferedImage(11, 9, 5);
		for(int a = 0; a < image2.getWidth(); a++){
			for(int b = 0; b < image2.getHeight(); b++){
				int rgb = 0xffffff;
				switch(Down[(b*image.getWidth())+a]){
				case'0': rgb = -16777216; break;
				case'2': rgb = -11184811; break;
				case'1': rgb = -3750202; break;
				}
				image2.setRGB(a, b, rgb);
			}
		}
		BufferedImage a1 = new BufferedImage(256, 256, 5);
		BufferedImage b1 = new BufferedImage(256, 256, 5);
		for(int x = 0; x < 11; x++){
			for(int y = 0; y < 9; y++){
				a1.setRGB(x, y, image.getRGB(x, y));
			}
		}
		for(int x = 0; x < 11; x++){
			for(int y = 0; y < 9; y++){
				b1.setRGB(x, y, image2.getRGB(x, y));
			}
		}
			
		int a = mc.renderEngine.allocateAndSetupTexture(a1);
		int b = mc.renderEngine.allocateAndSetupTexture(b1);
		idsOfArrows = new int[2];
		idsOfArrows[0] = a;
		idsOfArrows[1] = b;
		
	}
    
    public void tryBar(Minecraft mc, int i, int j){
    	//if(!(i > xPosition && i < (xPosition+width-11) && j > yPosition && j < (yPosition + height))){
            if(i >= xPosition+width-11 && i < xPosition+width && j >= yPosition+9 && j < yPosition+height-9){
        		if(items.length>visibleEntries){
        			int downSelected = j-(yPosition+9);
        			double barSize = getSizePerChunk();
        			double d = downSelected / ((double)barSize);
        			position = (int)Math.floor(d);
        		}
        	}
    	//}
    }
    
    
    public boolean mousePressed(Minecraft mc, int i, int j)
    {
    	if(!tryArrows(i, j, mc)){
    		if(i > xPosition && i < (xPosition+width-11) && j > yPosition && j < (yPosition + height)){
        		for(int a = 0; a < visibleEntries; a++){
        			if(i > xPosition && i < xPosition+width-11 && j > yPosition+(a*(height/visibleEntries)) && j < yPosition+((a+1)*(height/visibleEntries))){
        				idSelected = position+a;
        				mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
        			}
        		}
    		}else{
    			tryBar(mc, i, j);
    		}
    	}
    	boolean onListBox = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
    	mouseIsDown = onListBox;
    	return onListBox;
    	
    }
    
	private boolean tryArrows(int i, int j, Minecraft mc) {
		if(i >= arrowX && i <= arrowX+11 && j >= arrowY1 && j <= arrowY1+9){
			if(position > 0){
				position--;
				mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			}
			return true;
		}else if(i >= arrowX && i <= arrowX+11 && j >= arrowY2 && j <= arrowY2+9){
			if(position < (items.length>visibleEntries?items.length-visibleEntries:0)){
				position++;
				mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			}
			return true;
		}
		return false;
	}
	
	public int getPositions(){
		if(items.length>visibleEntries){
			return (items.length-visibleEntries);
		}else{
			return 0;
		}
	}
	
	public void addEntry(String[] names){
		String[][] array = new String[items.length+1][];
		System.arraycopy(items, 0, array, 0, items.length);
		array[array.length-1] = names;
		items = array.clone();
	}
	
	public void setEntry(String[] names, int id){
		items[id] = names;
	}
	
	public void removeEntry(int id){
		String[][] array = new String[items.length-1][];
		int setTo = 0;
		for(int a = 0; a < items.length; a++){
			if(a != id){
				array[setTo] = items[a];
			}
			setTo++;
		}
	}
	
    public void mouseReleased(int i, int j)
    {
    	mouseIsDown = false;
    }
    
    protected void mouseDragged(Minecraft mc, int i, int j)
    {
    	tryBar(mc, i, j);
    }
	
	public void reset(){
		items = new String[0][];
	}
	
	public void set(String[][] src){
		items = src;
	}
	
	public String[] get(int position){
		return items[position];
	}

	
	boolean mouseIsDown = false;
	int[] idsOfArrows;
}
