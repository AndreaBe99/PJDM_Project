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
import db.UtenteDAO;
import db.UtenteDAOJDBCImpl;
import entity.Account;
import entity.Utente;

public class UtenteServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	private UtenteDAO dao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UtenteServlet() {
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

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("nome") == null || request.getParameter("account") == null) {
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Occorre specificare un nome e un account da inserire");
			return;
		}

		String nome = request.getParameter("nome");
	//	String sesso =request.getParameter("sesso");
	//	double peso = Double.valueOf(request.getParameter("peso"));
		String account = request.getParameter("account");

		Utente oldUtente= dao.loadUtente(nome, account);
		if (oldUtente==null || !oldUtente.getNome().equals(nome) || !oldUtente.getAccount().equals(account)) {
			response.setStatus(400);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Purtroppo il seguente utente non è nel DB");
			return;
		}

		Utente newUtente = new Utente(nome, oldUtente.getSesso(), oldUtente.getPeso(), account);
		boolean isOk = dao.deleteUtente(nome, account);

		if (isOk) {
			response.setStatus(200);
			response.getWriter().append("Tutto OK");
		} else {
			response.setStatus(500);
			response.getWriter().append("Problemi :-(" + oldUtente.toString());
		}
	}

	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("account") == null) {
			response.setStatus(400);
			response.getWriter().append("Occorre specificare un account");
			return;
		}

		String nome = request.getParameter("nome");
		String account = request.getParameter("account");

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		if (request.getParameter("nome") == null) {
			ArrayList<Utente> allUtenti = dao.loadTuttiUtenti(account);
			JSONArray allAccountsJson = new JSONArray(allUtenti);
			out.print(allAccountsJson.toString());
			out.flush();
		} else {
			Utente utente = dao.loadUtente(nome, account);
			if (utente == null) {
				response.setStatus(404);
				response.getWriter().append("Non esiste un Utente con Username " + nome + " dell'account " + account + ".");
				return;
			}
			JSONObject utenteJson = new JSONObject(utente);
			out.print(utenteJson.toString());
			out.flush();
		}
	}

	/**
	 * Il metodo POST permette di inserire un nuovo musicista. Prevede due
	 * modalitÃ :
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
	
	protected void doPatch(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("nome") == null || request.getParameter("account") == null || request.getParameter("account") == null) {
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Occorre specificare un nome e un account o un peso da inserire");
			return;
		}

		String nome = request.getParameter("nome");
	//	String sesso =request.getParameter("sesso");
		double peso = Double.valueOf(request.getParameter("peso"));
		String account = request.getParameter("account");

		Utente oldUtente= dao.loadUtente(nome, account);
		if (oldUtente==null || !oldUtente.getNome().equals(nome) || !oldUtente.getAccount().equals(account)) {
			response.setStatus(400);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Purtroppo il seguente utente non è nel DB");
			return;
		}

		Utente newUtente = new Utente(nome, oldUtente.getSesso(), oldUtente.getPeso(), account);
		boolean isOk = dao.updateUtente(nome, account, peso);

		if (isOk) {
			response.setStatus(200);
			response.getWriter().append("Tutto OK");
		} else {
			response.setStatus(500);
			response.getWriter().append("Problemi :-(" + oldUtente.toString() + " " + newUtente.toString());
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

		String nome = null, account = null, sesso = null;
		double peso = 0;
		
		
		try {
			nome = jsonObject.getString("nome");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo nome");
		}
		
		try {
			sesso = jsonObject.getString("sesso");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo sesso");
		}
		
		try {
			peso = jsonObject.getDouble("peso");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo peso");
		}

		try {
			account = jsonObject.getString("account");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo account");
		}
		

		Utente newUtente = new Utente(nome, sesso , peso, account);
		int res = dao.insertUtente(newUtente);

		if (res > 0) {
			response.setStatus(200);
			response.getWriter().append("Tutto OK");
		} else {
			response.setStatus(500);
			response.getWriter().append("Problemi :-(");
		}

	}

	private void handleNameSSN(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("nome") == null || request.getParameter("sesso") == null || request.getParameter("peso") == null || request.getParameter("account") == null) {
			response.setStatus(500);
			response.getWriter().append("Occorre specificare un username, password e email da inserire");
			return;
		}

		String nome = request.getParameter("nome");
		String sesso = request.getParameter("sesso");
		double peso = Double.valueOf(request.getParameter("peso"));
		String account = request.getParameter("account");

		Utente newUtente = new Utente(nome, sesso, peso, account);
		int res = dao.insertUtente(newUtente);
	

		if (res > 0) {
			response.setStatus(200);
			response.getWriter().append("Tutto OK");
		} else {
			response.setStatus(500);
			response.getWriter().append("Problemi :-( creazione utente");
		}
	}

	@Override
	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");

		dao = new UtenteDAOJDBCImpl(ip, port, dbName, userName, password);
	}


}
