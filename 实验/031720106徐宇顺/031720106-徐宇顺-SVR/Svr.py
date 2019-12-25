#-*-coding:utf-8-*- 
import numpy as np  
from sklearn.svm import SVR  
import matplotlib.pyplot as plt

from scipy.interpolate import spline

filename = 'D:\\小黄片\\软件可靠性\\SYS1(failue_count).txt'
with open(filename) as file_object:
   lines = file_object.readlines()
listdata=[]
for line in lines:
   data1=[]
   linestr = line.strip()
   linestrlist = linestr.split("\t")
   linelist = [int(i) for i in linestrlist]
   data1.append(linelist[1])
   listdata.append(data1)
   del data1
###############################################################################  
# Generate sample data  
X = np.sort(listdata, axis=0)    
y = np.sin(X).ravel()   #np.sin()输出的是列，和X对应，ravel表示转换成行  

###############################################################################  
# Add noise to targets  
y[::4] +=(0.5 - np.random.rand(34))

###############################################################################  
# Fit regression model  
svr_rbf10 = SVR(kernel='rbf',C=100, gamma=10.0)  
svr_rbf1 = SVR(kernel='rbf', C=100, gamma=0.1)  
svr_rbf1 = SVR(kernel='rbf', C=100, gamma=0.1)  
#svr_lin = SVR(kernel='linear', C=1e3)  
#svr_poly = SVR(kernel='poly', C=1e3, degree=3)  
y_rbf10 = svr_rbf10.fit(X, y).predict(X)  
y_rbf1 = svr_rbf1.fit(X, y).predict(X) 
#y_lin = svr_lin.fit(X, y).predict(X)  
#y_poly = svr_poly.fit(X, y).predict(X)  

###############################################################################  
# look at the results  
lw = 2 #line width  
plt.scatter(X, y, color='darkorange', label='data')

plt.plot(X, y,color='red',linewidth=1,label='Real line')

plt.plot(X, y_rbf10, color='green', lw=lw, label='RBF gamma=10.0')  
#plt.plot(X, y_rbf1, color='yellow', lw=lw, label='RBF gamma=1.0')  
#plt.plot(X, y_lin, color='c', lw=lw, label='Linear model')  
#plt.plot(X, y_poly, color='cornflowerblue', lw=lw, label='Polynomial model')


plt.xlabel('data')  
plt.ylabel('target')  
plt.title('SVR')  
plt.legend()  
plt.show()
