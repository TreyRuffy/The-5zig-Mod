/*
 * Copyright (c) 2019-2020 5zig Reborn
 * Copyright (c) 2015-2019 5zig
 *
 * This file is part of The 5zig Mod
 * The 5zig Mod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The 5zig Mod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The 5zig Mod.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketFileTransferStart implements Packet {

	private int fileId;
	private int parts;
	private int chunkSize;

	public PacketFileTransferStart(int fileId, int parts, int chunkSize) {
		this.fileId = fileId;
		this.parts = parts;
		this.chunkSize = chunkSize;
	}

	public PacketFileTransferStart() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.fileId = PacketBuffer.readVarIntFromBuffer(buffer);
		this.parts = PacketBuffer.readVarIntFromBuffer(buffer);
		this.chunkSize = PacketBuffer.readVarIntFromBuffer(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, fileId);
		PacketBuffer.writeVarIntToBuffer(buffer, parts);
		PacketBuffer.writeVarIntToBuffer(buffer, chunkSize);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getConversationManager().handleFileStart(fileId, parts, chunkSize);
	}

	public enum Type {
		IMAGE, AUDIO
	}

}
