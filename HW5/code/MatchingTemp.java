import java.io.*;

public class MatchingTemp
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        HashTable<String, AVLTree<String, LinkedList<IndexTuple>>> hashtable = new HashTable<>();
		LinkedList<String> fileLine = new LinkedList<>();

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

	private static void command(String input, HashTable<String, AVLTree<String, LinkedList<IndexTuple>>> hashTable, LinkedList<String> fileLine) throws IOException
	{
		if (input.charAt(0) == '<') {
			readFile(input.substring(input.lastIndexOf(" ") + 1), hashTable, fileLine);
		} else if (input.charAt(0) == '@') {
            printAvlTree(input.substring(input.lastIndexOf(" ") + 1), hashTable);
		} else if (input.charAt(0) == '?') {
			printLinkedList(input.substring(2), hashTable);
		} else if (input.charAt(0) == '+') {
			//addNewLine(input.substring(2), hashTable, fileLine);
		} else if (input.charAt(0) == '/') {

		} else {
			throw new IOException(input, null);
		}
	}

	private static void readFile(String filepath, HashTable<String, AVLTree<String, LinkedList<IndexTuple>>> hashTable, LinkedList<String> fileLine) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        AVLTree<String, LinkedList<IndexTuple>> avlTree;
        AVLNode<String, LinkedList<IndexTuple>> avlNode;
        int i = 1;
        while((line = reader.readLine()) != null) {
			fileLine.append(line);
            for (int j = 0; j < line.length() - 5; j++) {
                String substring = line.substring(j, j + 6);
                avlTree = hashTable.get(asciiSumModulo(substring));
                if (avlTree == null) {
                    avlTree = new AVLTree<>();
                    avlTree.startInsert(new AVLNode(substring, new LinkedList<>(new IndexTuple(i, j+1))));
                    hashTable.put(asciiSumModulo(substring), avlTree);
                } else {
                    if (avlTree.startSearch(substring) != AVLTree.NIL) {
                        avlNode = avlTree.startSearch(substring);
                        avlNode.item.insert(new IndexTuple(i, j+1));
                    } else {
                        avlTree.startInsert(new AVLNode(substring, new LinkedList<>(new IndexTuple(i, j+1))));
                    }
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

    private static void printAvlTree(String hashIndex, HashTable<String, AVLTree<String, LinkedList<IndexTuple>>> hashTable) {
        AVLTree<String, LinkedList<IndexTuple>> avlTree; 
        avlTree = hashTable.get(Integer.parseInt(hashIndex));
        if (avlTree == null) {
            System.out.println("EMPTY");
        } else {
            avlTree.preOrderPrint();
        }
    }

	private static void printLinkedList(String subString, HashTable<String, AVLTree<String, LinkedList<IndexTuple>>> hashTable) {
		try{
			AVLTree<String, LinkedList<IndexTuple>> avlTree;
			LinkedList<IndexTuple> checkList, currentList;
			LinkedListNode<IndexTuple> checkNode, currNode;
			IndexTuple checkNodeIdxTuple;

			avlTree = hashTable.get(asciiSumModulo(subString.substring(0, 6)));
			if (avlTree != null) {
				checkList = avlTree.startSearch(subString.substring(0, 6)).item.copy();
			} else {
				System.out.println("(0, 0)");
				return;
			}

			for (int i=1; i<subString.length() - 5; i++) {
				avlTree = hashTable.get(asciiSumModulo(subString.substring(i, i + 6)));
				if (avlTree != null) {
					currentList = avlTree.startSearch(subString.substring(i, i + 6)).item;
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
		} catch (NullPointerException e) {
			System.out.println("(0, 0)");
		}
	}

	// private static void addNewLine(String newLine, HashTable<String, AVLTree<String, LinkedList<IndexTuple>>> hashTable, LinkedList<String> fileLine) {
	// 	AVLTree<String, LinkedList<IndexTuple>> avlTree;
    //     AVLNode<String, LinkedList<IndexTuple>> avlNode;
	// 	fileLine.append(newLine);
	// 	for (int i = 0; i < newLine.length() - 5; i++) {
	// 		String substring = newLine.substring(i, i + 6);
	// 		avlTree = hashTable.get(asciiSumModulo(substring));
	// 		if (avlTree == null) {
    //             avlTree = new AVLTree<>();
    //             avlTree.startInsert(new AVLNode(substring, new LinkedList<>(new IndexTuple(i, j+1))));
    //             hashTable.put(asciiSumModulo(substring), avlTree);
    //         } else {
    //             if (avlTree.startSearch(substring) != AVLTree.NIL) {
    //                 avlNode = avlTree.startSearch(substring);
    //                 avlNode.item.insert(new IndexTuple(i, j+1));
    //             } else {
    //                 avlTree.startInsert(new AVLNode(substring, new LinkedList<>(new IndexTuple(i, j+1))));
    //             }
    //         }
	// 	}
	// 	System.out.println(fileLine.numitems);
	// }
}

class LinkedListNode<T extends Comparable<T>> {
    T indexTuple;
    LinkedListNode<T> next;

    public LinkedListNode(T indexTuple) {
        this.indexTuple = indexTuple;
        this.next = null;
    }
}

class LinkedList<T extends Comparable<T>> {
    LinkedListNode<T> head; // dummy head
    int numitems;

    public LinkedList() {
        this.head = new LinkedListNode<T>(null);
        this.head.next = new LinkedListNode<T>(null);
        numitems = 0;
    }

    public LinkedList(T indexTuple) {
        this.head = new LinkedListNode<T>(null);
        this.head.next = new LinkedListNode<T>(indexTuple);
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

	public LinkedList<T> copy() {
		// Create a new linked list with the same value
		LinkedList<T> newList = new LinkedList<>();
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

class AVLNode<T extends Comparable<T>, V> {
    public T key;
    public AVLNode<T, V> left, right;
    public int height;
    public V item;

    public AVLNode(T key, V item) {
        this.key = key;
        this.item = item;
        this.left = this.right = AVLTree.NIL;
        this.height = 1;
    }

    public AVLNode(T key, V item, AVLNode<T, V> leftChild, AVLNode<T, V> rightChild, int height) {
        this.key = key;
        this.item = item;
        this.left = leftChild;
        this.right = rightChild;
        this.height = height;
    }
}

class AVLTree<T extends Comparable<T>, V> {
    private final int LL=1, LR=2, RR=3, RL=4, NO_NEED=0, ILLEGAL=-1;
    public static final AVLNode NIL = new AVLNode(null, null, null, null, 0);
    private AVLNode<T, V> root;

	public AVLTree() {
		root = NIL;
	}

	public AVLNode<T, V> startSearch(T value) {
		return searchList(root, value);
	}

	public AVLNode<T, V> searchList(AVLNode<T, V> node, T value) {
		if (node == NIL) {
			return NIL;
		} else if (value.compareTo(node.key) == 0) {
			return node;
		} else if (value.compareTo(node.key) < 0) {
			return searchList(node.left, value);
		} else {
			return searchList(node.right, value);
		}
	}

	public void preOrderPrint() {
		if (root == NIL) {
			System.out.println("EMPTY");
		} else {
			System.out.print(this.root.key);
            if (root.left != NIL) {
                preOrderPrint(root.left);
            }
			if (root.right != NIL) {
                preOrderPrint(root.right);
            }
            System.out.println("");
		}
	}

	public void preOrderPrint(AVLNode<T, V> node) {
		System.out.print(" " + node.key);
        if (node.left != NIL) {
            preOrderPrint(node.left);
        }
		if (node.right != NIL) {
            preOrderPrint(node.right);
        }
	}

    private int needBalance(AVLNode<T, V> node) {
        int type = ILLEGAL;
        if (node.left.height + 2 <= node.right.height) {
            if (node.right.left.height <= node.right.right.height) {
                type = RR;
            } else {
                type = RL;
                }
        } else if (node.left.height >= node.right.height + 2) {
            if (node.left.left.height >= node.left.right.height) {
                type = LL;
            } else {
                type = LR;
            }
        } else {
            type = NO_NEED;
        }
        return type;
    }

    private AVLNode<T, V> balanceAVL(AVLNode<T, V> node, int type) {
        AVLNode<T, V> returnList = NIL;
        switch (type) {
            case LL:
                returnList = rotateRight(node);
                break;
            case LR:
                node.left = rotateLeft(node.left);
                returnList = rotateRight(node);
                break;
            case RR:
                returnList = rotateLeft(node);
                break;
            case RL:
                node.right = rotateRight(node);
                returnList = rotateLeft(node);
                break;
        }

        return returnList;
    }

    AVLNode<T, V> rotateRight(AVLNode<T, V> t) {
        AVLNode<T, V> LChild = t.left;
        if (LChild == NIL) {
            return NIL;
        }
        AVLNode<T, V> LRChild = LChild.right;
        LChild.right = t;
        t.left = LRChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        LChild.height = 1 + Math.max(LChild.left.height, LChild.right.height);
        return LChild;
    }
    
    AVLNode<T, V> rotateLeft(AVLNode<T, V> t) {
        AVLNode<T, V> RChild = t.right;
        if (RChild == NIL || RChild == null) {
            return NIL;
        }
        AVLNode<T, V> RLChild = RChild.left;
        RChild.left = t;
        t.right = RLChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);
        return RChild;
    }
    
    void startInsert(AVLNode<T, V> newNode) {
        root = insert(root, newNode);
    }

    AVLNode<T, V> insert(AVLNode<T, V> pNode, AVLNode<T, V> newNode) {
        if (pNode == NIL) {
            pNode = newNode;
        } else if (newNode.key.compareTo(pNode.key) < 0) {
            pNode.left = insert(pNode.left, newNode);
            pNode.height = 1 + Math.max(pNode.left.height, pNode.right.height);
            int type = needBalance(pNode);
            if (type != NO_NEED) {
                pNode = balanceAVL(pNode, type);
            }
        } else if (newNode.key.compareTo(pNode.key) > 0) {
            pNode.right = insert(pNode.right, newNode);
            pNode.height = 1 + Math.max(pNode.left.height, pNode.right.height);
            int type = needBalance(pNode);
            if (type != NO_NEED) {
                pNode = balanceAVL(pNode, type);
            }
        } 
        return pNode;
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