package net.minecraft.src;

public class BlockCentrifuge extends BlockContainer {

	protected BlockCentrifuge(int i) {
		super(i, Material.rock);
	}
	
	

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		TileEntity entity = world.getBlockTileEntity(i, j, k);
		if(entity != null){
			ModLoader.getMinecraftInstance().displayGuiScreen(new GuiCentrifuge(entityplayer, (TileEntityCentrifuge)entity));
			return true;
		}
		return false;
	}

    public int getBlockTextureFromSide(int i)
    {
        if (i == 1)
        {
            return mod_MyPeople.centrifugeTop;
        }
        return blockIndexInTexture;
    }

	@Override
	public TileEntity getBlockEntity() {
		return new TileEntityCentrifuge();
	}

}
