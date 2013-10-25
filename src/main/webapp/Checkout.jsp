<%@ page session="true" import="java.util.*, ShoppingServlet.CD"%>
<html>
<head>
<title>Busqueda de Paquete</title>
</head>
<body bgcolor="#33CCFF">
	<font face="Times New Roman,Times" size=+3> Music Without
		Borders Checkout </font>
	<hr>
<%
List<CD> lista = (List<CD>) session.getValue("lista");
	if (lista.size() > 0) {
%>

	<p>
	Se ha encontrado
	<p>
			<a href="/examen/EShop.jsp">Volver a Buscar</a>
			<%
				}
	else{
			%>
			<p>
			
			No se ha encontrado el paquete
			
		<p>
			<a href="/examen/EShop.jsp">Volver a Buscar</a>
	</center>
				<%
				}

			%>
</body>
</html>