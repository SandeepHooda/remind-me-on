package com.communication.phone.text;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TextStatus
 */
@WebServlet("/TextStatus")
public class TextStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TextStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String from_number = req.getParameter("From"); // Sender's phone number
        String to_number = req.getParameter("To"); // Receiver's phone number - Plivo number
        String status = req.getParameter("Status"); // Status of the message
        String uuid = req.getParameter("MessageUUID"); // Message UUID

        // Prints the status of the message
        System.out.println("From: " + from_number + ", To: " + to_number + ", Status: " + status + ", MessageUUID: " + uuid);
        resp.getWriter().print("Delivery status reported");
    }

    

}
