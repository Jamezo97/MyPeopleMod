package net.minecraft.src;

public class ItemTestTubeBlood extends Item{
	
	public ItemTestTubeBlood(int i){
		super(i);
	}
	
    public int getColorFromDamage(int par1, int par2)
    {
        EntityEggInfo entityegginfo = (EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(par1));

        if (entityegginfo != null)
        {
            if (par2 == 0)
            {
                return entityegginfo.primaryColor;
            }
            else
            {
                return entityegginfo.secondaryColor;
            }
        }
        else
        {
            return 0xffffff;
        }
    }

}
