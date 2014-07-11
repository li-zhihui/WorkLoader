package workload.jetty;

public class JettyServerStart {

	public static void main(String[] args) {
		String workPath = args[0];
		JettyCustomServer server = new JettyCustomServer(workPath, "/test");
		server.startServer();

	}
}
