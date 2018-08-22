package com.github.hageldave.fftbenchmark.interfaces;

public interface PureFFT2D {

	public void setupFFT(double[] toTransformLater, int width, int height);
	
	public double transformAndGetDC();
	
}
