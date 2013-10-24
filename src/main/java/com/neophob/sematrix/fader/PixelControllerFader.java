/**
 * Copyright (C) 2011-2013 Michael Vogt <michu@neophob.com>
 *
 * This file is part of PixelController.
 *
 * PixelController is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PixelController is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PixelController.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.neophob.sematrix.fader;

import com.neophob.sematrix.fader.Fader.FaderName;
import com.neophob.sematrix.properties.ApplicationConfigurationHelper;

/**
 * 
 * @author michu
 *
 */
public final class PixelControllerFader {

	private int presetLoadingFadeTime;
	
	public PixelControllerFader(ApplicationConfigurationHelper ah) {
		presetLoadingFadeTime = ah.getPresetLoadingFadeTime();
	}
	
	/* 
	 * FADER ======================================================
	 */

	/**
	 * return a NEW INSTANCE of a fader.
	 *
	 * @param faderName the fader name
	 * @return the fader
	 */
	public static Fader getFader(FaderName faderName) {
		switch (faderName) {
		case CROSSFADE:
			return new Crossfader();
		case SWITCH:
			return new Switch();
		case SLIDE_UPSIDE_DOWN:
			return new SlideUpsideDown();
		case SLIDE_LEFT_RIGHT:
			return new SlideLeftRight();
		}
		return null;
	}

	/**
	 * return a fader with a specific duration
	 * 
	 * @param index
	 * @return
	 */
	public Fader getPresetFader(int index) {
		switch (index) {
		case 0:
			return new Switch();
		case 1:
			return new Crossfader(presetLoadingFadeTime);
		case 2:
			return new SlideUpsideDown(presetLoadingFadeTime);
		case 3:
			return new SlideLeftRight(presetLoadingFadeTime);
		}
		return null;
	}

	/**
	 * return a fader with default duration
	 * 
	 * @param index
	 * @return
	 */
	public Fader getFader(int index) {
		switch (index) {
		case 0:
			return new Switch();
		case 1:
			return new Crossfader();
		case 2:
			return new SlideUpsideDown();
		case 3:
			return new SlideLeftRight();
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public int getFaderCount() {
		return FaderName.values().length;
	}



}
