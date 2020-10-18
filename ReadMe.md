####Description
Sample program to compare the time taken for determining Top K elements in N sized array using Min heap and Max heap approach.
- Compares the values when ranges from 1 to N
- For each k, average of 10 iterations is shown in the result

#### Steps to Run
- By default, the program executes for 1M records. Run with the following parameters to negate the effect of GC.
```
java -Xms6G -Xmx6G -XX:NewSize=5G -XX:MaxNewSize=5G TopKElements
```
- You can also pass the input array size as parameter. But if you increase the array size, you need to increase memory parameters accordingly.
````
java -Xms12G -Xmx12G -XX:NewSize=11G -XX:MaxNewSize=11G TopKElements 10000000
````