package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class mod_MyPeople extends BaseMod{
	public static Item personSpawner;
	public static Item needle;
	public static Item needleFull;
	public static Item knife;
	public static Item knifeBlood;
	public static Item testTube;
	public static Item testTubeBlood;
	public static Item testTubeSpunBlood;
	public static Item personFinder;
	public static Block humanEgg;
	public static Block centrifuge;
	public static final int centrifugeTop = ModLoader.addOverride("/terrain.png", "/MyPeople/centrifugeTop.png");
	public static int textureFX;
	public String getVersion() {
		return "0.4_1";
	}

	public void load() {
		int i = 239;
		try {
			Method m = ModLoader.class.getDeclaredMethod("getUniqueItemSpriteIndex", new Class[]{});
			if(m != null){
				m.setAccessible(true);
				try {
					Object o = m.invoke(null, new Object[]{});
					if(o != null){
						int i2 = (Integer)o;
						i = i2;
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		loadConfig();
		personSpawner = new ItemPersonSpawner(getID("personSpawner", 989)).setIconIndex(0).setItemName("item.personSpawner");
		needle = new Item(getID("needle", 990)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/MyPeople/needleEmpty.png")).setItemName("item.needleEmpty");
		needleFull = new ItemTestTubeBlood(getID("needleFull", 991)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/MyPeople/needleFull.png")).setItemName("item.needleFull").setHasSubtypes(true).setContainerItem(needle);
		knife = new ItemKnife(getID("knife", 992)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/MyPeople/knife.png")).setItemName("item.knife").setMaxStackSize(1);
		knifeBlood = new ItemKnife(getID("knifeBlood", 993), true).setIconIndex(ModLoader.addOverride("/gui/items.png", "/MyPeople/knifeBlood.png")).setItemName("item.knifeBlood").setMaxStackSize(1).setContainerItem(knife);
		testTube = new Item(getID("testTube", 994)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/MyPeople/testTube.png")).setItemName("item.testTube").setMaxStackSize(1);
		testTubeBlood = new ItemTestTubeBlood(getID("testTubeBlood", 995)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/MyPeople/testTubeBlood.png")).setItemName("item.testTubeBlood").setMaxStackSize(1).setHasSubtypes(true).setContainerItem(testTube);
		testTubeSpunBlood = new ItemTestTubeBlood(getID("testTubeSpunBlood", 996)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/MyPeople/testTubeSpun.png")).setItemName("item.testTubeSpun").setMaxStackSize(1).setHasSubtypes(true).setContainerItem(testTube);
		personFinder = new ItemPersonFinder(getID("personFinder", 997)).setIconIndex(i).setItemName("clonetracker");
		humanEgg = new BlockHumanEgg(getID("humanEgg", 178)).setBlockName("tile.humanEgg").setHardness(1.0f);
		centrifuge = new BlockCentrifuge(getID("centrifuge", 179)).setBlockName("tile.centrifuge").setHardness(3.5f);
		humanEgg.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/MyPeople/humanEgg.png");
		centrifuge.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/MyPeople/centrifugeSide.png");
		ModLoader.registerBlock(humanEgg);
		ModLoader.registerBlock(centrifuge);
		ModLoader.addName(personSpawner, "Person Spawner");
		ModLoader.addName(needle, "Needle");
		ModLoader.addName(needleFull, "Needle Containing Genomic DNA");
		ModLoader.addName(humanEgg, "Human Egg");
		ModLoader.addName(knife, "Knife");
		ModLoader.addName(knifeBlood, "Blood Covered Knife");
		ModLoader.addName(centrifuge, "Centrifuge");
		ModLoader.addName(testTube, "Test Tube");
		ModLoader.addName(testTubeBlood, "Test Tube Of Blood");
		ModLoader.addName(testTubeSpunBlood, "Seperated Blood Sample");
		ModLoader.addName(personFinder, "Clone Tracker");
		ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(new TexturePersonFinderFX(ModLoader.getMinecraftInstance()));
		ModLoader.addShapelessRecipe(new ItemStack(testTubeBlood, 1, 0), new Object[]{
			new ItemStack(knife, 1, 0), testTube
		});
		for(int a = 50; a < 63; a++){
			ModLoader.addShapelessRecipe(new ItemStack(testTubeBlood, 1, a), new Object[]{
				new ItemStack(knifeBlood, 1, a), testTube
			});
			ModLoader.addShapelessRecipe(new ItemStack(Item.monsterPlacer, 1, a), new Object[]{
				new ItemStack(needleFull, 1, a), Item.egg, new ItemStack(Item.dyePowder, 1, 15)
			});
			ModLoader.addShapelessRecipe(new ItemStack(needleFull, 1, a), new Object[]{
				needle, new ItemStack(testTubeSpunBlood, 1, a)
			});
		}
		for(int a = 90; a < 97; a++){
			ModLoader.addShapelessRecipe(new ItemStack(testTubeBlood, 1, a), new Object[]{
				new ItemStack(knifeBlood, 1, a), testTube
			});
			ModLoader.addShapelessRecipe(new ItemStack(Item.monsterPlacer, 1, a), new Object[]{
				new ItemStack(needleFull, 1, a), Item.egg, new ItemStack(Item.dyePowder, 1, 15)
			});
			ModLoader.addShapelessRecipe(new ItemStack(needleFull, 1, a), new Object[]{
				needle, new ItemStack(testTubeSpunBlood, 1, a)
			});
		}
		ModLoader.addShapelessRecipe(new ItemStack(testTubeBlood, 1, 120), new Object[]{
			new ItemStack(knifeBlood, 1, 120), testTube
		});
		ModLoader.addShapelessRecipe(new ItemStack(needleFull, 1, 120), new Object[]{
			needle, new ItemStack(testTubeSpunBlood, 1, 120)
		});
		ModLoader.addShapelessRecipe(new ItemStack(Item.monsterPlacer, 1, 120), new Object[]{
			new ItemStack(needleFull, 1, 120), Item.egg, new ItemStack(Item.dyePowder, 1, 15)
		});
		ModLoader.addShapelessRecipe(new ItemStack(testTubeBlood, 1, 0), new Object[]{
			new ItemStack(knifeBlood, 1, 0), testTube
		});
		ModLoader.addRecipe(new ItemStack(testTube, 1), new Object[]{
			"X X", "X X", "XXX", Character.valueOf('X'), Block.thinGlass
		});
		ModLoader.addRecipe(new ItemStack(knife, 1), new Object[]{
			"X  ", " X ", "  Y", Character.valueOf('X'), Item.ingotIron, Character.valueOf('Y'), Item.stick
		});
		ModLoader.addRecipe(new ItemStack(needle), new Object[]{
			"XXX", "X X", " Y ", Character.valueOf('X'), Item.ingotIron, Character.valueOf('Y'), Item.ingotGold
		});
		ModLoader.addRecipe(new ItemStack(centrifuge), new Object[]{
			"XYX", "YDY", "XYX", Character.valueOf('X'), Block.pistonBase, Character.valueOf('Y'), Item.ingotIron, Character.valueOf('D'), Item.diamond
		});
		ModLoader.addShapelessRecipe(new ItemStack(personFinder), new Object[]{
			new ItemStack(Item.compass), new ItemStack(testTubeSpunBlood, 1, 0)
		});
		ModLoader.addShapelessRecipe(new ItemStack(needleFull), new Object[]{
			needle, new ItemStack(testTubeSpunBlood, 1, 0)
		});
		ModLoader.addShapelessRecipe(new ItemStack(humanEgg), new Object[]{
			new ItemStack(needleFull, 1, 0), Item.egg, new ItemStack(Item.dyePowder, 1, 15)
		});
		ModLoader.registerEntityID(net.minecraft.src.EntityMyPerson.class, "MyPerson", ModLoader.getUniqueEntityId());
		ModLoader.registerTileEntity(net.minecraft.src.TileEntityHumanEgg.class, "HumanEgg");
		ModLoader.registerTileEntity(net.minecraft.src.TileEntityCentrifuge.class, "Centrifuge");
	}
	
	public static ArrayList<Integer> load(Class c){
		ArrayList<Integer> logers = new ArrayList<Integer>();
		try{
			Field[] fields = Block.class.getDeclaredFields();
			for(int a = 0; a < fields.length; a++){
				try{
					fields[a].setAccessible(true);
					if(java.lang.reflect.Modifier.isStatic(fields[a].getModifiers())){
						Object o = fields[a].get(null);
						if(o != null && c.isAssignableFrom(o.getClass())){
							int id = ((Block)o).blockID;
							logers.add(id);
						}
					}
				}catch(Exception e2){
					e2.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return logers;
	}
	static ArrayList<Integer> logs;
	static ArrayList<Integer> ores;

	private int getID(String s, int i) {
		if(data.containsKey(s+"ID")){
			try{
				int i2 = i;
				i2 = Integer.parseInt(data.get(s+"ID"));
				return i2;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return i;
	}
	
	public static ArrayList<Integer> getLogs(){
		if(logs == null){
			logs = load(BlockLog.class);
		}
		return logs;
	}
	
	public static ArrayList<Integer> getOres(){
		if(ores == null){
			ores = load(BlockOre.class);
			ores.add(Block.oreRedstone.blockID);
			ores.add(Block.oreRedstoneGlowing.blockID);
		}
		return ores;
	}
	

	@Override
	public void addRenderer(Map map) {
		map.put(net.minecraft.src.EntityMyPerson.class, new RenderMyPlayer());
	}
	
	public static MovingObjectPosition getLookAt(){
		double d = 250d;
		Vec3D vec3d = ModLoader.getMinecraftInstance().thePlayer.getPosition(1.0f);
        Vec3D vec3d1 = ModLoader.getMinecraftInstance().thePlayer.getLook(1.0f);
        Vec3D vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
        return ModLoader.getMinecraftInstance().theWorld.rayTraceBlocks(vec3d, vec3d2);
	}
	
	static String lastName = "";
	
	
	
	public void loadConfig(){
		File f2 = new File(ModLoader.getMinecraftInstance().getAppDir("minecraft"), "config/MyPeople.cfg");
		f2.getParentFile().mkdirs();
		if(!f2.exists()){
			BufferedWriter writer = null;
			try{
				writer = new BufferedWriter(new FileWriter(f2));
				String[] data = {"personSpawnerID = 989"
						, "needleID = 990"
						, "needleFullID = 991"
						, "knifeID = 992"
						, "knifeBloodID = 993"
						, "testTubeID = 994"
						, "testTubeBloodID = 995"
						, "testTubeSpunBloodID = 996"
						, "personFinder = 997"
						, "humanEggID = 178"
						, "centrifugeID = 179"
				};
				for(int a = 0; a < data.length; a++){
					writer.write(data[a]);
					if(a+1 != data.length){writer.newLine();}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(writer != null){
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			BufferedReader reader = null;
			try{
				reader = new BufferedReader(new FileReader(f2));
				String s = reader.readLine();
				while(s != null){
					if(s.contains("=")){
						String[] split = s.split("=");
						if(split.length==2){
							data.put(split[0].trim(), split[1].trim());
						}
					}
					s = reader.readLine();
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(reader != null){
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	HashMap<String, String> data = new HashMap<String, String>();
	static boolean hornBlown = false;
	static Entity entityToFind = null;
	

}
