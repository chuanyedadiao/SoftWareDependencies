#include<iostream>
#include<fstream>
#include<math.h>
#define E 2.71828
using namespace std;
int s[200];
int getx(int j){
	int x=0;
	for(int i=0;i<=j;i++){
		x+=s[i];
	}
	return x;
}
int main(){
	//ȡeps=0.1��ʱ���a��b��ֵ���м��� 
	double a=142.704,b=3.44848e-005;
	int amount=0;

	fstream input;
	input.open("test.txt",ios::in);
	if(!input){
		cout<<"���ļ�ʧ��";
		exit(1);
	}
	int t=0;
	while(!input.eof()){
		input>>t>>s[amount++];
	}
	input.close();

	t=0;
	for(int i=0;i<amount;i++){
		t+=s[i];
	}

	fstream output;
	output.open("out.txt",ios::out);
	if(!output){
		cout<<"���ļ�ʧ�ܣ�"<<endl;
		exit(1);
	}

	int xi;
	double m;
	double result,xresult;
	for(int i=0;i<amount;i++){
		xi=getx(i);
		output<<xi<<endl;
		m=a*(pow(E,-b*(t+xi))-pow(E,-b*t));
	    result=1-pow(E,m);
	    xresult=double(i)/double(amount+1);
		output<<result<<" "<<xresult<<endl;
	}
	output.close();
}
