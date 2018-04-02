#ifndef HEADER_SEARCH_H_
#define HEADER_SEARCH_H_

namespace Algorithm {
namespace Search {
long linear_search1(long a[], long n, long find);

long linear_search2(long *pointer, long n, long find);

int binarySearch(int arr[], int l, int r, int x);

void NaivePatternSearch(char *pat, char *txt);

void computeLPSArray(char *pat, int M, int *lps);

void KMPSearch(char *pat, char *txt);
}
}
#endif /* HEADER_SEARCH_H_ */

