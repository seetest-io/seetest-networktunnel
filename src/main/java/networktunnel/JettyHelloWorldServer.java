package networktunnel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Simple Embedded Jetty Http Server.
 */
public class JettyHelloWorldServer extends AbstractHandler {

    private String defaultResponse = "Hello World!";

    public JettyHelloWorldServer() {
    }

    /**
     * Constructor with default message.
     * @param defaultResponse
     */
    public JettyHelloWorldServer(String defaultResponse) {
        this.defaultResponse = defaultResponse;
    }

    /**
     * Handles request.
     * @param target
     * @param baseRequest
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>" + defaultResponse + "</h1>");
    }

    public static void main(String[] args) throws Exception
    {
        Server server = new Server(80);
        server.setHandler(new JettyHelloWorldServer());
        server.start();
        server.join();
    }
}
