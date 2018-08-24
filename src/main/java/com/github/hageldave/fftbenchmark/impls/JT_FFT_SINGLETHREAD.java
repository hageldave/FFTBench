package com.github.hageldave.fftbenchmark.impls;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.DoubleFFT_2D;
import org.jtransforms.fft.DoubleFFT_3D;
import org.jtransforms.fft.FloatFFT_1D;
import org.jtransforms.fft.FloatFFT_3D;

import com.github.hageldave.fftbenchmark.interfaces.FFT1D;
import com.github.hageldave.fftbenchmark.interfaces.FFT2D;
import com.github.hageldave.fftbenchmark.interfaces.FFT3D;

public class JT_FFT_SINGLETHREAD implements FFT1D,FFT2D,FFT3D {

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
	public void doubleListFFT_SetDC2Zero_1D(ArrayList<Double> list, ArrayList<Double> result) {
		DoubleFFT_1D fft = new DoubleFFT_1D(list.size());
		double[] transform = new double[list.size()*2];
		int i=0;
		for(Double v:list)
			transform[i++] = v;
		fft.realForwardFull(transform);
		transform[0]=0;
		fft.complexInverse(transform, false);
		for(i=0;i<list.size();i++)
			result.add(transform[i]);
	}

	@Override
	public void doubleArrayFFT_SetDC2Zero_2D(double[] array, double[] result, int width, int height) {
		DoubleFFT_2D fft = new DoubleFFT_2D(height,width);
		disableThreading(fft);
		double[] transform = Arrays.copyOf(array, width*height*2);
		fft.realForwardFull(transform);
		transform[0]=0;
		fft.complexInverse(transform, false);
		System.arraycopy(transform, 0, result, 0, width*height);
	}

	@Override
	public void double2DArrayFFT_SetDC2Zero_2D(double[][] array, double[][] result, int width, int height) {
		DoubleFFT_2D fft = new DoubleFFT_2D(height,width);
		disableThreading(fft);
		double[] transform = new double[width*height*2];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				transform[i*width+j] = array[i][j];
		fft.realForwardFull(transform);
		transform[0]=0;
		fft.complexInverse(transform, false);
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				result[i][j] = transform[i*width+j];
	}
	
	@Override
	public void doubleArrayFFT_SetDC2Zero_3D(float[] array, float[] result, int width, int height, int depth) {
		FloatFFT_3D fft = new FloatFFT_3D(depth, height, width);
		disableThreading(fft);
		float[] transform = Arrays.copyOf(array, width*height*depth*2);
		fft.realForwardFull(transform);
		transform[0]=0;
		fft.complexInverse(transform, false);
		System.arraycopy(transform, 0, result, 0, width*height*depth);
	}

	@Override
	public void double3DArrayFFT_SetDC2Zero_3D(float[][][] array, float[][][] result, int width, int height,
			int depth) {
		FloatFFT_3D fft = new FloatFFT_3D(depth, height, width);
		disableThreading(fft);
		float[] transform = new float[width*height*depth*2];
		int sliceStride = width*height;
		for(int k = 0; k < depth; k++)
			for(int i = 0; i < height; i++)
				for(int j = 0; j < width; j++)
					transform[k*sliceStride+i*width+j] = array[k][i][j];
		fft.realForwardFull(transform);
		transform[0]=0;
		fft.complexInverse(transform, false);
		for(int k = 0; k < depth; k++)
			for(int i = 0; i < height; i++)
				for(int j = 0; j < width; j++)
					 result[k][i][j] = transform[k*sliceStride+i*width+j];
	}
	
	public static void disableThreading(Object obj){
		try {
			Field field = obj.getClass().getDeclaredField("useThreads");
			field.setAccessible(true);
			field.setBoolean(obj, false);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
