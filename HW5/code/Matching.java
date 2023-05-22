import java.io.*;

public class Matching
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        HashTable<String, AVLTree<IndexTuple, String>> hashtable = new HashTable<>();
		LinkedList<String, String> fileLine = new LinkedList<>(null);

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;
				command(input, hashtable, fileLine);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input, HashTable<String, AVLTree<IndexTuple, String>> hashTable, LinkedList<String, String> fileLine) throws IOException
	{
		if (input.charAt(0) == '<') {
			readFile(input.substring(input.lastIndexOf(" ") + 1), hashTable, fileLine);
		} else if (input.charAt(0) == '@') {
            printAvlTree(input.substring(input.lastIndexOf(" ") + 1), hashTable);
		} else if (input.charAt(0) == '?') {
			printLinkedList(input.substring(2), hashTable);
		} else if (input.charAt(0) == '+') {
			addNewLine(input.substring(2), hashTable, fileLine);
		} else if (input.charAt(0) == '/') {

		} else {
			throw new IOException(input, null);
		}
	}

	private static void readFile(String filepath, HashTable<String, AVLTree<IndexTuple, String>> hashTable, LinkedList<String, String> fileLine) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        AVLTree<IndexTuple, String> avlTree; 
        int i = 1;
        while((line = reader.readLine()) != null) {
			fileLine.append(line);
            for (int j = 0; j < line.length() - 5; j++) {
                String substring = line.substring(j, j + 6);
                avlTree = hashTable.get(asciiSumModulo(substring));
                if (avlTree == null) {
                    avlTree = new AVLTree(new IndexTuple(i, j+1), substring);
                    hashTable.put(asciiSumModulo(substring), avlTree);
                } else {
                    avlTree.startInsert(new IndexTuple(i, j+1), substring);
                }
            }
            i++;
        }
        reader.close();
	}

    private static int asciiSumModulo(String s) {
        int sum = 0;
        for (char c : s.toCharArray()) {
            sum += (int) c;
        }
        return sum % 100;
    }

    private static void printAvlTree(String hashIndex, HashTable<String, AVLTree<IndexTuple, String>> hashTable) {
        AVLTree<IndexTuple, String> avlTree; 
        avlTree = hashTable.get(Integer.parseInt(hashIndex));
        if (avlTree == null) {
            System.out.println("EMPTY");
        } else {
            avlTree.preOrderPrint();
        }
    }

	private static void printLinkedList(String subString, HashTable<String, AVLTree<IndexTuple, String>> hashTable) {
		AVLTree<IndexTuple, String> avlTree;
		LinkedList<IndexTuple, String> checkList, currentList;
		LinkedListNode<IndexTuple> checkNode, currNode;
		IndexTuple checkNodeIdxTuple;

		avlTree = hashTable.get(asciiSumModulo(subString.substring(0, 6)));
		if (avlTree != null) {
			checkList = avlTree.startSearch(subString.substring(0, 6)).copy();
		} else {
			System.out.println("(0, 0)");
			return;
		}
		for (int i=1; i<subString.length() - 5; i++) {
			avlTree = hashTable.get(asciiSumModulo(subString.substring(i, i + 6)));
			if (avlTree != null) {
				currentList = avlTree.startSearch(subString.substring(i, i + 6));
				if (currentList.numitems == 0 || checkList.numitems == 0 || currentList == null || currentList == avlTree.getNIL()) {
					System.out.println("(0, 0)");
					return;
				}
				checkNode = checkList.head.next;
				for (int j=0; j<checkList.numitems; j++) {
					boolean hasNextSubString = false;
					currNode = currentList.head.next;
					checkNodeIdxTuple = checkNode.indexTuple;
					checkNodeIdxTuple.addIndex2(i);
					for (int k=0; k<currentList.numitems; k++) {
						if (checkNode.indexTuple.compareTo(currNode.indexTuple) == 0) {
							hasNextSubString = true;
						}
						currNode = currNode.next;
					}
					checkNodeIdxTuple.addIndex2(-1 * i);
					if (! hasNextSubString) {
						checkNode = checkNode.next;
						checkList.remove(checkNodeIdxTuple);
					} else {
						checkNode = checkNode.next;
					}
				}
			} else {
				System.out.println("(0, 0)");
				return;
			}
		}
		if (checkList.numitems == 0) {
			System.out.println("(0, 0)");
				return;
		} else {
			checkList.printIndexTuples();
		}
	}

	private static void addNewLine(String newLine ,HashTable<String, AVLTree<IndexTuple, String>> hashTable, LinkedList<String, String> fileLine) {
		AVLTree<IndexTuple, String> avlTree;
		fileLine.append(newLine);
		for (int i = 0; i < newLine.length() - 5; i++) {
			String substring = newLine.substring(i, i + 6);
			avlTree = hashTable.get(asciiSumModulo(substring));
			if (avlTree == null) {
				avlTree = new AVLTree(new IndexTuple(i, i+1), substring);
				hashTable.put(asciiSumModulo(substring), avlTree);
			} else {
				avlTree.startInsert(new IndexTuple(i, i+1), substring);
			}
		}
		System.out.println(fileLine.numitems);
	}
}

class LinkedListNode<T extends Comparable<T>> {
    T indexTuple;
    LinkedListNode<T> next;

    public LinkedListNode(T indexTuple) {
        this.indexTuple = indexTuple;
        this.next = null;
    }
}

class LinkedList<T extends Comparable<T>, V extends Comparable<V>> {
    LinkedListNode<T> head; // dummy head
    V value;
    int numitems;
    int height;
    LinkedList<T, V> left, right;

    public LinkedList(V value) {
        this.head = new LinkedListNode<T>(null);
        this.left = null;
        this.right = null;
        this.value = value;
        numitems = 0;
        height = 1;
    }

    public LinkedList(T indexTuple, V value) {
        this.head = new LinkedListNode<T>(null);
        this.head.next = new LinkedListNode<T>(indexTuple);
        this.left = null;
        this.right = null;
        this.value = value;
        numitems = 0;
        height = 1;
    }

    public LinkedList(T indexTuple, V value, LinkedList<T, V> left, LinkedList<T, V> right, int heigth) {
        this.head = new LinkedListNode<T>(null);
        this.head.next = new LinkedListNode<T>(indexTuple);
        this.left = left;
        this.right = right;
        this.value = value;
        this.height = heigth;
        numitems = 1;
    }

    // Append a new node at the end of the list
    public void append(T indexTuple) {
        LinkedListNode<T> newNode = new LinkedListNode<>(indexTuple);
        LinkedListNode<T> currentNode = this.head;
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        currentNode.next = newNode;
        numitems += 1;
    }

    // Insert a new node at a sorted position in the list
    public void insert(T indexTuple) {
        LinkedListNode<T> newNode = new LinkedListNode<>(indexTuple);
        LinkedListNode<T> currentNode = this.head;
        while (currentNode.next != null && currentNode.next.indexTuple.compareTo(indexTuple) < 0) {
            currentNode = currentNode.next;
        }
        newNode.next = currentNode.next;
        currentNode.next = newNode;
        numitems += 1;
    }


    // Remove a node with the specified indexTuple
    public void remove(T indexTuple) {
        LinkedListNode<T> currentNode = this.head;
        while (currentNode.next != null) {
            if (currentNode.next.indexTuple.equals(indexTuple)) {
                currentNode.next = currentNode.next.next;
                numitems -= 1;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    // Find a node with the specified indexTuple
    public LinkedListNode<T> find(T indexTuple) {
        LinkedListNode<T> currentNode = this.head;
        while (currentNode.next != null) {
            if (currentNode.next.indexTuple.equals(indexTuple)) {
                return currentNode.next;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

	public LinkedList<T, V> copy() {
		// Create a new linked list with the same value
		LinkedList<T, V> newList = new LinkedList<>(this.value);
		newList.height = this.height;
		newList.numitems = this.numitems;
		
		// Copy the linked list
		LinkedListNode<T> currentOriginalNode = this.head.next;
		LinkedListNode<T> currentNewNode = newList.head;
		while (currentOriginalNode != null) {
			LinkedListNode<T> newNode = new LinkedListNode<>(currentOriginalNode.indexTuple);
			currentNewNode.next = newNode;
			currentNewNode = newNode;
			currentOriginalNode = currentOriginalNode.next;
		}
	
		return newList;
	}

	public void printIndexTuples() {
		LinkedListNode<T> currentNode = this.head.next;
		while (currentNode != null) {
			System.out.print(currentNode.indexTuple.toString() + " ");
			currentNode = currentNode.next;
		}
		System.out.println("");
	}

}

class AVLTree<T extends Comparable<T>, V extends Comparable<V>> {
    private final int LL=1, LR=2, RR=3, RL=4, NO_NEED=0, ILLEGAL=-1;
    final LinkedList<T, V> NIL = new LinkedList<>(null, null, null, null, 0);
    private LinkedList<T, V> root;

	public AVLTree() {
		root = NIL;
	}

    public AVLTree(T indexTuple, V value) {
        root = new LinkedList(indexTuple, value, NIL, NIL, 1);
    }

    public LinkedList<T, V> getNIL() {
        return NIL;
    }

	public LinkedList<T, V> startSearch(V value) {
		return searchList(root, value);
	}

	public LinkedList<T, V> searchList(LinkedList<T, V> linkedList, V value) {
		if (linkedList == NIL || linkedList == null) {
			return NIL;
		} else if (value.compareTo(linkedList.value) == 0) {
			return linkedList;
		} else if (value.compareTo(linkedList.value) < 0) {
			return searchList(linkedList.left, value);
		} else {
			return searchList(linkedList.right, value);
		}
	}

	public void preOrderPrint() {
		if (root == null || root == NIL) {
			System.out.println("EMPTY");
		} else {
			System.out.print(this.root.value);
            if (root.left != null && root.left != NIL) {
                preOrderPrint(root.left);
            }
			if (root.right != null && root.right != NIL) {
                preOrderPrint(root.right);
            }
            System.out.println("");
		}
	}

	public void preOrderPrint(LinkedList<T, V> currentList) {
		System.out.print(" " + currentList.value);
        if (currentList.left != null && currentList.left != NIL) {
            preOrderPrint(currentList.left);
        }
		if (currentList.right != null && currentList.right != NIL) {
            preOrderPrint(currentList.right);
        }
	}

    private int needBalance(LinkedList<T, V> linkedlist) {
        int type = ILLEGAL;
        if (linkedlist.left.height+2 <= linkedlist.right.height) {
            if (linkedlist.right.left.height <= linkedlist.right.right.height) {
                type = RR;
            } else {
                type = RL;
                }
        } else if (linkedlist.left.height >= linkedlist.right.height + 2) {
            if (linkedlist.left.left.height >= linkedlist.left.right.height) {
                type = LL;
            } else {
                type = LR;
            }
        } else {
            type = NO_NEED;
        }
        return type;
    }

    private LinkedList<T, V> balanceAVL(LinkedList<T, V> linkedList, int type) {
        LinkedList<T, V> returnList = NIL;
        switch (type) {
            case LL:
                returnList = rotateRight(linkedList);
                break;
            case LR:
                linkedList.left = rotateLeft(linkedList.left);
                returnList = rotateRight(linkedList);
                break;
            case RR:
                returnList = rotateLeft(linkedList);
                break;
            case RL:
                linkedList.right = rotateRight(linkedList);
                returnList = rotateLeft(linkedList);
                break;
        }

        return returnList;
    }

    LinkedList<T, V> rotateRight(LinkedList<T, V> t) {
        LinkedList<T, V> LChild = t.left;
        if (LChild == NIL || LChild == null) {
            return NIL;
        }
        LinkedList<T, V> LRChild = LChild.right;
        LChild.right = t;
        t.left = LRChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        LChild.height = 1 + Math.max(LChild.left.height, LChild.right.height);
        return LChild;
    }
    
    LinkedList<T, V> rotateLeft(LinkedList<T, V> t) {
        LinkedList<T, V> RChild = t.right;
        if (RChild == NIL || RChild == null) {
            return NIL;
        }
        LinkedList<T, V> RLChild = RChild.left;
        RChild.left = t;
        t.right = RLChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);
        return RChild;
    }
    
    void startInsert(T indexTuple, V value) {
        root = insert(root, indexTuple, value);
    }

    LinkedList<T, V> insert(LinkedList<T, V> linkedList, T indexTuple, V value) {
        if (linkedList == NIL || linkedList == null) {
            linkedList =  new LinkedList<>(indexTuple, value, NIL, NIL, 1);
        } else if (value.compareTo(linkedList.value) < 0) {
            linkedList.left = insert(linkedList.left, indexTuple, value);
            linkedList.height = 1 + Math.max(linkedList.left.height, linkedList.right.height);
            int type = needBalance(linkedList);
            if (type != NO_NEED) {
                linkedList = balanceAVL(linkedList, type);
            }
        } else if (value.compareTo(linkedList.value) > 0) {
            linkedList.right = insert(linkedList.right, indexTuple, value);
            linkedList.height = 1 + Math.max(linkedList.left.height, linkedList.right.height);
            int type = needBalance(linkedList);
            if (type != NO_NEED) {
                linkedList = balanceAVL(linkedList, type);
            }
        } else {
            linkedList.insert(indexTuple);
        }
        return linkedList;
    }
}

class HashTable<K, V> {
    private static final int DEFAULT_SIZE = 100;
	private V[] hashTable;
	
	@SuppressWarnings("unchecked")
	public HashTable() {
		this.hashTable = (V[]) new Object[DEFAULT_SIZE];
	}

	public void put(int index, V value) {
        hashTable[index] = value;
    }

	public V get(int index) {
		return hashTable[index];
	}
    
}

class IndexTuple implements Comparable<IndexTuple> {
    private int index1;
    private int index2;

    public IndexTuple(int index1, int index2) {
        this.index1 = index1;
        this.index2 = index2;
    }

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

	public void addIndex2(int addValue) {
		this.index2 += addValue;
	}

    @Override
    public int compareTo(IndexTuple other) {
        if (this.index1 != other.index1) {
            return Integer.compare(this.index1, other.index1);
        }
        return Integer.compare(this.index2, other.index2);
    }

    @Override
    public String toString() {
        return "(" + index1 + ", " + index2 + ")";
    }
}
