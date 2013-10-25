package ShoppingServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class ShoppingServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8705831569797721077L;

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// esto indica que si hay sesion me la usas pero sino no me las crees.
		HttpSession session = req.getSession(false);

		// en caso de que no haya valorpara la sesion pero se haya creado
		// enviame error
		if (session == null) {
			res.sendRedirect("http://localhost:8000/error.html");
		}
		// obtengo el vector del carrito de la compra puede estar vacio o no
		Vector buylist = (Vector) session.getValue("shopping.shoppingcart");

		// que haccion has hecho --> Entregado o INSERT o validar
		String action = req.getParameter("action");
		buylist = new Vector();

		if (!action.equals("validar")) {

			if (action.equals("Entregado")) {
				CD entrega = getCD(req);
				// realizamso consulta alas bbdd para elimiarlo
				Context envContext = null;
				try {
					// realizamos una consulta la base de datos.
					envContext = new InitialContext();
					Context initContext = (Context) envContext
							.lookup("java:/comp/env");
					DataSource ds3 = (DataSource) initContext
							.lookup("jdbc/testDB");
					Connection con3 = ds3.getConnection();

					Statement stmt3 = con3.createStatement();
					String query3 = "Delete from Boxes where Code='"
							+ entrega.getCode() + "';";
					stmt3.executeUpdate(query3);

					// buscamos el resto de cajas de ese almacen
					query3 = "select * from Boxes where Warehouse="
							+ entrega.getWarehouse() + ";";
					ResultSet rs = stmt3.executeQuery(query3);

					while (rs.next()) {
						CD Paquete = new CD();
						Paquete.setCode(rs.getString("Code"));
						Paquete.setContents(rs.getString("Contents"));
						Paquete.setValue(rs.getInt("VALUE"));
						Paquete.setWarehouse(rs.getInt("Warehouse"));
						buylist.add(Paquete);
					}

					rs.close();
					stmt3.close();
					con3.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (action.equals("INSERT")) {

				CD nuevopaquete = getinsertCD(req);

				Context envContext = null;
				try {
					// realizamos una consulta la base de datos.
					envContext = new InitialContext();
					Context initContext = (Context) envContext
							.lookup("java:/comp/env");
					DataSource ds3 = (DataSource) initContext
							.lookup("jdbc/testDB");
					// DataSource ds =
					// (DataSource)envContext.lookup("java:/comp/env/jdbc/testDB");
					Connection con3 = ds3.getConnection();

					Statement stmt3 = con3.createStatement();
					String query3 = "INSERT INTO Boxes(Code,Contents,VALUE,Warehouse) VALUES('"
							+ nuevopaquete.getCode()
							+ "','"
							+ nuevopaquete.getContents()
							+ "',"
							+ nuevopaquete.getValue()
							+ ","
							+ nuevopaquete.getWarehouse() + ");";
					stmt3.executeUpdate(query3);
					
					stmt3.close();
					con3.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			session.putValue("shopping.shoppingcart", buylist);
			String url = "/EShop.jsp";
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(url);
			rd.forward(req, res);

		} else if (action.equals("validar")) {

			CD paquete = validarCD(req);

			Context envContext = null;
			try {
				// realizamos una consulta la base de datos.
				envContext = new InitialContext();
				Context initContext = (Context) envContext
						.lookup("java:/comp/env");
				DataSource ds3 = (DataSource) initContext.lookup("jdbc/testDB");
				Connection con3 = ds3.getConnection();

				Statement stmt3 = con3.createStatement();
				String query3 = "select * from Boxes where Code='"
						+ paquete.getCode() + "' and Warehouse="
						+ paquete.getWarehouse() + ";";
				ResultSet rs = stmt3.executeQuery(query3);
				int numero = 0;
				List<CD> lista = new ArrayList<CD>();
				while (rs.next()) {
					numero++;
					CD Paquete = new CD();
					Paquete.setCode(rs.getString("Code"));
					Paquete.setContents(rs.getString("Contents"));
					Paquete.setValue(rs.getInt("VALUE"));
					Paquete.setWarehouse(rs.getInt("Warehouse"));
					buylist.add(Paquete);
					lista.add(Paquete);

				}
				rs.close();
				stmt3.close();
				con3.close();
				session.putValue("lista", lista);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String url = "/Checkout.jsp";
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(url);
			rd.forward(req, res);
		}
	}

	private CD getCD(HttpServletRequest req) {
		// imagine if all this was in a scriptlet...ugly, eh?
		String myCd = req.getParameter("CD");

		StringTokenizer t = new StringTokenizer(myCd, "|");
		String Code = t.nextToken();
		String Content = t.nextToken();
		int value = (new Integer(t.nextToken().replace('$', ' ').trim()))
				.intValue();
		int warehouse = (new Integer(t.nextToken().replace('$', ' ').trim()))
				.intValue();

		CD Paquete = new CD();
		Paquete.setCode(Code);
		Paquete.setContents(Content);
		Paquete.setValue(value);
		Paquete.setWarehouse(warehouse);
		return Paquete;
	}

	private CD getinsertCD(HttpServletRequest req) {
		// imagine if all this was in a scriptlet...ugly, eh?
		String code = req.getParameter("code");
		String Content = req.getParameter("contents");
		int value = (new Integer(req.getParameter("value"))).intValue();
		int warehouse = (new Integer(req.getParameter("warehouse"))).intValue();

		CD Paquete = new CD();
		Paquete.setCode(code);
		Paquete.setContents(Content);
		Paquete.setValue(value);
		Paquete.setWarehouse(warehouse);

		return Paquete;
	}

	private CD validarCD(HttpServletRequest req) {

		String code = req.getParameter("code2");
		int warehouse = (new Integer(req.getParameter("almacen"))).intValue();

		CD Paquete = new CD();
		Paquete.setCode(code);
		Paquete.setWarehouse(warehouse);
		return Paquete;
	}
}