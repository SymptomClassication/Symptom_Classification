import java.util.Arrays;


//TODO ONLY SKELETON CODE FOR NOW
public class HistogramPage 
{
    public void test()
    {
        int[] data = {5, 10, 15, 20, 25};
        // find the maximum data value
        int maxDataValue = Arrays.stream(data).max().getAsInt();
        // iterate over each row of the histogram
        for (int row = maxDataValue; row >= 1; row--) {
            // iterate over each data point
            for (int i = 0; i < data.length; i++) {
                // check if this data point should be included in this row
                if (data[i] >= row) {
                    System.out.print("* ");
                } 
                else 
                {
                    System.out.print("  ");
                }
            }
            // move to the next row
            System.out.println();
        }
    }
}

