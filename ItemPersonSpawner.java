package net.minecraft.src;

public class ItemPersonSpawner extends Item{

	protected ItemPersonSpawner(int i) {
		super(i);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		i += Facing.offsetsXForSide[l];
		j += Facing.offsetsYForSide[l];
		k += Facing.offsetsZForSide[l];
		if(world.getBlockId(i, j, k)==0 && world.getBlockId(i, j+1, k)==0){
			itemstack.stackSize--;
			ModLoader.getMinecraftInstance().displayGuiScreen(new GuiChooseName(i,j,k));
		}
		return true;
	}	

}
