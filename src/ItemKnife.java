package net.minecraft.src;

public class ItemKnife extends Item{
	
	protected ItemKnife(int i) {
		this(i, false);
		setHasSubtypes(true);
	}
	
	boolean hasBlood = false;
	public ItemKnife(int id, boolean hasBlood){
		super(id);
		this.hasBlood = hasBlood;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(!hasBlood){
			entityplayer.attackEntityFrom(DamageSource.generic, 19);
			itemstack = new ItemStack(mod_MyPeople.knifeBlood, 1, 0);
		}
		return itemstack;
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

	@Override
	public int getDamageVsEntity(Entity entity) {
		return 4;
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving,
			EntityLiving entityliving1) {
		itemstack.itemID = mod_MyPeople.knifeBlood.shiftedIndex;
		if(entityliving instanceof EntityCreeper){itemstack.setItemDamage(50);}else
		if(entityliving instanceof EntitySkeleton){itemstack.setItemDamage(51);}else
		if(entityliving instanceof EntitySpider){itemstack.setItemDamage(52);}else
		if(entityliving instanceof EntityZombie){itemstack.setItemDamage(54);}else
		if(entityliving instanceof EntitySlime){itemstack.setItemDamage(55);}else
		if(entityliving instanceof EntityGhast){itemstack.setItemDamage(56);}else
		if(entityliving instanceof EntityPigZombie){itemstack.setItemDamage(57);}else
		if(entityliving instanceof EntityEnderman){itemstack.setItemDamage(58);}else
		if(entityliving instanceof EntityCaveSpider){itemstack.setItemDamage(59);}else
		if(entityliving instanceof EntitySilverfish){itemstack.setItemDamage(60);}else
		if(entityliving instanceof EntityBlaze){itemstack.setItemDamage(61);}else
		if(entityliving instanceof EntityMagmaCube){itemstack.setItemDamage(62);}else
		if(entityliving instanceof EntityPig){itemstack.setItemDamage(90);}else
		if(entityliving instanceof EntitySheep){itemstack.setItemDamage(91);}else
		if(entityliving instanceof EntityCow){itemstack.setItemDamage(92);}else
		if(entityliving instanceof EntityChicken){itemstack.setItemDamage(93);}else
		if(entityliving instanceof EntitySquid){itemstack.setItemDamage(94);}else
		if(entityliving instanceof EntityWolf){itemstack.setItemDamage(95);}else
		if(entityliving instanceof EntityMooshroom){itemstack.setItemDamage(96);}else
		if(entityliving instanceof EntityVillager){itemstack.setItemDamage(120);}else
		if(entityliving instanceof EntityMyPerson){itemstack.setItemDamage(0);}else{
			itemstack.itemID = mod_MyPeople.knife.shiftedIndex;
		}
		return true;
	}
	
	
	
	
	

}
