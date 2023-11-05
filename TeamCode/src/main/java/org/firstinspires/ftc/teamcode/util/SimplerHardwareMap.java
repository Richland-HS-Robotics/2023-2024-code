
/**
 Copyright (c) 2019 HF Robotics (http://www.hfrobots.com)
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 **/
package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.List;
import java.util.Set;

/**
 * This is a wrapper interface for the hardwareMap variable.
 * This allows us to create a mock hardwareMap to use for unit tests.
 * @see RealSimplerHardwareMap
 *
 */
public interface SimplerHardwareMap {
    <T> T get(Class<? extends T> classOrInterface, String deviceName);

    <T> List<T> getAll(Class<? extends T> classOrInterface);

    Set<String> getNamesOf(HardwareDevice device);
}