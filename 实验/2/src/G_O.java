
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class G_O {
    ArrayList<Double> T = new ArrayList<Double>();
    double D=0;
    double f=0;
    double xl=0;
    double xr=0;
    double xm=0;
    double b=0;
    double a=0;
    double v=0.0001;


    public void setT(G_O g_o) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("D:\\小黄片\\软件可靠性\\实验\\2\\src\\test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        double temp=0;
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] T0 = line.split("\\s+");
        temp=Double.valueOf(T0[1]);
        g_o.T.add(temp);
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(line != null){
            String[] numbers = line.split("\\s+");
            temp+=Double.valueOf(numbers[1]);
            g_o.T.add(temp);
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

    public double getD(G_O g_o){
        double result=0;
        double sum=0;
        int n=g_o.T.size();
        for(int i=0;i<n;i++)
        {
            sum += g_o.T.get(i);
        }
        result=sum/(n*g_o.T.get(n-1));
        return result;
    }

    public double getF(G_O g_o,double D){
        double p1,p2;
        double result;
        p1=(1-D*g_o.xm)*(Math.pow(Math.E,g_o.xm));
        p2=(D-1)*g_o.xm-1;
        result=p1+p2;
        return result;
    }

    public double getb(G_O g_o){
        int n=g_o.T.size();
        double result = g_o.xm/(g_o.T.get(n-1));
        return result;
    }

    public double geta(G_O g_o){
        double result,temp1,temp2;
        int n=g_o.T.size();
        temp2=-b*(g_o.T.get(n-1));
        temp1=Math.pow(Math.E,temp2);
        result=n/(1-temp1);
        return result;
    }

    public static void main(String[] args){
        G_O g_o = new G_O();
        g_o.setT(g_o);
        g_o.D=g_o.getD(g_o);
        if(g_o.D>=1.0/2) {
            System.out.println("参数估计无解"+g_o.D);
            return;
        }
        else if(g_o.D>0.0 && g_o.D<1.0/2){
            g_o.xl = (1.0 - 2.0 * g_o.D) / 2.0;
            g_o.xr = 1.0 / g_o.D;
        }
        while(Math.abs(g_o.xr-g_o.xl)>g_o.v){
            g_o.xm = (g_o.xr+g_o.xl)/2.0;
            g_o.f=g_o.getF(g_o,g_o.D);
            if(g_o.f>g_o.v){
                g_o.xl = g_o.xm;
                continue;
            }
            else if(g_o.f<-g_o.v){
                g_o.xr=g_o.xm;
                continue;
            }
            else break;
        }
        g_o.b=g_o.getb(g_o);
        g_o.a=g_o.geta(g_o);

        System.out.println("b= "+g_o.b);
        System.out.println("a= "+g_o.a);
        return;
    }
}
