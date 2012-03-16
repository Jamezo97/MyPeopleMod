package net.minecraft.src;

import java.util.List;

public class ContainerTransferPlayerItems extends Container
{
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    public boolean isSinglePlayer;
    
    public ContainerTransferPlayerItems(EntityPlayer mainPlayer, EntityPlayer other)
    {
    	craftMatrix = new InventoryCrafting(this, 2, 2);
        craftResult = new InventoryCraftResult();
        isSinglePlayer = true;
    	InventoryPlayer inventoryplayer = mainPlayer.inventory;
    	InventoryPlayer inventoryplayer1 = other.inventory;
        addSlot(new SlotCrafting(mainPlayer, craftMatrix, craftResult, 0, 108, 99));
        for (int i = 0; i < 2; i++)
        {
            for (int i1 = 0; i1 < 2; i1++)
            {
                addSlot(new Slot(craftMatrix, i1 + i * 2, 52 + i1 * 18, 89 + i * 18));
            }
        }
        
        
        for (int j = 0; j < 2; j++)
        {
        	for(int k = 0; k < 2; k++){
                addSlot(new SlotArmorOtherPlayer(this, inventoryplayer, inventoryplayer.getSizeInventory() - 1 - k-(j*2), 8+(18*j), 89 + k * 18, (j*2)+k));	
        	}
        }
        for (int k = 0; k < 3; k++)
        {
            for (int k1 = 0; k1 < 9; k1++)
            {
                addSlot(new Slot(inventoryplayer, k1 + (k + 1) * 9, 8 + k1 * 18, 9 + k * 18));
            }
        }
        for (int l = 0; l < 9; l++)
        {
            addSlot(new Slot(inventoryplayer, l, 8 + l * 18, 67));
        }
        //------------------
        for (int j = 0; j < 2; j++)
        {
        	for(int k = 0; k < 2; k++){
                addSlot(new SlotArmorOtherPlayer(this, inventoryplayer1, inventoryplayer1.getSizeInventory() - 1 - k-(j*2), 134+(18*j), 89 + k * 18, (j*2)+k));	
        	}
        }
        for (int k = 0; k < 3; k++)
        {
            for (int k1 = 0; k1 < 9; k1++)
            {
                addSlot(new Slot(inventoryplayer1, k1 + (k + 1) * 9, 8 + k1 * 18, 129 + k * 18));
            }
        }
        for (int l = 0; l < 9; l++)
        {
            addSlot(new Slot(inventoryplayer1, l, 8 + l * 18, 187));
        }

        onCraftMatrixChanged(craftMatrix);
    }


    public void onCraftMatrixChanged(IInventory iinventory)
    {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix));
    }

    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
        super.onCraftGuiClosed(entityplayer);
        for (int i = 0; i < 4; i++)
        {
            ItemStack itemstack = craftMatrix.func_48081_b(i);
            if (itemstack != null)
            {
            	entityplayer.func_48153_a(itemstack);
            }
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public ItemStack transferStackInSlot(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i == 0)
            {
                if (!mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }
            }else if(i >= 9 && i <= 44){
            	if (!mergeItemStack(itemstack1, 49, 85, false))
                {
                    return null;
                }
            }else if(i >= 49 && i <= 84){
            	if (!mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }else if(i >= 5 && i <= 8){
            	if (!mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }else if(i >= 45 && i <= 48){
            	if (!mergeItemStack(itemstack1, 49, 85, false))
                {
                    return null;
                }
            }else if (!mergeItemStack(itemstack1, 9, 45, false))
            {
                return null;
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
}
