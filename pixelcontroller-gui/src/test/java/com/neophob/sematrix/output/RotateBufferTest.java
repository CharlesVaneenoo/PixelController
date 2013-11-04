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
package com.neophob.sematrix.output;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neophob.sematrix.properties.DeviceConfig;

/**
 * verify the rotate buffer code
 * @author michu
 *
 */
public class RotateBufferTest {

	public static void dumpBuffer(int[] ret) {
		int a=0;
		for (int r: ret) {
			System.out.print(r+", ");
			a++;
			if (a==8) {System.out.println();a=0;}
		}
	}
	
	@Test
	public void rotateTest8x8() {
		int[] ret;
		int[] buffer = new int[] {
			1,2,3,0,0,0,0,0,
			0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,
			9,0,0,0,0,0,0,4
		};
		
		ret = RotateBuffer.transformImage(buffer, DeviceConfig.NO_ROTATE, 8, 8);
		assertEquals(1, ret[0]);
		assertEquals(2, ret[1]);

		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_90, 8, 8);
		assertEquals(1, ret[7]);
		assertEquals(2, ret[15]);
		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_90_FLIPPEDY, 8, 8);
		assertEquals(1, ret[63]);
		assertEquals(2, ret[55]);

		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_180, 8, 8);
		assertEquals(1, ret[63]);
		assertEquals(2, ret[62]);
		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_180_FLIPPEDY, 8, 8);
		assertEquals(1, ret[7]);
		assertEquals(2, ret[6]);

		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_270, 8, 8);
		assertEquals(1, ret[56]);
		assertEquals(2, ret[48]);
	}

	@Test
	public void rotateTest8x4() {
		int[] ret;
		int[] buffer = new int[] {
			1,2,3,0,0,0,0,0,
			0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,4
		};
		
		ret = RotateBuffer.transformImage(buffer, DeviceConfig.NO_ROTATE, 8, 4);
		assertEquals(1, ret[0]);
		assertEquals(3, ret[2]);

		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_90, 8, 4);
		assertEquals(1, ret[7]);
		assertEquals(2, ret[15]);
		assertEquals(4, ret[24]);
		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_90_FLIPPEDY, 8, 4);
		assertEquals(1, ret[31]);
		assertEquals(2, ret[23]);
		assertEquals(4, ret[0]);

		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_180, 8, 4);
		assertEquals(1, ret[31]);
		assertEquals(2, ret[30]);
		assertEquals(4, ret[0]);
		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_180_FLIPPEDY, 8, 4);
		assertEquals(1, ret[7]);
		assertEquals(2, ret[6]);
		assertEquals(4, ret[24]);

		ret = RotateBuffer.transformImage(buffer, DeviceConfig.ROTATE_270, 8, 4);
		assertEquals(1, ret[24]);
		assertEquals(2, ret[16]);
		assertEquals(4, ret[7]);
	}

}
