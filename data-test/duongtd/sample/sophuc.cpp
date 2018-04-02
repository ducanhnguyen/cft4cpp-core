# include <iostream>
using namespace std;


class sophuc
{
public:
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
    friend ostream &operator<<(ostream &os,sophuc &p);
    friend sophuc operator+(sophuc &x,sophuc &y);
};

sophuc z;

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
sophuc cong(sophuc x, sophuc y)
{
	sophuc c;
    c.setT(x.getT() + y.getT());
    c.setA(x.getA() + y.getA());
    return c;
}

int main()
{
	cout<<"nhap vao 2 so phuc:\n";
    sophuc a,b,c;
    a.nhap();
    b.nhap();
    c=cong(a,b);
    cout<<(c);
}

