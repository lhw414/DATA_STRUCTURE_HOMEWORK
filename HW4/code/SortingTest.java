import java.io.*;
import java.util.*;

public class SortingTest {
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            boolean isRandom = false; // 입력받은 배열이 난수인가 아닌가?
            int[] value; // 입력 받을 숫자들의 배열
            String nums = br.readLine(); // 첫 줄을 입력 받음
            if (nums.charAt(0) == 'r') {
                // 난수일 경우
                isRandom = true; // 난수임을 표시

                String[] nums_arg = nums.split(" ");

                int numsize = Integer.parseInt(nums_arg[1]); // 총 갯수
                int rminimum = Integer.parseInt(nums_arg[2]); // 최소값
                int rmaximum = Integer.parseInt(nums_arg[3]); // 최대값

                Random rand = new Random(); // 난수 인스턴스를 생성한다.

                value = new int[numsize]; // 배열을 생성한다.
                for (int i = 0; i < value.length; i++) // 각각의 배열에 난수를 생성하여 대입
                    value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
            } else {
                // 난수가 아닐 경우
                int numsize = Integer.parseInt(nums);

                value = new int[numsize]; // 배열을 생성한다.
                for (int i = 0; i < value.length; i++) // 한줄씩 입력받아 배열원소로 대입
                    value[i] = Integer.parseInt(br.readLine());
            }

            // 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
            while (true) {
                int[] newvalue = (int[]) value.clone(); // 원래 값의 보호를 위해 복사본을 생성한다.
                char algo = ' ';

                if (args.length == 4) {
                    return;
                }

                String command = args.length > 0 ? args[0] : br.readLine();

                if (args.length > 0) {
                    args = new String[4];
                }

                long t = System.currentTimeMillis();
                switch (command.charAt(0)) {
                    case 'B': // Bubble Sort
                        newvalue = DoBubbleSort(newvalue);
                        break;
                    case 'I': // Insertion Sort
                        newvalue = DoInsertionSort(newvalue);
                        break;
                    case 'H': // Heap Sort
                        newvalue = DoHeapSort(newvalue);
                        break;
                    case 'M': // Merge Sort
                        newvalue = DoMergeSort(newvalue);
                        break;
                    case 'Q': // Quick Sort
                        newvalue = DoQuickSort(newvalue);
                        break;
                    case 'R': // Radix Sort
                        newvalue = DoRadixSort(newvalue);
                        break;
                    case 'S': // Search
                        algo = DoSearch(newvalue);
                        break;
                    case 'X':
                        return; // 프로그램을 종료한다.
                    default:
                        throw new IOException("잘못된 정렬 방법을 입력했습니다.");
                    }
                    if (isRandom) {
                        // 난수일 경우 수행시간을 출력한다.
                        System.out.println((System.currentTimeMillis() - t) + " ms");
                    } else {
                        // 난수가 아닐 경우 정렬된 결과값을 출력한다.
                        if (command.charAt(0) != 'S') {
                            for (int i = 0; i < newvalue.length; i++) {
                                System.out.println(newvalue[i]);
                            }
                        } else {
                            System.out.println(algo);
                        }
                    }
    
                }
            } catch (IOException e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoBubbleSort(int[] value) {
            // Bubble Sort 구현
            for (int i = 0; i < value.length - 1; i++) {
                for (int j = 0; j < value.length - i - 1; j++) {
                    if (value[j] > value[j + 1]) {
                        int temp = value[j];
                        value[j] = value[j + 1];
                        value[j + 1] = temp;
                    }
                }
            }
            return value;
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoInsertionSort(int[] value) {
            // Insertion Sort 구현
            for (int i = 1; i < value.length; i++) {
                int key = value[i];
                int j = i - 1;
                while (j >= 0 && value[j] > key) {
                    value[j + 1] = value[j];
                    j--;
                }
                value[j + 1] = key;
            }
            return value;
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoHeapSort(int[] value) {
            // Heap Sort 구현
            PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
            for (int num : value) {
                pq.add(num);
            }
            for (int i = 0; i < value.length; i++) {
                value[i] = pq.poll();
            }
            return value;
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoMergeSort(int[] value) {
            // Merge Sort 구현
            if (value.length <= 1) {
                return value;
            }
            int mid = value.length / 2;
            int[] left = Arrays.copyOfRange(value, 0, mid);
            int[] right = Arrays.copyOfRange(value, mid, value.length);
            left = DoMergeSort(left);
            right = DoMergeSort(right);
            return merge(left, right);
        }
    
        private static int[] merge(int[] left, int[] right) {
            int[] result = new int[left.length + right.length];
            int i = 0, j = 0, k = 0;
            while (i < left.length && j < right.length) {
                if (left[i] <= right[j]) {
                    result[k++] = left[i++];
                } else {
                    result[k++] = right[j++];
                }
            }
            while (i < left.length) {
                result[k++] = left[i++];
            }
            while (j < right.length) {
                result[k++] = right[j++];
            }
            return result;
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoQuickSort(int[] value){
            return quickSort(value, 0, value.length - 1);
        }

        private static int[] quickSort(int[] arr, int left, int right) {
            if (left < right) {
                int pivotIndex = partition(arr, left, right);
                quickSort(arr, left, pivotIndex - 1);
                quickSort(arr, pivotIndex + 1, right);
            }
            return arr;
        }

        private static int partition(int[] arr, int left, int right) {
            int pivot = arr[right];
            int i = left - 1;
            for (int j = left; j < right; j++) {
                if (arr[j] < pivot) {
                    i++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            int temp = arr[i + 1];
            arr[i + 1] = arr[right];
            arr[right] = temp;
            return i + 1;
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////


        private static int[] DoRadixSort(int[] arr) {
            if (arr.length <= 1) {
                return arr;
            }
    
            // Find the maximum and minimum numbers in one pass
            int max = arr[0], min = arr[0];
            for (int num : arr) {
                if (num > max) {
                    max = num;
                } else if (num < min) {
                    min = num;
                }
            }
    
            // Offset for handling negative numbers
            int offset = -min;
    
            // Adding the offset to all the numbers in the array
            for (int i = 0; i < arr.length; i++) {
                arr[i] += offset;
            }
    
            // Find the maximum number to know the number of digits
            int maxDigits = Integer.toString(max + offset).length();
    
            // Perform counting sort for every digit
            for (int exp = 1; maxDigits > 0; exp *= 10, maxDigits--) {
                countingSort(arr, exp);
            }
    
            // Subtract the offset from all the numbers in the array
            for (int i = 0; i < arr.length; i++) {
                arr[i] -= offset;
            }
    
            return arr;
        }
    
        private static void countingSort(int[] arr, int exp) {
            int n = arr.length;
            int[] output = new int[n];
            int[] count = new int[10];
    
            // Store count of occurrences in count[]
            for (int num : arr) {
                count[(num / exp) % 10]++;
            }
    
            // Change count[i] so that count[i] now contains actual position of this digit in output[]
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
    
            // Build the output array
            for (int i = n - 1; i >= 0; i--) {
                output[count[(arr[i] / exp) % 10] - 1] = arr[i];
                count[(arr[i] / exp) % 10]--;
            }
    
            // Copy the output array to arr[], so that arr[] now contains sorted numbers according to current digit
            System.arraycopy(output, 0, arr, 0, n);
        }
    
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static char DoSearch(int[] value) {
            // Search 구현
            if (isSorted(value)) {
                return 'B';
            }
            int[] temp = Arrays.copyOf(value, value.length);
            DoInsertionSort(temp);
            if (isSame(value, temp)) {
                return 'I';
            }
            temp = Arrays.copyOf(value, value.length);
            DoHeapSort(temp);
            if (isSame(value, temp)) {
                return 'H';
            }
            temp = Arrays.copyOf(value, value.length);
            DoMergeSort(temp);
            if (isSame(value, temp)) {
                return 'M';
            }
            temp = Arrays.copyOf(value, value.length);
            DoQuickSort(temp);
            if (isSame(value, temp)) {
                return 'Q';
            }
            temp = Arrays.copyOf(value, value.length);
            DoRadixSort(temp);
            if (isSame(value, temp)) {
                return 'R';
            }
            return 'X';
        }
    
        private static boolean isSorted(int[] value) {
            for (int i = 1; i < value.length; i++) {
                if (value[i - 1] > value[i]) {
                    return false;
                }
            }
            return true;
        }

        private static boolean isSame(int[] a, int[] b) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) {
                    return false;
                }
            }
            return true;
        }

}

