package com.github.hageldave.fftbenchmark.interfaces;

import java.util.ArrayList;

public interface FFT1D {
	
	public void doubleArrayFFT_SetDC2Zero_1D(double[] array, double[] result);
	
	public void doubleListFFT_SetDC2Zero_1D(ArrayList<Double> list, ArrayList<Double> result);

}
