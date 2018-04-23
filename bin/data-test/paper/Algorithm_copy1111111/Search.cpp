#include "stdio.h"
#include "string.h"
#include<bits/stdc++.h>
#include "Search.h"

namespace Algorithm {
namespace Search {
/**
 * LINEAR SEARCH 1 (use array)
 *
 * http://www.programmingsimplified.com/c/source-code/c-program-linear-search
 */
long linear_search1(long a[], long n, long find) {
	long c;

	for (c = 0; c < n; c++) {
		if (a[c] == find)
			return c;
	}

	return -1;
}

/**
 * LINEAR SEARCH 2 (use pointer)
 *
 * http://www.programmingsimplified.com/c/source-code/c-program-linear-search
 */
long linear_search2(long *pointer, long n, long find) {
	long c;

	for (c = 0; c < n; c++) {
		if (*(pointer + c) == find)
			return c;
	}

	return -1;
}

/**
 * BINARY SEARCH
 *
 *  C program for binary search: This code implements binary search in c language. It can only be used for sorted arrays, but it's fast as compared to linear search. If you wish to use binary search on an array which is not sorted then you must sort it using some sorting technique say merge sort and then use binary search algorithm to find the desired element in the list. If the element to be searched is found then its position is printed.
 *
 *  http://www.geeksforgeeks.org/binary-search/
 */
int binarySearch(int arr[], int l, int r, int x) {
	if (r >= l) {
		int mid = l + (r - l) / 2;

		// If the element is present at the middle itself
		if (arr[mid] == x)
			return mid;

		// If element is smaller than mid, then it can only be present
		// in left subarray
		if (arr[mid] > x)
			return binarySearch(arr, l, mid - 1, x);

		// Else the element can only be present in right subarray
		return binarySearch(arr, mid + 1, r, x);
	}

	// We reach here when element is not present in array
	return -1;
}

/**
 * Naive Pattern Searching algorithm
 *
 * http://www.geeksforgeeks.org/searching-for-patterns-set-1-naive-pattern-searching/
 */
void NaivePatternSearch(char *pat, char *txt) {
	int M = strlen(pat);
	int N = strlen(txt);

	/* A loop to slide pat[] one by one */
	for (int i = 0; i <= N - M; i++) {
		int j;

		/* For current index i, check for pattern match */
		for (j = 0; j < M; j++)
			if (txt[i + j] != pat[j])
				break;

		if (j == M)  // if pat[0...M-1] = txt[i, i+1, ...i+M-1]
			printf("Pattern found at index %d n", i);
	}
}

/**
 * SEARCHING FOR PATTERNS | SET 2 (KMP ALGORITHM)
 *
 * http://www.geeksforgeeks.org/searching-for-patterns-set-2-kmp-algorithm/
 */
// Fills lps[] for given patttern pat[0..M-1]
void computeLPSArray(char *pat, int M, int *lps) {
	// length of the previous longest prefix suffix
	int len = 0;

	lps[0] = 0;  // lps[0] is always 0

	// the loop calculates lps[i] for i = 1 to M-1
	int i = 1;
	while (i < M) {
		if (pat[i] == pat[len]) {
			len++;
			lps[i] = len;
			i++;
		} else { // (pat[i] != pat[len])
			// This is tricky. Consider the example.
			// AAACAAAA and i = 7. The idea is similar
			// to search step.
			if (len != 0) {
				len = lps[len - 1];

				// Also, note that we do not increment
				// i here
			} else { // if (len == 0)
				lps[i] = 0;
				i++;
			}
		}
	}
}

// Prints occurrences of txt[] in pat[]
void KMPSearch(char *pat, char *txt) {
	int M = strlen(pat);
	int N = strlen(txt);

	// create lps[] that will hold the longest prefix suffix
	// values for pattern
	int lps[M];

	// Preprocess the pattern (calculate lps[] array)
	computeLPSArray(pat, M, lps);

	int i = 0;		// index for txt[]
	int j = 0;		// index for pat[]
	while (i < N) {
		if (pat[j] == txt[i]) {
			j++;
			i++;
		}

		if (j == M) {
			printf("Found pattern at index %d n", i - j);
			j = lps[j - 1];
		}

		// mismatch after j matches
		else if (i < N && pat[j] != txt[i]) {
			// Do not match lps[0..lps[j-1]] characters,
			// they will match anyway
			if (j != 0)
				j = lps[j - 1];
			else
				i = i + 1;
		}
	}
}
}
}








