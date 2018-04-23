
#ifndef _SAMPLE_R1_DLL_H
#define _SAMPLE_R1_DLL_H

// Define R1_API switch
#ifdef BUILDING_DLL
#  define R1_API __declspec(dllexport)
#else
#  define R1_API __declspec(dllimport)
#endif

// STL
#include <exception>

/**
 * Find minimum value of the array
 * @throws std::exception n <= 0
 */
R1_API int findMin(int a[], int n);

#endif //_SAMPLE_R1_DLL_H
