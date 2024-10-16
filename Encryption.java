import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.lang.StringBuilder;

public class Encryption {
    class LinkNode {
        protected int data;
        protected LinkNode next;
        protected LinkNode prev;

        public LinkNode() {
            next = null;
            prev = null;
            data = 0;
        }
    }

    private LinkNode head;
    private LinkNode tail;
    private int size;

    public Encryption() {
        head = null;
        tail = null;
        size = 0;
    }

    // takes in a message and encrypts it
    public void encrypt(String filename, String args[]) {
        String messageToEncode = getInputForEncryption(args);
        int numericalMessage[] = new int[messageToEncode.length()];
        int charMessage[] = new int[messageToEncode.length()];
        runAlgorithm(messageToEncode, numericalMessage);
        String encryptedMessage = createMessage(charMessage, numericalMessage, messageToEncode, args);
        System.out.println("The encrypted message is: " + encryptedMessage);
    }

    // takes an encrypted message and decrypts it
    public void decrypt(String filename, String args[]) {
        String messageToDecode = getInputForEncryption(args);
        int numericalMessage[] = new int[messageToDecode.length()];
        int charMessage[] = new int[messageToDecode.length()];
        runAlgorithm(messageToDecode, numericalMessage);
        String decryptedMessage = createMessage(charMessage, numericalMessage, messageToDecode, args);
        System.out.println("The decrypted message is: " + decryptedMessage);
    }

    // finds first joker then swaps it with the next card
    private void swapFirstJoker() {
        findCard(27);
    }

    // finds second joker then moves it 2 places ahead
    private void moveSecondJoker() {
        findCard(28);
        findCard(28);
    }

    // finds card and then calls swap
    private void findCard(int joker) {
        LinkNode temp = head;
        do {
            if (temp.data == joker) {
                swap(temp);
                return;
            }
            temp = temp.next;
        } while (temp != head);
        System.out.println("Error, joker card not found. Program exiting.");
        System.exit(0);
    }

    // swaps joker 1 or 2 places depending on what joker needs to be swapped
    private void swap(LinkNode temp) {
        if (temp == head) {
            head = temp.next;
            head.prev = tail;
            tail.next = head;
            temp.next = head.next;
            temp.next.prev = temp;
            head.next = temp;
            temp.prev = head;
            return;
        }
        if (temp == tail) {
            temp.prev.next = head;
            head.prev = temp.prev;
            head.next.prev = temp;
            temp.next = head.next;
            temp.prev = head;
            head.next = temp;
            tail = head;
            head = temp;
            return;
        }
        if (temp.next == tail) {
            tail.prev = temp.prev;
            temp.prev.next = tail;
            temp.next = head;
            temp.prev = tail;
            tail.next = temp;
            head.prev = temp;
            tail = temp;
            return;
        }
        temp.next = temp.next.next;
        temp.next.prev.next = temp;
        temp.next.prev.prev = temp.prev;
        temp.prev.next = temp.next.prev;
        temp.prev = temp.next.prev;
        temp.next.prev = temp;
        return;
    }

    // swaps all cards before first joker with all cards after second joker
    private void tripleCut() {
        LinkNode firstJoker = head;
        while (firstJoker.data != 27 && firstJoker.data != 28) {
            firstJoker = firstJoker.next;
        }
        LinkNode secondJoker = firstJoker.next;
        while (secondJoker.data != 27 && secondJoker.data != 28) {
            secondJoker = secondJoker.next;
        }
        LinkNode newHead = secondJoker.next;
        LinkNode newTail = firstJoker.prev;
        if (firstJoker == head && secondJoker == tail) {
            return;
        }
        if (firstJoker == head) {
            head = secondJoker.next;
            tail = secondJoker;
            return;
        }
        if (secondJoker == tail) {
            head = firstJoker;
            tail = firstJoker.prev;
            return;
        }
        firstJoker.prev.next = newHead;
        secondJoker.next.prev = newTail;
        head.prev = secondJoker;
        tail.next = firstJoker;
        secondJoker.next = head;
        firstJoker.prev = tail;
        head = newHead;
        tail = newTail;
    }

    // finds value of bottom card, counts through the cards that many cards
    // then moves those cards inclusive to the bottom of the deck
    private void bottomCard() {
        int bottomCard = tail.data;
        if (bottomCard == 27 || bottomCard == 28) {
            return;
        }
        LinkNode temp = head;
        for (int i = 0; i < bottomCard - 1; i++) {
            temp = temp.next;
        }
        LinkNode newHead = temp.next;
        temp.next.prev = tail;
        head.prev = tail.prev;
        tail.prev.next = head;
        tail.prev = temp;
        temp.next = tail;
        tail.next = newHead;
        head = newHead;
    }

    // finds value of top card, counts down that many cards and records
    // that value unless it's a joker
    private int topCard() {
        int topCard = head.data;
        if (topCard == 28) {
            topCard = 27;
        }
        LinkNode temp = head;
        for (int i = 0; i < topCard; i++) {
            temp = temp.next;
        }
        if (temp.data == 27 || temp.data == 28) {
            return 27;
        }
        return temp.data;
    }

    // opens the given file, and adds the data to the deck
    public void openFile(String filename) {
        Scanner fileReader;
        try {
            fileReader = new Scanner(new FileInputStream(filename));
            while (fileReader.hasNextInt()) {
                insertRear(fileReader.nextInt());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found or could not be opened.");
            System.exit(0);
        }
    }

    // prompts user for message to encrypt or decrypt
    private String getInputForEncryption(String args[]) {
        Scanner input = new Scanner(System.in);
        if (args[0].equals("e")) {
            System.out.print("Enter message to be encrypted (non-letters ignored): ");
            String message = editString(input.nextLine());
            checkForInput(message);
            message = addXValues(message);
            System.out.println("Plaintext message is: " + message);
            return message;
        } else {
            System.out.print("Enter message to be decrypted: ");
            String message = editString(input.nextLine());
            checkForInput(message);
            return message;
        }
    }

    // ensures that the user has entered a message
    private void checkForInput(String message) {
        if (message.length() == 0) {
            System.out.println("No message entered.");
            System.exit(0);
        }
    }

    // creates new encrypted or decrypted message
    private String createMessage(int charMessage[], int numericalMessage[], String message, String args[]) {
        int x = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charMessage.length; i++) {
            charMessage[i] = message.charAt(i) - 64;
        }
        if (args[0].equals("e")) {
            for (int i = 0; i < charMessage.length; i++) {
                x = ((charMessage[i] + numericalMessage[i]));
                if (x > 26) {
                    x = x - 26;
                }
                sb.append((char) (x + 64));
            }
            return sb.toString();
        } else {
            for (int i = 0; i < charMessage.length; i++) {
                x = ((charMessage[i] - numericalMessage[i]));
                if (x < 1) {
                    x = x + 26;
                }
                sb.append((char) (x + 64));
            }
        }
        return sb.toString();
    }

    // runs algorithm and adds the keystream values to the array
    private void runAlgorithm(String message, int numericalMessage[]) {
        int keystreamValue = 0;
        int keystreamLength = 0;
        while (keystreamLength != message.length()) {
            swapFirstJoker();
            moveSecondJoker();
            tripleCut();
            bottomCard();
            keystreamValue = topCard();
            if (keystreamValue != 27) {
                numericalMessage[keystreamLength] = keystreamValue;
                keystreamLength++;
            }
        }
    }

    // removes any non letter characters from the message
    private String editString(String s) {
        s = s.trim().toUpperCase();
        StringBuilder sb = new StringBuilder(s);
        for (int i = s.length() - 1; i >= 0; i--) {
            if (!Character.isAlphabetic(s.charAt(i))) {
                sb = sb.deleteCharAt(i);
            }
        }
        return sb.toString();
    }

    // adds the correct number of X values to the message before encryption
    private String addXValues(String s) {
        int numOfXValues = s.length() % 5;
        if (numOfXValues == 0)
            return s;
        // 5 - numOfXValues makes it the correct num of x's to be added
        for (int i = 0; i < 5 - numOfXValues; i++)
            s += "X";
        return s;
    }

    // checks to see if the deck is empty
    private boolean isEmpty() {
        return head == null;
    }

    // adds cards into the deck
    private void insertRear(int newValue) {
        LinkNode temp = new LinkNode();
        size++;
        temp.data = newValue;
        if (isEmpty()) {
            head = tail = temp;
            return;
        }
        // once at size 27, the 28th card will be the last one in the algorithm
        if (size == 28) {
            tail.next = temp;
            temp.prev = tail;
            temp.next = head;
            head.prev = tail = temp;
            return;
        }
        tail.next = temp;
        temp.prev = tail;
        tail = temp;
    }
}
