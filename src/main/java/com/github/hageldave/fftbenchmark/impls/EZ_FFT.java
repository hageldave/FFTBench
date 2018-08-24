package com.github.hageldave.fftbenchmark.impls;

import java.util.ArrayList;

import com.github.hageldave.fftbenchmark.interfaces.FFT1D;
import com.github.hageldave.fftbenchmark.interfaces.FFT2D;
import com.github.hageldave.fftbenchmark.interfaces.FFT3D;

import hageldave.ezfftw.dp.FFT;
import hageldave.ezfftw.dp.FFTW_Guru;
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
		try(
			NativeRealArray a1 = new NativeRealArray(width*height);
			NativeRealArray a2 = new NativeRealArray(width*height);
		){
			a1.set(0, array);
			FFTW_Guru.execute_split_r2c(a1, a1, a2, width,height);
			a1.set(0, 0);
			FFTW_Guru.execute_split_c2r(a1, a2, a1, width,height);
			a1.get(0, result);
		};
	}

	@Override
	public void double2DArrayFFT_SetDC2Zero_2D(double[][] array, double[][] result, int width, int height) {
		try(
			NativeRealArray a1 = new NativeRealArray(width*height);
			NativeRealArray a2 = new NativeRealArray(width*height);
		){
			for(int i=0;i<height;i++)
				a1.set(i*width, array[i]);
			FFTW_Guru.execute_split_r2c(a1, a1, a2, width,height);
			a1.set(0, 0);
			FFTW_Guru.execute_split_c2r(a1, a2, a1, width,height);
			for(int i=0;i<height;i++)
				a1.get(i*width, result[i]);
		}
	}

	@Override
	public void doubleArrayFFT_SetDC2Zero_3D(float[] array, float[] result, int width, int height, int depth) {
		try(
			hageldave.ezfftw.fp.NativeRealArray a1 = new hageldave.ezfftw.fp.NativeRealArray(width*height*depth);
			hageldave.ezfftw.fp.NativeRealArray a2 = new hageldave.ezfftw.fp.NativeRealArray(width*height*depth);
		){
			a1.set(0, array);
			hageldave.ezfftw.fp.FFTW_Guru.execute_split_r2c(a1, a1, a2, width,height,depth);
			a1.set(0, 0);
			hageldave.ezfftw.fp.FFTW_Guru.execute_split_c2r(a1, a2, a1, width,height,depth);
			a1.get(0, result);
		};
	}

	@Override
	public void double3DArrayFFT_SetDC2Zero_3D(float[][][] array, float[][][] result, int width, int height,
			int depth) {
		try(
				hageldave.ezfftw.fp.NativeRealArray a1 = new hageldave.ezfftw.fp.NativeRealArray(width*height*depth);
				hageldave.ezfftw.fp.NativeRealArray a2 = new hageldave.ezfftw.fp.NativeRealArray(width*height*depth);
		){
			int stride = width*height;
			for(int j=0;j<depth;j++)
				for(int i=0;i<height;i++)
					a1.set(j*stride+i*width, array[j][i]);
			hageldave.ezfftw.fp.FFTW_Guru.execute_split_r2c(a1, a1, a2, width,height,depth);
			a1.set(0, 0);
			hageldave.ezfftw.fp.FFTW_Guru.execute_split_c2r(a1, a2, a1, width,height,depth);
			for(int j=0;j<depth;j++)
				for(int i=0;i<height;i++)
					a1.get(j*stride+i*width, result[j][i]);
		}
	}


}
