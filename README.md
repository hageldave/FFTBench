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
