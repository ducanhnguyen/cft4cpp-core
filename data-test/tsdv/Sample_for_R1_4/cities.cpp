# include <cmath>
# include <cstdlib>
# include <cstring>
# include <ctime>
# include <fstream>
# include <iomanip>
# include <iostream>

using namespace std;

# include "cities.hpp"

//****************************************************************************80

char ch_cap ( char ch )

//****************************************************************************80
//
//  Purpose:
//
//    CH_CAP capitalizes a single character.
//
//  Discussion:
//
//    This routine should be equivalent to the library "toupper" function.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    19 July 1998
//
//  Author:
//
//    John Burkardt
//
//  Parameters:
//
//    Input, char CH, the character to capitalize.
//
//    Output, char CH_CAP, the capitalized character.
//
{
  if ( 97 <= ch && ch <= 122 ) 
  {
    ch = ch - 32;
  }   

  return ch;
}
//****************************************************************************80

bool ch_eqi ( char ch1, char ch2 )

//****************************************************************************80
//
//  Purpose:
//
//    CH_EQI is true if two characters are equal, disregarding case.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    13 September 2009
//
//  Author:
//
//    John Burkardt
//
//  Parameters:
//
//    Input, char CH1, CH2, the characters to compare.
//
//    Output, bool CH_EQI, is true if the two characters are equal,
//    disregarding case.
//
{
  bool value;

  if ( 97 <= ch1 && ch1 <= 122 ) 
  {
    ch1 = ch1 - 32;
  } 
  if ( 97 <= ch2 && ch2 <= 122 ) 
  {
    ch2 = ch2 - 32;
  }     

  value = ( ch1 == ch2 );

  return value;
}
//****************************************************************************80

int ch_to_digit ( char ch )

//****************************************************************************80
//
//  Purpose:
//
//    CH_TO_DIGIT returns the integer value of a base 10 digit.
//
//  Example:
//
//     CH  DIGIT
//    ---  -----
//    '0'    0
//    '1'    1
//    ...  ...
//    '9'    9
//    ' '    0
//    'X'   -1
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    13 June 2003
//
//  Author:
//
//    John Burkardt
//
//  Parameters:
//
//    Input, char CH, the decimal digit, '0' through '9' or blank are legal.
//
//    Output, int CH_TO_DIGIT, the corresponding integer value.  If the 
//    character was 'illegal', then DIGIT is -1.
//
{
  int digit;

  if ( '0' <= ch && ch <= '9' )
  {
    digit = ch - '0';
  }
  else if ( ch == ' ' )
  {
    digit = 0;
  }
  else
  {
    digit = -1;
  }

  return digit;
}
/******************************************************************************/

double degrees_to_radians ( double degrees )

/******************************************************************************/
/*
  Purpose:

    DEGREES_TO_RADIANS converts an angle from degrees to radians.

  Licensing:

    This code is distributed under the GNU LGPL license. 

  Modified:

    19 May 2016

  Author:

    John Burkardt

  Parameters:

    Input, double DEGREES, an angle in degrees.

    Output, double DEGREES_TO_RADIANS, the equivalent angle
    in radians.
*/
{
  const double r8_pi = 3.141592653589793;
  double radians;

  radians = ( degrees / 180.0 ) * r8_pi;

  return radians;
}

double dms_to_radians ( int dms[] )

//****************************************************************************80
//
//  Purpose:
//
//    DMS_TO_RADIANS converts degrees, minutes, seconds to radians.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    17 October 2010
//
//  Author:
//
//    John Burkardt
//
//  Parameters:
//
//    Input, int DMS[4], the measurement of an angle in
//    degrees, minutes and seconds.  The fourth
//    argument is +1/-1 for North/South latitude or East/West longitude.
//
//    Output, double DMS_TO_RADIANS, the measurement of the same
//    angle in radians.
//
{
  double d;
  double pi = 3.141592653589793;
  double r;
  int s;

  s = i4_sign ( dms[3] ) * ( dms[2] + 60 * dms[1] + 3600 * dms[0] );
  d = ( double ) ( s ) / 3600.0;
  r = pi * d / 180.0;

  return r;
}
int i4_sign ( int i )

//****************************************************************************80
//
//  Purpose:
//
//    I4_SIGN returns the sign of an I4.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    27 March 2004
//
//  Author:
//
//    John Burkardt
//
//  Parameters:
//
//    Input, int I, the integer whose sign is desired.
//
//    Output, int I4_SIGN, the sign of I.
{
  int value;

  if ( i < 0 ) 
  {
    value = -1;
  }
  else
  {
    value = 1;
  }
  return value;
}

char lat_char ( int i )

//****************************************************************************80
//
//  Purpose:
//
//    LAT_CHAR returns a character for negative or positive latitude.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    16 October 2010
//
//  Author:
//
//    John Burkardt
//
//  Parameters:
//
//    Input, int I, is negative for negative latitude, 
//    and positive for positive latitude.
//
//    Output, char LAT_CHAR, is 'S' for negative latitude, and
//    'N' for positive latitude.
//
{
  char value;

  if ( i < 0 )
  {
    value = 'S';
  }
  else if ( 0 < i )
  {
    value = 'N';
  }
  else
  {
    value = '?';
  }
  return value;
}
double r8_abs ( double x )

//****************************************************************************80
//
//  Purpose:
//
//    R8_ABS returns the absolute value of an R8.
//
//  Licensing:
//
//    This code is distributed under the GNU LGPL license. 
//
//  Modified:
//
//    14 November 2006
//
//  Author:
//
//    John Burkardt
//
//  Parameters:
//
//    Input, double X, the quantity whose absolute value is desired.
//
//    Output, double R8_ABS, the absolute value of X.
//
{
  double value;

  if ( 0.0 <= x )
  {
    value = + x;
  } 
  else
  {
    value = - x;
  }
  return value;
}

