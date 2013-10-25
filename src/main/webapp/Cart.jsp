<%@ page session="true" import="java.util.*, ShoppingServlet.CD"%>
<%
	Vector buylist = (Vector) session.getValue("shopping.shoppingcart");
	if (buylist != null && (buylist.size() > 0)) {
%>
<center>
	<table border="0" cellpadding="0" width="100%" bgcolor="#FFFFFF">
		<tr>
			<td><b>CODE</b></td>
			<td><b>CONTENTS</b></td>
			<td><b>VALUE</b></td>
			<td><b>WAREHOUSE</b></td>		
			
		</tr>
		<%
			for (int index = 0; index < buylist.size(); index++) {
					CD anOrder = (CD) buylist.elementAt(index);
		%>
		<tr>
			<td><b><%=anOrder.getCode()%></b></td>
			<td><b><%=anOrder.getContents()%></b></td>
			<td><b><%=anOrder.getValue()%></b></td>
			<td><b><%=anOrder.getWarehouse()%></b></td>			
			
		</tr>
		<%
			}
		%>
		<%
	}
%>
	</table>
	<p>
	<p>
	<form name="insert" action="/examen/ShoppingServlet"
		method="POST">
		<b>Code: </b><input type="text" name="code" SIZE="4" value=1>
		<b>Contents: </b><input type="text" name="contents" SIZE="7" value=1>
		<b>Value: </b><input type="text" name="value" SIZE="3" value=1>
		<b>Warehouse: </b><input type="text" name="warehouse" SIZE="3" value=1>
		<input type="hidden" name="action" value="INSERT"> <input
			type="submit" name="validar" value="Insertar Paquete">
	</form>
	<p>
	<form name="checkoutForm" action="/examen/ShoppingServlet"
		method="POST">
		<b>Inserte Code a Buscar: </b><input type="text" name="code2" SIZE="4" value=1>
		<b>Almacen de Busqueda: </b><input type="text" name="almacen" SIZE="4" value=1>
		<input type="hidden" name="action" value="validar"> <input
			type="submit" name="Buscar" value="Buscar Paquete">
	</form>
</center>
