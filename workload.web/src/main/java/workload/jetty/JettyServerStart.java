package workload.jetty;

public class JettyServerStart
{

	public static void main(String[] args)
	{
		JettyCustomServer server = new JettyCustomServer(
				"./jetty/etc/jetty.xml", "/test");
		server.startServer();

	}
}
