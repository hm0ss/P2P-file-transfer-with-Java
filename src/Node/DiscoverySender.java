/**
 * A Class to send discovery messages
 */

package Node;

import java.util.Vector;

public class DiscoverySender extends Thread {
    private Vector<Node> cluster;
    private Node node;
    private long repeatTime;

    /**
     * Constructor of this class
     *
     * @param n
     * @param t
     */
    public DiscoverySender(Node n, long t) {
        cluster = n.cluster;
        node = n;
        repeatTime = t;
    }

    @Override
    public void run() {
        while (true) {
            long temp = System.currentTimeMillis();

            while (System.currentTimeMillis() < temp + repeatTime) ;

            try {
                sendDiscovery();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * A Method to create the message that is to be sent
     *
     * @return
     */
    String createMessage() {
        String ans = "DIS";

        //Add yourself
        node.cluster.add(new Node(node.ip, node.udpPort, node.name));
        for (int i = 0; i < cluster.size(); i++)
            ans = ans + cluster.get(i).getName() + "|" + cluster.get(i).getIp() + "p" + cluster.get(i).getUDPPort() + "\n";

        for (int i = 0; i < node.cluster.size(); i++)
            if (node.cluster.get(i).getName().equals(node.getName()))
                node.cluster.remove(i);


        return ans;

    }

    /**
     * Send discovery message to all nodes in cluster
     */
    void sendDiscovery() {
        String msg = createMessage();
        //      System.out.println("SEND DISCOVERY, MSG IS : "+msg);


        for (int i = 0; i < cluster.size(); i++)
            Node.sendUDPSignal(cluster.get(i).getIp(), cluster.get(i).getUDPPort(), msg);
    }
}
