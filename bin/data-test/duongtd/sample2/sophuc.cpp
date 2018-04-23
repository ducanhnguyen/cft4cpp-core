# include <iostream>
using namespace std;


struct sophuc
{
    int t;
    int a;
    void setT(int t){
    	this->t = t;
	}
	int getT(){
		return t;
	}
	void setA(int a){
    	this->a = a;
	}
	int getA(){
		return a;
	}
	void nhap()
    {
        cin>>t>>a;
    }
    
	
};

ostream &operator<<(ostream &os,sophuc &p)
{
    os<<p.t;
    if(p.a>0)
    os<<"+";
    if(p.a!=0)
    os<<p.a<<"*i";
}
sophuc operator+(sophuc &x,sophuc &y)
{
    sophuc c;
    c.t=y.t+x.t;
    c.a=y.a+x.a;
    return c;
}
sophuc* cong2(sophuc x, sophuc y){
	sophuc* c;
    *c->setT(x.getT() + y.getT());
    *c->setA(x.getA() + y.getA());
    return c;	
}

sophuc cong(sophuc x, sophuc y)
{
	sophuc c;
    c.setT(x.getT() + y.getT());
    c.setA(x.getA() + y.getA());
    return c;
}

int forTesting(int a, int b){
	if(b == 0)
		return a;
	return a/b;
}

int* arrayTesting(int a, int b){
	int *re;
	re = &a;
	*(re + 1) = 9;
	return re;
}

int main()
{
	cout<<"nhap vao 2 so phuc:\n";
    sophuc x, y, z;
    x.nhap();
    y.nhap();
    z=cong(x, y);
    cout<<(z);
    int *a = arrayTesting(1,2);
    cout<<*(a + 1);
}


