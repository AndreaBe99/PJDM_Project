package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.AccountDAO;
import db.AccountDAOJDBCImpl;
import db.AcquisizioneDAO;
import db.AcquisizioneDAOJDBCImpl;
import db.UtenteDAO;
import db.UtenteDAOJDBCImpl;
import entity.Account;
import entity.Acquisizione;
import entity.Utente;

public class AcquisizioneServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	private AcquisizioneDAO dao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AcquisizioneServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void destroy() {
		dao.closeConnection();
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(405);
		response.getWriter().append("L'implementazione del metodo PUT e' implementato come esercizio.");
	}


	//DOdelete

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("account") == null  || request.getParameter("utente") == null) {
			response.setStatus(400);
			response.getWriter().append("Occorre specificare un account e un nome per le acquisizioni");
			return;
		}

		String account = request.getParameter("account");
		String utente = request.getParameter("utente");

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		ArrayList<Acquisizione> allAcquisizioni = dao.loadTutteAcquisizioni(utente, account);
		JSONArray allAccountsJson = new JSONArray(allAcquisizioni);
		out.print(allAccountsJson.toString());
		out.flush();
	}

	/**
	 * Il metodo POST permette di inserire un nuovo musicista. Prevede due
	 * modalità:
	 * 
	 * - invare un nuovo musicista specificando name e ssn
	 * 
	 * - invare un nuovo musicista specificandolo in una stringa JSON
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("json") != null) {
			handleJson(request, response);
		} else {
			handleNameSSN(request, response);
		}

	}

	private void handleJson(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String inputJson = request.getParameter("json");
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(inputJson);
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Input Malformato");
			return;
		}

		String data = null, utente = null, account = null;
		double alcol = 0, peso = 0;
		
		
		try {
			alcol = jsonObject.getDouble("alcol");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo alcol");
		}
		
		try {
			peso = jsonObject.getDouble("peso");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo peso");
		}
		
		try {
			data = jsonObject.getString("data");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo data");
		}
		
		try {
			utente = jsonObject.getString("utente");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo utente");
		}

		try {
			account = jsonObject.getString("account");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo account");
		}
		

		Acquisizione newAcquisizione = new Acquisizione(alcol, peso, data, utente, account);
		int res = dao.insertAcquisizione(newAcquisizione);

		if (res > 0) {
			response.setStatus(200);
			response.getWriter().append("Tutto OK");
		} else {
			response.setStatus(500);
			response.getWriter().append("Problemi :-(");
		}

	}

	private void handleNameSSN(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("alcol") == null || request.getParameter("peso") == null || request.getParameter("data") == null || request.getParameter("utente") == null || request.getParameter("account") == null) {
			response.setStatus(500);
			response.getWriter().append("Occorre specificare alcol, data, utente, account da inserire");
			return;
		}

		double alcol = Double.valueOf(request.getParameter("alcol"));
		double peso = Double.valueOf(request.getParameter("peso"));
		String data = request.getParameter("data");
		String utente = request.getParameter("utente");
		String account = request.getParameter("account");

		Acquisizione newAcquisizione = new Acquisizione(alcol, peso, data, utente, account);
		int res = dao.insertAcquisizione(newAcquisizione);
	

		if (res > 0) {
			response.setStatus(200);
			response.getWriter().append("Tutto OK");
		} else {
			response.setStatus(500);
			response.getWriter().append("Problemi :-(");
		}
	}

	@Override
	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");

		dao = new AcquisizioneDAOJDBCImpl(ip, port, dbName, userName, password);
	}


}
