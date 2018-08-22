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

import java.util.ArrayList;
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

import com.github.hageldave.fftbenchmark.interfaces.FFT1D;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(value=1)
public class FFT1dBenchmark {

	private static final int NUM_THREADS = 16;
	
	@Param({
		"512",
		"1536",
		"16384",
		"98304",
		"589829"
	})
	public int dims=0;
	
	@Param({
		"JTransforms",
		"ezFFTW"
	})
	public String implementation="";
	
	private FFT1D fft_test;
	private double[] toTransform;
	private double[] result;
	private ArrayList<Double> listToTransform;
	private ArrayList<Double> listResult;
	private double[][] multipleToTransform;
	private double[][] multipleResult;
	private int size=0;

	@Setup
	public void setup(){
		size=dims;
		fft_test = Implementations.valueOf(implementation).fft1d;
		toTransform = new double[size];
		result = new double[size];
		listToTransform = new ArrayList<>(size*2/3);
		listResult = new ArrayList<>(size*2/3);
		multipleToTransform = new double[NUM_THREADS][size];
		multipleResult = new double[NUM_THREADS][size];
		for(int i=0;i<size;i++){
			toTransform[i] = i*0.01+Math.sin(i);
			listToTransform.add(toTransform[i]);
			for(int j=0;j<NUM_THREADS;j++){
				multipleToTransform[j][i] = toTransform[i];
			}
		}
	}

	@Benchmark
	public double arrayInput() {
		fft_test.doubleArrayFFT_SetDC2Zero_1D(toTransform, result);
		return result[size-1];
	}
	
	@Benchmark
	public double listInput() {
		try{
			fft_test.doubleListFFT_SetDC2Zero_1D(listToTransform, listResult);
			return listResult.get(size-1);
		} finally {
			listResult.clear();
		}
	}
	
	@Benchmark
	public double parallelInvocations() {
		fft_test.doubleArrayFFT_SetDC2Zero_1D(toTransform, result);
		IntStream.range(0, NUM_THREADS).parallel().forEach((int i)->{
			fft_test.doubleArrayFFT_SetDC2Zero_1D(multipleToTransform[i], multipleResult[i]);
		});
		return multipleResult[NUM_THREADS/2][size-1];
	}

}
