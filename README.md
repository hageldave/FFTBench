# FFTBench
This is a micro benchmark for FFTs in Java using the java microbenchmark harness [JMH](http://openjdk.java.net/projects/code-tools/jmh/).

So far only two implementations are tested which are the [JTransforms](https://github.com/wendykierp/JTransforms) library and my own implementation [ezFFTW](https://github.com/hageldave/ezfftw) which is build on top of FFTW.

---
## Build & Run
This is a maven project, so in order to build the benchmark use
```
mvn clean install
```
This will create two jars in the ```target``` folder, ```benchmarks.jar``` and ```fftbench-1.0.jar```.
In order to run the benchmark use
```
java -jar benchmarks.jar
```

---
## What is benchmarked?
The general task that is to be performed by the libraries is to do a full forward FFT from real to complex with all real and imaginary parts present in the result (none ommited due to symmetry), then setting the DC value to zero (i.e. the zeroth frequency component), and performing the inverse FFT from complex to real. This is done by calling a method that specifies the input array, the output array and the probem size (array length in 1D, otherwise seperately specified).
This is done for several problem sizes, in 1D, 2D and 3D, and in multiple sequential calls as well as multiple concurrent calls.


