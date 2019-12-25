import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JM {
    //使用List存储数据T[i]
    ArrayList<Double> T = new ArrayList<Double>();
    ArrayList<Double> F = new ArrayList<Double>();
    double N = 0;
    double Fi = 0;
    double ex = 0.001;
    double ey = 0.001;

    //从文本文件中读取数据
    public void setT(JM jm) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("D:\\\\小黄片\\\\软件可靠性\\\\实验\\\\2\\\\src\\\\test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double temp = 0;
        jm.T.add(0.0);
        while (line != null) {
            String[] numbers = line.split("\\s+");
            temp += Double.valueOf(numbers[1]);
            jm.T.add(temp);
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

        System.out.println(jm.T.size());
    }

    //计算P的值
    public double getP(JM jm) {
        double result = 0;
        double sum = 0;
        int n = jm.T.size()-1;
        for(int i = 1; i <= n; i++){
            sum += (i - 1) * (jm.T.get(i) - jm.T.get(i-1));
        }
        result = sum / jm.T.get(n);
        return result;
    }

    //求函数f（N）
    public double fun(JM jm, double k, double P) {
        double result = 0;
        double sum = 0;
        int n = jm.T.size()-1;
        for (int i = 1; i <= n; i++) {
            sum += (1 / (k-i+1));
        }
        result = sum - n/(k-P);
        return result;
    }

    //计算N的值
    public void setN(JM jm) {
        int n = jm.T.size()-1;
        double left = 0;
        double right = 0;
        double root = 0;
        double f_left = 0;
        double f_right = 0;
        double f_root = 0;

        double P;
        P = jm.getP(jm);
        System.out.println(P);
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
            f_right = jm.fun(jm, right, P);
        }
        if(-jm.ex <= f_right){
            root = right;
            jm.N = root;
            return;
        }
        else  {
            while (true) {
                //步骤3
                if (Math.abs(right-left) < jm.ex) {
                    root = (right+left)/2;
                    break;
                }
                if (Math.abs(right-left) > jm.ex){
                    root = (right+left)/2;
                }

                //步骤4
                f_root = jm.fun(jm, root, P);
                if (f_root > jm.ey) {
                    left = root;
                    continue;
                }
                if (-jm.ey <= f_root && f_root <= jm.ey) {
                    jm.N = root;
                    break;
                }
                if (f_root < -jm.ey) {
                    right = root;
                    continue;
                }
            }
        }
        jm.N = root;
        return;
    }

    //计算∮的值
    public void setFi(JM jm) {
        double sum = 0;
        int n = jm.T.size()-1;
        for(int i = 1; i <= n; i++){
            sum += (i-1) * (jm.T.get(i) - jm.T.get(i-1));
        }
        jm.Fi = n / (jm.N*jm.T.get(n) - sum);
    }

    public void setF(JM jm) {
        int n = jm.T.size();
        int k =10;
        double index = 0;
        double result = 0;
        int j = n-1;
        for (int i = 0; i < k; i++) {
            index = -jm.Fi*(jm.N-j)*jm.T.get(j);
            j--;
            result = 1-Math.exp(index);
            jm.F.add(i, result);
        }
        System.out.println(jm.F.size());
        for (int i = 0; i < k; i++) {
            System.out.println(jm.F.get(i));
        }
        return;
    }

    public static void main(String[] args) {
        JM jm = new JM();
        jm.setT(jm);
        jm.setN(jm);
        jm.setFi(jm);
        //    jm.setF(jm);
        System.out.println(" N = "+jm.N);
        System.out.println("∮ = "+jm.Fi);
    }
}
