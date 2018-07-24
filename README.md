# CFT4Cpp-core (no graphical user iterface)
Tool for test data generation for C/C++ projects

## Developement environment
* IDE: Eclipse Java EE IDE for Web Developers (Oxygen.3a Release)
* Java 8
* Windows 10

## Set up
- Import CFT4cpp-core to Eclipse.
- Add libraries stored in /lib/
- Install IDE Dev-cpp (https://sourceforge.net/projects/orwelldevcpp/). We use the compiler of Dev-Cpp.

## Run the experiment
# Step 1. Configure: Go to /local/setting.properties

(i) Configure the compiler of Dev-cpp
+ GNU_GCC_PATH: the absolute path of gcc.exe
+ GNU_GPlusPlus_PATH: the absolute path of g++.exe
+ GNU_GENERAL: the absolute path of folder *bin*
+ GNU_MAKE_PATH: the absolute path of mingw32-make.exe

(ii) Set up SMT-Solver Z3
+ Z3_SOLVER_PATH: the absolute path of z3.exe

(iii) Set up mcpp to remove Preprocessor directives
+ MCPP_EXE_PATH: the absolute path of mcpp.exe

(iv) Set up the range of parameters
+ DEFAULT_CHARACTER_LOWER_BOUND: the lower bound of character variable
+ DEFAULT_CHARACTER_UPPER_BOUND: the upper bound of character variable
+ DEFAULT_NUMBER_LOWER_BOUND: the lower bound of number variable
+ DEFAULT_NUMBER_UPPER_BOUND: the upper bound of number variable
+ TEST_ARRAY_SIZE: the maximum size of array (used when an array parameter does not specify its size)

(v) The path of test report
+ TEST_REPORT

# Step 2. Select 
- After configuration, go to /src/console.java
- In the main() function, set up the path of (i) the testing project, (ii) the path of file containing the testing functions, and (iii) the path of the configuration file (search "AUTHOR: PLEASE CONFIGURE HERE")
- Run! Enjoy!

## Note
# List of functions in the experiment

+ test.txt (the path of file containing the testing functions)
"\bubbleSort2(int[],int)

\insertionSort(int[],int)

\quickSort(int[],int,int)

\quickSortIterative(int[],int,int)

\merge(int[],int,int,int)

\mergeSort(int[],int,int)

\Tritype(int,int,int)

\uninit_var(int[3],int[3])

\ArrayCmp(int,unsigned char*,unsigned char*)

\Fibonacci(int)

\find_maximum(int[],int)

\add_digits(int)

\reverse(long)

\check_prime(int)

\check_armstrong(long long)

\power(int,int)

\print_floyd(int)

\find_minimum(int[],int)

\concatenate(char[],char[])

\concatenate_string(char*,char*)

\reverse2(char*,int,int)

\copy_string(char*,char*)

\string_length(char*)

\compare_string(char*,char*)

\check_vowel(char)

\check_subsequence(char[],char[])

\find_frequency(char[],int[])

\reverse_array(int*,int)

\string_length1(char*)

\check_anagram(char[],char[])

\printClosest(int[],int,int)

\largestNum(int,int)

\frequencyDigits(int,int)

\lds(int[],int)

\getPriority(char)

\printDistinctPFs(int)

\steps(int,int)

\minimumNumberOfDigits(int,int)

\printSmallestNumber(int,int)

\binomialCoeff(int[],int)

\sumOfproduct(int)

\mergeTwoArray(int[],int,int[],int,int[])

\linear_search1(long[],long,long)

\linear_search2(long*,long,long)

\binarySearch(int[],int,int,int)

\NaivePatternSearch(char*,char*)

\computeLPSArray(char*,int,int*)

\KMPSearch(char*,char*)

\selectionSort(int[],int)

\bubbleSort1(int[],int)"

## Developers
Rd320 room, E3 building, 144 Xuan Thuy street, Ha Noi
