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

package eu.the5zig.mod.gui;

import com.google.common.collect.Lists;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.TextMacro;
import eu.the5zig.mod.config.TextMacros;
import eu.the5zig.mod.gui.elements.Clickable;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IGuiList;
import eu.the5zig.util.minecraft.ChatColor;

public class GuiTextMacroList extends Gui {

	private IGuiList<TextMacro> guiList;

	public GuiTextMacroList(Gui lastScreen) {
		super(lastScreen);
	}

	@Override
	public void initGui() {
		guiList = The5zigMod.getVars().createGuiList(new Clickable<TextMacro>() {
			@Override
			public void onSelect(int id, TextMacro row, boolean doubleClick) {
				if (doubleClick) {
					actionPerformed0(getButtonById(2));
				}
			}
		}, getWidth(), getHeight(), 64, getHeight() - 48, 0, getWidth(), The5zigMod.getTextMacroConfiguration().getConfigInstance().getMacros());
		guiList.setRowWidth(220);
		addGuiList(guiList);

		addButton(The5zigMod.getVars().createButton(1, getWidth() / 2 - 190, getHeight() - 38, 90, 20, I18n.translate("text_macros.add")));
		addButton(The5zigMod.getVars().createButton(2, getWidth() / 2 - 95, getHeight() - 38, 90, 20, I18n.translate("text_macros.edit")));
		addButton(The5zigMod.getVars().createButton(3, getWidth() / 2, getHeight() - 38, 90, 20, I18n.translate("text_macros.delete")));
		addButton(The5zigMod.getVars().createButton(200, getWidth() / 2 + 95, getHeight() - 38, 95, 20, The5zigMod.getVars().translate("gui.back")));
	}

	@Override
	protected void actionPerformed(IButton button) {
		TextMacros textMacrosConfig = The5zigMod.getTextMacroConfiguration().getConfigInstance();
		if (button.getId() == 1) {
			TextMacro textMacro = new TextMacro(Lists.<Integer>newArrayList(), "", true);
			textMacrosConfig.getMacros().add(textMacro);
			The5zigMod.getVars().displayScreen(new GuiTextMacroEdit(this, textMacro));
		}
		TextMacro selectedRow = guiList.getSelectedRow();
		if (button.getId() == 2) {
			if (selectedRow == null)
				return;
			The5zigMod.getVars().displayScreen(new GuiTextMacroEdit(this, selectedRow));
		}
		if (button.getId() == 3) {
			if (selectedRow == null)
				return;
			textMacrosConfig.getMacros().remove(selectedRow);
			The5zigMod.getTextMacroConfiguration().saveConfig();
		}
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int y = 0;
		for (String line : The5zigMod.getVars().splitStringToWidth(I18n.translate("text_macros.help"), getWidth() / 4 * 3)) {
			drawCenteredString(ChatColor.GRAY + line, getWidth() / 2, 34 + y);
			y += 10;
		}
	}

	@Override
	public String getTitleKey() {
		return "text_macros.title";
	}
}
