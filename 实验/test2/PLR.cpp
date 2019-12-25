#include<iostream>
#include<fstream>
#include<math.h>
#define E 2.71828
using namespace std;
int xi[200];
int ti[200];
double a=142.704,b=3.44848e-005;
double Fi=3.448e-05,N0=142;

double fabs1(int n){
	unsigned sum=1;
	for(;n>=1;n--){
		sum*=n;
	}
	return (double)sum;
}

int main(){
	int amount=1;
	fstream input;
	input.open("D:\\小黄片\\软件可靠性\\实验\\test2\\SYS1.txt",ios::in);
	if(!input){
		cout<<"打开文件失败1";
		exit(1);
	}
	int t=0;
	while(!input.eof()){
		input>>t>>xi[amount++];
	}
	input.close();

	t=0;
	for(int i=1;i<amount;i++){
		t+=xi[i];
		ti[i]=t;
	}

	double JMresult[200],GOresult[200];
	for(int i=1;i<amount;i++){
		double tmp1=Fi*(double)(N0-i+1);
		double tmp2=(-1.0)*Fi*(double)(N0-i+1)*(double)xi[i];
		double tmp3=pow(E,tmp2);
		JMresult[i]=tmp1*tmp3;
	}

	for(int i=1;i<amount;i++){
		double tmp1=pow(E,-b*ti[i]);
		double tmp2=a*(1.0-tmp1);
		double tmp3=pow(tmp2,double(i));
		double tmp4=tmp3/(double)fabs1(i);
		GOresult[i]=tmp4*pow(E,(-1.0)*tmp2);
	}

	double DIVresult[200];
	for(int i=1;i<amount;i++){
		DIVresult[i]=JMresult[i]/GOresult[i];
	}

	fstream output;
	output.open("D:\\小黄片\\软件可靠性\\实验\\test2\\PLR.txt",ios::out);
	if(!output){
		cout<<"打开文件失败2！"<<endl;
		exit(1);
	}
	double sum=1;
	for(int i=1;i<amount;i++){
		sum*=DIVresult[i]; 
		output<<sum<<endl;
	}
	output.close();
}
