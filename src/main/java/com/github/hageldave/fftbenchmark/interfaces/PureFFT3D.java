package com.github.hageldave.fftbenchmark.interfaces;

public interface PureFFT3D {

	public void setupFFT(double[] toTransformLater, int width, int height, int depth);
	
	public double transformAndGetDC();
	
}
