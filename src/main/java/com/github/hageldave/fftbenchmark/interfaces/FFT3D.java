package com.github.hageldave.fftbenchmark.interfaces;

public interface FFT3D {
	
	public void doubleArrayFFT_SetDC2Zero_3D(double[] array, double[] result, int width, int height, int depth);
	
	public void double3DArrayFFT_SetDC2Zero_3D(double[][][] array, double[][][] result, int width, int height, int depth);

}
