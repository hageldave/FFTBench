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

---
## My Results
In the following the results I got with my machine are shown. Please note that these results are only the observations for my machine and cannot be understood as a general truth for the libraries as these may likely perform differently on other systems.


The benchmark results feature 2 implementations, JTransforms and ezFFTW. Due to JTransforms hueristically deciding on using multithreading, a separate JTransforms implementation with disabled threading is featured for better comparability to the non-threaded ezFFTW library.

```
Benchmark                                (dims)  (implementation)  Mode  Cnt   Score    Error  Units
FFT1dBenchmark.arrayInput                  1024       JTransforms  avgt    3  ≈ 10⁻⁴            s/op
FFT1dBenchmark.arrayInput                  1024            ezFFTW  avgt    3   0.006 ±  0.053   s/op
FFT1dBenchmark.arrayInput                 16384       JTransforms  avgt    3   0.002 ±  0.001   s/op
FFT1dBenchmark.arrayInput                 16384            ezFFTW  avgt    3   0.003 ±  0.004   s/op
FFT1dBenchmark.arrayInput                 98304       JTransforms  avgt    3   0.033 ±  0.006   s/op
FFT1dBenchmark.arrayInput                 98304            ezFFTW  avgt    3   0.029 ±  0.005   s/op
FFT1dBenchmark.arrayInput                589829       JTransforms  avgt    3   1.052 ±  0.157   s/op
FFT1dBenchmark.arrayInput                589829            ezFFTW  avgt    3   0.815 ±  0.025   s/op
FFT1dBenchmark.arrayInput               1000000       JTransforms  avgt    3   0.290 ±  0.009   s/op
FFT1dBenchmark.arrayInput               1000000            ezFFTW  avgt    3   0.175 ±  0.039   s/op
FFT1dBenchmark.arrayInput              10000000       JTransforms  avgt    3   3.205 ±  0.763   s/op
FFT1dBenchmark.arrayInput              10000000            ezFFTW  avgt    3   4.560 ±  0.532   s/op
FFT1dBenchmark.parallelInvocations         1024       JTransforms  avgt    3  ≈ 10⁻³            s/op
FFT1dBenchmark.parallelInvocations         1024            ezFFTW  avgt    3   0.124 ±  0.530   s/op
FFT1dBenchmark.parallelInvocations        16384       JTransforms  avgt    3   0.012 ±  0.001   s/op
FFT1dBenchmark.parallelInvocations        16384            ezFFTW  avgt    3   0.042 ±  0.123   s/op
FFT1dBenchmark.parallelInvocations        98304       JTransforms  avgt    3   0.200 ±  0.013   s/op
FFT1dBenchmark.parallelInvocations        98304            ezFFTW  avgt    3   0.130 ±  0.013   s/op
FFT1dBenchmark.parallelInvocations       589829       JTransforms  avgt    3   8.485 ±  1.473   s/op
FFT1dBenchmark.parallelInvocations       589829            ezFFTW  avgt    3   6.298 ±  0.083   s/op
FFT1dBenchmark.parallelInvocations      1000000       JTransforms  avgt    3   2.392 ±  0.469   s/op
FFT1dBenchmark.parallelInvocations      1000000            ezFFTW  avgt    3   0.999 ±  0.114   s/op
FFT2dBenchmark.parallelInvocations     1280x720       JTransforms  avgt    3   0.407 ±  0.018   s/op
FFT2dBenchmark.parallelInvocations     1280x720       snglthrd_JT  avgt    3   0.485 ±  0.041   s/op
FFT2dBenchmark.parallelInvocations     1280x720            ezFFTW  avgt    3   0.494 ±  0.140   s/op
FFT2dBenchmark.parallelInvocations    1920x1080       JTransforms  avgt    3   0.988 ±  0.140   s/op
FFT2dBenchmark.parallelInvocations    1920x1080       snglthrd_JT  avgt    3   1.165 ±  0.037   s/op
FFT2dBenchmark.parallelInvocations    1920x1080            ezFFTW  avgt    3   1.193 ±  0.182   s/op
FFT2dBenchmark.parallelInvocations    1087x1931       JTransforms  avgt    3   5.086 ±  1.060   s/op
FFT2dBenchmark.parallelInvocations    1087x1931       snglthrd_JT  avgt    3   7.348 ±  0.771   s/op
FFT2dBenchmark.parallelInvocations    1087x1931            ezFFTW  avgt    3   3.005 ±  0.355   s/op
FFT2dBenchmark.parallelInvocations    3000x2000       JTransforms  avgt    3   3.933 ±  0.954   s/op
FFT2dBenchmark.parallelInvocations    3000x2000       snglthrd_JT  avgt    3   4.177 ±  0.392   s/op
FFT2dBenchmark.parallelInvocations    3000x2000            ezFFTW  avgt    3   4.488 ±  1.098   s/op
FFT2dBenchmark.parallelInvocations    4928x3264       JTransforms  avgt    3  14.462 ±  2.747   s/op
FFT2dBenchmark.parallelInvocations    4928x3264       snglthrd_JT  avgt    3  16.362 ±  5.528   s/op
FFT2dBenchmark.parallelInvocations    4928x3264            ezFFTW  avgt    3  16.547 ±  5.310   s/op
FFT2dBenchmark.parallelInvocations    5000x5000       JTransforms  avgt    3  18.162 ± 11.431   s/op
FFT2dBenchmark.parallelInvocations    5000x5000       snglthrd_JT  avgt    3  21.460 ±  1.884   s/op
FFT2dBenchmark.parallelInvocations    5000x5000            ezFFTW  avgt    3  27.880 ±  1.644   s/op
FFT2dBenchmark.singleArrayInput        1280x720       JTransforms  avgt    3   0.058 ±  0.006   s/op
FFT2dBenchmark.singleArrayInput        1280x720       snglthrd_JT  avgt    3   0.137 ±  0.003   s/op
FFT2dBenchmark.singleArrayInput        1280x720            ezFFTW  avgt    3   0.155 ±  0.006   s/op
FFT2dBenchmark.singleArrayInput       1920x1080       JTransforms  avgt    3   0.125 ±  0.005   s/op
FFT2dBenchmark.singleArrayInput       1920x1080       snglthrd_JT  avgt    3   0.331 ±  0.018   s/op
FFT2dBenchmark.singleArrayInput       1920x1080            ezFFTW  avgt    3   0.360 ±  0.012   s/op
FFT2dBenchmark.singleArrayInput       1087x1931       JTransforms  avgt    3   0.634 ±  0.043   s/op
FFT2dBenchmark.singleArrayInput       1087x1931       snglthrd_JT  avgt    3   2.132 ±  1.010   s/op
FFT2dBenchmark.singleArrayInput       1087x1931            ezFFTW  avgt    3   0.998 ±  0.043   s/op
FFT2dBenchmark.singleArrayInput       3000x2000       JTransforms  avgt    3   0.397 ±  0.086   s/op
FFT2dBenchmark.singleArrayInput       3000x2000       snglthrd_JT  avgt    3   1.100 ±  0.027   s/op
FFT2dBenchmark.singleArrayInput       3000x2000            ezFFTW  avgt    3   1.374 ±  0.132   s/op
FFT2dBenchmark.singleArrayInput       4928x3264       JTransforms  avgt    3   1.622 ±  0.373   s/op
FFT2dBenchmark.singleArrayInput       4928x3264       snglthrd_JT  avgt    3   4.553 ±  1.005   s/op
FFT2dBenchmark.singleArrayInput       4928x3264            ezFFTW  avgt    3   4.806 ±  0.953   s/op
FFT2dBenchmark.singleArrayInput       5000x5000       JTransforms  avgt    3   1.941 ±  5.719   s/op
FFT2dBenchmark.singleArrayInput       5000x5000       snglthrd_JT  avgt    3   6.033 ±  0.635   s/op
FFT2dBenchmark.singleArrayInput       5000x5000            ezFFTW  avgt    3   8.422 ±  0.207   s/op
FFT3dBenchmark.parallelInvocations  100x120x140       JTransforms  avgt    2   0.448            s/op
FFT3dBenchmark.parallelInvocations  100x120x140       snglthrd_JT  avgt    2   0.654            s/op
FFT3dBenchmark.parallelInvocations  100x120x140            ezFFTW  avgt    2   0.369            s/op
FFT3dBenchmark.parallelInvocations  256x256x256       JTransforms  avgt    2   3.145            s/op
FFT3dBenchmark.parallelInvocations  256x256x256       snglthrd_JT  avgt    2   4.766            s/op
FFT3dBenchmark.parallelInvocations  256x256x256            ezFFTW  avgt    2   7.370            s/op
FFT3dBenchmark.parallelInvocations  400x300x200       JTransforms  avgt    2   6.975            s/op
FFT3dBenchmark.parallelInvocations  400x300x200       snglthrd_JT  avgt    2   8.962            s/op
FFT3dBenchmark.parallelInvocations  400x300x200            ezFFTW  avgt    2   6.210            s/op
FFT3dBenchmark.singleArrayInput     100x120x140       JTransforms  avgt    2   0.106            s/op
FFT3dBenchmark.singleArrayInput     100x120x140       snglthrd_JT  avgt    2   0.311            s/op
FFT3dBenchmark.singleArrayInput     100x120x140            ezFFTW  avgt    2   0.176            s/op
FFT3dBenchmark.singleArrayInput     256x256x256       JTransforms  avgt    2   0.712            s/op
FFT3dBenchmark.singleArrayInput     256x256x256       snglthrd_JT  avgt    2   2.272            s/op
FFT3dBenchmark.singleArrayInput     256x256x256            ezFFTW  avgt    2   4.252            s/op
FFT3dBenchmark.singleArrayInput     400x300x200       JTransforms  avgt    2   1.403            s/op
FFT3dBenchmark.singleArrayInput     400x300x200       snglthrd_JT  avgt    2   4.277            s/op
FFT3dBenchmark.singleArrayInput     400x300x200            ezFFTW  avgt    2   2.955            s/op
```
See the whole console output [here](https://github.com/hageldave/FFTBench/blob/master/runs/ubuntu_16.04_phenom_II_x4_955.txt).
