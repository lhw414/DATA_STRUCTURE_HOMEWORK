import java.io.*;

public class Matching {

  public static void main(String args[]) {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    HashTable<AVLTree<String, LinkedList<IndexTuple>>> hashtable = new HashTable<>();
    LinkedList<String> fileLine = new LinkedList<>();

    while (true) {
      try {
        String input = br.readLine();
        if (input.compareTo("QUIT") == 0) {
          break;
        } else if (input.charAt(0) == '<') {
          hashtable = readFile(input.substring(input.lastIndexOf(" ") + 1));
          fileLine =
            getFileLinkedList(input.substring(input.lastIndexOf(" ") + 1));
        } else if (input.charAt(0) == '@') {
          printAvlTree(input.substring(input.lastIndexOf(" ") + 1), hashtable);
        } else if (input.charAt(0) == '?') {
          printLinkedList(input.substring(2), hashtable);
        } else if (input.charAt(0) == '+') {
          addNewLine(input.substring(2), hashtable, fileLine);
        } else if (input.charAt(0) == '/') {
          hashtable = deleteSubstring(input.substring(2), hashtable, fileLine);
        } else {
          throw new IOException(input, null);
        }
      } catch (IOException e) {
        System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
      }
    }
  }

  private static HashTable<AVLTree<String, LinkedList<IndexTuple>>> readFile(
    String filepath
  ) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filepath));
    String line;
    AVLTree<String, LinkedList<IndexTuple>> avlTree;
    AVLNode<String, LinkedList<IndexTuple>> avlNode;
    HashTable<AVLTree<String, LinkedList<IndexTuple>>> hashTable = new HashTable<AVLTree<String, LinkedList<IndexTuple>>>();
    int i = 1;
    while ((line = reader.readLine()) != null) {
      for (int j = 0; j < line.length() - 5; j++) {
        String substring = line.substring(j, j + 6);
        avlTree = hashTable.get(asciiSumModulo(substring));
        if (avlTree == null) {
          avlTree = new AVLTree<>();
          avlTree.insert(
            new AVLNode<>(substring, new LinkedList<>(new IndexTuple(i, j + 1)))
          );
          hashTable.put(asciiSumModulo(substring), avlTree);
        } else {
          if (avlTree.startSearch(substring) != AVLTree.NIL) {
            avlNode = avlTree.startSearch(substring);
            avlNode.item.append(new IndexTuple(i, j + 1));
          } else {
            avlTree.insert(
              new AVLNode<>(
                substring,
                new LinkedList<>(new IndexTuple(i, j + 1))
              )
            );
          }
        }
      }
      i++;
    }
    reader.close();

    return hashTable;
  }

  private static LinkedList<String> getFileLinkedList(String filepath)
    throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filepath));
    String line;
    LinkedList<String> fileLine = new LinkedList<>();
    while ((line = reader.readLine()) != null) {
      fileLine.append(line);
    }
    reader.close();

    return fileLine;
  }

  private static int asciiSumModulo(String s) {
    int sum = 0;
    for (char c : s.toCharArray()) {
      sum += (int) c;
    }
    return sum % 100;
  }

  private static void printAvlTree(
    String hashIndex,
    HashTable<AVLTree<String, LinkedList<IndexTuple>>> hashTable
  ) {
    AVLTree<String, LinkedList<IndexTuple>> avlTree;
    avlTree = hashTable.get(Integer.parseInt(hashIndex));
    if (avlTree == null) {
      System.out.println("EMPTY");
    } else {
      avlTree.preOrderPrint();
    }
  }

  private static void printLinkedList(
    String subString,
    HashTable<AVLTree<String, LinkedList<IndexTuple>>> hashTable
  ) {
    try {
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
      for (int i = 1; i < subString.length() - 5; i++) {
        avlTree = hashTable.get(asciiSumModulo(subString.substring(i, i + 6)));
        if (avlTree != null) {
          currentList =
            avlTree.startSearch(subString.substring(i, i + 6)).item.copy();
          checkNode = checkList.head.next;
          int checkListLength = checkList.numitems;
          for (int j = 0; j < checkListLength; j++) {
            boolean hasNextSubString = false;
            currNode = currentList.head.next;
            checkNodeIdxTuple = checkNode.indexTuple;
            checkNodeIdxTuple.addIndex2(i);
            for (int k = 0; k < currentList.numitems; k++) {
              if (checkNode.indexTuple.compareTo(currNode.indexTuple) == 0) {
                hasNextSubString = true;
                break;
              }
              currNode = currNode.next;
            }
            checkNodeIdxTuple.addIndex2(-1 * i);
            if (!hasNextSubString) {
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

  private static void addNewLine(
    String newLine,
    HashTable<AVLTree<String, LinkedList<IndexTuple>>> hashTable,
    LinkedList<String> fileLine
  ) {
    AVLTree<String, LinkedList<IndexTuple>> avlTree;
    AVLNode<String, LinkedList<IndexTuple>> avlNode;
    fileLine.append(newLine);
    for (int i = 0; i < newLine.length() - 5; i++) {
      String substring = newLine.substring(i, i + 6);
      avlTree = hashTable.get(asciiSumModulo(substring));
      if (avlTree == null) {
        avlTree = new AVLTree<>();
        avlTree.insert(
          new AVLNode<>(
            substring,
            new LinkedList<>(new IndexTuple(fileLine.numitems, i + 1))
          )
        );
        hashTable.put(asciiSumModulo(substring), avlTree);
      } else {
        if (avlTree.startSearch(substring) != AVLTree.NIL) {
          avlNode = avlTree.startSearch(substring);
          avlNode.item.append(new IndexTuple(fileLine.numitems, i + 1));
        } else {
          avlTree.insert(
            new AVLNode<>(
              substring,
              new LinkedList<>(new IndexTuple(fileLine.numitems, i + 1))
            )
          );
        }
      }
    }
    System.out.println(fileLine.numitems);
  }

  private static HashTable<AVLTree<String, LinkedList<IndexTuple>>> deleteSubstring(
    String substring,
    HashTable<AVLTree<String, LinkedList<IndexTuple>>> hashTable,
    LinkedList<String> fileLine
  ) {
    HashTable<AVLTree<String, LinkedList<IndexTuple>>> newHashTable = new HashTable<>();
    AVLTree<String, LinkedList<IndexTuple>> avlTree = hashTable.get(
      asciiSumModulo(substring)
    );
    LinkedList<IndexTuple> idxTuple;
    int deleteNumItems = 0;

    if (avlTree != null) {
      AVLNode<String, LinkedList<IndexTuple>> avlNode = avlTree.startSearch(
        substring
      );
      if (avlNode != AVLTree.NIL) {
        deleteNumItems = avlNode.item.numitems;
        idxTuple = avlNode.item;
        System.out.println(deleteNumItems);
        for (int i = 0; i < fileLine.numitems; i++) {
          StringBuilder sb = new StringBuilder();
          String line = fileLine.get(i);
          int[] deleteindex = new int[line.length()];
          for (int j = 0; j < idxTuple.numitems; j++) {
            IndexTuple idx = idxTuple.get(j);
            if (idx.getIndex1() == i + 1) {
              for (int k = 0; k < 6; k++) {
                deleteindex[idx.getIndex2() + k - 1] = -100;
              }
            }
          }
          for (int j = 0; j < line.length(); j++) {
            if (deleteindex[j] != -100) {
              sb.append(line.charAt(j));
            }
          }
          fileLine.set(i, sb.toString());
        }
        for (int i = 0; i < fileLine.numitems; i++) {
          String line = fileLine.get(i);
          if (line.length() < 6) {
            continue;
          }
          for (int j = 0; j < line.length() - 5; j++) {
            substring = line.substring(j, j + 6);
            avlTree = newHashTable.get(asciiSumModulo(substring));
            if (avlTree == null) {
              avlTree = new AVLTree<>();
              avlTree.insert(
                new AVLNode<>(
                  substring,
                  new LinkedList<>(new IndexTuple(i + 1, j + 1))
                )
              );
              newHashTable.put(asciiSumModulo(substring), avlTree);
            } else {
              if (avlTree.startSearch(substring) != AVLTree.NIL) {
                avlNode = avlTree.startSearch(substring);
                avlNode.item.append(new IndexTuple(i + 1, j + 1));
              } else {
                avlTree.insert(
                  new AVLNode<>(
                    substring,
                    new LinkedList<>(new IndexTuple(i + 1, j + 1))
                  )
                );
              }
            }
          }
        }
        return newHashTable;
      }
    }

    return hashTable;
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

class LinkedList<T extends Comparable<T>> {

  LinkedListNode<T> head; // dummy head
  int numitems;

  public LinkedList() {
    this.head = new LinkedListNode<T>(null);
    this.head.next = null;
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
    while (
      currentNode.next != null &&
      currentNode.next.indexTuple.compareTo(indexTuple) < 0
    ) {
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
      LinkedListNode<T> newNode = new LinkedListNode<>(
        currentOriginalNode.indexTuple
      );
      currentNewNode.next = newNode;
      currentNewNode = newNode;
      currentOriginalNode = currentOriginalNode.next;
    }

    return newList;
  }

  public T get(int index) {
    if (index < 0 || index >= numitems) {
      throw new IndexOutOfBoundsException("Invalid index");
    }

    LinkedListNode<T> currentNode = head.next;
    for (int i = 0; i < index; i++) {
      currentNode = currentNode.next;
    }

    return currentNode.indexTuple;
  }

  public void set(int index, T value) {
    if (index < 0 || index >= numitems) {
      throw new IndexOutOfBoundsException("Invalid index");
    }

    LinkedListNode<T> currentNode = head.next;
    for (int i = 0; i < index; i++) {
      currentNode = currentNode.next;
    }

    currentNode.indexTuple = value;
  }

  public void printIndexTuples() {
    LinkedListNode<T> currentNode = this.head.next;
    System.out.print(currentNode.indexTuple.toString());
    currentNode = currentNode.next;
    while (currentNode != null) {
      System.out.print(" " + currentNode.indexTuple.toString());
      currentNode = currentNode.next;
    }
    System.out.println();
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

  public AVLNode(
    T key,
    V item,
    AVLNode<T, V> leftChild,
    AVLNode<T, V> rightChild,
    int height
  ) {
    this.key = key;
    this.item = item;
    this.left = leftChild;
    this.right = rightChild;
    this.height = height;
  }
}

class AVLTree<T extends Comparable<T>, V> {

  private final int LL = 1, LR = 2, RR = 3, RL = 4, NO_NEED = 0, ILLEGAL = -1;
  public static final AVLNode NIL = new AVLNode<>(null, null, null, null, 0);
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
    if (node.left.height + 2 <= node.right.height) { // type R
      if (node.right.left.height <= node.right.right.height) {
        type = RR;
      } else {
        type = RL;
      }
    } else if (node.left.height >= node.right.height + 2) { // type L
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

  private AVLNode<T, V> balanceAVLTree(AVLNode<T, V> rotateNode, int type) {
    AVLNode<T, V> returnNode = NIL;
    if (type == LL) {
      returnNode = rightRotate(rotateNode);
    } else if (type == LR) {
      rotateNode.left = leftRotate(rotateNode.left);
      returnNode = rightRotate(rotateNode);
    } else if (type == RR) {
      returnNode = leftRotate(rotateNode);
    } else if (type == RL) {
      rotateNode.right = rightRotate(rotateNode.right);
      returnNode = leftRotate(rotateNode);
    }
    return returnNode;
  }

  private AVLNode<T, V> leftRotate(AVLNode<T, V> rotateNode) {
    AVLNode<T, V> RChild = rotateNode.right;

    if (RChild == NIL) return NIL;

    AVLNode<T, V> RLChild = RChild.left;
    RChild.left = rotateNode;
    rotateNode.right = RLChild;

    rotateNode.height =
      Math.max(rotateNode.left.height, rotateNode.right.height) + 1;
    RChild.height = Math.max(RChild.left.height, RChild.right.height) + 1;
    return RChild;
  }

  private AVLNode<T, V> rightRotate(AVLNode<T, V> rotateNode) {
    AVLNode<T, V> LChild = rotateNode.left;

    if (LChild == NIL) return NIL;

    AVLNode<T, V> LRChild = LChild.right;
    LChild.right = rotateNode;
    rotateNode.left = LRChild;

    rotateNode.height =
      Math.max(rotateNode.left.height, rotateNode.right.height) + 1;
    LChild.height = Math.max(LChild.left.height, LChild.right.height) + 1;
    return LChild;
  }

  public void insert(AVLNode<T, V> newNode) {
    root = insertItem(root, newNode);
  }

  private AVLNode<T, V> insertItem(AVLNode<T, V> tNode, AVLNode<T, V> newNode) {
    int type;
    if (tNode == NIL) {
      tNode = newNode;
    } else if (newNode.key.compareTo(tNode.key) < 0) {
      tNode.left = insertItem(tNode.left, newNode);
      tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
      type = needBalance(tNode);
      if (type != NO_NEED) {
        tNode = balanceAVLTree(tNode, type);
      }
    } else {
      tNode.right = insertItem(tNode.right, newNode);
      tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
      type = needBalance(tNode);
      if (type != NO_NEED) {
        tNode = balanceAVLTree(tNode, type);
      }
    }
    return tNode;
  }

  public void delete(T item) {
    root = findAndDelete(root, item);
  }

  private AVLNode<T, V> findAndDelete(AVLNode<T, V> parentNode, T item) {
    if (parentNode == NIL) {
      return NIL;
    } else {
      if (item.compareTo(parentNode.key) == 0) {
        parentNode = deleteNode(parentNode);
      } else if (item.compareTo(parentNode.key) < 0) {
        parentNode.left = findAndDelete(parentNode.left, item);
        parentNode.height =
          1 + Math.max(parentNode.right.height, parentNode.left.height);
        int type = needBalance(parentNode);
        if (type != NO_NEED) {
          parentNode = balanceAVLTree(parentNode, type);
        }
      } else {
        parentNode.right = findAndDelete(parentNode.right, item);
        parentNode.height =
          1 + Math.max(parentNode.right.height, parentNode.left.height);
        int type = needBalance(parentNode);
        if (type != NO_NEED) {
          parentNode = balanceAVLTree(parentNode, type);
        }
      }
      return parentNode;
    }
  }

  private AVLNode<T, V> deleteNode(AVLNode<T, V> parentNode) {
    if ((parentNode.left == NIL) && (parentNode.right == NIL)) {
      return NIL;
    } else if (parentNode.left == NIL) {
      return parentNode.right;
    } else if (parentNode.right == NIL) {
      return parentNode.left;
    } else {
      returnPair rPair = deleteMinItem(parentNode.right);
      parentNode.key = rPair.item;
      parentNode.right = rPair.node;

      parentNode.height =
        1 + Math.max(parentNode.right.height, parentNode.left.height);
      int type = needBalance(parentNode);
      if (type != NO_NEED) {
        parentNode = balanceAVLTree(parentNode, type);
      }
      return parentNode;
    }
  }

  private returnPair deleteMinItem(AVLNode<T, V> parentNode) {
    if (parentNode.left == NIL) {
      return new returnPair(parentNode.key, parentNode.right);
    } else {
      returnPair rPair = deleteMinItem(parentNode.left);
      parentNode.left = rPair.node;

      parentNode.height =
        1 + Math.max(parentNode.right.height, parentNode.left.height);
      int type = needBalance(parentNode);
      if (type != NO_NEED) {
        parentNode = balanceAVLTree(parentNode, type);
      }

      rPair.node = parentNode;
      return rPair;
    }
  }

  private class returnPair {

    T item;
    AVLNode<T, V> node;

    private returnPair(T item, AVLNode<T, V> node) {
      this.item = item;
      this.node = node;
    }
  }
}

class HashTable<V> {

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
