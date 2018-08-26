/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.hageldave.fftbenchmark;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import com.github.hageldave.fftbenchmark.impls.Implementations;
import com.github.hageldave.fftbenchmark.interfaces.FFT3D;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 2, time = 20, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 20, timeUnit = TimeUnit.SECONDS)
@Fork(value=1, jvmArgsAppend={"-Xmx6g","-Xms3g"})
public class FFT3dBenchmark {

	private static final int NUM_THREADS = 4;
	
	@Param({
		"100x120x140",
		"256x256x256",
		"400x300x200",
	})
	public String dims="";
	
	@Param({
		"JTransforms",
		"snglthrd_JT",
		"ezFFTW"
	})
	public String implementation="";
	
	private int width=0;
	private int height=0;
	private int depth=0;
	private int size=0;
	private FFT3D fft_test;
	private float[] toTransform;
	private float[] result;

	@Setup
	public void setup(){
		fft_test = Implementations.valueOf(implementation).fft3d;
		String[] dimstr = dims.split("x");
		width = Integer.parseInt(dimstr[0]);
		height = Integer.parseInt(dimstr[1]);
		depth = Integer.parseInt(dimstr[2]);
		size = width*height*depth;
		
		toTransform = new float[size];
		result = new float[size];
	}

	@Benchmark
	public double singleArrayInput() {
		fft_test.doubleArrayFFT_SetDC2Zero_3D(toTransform, result, width, height, depth);
		return result[size-1];
	}
	
	@Benchmark
	public double parallelInvocations() {
		fft_test.doubleArrayFFT_SetDC2Zero_3D(toTransform, result, width, height, depth);
		IntStream.range(0, NUM_THREADS).parallel().forEach((int i)->{
			fft_test.doubleArrayFFT_SetDC2Zero_3D(toTransform, result, width, height, depth);
		});
		return result[size-1];
	}

}
