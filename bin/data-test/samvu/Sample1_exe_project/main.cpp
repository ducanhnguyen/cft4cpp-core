#include <iostream>

/* run this program using the console pauser or add your own getch, system("pause") or input loop */
int symbolic_execution4(int a, int b);
int main()
{
	symbolic_execution4(2, 3);
	return 0;
}

int symbolic_execution4(int a, int b){
	if (a>b)
		return a;
	return b;
}
