package net.minecraft.src;

public class TileEntityCentrifuge extends TileEntity implements IInventory {
	ItemStack[] items = null;

	int furnaceBurnTime = 0, currentItemBurnTime = 0, timeSpun;
	boolean spinPressed = false;
	public TileEntityCentrifuge() {
		items = new ItemStack[5];
	}

	public boolean canSpin() {
		boolean canSpin = false;
		for (int a = 0; a < 4; a++) {
			ItemStack item = items[a];
			if (item != null) {
				if (item.itemID != mod_MyPeople.testTubeBlood.shiftedIndex) {
					canSpin = false;
					break;
				} else {
					canSpin = true;
				}
			}
		}
		return canSpin;
	}

	public void closeChest() {
	}

	public ItemStack decrStackSize(int i, int j) {
		if (items[i] != null) {
			if (items[i].stackSize <= j) {
				ItemStack itemstack = items[i];
				items[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = items[i].splitStack(j);
			if (items[i].stackSize == 0) {
				items[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	public float getFuelDone() {
		return (((float) furnaceBurnTime / ((float) currentItemBurnTime)));
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public String getInvName() {
		return "Centrifuge";
	}

	public int getItemBurnTime(ItemStack itemstack) {
		if (itemstack == null) {
			return 0;
		}
		int i = itemstack.getItem().shiftedIndex;
		if (i < 256 && Block.blocksList[i].blockMaterial == Material.wood) {
			return 300;
		}
		if (i == Item.stick.shiftedIndex) {
			return 100;
		}
		if (i == Item.coal.shiftedIndex) {
			return 1600;
		}
		if (i == Item.bucketLava.shiftedIndex) {
			return 20000;
		}
		if (i == Block.sapling.blockID) {
			return 100;
		}
		if (i == Item.blazeRod.shiftedIndex) {
			return 2400;
		} else {
			return ModLoader.addAllFuel(i, itemstack.getItemDamage());
		}
	}
	float spinTime = 6400f;
	public float getPercentDone() {
		return (((float) timeSpun) / spinTime);
	}

	@Override
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return items[i];
	}

	public boolean isBurning() {
		return furnaceBurnTime > 0;
	}

	public boolean isSpinning() {
		return timeSpun > 0;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityplayer.getDistanceSq((double) xCoord + 0.5D,
				(double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openChest() {
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		items = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < items.length) {
				items[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		furnaceBurnTime = nbttagcompound.getInteger("furnaceBurnTime");
		currentItemBurnTime = nbttagcompound.getInteger("currentItemBurnTime");
		timeSpun = nbttagcompound.getInteger("timeSpun");
		spinPressed = nbttagcompound.getBoolean("spinPressed");
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		items[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	public void spin() {
		for (int a = 0; a < 4; a++) {
			ItemStack item = items[a];
			if (item != null) {
				item.itemID = mod_MyPeople.testTubeSpunBlood.shiftedIndex;
			}
		}
	}

	@Override
	public void updateEntity() {
		boolean updateItems = false;
		if (furnaceBurnTime > 0) {
			furnaceBurnTime--;
		}
		if (furnaceBurnTime == 0 && canSpin() && spinPressed) {
			currentItemBurnTime = furnaceBurnTime = getItemBurnTime(items[4]);
			if (furnaceBurnTime > 0) {
				items[4].stackSize--;
				if (items[4].stackSize <= 0) {
					items[4] = null;
				}
			}
		}
		if (isBurning() && canSpin() && spinPressed) {
			timeSpun++;
			if (timeSpun >= spinTime) {
				timeSpun = 0;
				spinPressed = false;
				spin();
			}
		} else {
			timeSpun = 0;
		}

		if (updateItems) {
			onInventoryChanged();
		}
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				items[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbttagcompound.setInteger("timeSpun", timeSpun);
		nbttagcompound.setInteger("currentItemBurnTime", currentItemBurnTime);
		nbttagcompound.setInteger("furnaceBurnTime", furnaceBurnTime);
		nbttagcompound.setBoolean("spinPressed", spinPressed);
		nbttagcompound.setTag("Items", nbttaglist);

	}

	@Override
	public ItemStack func_48081_b(int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
