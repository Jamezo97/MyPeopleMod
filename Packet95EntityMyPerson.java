package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet95EntityMyPerson extends Packet{

	int type = 0;
	public void readPacketData(DataInputStream in) throws IOException {
		type = in.readByte()&0xFF;
	}

	@Override
	public void writePacketData(DataOutputStream out) throws IOException {
		
		
	}

	@Override
	public void processPacket(NetHandler nethandler) {
		if(type == 1){
			
		}
	}

	@Override
	public int getPacketSize() {
		return 1;
	}

}
