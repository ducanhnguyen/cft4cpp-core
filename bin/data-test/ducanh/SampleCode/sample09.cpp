#include<stdio.h>
#include<stdlib.h>
#include<time.h>    
#define swap(type, a, b) {type temp = a; a = b; b = temp; } // hang hoan vi
 
 //Sample for item 16: Function check if condition with swap function
void Sample16(int *a, int l, int r)
{
    srand(time(NULL));  
    int key = a[l + rand() % (r-l+1)];  
    //int key = a[(l+r)/2];
    int i = l, j = r;
 
    while(i <= j)
    {
        while(a[i] < key) i++;     
        while(a[j] > key) j--;     
        if(i <= j)
        {
            if (i < j)
                swap(int, a[i], a[j]);  
            i++;
            j--;
        }
    }
    //bay gio ta co 1 mang : a[l]....a[j]..a[i]...a[r]
    if (l < j) quickSort(a, l, j); 
    if (i < r) quickSort(a, i, r); 
}
 

int main ()
{
    int i, arr[] = { 40, 10, 100, 90, 20, 25 };
    quickSort(arr, 0, 5);
    for (i=0; i<6; i++)
        printf ("%d ", arr[i]);
    system("pause");
    return 0;
    
}
