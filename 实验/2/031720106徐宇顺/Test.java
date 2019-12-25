import org.omg.CORBA.MARSHAL;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    ArrayList<Double> Ta = new ArrayList<Double>();
    ArrayList<Double> Tb = new ArrayList<Double>();
    ArrayList<Double> Fa = new ArrayList<Double>();
    ArrayList<Double> Fb = new ArrayList<Double>();
    ArrayList<Double> Fjm = new ArrayList<Double>();
    ArrayList<Double> FGo = new ArrayList<Double>();
    double N0=141.90625;
    double FI=3.496383885854378E-5;
    double a=142.8807301485316;
    double b=3.4204071342356955E-5;
    double sum=0;

    public void setTa(Test test){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("D:\\小黄片\\软件可靠性\\实验\\2\\src\\test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int i=0;
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(line != null){
            String[] numbers = line.split("\\s+");
            test.Ta.add(Double.valueOf(numbers[1]));
            i++;
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

    public void setTb(Test jm) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("D:\\小黄片\\软件可靠性\\实验\\2\\src\\test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int i=0;
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double temp=0;

        String[] T0 = line.split("\\s+");
        temp=Double.valueOf(T0[1]);
        jm.Tb.add(temp);
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(line != null){
            String[] numbers = line.split("\\s+");
            temp+=Double.valueOf(numbers[1]);
            jm.Tb.add(temp);
            jm.sum+=temp;
            i++;
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

    public double getfa(Test test,int i){
        double result;
        double temp1 = test.FI*(test.N0-i+1);
        double temp2 = -test.FI*(test.N0-i+1)*test.Ta.get(i-1);
        double temp3 = Math.pow(Math.E,temp2);
        result = temp1*temp3;
        return result;
    }

    public double getfb(Test test,int i){
        double temp1 = (test.a*(1-Math.pow(Math.E,-test.b*test.Tb.get(i-1))));
        double temp2 = Math.pow(Math.E,-temp1);
        double temp3 = Math.pow(temp1,(double)i);
        double temp4 = 1;
        for(int j=i;j>=1;j--){
            temp4 *= j;
        }
        double result = (temp3/temp4)*temp2;
        return result;
    }

    public void getJm(Test test){
        ArrayList<Double> vertical = new ArrayList<Double>();

        int n = test.Ta.size();
        int k =10;
        double x = 0;
        double index = 0;
        double result = 0;
        double temp;
        int j = n-1;
        for (int i = 0; i < n; i++) {
            x = test.Ta.get(j);
            index = -test.FI*(test.N0-j+1)*x;
            result = 1-Math.pow(Math.E, index);
            test.Fjm.add(result);
            temp = (double)j/n;
            j--;
            vertical.add(temp);
        }

        for (int i = 0; i < test.Fjm.size(); i++) {
            System.out.println(test.Fjm.get(i)+"    "+vertical.get(i));
        }
    }

    public void getGo(Test test){
        ArrayList<Double> vertical = new ArrayList<Double>();

        int n = test.Tb.size();
        int k =10;
        double t = 0;
        double s = 0;
        double result = 0;
        double index = 0;
        double temp=0;
        int j = n-1;
        for (int i = 1; i < n; i++) {
            t = test.Tb.get(j)-test.Tb.get(j-1);
            s = test.Tb.get(j);
            index = -test.a*(1-Math.exp(-test.b*t))*Math.exp(-test.b*s);
            result = 1-Math.exp(index);
            test.FGo.add(result);
            temp = (double)j/n;
            j--;
            vertical.add(temp);
        }

        for (int i = 0; i < test.FGo.size(); i++) {
            System.out.println(test.FGo.get(i)+"    "+vertical.get(i));
        }
        return;
    }



    public void GetYF(Test test){
        ArrayList<Double> vertical = new ArrayList<Double>();
        ArrayList<Double> horizon = new ArrayList<Double>();

        double temp1,temp2;
        double sum1=0,sum2=0;
        int n=test.Fjm.size();
        int i,j,k;
        for(i=0;i<n;i++){
            temp1 = -Math.log(1-test.Fjm.get(i));
            horizon.add(temp1);
            sum2+=temp1;
        }
        int m = horizon.size();
        for(i=0;i<n;i++){
            for(j=0;j<=i;j++){
                sum1+=horizon.get(i);
            }
            temp2=sum1/sum2;
            vertical.add(temp2);
            sum1=0;
        }

        System.out.println("J-M  Y图啊啊啊啊啊\n\n\n");
        for(i=0;i<n;i++){
            System.out.println(horizon.get(i)+"      "+vertical.get(i));
        }
    }

    public void GetYG(Test test){
        ArrayList<Double> vertical = new ArrayList<Double>();
        ArrayList<Double> horizon = new ArrayList<Double>();

        double temp1,temp2;
        double sum1=0,sum2=0;
        int n=test.FGo.size();
        int i,j,k;
        for(i=0;i<n;i++){
            temp1 = -Math.log(1-test.FGo.get(i));
            horizon.add(temp1);
            sum2+=temp1;
        }
        int m = horizon.size();
        for(i=0;i<n;i++){
            for(j=0;j<=i;j++){
                sum1+=horizon.get(i);
            }
            temp2=sum1/sum2;
            vertical.add(temp2);
            sum1=0;
        }

        System.out.println("G-O   Y图啊啊啊啊啊\n\n\n");
        for(i=0;i<n;i++){
            System.out.println(horizon.get(i)+"      "+vertical.get(i));
        }
    }

    public static void main(String[] args){
        Test test = new Test();
        double PLR=1;
        test.setTa(test);
        test.setTb(test);
        int n = test.Ta.size();
        double tempa,tempb,sum=0;
        int i;
        for(i=1;i<=n;i++){
            tempa=test.getfa(test,i);
            test.Fa.add(Double.valueOf(tempa));
            tempb=test.getfb(test,i);
            test.Fb.add(Double.valueOf(tempb));
        }

        test.getJm(test);
        test.getGo(test);

        Bubble_Sort.bubbleSort(test.Fjm);
        Bubble_Sort.bubbleSort(test.FGo);

        test.GetYF(test);
        test.GetYG(test);
        for(int j=0;j<n;j++){
            PLR *= test.Fa.get(j)/test.Fb.get(j);
        }

        System.out.println("PLR= "+PLR);
        return;
    }
}


class Swap{

    public static void swap( ArrayList<Double> a,int i,int j){

        double temp = a.get(i);

        a.set(i,a.get(j));

        a.set(j,temp);

    }

}

/**

 * 冒泡排序

 */

class Bubble_Sort{

    public static void bubbleSort( ArrayList<Double> a){
        for(int i = 0;i<a.size();i++){
            for(int j = 0;j<a.size()-1-i;j++){
                if(a.get(j) > a.get(j+1)){
                    Swap.swap(a, j, j+1);
                }

            }

        }
    }
}
