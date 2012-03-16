package net.minecraft.src;

import java.util.Random;

public class TileEntityHumanEgg extends TileEntity{
	
	public TileEntityHumanEgg(){
		timeStarted = 0;
	}
	
	int timeout = 0;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		size = nbt.getLong("size");
		zapped = nbt.getBoolean("zapped");
	}
	
	



	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("size", size);
		nbt.setBoolean("zapped", zapped);
	}

    private float getSoundPitch()
    {
    	return (r.nextFloat() - r.nextFloat()) * 0.2F + 1.0F;
    }
    
    public long getDiff(){
    	return size - worldObj.getWorldTime();
    }
    
    public float percentDone(){
    	return (float)((48000d-(double)getDiff())/48000d);
    }
	//20MC 1IRL
	@Override
	public void updateEntity() {
		if(zapped){
			if(timeout-- < 1){
				if(r.nextFloat() < .1){
					worldObj.playSoundEffect(((double)xCoord)+.5D, ((double)yCoord)+.5D, ((double)zCoord)+.5D, "step.gravel", .8f*percentDone(), getSoundPitch());
					spawnParticles();
				}
				timeout = 100;
			}

			if(worldObj.getWorldTime()>size){
				ModLoader.getMinecraftInstance().sndManager.playSound(Block.blocksList[mod_MyPeople.humanEgg.blockID].stepSound.getStepSound(), (float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, (Block.blocksList[mod_MyPeople.humanEgg.blockID].stepSound.getVolume() + 1.0F) / 8F, Block.blocksList[mod_MyPeople.humanEgg.blockID].stepSound.getPitch() * 0.5F);
				worldObj.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
				ModLoader.getMinecraftInstance().displayGuiScreen(new GuiChooseName(xCoord, yCoord, zCoord));
			}
			if(timeStarted < System.currentTimeMillis()){
				timeStarted += 1000;
				size += (1f-(1f*worldObj.getLightBrightness(xCoord, yCoord, zCoord)))*20;
				if(timeStarted +1000 < System.currentTimeMillis()){
					timeStarted = System.currentTimeMillis();
				}
			}
		}
	}
	
	private void spawnParticles() {
		int i = (int)Math.floor(percentDone()*50d)+1;
		if(i>0){
			for(int a = 0; a < r.nextInt(i)+i; a++){
				worldObj.spawnParticle((new StringBuilder()).append("tilecrack_").append(mod_MyPeople.humanEgg.blockID).toString(), (double)xCoord + ((r.nextFloat()*2d)-1d)/2d+.5d, (double)yCoord + ((r.nextFloat()))/2d+.2d, (double)zCoord + ((r.nextFloat()*2d)-1d)/2d+.5d, ((r.nextFloat()*2d)-1d), r.nextFloat(), ((r.nextFloat()*2d)-1d));
			}
		}
	}
	long timeStarted = 0;
	public void onPowered() {
		if(zapped == false){
			zapped = true;
			size = worldObj.getWorldTime() + 48000;
			for(int a = 0; a < 6; a++){
				worldObj.playSoundEffect(((double)xCoord)+.5D, ((double)yCoord)+.5D, ((double)zCoord)+.5D, "random.fizz", 2f, getSoundPitch());
			}
		}
	}
	Random r = new Random();
	boolean zapped = false;
	long size = 0;
	

	
	
}
