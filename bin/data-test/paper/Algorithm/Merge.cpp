namespace Algorithm {
namespace Merge {
/**
 * MERGE TWO ARRAY
 *
 * http://www.programmingsimplified.com/c/source-code/c-program-merge-two-arrays
 */
void mergeTwoArray(int a[], int m, int b[], int n, int sorted[]) {
	int i, j, k, t;

	t = j = k = 0;

	for (i = 0; i < m + n; t++) {
		if (j < m && k < n) {
			if (a[j] < b[k]) {
				sorted[i] = a[j];
				j++;
			} else {
				sorted[i] = b[k];
				k++;
			}
			i++;
		} else if (j == m) {
			for (int t = 0; i < m + n;t++) {
				sorted[i] = b[k];
				k++;
				i++;
			}
		} else {
			for (int t = 0; i < m + n;t++) {
				sorted[i] = a[j];
				j++;
				i++;
			}
		}
	}
}
}
}

