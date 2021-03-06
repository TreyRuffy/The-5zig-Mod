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

package eu.the5zig.mod.manager;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.Server;
import eu.the5zig.util.Utils;

public class AutoReconnectManager {

	private long countdownStartTime;
	private Object parentScreen;
	private Object lastServerData;

	public void setServerData(Object serverData) {
		this.lastServerData = serverData;
	}

	public void startCountdown(Object parentScreen) {
		Server server = The5zigMod.getDataManager().getServer();
		The5zigMod.getDataManager().resetServer();
		if (lastServerData == null) {
			return;
		}
		System.out.println(The5zigMod.getConfig().getInt("autoServerReconnect"));
		if (The5zigMod.getConfig().getInt("autoServerReconnect") < 5 || (server != null && !server.isAutoReconnecting())) {
			countdownStartTime = 0;
			return;
		}
		countdownStartTime = System.currentTimeMillis();
		this.parentScreen = parentScreen;
	}

	public void checkCountdown(int guiWidth, int guiHeight) {
		if (lastServerData == null || countdownStartTime == 0) {
			return;
		}
		if (System.currentTimeMillis() - countdownStartTime > The5zigMod.getConfig().getInt("autoServerReconnect") * 1000) {
			The5zigMod.getVars().joinServer(parentScreen, lastServerData);
			countdownStartTime = 0;
		} else {
			The5zigMod.getVars().drawCenteredString(
					I18n.translate("server.reconnecting", Utils.getShortenedDouble((The5zigMod.getConfig().getInt("autoServerReconnect")
							* 1000 - (System.currentTimeMillis() - countdownStartTime)) / 1000.0, 1)), guiWidth / 2,
					guiHeight - 12);
		}
	}

}
