

int Level1InputSimple(int* p){
	if (*p > 0)
		return 1;
	else
		return 0;
}


int Level1InputAdvance(int* p){
	if (*p > 0)
		return *(p+1);
	else
		return *(p+2);
}


int Level2InputSimple(int** p){
	if (**p > 0)
		return 1;
	else
		return 0;
}


int Level2InputAdvance(int** p){
	if (**p > 0)
		return **(p+1);
	else
		return **(p+2);
}



void Level1OutputSimple(int* p){
	*p = *p + 1;
}


void Level1OutputAdvance(int* p){
	*p = *p + 1;
	if (*p > 0)
		*(p+1) = 2;
	else
		*(p+2) = 1;
}


void Level2OutputSimple(int** p){
	**p = **p - 1;
}


void Level2OutputAdvance(int** p){
	**p = **p - 1;
	if (**p > 0)
		**(p+1) = 2;
	else
		**(p+2) = 1;
}

