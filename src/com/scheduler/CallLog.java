package com.scheduler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reminder.vo.CallLogs;

import mangodb.MangoDB;

/**
 * Servlet implementation class CallLog
 */
@WebServlet("/CallLog")
public class CallLog extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallLog() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/xml");
		String id = request.getParameter("id");
		String logJson = MangoDB.getDocumentWithQuery("remind-me-on", "call-logs", id, null,true, null, null);
		Gson  json = new Gson();
		CallLogs log = json.fromJson(logJson, new TypeToken<CallLogs>() {}.getType());
		String messageToSpeak = "";
		if (log.getMessage().length() < 120) {
			messageToSpeak = log.getMessage()+". I repeat. "+log.getMessage()+". I repeat. "+log.getMessage();
		}else {
			messageToSpeak = log.getMessage();
			if (messageToSpeak.length() > 360) {
				messageToSpeak = messageToSpeak.substring(0, 360);
			}
		}
		
		String message = "<Response><Speak>"+messageToSpeak+".</Speak></Response>";
		response.getWriter().print(message);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
