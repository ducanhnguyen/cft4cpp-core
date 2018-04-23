

int Dim1InputSimple(int p[]){
	if (p[0] > 0)
		return 1;
	else
		return 0;
}


int Dim1InputAdvance(int p[]){
	if (p[0] > 0)
		return p[1];
	else
		return p[2];
}


int Dim2InputSimple(int p[][1]){
	if (p[0][0] > 0)
		return 1;
	else
		return 0;
}


int Dim2InputAdvance(int p[][1]){
	if (p[0][0] > 0)
		return p[1][0];
	else
		return p[2][0];
}



void Dim1OutputSimple(int p[]){
	p[0] = p[0] + 1;
}


void Dim1OutputAdvance(int p[]){
	p[0] = p[0] + 1;
	if (p[0] > 0)
		p[1] = 2;
	else
		p[2] = 1;
}


void Dim2OutputSimple(int p[][1]){
	p[0][0] = p[0][0] - 1;
}


void Dim2OutputAdvance(int p[][1]){
	p[0][0] = p[0][0] - 1;
	if (p[0][0] > 0)
		p[1][0] = 2;
	else
		p[2][0] = 1;
}

