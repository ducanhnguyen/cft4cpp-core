#include "stdio.h"
#include <stdlib.h>
//namespace Algorithm {
	//namespace Utils {
		/*
		Should return the type of the triangle which has sides of these lengths.
			3 = not a triangle
			2 = 	l triangle
			1 = isoceles triangle
			0 = other triangle

		Link: pathcrawler-online.com
		*/
		int Tritype(int i, int j, int k);


		/*
		Link: pathcrawler-online.com
		*/
		int uninit_var(int a[3], int b[3]);


		/*
		Compares (with respect to the lexicographic order) the subarrays with the first n elements of given arrays t1 and t2,
		Returns  0 if the subarrays are equal,
		1 if the subarray in t1 is greater than in t2,
		-1 if the subarray in t2 is greater than in t1

		Link: pathcrawler-online.com
		*/
		int ArrayCmp(int n, unsigned char* t1, unsigned char* t2);

		/*
		The recursive function
		Link: http://www.programmingsimplified.com
		*/
		int Fibonacci(int n);

		/*
		Find maximum element in array
		Link: http://www.programmingsimplified.com/c/source-code/c-program-find-maximum-element-in-array
		*/
		int find_maximum(int a[], int n);

		/*
		Add digits of number in c
		Link: http://www.programmingsimplified.com/c/program/c-program-add-number-digits
		*/
		int add_digits(int n);

		/*
		Reverse number using recursion
		Link: http://www.programmingsimplified.com/c/source-code/c-program-reverse-number
		*/
		long reverse(long n);

		/*
		Print diamond using recursion
		Link: http://www.programmingsimplified.com/c/source-code/c-program-print-diamond-pattern
		*/
		void print (int r);

		/*
		C program for prime number
		Link: http://www.programmingsimplified.com/c/source-code/c-program-for-prime-number
		*/
		int check_prime(int a);

		/*
		Check Armstrong number using function
		Link: http://www.programmingsimplified.com/c-program-find-armstrong-number
		*/
		int check_armstrong(long long);
		long long power(int, int);

		/*
		Print Floyd's triangle using recursion
		Link: http://www.programmingsimplified.com/c-program-print-floyd-triangle
		*/
		void print_floyd(int n);

		/*
		Find minimum element in array
		Link: http://www.programmingsimplified.com/c/source-code/c-program-find-minimum-element-in-array
		*/
		int find_minimum(int a[], int n);

		/*
		Reverse an array
		Link: http://www.programmingsimplified.com/c-program-reverse-array
		*/
		void reverse_array(int *pointer, int n);


		/*
		Concatenate strings without strcat function
		Link: http://www.programmingsimplified.com/c-program-concatenate-strings
		*/
		void concatenate(char p[], char q[]);

		/*
		Concatenate strings using pointers without strcat function
		Link: http://www.programmingsimplified.com/c-program-concatenate-strings
		*/
		void concatenate_string(char *original, char *add);
		/*
		Reverse a string using pointers
		Link: http://www.programmingsimplified.com/c-program-reverse-string
		*/
		int string_length1(char *pointer);

		/*
		Reverse a string using recursion
		Link: http://www.programmingsimplified.com/c-program-reverse-string
		*/
		void reverse2(char *x, int begin, int end);

		/*
		Link: http://www.programmingsimplified.com/c-program-find-palindrome
		*/
		int string_length(char *string);

		/*
		Link: http://www.programmingsimplified.com/c-program-find-palindrome
		*/
		void copy_string(char *target, char *source);

		/*
		Link: http://www.programmingsimplified.com/c-program-find-palindrome
		*/
		int compare_string(char *first, char *second);

		/*
		Delete vowels from a string
		Link: http://www.programmingsimplified.com/c/source-code/c-program-remove-vowels-from-string
		*/
		int check_vowel(char a);

		/*
		Check subsequence
		Link: http://www.programmingsimplified.com/c/source-code/c-program-check-subsequence
		*/
		int check_subsequence (char a[], char b[]);

		/*
		Find frequency of characters in a string
		Link: http://www.programmingsimplified.com/c-program-find-characters-frequency
		*/
		void find_frequency(char s[], int count[]) ;

		/*
		Check whether two strings are anagrams or not,
		Link: http://www.programmingsimplified.com/c/source-code/c-anagram-programcheck_subsequence
		*/
		int check_anagram(char a[], char b[]);
//	}
//}
