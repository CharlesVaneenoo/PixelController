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
package com.neophob.sematrix.effect;

import com.neophob.sematrix.glue.MatrixData;
import com.neophob.sematrix.input.SeSound;
import com.neophob.sematrix.resize.Resize.ResizeName;

/**
 * The Class Voluminize.
 */
public class Voluminize extends Effect {

    private float volume = 0;
    private SeSound sound;
    
	/**
	 * Instantiates a new voluminize.
	 *
	 * @param controller the controller
	 */
	public Voluminize(MatrixData matrix, SeSound sound) {
		super(matrix, EffectName.VOLUMINIZE, ResizeName.QUALITY_RESIZE);
		this.sound = sound;
	}

	/* (non-Javadoc)
	 * @see com.neophob.sematrix.effect.Effect#getBuffer(int[])
	 */
	public int[] getBuffer(int[] buffer) {
		int[] ret = new int[buffer.length];
		
		for (int i=0; i<buffer.length; i++){
    		ret[i]= (int)(buffer[i]*volume);
		}
		return ret;
	}
	
    @Override
    public void update() {
        volume = sound.getVolumeNormalized();
    }

}
