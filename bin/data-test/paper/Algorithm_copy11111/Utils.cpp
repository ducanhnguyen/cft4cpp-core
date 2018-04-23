#include "utils.h"

namespace utils {
	/*
	Should return the type of the triangle which has sides of these lengths.
		3 = not a triangle
		2 = 	l triangle
		1 = isoceles triangle
		0 = other triangle

	Link: pathcrawler-online.com
	*/
	int Tritype(int i, int j, int k) {
		int trityp = 0;
		if (i < 0 || j < 0 || k < 0)
			return 3;
		if (i + j <= k || j + k <= i || k + i <= j)
			return 3;
		if (i == j)
			trityp = trityp + 1;
		if (i == k)
			trityp = trityp + 1;
		if (j == k)
			trityp = trityp + 1;
		if (trityp >= 2)
			trityp = 2;
		return trityp;
	}


	/*
	Link: pathcrawler-online.com
	*/
	int uninit_var(int a[3], int b[3]) {
		int i, k=0;

		for(i=0; i<2; i++) {
			if(a[i] == 0)
				return 0;

			if(a[i] != a[i+1])
				k = 0;
			else if(k == 2)
				return 0;

			while(b[k] != a[i])
				if(k == 2)
					return 0;
				else
					k++;
		}
		return 1;
	}


	/*
	Compares (with respect to the lexicographic order) the subarrays with the first n elements of given arrays t1 and t2,
	Returns  0 if the subarrays are equal,
	1 if the subarray in t1 is greater than in t2,
	-1 if the subarray in t2 is greater than in t1

	Link: pathcrawler-online.com
	*/
	int ArrayCmp(int n, unsigned char* t1, unsigned char* t2) {
		int i;
		for (i = 0; i < n; i++) {
			if (t1[i] > t2[i])
				return -1;
			else if (t1[i] < t2[i])
				return 1;
		}
		return 0;
	}

	/*
	The recursive function
	Link: http://www.programmingsimplified.com
	*/
	int Fibonacci(int n) {
		if ( n == 0 )
			return 0;
		else if ( n == 1 )
			return 1;
		else {
			int x = Fibonacci(n-1);
			int y =  Fibonacci(n-2);
			return x+y;
		}

	}

	/*
	Find maximum element in array
	Link: http://www.programmingsimplified.com/c/source-code/c-program-find-maximum-element-in-array
	*/
	int find_maximum(int a[], int n) {
		int c, max, index;

		max = a[0];
		index = 0;

		for (c = 1; c < n; c++) {
			if (a[c] > max) {
				index = c;
				max = a[c];
			}
		}

		return index;
	}
	/*
	Add digits of number in c
	Link: http://www.programmingsimplified.com/c/program/c-program-add-number-digits
	*/
	int add_digits(int n) {
		static int sum = 0;

		if (n == 0) {
			return 0;
		}
		sum = n%10 + add_digits(n/10);

		return sum;
	}

	/*
	Reverse number using recursion
	Link: http://www.programmingsimplified.com/c/source-code/c-program-reverse-number
	*/
	long reverse(long n) {
		static long r = 0;

		if (n == 0)
			return 0;

		r = r * 10;
		r = r + n % 10;
		reverse(n/10);
		return r;
	}

	/*
	Print diamond using recursion
	Link: http://www.programmingsimplified.com/c/source-code/c-program-print-diamond-pattern
	*/
	void print (int r) {
		int c, space;
		int stars = -1;

		if (r <= 0)
			return;

		space = r - 1;
		stars += 2;

		for (c = 0; c < space; c++)
			printf(" ");

		for (c = 0; c < stars; c++)
			printf("*");

		printf("\n");
		print(--r);

		space = r + 1;
		stars -= 2;

		for (c = 0; c < space; c++)
			printf(" ");

		for (c = 0; c < stars; c++)
			printf("*");

		printf("\n");
	}

	/*
	C program for prime number
	Link: http://www.programmingsimplified.com/c/source-code/c-program-for-prime-number
	*/
	int check_prime(int a) {
		int c;

		for ( c = 2 ; c <= a - 1 ; c++ ) {
			if ( a%c == 0 )
				return 0;
		}
		if ( c == a )
			return 1;
	}

	/*
	Check Armstrong number using function
	Link: http://www.programmingsimplified.com/c-program-find-armstrong-number
	*/
	int check_armstrong(long long);
	long long power(int, int);

	int check_armstrong(long long n) {
		long long sum = 0, temp;
		int remainder, digits = 0;

		temp = n;

		while (temp != 0) {
			digits++;
			temp = temp/10;
		}

		temp = n;

		while (temp != 0) {
			remainder = temp%10;
			sum = sum + power(remainder, digits);
			temp = temp/10;
		}

		if (n == sum)
			return 1;
		else
			return 0;
	}

	long long power(int n, int r) {
		int c;
		long long p = 1;

		for (c = 1; c <= r; c++)
			p = p*n;

		return p;
	}
	/*
	Print Floyd's triangle using recursion
	Link: http://www.programmingsimplified.com/c-program-print-floyd-triangle
	*/
	void print_floyd(int n) {
		static int row = 1, c = 1;
		int d;

		if (n <= 0)
			return;

		for (d = 1; d <= row; ++d)
			printf("%d ", c++);

		printf("\n");
		row++;

		print_floyd(--n);
	}

	/*
	Find minimum element in array
	Link: http://www.programmingsimplified.com/c/source-code/c-program-find-minimum-element-in-array
	*/
	int find_minimum(int a[], int n) {
		int c, min, index;

		min = a[0];
		index = 0;

		for (c = 1; c < n; c++) {
			if (a[c] < min) {
				index = c;
				min = a[c];
			}
		}

		return index;
	}




	/*
	Reverse an array
	Link: http://www.programmingsimplified.com/c-program-reverse-array
	*/
	void reverse_array(int *pointer, int n) {
		int *s, c, d;

		s = (int*)malloc(sizeof(int)*n);

		if( s == NULL )
			exit(EXIT_FAILURE);
		d = 0 ;
		for ( c = n - 1; c >= 0 ; c-- ) {
			*(s+d) = *(pointer+c);
			d++;
		}

		for ( c = 0 ; c < n ; c++ )
			*(pointer+c) = *(s+c);

		free(s);
	}

	/*
	Concatenate strings without strcat function
	Link: http://www.programmingsimplified.com/c-program-concatenate-strings
	*/
	void concatenate(char p[], char q[]) {
		int c, d;
		c = 0;

		while (p[c] != '\0') {
			c++;
		}

		d = 0;

		while (q[d] != '\0') {
			p[c] = q[d];
			d++;
			c++;
		}

		p[c] = '\0';
	}

	/*
	Concatenate strings using pointers without strcat function
	Link: http://www.programmingsimplified.com/c-program-concatenate-strings
	*/
	void concatenate_string(char *original, char *add) {
		while(*original)
			original++;

		while(*add) {
			*original = *add;
			add++;
			original++;
		}
		*original = '\0';
	}

	/*
	Reverse a string using pointers
	Link: http://www.programmingsimplified.com/c-program-reverse-string
	*/
	int string_length1(char *pointer) {
		int c = 0;

		while( *(pointer + c) != '\0' )
			c++;

		return c;
	}


	/*
	Reverse a string using recursion
	Link: http://www.programmingsimplified.com/c-program-reverse-string
	*/
	void reverse2(char *x, int begin, int end) {
		char c;

		if (begin >= end)
			return;

		c          = *(x+begin);
		*(x+begin) = *(x+end);
		*(x+end)   = c;

		reverse2(x, ++begin, --end);
	}

	/*
	Link: http://www.programmingsimplified.com/c-program-find-palindrome
	*/
	int string_length(char *string) {
		int length = 0;

		while(*string) {
			length++;
			string++;
		}

		return length;
	}
	/*
	Link: http://www.programmingsimplified.com/c-program-find-palindrome
	*/
	void copy_string(char *target, char *source) {
		while(*source) {
			*target = *source;
			source++;
			target++;
		}
		*target = '\0';
	}
	/*
	Link: http://www.programmingsimplified.com/c-program-find-palindrome
	*/
	int compare_string(char *first, char *second) {
		while(*first==*second) {
			if ( *first == '\0' || *second == '\0' )
				break;

			first++;
			second++;
		}
		if( *first == '\0' && *second == '\0' )
			return 0;
		else
			return -1;
	}

	/*
	Delete vowels from a string
	Link: http://www.programmingsimplified.com/c/source-code/c-program-remove-vowels-from-string
	*/
	int check_vowel(char a) {
		if ( a >= 'A' && a <= 'Z' )
			a = a + 'a' - 'A';

		if ( a == 'a' || a == 'e' || a == 'i' || a == 'o' || a == 'u')
			return 1;

		return 0;
	}

	/*
	Check subsequence
	Link: http://www.programmingsimplified.com/c/source-code/c-program-check-subsequence
	*/
	int check_subsequence (char a[], char b[]) {
		int c, d;

		c = 0;
		d = 0;

		while (a[c] != '\0') {
			while ((a[c] != b[d]) && b[d] != '\0') {
				d++;
			}
			if (b[d] == '\0')
				break;
			d++;
			c++;
		}
		if (a[c] == '\0')
			return 1;
		else
			return 0;
	}

	/*
	Find frequency of characters in a string
	Link: http://www.programmingsimplified.com/c-program-find-characters-frequency
	*/
	void find_frequency(char s[], int count[]) {
		int c = 0;

		while (s[c] != '\0') {
			if (s[c] >= 'a' && s[c] <= 'z' )
				count[s[c]-'a']++;
			c++;
		}
	}

	/*
	Check whether two strings are anagrams or not,
	Link: http://www.programmingsimplified.com/c/source-code/c-anagram-programcheck_subsequence
	*/
	int check_anagram(char a[], char b[]) {
		int first[26], second[26], c = 0;
		first[20] = 0;
		second[26]=0;
		while (a[c] != '\0') {
			first[a[c]-'a']++;
			c++;
		}

		c = 0;

		while (b[c] != '\0') {
			second[b[c]-'a']++;
			c++;
		}

		for (c = 0; c < 26; c++) {
			if (first[c] != second[c])
				return 0;
		}

		return 1;
	}
}


