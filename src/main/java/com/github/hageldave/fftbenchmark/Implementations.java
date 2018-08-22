package com.github.hageldave.fftbenchmark;

import com.github.hageldave.fftbenchmark.interfaces.FFT1D;
import com.github.hageldave.fftbenchmark.interfaces.FFT2D;
import com.github.hageldave.fftbenchmark.interfaces.FFT3D;

public enum Implementations {
	JTransforms(new JT_FFT(),new JT_FFT(), new JT_FFT()),
	ezFFTW(new EZ_FFT(), new EZ_FFT(), new EZ_FFT()),
	;
	
	public final FFT1D fft1d;
	public final FFT2D fft2d;
	public final FFT3D fft3d;
	
	private Implementations(FFT1D fft1d, FFT2D fft2d, FFT3D fft3d) {
		this.fft1d = fft1d;
		this.fft2d = fft2d;
		this.fft3d = fft3d;
	}	
}
