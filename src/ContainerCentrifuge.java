package net.minecraft.src;

public class ContainerCentrifuge extends Container{

	TileEntityCentrifuge tE;
	
	public ContainerCentrifuge(InventoryPlayer inventoryplayer, TileEntityCentrifuge tE){
		this.tE = tE;
		
		addSlot(new SlotCentrifuge(tE, 0, 80, 7));
		addSlot(new SlotCentrifuge(tE, 1, 102, 29));
		addSlot(new SlotCentrifuge(tE, 2, 80, 51));
		addSlot(new SlotCentrifuge(tE, 3, 58, 29));
		addSlot(new Slot(tE, 4, 80, 29));
        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlot(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlot(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }
	}
	
    public ItemStack transferStackInSlot(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i >= 0 && i < 4){
            	if (!mergeItemStack(itemstack1, 5, 41, true)/* && !tE.isSpinning()*/)
                {
                    return null;
                }
            }else if(i == 4){
            	if (!mergeItemStack(itemstack1, 5, 41, true))
                {
                    return null;
                }
            }else if(i >= 5 && i < 42){
            	if(itemstack1.itemID == mod_MyPeople.testTubeBlood.shiftedIndex){
            		if (!tE.isSpinning() && !mergeItemStack(itemstack1, 0, 4, true))
                    {
                        return null;
                    }
            	}else if(tE.getItemBurnTime(itemstack1)>0){
            		if (!mergeItemStack(itemstack1, 4, 5, true))
                    {
                        return null;
                    }
            	}
            }
            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onPickupFromSlot(itemstack1);
            }
            else
            {
                return null;
            }
        }
        return itemstack;
    }
	
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
