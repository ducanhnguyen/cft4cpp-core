#include <exception>
#include <iostream>

void foo(){
	int x = 2 > 3 ? 4 : 5;
}

void bubbleSort(int arr[], int n) {
      bool swapped = true;
      int j = 0;
      int tmp;
      while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < n - j; i++) {
                  if (arr[i] > arr[i + 1]) {
                        tmp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = tmp;
                        swapped = true;
                  }
            }
      }
}

int CFGIfElse(int x){
	
	// Simple if
	if (x < 0)
		x = -x;
	
	// If..else
	if (x < 10)
		x++;
	else
		x--;
	
	// Nested if else
	if (x == 11)
		x = 1;
	else if (x == 12)
		x = 2;
	else
		x--;
	
	// Nested if else
	if (x > 20){
		if (x > 30)
			x = x + 2;
		else
			x = x - 2;
	}
	
	return x;
}

int CFGSwitch (int x, int y){
	
	// Simple switch
	switch (x){
	case 1:
		y = 2; break;
	case 2:
		y = 3; break;
	default:
		y = 4; break;
	}
	
	// Nested switch
	switch (x){
	case 1:
		y = 2; break;
	case 2:
		switch (y){
			case 2:
				x++; break;
			case 3:
				x+=2; break;
			default:
				x+=4; break;
		}
		
		break;
	default:
		y = 4; break;
	}
	
	// Switch without default
	switch (x){
	case 4:
		y = 2; break;
	case 5:
		y = 3; break;
	}
	
	// Switch multiple case
	switch (y){
	case 1: case 3: case 5: case 7: case 8: case 10: case 12:
		x = 31; break;
	case 4: case 6: case 9: case 11:
		x = 30; break;
	case 2:
		x = 28; break;
	}
	
	return y;
}

int CFGFor(int n){
	int s = 0, k = 0;
	
	// Simple for
	for (int i = 0; i < n; i++)
		s = s + 1;
		
	// Nested for
	for (int i = 0; i < n; i++)
		for (int j = i + 1; j < n; j++)
			s = s - 1;
	
	// Lack of init statement
	for (; k < n; k++)
		s = s + k;
		
	// Lack of condition statement
	for (k = 0;;k++){
		if (k == n)
			break;
	}
	
	// Lack of iter statement
	for (k = 0; k < n;){
		k++;
	}
	
	// Lack all
	for (;;){
		k++;
		if (k > 100)
			break;
	}
	
	// Test continue inside for
	for (int i = 0; i < n; i++){
		if (i == 2){
			i = 4;
			continue;
		}
	}
	
	return s;
}

int CFGWhile (int n){
	
	// Simple while
	while (n < 0)
		n++;
		
	// Nested while
	while (n > 0){
		while (n == 2)
			n--;
	}
	
	// Always true
	while (true){
		n++;
		if (n > 100)
			break;
	}
	
	// Test continue inside while
	while (n < 100){
		if (n == 10){
			continue;
		}
	}
	
	return n;
}

int CFGDoWhile (int n){
	
	// Simple do
	do
		n++;
	while (n < 0);
		
	// Nested do
	do {
		do
			n--;
		while (n == 2);
	} while (n > 0);
	
	// Always true
	do {
		n++;
		if (n > 100)
			break;
	} while (true);
	
	// Test continue inside do
	do {
		if (n == 10){
			continue;
		}
	} while (n < 100);
	
	return n;
}

int CFGException(int x){
	// Throw exception out of function
	if (x == 0)
		throw std::exception();
		
	// Simple try..catch
	try {
		x++;
		if (x == 2)
			throw x;
	} catch (int x){
		std::cout << x;
	} catch (std::exception& e){
		std::cout << e.what();
	}
	
	// Try..catch with default
	try {
		x++;
		if (x == 10)
			throw x;
	} catch (std::exception& e){
		std::cout << e.what();
	} catch (...){
		std::cout << "Error here";
	}
	
	// Nested try..catch
	try {
		x++;
		try {
			if (x == 10)
				throw x;
		} catch (int x){
			std::cout << x;
		}
	} catch (std::exception& e){
		std::cout << e.what();
	} 
	
	// Rethrow inside catch
	try {
		x++;
		if (x == 2)
			throw x;
	} catch (int x){
		std::cout << x;
		throw x;
	} 
}
