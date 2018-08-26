package com.github.hageldave.fftbenchmark.impls;

import java.util.Arrays;

import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.DoubleFFT_2D;
import org.jtransforms.fft.FloatFFT_3D;

import com.github.hageldave.fftbenchmark.interfaces.FFT1D;
import com.github.hageldave.fftbenchmark.interfaces.FFT2D;
import com.github.hageldave.fftbenchmark.interfaces.FFT3D;

public class JT_FFT implements FFT1D,FFT2D,FFT3D {

	@Override
	public void doubleArrayFFT_SetDC2Zero_1D(double[] array, double[] result) {
		DoubleFFT_1D fft = new DoubleFFT_1D(array.length);
		double[] transform = Arrays.copyOf(array, array.length*2);
		fft.realForwardFull(transform);
		transform[0]=0;
		fft.complexInverse(transform, false);
		System.arraycopy(transform, 0, result, 0, array.length);
	}

	@Override
	public void doubleArrayFFT_SetDC2Zero_2D(double[] array, double[] result, int width, int height) {
		DoubleFFT_2D fft = new DoubleFFT_2D(height,width);
		double[] transform = Arrays.copyOf(array, width*height*2);
		fft.realForwardFull(transform);
		transform[0]=0;
		fft.complexInverse(transform, false);
		System.arraycopy(transform, 0, result, 0, width*height);
	}
	
	@Override
	public void doubleArrayFFT_SetDC2Zero_3D(float[] array, float[] result, int width, int height, int depth) {
		FloatFFT_3D fft = new FloatFFT_3D(depth, height, width);
		float[] transform = Arrays.copyOf(array, width*height*depth*2);
		fft.realForwardFull(transform);
		transform[0]=0;
		fft.complexInverse(transform, false);
		System.arraycopy(transform, 0, result, 0, width*height*depth);
	}

	

}
