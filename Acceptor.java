class Acceptor {
    private int highestProposalId = -1;
    private String acceptedValue = null;

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Acceptor listening on port " + port);
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleRequest(clientSocket)).start();
        }
    }

    private void handleRequest(Socket clientSocket) {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            Message message = (Message) in.readObject();
            if (message.type.equals("prepare")) {
                if (message.proposalId > highestProposalId) {
                    highestProposalId = message.proposalId;
                    out.writeObject(new Message("promise", highestProposalId, acceptedValue));
                }
            } else if (message.type.equals("accept")) {
                if (message.proposalId >= highestProposalId) {
                    highestProposalId = message.proposalId;
                    acceptedValue = message.value;
                    out.writeObject(new Message("accepted", highestProposalId, acceptedValue));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}