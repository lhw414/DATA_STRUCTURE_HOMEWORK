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
        
        /**
         * 
         * 
         * 
         */
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoBubbleSort(int[] value) {
            int temp;

            for (int i = 0; i < value.length - 1; i++) {
                for (int j = 0; j < value.length - i - 1; j++) {
                    if (value[j] > value[j + 1]) {
                        temp = value[j];
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
            buildMaxHeap(value);
            int n = value.length;
            for (int i = n - 1; i >= 1; i--) {
                int temp = value[0];
                value[0] = value[i];
                value[i] = temp;
                percolateDown(value, 0, i);
            }

            return value;
        }
    
        private static void buildMaxHeap(int[] array) {
            int n = array.length;
            for (int i = (n - 2) / 2; i >= 0; i--) {
                percolateDown(array, i, n);
            }
        }
    
        private static void percolateDown(int[] array, int k, int n) {
            int leftChild = 2 * k + 1;
            int rightChild = 2 * k + 2;
    
            if (leftChild >= n) {
                return;
            }
    
            int maxChild = leftChild;
    
            if (rightChild < n && array[leftChild] < array[rightChild]) {
                maxChild = rightChild;
            }
    
            if (array[k] < array[maxChild]) {
                int temp = array[k];
                array[k] = array[maxChild];
                array[maxChild] = temp;
                percolateDown(array, maxChild, n);
            }
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoMergeSort(int[] value){
            int[] switchingValue = new int[value.length];
            for (int i = 0; i < value.length; i++){
                switchingValue[i] = value[i];
            }
            switchingMergeSort(0, value.length-1, value, switchingValue);
            return (value);
        }
    
        private static void switchingMergeSort(int p, int r, int[] value, int[] switchingValue){
            if (p < r){
                int q = (p+r)/2;
                switchingMergeSort(p, q, switchingValue, value);
                switchingMergeSort(q+1, r, switchingValue, value);
                switchingMerge(p, q, r, switchingValue, value);
            }
        }
    
        private static void switchingMerge(int p, int q, int r, int[] arr1, int[] arr2){
            int i = p, j = q+1, t = p;
            while (i <= q && j <= r){
                if (arr1[i] <= arr1[j]){
                    arr2[t++] = arr1[i++];
                } else{
                    arr2[t++] = arr1[j++];
                }
            }
            while (i <= q){
                arr2[t++] = arr1[i++];
            }
            while (j <= r){
                arr2[t++] = arr1[j++];
            }
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
			int[] sortedArr = Arrays.copyOf(arr, arr.length);
	
			int[] negArr = new int[sortedArr.length];
			int[] posArr = new int[sortedArr.length];
	
			int negIndex = 0;
			int posIndex = 0;
	
			for (int num : sortedArr) {
				if (num < 0) {
					negArr[negIndex++] = num;
				} else {
					posArr[posIndex++] = num;
				}
			}
	
			int offset = 0;
			if (negIndex > 0) {
				offset = -1 * (getMin(negArr, negIndex) - 1);
				for (int i = 0; i < negIndex; i++) {
					negArr[i] += offset;
				}
	
				radixSortPositive(negArr, negIndex);
			}
	
			if (posIndex > 0) {
				radixSortPositive(posArr, posIndex);
			}
	
			for (int i = 0; i < negIndex; i++) {
				negArr[i] -= offset;
				sortedArr[i] = negArr[i];
			}
	
			for (int i = 0; i < posIndex; i++) {
				sortedArr[i + negIndex] = posArr[i];
			}
	
			return sortedArr;
		}

		private static void radixSortPositive(int[] arr, int size) {
			int max = getMax(arr, size);
	
			for (int exp = 1; max / exp > 0; exp *= 10) {
				countingSort(arr, size, exp);
			}
		}
	
		private static void countingSort(int[] arr, int size, int exp) {
			int[] output = new int[size];
			int[] count = new int[10];
	
			for (int i = 0; i < size; i++) {
				count[(arr[i] / exp) % 10]++;
			}
	
			for (int i = 1; i < 10; i++) {
				count[i] += count[i - 1];
			}
	
			for (int i = size - 1; i >= 0; i--) {
				output[count[(arr[i] / exp) % 10] - 1] = arr[i];
				count[(arr[i] / exp) % 10]--;
			}
	
			System.arraycopy(output, 0, arr, 0, size);
		}
	
		private static int getMax(int[] arr, int size) {
			int max = arr[0];
	
			for (int i = 1; i < size; i++) {
				if (arr[i] > max) {
					max = arr[i];
				}
			}
	
			return max;
		}
	
		private static int getMin(int[] arr, int size) {
			int min = arr[0];
	
			for (int i = 1; i < size; i++) {
				if (arr[i] < min) {
					min = arr[i];
				}
			}
	
			return min;
		}
    
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static char DoSearch(int[] value) {

            int maxValue = getMax(value, value.length);
            int minValue = getMin(value, value.length);

            double sortedRate;

            if (value == null || value.length < 2) {
                sortedRate = 1.0;
            } else {

                int sortedPairsCount = 0;

                for (int i = 0; i < value.length - 1; i++) {
                    if (value[i] <= value[i + 1]) {
                        sortedPairsCount++;
                    }
                }

                sortedRate = (double) sortedPairsCount / (value.length - 1);
            }

            if (sortedRate == 1.0) {
                DoInsertionSort(value);
                return 'I';
            }

            int hashTableSize = maxValue - minValue;
            int collisions = 0;
            Hashtable<Integer, Integer> hashTable = new Hashtable<>(hashTableSize);

            for (int num : value) {
                int hash_num = num - minValue;
                int hash = hash_num % hashTableSize;
                if (hashTable.containsKey(hash)) {
                    collisions++;
                } else {
                    hashTable.put(hash, hash_num);
                }
            }

            double collisionRate = (double) collisions / value.length;
            System.out.println(collisionRate);
            if(collisionRate > 0.99999) {
                DoMergeSort(value);
                return 'H';
            }

            int digits = Math.max(Integer.toString(Math.abs(maxValue)).length(), Integer.toString(Math.abs(minValue)).length());
            
            if (digits <= 2 && digits <= 0.125 * (Math.log(value.length) / Math.log(2))) {
                DoRadixSort(value);
                return 'R';
            }

            DoQuickSort(value);
            return 'M';
        }

}

