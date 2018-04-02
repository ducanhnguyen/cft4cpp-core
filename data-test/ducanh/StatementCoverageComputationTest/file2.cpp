#include <iostream>
#include <fstream>
#include <string>
#include <windows.h>
#include <conio.h>
#include <iomanip>

using namespace std;
void StrDel(char s[],int k,int h)
{
    memmove(s+k,s+k+h,strlen(s)-k+1);
}

void xoa_dau_cach_thua(char s[]) //
{
    while (s[0] == ' ') StrDel(s, 0, 1);
    int i = 0;
    while( i<strlen(s))
    {
        if (s[i]==' ')
            while (s[i+1]==' ' & s[i+1]!='\0')
                StrDel(s, i+1, 1);
        i++;
    }
}
