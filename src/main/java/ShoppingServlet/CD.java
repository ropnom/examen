package ShoppingServlet;

public class CD {

	String Code;
	String Contents;
	int value;
	int Warehouse;

	public CD() {
		Code = "";
		Contents = "";		
		value= 0;
		value = 0;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getContents() {
		return Contents;
	}

	public void setContents(String contents) {
		Contents = contents;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getWarehouse() {
		return Warehouse;
	}

	public void setWarehouse(int warehouse) {
		Warehouse = warehouse;
	}

	
}