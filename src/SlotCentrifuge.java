package net.minecraft.src;

public class SlotCentrifuge extends Slot{

	public SlotCentrifuge(IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
	}
	
	public boolean isItemValid(ItemStack itemstack) {
		if(itemstack.itemID == mod_MyPeople.testTubeBlood.shiftedIndex){
			return true;
		}
		return false;
	}
	
	

}
