package net.minecraft.src;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;

public class EntityMyPerson extends EntityPlayer {
	
	public File customTextures = new File(ModLoader.getMinecraftInstance().getMinecraftDir(), "CustomTextures");

	public static int changeState(int state) {
		state += 1;
		if (state > 4) {
			state = 0;
		}
		return state;
	}
	public static String getState(int state) {
		if (state == 0) {
			return "Follow And Attack";
		} else if (state == 1) {
			return "Guard Position";
		} else if (state == 2) {
			return "Hunt";
		} else if (state == 3){
			return "Stay";
		}else{
			return "Follow";
		}
	}
	public Entity currentTarget;
	public Entity entityToAttack;
	public boolean followPlayer = true;
	int foodCoolDown = 0, checkPlayerTimer = 0;
	DecimalFormat format = new DecimalFormat("");

	public boolean getItems = true;

	double[] guardPositions = new double[5];

	private boolean hasAttacked = true;

	boolean justLoaded = true, retaliate = true, lumber = false, mine = false;

	float lastScaleCheck = 1.0f;

	float maxScale = 1.1f;

	public PathEntity pathToEntity;

	public String name = "";
	
	Random r = new Random();


	float scale = 1.0f;
	
	public boolean sprintEnemy = false;
	
	int state = 0;

	public EntityMyPerson(World world) {
		super(world);
		ignoreFrustrumCheck = true;
		foodCoolDown = 0;
		scale = .5f;
		lastScaleCheck = .5f;
		maxScale = 1.1f;
		justLoaded = true;
		state = 3;
		if(!customTextures.exists()){
			customTextures.mkdir();
		}
	}

	public void addToPlayerList() {
		if (!worldObj.playerEntities.contains(this)) {
			worldObj.playerEntities.add(this);
		}
	}

	protected void attackEntity(Entity entity, float f) {
		if (attackTime <= 0) {
			if (f < 3d || !inventory.hasItem(Item.arrow.shiftedIndex )
					|| entity instanceof EntityEnderman
					|| entity instanceof EntitySlime
					|| !inventory.hasItem(Item.bow.shiftedIndex)) {
				int loc = checkInventorySword();
				if (loc > -1) {
					inventory.currentItem = loc;
				}
			} else if (f < 12) {
				int loc = checkInventoryBow();
				if (loc > -1) {
					inventory.currentItem = loc;
				}
			}
			if (f < (3d)) {

				hasAttacked = true;
				swingItem();
				ItemStack item = getCurrentEquippedItem();
				attackTargetEntityWithCurrentItem(entity);
				scale += .001f;
				attackTime = 5;
			} else if (f < 12
					&& inventory.getCurrentItem() != null
					&& inventory.getCurrentItem().itemID == Item.bow.shiftedIndex
					&& inventory.hasItem(Item.arrow.shiftedIndex)) {
				double d = entity.posX - posX;
				double d1 = entity.posZ - posZ;
				if (attackTime == 0) {
					EntityArrow entityarrow = new EntityArrow(worldObj, this,
							1.0f);
					double d2 = (entity.posY + entity.getEyeHeight())
							- 0.69999998807907104D - entityarrow.posY;
					float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
					worldObj.playSoundAtEntity(this, "random.bow", 1.0F,
							1.0F / (rand.nextFloat() * 0.4F + 0.8F));
					worldObj.spawnEntityInWorld(entityarrow);
					entityarrow.setArrowHeading(d, d2 + f1, d1, 1.6F,
							0F);
					this.inventory
							.consumeInventoryItem(Item.arrow.shiftedIndex);
					attackTime = 20;
					scale += .001f;
				}
				rotationYaw = (float) ((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
				hasAttacked = true;
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		if(retaliate){
			Entity e = damagesource.getEntity();
			if (e != null) {
				if (!(e instanceof EntityPlayer) && !(e instanceof EntityMyPerson)) {
					currentTarget = e;
					entityToAttack = e;
					pathToEntity = getPathToEntity(entityToAttack,
							24F);
				}
			}
		}
		return super.attackEntityFrom(damagesource, i);
	}
/*	public boolean isAIEnabled()
    {
        return true;
    }*/
	
	public PathEntity getPathToEntity(Entity e, float f){
		return worldObj.func_48463_a(this, e, f, true, false, false, true);
	}
	
	public PathEntity getEntityPathToXYZ(int x, int y, int z, float f){
		return worldObj.func_48460_a(this, x, y, z, f, true, false, false, true);
	}

	@Override
	public void attackTargetEntityWithCurrentItem(Entity entity) {
		int i = inventory.getDamageVsEntity(entity);
		if (isPotionActive(Potion.damageBoost)) {
			i += 3 << getActivePotionEffect(Potion.damageBoost).getAmplifier();
		}
		if (isPotionActive(Potion.weakness)) {
			i -= 2 << getActivePotionEffect(Potion.weakness).getAmplifier();
		}
		int j = 0;
		int k = 0;
		if (entity instanceof EntityLiving) {
			k = EnchantmentHelper.getEnchantmentModifierLiving(inventory,
					(EntityLiving) entity);
			j += EnchantmentHelper.getKnockbackModifier(inventory,
					(EntityLiving) entity);
		}
		if (isSprinting()) {
			j++;
		}
		if (i > 0 || k > 0) {
			boolean flag = fallDistance > 0.0F && !onGround && !isOnLadder()
					&& !isInWater() && !isPotionActive(Potion.blindness)
					&& ridingEntity == null && (entity instanceof EntityLiving);
			if (flag) {
				i += rand.nextInt(i / 2 + 2);
			}
			i += k;
			boolean flag1 = entity.attackEntityFrom(
					DamageSource.causePlayerDamage(this),
					(int) Math.ceil(i * scale));
			if (flag1) {
				if (j > 0) {
					entity.addVelocity(
							-MathHelper.sin((rotationYaw * 3.141593F) / 180F)
									* j * 0.5F, 0.10000000000000001D,
							MathHelper.cos((rotationYaw * 3.141593F) / 180F)
									* j * 0.5F);
					motionX *= 0.59999999999999998D;
					motionZ *= 0.59999999999999998D;
					setSprinting(false);
				}
				if (flag) {
					onCriticalHit(entity);
				}
				if (k > 0) {
					onEnchantmentCritical(entity);
				}
				if (i >= 18) {
					triggerAchievement(AchievementList.overkill);
				}
			}
			ItemStack itemstack = getCurrentEquippedItem();
			if (itemstack != null && (entity instanceof EntityLiving)) {
				itemstack.hitEntity((EntityLiving) entity, this);
				if (itemstack.stackSize <= 0) {
					itemstack.onItemDestroyedByUse(this);
					destroyCurrentEquippedItem();
				}
			}
			if (entity instanceof EntityLiving) {
				if (entity.isEntityAlive()) {
					alertWolves((EntityLiving) entity, true);
				}
				addStat(StatList.damageDealtStat, i);
				int l = EnchantmentHelper.getFireAspectModifier(inventory,
						(EntityLiving) entity);
				if (l > 0) {
					entity.setFire(l * 4);
				}
			}
			addExhaustion(0.3F);
		}
	}
	@Override
	protected boolean canDespawn() {
		return false;
	}
	public String changeState() {
		state = changeState(state);
		if (state == 1) {
			setGuardPositions();
		}
		return getState();
	}
	public int checkInventoryBow() {
		if (inventory.getCurrentItem() == null
				|| inventory.getCurrentItem().itemID != Item.bow.shiftedIndex
				&& (entityToAttack != null && !(entityToAttack instanceof EntityEnderman))) {
			for (int a = 0; a < 9; a++) {
				ItemStack item = inventory.mainInventory[a];
				if (item != null && item.itemID == Item.bow.shiftedIndex) {
					return a;
				}
			}
		}
		return -1;
	}
	public int checkInventorySword() {
		int position = -1;
		int goodness = -1;
		for (int a = 0; a < 9; a++) {
			ItemStack stack = inventory.mainInventory[a];
			if (stack != null) {
				if (isSword(stack.itemID)) {
					int it = getSwordGoodness(stack.itemID);
					if (it > goodness) {
						goodness = it;
						position = a;
					}
				}
			}
		}
		if (position > -1) {
			return position;
		}

		return -1;
	}

	private boolean checkPlayerAndRun() {
		if (state == 0 || state == 4) {
			EntityPlayer p = ModLoader.getMinecraftInstance().thePlayer;
			if (p != null) {
				double distance = getDistanceToEntity(p);
				if (distance > 25) {
					int i = MathHelper.floor_double(p.posX) - 2;
					int j = MathHelper.floor_double(p.posZ) - 2;
					int k = MathHelper.floor_double(p.boundingBox.minY);
					for (int l = 0; l <= 4; l++) {
						for (int i1 = 0; i1 <= 4; i1++) {
							if ((l < 1 || i1 < 1 || l > 3 || i1 > 3)
									&& worldObj.isBlockNormalCube(i + l, k - 1,
											j + i1)
									&& !worldObj.isBlockNormalCube(i + l, k, j
											+ i1)
									&& !worldObj.isBlockNormalCube(i + l,
											k + 1, j + i1)) {
								setLocationAndAngles((i + l) + 0.5F, k,
										(j + i1) + 0.5F, rotationYaw,
										rotationPitch);
								return true;
							}
						}
					}
				} else if (distance > (state==4?1d:5d)) {
					float distanceTo = getDistanceToEntity(p);
					pathToEntity = getPathToEntity(p,
							(float) Math.ceil(distanceTo));
					return true;
				}
			}
		} else if (state == 1) {
			double distance = this.getDistance(guardPositions[0],
					guardPositions[1], guardPositions[2]);
			if (distance > 40) {
				this.setLocationAndAngles(guardPositions[0], guardPositions[1],
						guardPositions[2], (float) guardPositions[3],
						(float) guardPositions[4]);
			} else if (distance > .25d) {
				pathToEntity = getEntityPathToXYZ((int) Math.round(guardPositions[0]),
						(int) Math.round(guardPositions[1]),
						(int) Math.round(guardPositions[2]),
						(float) distance * 2f);
				return true;
			}
		}
		return false;
	}

	private Entity findMonsterToAttack() {
		if (state != 3 && state != 4) {
			List get = worldObj.getEntitiesWithinAABB(
					net.minecraft.src.IMob.class,
					AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ,
							posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(24D,
							4D, 24D));
			int index = 0;
			double distanceSq = -1;
			if (get.size() > 0) {
				for (int a = 0; a < get.size(); a++) {
					double d = this.getDistanceSqToEntity((Entity) get.get(a));
					if ((d < distanceSq || distanceSq == -1)
							&& canEntityBeSeen((Entity) get.get(a)) && ((Entity)get.get(a)).isEntityAlive()) {
						distanceSq = d;
						if (distanceSq < 0) {
							distanceSq *= -1;
						}
						index = a;
					}
				}
				if (distanceSq != -1) {
					return (Entity) get.get(index);
				}
			}
		}
		return null;
	}

	public boolean findXPOrItemstacksAndSetCourse() {
		if (getItems) {
			List entities = worldObj.getEntitiesWithinAABB(
					net.minecraft.src.EntityXPOrb.class,
					AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ,
							posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D,
							4D, 16D));
			List entities2 = worldObj.getEntitiesWithinAABB(
					net.minecraft.src.EntityItem.class,
					AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ,
							posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D,
							4D, 16D));
			for (int a = 0; a < entities2.size(); a++) {
				entities.add(entities2.get(a));
			}
			int index = 0;
			double distanceSq = -1;
			if (entities.size() > 0) {
				for (int a = 0; a < entities.size(); a++) {
					double d = this.getDistanceSqToEntity((Entity) entities
							.get(a));
					if ((d < distanceSq || distanceSq == -1)
							&& canEntityBeSeen((Entity) entities.get(a))) {
						distanceSq = d;
						if (distanceSq < 0) {
							distanceSq *= -1;
						}
						index = a;
					}
				}
				if (distanceSq != -1) {
					pathToEntity = getPathToEntity((Entity) entities.get(index), 20F);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void func_6420_o() {
	}

	public float getBlockPathWeight(int i, int j, int k) {
		if (worldObj.getBlockId(i, j - 1, k) == Block.grass.blockID) {
			return 10F;
		} else {
			return worldObj.getLightBrightness(i, j, k) - 0.5F;
		}
	}

	@Override
	public float getEyeHeight() {
		return super.getEyeHeight() * scale;
	}

	@Override
	public int getMaxHealth() {
		return 20 + 10 * ((int) Math.ceil((scale - .5f) * 2));
	}

	public int getPotionId(ItemFood food){
		Field f = null;
		try{
			//TODO Change Declared Field Type To New Type
			f = ItemFood.class.getDeclaredField("bX");
		}catch(Exception e){
			try{
				f = ItemFood.class.getDeclaredField("potionId");
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
		if(f != null){
			try{
				f.setAccessible(true);
				Object o = f.get(food);
				if(o != null){
					return (Integer)o;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return -1;
	}

	public String getState() {
		return getState(state);
	}

	public int getSwordGoodness(int id) {
		switch (id) {
		case 268:
			return 0;
		case 272:
			return 1;
		case 267:
			return 2;
		case 283:
			return 3;
		case 276:
			return 4;
		}

		return -1;
	}

	public boolean hasPath() {
		return pathToEntity != null;
	}

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		if (entityplayer.ridingEntity == this) {
			entityplayer.mountEntity(null);
		} else {
			ModLoader.getMinecraftInstance().displayGuiScreen(
					new GuiTransferPlayerItems(this, entityplayer));
		}
		return false;
	}

	public boolean isHunger(ItemStack a){
		int i1 = getPotionId((ItemFood)a.getItem());
		if(i1==Potion.hunger.id){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean isPlayerFullyAsleep() {
		return true;
	}

	@Override
	public boolean isPlayerSleeping() {
		if(ModLoader.getMinecraftInstance().thePlayer.isPlayerSleeping()){
			playerLocation = ModLoader.getMinecraftInstance().thePlayer.playerLocation;
			return true;
		}
		return false;
	}

	public boolean isSword(int i) {
		return (i == Item.swordWood.shiftedIndex
				|| i == Item.swordStone.shiftedIndex
				|| i == Item.swordSteel.shiftedIndex
				|| i == Item.swordGold.shiftedIndex || i == Item.swordDiamond.shiftedIndex);
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		removeFromPlayerList();
		if(mod_MyPeople.entityToFind != null && mod_MyPeople.entityToFind.equals(this)){
			mod_MyPeople.entityToFind = null;
		}
		super.onDeath(damagesource);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		double addXp = 0;
		if (scale < maxScale + addXp || justLoaded) {
			scale += .000005f;
			setSize(.6f * scale, 1.8f * scale);
			yOffset = (1.62F * scale) - .2f;
			if (scale - lastScaleCheck > .05 || scale - lastScaleCheck < -.05) {
				lastScaleCheck = scale;
				setPosition(posX, posY + .1, posZ);
			}
		} else if (scale > maxScale + addXp) {
			scale = (float) (maxScale + addXp);
		}
		if (foodCoolDown > 0) {
			foodCoolDown--;
		}
		if (checkPlayerTimer > 0) {
			checkPlayerTimer--;
		}
		if (entityToAttack == null && foodCoolDown == 0 && foodStats.needFood()
				&& rand.nextFloat() < .04f) {
			tryEat();
		}
		moveSpeed = .7f*scale;
		if (justLoaded) {
			addToPlayerList();
			worldObj.obtainEntitySkin(this);
			justLoaded = false;
		}
		if(isMining && blockToGet != null){
			onMine();
		}
		
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		scale = nbttagcompound.getFloat("scale");
		username = nbttagcompound.getString("username");
		skinUrl = (new StringBuilder())
				.append("http://s3.amazonaws.com/MinecraftSkins/")
				.append(username).append(".png").toString();
		lastScaleCheck = nbttagcompound.getFloat("lastScaleCheck");
		state = nbttagcompound.getInteger("state");
		guardPositions[0] = nbttagcompound.getDouble("guardPositions[0]");
		guardPositions[1] = nbttagcompound.getDouble("guardPositions[1]");
		guardPositions[2] = nbttagcompound.getDouble("guardPositions[2]");
		guardPositions[3] = nbttagcompound.getDouble("guardPositions[3]");
		guardPositions[4] = nbttagcompound.getDouble("guardPositions[4]");
		setPosition(posX, posY + (maxScale + .5f - scale), posZ);
		getItems = nbttagcompound.getBoolean("getItems");
		sprintEnemy = nbttagcompound.getBoolean("sprintEnemy");
		name = nbttagcompound.getString("name");
		texture = nbttagcompound.getString("texture");
		lumber = nbttagcompound.getBoolean("lumber");
		mine = nbttagcompound.getBoolean("mine");
	}
	
	public void removeFromPlayerList() {
		if (worldObj.playerEntities.contains(this)) {
			worldObj.playerEntities.remove(this);
		}
	}

	public void setGuardPositions() {
		guardPositions[0] = posX;
		guardPositions[1] = Math.ceil(posY - 1);
		guardPositions[2] = posZ;
		guardPositions[3] = rotationYaw;
		guardPositions[4] = rotationPitch;
	}

	public void tryEat() {
		IInventory inventoryy = inventory;
		int best = -1;
		ArrayList<Integer> goodOnes = new ArrayList<Integer>();
		boolean addToList = false;
		for (int a = 0; a < inventory.getSizeInventory(); a++) {
			ItemStack item = inventory.getStackInSlot(a);
			if (item != null
					&& Item.itemsList[item.itemID] != null
					&& ItemFood.class
							.isAssignableFrom(Item.itemsList[item.itemID]
									.getClass())) {
				boolean isHunger = isHunger(item);
				if(best == -1 || !isHunger){
					if(addToList){
						goodOnes.add(a);
					}else{
						if(!isHunger){
							addToList = true;
							goodOnes.add(a);
						}else{
							best = a;
						}
					}
				}
			}
		}
		if(goodOnes.size() > 0 || best > -1){
			ItemStack item = inventoryy.getStackInSlot(goodOnes.size()>0?goodOnes.get(rand.nextInt(goodOnes.size())):best);
			foodCoolDown = 25;
			func_35201_a(item, 6);
			item.getItem().onFoodEaten(item, worldObj, this);
		}
	}
	@Override
	protected void updateEntityActionState() {
		entityAge++;
		EntityPlayer entityplayer = worldObj
				.getClosestPlayerToEntity(this, -1D);
		despawnEntity();
		moveStrafing = 0.0F;
		moveForward = 0.0F;
		float f = 8F;

		if (currentTarget != null) {
			faceEntity(currentTarget, 10F, getVerticalFaceSpeed());
			if (numTicksToChaseTarget-- <= 0 || currentTarget.isDead
					|| currentTarget.getDistanceSqToEntity(this) > (f * f)) {
				currentTarget = null;
			}
		} else {
			if (rand.nextFloat() < 0.05F) {
				randomYawVelocity = (rand.nextFloat() - 0.5F) * 20F;
			}
			rotationYaw += randomYawVelocity;
			rotationPitch = defaultPitch;
		}
		boolean flag = isInWater();
		boolean flag1 = handleLavaMovement();
		if (flag || flag1) {
			isJumping = rand.nextFloat() < 0.8F;
		}

		updateMovements();
		super.updateEntityActionState();
	}
	
	public double getDistance(int[] pos){
		double dx = pos[0]-posX;
		double dy = pos[1]-posY;
		double dz = pos[2]-posZ;
		return dx*dx + dy*dy + dz*dz;
	}
	public boolean isMining = false;
	private void updateMovements() {
		Profiler.startSection("ai");
		hasAttacked = false;
		float f = 24F;
		if (entityToAttack == null) {
			if(state != 3){
				entityToAttack = findMonsterToAttack();
			}
			if (entityToAttack != null) {
				pathToEntity = getPathToEntity(entityToAttack, f);
			} else if (checkPlayerTimer <= 0 && pathToEntity == null) {
				if (!findXPOrItemstacksAndSetCourse()) {
					checkPlayerAndRun();
				}
				checkPlayerTimer = 20;
			}
		} else if (entityToAttack != null && !entityToAttack.isEntityAlive()) {
			entityToAttack = null;
			pathToEntity = null;
			currentTarget = null;
		} else if (entityToAttack != null) {
			float f1 = entityToAttack.getDistanceToEntity(this);
			if (canEntityBeSeen(entityToAttack)) {
				attackEntity(entityToAttack, f1);
			}
		}
		if((lumber | mine) && blockToGet == null && entityToAttack == null){
			checkLumberOrMine();
		}
		if(isMining && entityToAttack != null){
			stopMining();
			blockToGet = null;
		}
		 if((lumber | mine) && blockToGet != null){
				double d = getDistance(blockToGet);
				if(d <= 16 && !isMining){
					startMining();
				}else if(d > 16 && isMining){
					stopMining();
				}
			}
		if(blockToGet != null && pathToEntity == null){
			setPathToBlock(blockToGet);
		}
		checkBlockValid();
		if (entityToAttack != null
				&& ((EntityLiving) entityToAttack).isMovementBlocked()) {
			entityToAttack = null;
			pathToEntity = null;
			currentTarget = null;
		}
		if (pathToEntity == null && entityToAttack != null) {
			pathToEntity = getPathToEntity(entityToAttack, f);
			if (pathToEntity == null) {
				entityToAttack = null;
				pathToEntity = null;
			}
		}
		if (entityToAttack != null && pathToEntity != null && sprintEnemy && !isCollidedHorizontally) {
			if (!isSprinting() && this.foodStats.getFoodLevel() > 4) {
				setSprinting(true);
			}
		} else {
			if (isSprinting()) {
				setSprinting(false);
			}
		}

		Profiler.endSection();
		if (!hasAttacked && entityToAttack != null
				&& (pathToEntity == null || rand.nextInt(20) == 0)) {
			pathToEntity = getPathToEntity(entityToAttack, f);
		} else if (!hasAttacked	&& (pathToEntity == null && rand.nextInt(180) == 0 || rand.nextInt(120) == 0) && entityAge < 100 &&(state != 1 && state != 3 && state != 4)) {
			updateWanderPath();
		}
		int i = MathHelper.floor_double(boundingBox.minY + 0.5D);
		boolean flag = isInWater();
		boolean flag1 = handleLavaMovement();
		rotationPitch = 0.0F;
		if (pathToEntity == null || rand.nextInt(100) == 0) {
			super.updateEntityActionState();
			pathToEntity = null;
			return;
		}
		Profiler.startSection("followpath");
		Vec3D vec3d = pathToEntity.func_48640_a(this);
		for (double d = width * 2.0F; vec3d != null
				&& vec3d.squareDistanceTo(posX, vec3d.yCoord, posZ) < d * d;) {
			pathToEntity.incrementPathIndex();
			if (pathToEntity.isFinished()) {
				vec3d = null;
				pathToEntity = null;
			} else {
				vec3d = pathToEntity.func_48640_a(this);
			}
		}

		isJumping = false;
		if (vec3d != null) {
			double d1 = vec3d.xCoord - posX;
			double d2 = vec3d.zCoord - posZ;
			double d3 = vec3d.yCoord - i;
			float f2 = (float) ((Math.atan2(d2, d1) * 180D) / 3.1415927410125732D) - 90F;
			float f3 = f2 - rotationYaw;
			moveForward = moveSpeed;
			for (; f3 < -180F; f3 += 360F) {
			}
			for (; f3 >= 180F; f3 -= 360F) {
			}
			if (f3 > 30F) {
				f3 = 30F;
			}
			if (f3 < -30F) {
				f3 = -30F;
			}
			rotationYaw += f3;
			if (hasAttacked && entityToAttack != null) {
				double d4 = entityToAttack.posX - posX;
				double d5 = entityToAttack.posZ - posZ;
				float f5 = rotationYaw;
				rotationYaw = (float) ((Math.atan2(d5, d4) * 180D) / 3.1415927410125732D) - 90F;
				float f4 = (((f5 - rotationYaw) + 90F) * 3.141593F) / 180F;
				moveStrafing = -MathHelper.sin(f4) * moveForward * 1.0F;
				moveForward = MathHelper.cos(f4) * moveForward * 1.0F;
			}
			if (d3 > 0.0D) {
				isJumping = true;
			}
		}
		if (entityToAttack != null) {
			faceEntity(entityToAttack, 30F, 30F);
		}
		if (isCollidedHorizontally && !hasPath()) {
			isJumping = true;
		}
		if (rand.nextFloat() < 0.8F && (flag || flag1)) {
			isJumping = true;
		}
		if(blockToGet != null && isMining && entityToAttack == null){
			faceBlock(blockToGet, 30F, getVerticalFaceSpeed());
		}
		Profiler.endSection();
	}
	

	public void checkBlockValid(){
		if(blockToGet != null){
			int i = worldObj.getBlockId(blockToGet[0], blockToGet[1], blockToGet[2]);
			if(!(mod_MyPeople.getLogs().contains(i) || mod_MyPeople.getOres().contains(i) || isLeaves(i))){
				blockToGet = null;
				pathToEntity = null;
				stopMining();
			}
		}
	}
	
    private boolean isLeaves(int id) {
    	if(id > 0 && id < 256){
    		Block b = (Block.blocksList[id]);
    		if(b != null){
    			Class c = b.getClass();
    			if(c != null){
    				return c.isAssignableFrom(BlockLeaves.class);
    			}
    		}
    	}
		return false;
	}
	public void faceBlock(int[] pos, float f, float f1)
    {
        double d = ((double)pos[0]) - posX + .5d;
        double d1 = ((double)pos[1])+.5 - (posY + (double)getEyeHeight());
        double d2 = ((double)pos[2])- posZ + .5d;


        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f2 = (float)((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
        float f3 = (float)(-((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D));
        rotationPitch = updateRotation(rotationPitch, f3, f1);
        rotationYaw = updateRotation(rotationYaw, f2, f);
    }
    
    private float updateRotation(float f, float f1, float f2)
    {
        float f3;
        for (f3 = f1 - f; f3 < -180F; f3 += 360F) { }
        for (; f3 >= 180F; f3 -= 360F) { }
        if (f3 > f2)
        {
            f3 = f2;
        }
        if (f3 < -f2)
        {
            f3 = -f2;
        }
        return f + f3;
    }
	
	public void setPathToBlock(int[] pos){
		pathToEntity = getEntityPathToXYZ(pos[0], pos[1], pos[2], 20F);
	}
	
	public String getEntityTexture(){
		return texture;
	}
	
	public void changeSkin(String s){
		if(s.startsWith("*")){
			username = s;
			s = s.substring(1);
			skinUrl = null;
			texture = s;
			String s2 = s.substring((s.contains("/")?s.lastIndexOf("/")+1:0), (s.contains(".")?s.lastIndexOf("."):s.length()));
			name = s2;
		}else{
			username = s;
			skinUrl = (new StringBuilder()).append("http://s3.amazonaws.com/MinecraftSkins/").append(username).append(".png").toString();
			name = s;
			worldObj.obtainEntitySkin(this);
		}

	}
	
	protected void updateWanderPath() {
		Profiler.startSection("stroll");
		boolean flag = false;
		int i = -1;
		int j = -1;
		int k = -1;
		float f = -99999F;
		for (int l = 0; l < 10; l++) {
			int i1 = MathHelper.floor_double((posX + rand.nextInt(13)) - 6D);
			int j1 = MathHelper.floor_double((posY + rand.nextInt(7)) - 3D);
			int k1 = MathHelper.floor_double((posZ + rand.nextInt(13)) - 6D);
			float f1 = getBlockPathWeight(i1, j1, k1);
			if (f1 > f) {
				f = f1;
				i = i1;
				j = j1;
				k = k1;
				flag = true;
			}
		}

		if (flag) {
			pathToEntity = getEntityPathToXYZ(i, j, k, 10F);
		}
		Profiler.endSection();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setString("username", username);
		nbttagcompound.setFloat("scale", scale);
		nbttagcompound.setFloat("lastScaleCheck", lastScaleCheck);
		nbttagcompound.setInteger("state", state);
		nbttagcompound.setDouble("guardPositions[0]", guardPositions[0]);
		nbttagcompound.setDouble("guardPositions[1]", guardPositions[1]);
		nbttagcompound.setDouble("guardPositions[2]", guardPositions[2]);
		nbttagcompound.setDouble("guardPositions[3]", guardPositions[3]);
		nbttagcompound.setDouble("guardPositions[4]", guardPositions[4]);
		nbttagcompound.setBoolean("getItems", getItems);
		nbttagcompound.setBoolean("sprintEnemy", sprintEnemy);
		nbttagcompound.setString("name", name);
		nbttagcompound.setString("texture", texture);
		nbttagcompound.setBoolean("lumber", lumber);
		nbttagcompound.setBoolean("mine", mine);
	}
	
	int viewDistance = 10;
	int[] blockToGet;
	public boolean checkLumberOrMine(){
		int[] ids = lumberOrMine();
		if(ids != null){
			blockToGet = ids;
			setPathToBlock(ids);
			return true;
		}
		return false;
	}
	
	public boolean switchToBestPickaxe(){
		int best = -1;
		int goodness = -1;
		for(int a = 0; a < 9; a ++){
			ItemStack stack = inventory.mainInventory[a];
			if(stack != null && ItemPickaxe.class.isAssignableFrom(stack.getItem().getClass())){
				ItemPickaxe pick = (ItemPickaxe)stack.getItem();
				int i = pick.toolMaterial.getMaxUses();
				if(i > goodness){
					goodness = i;
					best = a;
				}
			}
		}
		if(best > -1){
			inventory.currentItem = best;
			return true;
		}
		return false;
	}
	
	public boolean switchToBestAxe(){
		int best = -1;
		int goodness = -1;
		for(int a = 0; a < 9; a ++){
			ItemStack stack = inventory.mainInventory[a];
			if(stack != null && ItemAxe.class.isAssignableFrom(stack.getItem().getClass())){
				ItemAxe pick = (ItemAxe)stack.getItem();
				int i = pick.toolMaterial.getMaxUses();
				if(i > goodness){
					goodness = i;
					best = a;
				}
			}
		}
		if(best > -1){
			inventory.currentItem = best;
		}
		return true;
	}
	
	private int[] lumberOrMine() {
		if(lumber || mine){
			ArrayList<int[]> blocksToMines = new ArrayList<int[]>();
			if(lumber){
				ArrayList<int[]> blockss = getBlocks(mod_MyPeople.getLogs());
				for(int a = 0; a < blockss.size(); a++){
					blocksToMines.add(blockss.get(a));
				}
			}
			if(mine){
				ArrayList<int[]> blockss = getBlocks(mod_MyPeople.getOres());
				for(int a = 0; a < blockss.size(); a++){
					blocksToMines.add(blockss.get(a));
				}
			}
			MovingObjectPosition p = null;
			if(blocksToMines.size() > 0){
				blocksToMines = sort(blocksToMines);
				for(int a = 0; a < blocksToMines.size(); a++){
					int[] pos = blocksToMines.get(a);
					int id = worldObj.getBlockId(pos[0], pos[1], pos[2]);
					p = canSeeBlock(pos[0], pos[1], pos[2]);
					if(p == null || isValidBlock(worldObj.getBlockId(p.blockX, p.blockY, p.blockZ))){
						boolean OK = false;
						if(mod_MyPeople.getLogs().contains(id)){
							OK = switchToBestAxe();
						}else{
							OK = switchToBestPickaxe();
						}
						if(!OK){
							return null;
						}
						return pos;
					}else{
						if(isLumber(id)){
							int id2 = worldObj.getBlockId(p.blockX, p.blockY, p.blockZ);
							if(isLeaves(id2)){
								switchToBestAxe();
								return new int[]{p.blockX, p.blockY, p.blockZ};
							}
						}/*else if(isOre(id)){
							int id2 = worldObj.getBlockId(p.blockX, p.blockY, p.blockZ);
							int switchid = -1;
							if((switchid = isBaseBlock(id2))!=-1){
								if(switchid==0){
									if(switchToBestPickaxe()){
										return new int[]{p.blockX, p.blockY, p.blockZ};
									}
								}else if(switchid == 1){
									return new int[]{p.blockX, p.blockY, p.blockZ};
								}
								return null;
							}
						}*/
					}
				}
			}
		}
		return null;
	}

	private int isBaseBlock(int id2) {
		if(id2==Block.stone.blockID||id2==Block.sandStone.blockID || id2 == Block.cobblestone.blockID|| id2 == Block.cobblestoneMossy.blockID){
			return 0;
		}else if(id2==Block.dirt.blockID || id2==Block.gravel.blockID||id2==Block.sand.blockID){
			return 1;
		}
		return -1;
	}
	private boolean isValidBlock(int blockId) {
		return (isLumber(blockId)&&lumber)||(isOre(blockId)&&mine);
	}
	private ArrayList<int[]> sort(ArrayList<int[]> blocksToMines) {
		ArrayList<int[]> ret = new ArrayList<int[]>();
		int limit = 4000;
		while(blocksToMines.size() > 0 && limit-- > 0){
			int closest = getClosest(blocksToMines);
			if(closest > -1){
				ret.add(blocksToMines.get(closest));
				blocksToMines.remove(closest);
			}
		}
		return ret;
	}
	
	public int getClosest(ArrayList<int[]> blocks){
		int closest = -1;
		double lDistance = 0;
		for(int a = 0; a < blocks.size(); a++){
			int[] pos = blocks.get(a);
			if(pos.length == 3){
				double distance = getDistance(pos);
				if(distance < lDistance || lDistance == 0){
					lDistance = distance;
					closest = a;
				}
			}
		}
		
		return closest;
	}
	public ArrayList<int[]> getBlocks(ArrayList<Integer> blockTypes){
		ArrayList<int[]> ret = new ArrayList<int[]>(); 
		int startX = (int)Math.floor(posX-10);
		int startY = (int)Math.floor(posY-5);
		int startZ = (int)Math.floor(posZ-10);
		int stopX = (int)Math.floor(posX+10);
		int stopY = (int)Math.floor(posY+5);
		int stopZ = (int)Math.floor(posZ+10);
		for(int a = startX; a < stopX; a++){
			for(int b = startY; b < stopY; b++){
				for(int c = startZ; c < stopZ; c++){
					if(b > 0){
						int id = worldObj.getBlockId(a, b, c);
						if(blockTypes.contains(id)){
							ret.add(new int[]{a, b, c});
						}
					}
				}
			}
		}
		return ret;
	}
	
	public MovingObjectPosition canSeeBlock(int x, int y, int z){
		double d = this.getDistance(new int[]{x, y, z});
		MovingObjectPosition p1 = worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(x, y, z));
		if(p1 != null){return p1;}
		MovingObjectPosition p2 = worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(x+1, y+1, z+1));
		if(p2 != null){return p2;}
/*		MovingObjectPosition p3 = worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(x+1, y+1, z));
		if(p3 != null){return p3;}
		MovingObjectPosition p4 = worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(x+1, y, z));
		if(p4 != null){return p4;}
		MovingObjectPosition p5 = worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(x, y, z+1));
		if(p5 != null){return p5;}
		MovingObjectPosition p6 = worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(x, y+1, z+1));
		if(p6 != null){return p6;}
		MovingObjectPosition p7 = worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(x, y+1, z));
		if(p7 != null){return p7;}
		MovingObjectPosition p8 = worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(x+1, y, z+1));
		if(p8 != null){return p8;}*/
		return null;
	}

	
	public void stopMining() {
		curBlockDamage = 0.0F;
        blockHitWait = 0;
        isMining = false;
        inventory.currentItem = 0;
        blockToGet = null;
        pathToEntity = null;
	}
	
	public void startMining() {
		isMining = true;
		int id = worldObj.getBlockId(blockToGet[0], blockToGet[1], blockToGet[2]);
		boolean OK = false;
		if(mod_MyPeople.getLogs().contains(id)){
			OK = switchToBestAxe();
		}else{
			OK = switchToBestPickaxe();
		}
	}
	
	public void onMine(){

        if (blockHitWait > 0)
        {
            blockHitWait--;
            return;
        }
		swingItem();
        int i = blockToGet[0];
        int j = blockToGet[1];
        int k = blockToGet[2];
        Minecraft mc = ModLoader.getMinecraftInstance();
        if (i == curBlockX && j == curBlockY && k == curBlockZ)
        {
            int i1 = mc.theWorld.getBlockId(i, j, k);
            if (i1 == 0)
            {
                return;
            }
            Block block = Block.blocksList[i1];
            curBlockDamage += block.blockStrength(this);
            if (blockDestroySoundCounter % 4F == 0.0F && block != null)
            {
            	try{
                ModLoader.getMinecraftInstance().sndManager.playSound(block.stepSound.getStepSound(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (block.stepSound.getVolume() + 1.0F) / 8F, block.stepSound.getPitch() * 0.5F);
            	}catch(Exception e){}
            }
            blockDestroySoundCounter++;

            if (curBlockDamage >= 1.0F)
            {
                onPlayerDestroyBlock(i, j, k);
                curBlockDamage = 0.0F;
                prevBlockDamage = 0.0F;
                blockDestroySoundCounter = 0.0F;
                blockHitWait = 5;
            }
        }
        else
        {
            curBlockDamage = 0.0F;
            prevBlockDamage = 0.0F;
            blockDestroySoundCounter = 0.0F;
            curBlockX = i;
            curBlockY = j;
            curBlockZ = k;
        }
	}
	
    public boolean onPlayerDestroyBlock2(int i, int j, int k)
    {
        World world = worldObj;
        Block block = Block.blocksList[world.getBlockId(i, j, k)];
        if (block == null)
        {
            return false;
        }
        try{
        	world.playAuxSFX(2001, i, j, k, block.blockID + (world.getBlockMetadata(i, j, k) << 12));
        }catch(Exception e){
        	System.out.println("Could Not Play Stupid Sound!   ID:" + block.blockID + " META:"+(world.getBlockMetadata(i, j, k)));
        	e.printStackTrace();
        }
        int i1 = world.getBlockMetadata(i, j, k);
        boolean flag = world.setBlockWithNotify(i, j, k, 0);
        if (block != null && flag)
        {
            block.onBlockDestroyedByPlayer(world, i, j, k, i1);
        }
        return flag;
    }
	
    public boolean onPlayerDestroyBlock(int i, int j, int k)
    {
        int i1 = worldObj.getBlockId(i, j, k);
        int j1 = worldObj.getBlockMetadata(i, j, k);
        boolean flag = onPlayerDestroyBlock2(i, j, k);
        ItemStack itemstack = this.getCurrentEquippedItem();
        boolean flag1 = this.canHarvestBlock(Block.blocksList[i1]);
        if (itemstack != null)
        {
            itemstack.onDestroyBlock(i1, i, j, k, this);
            if (itemstack.stackSize == 0)
            {
                itemstack.onItemDestroyedByUse(this);
                this.destroyCurrentEquippedItem();
            }
        }
        if (flag && flag1)
        {
            Block.blocksList[i1].harvestBlock(worldObj, this, i, j, k, j1);
        }
        checkSurroundingBlocks(i, j, k);
        return flag;
    }
	
    public void checkSurroundingBlocks(int i, int j, int k) {
    	int[][] ids = new int[6][3];
    	ids[0][0] = i;
    	ids[0][1] = j;
    	ids[0][2] = k+1;
    	ids[1][0] = i;
    	ids[1][1] = j;
    	ids[1][2] = k-1;
    	ids[2][0] = i;
    	ids[2][1] = j+1;
    	ids[2][2] = k;
    	ids[3][0] = i;
    	ids[3][1] = j-1;
    	ids[3][2] = k;
    	ids[4][0] = i+1;
    	ids[4][1] = j;
    	ids[4][2] = k;
    	ids[5][0] = i-1;
    	ids[5][1] = j;
    	ids[5][2] = k;
    	for(int a = 0; a < ids.length; a++){
    		int[] pos = ids[a];
    		int id = worldObj.getBlockId(pos[0], pos[1], pos[2]);
    		if(isLumber(id) && this.getDistance(pos) < 16){
    			switchToBestAxe();
    			blockToGet = pos;
    		}else if(isOre(id)&& this.getDistance(pos) < 16){
    			if(switchToBestPickaxe()){
    				blockToGet = pos;
    				break;
    			}
    		}
    	}
		
	}
    
    public boolean isLumber(int id){
    	return mod_MyPeople.getLogs().contains(id);
    }
    public boolean isOre(int id){
    	return mod_MyPeople.getOres().contains(id);
    }
	private int curBlockX;
    private int curBlockY;
    private int curBlockZ;
    private float curBlockDamage;
    private float prevBlockDamage;
    private float blockDestroySoundCounter;
    private int blockHitWait;
	
}
