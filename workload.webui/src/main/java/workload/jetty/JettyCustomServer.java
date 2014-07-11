package workload.jetty;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.xml.sax.SAXException;

public class JettyCustomServer extends Server {

	private String xmlConfigPath;

	private String contextPath;

	private String warPath;

	private String workPath;

	private String resourceBase;

	private String webXmlPath;

	public JettyCustomServer(String xmlConfigPath, String contextPath,
			String resourceBase, String webXmlPath) {
		this(xmlConfigPath, contextPath, resourceBase, webXmlPath, null);
	}

	public JettyCustomServer(String workPath, String contextPath) {
		this(workPath, contextPath, null, null, null);
	}

	public JettyCustomServer(String xmlConfigPath, String contextPath,
			String warPath) {
		this(xmlConfigPath, contextPath, null, null, warPath);
	}

	public JettyCustomServer(String workPath, String contextPath,
			String resourceBase, String webXmlPath, String warPath) {
		super();
		this.workPath = workPath;
		String xmlConfigPath = workPath + "/jetty/etc/jetty.xml";
		resourceBase = workPath + "/src/main/webapp";
		webXmlPath = workPath + "/src/main/webapp/WEB-INF/web.xml";
		if (StringUtils.isNotBlank(xmlConfigPath)) {
			this.xmlConfigPath = xmlConfigPath;
			readXmlConfig();
		}

		if (StringUtils.isNotBlank(warPath)) {
			this.warPath = warPath;
			if (StringUtils.isNotBlank(contextPath)) {
				this.contextPath = contextPath;
				applyHandle(true);
			}
		} else {
			if (StringUtils.isNotBlank(resourceBase))
				this.resourceBase = resourceBase;
			if (StringUtils.isNotBlank(webXmlPath))
				this.webXmlPath = webXmlPath;
			if (StringUtils.isNotBlank(contextPath)) {
				this.contextPath = contextPath;
				applyHandle(false);
			}
		}

	}

	private void readXmlConfig() {
		try {
			XmlConfiguration configuration = new XmlConfiguration(
					new FileInputStream(this.xmlConfigPath));
			configuration.configure(this);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void applyHandle(Boolean warDeployFlag) {

		ContextHandlerCollection handler = new ContextHandlerCollection();

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath(contextPath);
		webapp.setDefaultsDescriptor(workPath + "/jetty/etc/webdefault.xml");

		if (!warDeployFlag) {
			webapp.setResourceBase(resourceBase);
			webapp.setDescriptor(webXmlPath);
		} else {
			webapp.setWar(warPath);
		}

		handler.addHandler(webapp);

		super.setHandler(handler);
	}

	public void startServer() {
		try {
			super.start();
			System.out.println("current thread:"
					+ super.getThreadPool().getThreads() + "| idle thread:"
					+ super.getThreadPool().getIdleThreads());
			super.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getXmlConfigPath() {
		return xmlConfigPath;
	}

	public void setXmlConfigPath(String xmlConfigPath) {
		this.xmlConfigPath = xmlConfigPath;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getResourceBase() {
		return resourceBase;
	}

	public void setResourceBase(String resourceBase) {
		this.resourceBase = resourceBase;
	}

	public String getWebXmlPath() {
		return webXmlPath;
	}

	public void setWebXmlPath(String webXmlPath) {
		this.webXmlPath = webXmlPath;
	}

	public String getWarPath() {
		return warPath;
	}

	public void setWarPath(String warPath) {
		this.warPath = warPath;
	}

}
