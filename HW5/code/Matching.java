import java.io.*;

public class Matching
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{
		// TODO : 아래 문장을 삭제하고 구현해라.
		System.out.println("<< command 함수에서 " + input + " 명령을 처리할 예정입니다 >>");
	}
}

class ListNode<T> {
    T value;
    ListNode<T> next;

    public ListNode(T value) {
        this.value = value;
        this.next = null;
    }
}

class LinkedList<T> {
    ListNode<T> head;

    public LinkedList() {
        head = null;
    }

    public void add(T value) {
        ListNode<T> newNode = new ListNode<>(value);
        if (head == null) {
            head = newNode;
        } else {
            ListNode<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void printList() {
        ListNode<T> current = head;
        while (current != null) {
            System.out.print(current.value + " ");
            current = current.next;
        }
        System.out.println();
    }
}

class Node<T> {
    int key, height;
    Node<T> left, right;
    LinkedList<T> linkedList;

    Node(int value) {
        key = value;
        height = 1;
        linkedList = new LinkedList<>();
        linkedList.add(value);
    }
}

class AVLTree<T> {
    Node<T> root;

    int height(Node<T> node) {
        if (node == null)
            return 0;
        return node.height;
    }

    int max(int a, int b) {
        return Math.max(a, b);
    }

    Node<T> rotateRight(Node<T> y) {
        Node<T> x = y.left;
        Node<T> T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.right;
        Node<T> T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    int getBalance(Node<T> node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    Node<T> insert(Node<T> node, int key) {
        if (node == null)
            return new Node<>(key);

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node;

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && key < node.left.key)
            return rotateRight(node);

        if (balance < -1 && key > node.right.key)
            return rotateLeft(node);

        if (balance > 1 && key > node.left.key) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && key < node.right.key) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    void preOrder(Node<T> node) {
        if (node != null) {
            System.out.print(node.key + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }
}

class HashTable<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private ArrayList<LinkedList<Entry<K, V>>> buckets;
    private int size;

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    HashTable() {
        buckets = new ArrayList<>(DEFAULT_SIZE);
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            buckets.add(new LinkedList<>());
        }
        size = 0;
    }

    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        return hashCode % buckets.size();
    }

    private Entry<K, V> getEntry(K key) {
        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets.get(bucketIndex);
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry;
            }
        }
        return null;
    }

    public void put(K key, V value) {
        Entry<K, V> entry = getEntry(key);
        if (entry != null) {
            entry.value = value;
        } else {
            int bucketIndex = getBucketIndex(key);
            LinkedList<Entry<K, V>> bucket = buckets.get(bucketIndex);
            bucket.add(new Entry<>(key, value));
            size++;
        }
    }

    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        return entry != null ? entry.value : null;
    }

    public boolean containsKey(K key) {
        return getEntry(key) != null;
    }

    public void remove(K key) {
        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets.get(bucketIndex);
        Entry<K, V> entryToRemove = null;
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entryToRemove = entry;
                break;
            }
        }
        if (entryToRemove != null) {
            bucket.remove(entryToRemove);
            size--;
        }
    }

    public int size() {
        return size;
    }
}
