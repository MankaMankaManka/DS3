public class PaxosDemo {
    public static void main(String[] args) throws IOException {
        // Start acceptors
        new Thread(() -> {
            try { new Acceptor().start(8081); } catch (IOException e) { e.printStackTrace(); }
        }).start();
        new Thread(() -> {
            try { new Acceptor().start(8082); } catch (IOException e) { e.printStackTrace(); }
        }).start();

        // Start proposer
        new Thread(() -> {
            try {
                String[] acceptorAddresses = {"localhost", "localhost"};
                int[] acceptorPorts = {8081, 8082};
                
                Proposer proposer = new Proposer(1, "ValueA");
                proposer.sendPrepare(acceptorAddresses, acceptorPorts);
                Thread.sleep(2000); // Wait for promises
                proposer.sendAccept(acceptorAddresses, acceptorPorts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}