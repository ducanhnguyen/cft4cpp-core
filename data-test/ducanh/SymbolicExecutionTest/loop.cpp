int loop1(int a) {
	while (a!=2) {
		a++;
	}
	return a;
}

int loop2(int a) {
	while (a<100) {
		a++;
	}
	return a;
}

int loop3(int a) {
	a=0;
	while (a<100) {
		a++;
	}
	return a;
}

int loop4(int a) {
	a=0;
	while (a<-100) {
		a--;
	}
	return a;
}

int loop5(int a) {
	while (a<=100) {
		a++;
	}
	return a;
}

int loop6(int a) {
	a=0;
	while (a<=100) {
		a++;
	}
	return a;
}

int loop7(int a) {
	a=0;
	while (a<=-100) {
		a--;
	}
	return a;
}

int loop8(int a) {
	a=0;
	a=-1;
	while (a<=-100) {
		a--;
	}
	return a;
}

/*
Should copy all the elements of ordered arrays t1 and t2 into the ordered array t3

Link: pathcrawler-online.com
*/
void loop9(int t1[3], int t2[3], int t3[6]) {
	int i = 0, j = 0, k = 0 ;
	while (i < 3 && j < 3) {
		if (t1[i] < t2[j]) {
			t3[k] = t1[i];
			i++;
		} else {
			t3[k] = t2[j];
			j++;
		}
		k++ ;
	}
	while (i < 3) {
		t3[k] = t1[i];
		i++;
		k++;
	}
	while (j < 3) {
		t3[k] = t2[j];
		j++;
		k++;
	}
}

/*
Link: pathcrawler-online.com

Fix: int a[3];;a[0]=1;a[1]=1;int b[3];;b[0]=1;
*/
int loop10(int a[3], int b[3]) {
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
int loop11(int n, unsigned char* t1, unsigned char* t2) {
	int i;
	for (i = 0; i < n; i++) {
		if (t1[i] > t2[i])
			return -1;
		else if (t1[i] < t2[i])
			return 1;
	}
	return 0;
}

