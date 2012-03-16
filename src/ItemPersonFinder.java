package net.minecraft.src;

public class ItemPersonFinder extends Item{
	
	public ItemPersonFinder(int i){
		super(i);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		ModLoader.getMinecraftInstance().displayGuiScreen(new GuiChooserPlayerToFind());
		return itemstack;
	}
	
	

}
