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
        
        /** Reference start ~
         * ! REFERENCE : GPT - get sorting algorithms' skeleton code and implement simple sorting algorithms
         * @reference content
         *   - Get skeleton code and logic from GPT.
         *   - Implement simple sorting algorithms : DoBubbleSort, DoInsertionSort, Doheapsort
         *   - Implement skeleton code for DoQuickSort, DoMergeSort, DoRadixSort                         
         */
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoBubbleSort(int[] value) { // 버블 정렬 수행
            int temp;

            for (int i = 0; i < value.length - 1; i++) {
                for (int j = 0; j < value.length - i - 1; j++) {
                    if (value[j] > value[j + 1]) { // 앞 원소가 더 클 시, 값 교환
                        temp = value[j];
                        value[j] = value[j + 1];
                        value[j + 1] = temp;
                    }
                }
            }
            return value;
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoInsertionSort(int[] value) { // 선택 정렬 수행
            // Insertion Sort 구현
            for (int i = 1; i < value.length; i++) {
                int key = value[i];
                int j = i - 1;
                while (j >= 0 && value[j] > key) { // 배열이 정렬되도록 원소 삽입
                    value[j + 1] = value[j];
                    j--;
                }
                value[j + 1] = key;
            }
            return value;
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoHeapSort(int[] value) { // 힙 정렬 수행
            buildMaxHeap(value);
            int n = value.length;
            for (int i = n - 1; i >= 1; i--) { // max 값 찾은 후, percolate down 실행
                int temp = value[0];
                value[0] = value[i];
                value[i] = temp;
                percolateDown(value, 0, i);
            }

            return value;
        }
    
        private static void buildMaxHeap(int[] array) { // 임의의 배열에 maxheap build
            int n = array.length;
            for (int i = (n - 2) / 2; i >= 0; i--) {
                percolateDown(array, i, n);
            }
        }
    
        private static void percolateDown(int[] array, int k, int n) { // recursive하게 child node로 이동하여 max heap 수선
            int leftChild = 2 * k + 1;
            int rightChild = 2 * k + 2;
    
            if (leftChild >= n) {
                return;
            }
    
            int maxChild = leftChild;
    
            if (rightChild < n && array[leftChild] < array[rightChild]) {
                maxChild = rightChild;
            }
    
            if (array[k] < array[maxChild]) { // child node가 더 클 시, 값 교환
                int temp = array[k];
                array[k] = array[maxChild];
                array[maxChild] = temp;
                percolateDown(array, maxChild, n);
            }
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static int[] DoMergeSort(int[] value){ // 배열 할당으로 인한 수행 시간 증가를 줄이기 위해, switching merge sort 구현
            int[] switchingValue = new int[value.length];
            for (int i = 0; i < value.length; i++){
                switchingValue[i] = value[i]; // 보조 배열(switching 배열)에 value값 복사
            }
            switchingMergeSort(0, value.length-1, value, switchingValue);
            return (value);
        }
    
        private static void switchingMergeSort(int p, int r, int[] value, int[] switchingValue){ // switching merge 및 mergesort 시 두 개의 배열을 교차하여 전달
            if (p < r){
                int q = (p+r)/2;
                switchingMergeSort(p, q, switchingValue, value);
                switchingMergeSort(q+1, r, switchingValue, value);
                switchingMerge(p, q, r, switchingValue, value);
            }
        }
    
        private static void switchingMerge(int p, int q, int r, int[] arr1, int[] arr2){
            int i = p, j = q+1, t = p;
            while (i <= q && j <= r){ // 보조 배열에 정렬값 대입
                if (arr1[i] <= arr1[j]){
                    arr2[t++] = arr1[i++];
                } else{
                    arr2[t++] = arr1[j++];
                }
            }
            while (i <= q){ // 남은 값 복사
                arr2[t++] = arr1[i++];
            }
            while (j <= r){ // 남은 값 복사
                arr2[t++] = arr1[j++];
            }
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////

        private static int[] DoQuickSort(int[] array) { // 퀵 정렬 수행
            quickSort(array, 0, array.length - 1);

            return array;
        }
    
        private static void quickSort(int[] array, int low, int high) { // 3-way partition 구현
            if (low < high) {
                int[] partitionIndices = partition(array, low, high);
                int left = partitionIndices[0];
                int right = partitionIndices[1];
                quickSort(array, low, left - 1);
                quickSort(array, right + 1, high);
            }
        }
    
        private static int[] partition(int[] array, int low, int high) {
            int pivot = array[low];
            int left = low;
            int i = low + 1;
            int right = high;
    
            while (i <= right) {
                if (array[i] < pivot) { // pivot보다 작을시, pivot의 왼쪽인 1구역
                    int temp = array[i];
                    array[i] = array[left];
                    array[left] = temp;
                    left++;
                    i++;
                } else if (array[i] > pivot) { //pivot 보다 클 시, pivot의 오른쪽인 2구역
                    int temp = array[i];
                    array[i] = array[right];
                    array[right] = temp;
                    right--;
                } else { // pivot과 같을 시, pivot과 동일한 위치인 3구역
                    i++;
                }
            }
    
            return new int[] { left, right };
        }
    
        ////////////////////////////////////////////////////////////////////////////////////////////////


        private static int[] DoRadixSort(int[] arr) {
			int[] negArr = new int[arr.length];
			int[] posArr = new int[arr.length];
	
			int negIndex = 0;
			int posIndex = 0;
            int offset = -1 * getMin(arr, arr.length);
			for (int num : arr) {
				if (num < 0) {
					negArr[negIndex++] = num + offset; // counting sort를 위해 offset울 이용한 postive array 생성
				} else {
					posArr[posIndex++] = num;
				}
			}
	
			if (negIndex > 0) {
				radixSortPositive(negArr, negIndex); // postive array
			}
	
			if (posIndex > 0) {
				radixSortPositive(posArr, posIndex); // negative array
			}
	
			for (int i = 0; i < negIndex; i++) {
				arr[i] = negArr[i] - offset; // 원래의 값으로 복원 및 return array에 복사
			}
	
			for (int i = 0; i < posIndex; i++) {
				arr[i + negIndex] = posArr[i]; // return array에 이어붙임
			}
	
			return arr;
		}

		private static void radixSortPositive(int[] arr, int size) {
			int max = getMax(arr, size);
	
			for (int exp = 1; max / exp > 0; exp *= 10) { // 각 자리수에 대해 counting sort 수행
				countingSort(arr, size, exp);
			}
		}
	
		private static void countingSort(int[] arr, int size, int exp) { // 자릿수를 이용한 counting sort 진행
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

            for (int i = 0; i < size; i++) {
                arr[i] = output[i];
            }

		}
	
		private static int getMax(int[] arr, int size) { // 배열의 최대값 리턴
			int max = arr[0];
	
			for (int i = 1; i < size; i++) {
				if (arr[i] > max) {
					max = arr[i];
				}
			}
	
			return max;
		}
	
		private static int getMin(int[] arr, int size) { // 배열의 최소값 리턴
			int min = arr[0];
	
			for (int i = 1; i < size; i++) {
				if (arr[i] < min) {
					min = arr[i];
				}
			}
	
			return min;
		}
        // ! ~ Reference end : GPT - get sorting algorithms' skeleton code and implement simple sorting algorithms
    
        ////////////////////////////////////////////////////////////////////////////////////////////////
        private static char DoSearch(int[] value) {

            int maxValue = getMax(value, value.length);
            int minValue = getMin(value, value.length);

            // sortedRate 계산
            double sortedRate;

            if (value == null || value.length < 2) {
                sortedRate = 1.0;
            } else {

                int sortedPairsCount = 0;

                for (int i = 0; i < value.length - 1; i++) {
                    if (value[i] <= value[i + 1]) { // i번째 원소가 i+1번째 원소보다 작거나 같을때, sortedPair 증가
                        sortedPairsCount++;
                    }
                }

                sortedRate = (double) sortedPairsCount / (value.length - 1); // 비율 계산
            }

            if (sortedRate == 1.0) { // 만약 정렬되어 있는 배열이라면, Insertion sort 추천
                return 'I';
            }
            
            // collisionRate 계산
            int hashMapSize = maxValue - minValue;
            int collisions = 0;
            HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>(hashMapSize); // Ref : 자바 HashMap 사용 : HashMap을 이용하여 값의 중복도 확인

            for (int num : value) {
                if (hashMap.containsKey(num)) { // 만약 키 값이 이미 존재하면, collisions 증가
                    collisions++;
                } else { // 키 값이 없을시, 키 값 추가
                 hashMap.put(num, num);
                }
            }

            double collisionRate = (double) collisions / value.length; // 비율 계산

            int digits = Math.max(Integer.toString(Math.abs(maxValue)).length(), Integer.toString(Math.abs(minValue)).length()); // 배열의 최대 자릿수 계산
            if (digits <= 0.241 * (Math.log(value.length) / Math.log(2)) && collisionRate <= 0.9986) { // 자릿수가 0.25 * log2(n)보다 작고, collisionRate가 0.99801보다 작다면, radix sort 추천
                return 'R';
            }

            // 그 외의 경우에는, quick sort 추천
            return 'Q';
        }

}

