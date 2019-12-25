import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BP {
    ArrayList<Double> data = new ArrayList<Double>();

    ArrayList<Double> X = new ArrayList<Double>();  //输入
    ArrayList<Double> hi = new ArrayList<Double>();  //隐含层输入
    ArrayList<Double> ho = new ArrayList<Double>();  //隐含层输出
    ArrayList<Double> yi = new ArrayList<Double>();  //输出层输入
    ArrayList<Double> yo = new ArrayList<Double>();  //输出层输出
    ArrayList<Double> d0 = new ArrayList<Double>();  //期望输出

    ArrayList<Double> Wih = new ArrayList<Double>();   //输入层与中间层的连接权值
    ArrayList<Double> Who = new ArrayList<Double>();  //隐含层与输出层的连接权值

    ArrayList<Double> bh = new ArrayList<Double>(); //隐含层各神经元的阈值
    ArrayList<Double> bo = new ArrayList<Double>();  //输出层各神经元的阈值

    ArrayList<Double> delta_o = new ArrayList<Double>();
    ArrayList<Double> delta_h = new ArrayList<Double>();

    double Precision_Value = 0.01;  //计算精度，我也不知道定多少哈哈哈哈哈
    int Max_Learn_Times = 100000; //最大学习次数，我也不知道定多少哈哈哈哈hh
    int Now_Times = 1;
    double rate = 0.99;
    int step = 1;

    ArrayList<Double> e = new ArrayList<Double>();
    double E = 100;

     //总样本数
    int n = 5;
    int p = 5;
    int q = 5;
    int m = 26;

    public void Init(BP bp) {
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
            bp.data.add(Double.valueOf(numbers[1]));
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

        for(i=0;i<bp.n;i++){
            bp.X.add((double)0);
        }
        for(i=0;i<bp.q;i++){
            bp.d0.add((double)0);
        }
        for(i=0;i<bp.p;i++){
            bp.bh.add(0.0001);
        }
        for(i=0;i<bp.q;i++){
            bp.bo.add(0.0001);
        }
        for(i=0;i<bp.p;i++){
            bp.hi.add((double)0);
        }
        for(i=0;i<bp.p;i++){
            bp.ho.add((double)0);
        }
        for(i=0;i<bp.q;i++){
            bp.yi.add((double)0);
        }
        for(i=0;i<bp.q;i++){
            bp.yo.add((double)0);
        }
        for(i=0;i<bp.q;i++){
            bp.delta_o.add((double)0);
        }
        for(i=0;i<bp.p;i++){
            bp.delta_h.add((double)0);
        }
        for(i=0;i<bp.m;i++){
            bp.e.add((double)0);
        }
    }

    public void setX(BP bp,int k){
        int j = (k-1)%bp.m;
        for(int i=0;i<bp.n;i++){
            bp.X.set(i,bp.data.get((i+j*bp.n)%136));
        }
    }//输入

    public void setd0(BP bp,int k){
        int j = (k-1)%bp.m;
        for(int i=0;i<bp.q;i++){
            bp.d0.set(i,bp.data.get((i+j*bp.n+bp.step)%136));
        }
    }//期望输出

    public void setW(BP bp){
        //(y - Math.random()) % y;
        int i,j;
        boolean flag = false;
        double temp;
        for(i=0;i<bp.p;i++){
            for(j=0;j<bp.n;j++){
                temp = Math.random();
                if(flag)
                    bp.Wih.add(temp);
                else bp.Wih.add(-temp);
                flag = flag^true;
            }
        }

        flag = false;
        for(i=0;i<bp.p;i++){
            for(j=0;j<bp.q;j++){
                temp = Math.random();
                if(flag)
                    bp.Who.add(temp);
                else bp.Who.add(-temp);
                flag = flag^true;
            }
        }
    }//对应的

    public void getE(BP bp){
        int q= bp.q;
        double temp1 = 0;
        double sum = 0;
        for(int i=0;i<q;i++){
            temp1 = bp.d0.get(i)-bp.yo.get(i);
            sum += Math.pow(temp1,2.0);
        }
        bp.e.set((bp.Now_Times%bp.m),1.0/2*sum);
    }//误差函数

    public void getHi(BP bp){
        double sum = 0;
        double temp1=0;
        for(int h = 0;h<bp.p;h++){
            for(int i=0;i<bp.n;i++){
                sum+=bp.Wih.get(i*bp.p+h)*bp.X.get(i);
            }
            temp1 = sum-bp.bh.get(h);
            sum=0;
            bp.hi.set(h,temp1);
        }
    }

    public void getHo(BP bp){
        double result=0,temp1;
        int i=0;
        int p = bp.hi.size();
        for(i=0;i<p;i++){
            temp1 = 1+Math.exp(-bp.hi.get(i));
            result = 1.0/temp1;
           // System.out.print("bp.hi.get(i)="+bp.hi.get(i)+"   temp:    "+temp1+"\n");
            bp.ho.set(i,result);
        }
    }

    public void getYi(BP bp){
        double sum=0;
        double temp1;
        for(int o=0;o<bp.q;o++){
            for(int h=0;h<bp.p;h++){
                sum+=bp.Who.get(h*bp.q+o)*bp.ho.get(h);
            }
            temp1 = sum-bp.bo.get(o);
            sum=0;
            bp.yi.set(o,temp1);
        }
    }

    public void getYO(BP bp){
        double result=0,temp1;
        int i=0;
        int q = bp.yi.size();
        for(i=0;i<q;i++){
            temp1 = 1+Math.exp(-bp.yi.get(i));
            result = 1.0/temp1;
           // System.out.println("\ntemp1 = "+temp1);
            bp.yo.set(i,result);
        }
       /* System.out.println("\n\n这次的所有yi和yo:");
        for(i=0;i<bp.q;i++){
            System.out.println("    "+bp.yi.get(i)+"  "+bp.yo.get(i)+"...");
        }*/
    }



    public double getDerivatives(double temp){
        double result;
        result = temp*(1-temp);
        return result;
    }

    public void getdelta_o(BP bp){
        int q = bp.q;
        double temp1,temp2;
        for(int i=0;i<q;i++){
            temp1 =getDerivatives(bp.yo.get(i));
            temp2 = -(bp.d0.get(i)-bp.yo.get(i))*temp1;
            bp.delta_o.set(i,-temp2);
        }
    }

    public void getdelta_h(BP bp){
        int q = bp.q;
        int p = bp.p;
        double temp1,sum=0;
        for(int i=0;i<p;i++){
            temp1 = getDerivatives(bp.ho.get(i));
            for(int j=0;j<q;j++){
                sum += bp.delta_o.get(j)*bp.Who.get(i*q+j);
            }
            temp1 = temp1*sum;
            bp.delta_h.set(i,temp1);
        }
    }

    public void changeWho(BP bp){
        double change_value=0,temp;
        for(int h=0;h<bp.p;h++){
            for(int o=0;o<bp.q;o++){
                change_value = bp.delta_o.get(o)*bp.ho.get(h);
                temp = bp.Who.get(h*bp.q+o);
                bp.Who.set(h*bp.q+o,temp+bp.rate*change_value);
               // System.out.print("delta_o:  "+bp.delta_o.get(o)+"  ho:  "+bp.ho.get(h)+"   改变值："+change_value+"\n");
            }
        }
    }

    public void changeWih(BP bp){
        double change_value=0,temp;
        for(int h=0;h<bp.p;h++){
            for(int i=0;i<bp.n;i++){
                change_value = bp.delta_h.get(h)*bp.X.get(h);
                temp = bp.Wih.get(h+i*p);
                bp.Wih.set(h+i*p,temp+bp.rate*change_value);
            }
        }
    }

    public void getLastE(BP bp){
        double sum=0;
        for(int i=0;i<bp.m;i++){
            sum+=2*bp.e.get(i);
        }

        bp.E = 1.0/(2*bp.m)*sum;
    }

    public void printdata(BP bp){
        System.out.println("\n\n"+"第"+bp.Now_Times+"次");

        System.out.println("\n输入");
        for(int i=0;i<bp.n;i++){
            System.out.print("  "+bp.X.get(i)+"...");
        }

        System.out.println("\n实际输出");
        for(int i=0;i<bp.q;i++){
            System.out.print("  "+bp.yo.get(i)+"...");
        }

        /*System.out.println("\nWHO:::");
        for(int i=0;i<bp.p;i++){
            for(int j=0;j<bp.q;j++)
            System.out.print("  "+bp.Who.get(i*bp.q+j)+"...");
            System.out.print("\n");
        }*/

        /*System.out.println("\n期望输出");
        for(int i=0;i<bp.q;i++){
            System.out.print("  "+bp.d0.get(i)+"...");
        }*/

        /*System.out.println("\n误差:~~");
        for(int i=0;i<bp.m;i++){
            System.out.print("  "+bp.e.get(i)+"...");
        }*/

        System.out.println("\n\n误差值："+bp.E);
    }


    public static void main(String[] args){
        BP bp = new BP();
        bp.Init(bp);
        bp.setW(bp);
        for(bp.Now_Times=1;bp.Now_Times<=bp.Max_Learn_Times&&bp.E>bp.Precision_Value;bp.Now_Times++){
            //产生随机样本
            bp.setX(bp,bp.Now_Times);
            bp.setd0(bp,bp.Now_Times);
            bp.getHi(bp);
            bp.getHo(bp);
            bp.getYi(bp);
            bp.getYO(bp);
            bp.getdelta_o(bp);
            bp.getdelta_h(bp);
            bp.changeWih(bp);
            bp.changeWho(bp);
            bp.getE(bp);
            bp.getLastE(bp);
            bp.printdata(bp);
        }
    }
}
