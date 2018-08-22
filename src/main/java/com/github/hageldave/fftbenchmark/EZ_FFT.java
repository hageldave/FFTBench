package com.github.hageldave.fftbenchmark;

import java.util.ArrayList;

import com.github.hageldave.fftbenchmark.interfaces.FFT1D;
import com.github.hageldave.fftbenchmark.interfaces.FFT2D;
import com.github.hageldave.fftbenchmark.interfaces.FFT3D;

import hageldave.ezfftw.dp.FFT;
import hageldave.ezfftw.dp.RowMajorArrayAccessor;

public class EZ_FFT implements FFT1D,FFT2D,FFT3D {
	
	@Override
	public void doubleArrayFFT_SetDC2Zero_1D(double[] array, double[] result) {
		double[] complex_r = new double[array.length];
		double[] complex_i = new double[array.length];
		FFT.fft(array, complex_r, complex_i, array.length);
		complex_r[0]=0;
		FFT.ifft(complex_r, complex_i, result, array.length);
	}

	@Override
	public void doubleListFFT_SetDC2Zero_1D(ArrayList<Double> list, ArrayList<Double> result) {
		RowMajorArrayAccessor complex_r = new RowMajorArrayAccessor(list.size());
		RowMajorArrayAccessor complex_i = new RowMajorArrayAccessor(list.size());
		FFT.fft(
				(long[] indices) -> list.get((int)indices[0]), 
				complex_r.combineToComplexWriter(complex_i), 
				list.size()
		);
		complex_r.array[0]=0;
		FFT.ifft(
				complex_r.combineToComplexSampler(complex_i), 
				(double v, long[] indices)->result.add(v),
				list.size());
	}
	
	@Override
	public void doubleArrayFFT_SetDC2Zero_2D(double[] array, double[] result, int width, int height) {
		double[] complex_r = new double[array.length];
		double[] complex_i = new double[array.length];
		FFT.fft(array, complex_r, complex_i, width,height);
		complex_r[0]=0;
		FFT.ifft(complex_r, complex_i, result, width,height);
	}

	@Override
	public void double2DArrayFFT_SetDC2Zero_2D(double[][] array, double[][] result, int width, int height) {
		RowMajorArrayAccessor complex_r = new RowMajorArrayAccessor(width,height);
		RowMajorArrayAccessor complex_i = new RowMajorArrayAccessor(width,height);
		FFT.fft(
				(long[] indices)->array[(int)indices[1]][(int)indices[0]], 
				complex_r.combineToComplexWriter(complex_i), 
				width,
				height
		);
		complex_r.array[0]=0;
		FFT.ifft(
				complex_r.combineToComplexSampler(complex_i), 
				(double v, long[] indices) -> result[(int)indices[1]][(int)indices[0]]=v,
				width,
				height
		);
	}

	@Override
	public void doubleArrayFFT_SetDC2Zero_3D(double[] array, double[] result, int width, int height, int depth) {
		double[] complex_r = new double[array.length];
		double[] complex_i = new double[array.length];
		FFT.fft(array, complex_r, complex_i, width,height,depth);
		complex_r[0]=0;
		FFT.ifft(complex_r, complex_i, result, width,height,depth);
	}

	@Override
	public void double3DArrayFFT_SetDC2Zero_3D(double[][][] array, double[][][] result, int width, int height,
			int depth) {
		RowMajorArrayAccessor complex_r = new RowMajorArrayAccessor(width,height,depth);
		RowMajorArrayAccessor complex_i = new RowMajorArrayAccessor(width,height,depth);
		FFT.fft(
				(long[] indices)->array[(int)indices[2]][(int)indices[1]][(int)indices[0]], 
				complex_r.combineToComplexWriter(complex_i), 
				width,
				height,
				depth
		);
		complex_r.array[0]=0;
		FFT.ifft(
				complex_r.combineToComplexSampler(complex_i), 
				(double v, long[] indices) -> result[(int)indices[2]][(int)indices[1]][(int)indices[0]]=v,
				width,
				height,
				depth
		);
	}



}
