class Proposer {
    private int proposalId;
    private String value;
    private ConcurrentHashMap<Integer, String> promises = new ConcurrentHashMap<>();
    
    public Proposer(int proposalId, String value) {
        this.proposalId = proposalId;
        this.value = value;
    }

    public void sendPrepare(String[] acceptorAddresses, int[] acceptorPorts) {
        for (int i = 0; i < acceptorAddresses.length; i++) {
            final int idx = i;
            new Thread(() -> {
                try (Socket socket = new Socket(acceptorAddresses[idx], acceptorPorts[idx]);
                     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                    out.writeObject(new Message("prepare", proposalId, null));
                    Message response = (Message) in.readObject();
                    if (response.type.equals("promise")) {
                        promises.put(idx, response.value);
                        System.out.println("Received promise from Acceptor " + idx);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void sendAccept(String[] acceptorAddresses, int[] acceptorPorts) {
        for (int i = 0; i < acceptorAddresses.length; i++) {
            final int idx = i;
            new Thread(() -> {
                try (Socket socket = new Socket(acceptorAddresses[idx], acceptorPorts[idx]);
                     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                    out.writeObject(new Message("accept", proposalId, value));
                    Message response = (Message) in.readObject();
                    if (response.type.equals("accepted")) {
                        System.out.println("Value accepted by Acceptor " + idx);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}