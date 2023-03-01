package org.example;

import java.util.*;

enum TransactionType {
    DEPOSIT,
    WITHDRAWAL
}

class Transaction {
    private TransactionType transactionType;
    private long amount;
    private long timestamp;

    public Transaction(TransactionType transactionType, long amount, long timestamp) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public long getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

class Account {
    private final long timestamp;
    private final String accountId;

    private Long balance;

    private final List<Transaction> history;

    private long activity;

    private Queue<Transfer> withHeldAmount;

    public Account(long timestamp, String accountId) {
        this.timestamp = timestamp;
        this.accountId = accountId;
        this.balance = 0L;
        this.history = new ArrayList<>();
        this.activity = 0l;
        this.withHeldAmount = new ArrayDeque<>();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAccountId() {
        return accountId;
    }

    public Long getBalance() {
        return balance;
    }

    public void addToAccount(long amount) {
        this.activity += Math.abs(amount);
        this.balance += amount;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public long getActivity() {
        return activity;
    }

    public void putAmountOnHold(Transfer transfer) {
        balance -= transfer.getAmount();
        withHeldAmount.add(transfer);
    }

    public void preprocessWithHeldAmount(Long currentTimestamp) {
        while (!withHeldAmount.isEmpty() && withHeldAmount.peek().getTimestamp() < currentTimestamp - 86400000) {
            if(!withHeldAmount.peek().getAccepted()){
                balance += withHeldAmount.peek().getAmount();
            }

            withHeldAmount.poll();
        }
    }
}

class myAccountComparator implements Comparator<Account> {
    public int compare(Account s1, Account s2) {
        if (s1.getActivity() == s2.getActivity()) {
            return s1.getAccountId().compareTo(s2.getAccountId());
        }

        if (s1.getActivity() < s2.getActivity()) {
            return 1;
        }
        return -1; // desc order of activity
    }
}

class Transfer {
    private String sourceAccountId;
    private String destinationAccountId;
    private String transferId;
    private Long timestamp;

    private Long amount;

    private Boolean isAccepted;

    public Transfer(String sourceAccountId, String destinationAccountId, String transferId, Long timestamp, Long amount) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.transferId = transferId;
        this.timestamp = timestamp;
        this.amount= amount;
        this.isAccepted = false;
    }

    public Long getAmount() {
        return amount;
    }

    public void markAsAccepted() {
        isAccepted = true;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getDestinationAccountId() {
        return destinationAccountId;
    }

    public String getTransferId() {
        return transferId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }
}

class Bank {
    Map<String, Account> accountIdMapper;
    TreeSet<Account> activityTracker;
    ArrayList<Account> accountList;
    Map<String, Transfer> transferMap;
    long transferCount = 0;

    public Bank() {
        this.accountIdMapper = new HashMap<>();
        this.activityTracker = new TreeSet<Account>(new myAccountComparator());
        this.accountList = new ArrayList<>();
    }

    public String createAccount(final String timestamp, final String accountId) {
        if (accountIdMapper.containsKey(accountId)) {
            return "false";
        }

        Account newAccount = new Account(Long.parseLong(timestamp), accountId);
        accountIdMapper.put(accountId, newAccount);
        activityTracker.add(newAccount);
        accountList.add(newAccount);
        return "true";
    }

    public String deposit(final String accountId, final String timestamp, final String amount) {
        if (!accountIdMapper.containsKey(accountId)) {
            return "";
        }
        Account account = accountIdMapper.get(accountId);
        account.addToAccount(Long.parseLong(amount));
        account.getHistory().add(new Transaction(TransactionType.DEPOSIT, Long.parseLong(timestamp), Long.parseLong(amount)));
        return account.getBalance().toString();
    }

    public String pay(final String accountId, final String timestamp, String amount) {
        if (!accountIdMapper.containsKey(accountId)) {
            return "";
        }
        Account account = accountIdMapper.get(accountId);

        if (account.getBalance() < Long.parseLong(amount)) {
            return "";
        }

        account.addToAccount(-Long.parseLong(amount));
        account.getHistory().add(new Transaction(TransactionType.WITHDRAWAL, Long.parseLong(timestamp), Long.parseLong(amount)));
        return account.getBalance().toString();
    }

    public String topActivity(final String timestamp, final String n) {
        Collections.sort(accountList, new myAccountComparator());
        int size = Integer.parseInt(n);
        String result = "";
        int count = 0;
        for (Account account : accountList) {
            if (count == Math.min(size, activityTracker.size())) {
                break;
            }
            if (result.equals("")) {
                result += account.getAccountId() + "(" + account.getActivity() + ")";
            } else {
                result += ", " + account.getAccountId() + "(" + account.getActivity() + ")";
            }

            count++;
        }

        return result;
    }

    public String transfer(final String timestamp, final String srcAccountId, final String targetAccountId, final String amount) {
        if (srcAccountId.equals(targetAccountId)) {
            return "";
        }

        if (!accountIdMapper.containsKey(srcAccountId) || !accountIdMapper.containsKey(targetAccountId)) {
            return "";
        }

        Account sourceAccount = accountIdMapper.get(srcAccountId);

        if (sourceAccount.getBalance() < Long.parseLong(amount)) {
            return "";
        }

        String transferId = "transfer" + transferCount;
        transferCount++;
        Transfer transfer = new Transfer(srcAccountId, targetAccountId, transferId, Long.parseLong(timestamp), Long.parseLong(amount));
        transferMap.put(transferId, transfer);
        sourceAccount.putAmountOnHold(transfer);

        return transferId;
    }

//    public String acceptTransfer() {
//          // Didn't got time to complete
//    }
}


public class Main {
    public static void main(String[] args) {

        System.out.println("ok");
    }

    String[] solution(String[][] queries) {
        int n = queries.length;
        Bank bank = new Bank();
        String[] result = new String[n];
        for (int i = 0; i < n; i++) {
            String[] query = queries[i];
            if (query[0].equals("CREATE_ACCOUNT")) {
                result[i] = bank.createAccount(query[1], query[2]);
            } else if (query[0].equals("DEPOSIT")) {
                result[i] = bank.deposit(query[2], query[1], query[3]);
            } else if (query[0].equals("PAY")) {
                result[i] = bank.pay(query[2], query[1], query[3]);
            } else if (query[0].equals("TOP_ACTIVITY")) {
                result[i] = bank.topActivity(query[1], query[2]);
            } else if (query[0].equals("TRANSFER")) {
                result[i] = bank.topActivity(query[1], query[2]);
            } else if (query[0].equals("ACCEPT_TRANSFER")) {
                result[i] = bank.topActivity(query[1], query[2]);
            }
        }

        return result;
    }

}