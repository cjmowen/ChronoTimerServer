package csmSquared.server;

public class HTMLBuilder {
	private StringBuilder body;
	private String title;
	
	public HTMLBuilder() {
		body = new StringBuilder();
		title = "";
	}
	
	public HTMLBuilder(String title) {
		body = new StringBuilder();
		this.title = title;
	}
	
	public HTMLBuilder setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public HTMLBuilder addHeader(String title) {
		return addHeader(title, "");
	}
	
	public HTMLBuilder addHeader(String title, String subtitle) {
		body.append("<header><h1>" + title + "</h1><h2>" + subtitle + "</h2></header>");
		return this;
	}
	
	public HTMLBuilder addInput(String formAction, String formMethod,
			String inputName, String inputPlaceholder, String buttonValue) {
		body.append("<form action='" + formAction + "' method='" + formMethod + "'>")
			.append("<input type='text' name='" + inputName + "' placeholder='" + inputPlaceholder + "' required> ")
			.append("<input type='submit' value='" + buttonValue + "'>")
			.append("</form>");
		return this;
	}
	
	public HTMLBuilder startDiv() {
		return startDiv("");
	}
	
	public HTMLBuilder startDiv(String divClass) {
		body.append("<div class='" + divClass + "'>");
		return this;
	}
	
	public HTMLBuilder endDiv() {
		body.append("</div>");
		return this;
	}
	
	public HTMLBuilder startTable(String... tableHeaders) {
		body.append("<table><tr>");
		for(String header : tableHeaders) {
			body.append("<th>")
				.append(header)
				.append("</th>");
		}
		body.append("</tr>");
		return this;
	}
	
	public HTMLBuilder addTableRow(String... data) {
		body.append("<tr>");
		for(String td : data) {
			body.append("<td>")
				.append(td)
				.append("</td>");
		}
		body.append("</tr>");
		return this;
	}
	
	public HTMLBuilder endTable() {
		body.append("</table>");
		return this;
	}
	
	public HTMLBuilder addBreak() {
		body.append("<br/>");
		return this;
	}
	
	public HTMLBuilder addP(String text) {
		body.append("<p>")
			.append(text)
			.append("</p>");
		return this;
	}
	
	public HTMLBuilder add(String string) {
		body.append(string);
		return this;
	}
	
	public String build() {
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html><html><head>")
			.append("<link href='http://fonts.googleapis.com/css?family=Open+Sans:600' rel='stylesheet' type='text/css'>")
			.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"><meta charset=\"ISO-8859-1\">")
			.append("<title>")
			.append(title)
			.append("</title></head><body>")
			.append(body)
			.append("</body></html>");
		
		return html.toString();
	}
}
