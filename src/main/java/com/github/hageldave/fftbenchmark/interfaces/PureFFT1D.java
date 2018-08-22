package com.github.hageldave.fftbenchmark.interfaces;

public interface PureFFT1D {

	public void setupFFT(double[] toTransformLater);
	
	public double transformAndGetDC();
	
}
