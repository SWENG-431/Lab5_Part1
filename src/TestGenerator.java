import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.List;
import java.util.HashMap;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class TestGenerator
{
    private static HashMap<Integer, List<String>> eqClasses = new HashMap<>();

    public static void main(String[] args) throws Exception, ValueError
    {
        // Read the Eq.txt file to get the equivalence classes for each input argument.
        readEqFile("C:\\Users\\brian\\Documents\\Summer 2023\\SWENG 411\\Lab5_Part1\\Resources\\Eq.txt");

        // Generate a random value for each input argument, such that the value belongs to
        // one of the equivalence classes for that argument.
        int numInputs = eqClasses.size();
        int[] inputValues = new int[numInputs];
        for (int i = 0; i < numInputs; i++)
        {
            List<String> eqClass = eqClasses.get(i + 1);
            int size = eqClass.size();
            String inputValueStr = eqClass.get(size - 1);      //Store the rightmost class in the string (e.g. 16, 30 for n = 1)
            String []val = inputValueStr.split(", ");   //Split the class into individual values (val[0] = 16 for n = 1)
            inputValueStr = val[1];
            inputValues[i] = new Random().nextInt(Integer.parseInt(inputValueStr) - 1) + 1;
        }

        // Compute the expected output for the function foo using the check function for all
        // input arguments.
        int expectedOutput = 0;
        int index = 0;
        for (int inputValue : inputValues)
        {
            expectedOutput += check(inputValue, index);
            index++;
        }

        // Write the test case to the test.txt file, including the input argument values and
        // the expected output.
        writeTestCase("C:\\Users\\brian\\Documents\\Summer 2023\\SWENG 411\\Lab5_Part1\\Resources\\test.txt", inputValues, expectedOutput);
    }

    private static void readEqFile(String eqFile) throws Exception
    {
        BufferedReader reader = new BufferedReader(new FileReader(eqFile));
        String line;
        while ((line = reader.readLine()) != null)
        {
            String[] tokens = line.split(": ");
            int key = Integer.parseInt(tokens[0]);
            List<String> values = List.of(tokens[1].split("; "));
            eqClasses.put(key, values);
        }
        reader.close();
    }

    private static int check(int val, int index) throws ValueError
    {
        boolean found = false;
        int classNum = 1;
        int iterations = 0;
        for (List<String> eqClass : eqClasses.values())
        {
            if (iterations == index)
            {
                for (String var : eqClass)
                {
                    String[] classArr = var.split(", ");
                    String limit = classArr[1];

                    if (Integer.parseInt(limit) >= val)
                    {
                        found = true;
                        break;
                    } else
                        classNum++;
                }
            }
            else
                iterations++;

            if (found)
                break;
        }
        if (!found)
            throw new ValueError(val);

        return classNum;
    }

    private static void writeTestCase(String testFile, int[] inputValues, int expectedOutput) throws Exception
    {
        FileWriter writer = new FileWriter(testFile, true);
        writer.write(String.join(",", Arrays.stream(inputValues).mapToObj(String::valueOf).collect(Collectors.toList())));
        writer.write("," + expectedOutput + "\n");
        writer.close();
    }
}