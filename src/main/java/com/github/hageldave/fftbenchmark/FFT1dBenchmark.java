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

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.jtransforms.fft.DoubleFFT_1D;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import hageldave.ezfftw.dp.FFT;
import hageldave.ezfftw.dp.FFTW_Guru;
import hageldave.ezfftw.dp.NativeRealArray;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(value=2)
public class FFT1dBenchmark {

	//	public static void main(String[] args) throws Exception {
	//		Main.main(args);
	//	}

	@Param({
		"64",
		"511",
		"2048",
		"16385",
		"1048576",
		"4194305"
	})
	public int size=0;

	public double[] toTransform;
	public double[] toTransformJT;
	DoubleFFT_1D jt;
	NativeRealArray in;
	NativeRealArray rout;
	NativeRealArray iout;

	@Setup
	public void setup(){
		toTransform = new double[size];
		for(int i=0; i<size;i++)
			toTransform[i] = (Math.sin(i)+Math.cos(i+1)+Math.sin(i*2)+Math.cos(i*2+1));
		// jtransform setup
		toTransformJT = Arrays.copyOf(toTransform, size*2);
		jt = new DoubleFFT_1D(size);

		// ezfftw setup
		in = new NativeRealArray(size);
		in.set(toTransform);
		rout = new NativeRealArray(size);
		iout = new NativeRealArray(size);
	}

	@Benchmark
	public void pureFFT_JT() {
		jt.realForwardFull(toTransformJT);
		jt.realInverseFull(toTransformJT, false);
	}

	@Benchmark
	public void pureFFT_EZ() {
		FFTW_Guru.execute_split_r2c(in, rout, iout, size);
		FFTW_Guru.execute_split_c2r(rout, iout, in, size);
	}

	@Benchmark
	public double[] fftWithPrep_JT() {
		double[] input = Arrays.copyOf(toTransform, size*2);
		DoubleFFT_1D fft = new DoubleFFT_1D(size);
		fft.realForwardFull(input);
		/* processing in fourier space would be here */
		fft.realInverseFull(input, false);
		return input;
	}

	@Benchmark
	public double[] fftWithPrep_EZ() {
		double[] rOut = new double[size];
		double[] iOut = new double[size];
		FFT.fft(toTransform, rOut, iOut, size);
		/* processing in fourier space would be here */
		FFT.ifft(rOut, iOut, toTransform, size);
		return toTransform;
	}

}
