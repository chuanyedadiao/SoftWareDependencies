package com.practice.first;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JM {
    //使用List存储数据T[i]
    ArrayList<Double> T = new ArrayList<Double>();
    double N = 0;
    double Fi = 0;
    double ex = 0.1;
    double ey = 0.1;

    //从文本文件中读取数据
    public void setT(JM jm) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(line != null){
            String[] numbers = line.split("\\s+");
            jm.T.add(Double.valueOf(numbers[1]));
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //计算P的值
    public double getP(JM jm) {
        double result = 0;
        double sum = 0;
        int n = jm.T.size();
        for(int i = 1; i < n; i++){
            sum += i * (jm.T.get(i) - jm.T.get(i-1));
        }
        result = sum / jm.T.get(n-1);
        return result;
    }

    //求函数f（N）
    public double fun(JM jm, double k, double P) {
        double result = 0;
        double sum = 0;
        int n = jm.T.size();
        for (int i = 1; i <= n; i++) {
            sum += (1 / (k-i+1));
        }
        result = sum - n/(k-P);
        return result;
    }

    //计算N的值
    public void setN(JM jm) {
        int n = jm.T.size();
        double left = 0;
        double right = 0;
        double root = 0;
        double f_left = 0;
        double f_right = 0;
        double f_root = 0;

        double P;
        P = jm.getP(jm);

        //步骤1
        if(P > (n-1)/2){
            left = n-1;
            right = n;
        }
        else return;

        //步骤2
        f_left = jm.fun(jm, left, P);
        f_right = jm.fun(jm, right, P);
        while (f_right > jm.ey){
            left = right;
            right = right + 1;
        }
        if(-jm.ex <= f_right && f_right <= jm.ey){
            root = right;
            jm.N = root;
            return;
        }
        else  {
            while (true) {
                //步骤3
                if (Math.abs(right-left) < jm.ex) {
                    root = (right+left)/2;
                    jm.N = root;
                    return;
                }
                else {
                    root = (right+left)/2;
                }

                //步骤4
                f_root = jm.fun(jm, root, P);
                if (-jm.ey <= f_root && f_root <= jm.ey) {
                    jm.N = root;
                    return;
                }
                else if (f_root > jm.ey) {
                    left = root;
                }
                else {
                    right = root;
                }
            }
        }
    }

    //计算∮的值
    public void setFi(JM jm) {
        double sum = 0;
        int n = jm.T.size();
        for(int i = 1; i < n; i++){
            sum += i * (jm.T.get(i) - jm.T.get(i-1));
        }
        jm.Fi = n / (jm.N*jm.T.get(n-1) - sum);
    }

    public static void main(String[] args) {
        JM jm = new JM();
        jm.setT(jm);
        jm.setN(jm);
        jm.setFi(jm);
        System.out.println(" N = "+jm.N);
        System.out.println("∮ = "+jm.Fi);
    }
}
