//============================================================================
// Name        : AlgorithmCLibrary.cpp
// Author      : 
// Version     :
// Copyright   : Duc Anh Nguyen
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include "Merge.h"
#include "Search.h"
#include "Sort.h"

using namespace std;
using namespace Algorithm::Search;

int main() {
	char *txt = "ABABDABACDABABCABAB";
	char *pat = "ABABCABAB";
	Algorithm::Search::KMPSearch(pat, txt);
	return 0;
}


