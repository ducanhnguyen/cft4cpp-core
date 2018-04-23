 int test(){int a = 0;
	int b, c;
	// if
	if (a>b){
		a++;
	}
	if (a>b||a>c){
		a++;
	}else
		b++;
	// for
	for (int i=0;i<a;i++){
		a++;
	}
	for (;i<a;i++){
		a++;
	}
	// while
	while (a<b){
		a--;
	}
	// do
	do{
		a--;
	}while(a<b);
	// try-catch
	try{
		throw 20;
	}catch (int e){
		cout << "An exception occurred. Exception Nr. " << e << '\n';
	}
	return 0;
}