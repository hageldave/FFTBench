package com.github.hageldave.fftbenchmark.impls;

import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;

import com.github.hageldave.fftbenchmark.interfaces.FFT1D;
import com.github.hageldave.fftbenchmark.interfaces.FFT2D;
import com.github.hageldave.fftbenchmark.interfaces.FFT3D;

import hageldave.ezfftw.dp.FFT;
import hageldave.ezfftw.dp.NativeRealArray;
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
		double[][] complex = new double[2][width*height];
		FFT.fft(
				(/*supply*/)->nativeRealFromNested(array, width, height), 
				(NativeRealArray r, NativeRealArray i)->{r.get(0, complex[0]); i.get(0,complex[1]);},
				width,
				height
		);
		complex[0][0]=0;
		FFT.ifft(
				(/*supply*/)->new NativeRealArray(width*height).set(complex[0]),
				(/*supply*/)->new NativeRealArray(width*height).set(complex[1]),
				(NativeRealArray r)->nativeRealToNested(r, result, width, height),
				width,
				height
		);
	}

	@Override
	public void doubleArrayFFT_SetDC2Zero_3D(float[] array, float[] result, int width, int height, int depth) {
		float[] complex_r = new float[array.length];
		float[] complex_i = new float[array.length];
		hageldave.ezfftw.fp.FFT.fft(array, complex_r, complex_i, width,height,depth);
		complex_r[0]=0;
		hageldave.ezfftw.fp.FFT.ifft(complex_r, complex_i, result, width,height,depth);
	}

	@Override
	public void double3DArrayFFT_SetDC2Zero_3D(float[][][] array, float[][][] result, int width, int height,
			int depth) {
		hageldave.ezfftw.fp.RowMajorArrayAccessor complex_r = new hageldave.ezfftw.fp.RowMajorArrayAccessor(width,height,depth);
		hageldave.ezfftw.fp.RowMajorArrayAccessor complex_i = new hageldave.ezfftw.fp.RowMajorArrayAccessor(width,height,depth);
		hageldave.ezfftw.fp.FFT.fft(
				(long[] indices)->array[(int)indices[2]][(int)indices[1]][(int)indices[0]], 
				complex_r.combineToComplexWriter(complex_i), 
				width,
				height,
				depth
		);
		complex_r.array[0]=0;
		hageldave.ezfftw.fp.FFT.ifft(
				complex_r.combineToComplexSampler(complex_i), 
				(float v, long[] indices) -> result[(int)indices[2]][(int)indices[1]][(int)indices[0]]=v,
				width,
				height,
				depth
		);
	}

	private static NativeRealArray nativeRealFromNested(double[][] array, int width, int height){
		NativeRealArray natarray = new NativeRealArray(width*height);
		for(int row=0; row<height;row++)
			natarray.set(row*width, array[row]);
		return natarray;
	}
	
	private static void nativeRealToNested(NativeRealArray natArray, double[][] array, int width, int height){
		for(int row=0; row<height;row++)
			natArray.get(row*width, array[row]);
	}
	
	private static hageldave.ezfftw.fp.NativeRealArray nativeRealFromNested(float[][][] array, int width, int height, int depth){
		int stride = width*height;
		hageldave.ezfftw.fp.NativeRealArray natarray = new hageldave.ezfftw.fp.NativeRealArray(stride*depth);
		for(int slice=0; slice<depth;slice++)
			for(int row=0; row<height;row++)
				natarray.set(slice*stride+row*width, array[slice][row]);
		return natarray;
	}
	
	private static void nativeRealToNested(hageldave.ezfftw.fp.NativeRealArray natArray, float[][][] array, int width, int height, int depth){
		int stride = width*height;
		for(int slice=0; slice<depth;slice++)
			for(int row=0; row<height;row++)
				natArray.get(slice*stride+row*width, array[slice][row]);
	}


}
