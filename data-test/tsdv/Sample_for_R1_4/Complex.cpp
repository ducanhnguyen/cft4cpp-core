#include <string.h>

int ComplexTest2(int x, int y)
{
	bool greater = x > y;
	int max;

	if (greater) {
		max = x;
	} else {
		max = y;
	}

	bool less = !greater;
	less = less && x > 0;

	if (max > 0 || less) {
		return 1;
	} else {
		return 0;
	}
}

void toUpperCase(char* str, int len)
{
	for (int i = 0; i < len; i++) {
		if ('a' <= str[i] && str[i] <= 'z') {
			str[i] -= 32;
		}
	}
}

void toUpperCase2(char* str)
{
	int len = strlen(str);

	for (int i = 0; i < len; i++) {
		if ('a' <= str[i] && str[i] <= 'z') {
			str[i] -= 32;
		}
	}
}

int MultipleScopeTest(int x, int y)
{
	int value = x;

	if (value > 0) {
		return 1;
	}

	{
		int value = y;

		if (value > 0) {
			return -1;
		}
	}

	return 0;
}

int gcd(int a, int b)
{
	int temp;

	while (b > 0) {
		temp = a % b;
		a = b;
		b = temp;
	}

	return a;
}


/******************************************************************************
 * Function: RecursiveSearch
 * Purpose:  Uses recursion to search array for the desired value.
 * Params:   key - value to look for, array - array to search for the value,
 *         	 low - staring array index, high - last array index
 * Returns:	 Position in the array where the value was found or -1 if no match
 *				 was found.
 *****************************************************************************/
int RecursiveSearch(int key, int array[], int low, int high)
{
	int lo = low;                             // Stores current lowest index
	int hi = high;                            // Stores current highest index
	int mid = (low + high) / 2;               // Stores the median of low & high

	if (!(lo <= hi)) {                        // If lo is not <= to high
		return -1;    // Returns negative value
	}

	if (key == array[mid]) {                  // If there is a match
		return mid;    // Returns the position
	}

	if (key < array[mid]) {
		return RecursiveSearch(key, array, low, mid - 1);    // Highest index is mid - 1
	}

	else {                                 		  // if key > array[mid]
		return RecursiveSearch(key, array, mid + 1, high);    // Lowest index is mid + 1
	}
}
/******************************************************************************
 * Function: BinarySearch
 * Purpose:  Uses loop to search array for the desired value.
 * Params:   key - value to look for, array - array to search for the value,
 *				 size of the array (last array index).
 * Returns   Position in the array where the value was found or -1 if no match
 *  			 was found.
 *****************************************************************************/
int BinarySearch(int key, int array[], int size)
{
	int lo = 0;
	int hi = size;
	int mid;

	while (lo <= hi) {
		mid = (lo + hi) / 2;

		if (key == array[mid]) {
			return mid;
		}

		if (key < array[mid]) {
			hi = mid - 1;
		}

		else {
			lo = mid + 1;
		}
	}

	return -1;
}

struct BigThree {
	int high, medium, low;
};

BigThree getBigThree(int value1, int value2, int value3)
{
	int high_value = 0, low_value = 0, middle_value = 0;

// Check For Higher Number
	if (value1 >= value2 && value1 >= value3) {
		high_value = value1;
	} else if (value2 >= value1 && value2 >= value3) {
		high_value = value2;
	} else if (value3 >= value1 && value3 >= value2) {
		high_value = value3;
	}

// Check For Low Number
	if (value1 <= value2 && value1 <= value3) {
		low_value = value1;
	} else if (value2 <= value1 && value2 <= value3) {
		low_value = value2;
	} else if (value3 <= value1 && value3 <= value2) {
		low_value = value3;
	}

// Check For Middle Value

	if (value1 <= value2 && value1 >= value3) {
		middle_value = value1;
	} else if (value2 <= value1 && value2 >= value3)

	{
		middle_value = value2;
	} else if (value3 <= value1 && value3 >= value2) {
		middle_value = value3;
	}
// Code for check all the possible arrangement of values
// Check for 1 value
	else if (value1 >= value2 && value1 <= value3) {
		middle_value = value1;
	}
// Check for 2 value
	else if (value2 >= value1 && value2 <= value3) {
		middle_value = value2;
	}
// Check for 3 value
	else if (value3 >= value1 && value3 <= value2) {
		middle_value = value3;
	}

	BigThree res;
	res.high = high_value;
	res.medium = middle_value;
	res.low = low_value;
	return res;
}

double r8_uniform_01 ( int *seed )

//****************************************************************************80
//
//  Purpose:
//
//    R8_UNIFORM_01 is a unit pseudorandom R8.
//
//  Discussion:
//
//    This routine implements the recursion
//
//      seed = 16807 * seed mod ( 2**31 - 1 )
//      unif = seed / ( 2**31 - 1 )
//
//    The integer arithmetic never requires more than 32 bits,
//    including a sign bit.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    11 August 2004
//
//  Reference:
//
//    Paul Bratley, Bennett Fox, Linus Schrage,
//    A Guide to Simulation,
//    Springer Verlag, pages 201-202, 1983.
//
//    Bennett Fox,
//    Algorithm 647:
//    Implementation and Relative Efficiency of Quasirandom
//    Sequence Generators,
//    ACM Transactions on Mathematical Software,
//    Volume 12, Number 4, pages 362-376, 1986.
//
//  Parameters:
//
//    Input/output, int *SEED, a seed for the random number generator.
//
//    Output, double R8_UNIFORM_01, a new pseudorandom variate, strictly between
//    0 and 1.
//
{
  int k;
  double r;

  k = *seed / 127773;

  *seed = 16807 * ( *seed - k * 127773 ) - k * 2836;

  if ( *seed < 0 )
  {
    *seed = *seed + 2147483647;
  }

  r = ( double ) ( *seed ) * 4.656612875E-10;

  return r;
}

void revers ( int ivec[], int kdim )

//****************************************************************************80
//
//  Purpose:
//
//    REVERS reorders the subscript vector, if required.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    27 July 2008
//
//  Author:
//
//    Original FORTRAN77 version by M O'Flaherty, G MacKenzie.
//    C++ version by John Burkardt.
//
//  Reference:
//
//    M O'Flaherty, G MacKenzie,
//    Algorithm AS 172:
//    Direct Simulation of Nested Fortran DO-LOOPS,
//    Applied Statistics,
//    Volume 31, Number 1, 1982, pages 71-74.
//
//  Parameters:
//
//    Input/output, int IVEC[KDIM], the subscript vector.
//
//    Input, int KDIM, the dimension of the subscript vector.
//
{
  int i;
  int itemp;

  for ( i = 0; i < kdim / 2; i++ )
  {
    itemp          = ivec[i];
    ivec[i]        = ivec[kdim-1-i];
    ivec[kdim-1-i] = itemp;
  }

  return;
}

float r4_uni ( int *s1, int *s2 )

//*****************************************************************************80
//
//  Purpose:
//
//    R4_UNI returns a pseudorandom number between 0 and 1.
//
//  Discussion:
//
//    This function generates uniformly distributed pseudorandom numbers
//    between 0 and 1, using the 32-bit generator from figure 3 of
//    the article by L'Ecuyer.
//
//    The cycle length is claimed to be 2.30584E+18.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    08 July 2008
//
//  Author:
//
//    Original PASCAL version by Pierre L'Ecuyer.
//    C++ version by John Burkardt.
//
//  Reference:
//
//    Pierre LEcuyer,
//    Efficient and Portable Combined Random Number Generators,
//    Communications of the ACM,
//    Volume 31, Number 6, June 1988, pages 742-751.
//
//  Parameters:
//
//    Input/output, int *S1, *S2, two values used as the
//    seed for the sequence.  On first call, the user should initialize
//    S1 to a value between 1 and 2147483562;  S2 should be initialized
//    to a value between 1 and 2147483398.
//
//    Output, float R4_UNI, the next value in the sequence.
//
{
  int k;
  float value;
  int z;

  k = *s1 / 53668;
  *s1 = 40014 * ( *s1 - k * 53668 ) - k * 12211;
  if ( *s1 < 0 )
  {
    *s1 = *s1 + 2147483563;
  }

  k = *s2 / 52774;
  *s2 = 40692 * ( *s2 - k * 52774 ) - k * 3791;
  if ( *s2 < 0 )
  {
    *s2 = *s2 + 2147483399;
  }

  z = *s1 - *s2;
  if ( z < 1 )
  {
    z = z + 2147483562;
  }

  value = ( float ) ( z ) / 2147483563.0;

  return value;
}

float r4poly_value ( int n, float a[], float x )

//****************************************************************************80
//
//  Purpose:
//
//    R4POLY_VALUE evaluates a real polynomial.
//
//  Discussion:
//
//    For sanity's sake, the value of N indicates the NUMBER of 
//    coefficients, or more precisely, the ORDER of the polynomial,
//    rather than the DEGREE of the polynomial.  The two quantities
//    differ by 1, but cause a great deal of confusion.
//
//    Given N and A, the form of the polynomial is:
//
//      p(x) = a[0] + a[1] * x + ... + a[n-2] * x^(n-2) + a[n-1] * x^(n-1)
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    13 August 2004
//
//  Author:
//
//    John Burkardt
//
//  Parameters:
//
//    Input, int N, the order of the polynomial.
//
//    Input, float A[N], the coefficients of the polynomial.
//    A[0] is the constant term.
//
//    Input, float X, the point at which the polynomial is to be evaluated.
//
//    Output, float R4POLY_VALUE, the value of the polynomial at X.
//
{
  int i;
  float value;

  value = 0.0;

  for ( i = n-1; 0 <= i; i-- )
  {
    value = value * x + a[i];
  }

  return value;
}


double r8vec_dot_product ( int n, double a1[], double a2[] )

//****************************************************************************80
//
//  Purpose:
//
//    R8VEC_DOT_PRODUCT computes the dot product of a pair of R8VEC's.
//
//  Discussion:
//
//    An R8VEC is a vector of R8's.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license.
//
//  Modified:
//
//    03 July 2005
//
//  Author:
//
//    John Burkardt
//
//  Parameters:
//
//    Input, int N, the number of entries in the vectors.
//
//    Input, double A1[N], A2[N], the two vectors to be considered.
//
//    Output, double R8VEC_DOT_PRODUCT, the dot product of the vectors.
//
{
  int i;
  double value;

  value = 0.0;
  for ( i = 0; i < n; i++ )
  {
    value = value + a1[i] * a2[i];
  }
  return value;
}

double trigamma ( double x, int *ifault )

//****************************************************************************80
//
//  Purpose:
//
//    TRIGAMMA calculates trigamma(x) = d**2 log(gamma(x)) / dx**2
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    19 January 2008
//
//  Author:
//
//    Original FORTRAN77 version by BE Schneider.
//    C++ version by John Burkardt.
//
//  Reference:
//
//    BE Schneider,
//    Algorithm AS 121:
//    Trigamma Function,
//    Applied Statistics,
//    Volume 27, Number 1, pages 97-99, 1978.
//
//  Parameters:
//
//    Input, double X, the argument of the trigamma function.
//    0 < X.
//
//    Output, int *IFAULT, error flag.
//    0, no error.
//    1, X <= 0.
//
//    Output, double TRIGAMMA, the value of the trigamma function at X.
//
{
  double a = 0.0001;
  double b = 5.0;
  double b2 =  0.1666666667;
  double b4 = -0.03333333333;
  double b6 =  0.02380952381;
  double b8 = -0.03333333333;
  double value;
  double y;
  double z;
//
//  Check the input.
//
  if ( x <= 0.0 )
  {
    *ifault = 1;
    value = 0.0;
    return value;
  }

  *ifault = 0;
  z = x;
//
//  Use small value approximation if X <= A.
//
  if ( x <= a )
  {
    value = 1.0 / x / x;
    return value;
  }
//
//  Increase argument to ( X + I ) >= B.
//
  value = 0.0;

  while ( z < b )
  {
    value = value + 1.0 / z / z;
    z = z + 1.0;
  }
//
//  Apply asymptotic formula if argument is B or greater.
//
  y = 1.0 / z / z;

  value = value + 0.5 *
      y + ( 1.0
    + y * ( b2
    + y * ( b4
    + y * ( b6
    + y *   b8 )))) / z;

  return value;
}

