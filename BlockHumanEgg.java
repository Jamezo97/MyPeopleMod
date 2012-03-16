package net.minecraft.src;

import java.util.Random;

public class BlockHumanEgg extends BlockContainer {

	protected BlockHumanEgg(int i) {
		super(i, Material.glass);
	}	

    public TileEntity getBlockEntity(){
    	return new TileEntityHumanEgg();
    }
    
    

    @Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        if (l > 0 && Block.blocksList[l].canProvidePower())
        {
            boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k) || world.isBlockGettingPowered(i, j, k);
            if (flag)
            {
                TileEntity tileentity = world.getBlockTileEntity(i, j, k);
                if (tileentity != null && tileentity instanceof TileEntityHumanEgg)
                {
                    ((TileEntityHumanEgg)tileentity).onPowered();
                }
            }
        }
	}

	public boolean isOpaqueCube(){return false;}

    public boolean renderAsNormalBlock(){return false;}

    public int getRenderType(){return 1;}
}
