package com.practice.first;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class JM {
    ArrayList<Double> T = new ArrayList<Double>();  //放数据

    double N = 0;
    double FI = 0;
    double ex = 0.1;
    double ey = 0.1;
    public void SetT(JM jm){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("D:\\小黄片\\软件可靠性\\实验\\1\\failure data\\SYS1(failue_count).txt"));
        } catch (FileNotFoundException err) {
            err.printStackTrace();
        }

        String line = null;
        try {
            line = br.readLine();
        } catch (IOException err) {
            err.printStackTrace();
        }

        while(line != null){
            String[] numbers = line.split("\\s+");//拆开
            jm.T.add(Double.valueOf(numbers[1]));
            try {
                line = br.readLine();
            } catch (IOException err) {
                err.printStackTrace();
            }
        }

        try {
            br.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    //开始计算P值
    public double getP(Jm jm)
    {
        double p;
        double sum = 0;
        int n = jm.T.size();
        double temp = jm.T.get(n-1);
        for(int i = 1; i < n; i++){
            double t1 = jm.T.get(i);
            double t2 = jm.T.get(i-1);
            sum = sum + (i-1) * (t1 - t2);
        }
        p = sum / temp;
        return p;
    }

    public double getfun(JM jm,double P,double x)
    {
        double result;
        double temp1;
        double temp2 = 0;
        int n = jm.T.size();
        for (int i = 1; i <= n; i++) {
            temp2 += (1 / (k-i+1));
        }
        temp1 = n/(k-P);
        result = temp1 - temp2;
        return result;
    }

    public void getN(JM jm)
    {
        int n = jm.T.size();
        double left,right;
        double P;
        P = jm.getP(jm);

        if(P>(n-1)/2){
            left = n-1;
            right = n;
        }
        else return;

        double fright,fleft,froot;
        fright = jm.getfun(jm,P,right);
        fleft = jm.getfun(jm,P,left);
        double root;

        while(fright>jm.ey){
            left = right;
            right= right + 1;
        }
        if(fright<= jm.ey && fright >= -jm.ex){
            root = right;
            jm.N = root;
            return;
        }
        else {
            while(true)
            {
                if (Math.abs(right - left) < jm.ex) {
                    root = (left + right) / 2;
                    jm.N = root;
                    return;
                }
                else{
                    root = (left + right) / 2;
                }
                froot = jm.getfun(jm,P,root);
                if(froot > jm.ey){
                    left = root;
                }
                else {
                    if (froot <= jm.ey && froot >= -jm.ey) {
                        jm.N = root;
                        return;
                    }
                    else if(froot < -jm.ey){
                        right = root;
                    }
                }
            }

        }
    }

    public double getFI(JM jm){
        int n = jm.T.size();
        double temp1 = 0;
        for(int i=1;i<n;i++)
        {
            double t1 = jm.T.get(i);
            double t2 = jm.T.get(i-1);
            temp1 += (i-1)*(t1-t2);
        }
        double result;
        result = n/(jm.N * jm.T.get(n-1) - temp1);
        return result;
    }

    public static void main(String[] args){
        JM jm = new JM();
        jm.SetT(jm);
        jm.getN(jm);
        jm.getFI(jm);
        System.out.println(" N = "+jm.N);
        System.out.println("∮ = "+jm.FI);
    }
}
