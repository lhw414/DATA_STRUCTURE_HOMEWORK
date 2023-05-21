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

	private static void command(String input) throws IOException
	{
		if (input.charAt(0) == '<') {
			readFile(input.substring(input.lastIndexOf(" ") + 1));
		} else if (input.charAt(0) == '@') {

		} else if (input.charAt(0) == '?') {

		} else if (input.charAt(0) == '+') {
			
		} else if (input.charAt(0) == '/') {

		} else {
			throw new IOException(input, null);
		}
	}

	private static void readFile(String filepath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line;
		while((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		reader.close();
	}
}

class Node<T extends Comparable<T>, V> {
	T indexTuple;
	V value;
    int height;
    Node<T, V> left, right;

    public Node(T indexTuple, V value) {
		this.indexTuple = indexTuple;
        this.value = value;
        height = 1;
    }

	public Node(T indexTuple, V value, Node<T, V> leftchild, Node<T, V> rightchild, int height) {
		this.indexTuple = indexTuple;
		this.value = value;
		this.left = leftchild;
		this.right = rightchild;
		this.height = height;
	}
}

class AVLTree<T extends Comparable<T>, V> {
    private Node<T, V> root;

	public AVLTree() {
		root = null;
	}

	int height(Node<T, V> node) {
        if (node == null)
            return 0;
        return node.height;
    }

	public void preOrderPrint() {
		if (root.left == null && root.right == null) {
			System.out.println("EMPTY");
		} else {
			System.out.print(this.root.indexTuple);
			preOrderPrint(root.left);
			preOrderPrint(root.right);
		}
	}

	public void preOrderPrint(Node<T, V> currentNode) {
		if (currentNode != null) {
			System.out.print(currentNode.indexTuple);
			preOrderPrint(root.left);
			preOrderPrint(root.right);
		}
	}

    Node<T, V> rotateRight(Node<T, V> y) {
        Node<T, V> x = y.left;
        Node<T, V> T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node<T, V> rotateLeft(Node<T, V> x) {
        Node<T, V> y = x.right;
        Node<T, V> T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    int getBalance(Node<T, V> node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    Node<T, V> insert(Node<T, V> node, T indexTuple, V value) { // 수정: 인자 변경
        if (node == null)
            return new Node<>(indexTuple, value);

        if (indexTuple.compareTo(node.indexTuple) < 0) { // 수정: indexTuple 비교
            node.left = insert(node.left, indexTuple, value);
        } else if (indexTuple.compareTo(node.indexTuple) > 0) { // 수정: indexTuple 비교
            node.right = insert(node.right, indexTuple, value);
        } else { // 수정: indexTuple이 같을 때는 중복을 허용하지 않거나 다른 처리를 수행할 수 있습니다.
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && indexTuple.compareTo(node.left.indexTuple) < 0) { // 수정: indexTuple 비교
            return rotateRight(node);
        }

        if (balance < -1 && indexTuple.compareTo(node.right.indexTuple) > 0) { // 수정: indexTuple 비교
            return rotateLeft(node);
        }

        if (balance > 1 && indexTuple.compareTo(node.left.indexTuple) > 0) { // 수정: indexTuple 비교
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && indexTuple.compareTo(node.right.indexTuple) < 0) { // 수정: indexTuple 비교
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

}

class HashTable<K, V> {
    private static final int DEFAULT_SIZE = 100;
	private V[] hashTable;
	
	@SuppressWarnings("unchecked")
	public HashTable() {
		this.hashTable = (V[]) new Object[DEFAULT_SIZE];
	}

	public int hash(K x) {
		return x.hashCode() % DEFAULT_SIZE;
	}

	public void put(K key, V value) {
        int index = hash(key);
		if (hashTable[index] == null) {

		} else {
			
		}
    }

	public V get(int index) {
		return hashTable[index];
	}
    
}
